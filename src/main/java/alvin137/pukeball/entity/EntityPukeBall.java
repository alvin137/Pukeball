package alvin137.pukeball.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import alvin137.pukeball.ConfigManager;
import alvin137.pukeball.NBTKEYS;
import alvin137.pukeball.PukeBall;
import alvin137.pukeball.item.ItemPukeBall;
import alvin137.pukeball.item.RegisterItems;
import alvin137.pukeball.network.PukeMessage;
import alvin137.pukeball.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class EntityPukeBall extends EntityThrowable {
	private double ballBonus;
	private ItemPukeBall item = null;
	private int gettick = 0;
	private boolean isTouched = false;
	private RayTraceResult r;

	public EntityPukeBall(World worldIn) {
		super(worldIn);
	}

	public EntityPukeBall(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityPukeBall(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static void registerFixesSnowball(DataFixer fixer) {
		EntityThrowable.registerFixesThrowable(fixer, "Snowball");
	}

	public EntityPukeBall(World worldIn, EntityLivingBase throwerIn, double ballBonus, ItemPukeBall item) {
		super(worldIn, throwerIn);
		this.ballBonus = ballBonus;
		this.item = item;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (isTouched) {gettick++;
		PukeBall.logger.info(gettick);;
		switch (gettick) {	
		case 1:isCatchSuccess((EntityLivingBase) this.r.entityHit, 1);break;
		case 30:isCatchSuccess((EntityLivingBase) this.r.entityHit, 2);break;
		case 60:isCatchSuccess((EntityLivingBase) this.r.entityHit, 3);break;
		case 90:isCatchSuccess((EntityLivingBase) this.r.entityHit, 4);break;
		case 91:this.setDead();break;
		default:break;
		}
		}

	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(RayTraceResult result) {
		
		if (this.world.isRemote) return;
		else if (this.getEntityData().hasKey(NBTKEYS.KEY_ENTITY)) {
			Entity e = EntityList.createEntityFromNBT(this.getEntityData().getCompoundTag(NBTKEYS.KEY_ENTITY),
					this.world);
			e.resetEntityId();
			e.setPosition(posX, posY, posZ);
			e.setUniqueId(UUID.randomUUID());
			this.world.spawnEntity(e);
			PukeBall.logger.info(this.getEntityData().getCompoundTag(NBTKEYS.KEY_ENTITY).getString("id"));
		} else if (result.entityHit != null && result.entityHit.isEntityAlive() && classVerify(result.entityHit)) {
			this.r = result;
			isTouched = true;
		}
		// 이 구문은 한 블럭 내려가야 모든 상황에서 실행될 수 있겠죠. 안내려가면 몹한테 안 맞는 이상 안죽음.
		//this.setDead();
	}

	public void makePukeBall() {
		ItemStack ball = RegisterItems.getPukeball(item);
		ball.setTagCompound(new NBTTagCompound());
		ball.getTagCompound().setTag(NBTKEYS.KEY_ENTITY, this.r.entityHit.serializeNBT());
		ball.getTagCompound().setString(NBTKEYS.KEY_ENTITY_NAME, this.r.entityHit.getName());
		this.r.entityHit.entityDropItem(ball, 1);
		this.r.entityHit.setDead();
		this.setDead();
	}

	/**
	 * 타입을 몰라서 Entity로 지었음. 확장 가능. 보스검사는 해야져 님아. 보스가 잡히면 어썸한 모드가 아니라 X발같은 모드가
	 * 됩니다. 종이 한장 차이임.
	 */
	public boolean classVerify(Entity entity) {
		return entity instanceof EntityLiving && entity.isNonBoss();
	}
	/*
	 * public boolean isCatchSuccess(EntityLivingBase e) { double statusBonus =
	 * 1, mhp = e.getMaxHealth(), hp = e.getHealth(), a = ((3 * mhp - 2 * hp) *
	 * ConfigManager.getConfig(EntityList.getEntityString(e))) / (3 * mhp)
	 * statusBonus, b = 65536 / Math.pow((255 / a), 0.1875); for (int i = 0; i <
	 * 4; i++) { if (b < e.getRNG().nextInt(65536)) {
	 * PukeBall.logger.info(String.format(
	 * "Mob %s catch failed in %dth shake. ballBonus = %f, statusBonus = %f,  mhp = %f, hp = %f, a = %f, b = %f, Catch Rate = %f"
	 * , EntityList.getEntityString(e), i + 1, ballBonus, statusBonus, mhp, hp,
	 * a, b, ConfigManager.getConfig(EntityList.getEntityString(e)))); return
	 * false; }
	 * 
	 * } PukeBall.logger.info(String.format(
	 * "Mob %s catch Successed. ballBonus = %f, statusBonus = %f,  mhp = %f, hp = %f, a = %f, b = %f, Catch Rate = %f"
	 * , EntityList.getEntityString(e), ballBonus, statusBonus, mhp, hp, a, b,
	 * ConfigManager.getConfig(EntityList.getEntityString(e)))); return true;
	 * 
	 * }
	 */

	public boolean isCatchSuccess(EntityLivingBase e, int i) {
		double statusBonus = 1, mhp = e.getMaxHealth(), hp = e.getHealth(),
				a = ((3 * mhp - 2 * hp) * ConfigManager.getConfig(EntityList.getEntityString(e))) / (3 * mhp)
						* statusBonus * ballBonus,
				b = 65536 / Math.pow((255 / a), 0.1875);
		if (b < e.getRNG().nextInt(65536)) {
			PukeBall.logger.info(String.format(
					"Mob %s catch failed in %dth shake. ballBonus = %f, statusBonus = %f,  mhp = %f, hp = %f, a = %f, b = %f, Catch Rate = %f",
					EntityList.getEntityString(e), i + 1, ballBonus, statusBonus, mhp, hp, a, b,
					ConfigManager.getConfig(EntityList.getEntityString(e))));
			return false;
		}
		PukeBall.logger.info(String.format(
				"Mob %s catch Successed. ballBonus = %f, statusBonus = %f,  mhp = %f, hp = %f, a = %f, b = %f, Catch Rate = %f",
				EntityList.getEntityString(e), ballBonus, statusBonus, mhp, hp, a, b,
				ConfigManager.getConfig(EntityList.getEntityString(e))));
		makePukeBall();
		return true;

	}

	public void callFriends(EntityLivingBase e) {
		this.getThrower().setRotationYawHead(180f);
		PukeBall.snw.sendToAllAround(new PukeMessage(1), new TargetPoint(e.dimension, e.posX, e.posY, e.posZ, 10));
		EntityLivingBase entitylivingbase = this.getThrower();
		int i = MathHelper.floor(e.posX);
		int j = MathHelper.floor(e.posY);
		int k = MathHelper.floor(e.posZ);
		EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityByName(EntityList.getEntityString(e),
				this.world);

		for (int l = 0; l < 50; ++l) {
			int i1 = i + MathHelper.getRandomIntegerInRange(this.rand, 1, 10)
					* MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
			int j1 = j + MathHelper.getRandomIntegerInRange(this.rand, 1, 10)
					* MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
			int k1 = k + MathHelper.getRandomIntegerInRange(this.rand, 1, 10)
					* MathHelper.getRandomIntegerInRange(this.rand, -1, 1);

			if (this.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isSideSolid(this.world,
					new BlockPos(i1, j1 - 1, k1), net.minecraft.util.EnumFacing.UP)) {
				entity.setPosition((double) i1, (double) j1, (double) k1);

				if (!this.world.isAnyPlayerWithinRangeAt((double) i1, (double) j1, (double) k1, 7.0D)
						&& this.world.checkNoEntityCollision(entity.getEntityBoundingBox(), entity)
						&& this.world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty()
						&& !this.world.containsAnyLiquid(entity.getEntityBoundingBox())) {
					e.world.spawnEntity(entity);
					PukeBall.logger.info("entity successfuly spawned");
					if (entitylivingbase != null)
						((EntityLiving) entity).setAttackTarget(entitylivingbase);
					((EntityLiving) entity).onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entity)),
							(IEntityLivingData) null);
					break;
				}
			}
		}

	}
}
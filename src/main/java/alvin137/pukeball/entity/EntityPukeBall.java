package alvin137.pukeball.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import alvin137.pukeball.ConfigManager;
import alvin137.pukeball.NBTKeys;
import alvin137.pukeball.PukeBall;
import alvin137.pukeball.item.ItemPukeBall;
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

public class EntityPukeBall extends EntityThrowable {
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

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	/**
	 * �����ϰ� ���ĺ����ϴ�. ���� if (result.entityHit != null)...�� �����ϴ� ���� �������̵忡���� �۵��ؾ�
	 * �Ѵٸ�, ������ ���� ó���Ͻø� �˴ϴ�.
	 * 
	 * protected void onImpact(RayTraceResult result) {
	 * if(this.worldObj.isRemote) return; else if
	 * (this.getEntityData().hasKey(NBTKEYS.KEY_ENTITY)) { // ... ... ... }else
	 * if (result.entityHit != null){ // ... } this.setDead(); }
	 */
	@Override
	protected void onImpact(RayTraceResult result) {
		if (this.worldObj.isRemote)
			return;
		else if (this.getEntityData().hasKey(NBTKeys.KEY_ENTITY)) {
			Entity e = EntityList.createEntityFromNBT(this.getEntityData().getCompoundTag(NBTKeys.KEY_ENTITY),
					this.worldObj);
			e.resetEntityId();
			e.setPosition(posX, posY, posZ);
			e.setUniqueId(UUID.randomUUID());
			this.worldObj.spawnEntityInWorld(e);
			PukeBall.logger.info(this.getEntityData().getCompoundTag(NBTKeys.KEY_ENTITY).getString("id"));
		} else if (result.entityHit != null && result.entityHit.isEntityAlive() && classVerify(result.entityHit)) {
			EntityLivingBase e = (EntityLivingBase) result.entityHit;
			if (isCatchSuccess(e)) {
				ItemStack ball = new ItemStack(CommonProxy.ball);
				ball.setTagCompound(new NBTTagCompound());
				ball.getTagCompound().setTag(NBTKeys.KEY_ENTITY, e.serializeNBT());
				ball.getTagCompound().setString(NBTKeys.KEY_ENTITY_NAME, e.getName());
				result.entityHit.entityDropItem(ball, 1);
				result.entityHit.setDead();
				return;
			} 
			callFriends(e);
		}
		// �� ������ �� �� �������� ��� ��Ȳ���� ����� �� �ְ���. �ȳ������� ������ �� �´� �̻� ������.
		this.setDead();
	}

	/**
	 * Ÿ���� ���� Entity�� ������. Ȯ�� ����. �����˻�� �ؾ��� �Ծ�. ������ ������ ����� ��尡 �ƴ϶� X�߰��� ��尡
	 * �˴ϴ�. ���� ���� ������.
	 */
	public boolean classVerify(Entity entity) {
		return entity instanceof EntityLiving && entity.isNonBoss();
	}

	public boolean isCatchSuccess(EntityLivingBase e) {
		double ballBonus = 1, statusBonus = 1, mhp = e.getMaxHealth(), hp = e.getHealth(),
				// a = ((3 * mhp - 2 * hp) *
				// ConfigManager.getConfig(EntityList.getEntityString(e)) *
				// ballBonus)
				a = ((3 * mhp - 2 * hp) * ConfigManager.getConfig(EntityList.getEntityString(e))) / (3 * mhp)
						* statusBonus,
				b = 65536 / Math.pow((255 / a), 0.1875);
		for (int i = 0; i < 4; i++) {
			if (b < e.getRNG().nextInt(65536)) {
				PukeBall.logger.info(String.format(
						"Mob %s catch failed in %dth shake. ballBonus = %f, statusBonus = %f,  mhp = %f, hp = %f, a = %f, b = %f, Catch Rate = %f",
						EntityList.getEntityString(e), i + 1, ballBonus, statusBonus, mhp, hp, a, b,
						ConfigManager.getConfig(EntityList.getEntityString(e))));
				return false;
			}
		}
		PukeBall.logger.info(String.format(
				"Mob %s catch Successed. ballBonus = %f, statusBonus = %f,  mhp = %f, hp = %f, a = %f, b = %f, Catch Rate = %f",
				EntityList.getEntityString(e), ballBonus, statusBonus, mhp, hp, a, b,
				ConfigManager.getConfig(EntityList.getEntityString(e))));
		return true;

	}

	public void callFriends(EntityLivingBase e) {
		this.getThrower().setRotationYawHead(180f);
			EntityLivingBase entitylivingbase = this.getThrower();
			int i = MathHelper.floor_double(e.posX);
			int j = MathHelper.floor_double(e.posY);
			int k = MathHelper.floor_double(e.posZ); 
				EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityByName(EntityList.getEntityString(e), this.worldObj);

				for (int l = 0; l < 50; ++l) {
					int i1 = i + MathHelper.getRandomIntegerInRange(this.rand, 1, 10)
							* MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
					int j1 = j + MathHelper.getRandomIntegerInRange(this.rand, 1, 10)
							* MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
					int k1 = k + MathHelper.getRandomIntegerInRange(this.rand, 1, 10)
							* MathHelper.getRandomIntegerInRange(this.rand, -1, 1);

					if (this.worldObj.getBlockState(new BlockPos(i1, j1 - 1, k1)).isSideSolid(this.worldObj,
							new BlockPos(i1, j1 - 1, k1), net.minecraft.util.EnumFacing.UP)) {
						entity.setPosition((double) i1, (double) j1, (double) k1);

						if (!this.worldObj.isAnyPlayerWithinRangeAt((double) i1, (double) j1, (double) k1, 7.0D)
								&& this.worldObj.checkNoEntityCollision(entity.getEntityBoundingBox(),
										entity)
								&& this.worldObj.getCollisionBoxes(entity, entity.getEntityBoundingBox())
										.isEmpty()
								&& !this.worldObj.containsAnyLiquid(entity.getEntityBoundingBox())) {
							e.worldObj.spawnEntityInWorld(entity);
							PukeBall.logger.info("entity successfuly spawned");
							if (entitylivingbase != null)
								((EntityLiving) entity).setAttackTarget(entitylivingbase);
							((EntityLiving) entity).onInitialSpawn(
									this.worldObj.getDifficultyForLocation(new BlockPos(entity)),
									(IEntityLivingData) null);
							break;
						}
					}
				}
				
	}
}
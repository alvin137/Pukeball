package alvin137.pukeball.entity;

import java.util.List;
import java.util.UUID;

import alvin137.pukeball.NBTKeys;
import alvin137.pukeball.item.ItemPukeBall;
import alvin137.pukeball.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
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
	 * 간단하게 고쳐봤읍니다. 만약 if (result.entityHit != null)...로 시작하는 곳이 서버사이드에서만 작동해야
	 * 한다면, 다음과 같이 처리하시면 됩니다.
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
			if (!this.worldObj.isRemote) {
				Entity e = EntityList.createEntityFromNBT(this.getEntityData().getCompoundTag(NBTKeys.KEY_ENTITY),
						this.worldObj);
				e.resetEntityId();
				e.setPosition(posX, posY, posZ);
				e.setUniqueId(UUID.randomUUID());
				this.worldObj.spawnEntityInWorld(e);
				// 제가 원했던 디버깅 구문. 엔티티의 Id를 출력합니다. serializeNBT 보고 출력시켜주라 한거였는데
				// ㅂㄷㅂㄷ ㅂㄷㅂㄷㅂㄷㅂㄷㅂㄷㅂㄷㅂㄷ. @see Entity#serializeNBT()
				System.out.printf("mobspawn %s",
						this.getEntityData().getCompoundTag(NBTKeys.KEY_ENTITY).getString("id"));
			}
		} else if (result.entityHit != null && result.entityHit.isEntityAlive() && classVerify(result.entityHit)) {
			ItemStack ball = new ItemStack(CommonProxy.ball);
			ball.setTagCompound(new NBTTagCompound());
			ball.getTagCompound().setTag(NBTKeys.KEY_ENTITY, result.entityHit.serializeNBT());
			ball.getTagCompound().setString(NBTKeys.KEY_ENTITY_NAME, result.entityHit.getName());
			result.entityHit.entityDropItem(ball, 1);
			result.entityHit.setDead();
		}
		// 이 구문은 한 블럭 내려가야 모든 상황에서 실행될 수 있겠죠. 안내려가면 몹한테 안 맞는 이상 안죽음.
		if (!this.worldObj.isRemote)
			this.setDead();
	}

	/**
	 * 타입을 몰라서 Entity로 지었음. 확장 가능. 보스검사는 해야져 님아. 보스가 잡히면 어썸한 모드가 아니라 X발같은 모드가
	 * 됩니다. 종이 한장 차이임.
	 */
	public boolean classVerify(Entity entity) {
		return entity instanceof EntityLiving && entity.isNonBoss();
	}
}
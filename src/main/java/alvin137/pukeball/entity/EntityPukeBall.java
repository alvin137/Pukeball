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
			if (!this.worldObj.isRemote) {
				Entity e = EntityList.createEntityFromNBT(this.getEntityData().getCompoundTag(NBTKeys.KEY_ENTITY),
						this.worldObj);
				e.resetEntityId();
				e.setPosition(posX, posY, posZ);
				e.setUniqueId(UUID.randomUUID());
				this.worldObj.spawnEntityInWorld(e);
				// ���� ���ߴ� ����� ����. ��ƼƼ�� Id�� ����մϴ�. serializeNBT ���� ��½����ֶ� �Ѱſ��µ�
				// �������� ����������������������������. @see Entity#serializeNBT()
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
		// �� ������ �� �� �������� ��� ��Ȳ���� ����� �� �ְ���. �ȳ������� ������ �� �´� �̻� ������.
		if (!this.worldObj.isRemote)
			this.setDead();
	}

	/**
	 * Ÿ���� ���� Entity�� ������. Ȯ�� ����. �����˻�� �ؾ��� �Ծ�. ������ ������ ����� ��尡 �ƴ϶� X�߰��� ��尡
	 * �˴ϴ�. ���� ���� ������.
	 */
	public boolean classVerify(Entity entity) {
		return entity instanceof EntityLiving && entity.isNonBoss();
	}
}
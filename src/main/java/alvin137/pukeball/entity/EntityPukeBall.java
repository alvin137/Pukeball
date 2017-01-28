package alvin137.pukeball.entity;

import java.util.List;

import alvin137.pukeball.NBTKEYS;
import alvin137.pukeball.item.ItemPukeBall;
import alvin137.pukeball.proxy.CommonProxy;
import net.minecraft.entity.EntityList;
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
	protected void onImpact(RayTraceResult result) {
		if (this.getEntityData().hasKey(NBTKEYS.KEY_ENTITY) || this.getEntityData().hasKey(NBTKEYS.KEY_ENTITY_NAME)) {
			EntityList.createEntityFromNBT(this.getEntityData().getCompoundTag(NBTKEYS.KEY_ENTITY), this.worldObj);
			EntityList.createEntityByIDFromName(this.getEntityData().getString(NBTKEYS.KEY_ENTITY_NAME),
					this.getEntityWorld());
			EntityList.createEntityByName(this.getEntityData().getString(NBTKEYS.KEY_ENTITY_NAME), this.worldObj);
			EntityList.createEntityByName(this.serializeNBT().getString(NBTKEYS.KEY_ENTITY_NAME), this.worldObj);
			EntityList.createEntityByName(this.serializeNBT().getString(NBTKEYS.KEY_ENTITY_NAME), this.getEntityWorld());
			// EntityList.createEntityFromNBT(this.getEntityData().getCompoundTag(NBTKEYS.KEY_ENTITY),
			// worldIn)
			System.out.println("mobspawn");
			System.out.println(this.getEntityData().getString(NBTKEYS.KEY_ENTITY_NAME));
		} else if (result.entityHit != null) {
			if (!(result.entityHit instanceof EntityPlayer) && result.entityHit.isDead) {
				ItemStack ball = new ItemStack(CommonProxy.ball);
				ball.setTagCompound(new NBTTagCompound());
				ball.getTagCompound().setTag(NBTKEYS.KEY_ENTITY, result.entityHit.serializeNBT());
				ball.getTagCompound().setString(NBTKEYS.KEY_ENTITY_NAME, result.entityHit.getName());
				result.entityHit.entityDropItem(ball, 1);
				result.entityHit.setDead();
			}

			if (!this.worldObj.isRemote) {
				this.setDead();
			}
		}
	}
}
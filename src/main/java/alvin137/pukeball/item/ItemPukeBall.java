package alvin137.pukeball.item;

import java.util.List;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;

import alvin137.pukeball.NBTKEYS;
import alvin137.pukeball.entity.EntityPukeBall;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPukeBall extends Item {
	public static String name = "pukeball";
	public String info;

	public ItemPukeBall() {
		setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.register(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (!playerIn.capabilities.isCreativeMode) {
			--itemStackIn.stackSize;
		}

		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ,
				SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
				0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote) {
			EntityPukeBall entitypukeball = new EntityPukeBall(worldIn, playerIn);
			entitypukeball.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F,
					1.0F);
			worldIn.spawnEntityInWorld(entitypukeball);
			NBTTagCompound nbt = itemStackIn.getTagCompound();
			if (nbt != null) {
				entitypukeball.getEntityData().setTag(NBTKEYS.KEY_ENTITY, nbt.getTag(NBTKEYS.KEY_ENTITY));
				entitypukeball.getEntityData().setTag(NBTKEYS.KEY_ENTITY_NAME, nbt.getTag(NBTKEYS.KEY_ENTITY_NAME));
			}
		}

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		System.out.println("asdfff");
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey(NBTKEYS.KEY_ENTITY_NAME, 8))
			list.add(TextFormatting.YELLOW + itemstack.getTagCompound().getString(NBTKEYS.KEY_ENTITY_NAME));
	}
}

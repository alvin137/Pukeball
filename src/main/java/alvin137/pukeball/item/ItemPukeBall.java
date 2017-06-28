package alvin137.pukeball.item;

import java.util.List;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;

import alvin137.pukeball.NBTKEYS;
import alvin137.pukeball.PSoundEvents;
import alvin137.pukeball.PukeBall;
import alvin137.pukeball.entity.EntityPukeBall;
import alvin137.pukeball.network.PukeMessage;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
	public static String name;
	private double ballBonus;
	public ItemPukeBall(String name, double ballBonus) {
		setUnlocalizedName(name);
		setRegistryName(PukeBall.MODID, name);
		setCreativeTab(PukeBall.tabPukeBall);
		this.ballBonus = ballBonus;	
		GameRegistry.register(this);
		RegisterItems.list.add(this);
	}

	@Override
	 public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	 {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		//PukeBall.snw.sendToAll(new PukeMessage(1));
		if (!playerIn.capabilities.isCreativeMode) {
			int size = itemstack.getCount();
			itemstack.setCount(size--);
		}

		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, PSoundEvents.BALL_THROWING,
				SoundCategory.NEUTRAL, 0.5F, 1F);
		if (!worldIn.isRemote) {
			EntityPukeBall entitypukeball = new EntityPukeBall(worldIn, playerIn, ballBonus, this);
			entitypukeball.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F,
					1.0F);
			worldIn.spawnEntity(entitypukeball);
			NBTTagCompound nbt = itemstack.getTagCompound();
			if (nbt != null) {
				entitypukeball.getEntityData().setTag(NBTKEYS.KEY_ENTITY, nbt.getTag(NBTKEYS.KEY_ENTITY));
			}
		}

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey(NBTKEYS.KEY_ENTITY_NAME, 8))
			list.add(TextFormatting.YELLOW + itemstack.getTagCompound().getString(NBTKEYS.KEY_ENTITY_NAME));
	}
	
	public double getBallType() {
		return this.ballBonus;
	}
}

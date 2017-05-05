package alvin137.pukeball.network;

import alvin137.pukeball.PukeBall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PukeMessageHandler implements IMessageHandler<PukeMessage,IMessage> {

	@Override
	public IMessage onMessage(PukeMessage message, MessageContext ctx) {
		// This is the player the packet was sent to the server from
	    //EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
		EntityPlayerMP serverPlayer= ctx.getServerHandler().playerEntity;
	    // The value that was sent
		//Minecraft.getMinecraft().addScheduledTask(new Runnable);
		//serverPlayer.applyOrientationToEntity(entityToUpdate);
		float yaw = serverPlayer.prevRotationYaw + (serverPlayer.rotationYaw - serverPlayer.prevRotationYaw) + 180.0F;
		GlStateManager.rotate(yaw, 0.0F, 90.0F, 0.0F);
		PukeBall.logger.info("checked");
		//Minecraft.getMinecraft().setRenderViewEntity(serverPlayer);
		//Minecraft.getMinecraft().getRenderViewEntity().rotationPitch = 90;
	    int amount = message.toSend;
	  //  serverPlayer.inventory.addItemStackToInventory(new ItemStack(Items.DIAMOND, amount));
	    PukeBall.logger.info(amount);
	    // No response packet
	    return null;
	}

}

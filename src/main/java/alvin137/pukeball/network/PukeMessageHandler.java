package alvin137.pukeball.network;

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
	    EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
	    // The value that was sent
	    int amount = message.toSend;
	    serverPlayer.inventory.addItemStackToInventory(new ItemStack(Items.DIAMOND, amount));
	    // No response packet
	    return null;
	}

}

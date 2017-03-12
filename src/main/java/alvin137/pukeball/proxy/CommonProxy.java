package alvin137.pukeball.proxy;

import alvin137.pukeball.item.ItemPukeBall;
import alvin137.pukeball.item.RegisterItems;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preinit(FMLPreInitializationEvent e) {
		RegisterItems.registerItem();
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postinit(FMLPostInitializationEvent e) {
	}
}

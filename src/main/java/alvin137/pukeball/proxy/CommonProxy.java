package alvin137.pukeball.proxy;

import alvin137.pukeball.item.ItemPukeBall;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public static ItemPukeBall ball;

	public void preinit(FMLPreInitializationEvent e) {
		ball = new ItemPukeBall();
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postinit(FMLPostInitializationEvent e) {
	}
}

package alvin137.pukeball.proxy;

import alvin137.pukeball.item.ItemPukeBall;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public static ItemPukeBall ball;
	public void init(FMLPreInitializationEvent e) {
		ball = new ItemPukeBall();
	}
}

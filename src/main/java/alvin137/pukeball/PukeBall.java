package alvin137.pukeball;

import alvin137.pukeball.entity.EntityPukeBall;
import alvin137.pukeball.item.ItemPukeBall;
import alvin137.pukeball.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = PukeBall.MODID, version = PukeBall.VERSION)
public class PukeBall {
	public static final String MODID = "pukeball";
	public static final String VERSION = "0.1";

	@Instance
	public static PukeBall pukeball;
	
	@SidedProxy(clientSide="alvin137.pukeball.proxy.ClientProxy", serverSide="alvin137.pukeball.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
	proxy.init(e);
	}
	

	@EventHandler
	public static void init(FMLInitializationEvent e) {
		EntityRegistry.registerModEntity(EntityPukeBall.class, "entitypukeball", 0, pukeball, 64, 1, true);
	}

	@EventHandler
	public static void postinit(FMLPostInitializationEvent e) {

	}
}

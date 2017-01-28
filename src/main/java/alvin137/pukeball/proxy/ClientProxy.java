package alvin137.pukeball.proxy;

import alvin137.pukeball.PukeBall;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLPreInitializationEvent e) {
		super.init(e);
		OBJLoader.INSTANCE.addDomain(PukeBall.MODID);
		ball.initModel();
		System.out.println("asdf");
	}
}

package alvin137.pukeball.proxy;

import alvin137.pukeball.PukeBall;
import alvin137.pukeball.entity.EntityPukeBall;
import alvin137.pukeball.entity.RenderPukeBall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preinit(FMLPreInitializationEvent e) {
		super.preinit(e);
		OBJLoader.INSTANCE.addDomain(PukeBall.MODID);
		ball.initModel();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityPukeBall.class,
				new RenderPukeBall<Entity>(Minecraft.getMinecraft().getRenderManager(), ball,
						Minecraft.getMinecraft().getRenderItem()));

	}

	@Override
	public void postinit(FMLPostInitializationEvent e) {

	}
}

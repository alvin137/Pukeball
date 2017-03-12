package alvin137.pukeball.proxy;

import java.util.List;

import alvin137.pukeball.PukeBall;
import alvin137.pukeball.entity.EntityPukeBall;
import alvin137.pukeball.entity.RenderPukeBall;
import alvin137.pukeball.item.ItemPukeBall;
import alvin137.pukeball.item.RegisterItems;
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
		RegisterItems.initModel();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		List<ItemPukeBall> list = RegisterItems.list;
		for(int i = 0; list.size() > i; i++)
		Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityPukeBall.class,
				new RenderPukeBall<Entity>(Minecraft.getMinecraft().getRenderManager(), list.get(i),
						Minecraft.getMinecraft().getRenderItem()));

	}

	@Override
	public void postinit(FMLPostInitializationEvent e) {

	}
}

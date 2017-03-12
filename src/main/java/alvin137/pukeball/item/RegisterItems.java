package alvin137.pukeball.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class RegisterItems {
	public static ItemPukeBall monsterball;
	public static ItemPukeBall superball;
	public static ItemPukeBall hyperball;
	public static ItemPukeBall masterball;
	public static List<ItemPukeBall> list = new ArrayList<ItemPukeBall>();
	public static void registerItem() {
		monsterball = new ItemPukeBall("monsterball", 1);
		superball = new ItemPukeBall("superball", 1.1);
		hyperball = new ItemPukeBall("hyperball", 1.3);
		masterball = new ItemPukeBall("masterball", 255);
	}
	
	public static void initModel() {
		monsterball.initModel();
		superball.initModel();
		hyperball.initModel();
		masterball.initModel();
	}
	
	public static ItemStack getPukeball(ItemPukeBall item) {
		double a = item.getBallType();
		if(a == 1) return new ItemStack(monsterball);
		else if(a==1.1) return new ItemStack(superball);
		else if (a==1.3) return new ItemStack(hyperball);
		else if(a==255) return new ItemStack(masterball);
		return null;	
	}
}

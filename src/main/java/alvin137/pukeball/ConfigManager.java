package alvin137.pukeball;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.config.Configuration;

public class ConfigManager {

	public static final String CATEGORY_MOBS = "Mobs";

	public static Map<String, Integer> map = new HashMap<String, Integer>();

	// public static double CatchRate;
	public static void setConfig(Configuration config) {
		List<String> list = EntityList.getEntityNameList();
		config.load();
		for (int i = 0; i < list.size(); i++) {
			// map.put(list.get(i), config.getFloat(list.get(i), CATEGORY_MOBS,
			// 30, 0, 255, list.get(i)+"Catch rate"));
			map.put(list.get(i), config.get(CATEGORY_MOBS, list.get(i), 30).getInt());
		}
		// CatchRate = config.get(CATEGORY_MOBS, "Catchrate", 50, "All catch
		// rate, divided by each entity's max health", 0, 255).getDouble();
		config.save();
	}

	public static double getConfig(String name) {
		return map.get(name);
		// return (CatchRate / e.getMaxHealth());
	}
}

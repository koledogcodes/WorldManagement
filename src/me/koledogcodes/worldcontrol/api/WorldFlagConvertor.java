package me.koledogcodes.worldcontrol.api;

import me.koledogcodes.worldcontrol.WorldControl;

public class WorldFlagConvertor {
	
	public WorldFlagConvertor(WorldControl i) {
		
	}

	public static String translateFlag(WorldFlag flag){
		return flag.name().toLowerCase().replaceAll("_", "-");
	}
}

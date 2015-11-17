package me.koledogcodes.worldcontrol.handler;

import me.koledogcodes.worldcontrol.WorldControl;

public class WorldControlInventoryHandler {
	
	private WorldControl plugin;
	public WorldControlInventoryHandler(WorldControl i) {
		plugin = i;
	}

	public WorldControlInventoryHandler() {
		
	}
	
	public WorldControl getWorldControl(){
		return plugin;
	}
	

}

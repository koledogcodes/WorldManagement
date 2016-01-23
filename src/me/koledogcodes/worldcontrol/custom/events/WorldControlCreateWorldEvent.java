package me.koledogcodes.worldcontrol.custom.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WorldControlCreateWorldEvent extends Event {

	String world;
	
	public WorldControlCreateWorldEvent(String world) {
		this.world = world;
	}
	
	public String getCreatedWorldName(){
		return world;
	}
	
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}

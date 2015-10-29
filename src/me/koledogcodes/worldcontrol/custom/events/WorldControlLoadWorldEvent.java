package me.koledogcodes.worldcontrol.custom.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WorldControlLoadWorldEvent extends Event {

	Player player;
	String world;
	
	public WorldControlLoadWorldEvent(Player player, String world) {
		this.player = player;
		this.world = world;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getLoadedWorldName(){
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

package me.koledogcodes.worldcontrol.custom.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WorldControlPreUnloadWorldEvent extends Event {

	Player player;
	String world;
	List<Player> players;
	
	public WorldControlPreUnloadWorldEvent(Player player, String world, List<Player> list) {
		this.player = player;
		this.world = world;
		this.players = list;
	}
	
	public Player getUnloader(){
		return player;
	}
	
	public String getUnloadedWorldName(){
		return world;
	}
	
	public List<Player> getPlayers(){
		return players;
	}
	
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}

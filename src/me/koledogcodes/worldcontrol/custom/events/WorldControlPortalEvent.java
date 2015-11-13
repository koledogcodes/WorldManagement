package me.koledogcodes.worldcontrol.custom.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WorldControlPortalEvent extends Event implements Cancellable {

	Player player;
	Location from;
	Location to;
	String portal;
	boolean cancelled;
	
	public WorldControlPortalEvent(Player player, Location from, Location to, String portal) {
		this.player = player;
		this.from = from;
		this.to = to;
		this.portal = portal;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Location getFrom(){
		return from;
	}
	
	public Location getTo(){
		return to;
	}
	
	public String getPortalName(){
		return portal;
	}
	
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}
}

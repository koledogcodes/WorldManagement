package me.koledogcodes.worldcontrol.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldPortalLocationFile;
import me.koledogcodes.worldcontrol.custom.events.WorldControlPortalEvent;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;

public class BukkitWorldControlPortalEvent implements Listener {
	
	public BukkitWorldControlPortalEvent(WorldControl i) {
		
	}

	private WorldControlHandler WorldControl = new WorldControlHandler();
	private HashMap<Player, Location> location_1 = new HashMap<Player, Location>();
	private HashMap<Player, Location> location_2 = new HashMap<Player, Location>();
	
	//TODO Creation of Portal [PORTAL]
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR){ return; }
		if (player.getItemInHand().getType() != Material.STONE_HOE){ return; }
		if (e.getClickedBlock().getType() == Material.AIR || e.getClickedBlock() == null){ return; }
		if (WorldControlHandler.portalCreationInfo.containsKey(player) == false){ return; }
		
		//Location 1
		if (e.getAction() == Action.LEFT_CLICK_BLOCK){
		e.setCancelled(true);
		location_1.put(player, e.getClickedBlock().getLocation());
		ChatUtili.sendTranslatedMessage(player, "&aLocation 1 set at '" + WorldControl.parseLocationToString(e.getClickedBlock().getLocation()) + "'.");
		}
		
		//Location 2
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
		e.setCancelled(true);
		location_2.put(player, e.getClickedBlock().getLocation());
		ChatUtili.sendTranslatedMessage(player, "&aLocation 2 set at '" + WorldControl.parseLocationToString(e.getClickedBlock().getLocation()) + "'.");
		
		if (location_1.containsKey(player)){
		WorldControl.createPortal(player, player.getWorld().getName(), WorldControlHandler.portalCreationInfo.get(player).split("\\,")[0], WorldControlHandler.portalCreationInfo.get(player).split("\\,")[1], location_1.get(player), location_2.get(player), Material.getMaterial(WorldControlHandler.portalCreationInfo.get(player).split("\\,")[2].toUpperCase()));	
		ChatUtili.sendTranslatedMessage(player, "&aPortal '" + WorldControlHandler.portalCreationInfo.get(player).split("\\,")[0] + "' has been set!");
		WorldControlHandler.portalCreationInfo.remove(player);
		}
		else {
			ChatUtili.sendTranslatedMessage(player, "&cPlease set the first location.");
		}
		}
	}
	
	//TODO Player Move Into Portal Block [PORTAL]
	@EventHandler
	public void onPlayerWalk(PlayerMoveEvent e){
		Player player = e.getPlayer();
		
		if (WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation())) == null){ return; }
		
		if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()){ return; }
		
		if (player.hasPermission("worldcontrol.portal.*") == false){
			if (player.hasPermission("worldcontrol.portal." + WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation())))){
				if (new WorldControlPortalEvent(player, player.getLocation(), WorldControl.getPortalDestinationLocation(WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation()))), WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation()))).isCancelled()){
					return;
				}
				Bukkit.getServer().getPluginManager().callEvent(new WorldControlPortalEvent(player, player.getLocation(), WorldControl.getPortalDestinationLocation(WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation()))), WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation()))));
				WorldControlHandler.portalTeleportInstance.put(player, true);
				player.teleport(WorldControl.getPortalDestinationLocation(WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation()))));
				ChatUtili.sendTranslatedMessage(player, ConfigFile.getCustomConfig().getString("portal-teleport-message").replaceAll("<player>", player.getName()));
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cYou do not have permission to use this portal.");
			}
		}
		else {
			player.teleport(WorldControl.getPortalDestinationLocation(WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(player.getLocation()))));
			ChatUtili.sendTranslatedMessage(player, ConfigFile.getCustomConfig().getString("portal-teleport-message").replaceAll("<player>", player.getName()));
		}
	}
	
	//TODO Liquid Flow Out [PORTAL]
	@EventHandler
	public void onLiquidFlow(BlockFromToEvent e){
		if (e.getBlock().getType() == Material.GOLD_BLOCK && (e.getToBlock().getType() == Material.PORTAL || e.getToBlock().getType() == Material.ENDER_PORTAL)){ e.setCancelled(true); }
		if (WorldPortalLocationFile.getCustomConfig().getString(WorldControl.parseLocationToString(e.getBlock().getLocation())) == null){ return; }
		e.setCancelled(true);
	}
}

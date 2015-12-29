package me.koledogcodes.worldcontrol.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.WorldControlSignType;
import me.koledogcodes.worldcontrol.custom.events.WorldControlLoadWorldEvent;
import me.koledogcodes.worldcontrol.custom.events.WorldControlUnloadWorldEvent;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;

public class BukkitWorldControlSignEvent implements Listener {
	
	
	public BukkitWorldControlSignEvent(WorldControl i) {
		
	}

	private WorldControlHandler WorldControl = new WorldControlHandler();
	
	//TODO Create WorldControl [SIGN]
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerSignChange(SignChangeEvent e){
		Player player = e.getPlayer();
		if (player.hasPermission("worldcontrol.admin.*") == false){ return; }
		//WorldControl Sign
		if (e.getLine(0).equalsIgnoreCase("[WorldControl]")){
			if (WorldControl.worldExists(e.getLine(1))){
				if (e.getLine(2).equalsIgnoreCase("Teleport")){
					WorldControl.setSignLinesEvent(e, "&8[&aWorldControl&8]", "&f&l" + e.getLine(1), "&aTeleport", "&b-&eClick&b-");
					WorldControl.toggleWorldControlSign(e.getBlock().getLocation());
					WorldControl.setWorldControlSignType(e.getBlock().getLocation(), WorldControlSignType.TELEPORT);
				}
				else if (e.getLine(2).equalsIgnoreCase("Status")){
					WorldControl.setSignLinesEvent(e, "&8[&aWorldControl&8]", "&f&l" + e.getLine(1), "&a(Loaded)", "");
					WorldControl.toggleWorldControlSign(e.getBlock().getLocation());
					WorldControl.setWorldControlSignType(e.getBlock().getLocation(), WorldControlSignType.STATUS);
				}
				else if (e.getLine(2).equalsIgnoreCase("players")){
					WorldControl.setSignLinesEvent(e, "&8[&aWorldControl&8]", "&f&l" + e.getLine(1), "&a" + Bukkit.getWorld(e.getLine(1)).getPlayers().size() + "/" + WorldControl.getWorldSettingValue(e.getLine(1), "player-limit"), "");
					WorldControl.toggleWorldControlSign(e.getBlock().getLocation());
					WorldControl.setWorldControlSignType(e.getBlock().getLocation(), WorldControlSignType.PLAYER_LIST);	
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cInvalid Sign Type!");
					WorldControl.clearSignEvent(e);
				}
			}
			else {
				if (e.getLine(2).equalsIgnoreCase("Status")){
				WorldControl.setSignLinesEvent(e, "&8[&aWorldControl&8]", "&f&l" + e.getLine(1), "&c(Not Loaded)", "");
				WorldControl.toggleWorldControlSign(e.getBlock().getLocation());
				WorldControl.setWorldControlSignType(e.getBlock().getLocation(), WorldControlSignType.STATUS);
				return;
				}
				
				ChatUtili.sendTranslatedMessage(player, "&cCannot use unloaded world '" + e.getLine(1) + "'.");
				WorldControl.clearSignEvent(e);
			}
		}
	}
	
	//TODO Use WorldControl [SIGN]
	@EventHandler
	public void onPlayerClickWCSign(PlayerInteractEvent e){
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK){ return; }
		if (e.getClickedBlock().getType() == Material.AIR || e.getClickedBlock() == null){ return; }
		if (e.getClickedBlock().getState() instanceof Sign){
			Sign s = (Sign) e.getClickedBlock().getState();
			
			//Teleport
			if (ChatColor.stripColor(s.getLine(0)).equalsIgnoreCase("[WorldControl]") && ChatColor.stripColor(s.getLine(2)).equalsIgnoreCase("Teleport")){
				WorldControl.tpToWorldSetLocation(e.getPlayer(), ChatColor.stripColor(s.getLine(1)));
			}
		}
	}
	
	//TODO Player Join World
	@EventHandler
	public void worldChangeEvent(PlayerChangedWorldEvent e){
		List<String> signs = WorldControl.getAllWorldControlSignLoc();
		for (int i = 0; i < signs.size(); i++){
			try {
				if (WorldControl.parseStringToLocation(signs.get(i)).getBlock().getState() instanceof Sign){
					Sign s = (Sign) WorldControl.parseStringToLocation(signs.get(i)).getBlock().getState();
					if (WorldControl.getWorldControlSignType(WorldControl.parseStringToLocation(signs.get(i))).toString().equalsIgnoreCase("PLAYER_LIST")){
						s.setLine(2, WorldControl.colorTranslate("&a" + Bukkit.getWorld(ChatColor.stripColor(s.getLine(1))).getPlayers().size() + "/" + WorldControl.getWorldSettingValue(ChatColor.stripColor(s.getLine(1)), "player-limit")));
						s.update();
					}
				}
			}
			catch (Exception exc){
				WorldControl.logConsole("Failed to set worldcontrol sign at (" + signs.get(i) + ")");
			}
		}
	}
	
	//TODO WorldControl Unload World
	@EventHandler
	public void wcUnloadWorld(WorldControlUnloadWorldEvent e){
		List<String> signs = WorldControl.getAllWorldControlSignLoc();
		
			for (int i = 0; i < signs.size(); i++){
				try {
					if (WorldControl.parseStringToLocation(signs.get(i)).getBlock().getState() instanceof Sign){
						Sign s = (Sign) WorldControl.parseStringToLocation(signs.get(i)).getBlock().getState();
					if (ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase(e.getUnloadedWorldName()) && WorldControl.getWorldControlSignType(WorldControl.parseStringToLocation(signs.get(i))).toString().equalsIgnoreCase("STATUS")){
						s.setLine(2, WorldControl.colorTranslate("&c(Not Loaded)"));
						s.update();
					}
				}
			}
			catch (Exception exc){
				WorldControl.logConsole("Failed to set worldcontrol sign at (" + signs.get(i) + ")");
			}
		}
	}
	
	//TODO WorldControl Load World
	@EventHandler
	public void wcLoadWorld(WorldControlLoadWorldEvent e){
		List<String> signs = WorldControl.getAllWorldControlSignLoc();
		for (int i = 0; i < signs.size(); i++){
			try {
				if (WorldControl.parseStringToLocation(signs.get(i)).getBlock().getState() instanceof Sign){
					Sign s = (Sign) WorldControl.parseStringToLocation(signs.get(i)).getBlock().getState();
					if (ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase(e.getLoadedWorldName()) && WorldControl.getWorldControlSignType(WorldControl.parseStringToLocation(signs.get(i))).toString().equalsIgnoreCase("STATUS")){
						s.setLine(2, WorldControl.colorTranslate("&a(Loaded)"));
						s.update();
					}
				}
			}
			catch (Exception exc){
				WorldControl.logConsole("Failed to set worldcontrol sign at (" + signs.get(i) + ")");
			}
	    }
	}
	
	@EventHandler
	public void onPlayerBlockBreak(BlockBreakEvent e){
		if (WorldControl.getAllWorldControlSignLoc().contains(WorldControl.parseLocationToString(e.getBlock().getLocation()))){
			List<String> signs = WorldControl.getAllWorldControlSignLoc();
			signs.remove(WorldControl.parseLocationToString(e.getBlock().getLocation()));
			WorldControl.setWorldControlSignLocations(signs);
			ChatUtili.sendTranslatedMessage(e.getPlayer(), "&aWorldControl sign deleted.");
		}
	}
	
}

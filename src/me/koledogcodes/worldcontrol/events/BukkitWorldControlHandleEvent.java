package me.koledogcodes.worldcontrol.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.WorldInfo;
import me.koledogcodes.worldcontrol.custom.events.WorldControlPreUnloadWorldEvent;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;

public class BukkitWorldControlHandleEvent implements Listener {
	
	public BukkitWorldControlHandleEvent(WorldControl i) {
		
	}

	private WorldControlHandler WorldControl = new WorldControlHandler();

	@EventHandler
	public void onWorldUnload(WorldControlPreUnloadWorldEvent e){
		
		if (WorldControlHandler.fallbackTpOverride.contains(e.getUnloadedWorldName())){
			WorldControlHandler.fallbackTpOverride.remove(e.getUnloadedWorldName());
			return;
		}
		
		WorldControl.logConsole("World '" + e.getUnloadedWorldName() + "' is being unloaded.");
		
		World world = Bukkit.getWorld(e.getUnloadedWorldName());
		
		for (Player player: e.getPlayers()){
			WorldInfo worldInfo = new WorldInfo(world);
			WorldControl.tpToWorldSetLocation(player, worldInfo.getFallbackWorld());
			WorldControl.logConsole("World '" + e.getUnloadedWorldName() + "' players are being tped to fallback world '" + worldInfo.getFallbackWorld() + "'.");
			if (player != null){
				ChatUtili.sendSimpleTranslatedMessage(player, "&7[&o" + player.getName() + ": &cWorld &4" + worldInfo.getWorldName() + " &cis unloading...");
				ChatUtili.sendSimpleTranslatedMessage(player, "&7[&o" + player.getName() + ": &cTeleporting you to fallback world.");
			}
			else {
				ChatUtili.sendSimpleTranslatedMessage(player, "&7[&oCONSOLE: &cWorld &4" + worldInfo.getWorldName() + " &cis unloading...");
				ChatUtili.sendSimpleTranslatedMessage(player, "&7[&oCONSOLE: &cTeleporting you to fallback world.");
			}
		}
	}

	@EventHandler
	public void onDeveloperJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase("_KoleNinja_")){
			ChatUtili.sendSimpleTranslatedMessage(player, "&f&l[&a&lWorldControl&f&l] &7This server uses WorldControlManager!");
		}
	}
}

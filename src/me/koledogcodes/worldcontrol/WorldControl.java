package me.koledogcodes.worldcontrol;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import me.koledogcodes.worldcontrol.commands.worldControlCommand;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldSignFile;
import me.koledogcodes.worldcontrol.configs.WorldSpawnFile;
import me.koledogcodes.worldcontrol.configs.WorldWhitelistFile;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlEvent;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlSignEvent;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutClickChat;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutHoverChat;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateType;

public class WorldControl extends JavaPlugin {

	private WorldControlHandler handler;
	
	@SuppressWarnings("unused")
	public void onEnable(){
		try {
			Updater updater = null;
			if (getConfig().getBoolean("auto-update")){
				updater = new Updater(this, 95788, getFile(), UpdateType.NO_VERSION_CHECK, false);
			}
			else {
				updater = new Updater(this, 95788, getFile(), UpdateType.NO_DOWNLOAD, true);
			}

			Metrics metrics = new Metrics(this);
			if (getConfig().getBoolean("opt-out")){
				metrics.start();
			}
			
			File file = new File(getDataFolder() + "/Worlds");
				file.mkdirs();
			file = new File(getDataFolder() + "/Whitelist");
				file.mkdirs();
			file = new File(getDataFolder() + "/Spawns");
				file.mkdirs();
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
		new WorldControlHandler(this);
		new ChatUtili();
		new PacketHandler();
		new PacketOutClickChat();
		new PacketOutHoverChat();
		new ConfigFile (this);
		new WorldConfigFile (this);
		new WorldWhitelistFile (this);
		new WorldSpawnFile (this);
		new WorldSignFile (this);
		
		saveDefaultConfig();
		reloadConfig();
		WorldConfigFile.reloadCustomConfig();
		WorldWhitelistFile.reloadCustomConfig();
		WorldSpawnFile.reloadCustomConfig();
		WorldSignFile.reloadCustomConfig();
		
		Bukkit.getPluginManager().registerEvents(new BukkitWorldControlSignEvent (this), this);
		Bukkit.getPluginManager().registerEvents(new BukkitWorldControlEvent (this), this);
		
		getCommand("worldcontrol").setExecutor(new worldControlCommand (this));
		
		handler = new WorldControlHandler(this);
		
		//Loading worlds
		for (int i = 0; i < getConfig().getStringList("worlds-to-load-on-startup").size(); i++){
			handler.loadWorld(null, getConfig().getStringList("worlds-to-load-on-startup").get(i));
		}
		
		//Regenerate Configuration
		handler.regenerateConfigForWorlds();
	}
	
	public void onDisable(){
		
	}
	
	public void reloadWorldControlPlugin(){
		Bukkit.getServer().getPluginManager().disablePlugin(this);
		Bukkit.getServer().getPluginManager().enablePlugin(this);
	}
}

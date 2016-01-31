package me.koledogcodes.worldcontrol;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import me.koledogcodes.worldcontrol.api.WorldFlagConvertor;
import me.koledogcodes.worldcontrol.commands.worldControlCommand;
import me.koledogcodes.worldcontrol.commands.worldControlGeneratorCommand;
import me.koledogcodes.worldcontrol.commands.worldControlImportCommand;
import me.koledogcodes.worldcontrol.commands.worldControlTabCompletionCommand;
import me.koledogcodes.worldcontrol.configs.BlockDataFile;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.MessageFile;
import me.koledogcodes.worldcontrol.configs.WorldConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldDataFile;
import me.koledogcodes.worldcontrol.configs.WorldPortalFile;
import me.koledogcodes.worldcontrol.configs.WorldPortalLocationFile;
import me.koledogcodes.worldcontrol.configs.WorldSignFile;
import me.koledogcodes.worldcontrol.configs.WorldSpawnFile;
import me.koledogcodes.worldcontrol.configs.WorldWhitelistFile;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlEvent;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlHandleEvent;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlLogEvent;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlPortalEvent;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlSignEvent;
import me.koledogcodes.worldcontrol.events.WorldControlEventCaller;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.timer.WorldControlTimer;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutClickChat;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutHoverChat;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateType;

public class WorldControl extends JavaPlugin {

	private WorldControlHandler handler;
	public static List<Timer> activeTimers = new ArrayList<Timer>();
	public static List<TimerTask> activeTasks = new ArrayList<TimerTask>();
	
	@SuppressWarnings("unused")
	public void onEnable(){
		try {
			Updater updater = null;
			if (getConfig().getBoolean("auto-update")){
				updater = new Updater(this, 95788, getFile(), UpdateType.DEFAULT, false);
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
			file = new File(getDataFolder() + "/Userdata");
				file.mkdirs();
			file = new File(getDataFolder() + "/Data");
				file.mkdirs();
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
		new WorldControlHandler(this);
		new ConfigFile (this);
		new WorldConfigFile (this);
		new WorldWhitelistFile (this);
		new WorldSpawnFile (this);
		new WorldSignFile (this);
		new WorldPortalFile (this);
		new WorldPortalLocationFile (this);
		new BlockDataFile (this);
		new MessageFile(this);
		
		handler = new WorldControlHandler(this);
		handler.generateConfiguration();
		handler.generateMessages();
		
		new ChatUtili();
		new PacketHandler();
		new PacketOutClickChat();
		new PacketOutHoverChat();
		new WorldFlagConvertor(this);
		new WorldControlEventCaller(this);
		new WorldDataFile(this);
		
		ConfigFile.reloadCustomConfig();
		WorldConfigFile.reloadCustomConfig();
		WorldWhitelistFile.reloadCustomConfig();
		WorldSpawnFile.reloadCustomConfig();
		WorldSignFile.reloadCustomConfig();
		WorldPortalFile.reloadCustomConfig();
		WorldPortalLocationFile.reloadCustomConfig();
		BlockDataFile.reloadCustomConfig();
		WorldDataFile.reloadCustomConfig();
		MessageFile.reloadCustomConfig();
		
		WorldControlHandler.setInstance(this);
		
		Bukkit.getPluginManager().registerEvents(new BukkitWorldControlHandleEvent (this), this);
		Bukkit.getPluginManager().registerEvents(new BukkitWorldControlSignEvent (this), this);
		Bukkit.getPluginManager().registerEvents(new BukkitWorldControlPortalEvent (this), this);
		Bukkit.getPluginManager().registerEvents(new BukkitWorldControlEvent (this), this);
		Bukkit.getPluginManager().registerEvents(new BukkitWorldControlLogEvent (this), this);
		
		getCommand("worldcontrol").setExecutor(new worldControlCommand (this));
		getCommand("worldcontrol").setTabCompleter(new worldControlTabCompletionCommand(this));
		getCommand("wc-import").setExecutor(new worldControlImportCommand (this));
		getCommand("wc-gen").setExecutor(new worldControlGeneratorCommand (this));
		
		//Loading worlds
		for (int i = 0; i < getConfig().getStringList("worlds-to-load-on-startup").size(); i++){
			handler.loadWorld(null, getConfig().getStringList("worlds-to-load-on-startup").get(i));
		}
		
		//Regenerate Configuration
		handler.logConsole("------------------------------------");
		for (String world: handler.getAllWorlds()){
			handler.logConsole("Regenerating configuration for world '" + world + "'.");
			handler.generateWorldConfiguration(world);
		}
		handler.logConsole("------------------------------------");
		
		//Regenerate World Data
		for (String world: handler.getAllLoadedWorlds()){
			handler.logConsole("Regenerating world data for world '" + world + "'.");
			handler.generateWorldData(Bukkit.getWorld(world));
		}
		handler.logConsole("------------------------------------");
		
		handler.startAutosave();
		WorldControlEventCaller.startLoadUnloadEventCaller();
		WorldControlEventCaller.startWorldCreateEventCaller();
		
		ChatUtili.messagePrefix = getConfig().getString("prefix");
		worldControlCommand.prefix = ChatUtili.messagePrefix;
		worldControlGeneratorCommand.prefix = ChatUtili.messagePrefix;
		worldControlImportCommand.prefix = ChatUtili.messagePrefix;
		
		handler.logConsole("WorldManagement has been enabled");
	}
	
	public void onDisable(){
		handler.logConsole("------------------------------------");
		getLogger().info("Saving block logging data.");
		BlockDataFile.saveCustomConfig();
		BlockDataFile.reloadCustomConfig();
		getLogger().info("Block logging data has been saved!");
		handler.logConsole("------------------------------------");
		getLogger().info("Cancelling all java timers.");
		WorldControlTimer timer = new WorldControlTimer();
		timer.cancelAllTimers(false);
		getLogger().info("All java timers have been cancelled!");
		handler.logConsole("------------------------------------");
		getLogger().info("Cancelling all threads");
		Bukkit.getScheduler().cancelAllTasks();
		getLogger().info("Cancelled all threads!");
		handler.logConsole("------------------------------------");
	}
	
	public void reloadWorldControlPlugin(){
		Bukkit.getServer().getPluginManager().disablePlugin(this);
		Bukkit.getServer().getPluginManager().enablePlugin(this);
	}
}

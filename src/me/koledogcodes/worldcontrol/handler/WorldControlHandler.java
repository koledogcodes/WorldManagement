package me.koledogcodes.worldcontrol.handler;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.WorldControlSignType;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.PlayerDataFile;
import me.koledogcodes.worldcontrol.configs.WorldConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldPortalFile;
import me.koledogcodes.worldcontrol.configs.WorldPortalLocationFile;
import me.koledogcodes.worldcontrol.configs.WorldSignFile;
import me.koledogcodes.worldcontrol.configs.WorldSpawnFile;
import me.koledogcodes.worldcontrol.configs.WorldWhitelistFile;
import me.koledogcodes.worldcontrol.custom.events.WorldControlLoadWorldEvent;
import me.koledogcodes.worldcontrol.custom.events.WorldControlUnloadWorldEvent;

public class WorldControlHandler {
	
	private WorldControl plugin;
	public WorldControlHandler(WorldControl i) {
		plugin = i;
	}

	public WorldControlHandler() {
		
	}
	
	public WorldControl getWorldControl(){
		return plugin;
	}
	
	private HashMap<Player, Integer> loop = new HashMap<Player, Integer>();
	private HashMap<Player, List<String>> flagValues = new HashMap<Player, List<String>>();
	private HashMap<Player, Integer> flagLoop = new HashMap<Player, Integer>();
	private HashMap<Player, StringBuilder> flagStringBuilder = new HashMap<Player, StringBuilder>();
	private HashMap<Player, List<ItemStack>> worldInventory = new HashMap<Player, List<ItemStack>>();
	private HashMap<Player, Integer> maxX = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> maxY = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> maxZ = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> minX = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> minY = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> minZ = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> x = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> y = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> z = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> occurnaces = new HashMap<Player, Integer>();
	private HashMap<Player, Block> block = new HashMap<Player, Block>();
	public HashMap<Player, String> world = new HashMap<Player, String>();
	public static HashMap<Player, Boolean> tpSuccecs = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> portalTeleportInstance = new HashMap<Player, Boolean>();
	public static HashMap<Player, String> portalCreationInfo = new HashMap<Player, String>();
	private HashMap<String, List<String>> worldWhitelist = new HashMap<String, List<String>>();
	private HashMap<Player, PlayerDataFile> playerDataFile = new HashMap<Player, PlayerDataFile>();
	
	public boolean deleteWorld(String world, File path) {
	      if(path.exists()) {
	          File files[] = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(world, files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
	
	public void loadWorld(Player player, String world){
		if (worldFolderExists(world)){
			if (worldExists(world) == false){
				Bukkit.createWorld(new WorldCreator(world));
				logConsole("World '" + world + "' has been loaded.");
				if (player != null){
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been loaded!");
					Bukkit.getServer().getPluginManager().callEvent(new WorldControlLoadWorldEvent(player, world));
				}
			}
			else {
				logConsole("World '" + world + "' has already been reloaded.");
				if (player != null){
				ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has already been loaded!");	
				}
			}
		}
		else {
			logConsole("Cannot load world '" + world + "' that doesn't exist.");
			if (player != null){
			ChatUtili.sendTranslatedMessage(player, "&cCannot load world '" + world + "' that doesn't exist.");
			}
		}
		
	}
	
	public void createWorld(Player player, String world, WorldType world_type, Environment envoirment, boolean generateStructures, int seed){
		if (worldExists(world)){
			logConsole("Cannot create world '" + world + "' that already exists!");
			ChatUtili.sendTranslatedMessage(player, "&cCannot create world '" + world + "' that already exists!");
		}
		else {
			WorldCreator creator = new WorldCreator(world);
			creator.type(world_type);
			creator.environment(envoirment);
			creator.generateStructures(generateStructures);
			creator.seed(seed);
			Bukkit.createWorld(creator);
			logConsole("World '" + world + "' has been created!");
			ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been succesfully created.");
		}
		
	}
	
	public void createWorld(Player player, String world, WorldType world_type){
		if (worldExists(world)){
			logConsole("Cannot create world '" + world + "' that already exists!");
			ChatUtili.sendTranslatedMessage(player, "&cCannot create world '" + world + "' that already exists!");
		}
		else {
			WorldCreator creator = new WorldCreator(world);
			creator.type(world_type);
			Bukkit.createWorld(creator);
			logConsole("World '" + world + "' has been created!");
			ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been succesfully created.");
		}
		
	}
	
	public void createWorld(Player player, String world, WorldType world_type, Environment envoirment){
		if (worldExists(world)){
			logConsole("Cannot create world '" + world + "' that already exists!");
			ChatUtili.sendTranslatedMessage(player, "&cCannot create world '" + world + "' that already exists!");
		}
		else {
			WorldCreator creator = new WorldCreator(world);
			creator.type(world_type);
			creator.environment(envoirment);
			Bukkit.createWorld(creator);
			logConsole("World '" + world + "' has been created!");
			ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been succesfully created.");
		}
		
	}
	
	public void createWorld(Player player, String world, WorldType world_type, Environment envoirment, boolean generateStructures){
		if (worldExists(world)){
			logConsole("Cannot create world '" + world + "' that already exists!");
			ChatUtili.sendTranslatedMessage(player, "&cCannot create world '" + world + "' that already exists!");
		}
		else {
			WorldCreator creator = new WorldCreator(world);
			creator.type(world_type);
			creator.environment(envoirment);
			creator.generateStructures(generateStructures);
			Bukkit.createWorld(creator);
			logConsole("World '" + world + "' has been created!");
			ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been succesfully created.");
		}
		
	}
	
	public void createWorld(Player player, String world){
		if (worldExists(world)){
			logConsole("Cannot create world '" + world + "' that already exists!");
			ChatUtili.sendTranslatedMessage(player, "&cCannot create world '" + world + "' that already exists!");
		}
		else {
			WorldCreator creator = new WorldCreator(world);
			Bukkit.createWorld(creator);
			logConsole("World '" + world + "' has been created!");
			ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been succesfully created.");
		}
		
	}
	
	public void tpToWorldSetLocation(Player player, String world){
		if (worldFolderExists(world)){
			if (worldExists(world)){
				tpSuccecs.put(player, true);
				if (getWorldTeleportLocation(world) == null){
					player.teleport(Bukkit.getWorld(world).getSpawnLocation());
				}
				else {
					player.teleport(getWorldTeleportLocation(world));
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&4Error: &cTping to world '" + world + "'.");
				ChatUtili.sendTranslatedMessage(player, "&cPlease load world '" + world + "' to tp.");
			}
		}
		else {
			ChatUtili.sendTranslatedMessage(player, "&cCannot tp to world '" + world + "' that doesn't exist!");
		}
	}
	
	public boolean worldExists(String world){
		if (Bukkit.getWorld(world) != null){
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean worldFolderExists(String world){
		File file = new File(world);
		if (file.isDirectory() && blacklistFolders().contains(world) == false){
			file = null;
			return true;
		}
		else {
			file = null;
			return false;
		}
	}
	
	public List<String> blacklistFolders(){
		List<String> blacklist = new ArrayList<String>();
		blacklist.clear();
		blacklist.add("plugins");
		blacklist.add("jars");
		blacklist.add("mods");
		blacklist.add("logs");
		blacklist.add("crash-reports");
		
		//Blacklisting extra folders
		for (int i = 0; i < ConfigFile.getCustomConfig().getStringList("blacklist-worlds").size(); i++){
		blacklist.add(ConfigFile.getCustomConfig().getStringList("blacklist-worlds").get(i));
		}
		return blacklist;
	}
	
	public String getAllWorldsStatus(){
		String temp = "";
		File getRoot = new File("plugins");
		File file = new File(getRoot.getAbsoluteFile().getParentFile().getPath().toString());
				
		final List<String> directories = new ArrayList<String>();
		file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				File folders = new File(dir, name);
				if (folders.isDirectory() && blacklistFolders().contains(folders.getName()) == false){
					directories.add(folders.getName());
				}
				return false;
			}
		});
		
		
		for (int i = 0; i < directories.size(); i++){
			if (i == (directories.size() - 1)){
				if (worldExists(directories.get(i))){
					temp += "&a" + directories.get(i) + " &b(loaded)";
				}
				else {
					temp += "&a" + directories.get(i) + " &b(not loaded)";
				}	
			}
			else {
				if (worldExists(directories.get(i))){
					temp += "&a" + directories.get(i) + " &b(loaded)&c, &a";
				}
				else {
					temp += "&a" + directories.get(i) + " &b(not loaded)&c, &a";
				}
			}
		}
		
		return temp;
	}
	
	public List<String> getAllWorlds(){
		File getRoot = new File("plugins");
		File file = new File(getRoot.getAbsoluteFile().getParentFile().getPath().toString());
				
		final List<String> directories = new ArrayList<String>();
		file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				File folders = new File(dir, name);
				if (folders.isDirectory() && blacklistFolders().contains(folders.getName()) == false){
					directories.add(folders.getName());
				}
				return false;
			}
		});
		
		
		return directories;
	}
	
	public void generateWorldConfiguration(Player player){
		if (worldFolderExists(world.get(player))){
			if (worldExists(world.get(player))){
				//Generate
					setWorldConfigOption(player, "pvp", true);	
					setWorldConfigOption(player, "build", true);
					setWorldConfigOption(player, "certain-blocks-place-allow", new String[]{"SEEDS"});
					setWorldConfigOption(player, "certain-blocks-break-allow", new String[]{"GRASS", "LONG_GRASS"});
					setWorldConfigOption(player, "weather-locked", false);
					setWorldConfigOption(player, "mob-spawn", true);
					setWorldConfigOption(player, "certain-mob-spawn-allow", new String[]{"CHICKEN","COW","SHEEP"});
					setWorldConfigOption(player, "player-limit", 1);	
					setWorldConfigOption(player, "commands-allowed", true);	
					setWorldConfigOption(player, "certain-commands-use-allow", new String[]{"spawn", "msg", "r"});
					setWorldConfigOption(player, "fallback-world", "world");
					setWorldConfigOption(player, "players-invincible", true);
					setWorldConfigOption(player, "mobs-invincible", true);
					setWorldConfigOption(player, "mobs-drop-loot", true);
					setWorldConfigOption(player, "mobs-drop-exp", true);
					setWorldConfigOption(player, "players-drop-loot", true);
					setWorldConfigOption(player, "players-drop-exp", true);
					setWorldConfigOption(player, "chat", true);
					setWorldConfigOption(player, "mob-limit", 100);
					setWorldConfigOption(player, "player-interact", true);
					setWorldConfigOption(player, "explosion", true);
					setWorldConfigOption(player, "nether-portal-can-create", true);
					setWorldConfigOption(player, "nether-portal-teleport", true);
					setWorldConfigOption(player, "title-join", false);
					setWorldConfigOption(player, "title-join-message-main", "&f&lWelcome <player>!");
					setWorldConfigOption(player, "title-join-message-main-display-time", 5);
					setWorldConfigOption(player, "title-join-message-sub", "<world-online>/<world-online-max>");
					setWorldConfigOption(player, "title-join-message-sub-display-time", 5);
					setWorldConfigOption(player, "default-gamemode", "survival");
					WorldConfigFile.saveCustomConfig();
					WorldConfigFile.reloadCustomConfig();
				ChatUtili.sendTranslatedMessage(player, "&aGenerated config for world '" + world.get(player) + "'.");
				world.remove(player);
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cPlease load world '" + world.get(player) + "' to generate a config.");
			}
		}
		else {
			ChatUtili.sendTranslatedMessage(player, "&cCannot generate a config for world '" + world.get(player) + "' that doesn't exist!");
		}		
	}
	
	public void generateWorldConfiguration(String world){
		if (worldFolderExists(world)){
			if (worldExists(world)){
				//Generate
					setWorldConfigOption(world, "pvp", true);	
					setWorldConfigOption(world, "build", true);
					setWorldConfigOption(world, "certain-blocks-place-allow", new String[]{"SEEDS"});
					setWorldConfigOption(world, "certain-blocks-break-allow", new String[]{"GRASS", "LONG_GRASS"});
					setWorldConfigOption(world, "weather-locked", false);
					setWorldConfigOption(world, "mob-spawn", true);
					setWorldConfigOption(world, "certain-mob-spawn-allow", new String[]{"CHICKEN","COW","SHEEP"});
					setWorldConfigOption(world, "player-limit", 1);	
					setWorldConfigOption(world, "commands-allowed", true);	
					setWorldConfigOption(world, "certain-commands-use-allow", new String[]{"spawn", "msg", "r"});
					setWorldConfigOption(world, "fallback-world", "world");
					setWorldConfigOption(world, "players-invincible", true);
					setWorldConfigOption(world, "mobs-invincible", true);
					setWorldConfigOption(world, "mobs-drop-loot", true);
					setWorldConfigOption(world, "mobs-drop-exp", true);
					setWorldConfigOption(world, "players-drop-loot", true);
					setWorldConfigOption(world, "players-drop-exp", true);
					setWorldConfigOption(world, "chat", true);
					setWorldConfigOption(world, "mob-limit", 100);
					setWorldConfigOption(world, "player-interact", true);
					setWorldConfigOption(world, "explosion", true);
					setWorldConfigOption(world, "nether-portal-can-create", true);
					setWorldConfigOption(world, "nether-portal-teleport", true);
					setWorldConfigOption(world, "title-join", false);
					setWorldConfigOption(world, "title-join-message-main", "&f&lWelcome <player>!");
					setWorldConfigOption(world, "title-join-message-main-display-time", 5);
					setWorldConfigOption(world, "title-join-message-sub", "<world-online>/<world-online-max>");
					setWorldConfigOption(world, "title-join-message-sub-display-time", 5);
					setWorldConfigOption(world, "default-gamemode", "survival");
					WorldConfigFile.saveCustomConfig();
					WorldConfigFile.reloadCustomConfig();
			}
		}
	}
	
	public void generateConfiguration(){
		setConfigOption("worlds-to-load-on-startup", new ArrayList<String>());
		setConfigOption("blacklist-worlds", new ArrayList<String>());
		setConfigOption("portal-teleport-message", "You have been teleported <player>!");
		setConfigOption("auto-update", true);
		setConfigOption("opt-out", true);
		setConfigOption("inventory-per-world", false);
	}
	
	public void setWorldConfigOption(Player player, String setting, Object value){
		if (WorldConfigFile.getCustomConfig().getString(world.get(player)  + "." +  setting) == null){
			WorldConfigFile.getCustomConfig().set(world.get(player)  + "." +  setting, value);
			WorldConfigFile.saveCustomConfig();
		}
		else {
			return;
		}
		
		setting = null;
		value = null;
	}
	
	public void setWorldConfigOption(String world, String worldSetting, Object value){
		if (WorldConfigFile.getCustomConfig().getString(world  + "." +  worldSetting) == null){
			WorldConfigFile.getCustomConfig().set(world  + "." +  worldSetting, value);
			WorldConfigFile.saveCustomConfig();
		}
		else {
			return;
		}
		
		worldSetting = null;
		value = null;
	}
	
	public void overrideWorldConfigOption(String world, String worldSetting, Object value){
		if (WorldConfigFile.getCustomConfig().getString(world  + "." +  worldSetting) != null){
			WorldConfigFile.getCustomConfig().set(world  + "." +  worldSetting, value);
			WorldConfigFile.saveCustomConfig();
		}
		else {
			return;
		}
		
		worldSetting = null;
		value = null;
	}
	
	public void setConfigOption(String configSetting, Object value){
		if (ConfigFile.getCustomConfig().getString(configSetting) == null){
			ConfigFile.getCustomConfig().set(configSetting, value);
			ConfigFile.saveCustomConfig();
		}
		else {
			return;
		}
		
		configSetting = null;
		value = null;
	}
	
	public Object getWorldSettingValue(String world, String setting){
		return WorldConfigFile.getCustomConfig().get(world  + "." + setting);
	}
	
	public boolean worldContainsSettings(String world){
		if (WorldConfigFile.getCustomConfig().getString(world + "." + "pvp") != null){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void unloadWorld(Player player, String world, boolean save){
		if (worldFolderExists(world)){
			if (worldExists(world) == false){
				logConsole("Cannot not unloaded a non loaded World '" + world + "'.");
				if (player != null){
					ChatUtili.sendTranslatedMessage(player, "&cCannot not unloaded a non loaded world '" + world + "'.");
				}
			}
			else {
				Bukkit.unloadWorld(world, save);
				Bukkit.getServer().getPluginManager().callEvent(new WorldControlUnloadWorldEvent(player, world));
				logConsole("World '" + world + "' has unloaded.");
				if (player != null){
				ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been unloaded!");	
				}
			}
		}
		else {
			logConsole("Cannot unload world '" + world + "' that doesn't exist.");
			if (player != null){
			ChatUtili.sendTranslatedMessage(player, "&cCannot unload world '" + world + "' that doesn't exist.");
			}
		}
		
	}
	
	public void copyWorld(Player player, String oldWorld, String newWorld){
		if (worldFolderExists(oldWorld)){
			if (worldExists(oldWorld)){
				WorldCreator creator = new WorldCreator(newWorld);
				creator.copy(Bukkit.getWorld(oldWorld));
				creator.createWorld();
				logConsole("[WorldControl] World '" + oldWorld + "' has been copied to new world '" + newWorld + "'.");
				ChatUtili.sendTranslatedMessage(player, "&aWorld '" + oldWorld + "' has been copied to new world '" + newWorld + "'.");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cCannot copy unloaded world '" + oldWorld + "'.");
			}
		}
	}
	
	public void saveWorld(Player player, String world){
		if (worldFolderExists(world)){
			if (worldExists(world)){
				unloadWorld(null, world, true);
				loadWorld(null, world);
				logConsole("[WorldControl] World '" + world + "' has saved!.");
				ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been saved!");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cCannot save unloaded world '" + world + "'.");
			}
		}
		else {
			ChatUtili.sendTranslatedMessage(player, "&cCannot save world '" + world + "' that doesn't exist.");
		}
	}
	
	public void toggleWorldWhitelist(Player player, String worldToWhitelist){
		if (worldFolderExists(worldToWhitelist)){
			if (worldExists(worldToWhitelist)){
				if (worldWhitelistIsEnabled(worldToWhitelist)){
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' whitelist has been disabled.");	
					setWorldWhitelist(worldToWhitelist, false);
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' whitelist has been enabled.");		
					setWorldWhitelist(worldToWhitelist, true);
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cCannot whitelist unloaded world '" + world + "'.");
			}
		}
		else {
			ChatUtili.sendTranslatedMessage(player, "&cCannot whitelist world '" + world + "' that doesn't exist.");
		}
	}
	
	public boolean worldWhitelistIsEnabled(String world){
		if (WorldWhitelistFile.getCustomConfig().getBoolean(world + ".enabled")){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setWorldWhitelist(String world, boolean enable){
		WorldWhitelistFile.getCustomConfig().set(world + ".enabled", enable);
		WorldWhitelistFile.saveCustomConfig();
		logConsole("World '" + world + "' whitelist has been set to: " + enable + ".");
	}
	
	public void tooglePlayerWorldWhitelist(Player player, String world, String playerToWhitelist){
		if (worldFolderExists(world)){
			if (worldExists(world)){
				if (worldWhiteListHasPlayer(playerToWhitelist, world)){	
					worldWhitelist.put(world, getWorldWhiteList(world));
					worldWhitelist.get(world).remove(playerToWhitelist);
					WorldWhitelistFile.getCustomConfig().set(world + ".players", worldWhitelist.get(world));
					WorldWhitelistFile.saveCustomConfig();
					logConsole("Player '" + playerToWhitelist + "' has been removed from world '" + world + "' whitelist.");
					ChatUtili.sendTranslatedMessage(player, "&aPlayer '" + playerToWhitelist + "' has been removed from world '" + world + "' whitelist.");
				}
				else {	
					worldWhitelist.put(world, getWorldWhiteList(world));
					worldWhitelist.get(world).add(playerToWhitelist);
					WorldWhitelistFile.getCustomConfig().set(world + ".players", worldWhitelist.get(world));
					WorldWhitelistFile.saveCustomConfig();
					ChatUtili.sendTranslatedMessage(player, "&aPlayer '" + playerToWhitelist + "' has been added to world '" + world + "' whitelist.");
					logConsole("Player '" + playerToWhitelist + "' has been added to world '" + world + "' whitelist.");
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cCannot use whitelist on an unloaded world '" + world + "'.");
			}
		}
		else {
			ChatUtili.sendTranslatedMessage(player, "&cCannot use whitelist on world '" + world + "' that doesn't exist.");
		}
	}
	
	public boolean worldWhiteListHasPlayer(String player, String world){
		if (getWorldWhiteList(world).contains(player)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public List<String> getWorldWhiteList(String world){
		List<String> list = null;
		if (worldFolderExists(world)){
			list = WorldWhitelistFile.getCustomConfig().getStringList(world + ".players");
			return list;
		}
		else {
			logConsole("Sorry, world '" + world + "' doesn't exist.");
			return list;
		}
	}
	
	public String cleanListArray(List<String> array, String emptyMessage){
		String string = "";
		for (int i = 0; i < array.size(); i++){
		if (i == (array.size() - 1)){
			string += array.get(i) + "";	
		}
		else {
			string += array.get(i) + ", ";	
		}
		}
		
		if (string == ""){
			string = emptyMessage;
		}
		return string;
	}
	
	public void setWorldTeleportLocation(String world, Location loc){
		WorldSpawnFile.getCustomConfig().set(world + ".world", loc.getWorld().getName());
		WorldSpawnFile.getCustomConfig().set(world + ".x", loc.getX());
		WorldSpawnFile.getCustomConfig().set(world + ".y", loc.getY());
		WorldSpawnFile.getCustomConfig().set(world + ".z", loc.getZ());
		WorldSpawnFile.getCustomConfig().set(world + ".yaw", loc.getYaw());
		WorldSpawnFile.getCustomConfig().set(world + ".pitch", loc.getPitch());
		WorldSpawnFile.saveCustomConfig();
	}
	
	public Location getWorldTeleportLocation(String world){
		return new Location(Bukkit.getWorld(world), WorldSpawnFile.getCustomConfig().getDouble(world + ".x"), WorldSpawnFile.getCustomConfig().getDouble(world + ".y"), WorldSpawnFile.getCustomConfig().getDouble(world + ".z"), (float) WorldSpawnFile.getCustomConfig().getDouble(world + ".yaw"), (float) WorldSpawnFile.getCustomConfig().getDouble(world + ".pitch"));
	}
	
	public void regenerateConfigForWorlds(){
		List<String> worlds = getAllWorlds();
		for (int i = 0; i < worlds.size(); i++){
			if (worldContainsSettings(worlds.get(i))){
				generateWorldConfiguration(worlds.get(i));
			}
		}
		
		logConsole("Configuration has been (re)generated.");
	}

	
	public void setSignLinesEvent(SignChangeEvent e, String line1, String line2, String line3, String line4){
		if (line1.equalsIgnoreCase("%line%")){
			e.setLine(0, colorTranslate(line1 + e.getLine(0)));
		}
		else {
			e.setLine(0, colorTranslate(line1));
		}
		
		if (line2.equalsIgnoreCase("%line%")){
			e.setLine(1, colorTranslate(line2 + e.getLine(1)));
		}
		else {
			e.setLine(1, colorTranslate(line2));
		}
		
		if (line3.equalsIgnoreCase("%line%")){
			e.setLine(2, colorTranslate(line3 + e.getLine(2)));
		}
		else {
			e.setLine(2, colorTranslate(line3));
		}
		
		if (line4.equalsIgnoreCase("%line%")){
			e.setLine(3, colorTranslate(line4 + e.getLine(3)));
		}
		else {
			e.setLine(3, colorTranslate(line4));
		}
		
	}
	
	public void clearSignEvent(SignChangeEvent s){
		s.setLine(0, "");
		s.setLine(1, "");
		s.setLine(2, "");
		s.setLine(3, "");
	}
	
	public void toggleWorldControlSign(Location loc){
		List<String> signs = getAllWorldControlSignLoc();
		if (signs.contains(parseLocationToString(loc))){
			return;
		}
		else {
			signs.add(parseLocationToString(loc));
			setWorldControlSignLocations(signs);
		}
	}
	
	public String constructCustomJoinMessage(Player player, World world, String string){
		string = string.replaceAll("<player>", player.getName());
		string = string.replaceAll("<displayname>", player.getDisplayName());
		string = string.replaceAll("<world-online>", world.getPlayers().size() + "");
		string = string.replaceAll("<world-online-max>", (int) getWorldSettingValue(world.getName(), "player-limit") + "");
		string = string.replaceAll("<players-online>", Bukkit.getServer().getOnlinePlayers().size() + "");
		return string;
	}
	
	public void setWorldControlSignLocations(List<String> signs){
		WorldSignFile.getCustomConfig().set("Signs", signs);
		WorldSignFile.saveCustomConfig();
	}
	
	public void setWorldControlSignType(Location loc, WorldControlSignType type){
		WorldSignFile.getCustomConfig().set(parseLocationToString(loc) + ".type", type.name());
		WorldSignFile.saveCustomConfig();
	}
	
	public Object getWorldControlSignType(Location loc){
		return WorldSignFile.getCustomConfig().get(parseLocationToString(loc) + ".type").toString();
	}
	
	public List<String> getAllWorldControlSignLoc(){
		return WorldSignFile.getCustomConfig().getStringList("Signs");
	}
	
	public String colorTranslate(String textToTranslate){
		return ChatColor.translateAlternateColorCodes('&', textToTranslate);
	}
	
	public String parseLocationToString(Location loc){
		return loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
	}
	
	public Location parseStringToLocation(String loc){
		return new Location(Bukkit.getWorld(loc.split(" ")[0]), Double.parseDouble(loc.split(" ")[1]), Double.parseDouble(loc.split(" ")[2]), Double.parseDouble(loc.split(" ")[3]));
	}
	
	public boolean portalDestinationExists(String dest){
		if (WorldPortalFile.getCustomConfig().getString("destinations." + dest) == null){
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean portalExists(String portal){
		if (WorldPortalFile.getCustomConfig().getString("portals." + portal) == null){
			return false;
		}
		else {
			return true;
		}
	}
	
	public void setDestination(String dest, Location loc){
		WorldPortalFile.getCustomConfig().set("destinations." + dest + ".world", loc.getWorld().getName());
		WorldPortalFile.getCustomConfig().set("destinations." + dest + ".x", loc.getX());
		WorldPortalFile.getCustomConfig().set("destinations." + dest + ".y", loc.getY());
		WorldPortalFile.getCustomConfig().set("destinations." + dest + ".z", loc.getZ());
		WorldPortalFile.getCustomConfig().set("destinations." + dest + ".pitch", loc.getPitch());
		WorldPortalFile.getCustomConfig().set("destinations." + dest + ".yaw", loc.getYaw());
		WorldPortalFile.saveCustomConfig();
		logConsole(" Destination '" + dest + "' has been set at '" + parseLocationToString(loc) + "'.");
	}
	
	public void createPortal(Player player, String world, String portal, String dest, Location loc1, Location loc2, Material replacementBlock){
		occurnaces.put(player, 0);

	    maxX.put(player, Math.max(loc1.getBlockX(), loc2.getBlockX()));
	    maxY.put(player, Math.max(loc1.getBlockY(), loc2.getBlockY()));
	    maxZ.put(player, Math.max(loc1.getBlockZ(), loc2.getBlockZ()));

	    minX.put(player, Math.min(loc1.getBlockX(), loc2.getBlockX()));
	    minY.put(player, Math.min(loc1.getBlockY(), loc2.getBlockY()));
	    minZ.put(player, Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

	    if (portalExists(portal)){
	    	deletePortal(portal);
	    }
	    
		for(x.put(player, minX.get(player)); x.get(player) <= maxX.get(player); x.put(player, x.get(player) + 1)){
			  for(y.put(player, minY.get(player)); y.get(player) <= maxY.get(player); y.put(player, y.get(player) + 1)){
				  for(z.put(player, minZ.get(player)); z.get(player) <= maxZ.get(player); z.put(player, z.get(player) + 1)){
					block.put(player, new Location(Bukkit.getWorld(world), x.get(player), y.get(player), z.get(player)).getBlock());
					if (block.get(player).getType() == Material.GOLD_BLOCK){
					occurnaces.put(player, occurnaces.get(player) + 1);
					block.get(player).setType(replacementBlock);
					WorldPortalLocationFile.getCustomConfig().set(parseLocationToString(block.get(player).getLocation()), portal);
					}
				  }
			  }
		  }
		
		WorldPortalLocationFile.saveCustomConfig();
	    
	    WorldPortalFile.getCustomConfig().set("portals." + portal + ".loc-1", parseLocationToString(loc1));
	    WorldPortalFile.getCustomConfig().set("portals." + portal + ".loc-2", parseLocationToString(loc2));
	    WorldPortalFile.getCustomConfig().set("portals." + portal + ".dest", dest);
	    WorldPortalFile.saveCustomConfig();
	    ChatUtili.sendTranslatedMessage(player, "&dBlock(s) Replaced: " + occurnaces.get(player));
	    logConsole("Portal '" + portal + "' has been set with destination '" + dest + "'.");
	}
	
	public void deletePortal(String portal){
	portal = portal.toLowerCase();
	Location loc1 = parseStringToLocation(WorldPortalFile.getCustomConfig().getString("portals." + portal + ".loc-1"));
	Location loc2 = parseStringToLocation(WorldPortalFile.getCustomConfig().getString("portals." + portal + ".loc-2"));

	//int occurnaces = 0;

	int maxX =  Math.max(loc1.getBlockX(), loc2.getBlockX());
	int maxY =  Math.max(loc1.getBlockY(), loc2.getBlockY());
	int maxZ =  Math.max(loc1.getBlockZ(), loc2.getBlockZ());

	int minX =  Math.min(loc1.getBlockX(), loc2.getBlockX());
	int minY =  Math.min(loc1.getBlockY(), loc2.getBlockY());
	int minZ =  Math.min(loc1.getBlockZ(), loc2.getBlockZ());

	for(int x =  minX; x <= maxX; x = x + 1){
	  for(int y =  minY; y <= maxY; y = y + 1){
	    for(int z =  minZ; z <= maxZ; z = z + 1){
	    	Block block =  new Location(loc1.getWorld(), x, y, z).getBlock();
	    	//occurnaces++;
	    	WorldPortalLocationFile.getCustomConfig().set(parseLocationToString(block.getLocation()), null);
	    }
      }
	}
	
	WorldPortalFile.getCustomConfig().set("portals." + portal, null);
	WorldPortalFile.saveCustomConfig();

	logConsole("Portal '" + portal + "' has been deleted.");
	}
	
	public void deleteDestination(String dest){
		WorldPortalFile.getCustomConfig().set("destinations." + dest, null);
		WorldPortalFile.saveCustomConfig();
		logConsole("Destination '" + dest + "' has been deleted.");
	}
	
	public Location getPortalDestinationLocation(String portal){
		return getDestinationLocation(WorldPortalFile.getCustomConfig().getString("portals." + portal + ".dest"));
	}
	
	public Location getDestinationLocation(String dest_name){
		Location loc = null;
		if (portalDestinationExists(dest_name)){
			 loc = new Location(Bukkit.getWorld(WorldPortalFile.getCustomConfig().getString("destinations." + dest_name + ".world")), WorldPortalFile.getCustomConfig().getDouble("destinations." + dest_name + ".x"), WorldPortalFile.getCustomConfig().getDouble("destinations." + dest_name + ".y"), WorldPortalFile.getCustomConfig().getDouble("destinations." + dest_name + ".z"), (float) WorldPortalFile.getCustomConfig().getDouble("destinations." + dest_name + ".yaw"), (float) WorldPortalFile.getCustomConfig().getDouble("destinations." + dest_name + ".pitch"));
			 return loc;
		}
		else {
			logConsole("Destination '" + dest_name + "' does not exist.");
			return loc;
		}
	}
	
	public boolean materialExists(Material material){
		boolean valid = true;
		try {
			Bukkit.createInventory(null, 9, "Test").addItem(new ItemStack(material));
			valid = true;
		}
		catch (Exception e){
			valid = false;
		}
		return valid;
	}
	
	public String getCleanPortalList(){
		String list = "";
		try {
			Set<String> nodes = WorldPortalFile.getCustomConfig().getConfigurationSection("portals").getKeys(false);
			list = nodes + "";
			
		}
		catch (Exception e){
			list = "No Portals";
		}
		return list;
	}
	
	public String getCleanDestinationsList(){
		String list = "";
		try {
		Set<String> nodes = WorldPortalFile.getCustomConfig().getConfigurationSection("destinations").getKeys(false);
		list = nodes + "";
		}
		catch (Exception e){
			list = "No Destinations";
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void setCurrentWorldInventory(Player player){
		playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		worldInventory.put(player, (List<ItemStack>) playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".inventory"));
		
		for (loop.put(player, 0); loop.get(player) < worldInventory.get(player).size(); loop.put(player, loop.get(player) + 1)){
				player.getInventory().setItem(loop.get(player), worldInventory.get(player).get(loop.get(player)));
		}
		
		player.getInventory().setHelmet(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".helm"));
		player.getInventory().setChestplate(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".chestplate"));
		player.getInventory().setLeggings(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".leggings"));
		player.getInventory().setBoots(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".boots"));
		player.updateInventory();
	}
	
	@SuppressWarnings("unchecked")
	public void setWorldInventory(Player player, String world){
		playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		worldInventory.put(player, (List<ItemStack>) playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + world + ".inventory"));
		
		for (loop.put(player, 0); loop.get(player) < worldInventory.get(player).size(); loop.put(player, loop.get(player) + 1)){
				player.getInventory().setItem(loop.get(player), worldInventory.get(player).get(loop.get(player)));
		}
		
		player.getInventory().setHelmet(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".helm"));
		player.getInventory().setChestplate(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".chestplate"));
		player.getInventory().setLeggings(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".leggings"));
		player.getInventory().setBoots(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".boots"));
		player.updateInventory();
	}
	
	@SuppressWarnings({ "unchecked" })
	public void setWorldFlag(Player player, String world, String flag, Object[] value){
	int index = 3;	
	if (worldContainsSettings(world) == false){
		generateWorldConfiguration(world);
	}
	if (worldFlagExists(world, flag)){
		try {
			//Setting flag values
			if (flag.equalsIgnoreCase("player-limit") || flag.equalsIgnoreCase("mob-limit") || flag.equalsIgnoreCase("title-join-message-main-display-time") || flag.equalsIgnoreCase("title-join-message-sub-display-time")){
				overrideWorldConfigOption(world, flag, Integer.parseInt(value[index].toString()));
				ChatUtili.sendTranslatedMessage(player, "&7World &e'" + world + "' &7flag &e'"  + flag + "' &7has been set to: &e" + value[index]);
			}
			else if (flag.equalsIgnoreCase("certain-blocks-place-allow") || flag.equalsIgnoreCase("certain-blocks-break-allow") ||  flag.equalsIgnoreCase("certain-mob-spawn-allow") || flag.equalsIgnoreCase("certain-commands-use-allow")){
				flagValues.put(player, ((List<String>) getWorldSettingValue(world, flag)));
				if (flag.equalsIgnoreCase("certain-commands-use-allow") == false){
					value[index] = value[index].toString().toUpperCase();
				}
				if (flagValues.get(player).contains(value[index].toString())){
					flagValues.get(player).remove(value[index].toString());
					overrideWorldConfigOption(world, flag, flagValues.get(player));
					ChatUtili.sendTranslatedMessage(player, "&7World &e'" + world + "' &7flag &e'"  + flag + "' &7value &e'" + value[index].toString() + "' &7has been removed.");
				}
				else {
					flagValues.get(player).add(value[index].toString());
					overrideWorldConfigOption(world, flag, flagValues.get(player));
					ChatUtili.sendTranslatedMessage(player, "&7World &e'" + world + "' &7flag &e'"  + flag + "' &7value &e'" + value[index].toString() + "' &7has been added.");
				}
			}
			else if (flag.equalsIgnoreCase("fallback-world") || flag.equalsIgnoreCase("title-join-message-main") || flag.equalsIgnoreCase("title-join-message-sub") || flag.equalsIgnoreCase("default-gamemode")){
				flagStringBuilder.put(player, new StringBuilder());
				for(flagLoop.put(player, index); flagLoop.get(player) < value.length; flagLoop.put(player, flagLoop.get(player) + 1)){
					flagStringBuilder.get(player).append(value[flagLoop.get(player)] + " ");
				}
				overrideWorldConfigOption(world, flag, flagStringBuilder.get(player));
				ChatUtili.sendTranslatedMessage(player, "&7World &e'" + world + "' flag '"  + flag + "' &7has been set to: &e" + flagStringBuilder.get(player));
			}
			else {
				overrideWorldConfigOption(world, flag, Boolean.parseBoolean(value[index].toString()));	
				ChatUtili.sendTranslatedMessage(player, "&7World &e'" + world + "' &7flag &e'"  + flag + "' &7has been set to: &e" + value[index]);
			}
		}
		catch (Exception e){
			ChatUtili.sendTranslatedMessage(player, "&cInvalid value supplied");
		}
	}
	else {
		ChatUtili.sendTranslatedMessage(player, "&cInvalid flag '" + flag + "'.");
	}
	}
	
	public boolean worldFlagExists(String world, String flag){
		if (WorldConfigFile.getCustomConfig().getString(world + "." + flag) != null){
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getWorldFlagsMessage(String world){
		String string = "";
		Object[] keys = WorldConfigFile.getCustomConfig().getConfigurationSection(world).getKeys(false).toArray();
	
		for (int i = 0; i < keys.length; i++){
			if (i == (keys.length - 1)){
				string += keys[i] + "&c.";
			}
			else {
				string += keys[i] + "&c,&a ";
			}
		}
		
		return string;
	}
	
	public void logConsole(String message){
		System.out.println("[WorldControl] " + message);	
	}
}

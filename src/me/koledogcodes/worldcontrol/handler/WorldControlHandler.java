package me.koledogcodes.worldcontrol.handler;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.WorldControlSignType;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldConfigFile;
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
	
	public HashMap<Player, String> world = new HashMap<Player, String>();
	public static HashMap<Player, Boolean> tpSuccecs = new HashMap<Player, Boolean>();
	private HashMap<String, List<String>> worldWhitelist = new HashMap<String, List<String>>();
	
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
					WorldConfigFile.saveCustomConfig();
					WorldConfigFile.reloadCustomConfig();
			}
		}
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
	
	public void logConsole(String message){
		System.out.println("[WorldControl] " + message);	
	}
}

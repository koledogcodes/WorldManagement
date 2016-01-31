package me.koledogcodes.worldcontrol.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.MessageType;
import me.koledogcodes.worldcontrol.api.MessageTypeConvertor;
import me.koledogcodes.worldcontrol.api.PlayerStat;
import me.koledogcodes.worldcontrol.api.WorldControlSignType;
import me.koledogcodes.worldcontrol.api.WorldData;
import me.koledogcodes.worldcontrol.api.WorldGenerator;
import me.koledogcodes.worldcontrol.configs.BlockDataFile;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.MessageFile;
import me.koledogcodes.worldcontrol.configs.PlayerDataFile;
import me.koledogcodes.worldcontrol.configs.WorldConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldDataFile;
import me.koledogcodes.worldcontrol.configs.WorldPortalFile;
import me.koledogcodes.worldcontrol.configs.WorldPortalLocationFile;
import me.koledogcodes.worldcontrol.configs.WorldSignFile;
import me.koledogcodes.worldcontrol.configs.WorldSpawnFile;
import me.koledogcodes.worldcontrol.configs.WorldWhitelistFile;
import me.koledogcodes.worldcontrol.custom.events.WorldControlPreUnloadWorldEvent;
import me.koledogcodes.worldcontrol.events.BukkitWorldControlEvent;
import me.koledogcodes.worldcontrol.timer.WorldControlTimer;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutBlockAction;

public class WorldControlHandler {
	
	private static WorldControl plugin;
	public WorldControlHandler(WorldControl i) {
		
	}

	public WorldControlHandler() {
		
	}
	
	public WorldControl getWorldControl(){
		return plugin;
	}
	
	public static void setInstance(WorldControl control){
		plugin = control;
	}
	
	private final static int RESULTS_PER_PAGE = 6;
	private WorldControlTimer timer = new WorldControlTimer();
	private HashMap<Integer, Thread> threads = new HashMap<Integer, Thread>();
	private HashMap<Player, Inventory> echest = new HashMap<Player, Inventory>();
	private HashMap<Player, List<String>> playerPotions = new HashMap<Player, List<String>>();
	private HashMap<Player, List<ItemStack>> echestItems = new HashMap<Player, List<ItemStack>>();
	private HashMap<Player, Double> rotation = new HashMap<Player, Double>();
	private HashMap<Player, Long> time = new HashMap<Player, Long>();
	private HashMap<Player, Long> day = new HashMap<Player, Long>();
	private HashMap<Player, Long> hour = new HashMap<Player, Long>();
	private HashMap<Player, Long> min = new HashMap<Player, Long>();
	private HashMap<Player, Long> secs = new HashMap<Player, Long>();
	private HashMap<Player, Integer> loop = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> loop2 = new HashMap<Player, Integer>();
	private HashMap<Player, String> mSecs = new HashMap<Player, String>();
	private HashMap<Player, Integer> blockDataLoop = new HashMap<Player, Integer>();
	private HashMap<Player, List<String>> flagValues = new HashMap<Player, List<String>>();
	private HashMap<Player, List<String>> blockDataList = new HashMap<Player, List<String>>();
	private HashMap<Player, Integer> flagLoop = new HashMap<Player, Integer>();
	private HashMap<Player, String> flagStringBuilder = new HashMap<Player, String>();
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
	private HashMap<CommandSender, Integer> i = new HashMap<CommandSender, Integer>();
	private HashMap<CommandSender, Integer> j = new HashMap<CommandSender, Integer>();
	private HashMap<CommandSender, Integer> k = new HashMap<CommandSender, Integer>();
	private HashMap<Player, Integer> occurnaces = new HashMap<Player, Integer>();
	private HashMap<Player, Block> block = new HashMap<Player, Block>();
	public HashMap<Player, String> world = new HashMap<Player, String>();
	public static HashMap<Player, Location> blockInspectionLocation = new HashMap<Player, Location>();
	public static HashMap<Player, String> blockInspectionLocationType = new HashMap<Player, String>();
	public static HashMap<Player, Boolean> blockInspection = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> tpSuccecs = new HashMap<Player, Boolean>();
	public static List<String> fallbackTpOverride = new ArrayList<String>();
	public static HashMap<Player, Boolean> portalTeleportInstance = new HashMap<Player, Boolean>();
	public static HashMap<Player, String> portalCreationInfo = new HashMap<Player, String>();
	private HashMap<String, List<String>> worldWhitelist = new HashMap<String, List<String>>();
	private HashMap<String, List<Player>> playerCollection = new HashMap<String, List<Player>>();
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
	      return (path.delete());
	}
	
	public void loadWorld(Player player, String world){
		if (worldFolderExists(world)){
			if (worldExists(world) == false){
				WorldData data = new WorldData(world);
				WorldCreator loader = new WorldCreator(world);
				loader.environment(data.getEnvironment());
				loader.seed(data.getSeed());
				loader.type(data.getWorldType());
				Bukkit.createWorld(loader);
				logConsole("World '" + world + "' has been loaded.");
				if (player != null){
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + world + "' has been loaded!");
					//Bukkit.getServer().getPluginManager().callEvent(new WorldControlLoadWorldEvent(player, world));
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
		if (world.toUpperCase().equals(world)){
			ChatUtili.sendTranslatedMessage(player, "&cYou cannot create a world that is all uppercase.");
			return;
		}
		
		if (worldFolderExists(world)){
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
		if (world.toUpperCase().equals(world)){
			ChatUtili.sendTranslatedMessage(player, "&cYou cannot create a world that is all uppercase.");
			return;
		}
		
		if (worldFolderExists(world)){
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
		if (world.toUpperCase().equals(world)){
			ChatUtili.sendTranslatedMessage(player, "&cYou cannot create a world that is all uppercase.");
			return;
		}
		
		if (worldFolderExists(world)){
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
		if (world.toUpperCase().equals(world)){
			ChatUtili.sendTranslatedMessage(player, "&cYou cannot create a world that is all uppercase.");
			return;
		}
		
		if (worldFolderExists(world)){
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
		if (world.toUpperCase().equals(world)){
			ChatUtili.sendTranslatedMessage(player, "&cYou cannot create a world that is all uppercase.");
			return;
		}
		
		if (worldFolderExists(world)){
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
			return isValidWorld(file.getPath());
		}
		else {
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
	
	public void messageWorldsStatus(final CommandSender player, final int page){
		excuteJavaThread(new Runnable(){

			@Override
			public void run() {
				
				List<String> worlds = getAllWorlds();
				
				if (page <= 0 || page > Math.ceil((double) worlds.size() / (double) RESULTS_PER_PAGE)){
					ChatUtili.sendTranslatedMessage(player, "&cInvalid page!");
					return;
				}
				
				player.sendMessage(ChatUtili.colorConvert("&7----- &6Worlds &7----- &c(" + worlds.size() + " worlds in total)"));

				if ((page * RESULTS_PER_PAGE) > worlds.size()){
					for (i.put(player, (page * RESULTS_PER_PAGE) - RESULTS_PER_PAGE); i.get(player) < (worlds.size()); i.put(player, i.get(player) + 1)){
						if (worldExists(worlds.get(i.get(player)))){
							ChatUtili.sendSimpleTranslatedMessage(player, "&a" + worlds.get(i.get(player)) + " &b(loaded)");
						}
						else {
							ChatUtili.sendSimpleTranslatedMessage(player, "&a" + worlds.get(i.get(player)) + " &b(not loaded)");
						}
					}
				}
				else {
					for (i.put(player, (page * RESULTS_PER_PAGE) - RESULTS_PER_PAGE); i.get(player) < (page * RESULTS_PER_PAGE); i.put(player, i.get(player) + 1)){
						if (worldExists(worlds.get(i.get(player)))){
							ChatUtili.sendSimpleTranslatedMessage(player, "&a" + worlds.get(i.get(player)) + " &b(loaded)");
						}
						else {
							ChatUtili.sendSimpleTranslatedMessage(player, "&a" + worlds.get(i.get(player)) + " &b(not loaded)");
						}
					}
				}
				
				player.sendMessage(ChatUtili.colorConvert("&fPage " + page + "/" + (int) (Math.ceil((double) worlds.size() / (double) RESULTS_PER_PAGE)) ));
				player.sendMessage(ChatUtili.colorConvert("&7---------- "));
				
				return;
			}
			
		});
	}
	
	public List<String> getAllWorlds(){
		File getRoot = new File("plugins");
		File file = new File(getRoot.getAbsoluteFile().getParentFile().getPath().toString());
		File[] getFiles = file.listFiles();
		
		List<String> directories = new ArrayList<String>();
		
		for (File f: getFiles){
			if (f.isDirectory() && blacklistFolders().contains(f.getName()) == false){
				if (isValidWorld(f.getPath())){
					directories.add(f.getName());
				}
				else {
					continue;
				}
			}
		}
		
		return directories;
	}
	
	public List<String> getAllLoadedWorlds(){
		File getRoot = new File("plugins");
		File file = new File(getRoot.getAbsoluteFile().getParentFile().getPath().toString());
		File[] getFiles = file.listFiles();
		
		final List<String> directories = new ArrayList<String>();
		
		for (File f: getFiles){
			if (f.isDirectory() && blacklistFolders().contains(f.getName()) == false){
				if (isValidWorld(f.getPath())){
					if (worldExists(f.getName())){
						directories.add(f.getName());
					}
				}
				else {
					continue;
				}
			}
		}
		
		
		return directories;
	}
	
	private boolean isValidWorld(String dir){
		if (new File(dir, "uid.dat").exists()){
			return true;
		}
		else {
			return false;
		}
	}
	
	public void generateWorldConfiguration(final Player player){
		excuteJavaThread(new Runnable(){

			@Override
			public void run() {
				
				if (worldFolderExists(world.get(player))){
					if (worldExists(world.get(player))){
						//Generate
							setWorldConfigOption(player, "pvp", true);	
							setWorldConfigOption(player, "build", true);
							
							if (getWorldSettingValue(player.getWorld().getName(), "certain-blocks-place-allow") != null){
								setWorldConfigOption(player, "block-place-list", getWorldSettingValue(player.getWorld().getName(), "certain-blocks-place-allow"));
								overrideWorldConfigOption(player.getWorld().getName(), "certain-blocks-place-allow", null);
							}
							else {
								setWorldConfigOption(player, "block-place-list", new String[]{"SEEDS"});
							}
							
							if (getWorldSettingValue(player.getWorld().getName(), "certain-blocks-break-allow") != null){
								setWorldConfigOption(player, "block-break-list", getWorldSettingValue(player.getWorld().getName(), "certain-blocks-break-allow"));
								overrideWorldConfigOption(player.getWorld().getName(), "certain-blocks-break-allow", null);
							}
							else {
								setWorldConfigOption(player, "block-break-list", new String[]{"SEEDS"});
							}
							
							setWorldConfigOption(player, "weather-locked", false);
							setWorldConfigOption(player, "mob-spawn", true);
							
							if (getWorldSettingValue(player.getWorld().getName(), "certain-mob-spawn-allow") != null){
								setWorldConfigOption(player, "mob-spawn-list", getWorldSettingValue(player.getWorld().getName(), "certain-mob-spawn-allow"));
								overrideWorldConfigOption(player.getWorld().getName(), "certain-mob-spawn-allow", null);
							}
							else {
								setWorldConfigOption(player, "mob-spawn-list", new String[]{"CHICKEN","COW","SHEEP"});
							}
							
							setWorldConfigOption(player, "player-limit", 1);	
							setWorldConfigOption(player, "commands-allowed", true);	
							
							if (getWorldSettingValue(player.getWorld().getName(), "certain-commands-use-allow") != null){
								setWorldConfigOption(player, "cmd-allowed-list", getWorldSettingValue(player.getWorld().getName(), "certain-commands-use-allow"));
								overrideWorldConfigOption(player.getWorld().getName(), "certain-commands-use-allow", null);
							}
							else {
								setWorldConfigOption(player, "cmd-allowed-list", new String[]{"spawn", "msg", "r"});
							}
							
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
							setWorldConfigOption(player, "world-inventory-bind", player.getWorld().getName());
							setWorldConfigOption(player, "world-enderchest-bind", player.getWorld().getName());
							setWorldConfigOption(player, "world-playerstats-bind", player.getWorld().getName());
							setWorldConfigOption(player, "world-chat-bind", player.getWorld().getName());
							setWorldConfigOption(player, "nether-world", player.getWorld().getName().replaceAll("_nether", "") + "_nether");
							setWorldConfigOption(player, "overworld-world", player.getWorld().getName().replaceAll("_nether", ""));
							setWorldConfigOption(player, "leaves-decay", false);
							setWorldConfigOption(player, "no-hunger", false);
							setWorldConfigOption(player, "lava-flow", false);
							setWorldConfigOption(player, "water-flow", false);
							setWorldConfigOption(player, "can-eat", true);
							setWorldConfigOption(player, "can-eat-list", new String[]{});
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
				
				return;
			}
			
		});
	}
	
	public void generateWorldConfiguration(String world){
		if (worldFolderExists(world)){
				//Generate
					setWorldConfigOption(world, "pvp", true);	
					setWorldConfigOption(world, "build", true);
					if (getWorldSettingValue(world, "certain-blocks-place-allow") != null){
						setWorldConfigOption(world, "block-place-list", getWorldSettingValue(world, "certain-blocks-place-allow"));
						overrideWorldConfigOption(world, "certain-blocks-place-allow", null);
					}
					else {
						setWorldConfigOption(world, "block-place-list", new String[]{"SEEDS"});
					}
					
					if (getWorldSettingValue(world, "certain-blocks-break-allow") != null){
						setWorldConfigOption(world, "block-break-list", getWorldSettingValue(world, "certain-blocks-break-allow"));
						overrideWorldConfigOption(world, "certain-blocks-break-allow", null);
					}
					else {
						setWorldConfigOption(world, "block-break-list", new String[]{"SEEDS"});
					}
					
					setWorldConfigOption(world, "weather-locked", false);
					setWorldConfigOption(world, "mob-spawn", true);
					
					if (getWorldSettingValue(world, "certain-mob-spawn-allow") != null){
						setWorldConfigOption(world, "mob-spawn-list", getWorldSettingValue(world, "certain-mob-spawn-allow"));
						overrideWorldConfigOption(world, "certain-mob-spawn-allow", null);
					}
					else {
						setWorldConfigOption(world, "mob-spawn-list", new String[]{"CHICKEN","COW","SHEEP"});
					}
					
					setWorldConfigOption(world, "player-limit", 1);	
					setWorldConfigOption(world, "commands-allowed", true);	
					
					if (getWorldSettingValue(world, "certain-commands-use-allow") != null){
						setWorldConfigOption(world, "cmd-allowed-list", getWorldSettingValue(world, "certain-commands-use-allow"));
						overrideWorldConfigOption(world, "certain-commands-use-allow", null);
					}
					else {
						setWorldConfigOption(world, "cmd-allowed-list", new String[]{"spawn", "msg", "r"});
					}
					
					setWorldConfigOption(world, "fallback-world", "world");
					setWorldConfigOption(world, "players-invincible", false);
					setWorldConfigOption(world, "mobs-invincible", false);
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
					setWorldConfigOption(world, "world-inventory-bind", world);
					setWorldConfigOption(world, "world-enderchest-bind", world);
					setWorldConfigOption(world, "world-playerstats-bind", world);
					setWorldConfigOption(world, "world-chat-bind", world);
					setWorldConfigOption(world, "nether-world", world.replaceAll("_nether", "") + "_nether");
					setWorldConfigOption(world, "overworld-world", world.replaceAll("_nether", ""));
					setWorldConfigOption(world, "leaves-decay", false);
					setWorldConfigOption(world, "no-hunger", false);
					setWorldConfigOption(world, "lava-flow", false);
					setWorldConfigOption(world, "water-flow", false);
					setWorldConfigOption(world, "can-eat", true);
					setWorldConfigOption(world, "can-eat-list", new String[]{});
					WorldConfigFile.saveCustomConfig();
					WorldConfigFile.reloadCustomConfig();
		}
	}
	
	public void generateConfiguration(){
		setConfigOption("prefix", "&dWorld &8>>");
		setConfigOption("worlds-to-load-on-startup", new ArrayList<String>());
		setConfigOption("blacklist-worlds", new ArrayList<String>());
		setConfigOption("portal-teleport-message", "You have been teleported <player>!");
		setConfigOption("auto-update", true);
		setConfigOption("opt-out", true);
		setConfigOption("Per-World.inventory", false);
		setConfigOption("Per-World.inventory-per-gamemode", false);
		setConfigOption("Per-World.enderchest", false);
		setConfigOption("Per-World.health", false);
		setConfigOption("Per-World.hunger", false);
		setConfigOption("Per-World.xp", false);
		setConfigOption("Per-World.potion", false);
		setConfigOption("Per-World.chat", false);
		setConfigOption("block-logging", true);
		setConfigOption("Autosave.time-in-mins", 15);
	}
	
	public void generateMessages(){
		setMessageOption("place-block-deny", "{PREFIX} &cYou cannot place block '&4{BLOCK}&c' in this world.");
		setMessageOption("break-block-deny", "{PREFIX} &cYou cannot break block '&4{BLOCK}&c' in this world.");
		setMessageOption("playerlimit-deny", "{PREFIX} &cYou cannot teleport to world '{WORLD}' becuase it has reached its player limit.");
		setMessageOption("player-cmd-deny", "{PREFIX} &cYou cannot use &4{COMMAND} &cin this world.");
		setMessageOption("player-chat-deny", "{PREFIX} &cYou cannot chat in world '&4{WORLD}&c'.");
		setMessageOption("player-whitelist-deny", "{PREFIX} &cYou cannot tp to world '{WORLD}' that your not whitelisted in.");
		setMessageOption("player-interact-deny", "{PREFIX} &cYou cannot use &4'{ITEM}' &cin this world.");
		setMessageOption("alternate-world-deny", "{PREFIX} &cAlternate world must be created to use this portal.");
		setMessageOption("nether-portal-deny", "{PREFIX} &cYou cannot use nether portals in this world.");
		setMessageOption("player-world-permission-deny", "{PREFIX} &cYou do not have permission to go to this world.");
		setMessageOption("player-eat-deny", "{PREFIX} &cYou cannot eat '&4{ITEM}&c' &cin this world.");
	}
	
	public String getDenyMessage(MessageType m){
		return MessageFile.getCustomConfig().getString(MessageTypeConvertor.translateType(m));
	}
	
	public String replaceBasicPlaceholders(Player player, World world, MessageType type){
		return getDenyMessage(type).
		replace("{PLAYER}", player.getName()).
		replace("{DISPLAYNAME}", player.getDisplayName()).
		replace("{WORLD}", world.getName()).
		replace("{PREFIX}", colorTranslate(ChatUtili.messagePrefix));
	}
	
	public void startAutosave(){
		long j = ConfigFile.getCustomConfig().getLong("Autosave.time-in-mins");
		WorldControlTimer timer = new WorldControlTimer();
		timer.registerNewRepeatingTimer(new TimerTask(){

			@Override
			public void run() {
				
				BlockDataFile.saveCustomConfig();
				
			}
			
		}, (((1000 * 60) * j) / 2), (((1000 * 60) * j) / 2));
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
	
	public void setMessageOption(String message, Object value){
		if (MessageFile.getCustomConfig().getString(message) == null){
			MessageFile.getCustomConfig().set(message, value);
			MessageFile.saveCustomConfig();
		}
		else {
			return;
		}
		
		message = null;
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
	
	public void unloadWorld(final Player player, final String world, final boolean save){
		excuteJavaThread(new Runnable(){

			@Override
			public void run() {
				
				if (worldFolderExists(world)){
					if (worldExists(world) == false){
						logConsole("Cannot not unloaded a non loaded World '" + world + "'.");
						if (player != null){
							ChatUtili.sendTranslatedMessage(player, "&cCannot not unloaded a non loaded world '" + world + "'.");
						}
					}
					else {
						Bukkit.getServer().getPluginManager().callEvent(new WorldControlPreUnloadWorldEvent(player, world, Bukkit.getServer().getWorld(world).getPlayers()));
						Bukkit.unloadWorld(world, save);
						//Bukkit.getServer().getPluginManager().callEvent(new WorldControlUnloadWorldEvent(player, world));
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
			
		});
		
	}
	
	public void copyWorld(Player player, String oldWorld, String newWorld){
		if (worldFolderExists(oldWorld)){
			if (worldExists(oldWorld)){
				WorldCreator creator = new WorldCreator(newWorld);
				creator.copy(Bukkit.getWorld(oldWorld));
				creator.createWorld();
				logConsole(" World '" + oldWorld + "' has been copied to new world '" + newWorld + "'.");
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
				fallbackTpOverride.add(world);
				unloadWorld(null, world, true);
				loadWorld(null, world);
				logConsole(" World '" + world + "' has saved!.");
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
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + worldToWhitelist + "' whitelist has been disabled.");	
					setWorldWhitelist(worldToWhitelist, false);
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + worldToWhitelist + "' whitelist has been enabled.");		
					setWorldWhitelist(worldToWhitelist, true);
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cCannot whitelist unloaded world '" + worldToWhitelist + "'.");
			}
		}
		else {
			ChatUtili.sendTranslatedMessage(player, "&cCannot whitelist world '" + worldToWhitelist + "' that doesn't exist.");
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
		string = string.replaceAll("<players-online>", Bukkit.getServer().getOnlinePlayers().toArray().length + "");
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
	
	@SuppressWarnings("deprecation")
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
						if (replacementBlock == Material.PORTAL){
							if (getCardinalDirection(player).equalsIgnoreCase("N") || getCardinalDirection(player).equalsIgnoreCase("S")){
								block.get(player).setType(replacementBlock);
							}
							else {
								block.get(player).setTypeIdAndData(replacementBlock.getId(), (byte) 2, true);
							}
						}
						else {
							block.get(player).setType(replacementBlock);
						}
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
	    	WorldPortalLocationFile.getCustomConfig().set(parseLocationToString(block.getLocation()), null);
	    }
      }
	}
	
	WorldPortalLocationFile.saveCustomConfig();
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
		if (material.isBlock() || material.isSolid() || material.isTransparent()){
			return true;
		}
		else { 
			return false;
		}
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
	
	public List<String> getDestinationsList(){
		return Arrays.asList(WorldPortalFile.getCustomConfig().getConfigurationSection("destinations").getKeys(false).toArray(new String[0]));
	}
	
	@SuppressWarnings("unchecked")
	public void setCurrentWorldInventory(Player player, GameMode mode){
		playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getString(player.getUniqueId().toString() + "." + getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind").toString() + ".main.inventory") == null){
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				logConsole("Player '" + player.getName() + "' does not have an inventory for world '" + player.getWorld().getName() + "'.");
				return;
			}
			
			worldInventory.put(player, (List<ItemStack>) playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind").toString() + ".main.inventory"));
		}
		else {
			if (playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind").toString() + "." + mode.name().toLowerCase() + ".inventory") == null){
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				logConsole("Player '" + player.getName() + "' does not have an inventory for world '" + player.getWorld().getName() + "'.");
				return;
			}
			
			worldInventory.put(player, (List<ItemStack>) playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind").toString() + "." + mode.name().toLowerCase() + ".inventory"));
		}
		
		for (loop.put(player, 0); loop.get(player) < worldInventory.get(player).size(); loop.put(player, loop.get(player) + 1)){
				player.getInventory().setItem(loop.get(player), worldInventory.get(player).get(loop.get(player)));
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.helm") != null){
				player.getInventory().setHelmet(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.helm"));
			}
			else {
				player.getInventory().setHelmet(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".helm") != null){
				player.getInventory().setHelmet(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".helm"));
			}
			else {
				player.getInventory().setHelmet(new ItemStack(Material.AIR));	
			}
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.chestplate") != null){
				player.getInventory().setChestplate(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.chestplate"));
			}
			else {
				player.getInventory().setChestplate(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".chestplate") != null){
				player.getInventory().setChestplate(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".chestplate"));
			}
			else {
				player.getInventory().setChestplate(new ItemStack(Material.AIR));	
			}
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.leggings") != null){
				player.getInventory().setLeggings(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.leggings"));
			}
			else {
				player.getInventory().setLeggings(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".leggings") != null){
				player.getInventory().setLeggings(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".leggings"));
			}
			else {
				player.getInventory().setLeggings(new ItemStack(Material.AIR));	
			}
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.boots") != null){
				player.getInventory().setBoots(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + ".main.boots"));
			}
			else {
				player.getInventory().setBoots(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".boots") != null){
				player.getInventory().setBoots(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + player.getWorld().getName() + "." + mode.name().toLowerCase() + ".boots"));
			}
			else {
				player.getInventory().setBoots(new ItemStack(Material.AIR));	
			}
		}
		
		player.updateInventory();
	}
	
	@SuppressWarnings("unchecked")
	public void setWorldInventory(Player player, String world, GameMode mode){
		playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getString(player.getUniqueId().toString() + "." + getWorldSettingValue(world, "world-inventory-bind").toString() + ".main.inventory") == null){
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				logConsole("Player '" + player.getName() + "' does not have an inventory for world '" + world + "'.");
				return;
			}
			
			worldInventory.put(player, (List<ItemStack>) playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + getWorldSettingValue(world, "world-inventory-bind").toString() + ".main.inventory"));
		}
		else {
			if (playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + getWorldSettingValue(world, "world-inventory-bind").toString() + "." + mode.name().toLowerCase() + ".inventory") == null){
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				logConsole("Player '" + player.getName() + "' does not have an inventory for world '" + world + "'.");
				return;
			}
			
			worldInventory.put(player, (List<ItemStack>) playerDataFile.get(player).getConfig().getList(player.getUniqueId().toString() + "." + getWorldSettingValue(world, "world-inventory-bind").toString() + "." + mode.name().toLowerCase() + ".inventory"));
		}
		
		for (loop.put(player, 0); loop.get(player) < worldInventory.get(player).size(); loop.put(player, loop.get(player) + 1)){
				player.getInventory().setItem(loop.get(player), worldInventory.get(player).get(loop.get(player)));
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.helm") != null){
				player.getInventory().setHelmet(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.helm"));
			}
			else {
				player.getInventory().setHelmet(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".helm") != null){
				player.getInventory().setHelmet(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".helm"));
			}
			else {
				player.getInventory().setHelmet(new ItemStack(Material.AIR));	
			}
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.chestplate") != null){
				player.getInventory().setChestplate(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.chestplate"));
			}
			else {
				player.getInventory().setChestplate(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".chestplate") != null){
				player.getInventory().setChestplate(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".chestplate"));
			}
			else {
				player.getInventory().setChestplate(new ItemStack(Material.AIR));	
			}
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.leggings") != null){
				player.getInventory().setLeggings(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.leggings"));
			}
			else {
				player.getInventory().setLeggings(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".leggings") != null){
				player.getInventory().setLeggings(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".leggings"));
			}
			else {
				player.getInventory().setLeggings(new ItemStack(Material.AIR));	
			}
		}
		
		if (mode == null){
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.boots") != null){
				player.getInventory().setBoots(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + ".main.boots"));
			}
			else {
				player.getInventory().setBoots(new ItemStack(Material.AIR));	
			}
		}
		else {
			if (playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".boots") != null){
				player.getInventory().setBoots(playerDataFile.get(player).getConfig().getItemStack(player.getUniqueId().toString() + "." + world + "." + mode.name().toLowerCase() + ".boots"));
			}
			else {
				player.getInventory().setBoots(new ItemStack(Material.AIR));	
			}
		}
		
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
			else if (flag.equalsIgnoreCase("block-place-list") || flag.equalsIgnoreCase("block-break-list") ||  flag.equalsIgnoreCase("mob-spawn-list") || flag.equalsIgnoreCase("cmd-allowed-list") || flag.equalsIgnoreCase("can-eat-list")){
				flagValues.put(player, ((List<String>) getWorldSettingValue(world, flag)));
				if (flag.equalsIgnoreCase("cmd-allowed-list") == false){
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
			else if (flag.equalsIgnoreCase("fallback-world") || flag.equalsIgnoreCase("title-join-message-main") || flag.equalsIgnoreCase("title-join-message-sub") || flag.equalsIgnoreCase("world-inventory-bind") || flag.equalsIgnoreCase("world-enderchest-bind") || flag.equalsIgnoreCase("world-playerstats-bind") || flag.equalsIgnoreCase("world-inventory-bind") || flag.equalsIgnoreCase("nether-world") || flag.equalsIgnoreCase("overworld-world") || flag.equalsIgnoreCase("world-chat-bind")){
				flagStringBuilder.put(player, "");
				for(flagLoop.put(player, index); flagLoop.get(player) < value.length; flagLoop.put(player, flagLoop.get(player) + 1)){
					flagStringBuilder.put(player, flagStringBuilder.get(player) + value[flagLoop.get(player)] + " ");
				}
				overrideWorldConfigOption(world, flag, new String(flagStringBuilder.get(player).substring(0, flagStringBuilder.get(player).length() - 1)));
				ChatUtili.sendTranslatedMessage(player, "&7World &e'" + world + "' &7flag '"  + flag + "' &7has been set to: &e" + flagStringBuilder.get(player));
			}
			else if (flag.equalsIgnoreCase("default-gamemode")){
				overrideWorldConfigOption(world, flag, value[index]);
				ChatUtili.sendTranslatedMessage(player, "&7World &e'" + world + "' &7flag '"  + flag + "' &7has been set to: &e" + value[index]);	
			}
			else {
				if (value[index].toString().equalsIgnoreCase("true") == false && value[index].toString().equalsIgnoreCase("false") == false){
					ChatUtili.sendTranslatedMessage(player, "&cOnly true/false values.");
					return;
				}
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
				string += colorTranslate(keys[i] + "&c");
			}
			else {
				string += colorTranslate(keys[i] + "&c,&a ");
			}
			
		}
		
		return string;
	}
	
	public boolean isInspector(Player player){
		if (blockInspection.containsKey(player) == false){
			return false;
		}
		else {
			if (blockInspection.get(player)){
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	private String getBlockTime(Player player, HashMap<Player, Long> time){
		day.put(player, (long) 0);
		hour.put(player, (long) 0); 
		min.put(player, (long) 0); 
		secs.put(player, (long) 0); 
		
		//Hour
		if (time.get(player) > ((1000 * 60) * 60)){
			hour.put(player, time.get(player) / ((1000 * 60) * 60) % 24);
			time.put(player, time.get(player) - hour.get(player));
		}
		
		//mins
		if (time.get(player) > (1000 * 60)){
			min.put(player, time.get(player) / (1000 * 60) % 60);
			time.put(player, time.get(player) - min.get(player));
		}
		
		//sec
		if (time.get(player) > 1000){
			secs.put(player, time.get(player) / 1000 % 60);
			time.put(player, time.get(player) - secs.get(player));
		}
		
		mSecs.put(player, "(Invalid Time)");
		
		if (hour.get(player) != 0){
			mSecs.put(player, hour.get(player) + " hr(s) " + min.get(player) + " min(s)");
			return mSecs.get(player);
		}
		
		if (min.get(player) != 0){
			mSecs.put(player, min.get(player) + " min(s) " + secs.get(player) + " sec(s)");			
			return mSecs.get(player);
		}
		
		if (secs.get(player) != 0){
			mSecs.put(player, secs.get(player) + " sec(s)");			
			return mSecs.get(player);
		}
		
		return mSecs.get(player);
	}
	
	public void messageBlockInformation(final Player player, Block block, int page){
		if (BlockDataFile.getCustomConfig().getString(parseLocationToString(block.getLocation())) == null){
			ChatUtili.sendTranslatedMessage(player, "&fNo block information for that location.");
			return;
		}
		
		int RPP = RESULTS_PER_PAGE;
		
		player.sendMessage(ChatUtili.colorConvert("&7----- &6WorldInspect &7----- &c(" + parseLocationToString(block.getLocation()) + ")"));
		
		blockDataList.put(player, BlockDataFile.getCustomConfig().getStringList(parseLocationToString(block.getLocation())));
		
		if (page <= 0 ||page > ((int) Math.ceil((double) blockDataList.get(player).size() / (double) RPP))){
			ChatUtili.sendTranslatedMessage(player, "&cInvalid page.");
			player.sendMessage(ChatUtili.colorConvert("&7---------- "));
			return;
		}
		
		Collections.reverse(blockDataList.get(player));
		
		if (blockDataList.get(player).size() <= RPP){
			for (blockDataLoop.put(player, 0); blockDataLoop.get(player) < blockDataList.get(player).size(); blockDataLoop.put(player, blockDataLoop.get(player) + 1)){
				time.put(player, System.currentTimeMillis() - Long.parseLong(blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[1]));
				player.sendMessage(ChatUtili.colorConvert("&7" + getBlockTime(player, time) + " &f- &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[0] + " &f" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[3] + " &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[2]));	
			}
				player.sendMessage(ChatUtili.colorConvert("&fPage 1/1"));
				player.sendMessage(ChatUtili.colorConvert("&7---------- "));
				return;
		}
		
		if (page == (Math.ceil(blockDataList.get(player).size() / RPP))){
			for (blockDataLoop.put(player, ((page * RPP) - RPP)); blockDataLoop.get(player) < (page * RPP); blockDataLoop.put(player, blockDataLoop.get(player) + 1)){
				time.put(player, System.currentTimeMillis() - Long.parseLong(blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[1]));
				player.sendMessage(ChatUtili.colorConvert("&7" + getBlockTime(player, time) + " &f- &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[0] + " &f" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[3] + " &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[2]));	
			}
				player.sendMessage(ChatUtili.colorConvert("&fPage " + page + "/" + ((int) Math.ceil((double) blockDataList.get(player).size() / (double) RPP))));
				player.sendMessage(ChatUtili.colorConvert("&7---------- "));
			return;
		}
		else {
			if ((page * RPP) > blockDataList.get(player).size()){
				for (blockDataLoop.put(player, ((page * RPP) - RPP)); blockDataLoop.get(player) < blockDataList.get(player).size(); blockDataLoop.put(player, blockDataLoop.get(player) + 1)){
						time.put(player, System.currentTimeMillis() - Long.parseLong(blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[1]));
						player.sendMessage(ChatUtili.colorConvert("&7" + getBlockTime(player, time) + " &f- &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[0] + " &f" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[3] + " &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[2]));	
					}
						player.sendMessage(ChatUtili.colorConvert("&fPage " + page + "/" + ((int) Math.ceil((double) blockDataList.get(player).size() / (double) RPP))));
			}
			else {
				for (blockDataLoop.put(player, ((page * RPP) - RPP)); blockDataLoop.get(player) < (page * RPP); blockDataLoop.put(player, blockDataLoop.get(player) + 1)){
					time.put(player, System.currentTimeMillis() - Long.parseLong(blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[1]));
					player.sendMessage(ChatUtili.colorConvert("&7" + getBlockTime(player, time) + " &f- &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[0] + " &f" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[3] + " &3" + blockDataList.get(player).get(blockDataLoop.get(player)).split("#")[2]));	
				}
					player.sendMessage(ChatUtili.colorConvert("&fPage " + page + "/" + ((int) Math.ceil((double) blockDataList.get(player).size() / (double) RPP))));
			}
		}
		

		player.sendMessage(ChatUtili.colorConvert("&7---------- "));
	}
	
	public void importOldInventories(final CommandSender sender){
		excuteJavaThread(new Runnable(){

			@Override
			public void run() {
				
				ChatUtili.sendTranslatedMessage(sender, "&aStarting to import old inventories.");
				logConsole("Starting to import old inventories.");
				File getUserdata = new File("plugins/WorldManagement/Userdata");
				File[] userdataFiles = getUserdata.listFiles();
				
				for (File file: userdataFiles){
					if (file.isFile()){
						if (file.getName().split("\\.")[1].equalsIgnoreCase("yml")){
							PlayerDataFile data = new PlayerDataFile(file.getName().split("\\.")[0]);
							
							for (String worlds: data.getConfig().getConfigurationSection(file.getName().split("\\.")[0]).getKeys(false)){
								List<?> items = data.getConfig().getList(file.getName().split("\\.")[0] + "." + worlds + ".inventory");
								data.getConfig().set(file.getName().split("\\.")[0] + "." + worlds + ".main.inventory", Arrays.asList(items.toArray()));
								data.getConfig().set(file.getName().split("\\.")[0] + "." + worlds + ".main.helm", data.getConfig().getItemStack(file.getName().split("\\.")[0] + "." + worlds + ".helm"));
								data.getConfig().set(file.getName().split("\\.")[0] + "." + worlds + ".main.chestplate", data.getConfig().getItemStack(file.getName().split("\\.")[0] + "." + worlds + ".chestplate"));
								data.getConfig().set(file.getName().split("\\.")[0] + "." + worlds + ".main.leggings", data.getConfig().getItemStack(file.getName().split("\\.")[0] + "." + worlds + ".leggings"));
								data.getConfig().set(file.getName().split("\\.")[0] + "." + worlds + ".main.boots", data.getConfig().getItemStack(file.getName().split("\\.")[0] + "." + worlds + ".boots"));
								data.saveConfig();
								items = null;
							}
						}
					}
					
					sender.sendMessage(colorTranslate("&c&l✔ &aImported uuid: " + file.getName().split("\\.")[0]));
					logConsole("Imported uuid: " + file.getName().split("\\.")[0]);
				}
				ChatUtili.sendTranslatedMessage(sender, "&aImported old inventories.");
				logConsole("Imported old inventories.");
				
			}
			
		});
	}
	
	public String getCardinalDirection(Player player) {
        rotation.put(player, (double) ((player.getLocation().getYaw() - 90) % 360));
        if (rotation.get(player) < 0) {
            rotation.put(player, rotation.get(player) + 360.0);
        }
         if (0 <= rotation.get(player) && rotation.get(player) < 45) {
            return "W";
        } else if (45 <= rotation.get(player) && rotation.get(player) < 135) {
            return "N";        
        } else if (135 <= rotation.get(player) && rotation.get(player) < 225) {
            return "E";
        } else if (225 <= rotation.get(player) && rotation.get(player) < 315) {
            return "S";
        } else if (315 <= rotation.get(player) && rotation.get(player) < 360) {
            return "W";
        } else {
            return null;
        }
	}
	
	@SuppressWarnings("unchecked")
	public Inventory getWorldEnderChest(Player player, World world){
		if (playerDataFile.containsKey(player) == false){
			playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		}
		
		if (playerDataFile.get(player).getConfig().getList(getWorldSettingValue(world.getName(), "world-enderchest-bind") + ".main.enderchest.items") == null){
			echest.put(player, Bukkit.createInventory(null, 27, "Ender Chest"));
			return echest.get(player);
		}
		
		echest.put(player, Bukkit.createInventory(null, playerDataFile.get(player).getConfig().getInt(getWorldSettingValue(world.getName(), "world-enderchest-bind") + ".main.enderchest.size"), "Ender Chest"));
		
		echestItems.put(player, (List<ItemStack>) playerDataFile.get(player).getConfig().getList(getWorldSettingValue(world.getName(), "world-enderchest-bind") + ".main.enderchest.items"));
		
		for (loop2.put(player, 0); loop2.get(player) < echestItems.get(player).size(); loop2.put(player, loop2.get(player) + 1)){
			echest.get(player).setItem(loop2.get(player), echestItems.get(player).get(loop2.get(player)));
		}
		
		return echest.get(player);
	}
	
	public void playOpenEnderChestAnimation(final Player player, final Location loc){
		timer.registerNewRepeatingTimer(new TimerTask() {
			
			@Override
			public void run() {
				
				if (BukkitWorldControlEvent.cancelBlockAnimation.containsKey(player)){
					if (BukkitWorldControlEvent.cancelBlockAnimation.get(player)){
						cancel();
					}
					else {
						for (Player pl: Bukkit.getServer().getOnlinePlayers()){
							PacketOutBlockAction packet = new PacketOutBlockAction();
							packet.sendBlockAction(pl, loc, "ENDER_CHEST", 1, 1);
						}
					}
				}
				else {
					cancel();
				}
				
			}
		}, 12, 13);
	}
	
	public int excuteJavaThread(Runnable runnable){
		int rnd = new Random().nextInt(999999);
		threads.put(rnd, new Thread(runnable));
		threads.get(rnd).start();
		try {
			threads.get(rnd).join();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rnd;
	}
	
	public void generateCustomWorld(CommandSender output, String newWorld, WorldGenerator gen){
		if (worldFolderExists(newWorld)){
			ChatUtili.sendTranslatedMessage(output, "&cCannot generate world '" + newWorld + "' in an existing world!");
			return;
		}
		
		WorldCreator c = new WorldCreator(newWorld);
		switch (gen){
			case PlotMe_DefaultGenerator:
				c.generator(gen.name().replaceAll("_", "-"));
				c.environment(Environment.NORMAL);
				c.createWorld();
				WorldData data = new WorldData(newWorld);
				data.setGenerator(gen.name());
				ChatUtili.sendTranslatedMessage(output, "&aGenerated plotme world '" + newWorld + "'.");
			break;
		}
		
	}
	
	public void forceGenerateCustomWorld(CommandSender output, String newWorld, String gen){
		if (worldFolderExists(newWorld)){
			ChatUtili.sendTranslatedMessage(output, "&cCannot generate world '" + newWorld + "' in an existing world!");
			return;
		}
		
		try {
			WorldCreator c = new WorldCreator(newWorld);
			c.generator(gen);
			c.createWorld();
			WorldData data = new WorldData(newWorld);
			data.setGenerator(gen);
			ChatUtili.sendTranslatedMessage(output, "&aGenerated " + gen + " world '" + newWorld + "'.");
		}
		catch (Exception e){
			ChatUtili.sendSimpleTranslatedMessage(output, "&cFailed to generate world '" + newWorld + "'.");
		}
	}
	
	public List<String> getSupportedWorldGenerators(){
		List<String> gens = new ArrayList<String>();
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlotMe") && Bukkit.getServer().getPluginManager().isPluginEnabled("PlotMe-DefaultGenerator")){
			gens.add("PlotMe-DefaultGenerator");
		}
		return gens;
	}
	
	public void modifyWorldEnviorment(String world, Environment env, WorldType type){
		WorldDataFile.getCustomConfig().set(world + ".env", env.name().toUpperCase());
		WorldDataFile.getCustomConfig().set(world + ".type", type.name().toUpperCase());
		WorldDataFile.saveCustomConfig();
	}
	
	public void modifyWorldEnviorment(String world, Environment env, WorldType type, String gen){
		WorldDataFile.getCustomConfig().set(world + ".env", env.name().toUpperCase());
		WorldDataFile.getCustomConfig().set(world + ".type", type.name().toUpperCase());
		WorldDataFile.getCustomConfig().set(world + ".generator", gen);
		WorldDataFile.saveCustomConfig();
	}
	
	public void generateWorldData(World world){
		WorldDataFile.getCustomConfig().set(world.getName() + ".env", world.getEnvironment().name().toUpperCase());
		WorldDataFile.getCustomConfig().set(world.getName() + ".type", world.getWorldType().name().toUpperCase());
		WorldDataFile.getCustomConfig().set(world.getName() + ".seed", world.getSeed());
		WorldDataFile.getCustomConfig().set(world.getName() + ".generate-strut", world.canGenerateStructures());
		WorldDataFile.saveCustomConfig();
	}
	
	public void setPlayerStat(Player player, String world, PlayerStat stat){
		playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		
		switch (stat) {
			case HEALTH:
				if (playerDataFile.get(player).getConfig().getString("health.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current") != null){
					player.setHealth(playerDataFile.get(player).getConfig().getDouble("health.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current"));
					player.setMaxHealth(playerDataFile.get(player).getConfig().getDouble("health.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".max"));
				}
				else {
					player.setHealth(player.getMaxHealth());
				}
			break;

			case HUNGER:
				if (playerDataFile.get(player).getConfig().getString("hunger.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current") != null){
					player.setFoodLevel(playerDataFile.get(player).getConfig().getInt("hunger.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current"));
				}
				else {
					player.setFoodLevel(20);
				}
			break;
			
			case EXP:
				if (playerDataFile.get(player).getConfig().getString("exp.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current-level") != null){
					player.setExp((float) playerDataFile.get(player).getConfig().getDouble("exp.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".boost"));
					player.setLevel(playerDataFile.get(player).getConfig().getInt("exp.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current-level"));
				}
				else {
					player.setLevel(0);
					player.setExp(0);
					player.setTotalExperience(0);
				}
			break;
			
			case POTION:
				playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
				if (playerDataFile.get(player).getConfig().getString("potion.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current") != null){
					removePotionEffects(player);
					playerPotions.put(player, playerDataFile.get(player).getConfig().getStringList("potion.main." + getWorldSettingValue(world, "world-playerstats-bind")  + ".current"));
					
					for (j.put(player, 0); j.get(player) < playerPotions.get(player).size(); j.put(player, j.get(player) + 1)){
						player.addPotionEffect(
						new PotionEffect(
						PotionEffectType.getByName(playerPotions.get(player).get(j.get(player)).split("\\#")[0].toUpperCase()), 
						Integer.parseInt(playerPotions.get(player).get(j.get(player)).split("\\#")[1]), 
						Integer.parseInt(playerPotions.get(player).get(j.get(player)).split("\\#")[2])));
					}
					
					playerPotions.get(player).clear();
				}
				else {
					removePotionEffects(player);
				}
			break;
			
		default:
			
			break;
		}
	}
	
	public void removePotionEffects(final Player player){
		excuteJavaThread(new Runnable(){

			@Override
			public void run() {
				
				while (player.getActivePotionEffects().iterator().hasNext()){
					player.removePotionEffect(player.getActivePotionEffects().iterator().next().getType());
				}
				
			}
			
		});
	}
	
	public List<Player> getPlayerCollection(Player player, String[] world){
		playerCollection.put(player.getName(), new ArrayList<Player>());
		
		for (k.put(player, 0); k.get(player) < world.length; k.put(player, k.get(player) + 1)){	
			if (worldExists(world[k.get(player)])){
				playerCollection.get(player.getName()).addAll(Bukkit.getWorld(world[k.get(player)]).getPlayers());
			}
		}
		
		return playerCollection.get(player.getName());
	}
	
	public void logConsole(String message){
		getWorldControl().getLogger().info(message);
	}
}

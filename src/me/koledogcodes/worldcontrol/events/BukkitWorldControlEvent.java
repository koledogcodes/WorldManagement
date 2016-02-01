package me.koledogcodes.worldcontrol.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TravelAgent;
import org.bukkit.World.Environment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.potion.PotionEffect;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.MessageType;
import me.koledogcodes.worldcontrol.api.PlayerStat;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.PlayerDataFile;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutTitle;

public class BukkitWorldControlEvent implements Listener {
	
	public BukkitWorldControlEvent(WorldControl i) {
		
	}

	private WorldControlHandler WorldControl = new WorldControlHandler();
	private HashMap<Player, String> customJoinMessage = new HashMap<Player, String>();
	HashMap<Player, PlayerDataFile> playerDataFile = new HashMap<Player, PlayerDataFile>();
	public static HashMap<Player, Boolean> cancelBlockAnimation = new HashMap<Player, Boolean>();
	private HashMap<Player, Integer> loop = new HashMap<Player, Integer>();
	private HashMap<Player, List<String>> potions = new HashMap<Player, List<String>>();
	
	//TODO Setup Player DataFile [CONFIGURATION]
	@EventHandler 
	public void onPlayerJoinGameSetFile(PlayerJoinEvent e){
		Player player = e.getPlayer();

		if (player.hasPermission("worldcontrol.admin.*")){
			WorldControl.messagePluginUpdate(player);
		}
		
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.inventory") == false 
		&& ConfigFile.getCustomConfig().getBoolean("Per-World.enderchest") == false 
		&& ConfigFile.getCustomConfig().getBoolean("Per-World.health") == false 
		&& ConfigFile.getCustomConfig().getBoolean("Per-World.hunger") == false 
		&& ConfigFile.getCustomConfig().getBoolean("Per-World.exp") == false 
		&& ConfigFile.getCustomConfig().getBoolean("Per-World.potion") == false){ return; }
		if (playerDataFile.containsKey(player) == false){
			playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		}
	}
	
	//TODO Save Player File, Set Player Inventory [CONFIGURATION]
	@EventHandler 
	public void onPlayerChangeModeSaveInv(PlayerGameModeChangeEvent e){
		try {
		Player player = e.getPlayer();
		
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.inventory") == false){ return; }
		
		if (playerDataFile.containsKey(player) == false){
			playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		}
		
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.inventory-per-gamemode")){
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".inventory", player.getInventory().getContents());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".helm", player.getInventory().getHelmet());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".chestplate", player.getInventory().getChestplate());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".leggings", player.getInventory().getLeggings());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".boots", player.getInventory().getBoots());
			playerDataFile.get(player).saveConfig();
			
			WorldControl.setCurrentWorldInventory(player, e.getNewGameMode());
		}
	}
	catch (Exception exc){
		
	}
	}
	
	//TODO Save Player File, Set Player Inventory [CONFIGURATION]
	@EventHandler 
	public void onPlayerChangeWorldSaveInv(PlayerChangedWorldEvent e){
		try {
		Player player = e.getPlayer();
		
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.inventory") == false){ return; }
		
		if (playerDataFile.containsKey(player) == false){
			playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		}
		
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.inventory-per-gamemode")){
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".inventory", player.getInventory().getContents());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".helm", player.getInventory().getHelmet());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".chestplate", player.getInventory().getChestplate());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".leggings", player.getInventory().getLeggings());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + "." + player.getGameMode().name().toLowerCase() + ".boots", player.getInventory().getBoots());
			playerDataFile.get(player).saveConfig();
			
			WorldControl.setCurrentWorldInventory(player, player.getGameMode());
		}
		else {
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + ".main.inventory", player.getInventory().getContents());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + ".main.helm", player.getInventory().getHelmet());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + ".main.chestplate", player.getInventory().getChestplate());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + ".main.leggings", player.getInventory().getLeggings());
			playerDataFile.get(player).getConfig().set(player.getUniqueId().toString() + "." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-inventory-bind") + ".main.boots", player.getInventory().getBoots());
			playerDataFile.get(player).saveConfig();
			
			WorldControl.setCurrentWorldInventory(player, null);
		}
	}
	catch (Exception exc){
		
	}
	}
	
	//TODO PvP [SETTING]
	@EventHandler 
	public void onPlayerPVP(EntityDamageByEntityEvent e){
	  if (e.getEntity() instanceof Player && e.getDamager() instanceof Player){
		if (((Player) e.getEntity()).hasPermission("worldcontrol.override.*")){ return; }
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
		  if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "pvp")){
			  return;
		  }
		  else {
			  e.setCancelled(true);
		  }
		}
	  }
	}
	
	//TODO Build [SETTING]
	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler 
	public void onPlayerBlockPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		List<Object> blacklist = (List<Object>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "block-place-list");
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			  if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "build")){
				  if (blacklist.contains("-" + e.getBlock().getTypeId()) || blacklist.contains("-" + e.getBlock().getType().name())){
					  ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLACE_BLOCK_DENY).replace("{BLOCK}", e.getBlock().getType().name()));
					  e.setCancelled(true);
				  }
				  else {
					  return;
				  }
			  }
			  else {
				  if (blacklist.contains(e.getBlock().getTypeId()) || blacklist.contains(e.getBlock().getType().name())){
					  return;
				  }
				  else {
					 ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLACE_BLOCK_DENY).replace("{BLOCK}", e.getBlock().getType().name()));
					 e.setCancelled(true);  
				  }
			  }
		}
	}
	
	//TODO Build [SETTING] (2)
	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler 
	public void onPlayerBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		List<Object> blacklist = (List<Object>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "block-break-list");
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			  if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "build")){
				  if (blacklist.contains("-" + e.getBlock().getTypeId()) || blacklist.contains("-" + e.getBlock().getType().name())){
					  ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.BREAK_BLOCK_DENY).replace("{BLOCK}", e.getBlock().getType().name()));
					  e.setCancelled(true);
				  }
				  else {
					  return;
				  }
			  }
			  else {
				  if (blacklist.contains(e.getBlock().getTypeId()) || blacklist.contains(e.getBlock().getType().name())){
					  return;
				  }
				  else {
					 ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.BREAK_BLOCK_DENY).replace("{BLOCK}", e.getBlock().getType().name()));
					 e.setCancelled(true);  
				  }
			  }
		}
	}
	
	//TODO Weather Locked [SETTING]
	@EventHandler 
	public void onWeatherChange(WeatherChangeEvent e){
		if (WorldControl.worldContainsSettings(e.getWorld().getName())){
			  if ((boolean) WorldControl.getWorldSettingValue(e.getWorld().getName(), "weather-locked")){
				  e.setCancelled(true);
				  
			  }
			  else {
				  return; 
			  }
		}
	}
	
	//TODO Mob Spawn [SETIING]
	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler 
	public void onMobSpawn(CreatureSpawnEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			  if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mob-spawn")){
				  if (((List<String>) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mob-spawn-list")).contains(String.valueOf("-" + e.getEntity().getType().getTypeId())) || ((List<String>) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mob-spawn-list")).contains("-" + e.getEntity().getType().name())){
					  e.setCancelled(true); 
				  }
				  else {
					  return;
				  } 
			  }
			  else {
				  if (((List<String>) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mob-spawn-list")).contains(String.valueOf(e.getEntity().getType().getTypeId())) || ((List<String>) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mob-spawn-list")).contains(e.getEntity().getType().name())){
					  return;
				  }
				  else {
					 e.setCancelled(true);  
				  }
			  }
		}
	}
	
	//TODO Player Limit [SETTING]
	@EventHandler 
	public void onPlayerTeleport(PlayerTeleportEvent e){
		Player player = e.getPlayer();
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (WorldControl.worldContainsSettings(e.getTo().getWorld().getName())){
			if (((int) WorldControl.getWorldSettingValue(e.getTo().getWorld().getName(), "player-limit")) == -1){
				return;	
			}
			
			if ((e.getTo().getWorld().getPlayers().size() + 1) > ((int) WorldControl.getWorldSettingValue(e.getTo().getWorld().getName(), "player-limit"))){
				ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, e.getTo().getWorld(), MessageType.PLAYERLIMIT_DENY).replace("{PREVIOUS}", e.getFrom().getWorld().getName()));
				e.setCancelled(true);
				return;
			}
			else {
				if (WorldControlHandler.tpSuccecs.containsKey(player)){
					if (WorldControlHandler.tpSuccecs.get(player)){
						WorldControlHandler.tpSuccecs.remove(player);
					}
				}
			}
		}
	}
	
	//TODO Player Limit [SETTING] (2)
	@EventHandler 
	public void onPlayerJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((int) WorldControl.getWorldSettingValue(player.getWorld().getName(), "player-limit") <= -1){
			return;	
			}
			
			if ((player.getWorld().getPlayers().size() - 0) >= (int) WorldControl.getWorldSettingValue(player.getWorld().getName(), "player-limit")){
				player.teleport(Bukkit.getWorld(WorldControl.getWorldSettingValue(player.getWorld().getName(), "fallback-world").toString()).getSpawnLocation());
				ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLAYERLIMIT_DENY).replace("{PREVIOUS}", player.getWorld().getName()));
			}
		}
	}
	
	//TODO Player Command [SETTING]
	@SuppressWarnings("unchecked")
	@EventHandler 
	public void onPlayerEnterCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("worldcontrol") || e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("wc") || e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("worldc") || e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("wcontrol")){
			return;
		}
		
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
		  if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "commands-allowed")){
				if (((List<String>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "cmd-allowed-list")).contains("-" + e.getMessage().replace("/", "").split(" ")[0])){
					e.setCancelled(true);
					ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLAYER_CMD_DENY).replace("{COMMAND}", e.getMessage()));
				}
				else {
					return;
				}
		  }
		  else {
			if (((List<String>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "cmd-allowed-list")).contains(e.getMessage().replace("/", "").split(" ")[0])){
				return;
			}
			else {
				e.setCancelled(true);
				ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLAYER_CMD_DENY).replace("{COMMAND}", e.getMessage()));
			}
		  }
		}
	}
	
	//TODO Invincible [SETTING]
	@EventHandler 
	public void onPlayerDamage(EntityDamageEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			if (e.getEntity() instanceof Player){
				Player player = (Player) e.getEntity();
				if (player.hasPermission("worldcontrol.override.*")){ return; }
				if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "players-invincible")){
					e.setCancelled(true);
				}
			}
			else {
				if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mobs-invincible")){
					e.setCancelled(true);
				}
			}
		}
	}
	
	//TODO Entity Death [SETTING]
	@EventHandler 
	public void onEntityDeath(EntityDeathEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			if (e.getEntity() instanceof Player){
				if (((Player) e.getEntity()).hasPermission("worldcontrol.override.*")){ return; }
				if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "players-drop-loot")){
					
				}
				else {
					e.getDrops().clear();
				}
				
				if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "players-drop-exp")){
					return;
				}
				else {
					e.setDroppedExp(0);
				}
			}
			else {
				if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mobs-drop-loot")){
					
				}
				else {
					e.getDrops().clear();
				}
				
				if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mobs-drop-exp")){
					return;
				}
				else {
					e.setDroppedExp(0);
				}
			}
		}
	}
	
	//TODO Player Chat [SETTING]
	@EventHandler 
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "chat")){
				return;
			}
			else {
				e.setCancelled(true);
				ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLAYER_CHAT_DENY).replace("{MESSAGE}", e.getMessage()));
			}
		}
	}
	
	//TODO WhiteList [SETTING]
	@EventHandler 
	public void onPlayerTeleport2(PlayerTeleportEvent e){
	Player player = e.getPlayer();
	if (player.hasPermission("worldcontrol.override.*")){ return; }
	if (WorldControl.worldWhitelistIsEnabled(e.getTo().getWorld().getName())){
		if (WorldControl.worldWhiteListHasPlayer(player.getName(), e.getTo().getWorld().getName()) == false){
			e.setCancelled(true);
			ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, e.getTo().getWorld(), MessageType.PLAYER_WHITELIST_DENY).replace("{PREVIOUS}", e.getFrom().getWorld().getName()));
		}
		else {
			return;
		}
	}
	}
	
	//TODO Limit mobs per world [SETTING]
	@EventHandler
	public void onMobSpawnLimit(CreatureSpawnEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			if (e.getEntity().getWorld().getLivingEntities().size() > (int) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mob-limit")){
				e.setCancelled(true);
			}
		}
	}
	
	//TODO Player Interact [SETTING]
	@EventHandler 
	public void onPlayerInteract(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "player-interact")){
				return;
			}
			else {
				e.setCancelled(true);
				ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLAYER_INTERACT_DENY).replace("{ITEM}", player.getItemInHand().getType().name()));
			}
		}
	}
	
	//TODO Explode [SETTING]
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "explosion")){
				return;
			}
			else {
				e.setCancelled(true);
			}
		}
	}
	
	//TODO Nether Portal Teleport [SETTING]
	@EventHandler 
	public void onPlayerPortalTeleport(PlayerPortalEvent e){
		Player player = e.getPlayer();
		if (e.getCause().equals(TeleportCause.NETHER_PORTAL)){
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "nether-portal-teleport")){
				try {
					e.useTravelAgent(true);
					
					if (player.getWorld().getEnvironment().equals(Environment.NORMAL)){
						TravelAgent travel = e.getPortalTravelAgent();
						Location newLoc = player.getLocation().clone();
						newLoc.setWorld(Bukkit.getWorld((String) WorldControl.getWorldSettingValue(player.getWorld().getName(), "nether-world")));
						travel.setSearchRadius(ConfigFile.getCustomConfig().getInt("Nether-Portals.overworld-nether-serach-raduis"));
						travel.findOrCreate(newLoc);
						
						e.useTravelAgent(true);
						e.setPortalTravelAgent(travel);
						e.setTo(newLoc);
					}	
					else if (player.getWorld().getEnvironment().equals(Environment.NETHER)){
						TravelAgent travel = e.getPortalTravelAgent();
						Location newLoc = player.getLocation().clone();
						newLoc.setWorld(Bukkit.getWorld((String) WorldControl.getWorldSettingValue(player.getWorld().getName(), "overworld-world")));
						travel.setSearchRadius(ConfigFile.getCustomConfig().getInt("Nether-Portals.nether-overworld-serach-raduis"));
						travel.findOrCreate(newLoc);
						
						e.useTravelAgent(true);
						e.setPortalTravelAgent(travel);
						e.setTo(newLoc);
					}
					else {
						
					}
				}
				catch (Exception exc){
					ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.ALTERNATE_WORLD_DENY));
				}
				return;
			}
			else {
				e.setCancelled(true);
				ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.NETHER_TELEPORT_DENY));			}
		}
	   }
		else if (e.getCause().equals(TeleportCause.END_PORTAL)){
			//CODE WILL BE HERE LATER
			return;
		}
		else {
			return;
		}
	}
	
	//TODO Nether Portal Create [SETTING]
	@EventHandler 
	public void onPlayerPortalCreate(PortalCreateEvent e){

		if (WorldControl.worldContainsSettings(e.getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(e.getWorld().getName(), "nether-portal-can-create")){
				return;
			}
			else {
				e.setCancelled(true);
			}
		}
	}
	
	//TODO World Title Join [FEATURE]
	@EventHandler 
	public void onPlayerChangeWorld(PlayerChangedWorldEvent e){
		try {
		Player player = e.getPlayer();
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "title-join")){
				if (PacketHandler.getBukkitVersion().contains("v1_8")){
					
					if (((String) WorldControl.getWorldSettingValue(player.getWorld().getName(), "title-join-message-main")).equalsIgnoreCase("none") == false){
					customJoinMessage.put(player, WorldControl.constructCustomJoinMessage(player, e.getPlayer().getWorld(), (String) WorldControl.getWorldSettingValue(player.getWorld().getName(), "title-join-message-main")));
					PacketOutTitle title = new PacketOutTitle();
					int displayTime = (int) WorldControl.getWorldSettingValue(player.getWorld().getName(), "title-join-message-main-display-time") * 20;
					title.sendTitle(player, customJoinMessage.get(player), displayTime, displayTime, displayTime);
					}
					
					if (((String) WorldControl.getWorldSettingValue(player.getWorld().getName(), "title-join-message-sub")).equalsIgnoreCase("none") == false){
					customJoinMessage.put(player, WorldControl.constructCustomJoinMessage(player, e.getPlayer().getWorld(), (String) WorldControl.getWorldSettingValue(player.getWorld().getName(), "title-join-message-sub")));
					PacketOutTitle title = new PacketOutTitle();
					int displayTime = (int) WorldControl.getWorldSettingValue(player.getWorld().getName(), "title-join-message-sub-display-time") * 20;
					title.sendSubtitle(player, customJoinMessage.get(player), displayTime, displayTime, displayTime);
					}
					
				}
				else {
					return;
				}
			}
			else {
				return;
			}
		}
	}
	catch (Exception exc){
		
	}
	}
	
	//TODO World Permission
	@EventHandler 
	public void onPlayerTeleportRestrict(PlayerTeleportEvent e){
		Player player = e.getPlayer();
		if (player.hasPermission("worldcontrol.override.*")){ return; }
		if (e.getFrom().getWorld().getName().equalsIgnoreCase(e.getTo().getWorld().getName())){ return; }
		
			if (player.hasPermission("worldcontrol.world.*") == false){
				if (player.hasPermission("worldcontrol.world." + e.getTo().getWorld().getName())){
					return;
				}
				else {
					e.setCancelled(true);
					ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, e.getTo().getWorld(), MessageType.PLAYER_WORLD_PERMISSION_DENY).replace("{PREVIOUS}", e.getFrom().getWorld().getName()).replace("{CURRENT}", player.getWorld().getName()).replace("{PERMISSION}", "worldcontrol.world." + e.getTo().getWorld().getName()));			
				}
			}
	}
	
	//TODO Default World GameMode [SETTING]
	@EventHandler 
	public void onPlayerTeleportChangeWorld(PlayerChangedWorldEvent e){
		try {
			Player player = e.getPlayer();
			if (player.hasPermission("worldcontrol.override.*")){ return; }
			player.setGameMode(GameMode.valueOf(WorldControl.getWorldSettingValue(player.getWorld().getName(), "default-gamemode").toString().toUpperCase()));
		}
		catch (Exception exc){
			
		}
	}
	
	//TODO EnderChest Override Bukkit [CONFIGURATION]
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerClickEnderChest(PlayerInteractEvent e){
		Player player = e.getPlayer();
		
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.enderchest") == false){ return; }
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK){ return; }
		
		if (e.getClickedBlock().getType() == Material.ENDER_CHEST){
			e.setCancelled(true);
			cancelBlockAnimation.put(player, false);
			WorldControl.playOpenEnderChestAnimation(player, e.getClickedBlock().getLocation());
			player.openInventory(WorldControl.getWorldEnderChest(player, player.getWorld()));
			
			return;
		}
	}
	
	//TODO EnderChest Save [CONFIGURATION]
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEnderChestClose(InventoryCloseEvent e){
			if (e.getInventory().getName().equalsIgnoreCase(ChatColor.stripColor("Ender Chest"))){
				Player player = (Player) e.getPlayer();
				
				if (ConfigFile.getCustomConfig().getBoolean("Per-World.enderchest") == false){ return; }
				
				cancelBlockAnimation.put(player, true);
				
				if (playerDataFile.containsKey(player) == false){
					playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
				}
				
				playerDataFile.get(player).getConfig().set(WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-enderchest-bind") + ".main.enderchest.size", e.getInventory().getSize());
				playerDataFile.get(player).getConfig().set(WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-enderchest-bind") + ".main.enderchest.items", e.getInventory().getContents());
				playerDataFile.get(player).saveConfig();
			}
			else {
				return;
			}

	}
	
	//TODO Set EnderChest contents [CONFIGURATION]
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEnderChestOpen(InventoryOpenEvent e){
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.enderchest") == false){ return; }
		
		if (e.getInventory().getName().equalsIgnoreCase(ChatColor.stripColor("Ender Chest"))){
			Player player = (Player) e.getPlayer();
			
			WorldControl = new WorldControlHandler();
			e.getInventory().setContents(WorldControl.getWorldEnderChest(player, player.getWorld()).getContents());
		}
		else {
			return;
		}
	}
	
	//TODO Leaves Decay [SETTING]
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent e){
		if (WorldControl.worldContainsSettings(e.getBlock().getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(e.getBlock().getWorld().getName(), "leaves-decay")){
				return;
			}
			else {
				e.setCancelled(true);
			}
		}
	}
	
	//TODO Saturation Loss [SETTING]
	@EventHandler
	public void onSaturationChange(FoodLevelChangeEvent e){
		if (e.getEntity() instanceof LivingEntity == false) { return; }
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "no-hunger")){
				e.setCancelled(true);
			}
			else {
				return;
			}
		}
	}
	
	//TODO Liquid Flow [SETTING]
	@EventHandler
	public void onLiquidFlow(BlockFromToEvent e){
		if (WorldControl.worldContainsSettings(e.getBlock().getWorld().getName())){
			if (e.getBlock().getType() == Material.STATIONARY_WATER){
				if (((boolean) WorldControl.getWorldSettingValue(e.getBlock().getWorld().getName(), "water-flow")) == false){
					e.setCancelled(true);
				}
			}
			
			if (e.getBlock().getType() == Material.STATIONARY_LAVA){
				if (((boolean) WorldControl.getWorldSettingValue(e.getBlock().getWorld().getName(), "lava-flow")) == false){
					e.setCancelled(true);
				}
			}

		}
	}
	
	//TODO Can Eat [SETTING]
	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler 
	public void onPlayerEat(PlayerItemConsumeEvent e){
		Player player = e.getPlayer();
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "can-eat")){
				if (((List<String>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "can-eat-list")).contains("-" + e.getItem().getType().name()) || ((List<String>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "can-eat-list")).contains(String.valueOf("-" + e.getItem().getTypeId()))){
					e.setCancelled(true);
					ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLAYER_EAT_DENY).replace("{ITEM}", e.getItem().getType().name()));			
				}
				
				return;
			}
			else {
				if (((List<String>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "can-eat-list")).contains(e.getItem().getType().name()) || ((List<String>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "can-eat-list")).contains(String.valueOf(e.getItem().getTypeId()))){
					return;
				}
				else {
					e.setCancelled(true);
					ChatUtili.sendSimpleTranslatedMessage(player, WorldControl.replaceBasicPlaceholders(player, player.getWorld(), MessageType.PLAYER_EAT_DENY).replace("{ITEM}", e.getItem().getType().name()));			
				}
			}
		}
	}
	
	//TODO Player Stat's [SETTING]
	@EventHandler
	public void onPlayerChangeWorldStats(PlayerChangedWorldEvent e){
		if (WorldControl.worldContainsSettings(e.getPlayer().getWorld().getName())){
			Player player = e.getPlayer();
			
			if (playerDataFile.containsKey(player) == false){
				playerDataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
			}
			
			if (ConfigFile.getCustomConfig().getBoolean("Per-World.health")){
				playerDataFile.get(player).getConfig().set("health.main." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-playerstats-bind") + ".current", player.getHealth());
				playerDataFile.get(player).getConfig().set("health.main." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-playerstats-bind") + ".max", player.getMaxHealth());
				playerDataFile.get(player).saveConfig();
				
				WorldControl.setPlayerStat(player, player.getWorld().getName(), PlayerStat.HEALTH);
			}
			
			if (ConfigFile.getCustomConfig().getBoolean("Per-World.hunger")){
				playerDataFile.get(player).getConfig().set("hunger.main." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-playerstats-bind") + ".current", player.getFoodLevel());
				playerDataFile.get(player).saveConfig();
				
				WorldControl.setPlayerStat(player, player.getWorld().getName(), PlayerStat.HUNGER);
			}
			
			if (ConfigFile.getCustomConfig().getBoolean("Per-World.xp")){
				playerDataFile.get(player).getConfig().set("exp.main." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-playerstats-bind") + ".current-level", player.getLevel());
				playerDataFile.get(player).getConfig().set("exp.main." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-playerstats-bind") + ".boost", player.getExp());
				playerDataFile.get(player).saveConfig();
				
				WorldControl.setPlayerStat(player, player.getWorld().getName(), PlayerStat.EXP);
			}
			
			if (ConfigFile.getCustomConfig().getBoolean("Per-World.potion")){
				potions.put(player, new ArrayList<String>());
				for (loop.put(player, 0); loop.get(player) < player.getActivePotionEffects().size(); loop.put(player, loop.get(player) + 1)){
					potions.get(player).add(							
					player.getActivePotionEffects().toArray(new PotionEffect[0])[loop.get(player)].getType().getName() + "#" +
					player.getActivePotionEffects().toArray(new PotionEffect[0])[loop.get(player)].getDuration() + "#" + 
					player.getActivePotionEffects().toArray(new PotionEffect[0])[loop.get(player)].getAmplifier());
				}
				
			
				playerDataFile.get(player).getConfig().set("potion.main." + WorldControl.getWorldSettingValue(e.getFrom().getName(), "world-playerstats-bind") + ".current", potions.get(player));
				playerDataFile.get(player).saveConfig();
				
				WorldControl.setPlayerStat(player, player.getWorld().getName(), PlayerStat.POTION);
			}
		}
	}
	
	//TODO Per World Chat [SETTING]
	@EventHandler
	public void onPlayerChatInWorld(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		if (ConfigFile.getCustomConfig().getBoolean("Per-World.chat")){
			if (WorldControl.worldContainsSettings(player.getWorld().getName())){
				e.getRecipients().clear();
				e.getRecipients().addAll(WorldControl.getPlayerCollection(player, WorldControl.getWorldSettingValue(player.getWorld().getName(), "world-chat-bind").toString().split("\\,")));
			}
		}
	}
}

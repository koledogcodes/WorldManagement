package me.koledogcodes.worldcontrol.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;

public class BukkitWorldControlEvent implements Listener {
	
	public BukkitWorldControlEvent(WorldControl i) {
		
	}

	private WorldControlHandler WorldControl = new WorldControlHandler();
	
	//TODO PvP [SETTING]
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerPVP(EntityDamageByEntityEvent e){
	  if (e.getEntity() instanceof Player && e.getDamager() instanceof Player){
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
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerBlockPlace(BlockPlaceEvent e){
		Player player = e.getPlayer();
		List<Object> blacklist = (List<Object>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "certain-blocks-place-allow");

		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			  if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "build")){
				  return;
			  }
			  else {
				  if (blacklist.contains(e.getBlock().getTypeId()) || blacklist.contains(e.getBlock().getType().name())){
					  return;
				  }
				  else {
					ChatUtili.sendTranslatedMessage(player, "&cYou cannot place block '&4" + e.getBlock().getType().name() + "&c' in this world.");
					 e.setCancelled(true);  
				  }
			  }
		}
	}
	
	//TODO Build [SETTING] (2)
	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		List<Object> blacklist = (List<Object>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "certain-blocks-break-allow");
		
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			  if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "build")){
				  return;
			  }
			  else {
				  if (blacklist.contains(e.getBlock().getTypeId()) || blacklist.contains(e.getBlock().getType().name())){
					  return;
				  }
				  else {
					  ChatUtili.sendTranslatedMessage(player, "&cYou cannot break block '&4" + e.getBlock().getType().name() + "&c' in this world.");
					 e.setCancelled(true);  
				  }
			  }
		}
	}
	
	//TODO Weather Locked [SETTING]
	@EventHandler (priority = EventPriority.MONITOR)
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
	@EventHandler (priority = EventPriority.MONITOR)
	public void onMobSpawn(CreatureSpawnEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			  if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "mob-spawn")){
				  return;  
			  }
			  else {
				  if (((List<String>) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "certain-mob-spawn-allow")).contains(e.getEntity().getType().getTypeId()) || ((List<String>) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "certain-mob-spawn-allow")).contains(e.getEntity().getType().name())){
					  return;
				  }
				  else {
					 e.setCancelled(true);  
				  }
			  }
		}
	}
	
	//TODO Player Limit [SETTING]
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent e){
		Player player = e.getPlayer();
		if (WorldControl.worldContainsSettings(e.getTo().getWorld().getName())){
			if (((int) WorldControl.getWorldSettingValue(e.getTo().getWorld().getName(), "player-limit")) == -1){
				ChatUtili.sendTranslatedMessage(player, "&aSuccesfully tped to world '" + player.getWorld().getName() + "' spawn location.");	
				return;	
			}
			
			if ((e.getTo().getWorld().getPlayers().size() + 1) > ((int) WorldControl.getWorldSettingValue(e.getTo().getWorld().getName(), "player-limit"))){
				ChatUtili.sendTranslatedMessage(player, "&cYou cannot teleport to world '" + e.getTo().getWorld().getName() + "' becuase it has reached its player limit.");
				e.setTo(e.getFrom());
				e.setCancelled(true);
				return;
			}
			else {
				if (WorldControlHandler.tpSuccecs.containsKey(player)){
					if (WorldControlHandler.tpSuccecs.get(player)){
						ChatUtili.sendTranslatedMessage(player, "&aSuccesfully tped to world '" + e.getTo().getWorld().getName() + "' spawn location.");	
						WorldControlHandler.tpSuccecs.remove(player);
					}
				}
			}
		}
	}
	
	//TODO Player Limit [SETTING] (2)
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((int) WorldControl.getWorldSettingValue(player.getWorld().getName(), "player-limit") <= -1){
			return;	
			}
			
			if ((player.getWorld().getPlayers().size() - 0) >= (int) WorldControl.getWorldSettingValue(player.getWorld().getName(), "player-limit")){
				player.teleport(Bukkit.getWorld(WorldControl.getWorldSettingValue(player.getWorld().getName(), "fallback-world").toString()).getSpawnLocation());
				ChatUtili.sendTranslatedMessage(player, "&cYou cannot be in world '" + e.getPlayer().getWorld().getName() + "' becuase it has reached its player limit.");
			}
		}
	}
	
	//TODO Player Command [SETTING]
	@SuppressWarnings("unchecked")
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerEnterCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		
		if (e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("worldcontrol") || e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("wc") || e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("worldc") || e.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("wcontrol")){
			return;
		}
		
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
		  if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "commands-allowed")){
			  return;
		  }
		  else {
			if (((List<String>) WorldControl.getWorldSettingValue(player.getWorld().getName(), "certain-commands-use-allow")).contains(e.getMessage().replace("/", "").split(" ")[0])){
				return;
			}
			else {
				e.setCancelled(true);
				ChatUtili.sendTranslatedMessage(player, "&cYou cannot use &4" + e.getMessage() + " &cin this world.");
			}
		  }
		}
	}
	
	//TODO Invincible [SETTING]
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerDamage(EntityDamageEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			if (e.getEntity() instanceof Player){
				Player player = (Player) e.getEntity();
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
	@EventHandler (priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent e){
		if (WorldControl.worldContainsSettings(e.getEntity().getWorld().getName())){
			if (e.getEntity() instanceof Player){
				if ((boolean) WorldControl.getWorldSettingValue(e.getEntity().getWorld().getName(), "players-drop-loot")){
					return;
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
					return;
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
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		if (WorldControl.worldContainsSettings(player.getWorld().getName())){
			if ((boolean) WorldControl.getWorldSettingValue(player.getWorld().getName(), "chat")){
				return;
			}
			else {
				e.setCancelled(true);
				ChatUtili.sendTranslatedMessage(player, "&cYou cannot chat in this world.");
			}
		}
	}
	
	//TODO WhiteList [SETTING]
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerTeleport2(PlayerTeleportEvent e){
	Player player = e.getPlayer();
	
	if (WorldControl.worldWhitelistIsEnabled(e.getTo().getWorld().getName())){
		if (WorldControl.worldWhiteListHasPlayer(player.getName(), e.getTo().getWorld().getName()) == false){
			e.setCancelled(true);
			ChatUtili.sendTranslatedMessage(player, "&cYou cannot tp to world '" + e.getTo().getWorld().getName() + "' that your not whitelisted in.");
		}
		else {
			return;
		}
	}
	}
	
}

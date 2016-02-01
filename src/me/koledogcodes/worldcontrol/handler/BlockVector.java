package me.koledogcodes.worldcontrol.handler;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.koledogcodes.worldcontrol.configs.OriginalBlockDataFile;

public class BlockVector {

	public BlockVector() {
		
	}
	
	private HashMap<Player, Integer> x = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> y = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> z = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> validRollbackOccurances = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> invalidRollbackOccurances = new HashMap<Player, Integer>();
	private HashMap<Player, Location> loc = new HashMap<Player, Location>();

	

	@SuppressWarnings("deprecation")
	public void rollbackBlockFromLocation(Player player, Location start, int radius){
		invalidRollbackOccurances.put(player, 0);
		validRollbackOccurances.put(player, 0);
		
		for (x.put(player, start.getBlockX() - (radius / 2)); x.get(player) <= (start.getBlockX() + (radius / 2)); x.put(player, x.get(player) + 1)){
			for (y.put(player, start.getBlockY() - (radius / 2)); y.get(player) <= (start.getBlockY() + (radius / 2)); y.put(player, y.get(player) + 1)){
				for (z.put(player, start.getBlockZ() - (radius / 2)); z.get(player) <= (start.getBlockZ() + (radius / 2)); z.put(player, z.get(player) + 1)){
					loc.put(player, new Location(player.getWorld(), x.get(player), y.get(player), z.get(player)));
					
					if (OriginalBlockDataFile.getCustomConfig().getString(parseLocationToString(loc.get(player))) == null){
						invalidRollbackOccurances.put(player, invalidRollbackOccurances.get(player) + 1);
						continue; 
					}
					else {
						if (loc.get(player).getBlock().getType().name().equalsIgnoreCase(OriginalBlockDataFile.getCustomConfig().getString(parseLocationToString(loc.get(player)) + ".block")) == false || loc.get(player).getBlock().getData() != OriginalBlockDataFile.getCustomConfig().getInt(parseLocationToString(loc.get(player)) + ".type")){
							loc.get(player).getBlock().setType(Material.valueOf(OriginalBlockDataFile.getCustomConfig().getString(parseLocationToString(loc.get(player)) + ".block")));
							loc.get(player).getBlock().setData((byte) OriginalBlockDataFile.getCustomConfig().getInt(parseLocationToString(loc.get(player)) + ".type"), true);
							loc.get(player).getBlock().getState().update(true);
							validRollbackOccurances.put(player, validRollbackOccurances.get(player) + 1);
						}
						
					}
				}
			}
		}
		
		ChatUtili.sendSimpleTranslatedMessage(player, "&f----- &aRollback Info &f-----");
		ChatUtili.sendTranslatedMessage(player, "&7Skipped Blocks: &c-" + invalidRollbackOccurances.get(player));
		ChatUtili.sendTranslatedMessage(player, "&7Reverted Blocks: &a+" + validRollbackOccurances.get(player));
		ChatUtili.sendTranslatedMessage(player, "&dAll revertable blocks within &5" + radius + "m &dhas been reverted!");
		ChatUtili.sendSimpleTranslatedMessage(player, "&f----------------------");
		
		invalidRollbackOccurances.remove(player);
		validRollbackOccurances.remove(player);
	}
	
	public String parseLocationToString(Location loc){
		return loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
	}
}

package me.koledogcodes.worldcontrol.events;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.configs.BlockDataFile;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;

public class BukkitWorldControlLogEvent implements Listener {
	
	public BukkitWorldControlLogEvent(WorldControl i) {
		
	}

	private WorldControlHandler WorldControl = new WorldControlHandler();
	private HashMap<Player, List<String>> log = new HashMap<Player, List<String>>();
	
	//TODO Player Place Block [LOG]
	@EventHandler
	public void onPlayerPlaceBlockLog(BlockPlaceEvent e){
		Player player = e.getPlayer();
		if (e.canBuild() == false){ return; }
		if (BlockDataFile.getCustomConfig().getString(WorldControl.parseLocationToString(e.getBlockPlaced().getLocation())) == null){
			log.put(player, BlockDataFile.getCustomConfig().getStringList(WorldControl.parseLocationToString(e.getBlockPlaced().getLocation()) + ".placed"));
			log.get(player).add(player.getName() + "#" + System.currentTimeMillis() + "#" + e.getBlockPlaced().getType().name());
			BlockDataFile.getCustomConfig().set(WorldControl.parseLocationToString(e.getBlockPlaced().getLocation()) + ".placed", log.get(player));
		}
		else {
			log.put(player, BlockDataFile.getCustomConfig().getStringList(WorldControl.parseLocationToString(e.getBlockPlaced().getLocation()) + ".placed"));
			log.get(player).add(player.getName() + "#" + System.currentTimeMillis() + "#" + e.getBlockPlaced().getType().name());
			BlockDataFile.getCustomConfig().set(WorldControl.parseLocationToString(e.getBlockPlaced().getLocation()) + ".placed", log.get(player));
		}
	}
	
	//TODO Player Break Block [LOG]
	@EventHandler
	public void onPlayerBreakBlockLog(BlockBreakEvent e){
		Player player = e.getPlayer();
		if (BlockDataFile.getCustomConfig().getString(WorldControl.parseLocationToString(e.getBlock().getLocation())) == null){
			log.put(player, BlockDataFile.getCustomConfig().getStringList(WorldControl.parseLocationToString(e.getBlock().getLocation()) + ".broken"));
			log.get(player).add(player.getName() + "#" + System.currentTimeMillis() + "#" + e.getBlock().getType().name());
			BlockDataFile.getCustomConfig().set(WorldControl.parseLocationToString(e.getBlock().getLocation()) + ".broken", log.get(player));
		}
		else {
			log.put(player, BlockDataFile.getCustomConfig().getStringList(WorldControl.parseLocationToString(e.getBlock().getLocation()) + ".broken"));
			log.get(player).add(player.getName() + "#" + System.currentTimeMillis() + "#" + e.getBlock().getType().name());
			BlockDataFile.getCustomConfig().set(WorldControl.parseLocationToString(e.getBlock().getLocation()) + ".broken", log.get(player));
		}
	}
	
	//TODO Player View BlockData [LOG]
	@EventHandler
	public void onPlayerClickLoggedBlock(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if (WorldControl.isInspector(player) == false){ return; }
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
		WorldControlHandler.blockInspectionLocation.put(player, e.getClickedBlock().getLocation());
		WorldControlHandler.blockInspectionLocationType.put(player, "PLACED");
		WorldControl.messagePlacedBlockInformation(player, e.getClickedBlock(), 1);
		}
		else if (e.getAction() == Action.LEFT_CLICK_BLOCK){
		e.setCancelled(true);
		WorldControlHandler.blockInspectionLocation.put(player, e.getClickedBlock().getLocation());
		WorldControlHandler.blockInspectionLocationType.put(player, "REMOVED");
		WorldControl.messageBrokenBlockInformation(player, e.getClickedBlock(), 1);
		}
		else {
			return;
		}
	}
}

package me.koledogcodes.worldcontrol.wrapped.packets;

import java.lang.reflect.Constructor;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.koledogcodes.worldcontrol.events.BukkitWorldControlEvent;
import me.koledogcodes.worldcontrol.handler.ChatUtili;

public class PacketOutBlockAction {

	public PacketOutBlockAction(){
	}
	
	public void sendBlockAction(Player player, Location loc, String block, int arg0, int arg1){
	try {
		//Version 1.8 and up
		if (Integer.valueOf(PacketHandler.getBukkitVersion().split("\\_")[1]) >= 8){
			Constructor<?> posConstructor = PacketHandler.getNMSClass("BlockPosition").getConstructor(double.class, double.class, double.class);
			Object BlockPos = posConstructor.newInstance(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			
			Object b = PacketHandler.getNMSClass("Blocks").getField(block).get(null);

	        Constructor<?> blockActionConstructor = PacketHandler.getNMSClass("PacketPlayOutBlockAction").getConstructor(BlockPos.getClass(), PacketHandler.getNMSClass("Block"), int.class, int.class);
	        Object packet = blockActionConstructor.newInstance(BlockPos, b, arg0, arg1);
	        
	        PacketHandler.sendPacket(player, packet);
		}
		//Version 1.7 and below
		else {
			BukkitWorldControlEvent.cancelBlockAnimation.put(player, true);
			ChatUtili.sendTranslatedMessage(player, "&cFailed to send block animation packet.");
		}
	} 
	catch (Exception e) {
		e.printStackTrace();
		BukkitWorldControlEvent.cancelBlockAnimation.put(player, true);
		ChatUtili.sendTranslatedMessage(player, "&cFailed to send block animation packet.");
	} 
	}
	
	
	
}

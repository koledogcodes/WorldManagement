package me.koledogcodes.worldcontrol.wrapped.packets;

import java.lang.reflect.Constructor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PacketOutHoverChat {

	public PacketOutHoverChat(){
		
	}
	
	String example = "v1_8_R1";
	
	public void sendHoverMessage(Player player, String firstMessage, String extraHoverableMessage, String hoverMessageDialog){
		if (Integer.valueOf(PacketHandler.getBukkitVersion().split("\\_")[1].replaceAll("R", "")) >= 8 && Integer.valueOf(PacketHandler.getBukkitVersion().split("\\_")[2].replaceAll("R", "")) >= 2){
			try {
				firstMessage = ChatColor.translateAlternateColorCodes('&', firstMessage);
				extraHoverableMessage = ChatColor.translateAlternateColorCodes('&', extraHoverableMessage);
				hoverMessageDialog = ChatColor.translateAlternateColorCodes('&', hoverMessageDialog);
		        Object chat = PacketHandler.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + firstMessage + " \",\"extra\":[{\"text\":\"" + extraHoverableMessage + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessageDialog + "\"}}]}");
		        Constructor<?> hoverMessageConstructor = PacketHandler.getNMSClass("PacketPlayOutChat").getConstructor(PacketHandler.getNMSClass("IChatBaseComponent"), byte.class);
		        Object packet = hoverMessageConstructor.newInstance(chat, (byte) 1);
		        PacketHandler.sendPacket(player, packet);
			} 
			catch (Exception e) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', firstMessage + " &c1.8 (Hover &cFailed)"));
			}
		}
		else {
			
	        try {
				firstMessage = ChatColor.translateAlternateColorCodes('&', firstMessage);
				extraHoverableMessage = ChatColor.translateAlternateColorCodes('&', extraHoverableMessage);
				hoverMessageDialog = ChatColor.translateAlternateColorCodes('&', hoverMessageDialog);
				
				Constructor<?> hoverMessageConstructor = PacketHandler.getNMSClass("PacketPlayOutChat").getConstructor(PacketHandler.getNMSClass("IChatBaseComponent"));
				Object chatSer = PacketHandler.getNMSClass("ChatSerializer").getMethod("a", String.class).invoke(null, "{\"text\":\"" + firstMessage + " \",\"extra\":[{\"text\":\"" + extraHoverableMessage + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessageDialog + "\"}}]}");;
				
				Object packet = hoverMessageConstructor.newInstance(chatSer);
				PacketHandler.sendPacket(player, packet);
	        } 
	        catch (Exception e) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', firstMessage + " &c1.7 (Hover &cFailed)"));

			} 
		}	
	}	
	
	
}

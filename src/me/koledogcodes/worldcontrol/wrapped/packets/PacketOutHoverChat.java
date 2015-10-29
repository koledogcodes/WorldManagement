package me.koledogcodes.worldcontrol.wrapped.packets;

import java.lang.reflect.Constructor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PacketOutHoverChat {

	public PacketOutHoverChat(){
	}
	
	public void sendHoverMessage(Player player, String firstMessage, String extraHoverableMessage, String hoverMessageDialog){
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
		e.printStackTrace();
	} 
	}	
	
	public void sendOnlyHoverMessage(Player player, String extraHoverableMessage, String hoverMessageDialog){
	try {;
		extraHoverableMessage = ChatColor.translateAlternateColorCodes('&', extraHoverableMessage);
		hoverMessageDialog = ChatColor.translateAlternateColorCodes('&', hoverMessageDialog);
        Object chat = PacketHandler.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + "null" + " \",\"extra\":[{\"text\":\"" + extraHoverableMessage + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessageDialog + "\"}}]}".replaceAll("null", ""));
        Constructor<?> hoverMessageConstructor = PacketHandler.getNMSClass("PacketPlayOutChat").getConstructor(PacketHandler.getNMSClass("IChatBaseComponent"), byte.class);
        Object packet = hoverMessageConstructor.newInstance(chat, (byte) 1);
        PacketHandler.sendPacket(player, packet);
	} 
	catch (Exception e) {
		e.printStackTrace();
	} 
	}	
	
	public void sendHoverMessageFirst(Player player, String lastMessage, String HoverableMessage, String hoverMessageDialog){
	try {
		lastMessage = ChatColor.translateAlternateColorCodes('&', lastMessage);
		HoverableMessage = ChatColor.translateAlternateColorCodes('&', HoverableMessage);
		hoverMessageDialog = ChatColor.translateAlternateColorCodes('&', hoverMessageDialog);
        Object chat = PacketHandler.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + lastMessage + " \",\"extra\":[{\"text\":\"" + HoverableMessage + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessageDialog + "\"}}],\"text\":\"" + lastMessage + " \"}");
        Constructor<?> hoverMessageConstructor = PacketHandler.getNMSClass("PacketPlayOutChat").getConstructor(PacketHandler.getNMSClass("IChatBaseComponent"), byte.class);
        Object packet = hoverMessageConstructor.newInstance(chat, (byte) 1);
        PacketHandler.sendPacket(player, packet);
	} 
	catch (Exception e) {
		e.printStackTrace();
	} 
	}
	
}

package me.koledogcodes.worldcontrol.wrapped.packets;

import java.lang.reflect.Constructor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PacketOutTitle {

	public PacketOutTitle(){
	}
	
	public void sendTitle(Player player, String message, int textFadeIn, int textDisplayTime, int textFadeOut){
	try {
		
		Object enumTitle = PacketHandler.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
        Object chat = PacketHandler.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
        Constructor<?> titleConstructor = PacketHandler.getNMSClass("PacketPlayOutTitle").getConstructor(PacketHandler.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], PacketHandler.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
        Object packet = titleConstructor.newInstance(enumTitle, chat, textFadeIn, textDisplayTime, textFadeOut);
        PacketHandler.sendPacket(player, packet);
	} 
	catch (Exception e) {
		e.printStackTrace();
	} 
	}
	
	public void sendSubtitle(Player player, String message, int textFadeIn, int textDisplayTime, int textFadeOut){
	try {
		Object enumTitle = PacketHandler.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
		Object chatComp = PacketHandler.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
		Constructor<?> titleConstruct = PacketHandler.getNMSClass("PacketPlayOutTitle").getConstructor(PacketHandler.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], PacketHandler.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
		Object packet = titleConstruct.newInstance(enumTitle, chatComp, textFadeIn, textDisplayTime, textFadeOut);
		PacketHandler.sendPacket(player, packet);
	} 
	catch (Exception e) {
		e.printStackTrace();
	} 
	}
	
	
}

package me.koledogcodes.worldcontrol.wrapped.packets;

import java.lang.reflect.Constructor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PacketOutClickChat {

	public PacketOutClickChat(){
	}
	
	public void sendClickableCommandMessage(Player player, String firstMessage, String clickableMessage, String command){
	try {
		if (firstMessage != null){firstMessage = ChatColor.translateAlternateColorCodes('&', firstMessage);}
		if (clickableMessage != null){clickableMessage = ChatColor.translateAlternateColorCodes('&', clickableMessage);}
        Object chat = PacketHandler.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + firstMessage + " \",\"extra\":[{\"text\":\"" + clickableMessage + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}}]}");
        Constructor<?> hoverMessageConstructor = PacketHandler.getNMSClass("PacketPlayOutChat").getConstructor(PacketHandler.getNMSClass("IChatBaseComponent"), byte.class);
        Object packet = hoverMessageConstructor.newInstance(chat, (byte) 1);
        PacketHandler.sendPacket(player, packet);
	} 
	catch (Exception e) {
		e.printStackTrace();
	} 
	}
	
	public void sendClickableSuggestMessage(Player player, String firstMessage, String clickableMessage, String suggestCommand){
	try {
		if (firstMessage != null){firstMessage = ChatColor.translateAlternateColorCodes('&', firstMessage);}
		if (clickableMessage != null){clickableMessage = ChatColor.translateAlternateColorCodes('&', clickableMessage);}
        Object chat = PacketHandler.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + firstMessage + " \",\"extra\":[{\"text\":\"" + clickableMessage + "\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + suggestCommand + "\"}}]}");
        Constructor<?> hoverMessageConstructor = PacketHandler.getNMSClass("PacketPlayOutChat").getConstructor(PacketHandler.getNMSClass("IChatBaseComponent"), byte.class);
        Object packet = hoverMessageConstructor.newInstance(chat, (byte) 1);
        PacketHandler.sendPacket(player, packet);
	} 
	catch (Exception e) {
		e.printStackTrace();
	} 
	}
	
	
}

package me.koledogcodes.worldcontrol.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.koledogcodes.worldcontrol.configs.ConfigFile;

public class ChatUtili {

	/* Constructor */
	public ChatUtili() {
	}
	
	public static String messagePrefix = colorConvert(ConfigFile.getCustomConfig().getString("prefix"));
	
	public static void sendTranslatedMessage(CommandSender player, String message){
	if (messagePrefix != null){
		if (player != null){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagePrefix + " " + message));
		}
	}
	else {
		if (player != null){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}
	}
	
	public static void sendSimpleTranslatedMessage(CommandSender player, String message){
		if (player != null){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}
	
	public static String colorConvert(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	/**
	 * Sends a broadcast message, supports color codes.
	 * @param message - Message to be broadcasted
	 */
	public static void sendTranslatedBroadcastMessage(String message){
	Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	
}

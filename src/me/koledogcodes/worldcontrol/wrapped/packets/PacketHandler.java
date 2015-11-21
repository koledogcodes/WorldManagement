package me.koledogcodes.worldcontrol.wrapped.packets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PacketHandler {
	
	/* Send UnHashed Packet */
	public static void sendPacket(Player player, Object packet){
	try {
		if (getBukkitVersion().contains("v1_8")){
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		}
		else if (getBukkitVersion().contains("v1_7")){
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		}
		else {
			player.sendMessage(ChatColor.RED + "Cannot use packets with invalid version.");
		}
	} 
	catch (Exception e){
		player.sendMessage(ChatColor.RED + "Packet failed to send.");
		e.printStackTrace();
	}	
	}
	
	/* Get Bukkit Version Class */
	public static String getBukkitVersion(){
		return Bukkit.getServer().getClass().getName().split("\\.")[3];
	}
	
	/* Find Declared Class */
	public static int findDeclaredClass(String classSearching, String classToFind){
		int index = 0;
		for (int i = 0; i < PacketHandler.getNMSClass(classSearching).getDeclaredClasses().length; i++){
			//System.out.println("[Class] Declared Classes: " + i + ". " + PacketHandler.getNMSClass(classSearching).getDeclaredClasses()[i]);
			if (PacketHandler.getNMSClass(classSearching).getDeclaredClasses()[i].getName().equalsIgnoreCase("net.minecraft.server." + getBukkitVersion() + "." + classSearching + "$" + classToFind)){
				index = i;
				return index;
			}
		}
		return index;
	}
	
	/* Get UnHashed NMS Class */
	public static Class<?> getNMSClass(String string){
	String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
	try {
		return Class.forName("net.minecraft.server." + version + "." + string);
	} 
	catch (ClassNotFoundException e){
		e.printStackTrace();
		System.out.println("[KinuxLib] Class Not Found: net.minecraft.server." + version + "." + string);
		return null;
	}
	}
	
	/* Get UnHashed NMS Class */
	public static Class<?> getCraftBukkitNMSClass(String string){
	String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
	try {
		return Class.forName("org.bukkit.craftbukkit." + version + "." + string);
	} 
	catch (ClassNotFoundException e){
		e.printStackTrace();
		System.out.println("[KinuxLib] Class Not Found: org.bukkit.craftbukkit." + version + "." + string);
		return null;
	}
	}
	
	public static byte reverseByteNumber(float f){
		return (byte) (f - f - f);
	}
}

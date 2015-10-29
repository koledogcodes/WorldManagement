package me.koledogcodes.worldcontrol.configs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.koledogcodes.worldcontrol.WorldControl;

public class WorldSpawnFile {
	
	//Instance
	static WorldControl plugin;
	public WorldSpawnFile(WorldControl i){
	plugin = i;
	}
	
	private static FileConfiguration customConfig = null;
	private static File customConfigFile = null;
	private static InputStream defConfigStream;
	public static void reloadCustomConfig(){
		
	if (customConfigFile == null) {
	customConfigFile = new File(plugin.getDataFolder() + "/Spawns", "WorldSpawns.yml"); 
	}
	customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	 defConfigStream = plugin.getResource("WorldSpawns.yml");
	if (defConfigStream != null) {
	@SuppressWarnings("deprecation")
	YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	customConfig.setDefaults(defConfig);
	}
	}
	 
	public static FileConfiguration getCustomConfig() {
	if (customConfig == null) {
	reloadCustomConfig(); 
	}
	return customConfig;
	}
	 
	public static void saveCustomConfig() {
	if (customConfig == null || customConfigFile == null) {
	return;
	}
	try {
	try {
		getCustomConfig().save(customConfigFile);
		if (defConfigStream != null){
			defConfigStream.close();
		}
	}
	catch (FileNotFoundException exc){
		if (defConfigStream != null){
			defConfigStream.close();
		}
	}
	if (defConfigStream != null){
		defConfigStream.close();
	}
	} catch (IOException ex) {
	plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	}
	}
	 
}

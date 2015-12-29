package me.koledogcodes.worldcontrol.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.koledogcodes.worldcontrol.WorldControl;

public class WorldConfigFile {
	
	//Instance
	static WorldControl plugin;
	private static File file;
	private static FileConfiguration customConfig = null;
	
	public WorldConfigFile(WorldControl i){
	plugin = i;
	final String PLUGIN_NAME = plugin.getName();
	
	file = new File("plugins/" + PLUGIN_NAME + "/Worlds", "Worlds.yml");

	if (!file.exists()){
		try {
			file.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
	
	public static FileConfiguration getCustomConfig(){
		if (customConfig == null) {
			reloadCustomConfig(); 
		}
		return customConfig;
	}
	
	public static void saveCustomConfig(){
		try {
			getCustomConfig().save(file);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reloadCustomConfig(){
		if (file.exists() == false){
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		customConfig = YamlConfiguration.loadConfiguration(file);
		
	}
	 
}

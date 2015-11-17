package me.koledogcodes.worldcontrol.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerDataFile {
	
	private String dataFolder = "plugins/WorldManagement/";
	@SuppressWarnings("unused")
	private String uuid = "";
	private File file;
	private FileConfiguration customConfig = null;

	public PlayerDataFile(String player) {
		this.uuid = player;
		this.file = new File(dataFolder + "Userdata", player + ".yml");

		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public FileConfiguration getConfig(){
		if (customConfig == null) {
			reloadConfig(); 
		}
		return customConfig;
	}
	
	public void saveConfig(){
		try {
			getConfig().save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reloadConfig(){
		if (file.exists() == false){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		customConfig = YamlConfiguration.loadConfiguration(file);
		
	}

}
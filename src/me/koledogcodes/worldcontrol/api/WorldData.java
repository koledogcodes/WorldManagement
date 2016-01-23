package me.koledogcodes.worldcontrol.api;

import org.bukkit.World.Environment;
import org.bukkit.WorldType;

import me.koledogcodes.worldcontrol.configs.WorldDataFile;

public class WorldData {

	private String world;
	
	public WorldData(String world) {
		this.world = world;
	}
	
	public String getWorldName(){
		return world;
	}
	
	public long getSeed(){
		return WorldDataFile.getCustomConfig().getLong(world + ".seed");
	}
	
	public Environment getEnvironment(){
		try {
			return Environment.valueOf(WorldDataFile.getCustomConfig().getString(world + ".env").toUpperCase());
		}
		catch (Exception e){
			return Environment.NORMAL;
		}
	}
	
	public WorldType getWorldType(){
		try {
			return WorldType.valueOf(WorldDataFile.getCustomConfig().getString(world + ".type").toUpperCase());
		}
		catch (Exception e){
			return WorldType.NORMAL;
		}
	}

	public boolean canGenerateStructures(){
		return WorldDataFile.getCustomConfig().getBoolean(world + ".generate-strut");
	}
}

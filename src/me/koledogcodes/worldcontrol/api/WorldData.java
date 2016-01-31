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
	
	public void setSeed(long l){
		WorldDataFile.getCustomConfig().set(world + ".seed", l);
		WorldDataFile.saveCustomConfig();
	}
	
	public long getSeed(){
		return WorldDataFile.getCustomConfig().getLong(world + ".seed");
	}
	
	public void setEnvironment(Environment env){
		WorldDataFile.getCustomConfig().set(world + ".env", env);
		WorldDataFile.saveCustomConfig();
	}
	
	public Environment getEnvironment(){
		try {
			return Environment.valueOf(WorldDataFile.getCustomConfig().getString(world + ".env").toUpperCase());
		}
		catch (Exception e){
			return Environment.NORMAL;
		}
	}
	
	public void setWorldType(WorldType type){
		WorldDataFile.getCustomConfig().set(world + ".type", type);
		WorldDataFile.saveCustomConfig();
	}
	
	public WorldType getWorldType(){
		try {
			return WorldType.valueOf(WorldDataFile.getCustomConfig().getString(world + ".type").toUpperCase());
		}
		catch (Exception e){
			return WorldType.NORMAL;
		}
	}
	
	public void setGenerateStructures(boolean b){
		WorldDataFile.getCustomConfig().set(world + ".generate-strut", b);
		WorldDataFile.saveCustomConfig();
	}

	public boolean canGenerateStructures(){
		return WorldDataFile.getCustomConfig().getBoolean(world + ".generate-strut");
	}
	
	public void setGenerator(String gen){
		WorldDataFile.getCustomConfig().set(world + ".generator", gen);
		WorldDataFile.saveCustomConfig();
	}

	public String getGenerator(){
		try {
			return WorldDataFile.getCustomConfig().getString(world + ".generator");
		}
		catch (Exception e){
			return "Default";
		}
	}
}

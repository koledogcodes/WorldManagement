package me.koledogcodes.worldcontrol.api;

import org.bukkit.World;

import me.koledogcodes.worldcontrol.handler.WorldControlHandler;

public class WorldInfo {

	private World world;
	private WorldControlHandler WorldControl = new WorldControlHandler();
	
	public WorldInfo(World world) {
		this.world = world;
	}
	
	public String getWorldName(){
		return world.getName();
	}
	
	public int getPlayerCount(){
		return world.getPlayers().size();
	}
	
	public int getMaxPlayers(){
		return (int) WorldControl.getWorldSettingValue(getWorldName(), "player-limit");
	}
	
	public boolean isWhitelisted(){
		return WorldControl.worldWhitelistIsEnabled(getWorldName());
	}
	
	public String getFallbackWorld(){
		return (String) WorldControl.getWorldSettingValue(world.getName(), "fallback-world");
	}
	
	public Object getFlagValue(WorldFlag flag){
		return WorldControl.getWorldSettingValue(world.getName(), WorldFlagConvertor.translateFlag(flag));
	}
}

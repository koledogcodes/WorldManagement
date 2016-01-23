package me.koledogcodes.worldcontrol.events;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import me.koledogcodes.worldcontrol.custom.events.WorldControlDeleteWorldEvent;
import org.bukkit.Bukkit;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.custom.events.WorldControlCreateWorldEvent;
import me.koledogcodes.worldcontrol.custom.events.WorldControlLoadWorldEvent;
import me.koledogcodes.worldcontrol.custom.events.WorldControlUnloadWorldEvent;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.timer.WorldControlTimer;

public class WorldControlEventCaller {

	private static WorldControlHandler WorldControl = new WorldControlHandler();
	private static HashMap<String, String> prevState = new HashMap<String, String>();
	private static HashMap<Integer, List<String>> prevWorlds = new HashMap<Integer, List<String>>();
	
	public WorldControlEventCaller(WorldControl i) {
		
	}
	
	//TODO Start EventCaller
	public static void startLoadUnloadEventCaller(){
		WorldControlTimer timer = new WorldControlTimer();
		timer.registerNewRepeatingTimer(new TimerTask(){

			@Override
			public void run() {
				
				for (String world: WorldControl.getAllWorlds()){
					if (prevState.containsKey(world) == false){
						if (WorldControl.worldExists(world)){
							prevState.put(world, "LOADED");
						}
						else {
							prevState.put(world, "UNLOADED");
						}
						continue;
					}
					
					if (prevState.get(world).equalsIgnoreCase("LOADED")){
						if (WorldControl.worldExists(world) == false){
							prevState.put(world, "UNLOADED");
							Bukkit.getServer().getPluginManager().callEvent(new WorldControlUnloadWorldEvent(null, world));
							continue;
						}
					}
					else if (prevState.get(world).equalsIgnoreCase("UNLOADED")){
						if (WorldControl.worldExists(world)){
							prevState.put(world, "LOADED");
							Bukkit.getServer().getPluginManager().callEvent(new WorldControlLoadWorldEvent(null, world));
							continue;
						}
					}
					else {
						continue;
					}
					
				}
				
			}
			
		}, 5, 5);
	}
	
	//TODO Start EventCaller
	public static void startWorldCreateEventCaller(){
		WorldControlTimer timer = new WorldControlTimer();
		timer.registerNewRepeatingTimer(new TimerTask(){

			@Override
			public void run() {
				
					if (prevWorlds.containsKey(0) == false){
						prevWorlds.put(0, WorldControl.getAllWorlds());
					}
					
					if (WorldControl.getAllWorlds().size() < prevWorlds.get(0).size()){
						Bukkit.getServer().getPluginManager().callEvent(new WorldControlDeleteWorldEvent(prevWorlds.get(0).get(prevWorlds.get(0).size() - 1)));
						prevWorlds.put(0, WorldControl.getAllWorlds());
					}
					else if (WorldControl.getAllWorlds().size() == prevWorlds.get(0).size()){

					}
					else if (WorldControl.getAllWorlds().size() > prevWorlds.get(0).size()){
						Bukkit.getServer().getPluginManager().callEvent(new WorldControlCreateWorldEvent(WorldControl.getAllWorlds().get(WorldControl.getAllWorlds().size() - 1)));
						prevWorlds.put(0, WorldControl.getAllWorlds());
					}
					else {
						
					}
					
			}
			
		}, 5, 5);
	}
}

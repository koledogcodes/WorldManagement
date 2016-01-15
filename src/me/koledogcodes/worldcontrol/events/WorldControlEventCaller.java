package me.koledogcodes.worldcontrol.events;

import java.util.HashMap;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.custom.events.WorldControlLoadWorldEvent;
import me.koledogcodes.worldcontrol.custom.events.WorldControlUnloadWorldEvent;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.timer.WorldControlTimer;

public class WorldControlEventCaller {

	private static WorldControlHandler WorldControl = new WorldControlHandler();
	private static HashMap<String, String> prevState = new HashMap<String, String>();
	
	public WorldControlEventCaller(WorldControl i) {
		
	}
	
	public static void start(){
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
	
}

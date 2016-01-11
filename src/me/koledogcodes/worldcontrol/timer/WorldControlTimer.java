package me.koledogcodes.worldcontrol.timer;

import java.util.Timer;
import java.util.TimerTask;

import me.koledogcodes.worldcontrol.WorldControl;

public class WorldControlTimer {
	
	public WorldControlTimer() {
	}
	
	public void registerNewRepeatingTimer(TimerTask task, long delay, long period){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, delay, period);
		WorldControl.activeTimers.add(timer);
		WorldControl.activeTasks.add(task);
	}
	
	public void registerNewNonRepeatingTimer(TimerTask task, long delay_unti_execute){
		Timer timer = new Timer();
		timer.schedule(task, delay_unti_execute);
		WorldControl.activeTimers.add(timer);
		WorldControl.activeTasks.add(task);
	}
	
	public void cancelAllTimers(boolean broadcast){
		if (broadcast){
			for (int i = 0; i < WorldControl.activeTasks.size(); i++){
				WorldControl.activeTasks.get(i).cancel();
				System.out.println("[WorldControl] WorldControlTask #" + i + " has been cancelled.");
			}
			
			for (int i = 0; i < WorldControl.activeTimers.size(); i++){
				WorldControl.activeTimers.get(i).cancel();
				System.out.println("[WorldControl] WorldControlTimer #" + i + " has been cancelled.");
			}
		}
		else {
			for (int i = 0; i < WorldControl.activeTasks.size(); i++){
				WorldControl.activeTasks.get(i).cancel();
			}
			
			for (int i = 0; i < WorldControl.activeTimers.size(); i++){
				WorldControl.activeTimers.get(i).cancel();
			}
		}
	}
}

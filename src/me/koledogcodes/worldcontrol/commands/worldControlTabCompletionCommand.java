package me.koledogcodes.worldcontrol.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;

public class worldControlTabCompletionCommand implements TabCompleter {
	
	public worldControlTabCompletionCommand(WorldControl i) {
		
	}

	private WorldControlHandler WorldControl = new WorldControlHandler();
	List<String> allowedArgs_2 = Arrays.asList("load,unload,delete,exist,tp,flag,createconf,reload,save,copy,whitelist,getflag,modify".split("\\,"));


	 @Override
     public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		 if (sender.hasPermission("worldcontrol.admin.*") == false){ return null; }
		 if (sender instanceof Player == false){ return null; }
		 
		 Player player = (Player) sender;
		 List<String> tabs = new ArrayList<String>();
		 tabs.clear();
		 
		 if (cmd.getName().equalsIgnoreCase("worldcontrol") || cmd.getName().equalsIgnoreCase("wc") || cmd.getName().equalsIgnoreCase("worldc") || cmd.getName().equalsIgnoreCase("wcontrol")){
			 if (args.length == 3){
				 if (args[0].equalsIgnoreCase("flag") || args[0].equalsIgnoreCase("getflag")){
					 if (WorldControl.worldFolderExists(args[1]) || args[1].equalsIgnoreCase("__global__")){
						 if (args[2] != ""){
							 for (String flag: Arrays.asList(ChatColor.stripColor(WorldControl.getWorldFlagsMessage(player.getWorld().getName())).split("\\, "))){
								 if (flag.toLowerCase().startsWith(args[2].toLowerCase())){
									 if (flag.contains(".")){
										 tabs.add(flag);
									 }
									 else {
										 tabs.add(flag);
									 }
								 }
							 }
							 
							 return tabs;
						 }
						 else {
							 tabs = Arrays.asList(ChatColor.stripColor(WorldControl.getWorldFlagsMessage(player.getWorld().getName())).split("\\, "));
							 return tabs;
						 }
					 }
				 }
			 }
			 
			 else if (args.length == 2){
				 if (allowedArgs_2.contains(args[0].toLowerCase())){
					 if (args[1] != ""){
						 for (String world: WorldControl.getAllWorlds()){
							 if (world.toLowerCase().startsWith(args[1])){
								 tabs.add(world);
							 }
						 }
						 
						 return tabs;
					 }
					 else {
						 return WorldControl.getAllWorlds();
					 }
				 }
			 }

		 }
		 
		 return null;
	 }
}

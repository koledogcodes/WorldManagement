package me.koledogcodes.worldcontrol.commands;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.configs.WorldConfigFile;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutHoverChat;

public class worldControlCommand implements CommandExecutor {
	
	//Constrcutor
	private WorldControl plugin;
	public worldControlCommand(WorldControl i) {
		plugin = i;
	}

	private PacketOutHoverChat packetHoverMessage = new PacketOutHoverChat();
	private String prefix = ChatUtili.messagePrefix;
	private WorldControlHandler WorldControl = new WorldControlHandler();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player == false){
		ChatUtili.sendTranslatedMessage(sender, "&cYou must be ingame to use &4/" + cmd.getName() + ".");	
			return true;
		}
		
		//Variables
		Player player = (Player) sender;
		
		if (player.hasPermission("worldcontrol.admin.*") == false){
			ChatUtili.sendTranslatedMessage(player, "&cYou do not have permission.");
			return true;
		}
		
		if (args.length == 0){
		ChatUtili.sendTranslatedMessage(player, "&3----- &bWorldControl Command Page &3-----");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc create <world> &3<world type> &3<envoirment> &3<generate structures> &3<seed>", " &b- &b(Hover)", "&aThis command simply creates a world with features.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc create <world>", " &b- &b(Hover)", "&aThis command simply creates a world.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc exist <world>", " &b- &b(Hover)", "&aThis command simply checks if a world exists.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc load <world>", " &b- &b(Hover)", "&aThis command simply loads exisitng worlds.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc unload <world>", " &b- &b(Hover)", "&aThis command simply unloads a exisitng world.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc tp <world>", " &b- &b(Hover)", "&aThis command teleports the player to the worlds \n &aspawn location.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc list", " &b- &b(Hover)", "&aThis command list all current worlds. \n &aAnd shows their loaded status.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc createconf <world>", " &b- &b(Hover)", "&aThis command creates a config file for the specified world.");	
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc reload", " &b- &b(Hover)", "&aThis command reloads the entire plugin.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc reload <world>", " &b- &b(Hover)", "&aThis command reloads a worlds config file.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc save <world>", " &b- &b(Hover)", "&aThis command saves a world to its world folder.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc copy <old world> <new world>", " &b- &b(Hover)", "&aThis command will copy a \n&aold world to a newly created world.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc whitelist <world>", " &b- &b(Hover)", "&aThis command toogles the whitelist on or off \n&afor the specifed world.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc whitelist <world> list", " &b- &b(Hover)", "&aThis command list all players in the whitelist for the specifed world.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc whitelist <world> set <player>", " &b- &b(Hover)", "&aThis command toogles players on or off \n&athe whitelist for the specifed world.");	
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc setlocation", " &b- &b(Hover)", "&aThis command sets the tp location for the specifed world. \n&a(Only applies to /wc tp <world>)");		
			return true;
		}
		
		if (args.length == 1){
		if (args[0].equalsIgnoreCase("create")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to create.");		
		}
		else if (args[0].equalsIgnoreCase("exist")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to check.");			
		}
		else if (args[0].equalsIgnoreCase("load")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to load.");	
		}
		else if (args[0].equalsIgnoreCase("tp")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to tp to.");
		}
		else if (args[0].equalsIgnoreCase("list")){
			ChatUtili.sendTranslatedMessage(player, "&6Worlds: &f" + WorldControl.getAllWorldsStatus());
		}
		else if (args[0].equalsIgnoreCase("createconf")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to create a config for.");
		}
		else if (args[0].equalsIgnoreCase("reload")){
			Bukkit.getServer().getPluginManager().disablePlugin(plugin);
			Bukkit.getServer().getPluginManager().enablePlugin(plugin);
			ChatUtili.sendTranslatedMessage(player, "&aWorldControl has been fully reloaded!");
		}
		else if (args[0].equalsIgnoreCase("unload")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to unload.");
		}
		else if (args[0].equalsIgnoreCase("save")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to save.");
		}
		else if (args[0].equalsIgnoreCase("copy")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a exisiting world to copy.");
		}
		else if (args[0].equalsIgnoreCase("whitelist")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to toogle whitelist on or off for.");
		}
		else if (args[0].equalsIgnoreCase("setLocation") || args[0].equalsIgnoreCase("setLoc")){
			WorldControl.setWorldTeleportLocation(player.getWorld().getName(), player.getLocation());
			ChatUtili.sendTranslatedMessage(player, "&aWorld '" + player.getWorld().getName() + "' tp location has been set at current location.");
		}
		else {
			packetHoverMessage.sendHoverMessage(player, prefix,  " &cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
		}
			return true;
		}
		
		if (args.length == 2){
		if (args[0].equalsIgnoreCase("create")){
			WorldControl.createWorld(player, args[1]);	
		}
		else if (args[0].equalsIgnoreCase("exist")){
			if (WorldControl.worldFolderExists(args[1])){
				if (WorldControl.worldExists(args[1])){
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + args[1] + "' exists and is loaded.");	
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&aWorld '" + args[1] + "' exists and is not loaded.");	
				}
			}
			else {
			ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");		
			}
		}
		else if (args[0].equalsIgnoreCase("load")){
			WorldControl.loadWorld(player, args[1]);
		}
		else if (args[0].equalsIgnoreCase("tp")){
			WorldControl.world.put(player, args[1]);
			WorldControl.tpToWorldSetLocation(player, args[1]);
		}
		else if (args[0].equalsIgnoreCase("createconf")){
			WorldControl.world.put(player, args[1]);
			WorldControl.generateWorldConfiguration(player);
			WorldConfigFile.reloadCustomConfig();
		}
		else if (args[0].equalsIgnoreCase("reload")){
			if (WorldControl.worldFolderExists(args[1])){
				WorldConfigFile.reloadCustomConfig();
				ChatUtili.sendTranslatedMessage(player, "&aWorld '" + args[1] + "' config file has been reloaded.");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&CWorld '" + args[1] + "' doesn't exist.");
			}
		}
		else if (args[0].equalsIgnoreCase("unload")){
			WorldControl.unloadWorld(player, args[1], true);
		}
		else if (args[0].equalsIgnoreCase("save")){
			WorldControl.saveWorld(player, args[1]);
		}
		else if (args[0].equalsIgnoreCase("copy")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a new world name for your copied world.");
		}
		else if (args[0].equalsIgnoreCase("whitelist")){
			WorldControl.toggleWorldWhitelist(player, args[1]);
		}
		else {
			packetHoverMessage.sendHoverMessage(player, prefix,  " &cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
		}
			return true;
		}
		
		if (args.length == 3){
			if (args[0].equalsIgnoreCase("create")){
				try {
					WorldControl.createWorld(player, args[1], WorldType.getByName(args[2].toUpperCase()));
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(player, "&cInvalid argument given.");
				}
			}
			else if (args[0].equalsIgnoreCase("copy")){
				WorldControl.copyWorld(player, args[1], args[2]);
			}
			else if (args[0].equalsIgnoreCase("whitelist")){
				if (WorldControl.worldFolderExists(args[1])){
					if (args[2].equalsIgnoreCase("set")){
						ChatUtili.sendTranslatedMessage(player, "&cPlease provide a player to toggle on or off world '" + args[1] + "' whitelist.");
					}
					else if (args[2].equalsIgnoreCase("list")){
						if (WorldControl.worldExists(args[1])){
							if (WorldControl.worldWhitelistIsEnabled(args[1])){
								ChatUtili.sendTranslatedMessage(player, "&6Whitelisted Players: &f" + WorldControl.cleanListArray(WorldControl.getWorldWhiteList(args[1]), "No Players"));
							}
							else {
								ChatUtili.sendTranslatedMessage(player, "&cWhitelist is not enabled for world '" + args[1] + "'.");
							}
						}
						else {
							ChatUtili.sendTranslatedMessage(player, "World '" + args[1] + "' doesn't exist.");
						}
					}
					else {
						packetHoverMessage.sendHoverMessage(player, prefix,  "&cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
					}
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' doesn't exist.");
				}
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  "&cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
			}
			return true;
		}
		
		if (args.length == 4){
			if (args[0].equalsIgnoreCase("create")){
				try {
					WorldControl.createWorld(player, args[1], WorldType.getByName(args[2].toUpperCase()), Environment.valueOf(args[3].toUpperCase()));
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(player, "&cInvalid argument given.");
				}
			}
			else if (args[0].equalsIgnoreCase("whitelist")){
				WorldControl.tooglePlayerWorldWhitelist(player, args[1], args[3]);
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  "&cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
			}
			return true;
		}
		
		if (args.length == 5){
			if (args[0].equalsIgnoreCase("create")){
				try {
					WorldControl.createWorld(player, args[1], WorldType.getByName(args[2].toUpperCase()), Environment.valueOf(args[3].toUpperCase()), Boolean.valueOf(args[4]));
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(player, "&cInvalid argument given.");
				}
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  "&cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
			}
			return true;
		}
		
		if (args.length == 6){
			if (args[0].equalsIgnoreCase("create")){
				try {
					WorldControl.createWorld(player, args[1], WorldType.getByName(args[2].toUpperCase()), Environment.valueOf(args[3].toUpperCase()), Boolean.valueOf(args[4]), Integer.parseInt(args[5]));
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(player, "&cInvalid argument given.");
				}
			}	
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  "&cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
			}
			return true;
		}
		
		
		
		
		return false;
	}
	
	

}
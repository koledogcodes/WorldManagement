package me.koledogcodes.worldcontrol.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.WorldInfo;
import me.koledogcodes.worldcontrol.configs.ConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldConfigFile;
import me.koledogcodes.worldcontrol.configs.WorldDataFile;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutHoverChat;

public class worldControlCommand implements CommandExecutor {
	
	private final int MAX_PAGE = 6;
	
	//Constrcutor
	private WorldControl plugin;
	public worldControlCommand(WorldControl i) {
		plugin = i;
	}

	private PacketOutHoverChat packetHoverMessage = new PacketOutHoverChat();
	public static String prefix = ChatUtili.messagePrefix;
	private WorldControlHandler WorldControl = new WorldControlHandler();
	
	@SuppressWarnings("deprecation")
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
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc create <world> &3<world type> &3<env> &3<generate &3structures> &3<seed>", " &b- &b(Hover)", "&aThis command simply creates a world with features.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc delete <world>", " &b- &b(Hover)", "&aThis command simply deletes a world.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc exist <world>", " &b- &b(Hover)", "&aThis command simply checks if a world exists.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc load <world>", " &b- &b(Hover)", "&aThis command simply loads exisitng worlds.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc unload <world>", " &b- &b(Hover)", "&aThis command simply unloads a exisitng world.");	
		ChatUtili.sendTranslatedMessage(player, "&3----- &bPage 1/" + MAX_PAGE + " &3-----");
			return true;
		}
		
		if (args.length == 1){
		if (args[0].equalsIgnoreCase("create")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to create.");		
		}
		else if (args[0].equalsIgnoreCase("delete")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to delete.");		
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
			WorldControl.messageWorldsStatus(player, 1);
		}
		else if (args[0].equalsIgnoreCase("createconf")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to create a config for.");
		}
		else if (args[0].equalsIgnoreCase("reload")){
			WorldControl.excuteJavaThread(new Runnable(){
				
				@Override
				public void run() {
					
					ConfigFile.reloadCustomConfig();
					WorldConfigFile.reloadCustomConfig();
					WorldDataFile.reloadCustomConfig();
					WorldControl.getWorldControl().reloadConfig();
					Bukkit.getServer().getPluginManager().disablePlugin(plugin);
					Bukkit.getServer().getPluginManager().enablePlugin(plugin);
					
					return;
					
				}
			});
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
		else if (args[0].equalsIgnoreCase("set-portal")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a portal name.");
		}
		else if (args[0].equalsIgnoreCase("set-portal-dest")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a destation name for this location.");
		}
		else if (args[0].equalsIgnoreCase("delete-portal")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a portal to delete.");
		}
		else if (args[0].equalsIgnoreCase("delete-portal-dest")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a destation name to delete.");
		}
		else if (args[0].equalsIgnoreCase("portal-list")){
			ChatUtili.sendTranslatedMessage(player, "&6Portals: &f" + WorldControl.getCleanPortalList());
		}
		else if (args[0].equalsIgnoreCase("dest-list")){
			ChatUtili.sendTranslatedMessage(player, "&6Dests: &f" + WorldControl.getCleanDestinationsList());
		}
		else if (args[0].equalsIgnoreCase("flag")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to set flags for.");
		}
		else if (args[0].equalsIgnoreCase("inspect") || args[0].equalsIgnoreCase("i")){
			if (WorldControlHandler.blockInspection.containsKey(player) == false){
			WorldControlHandler.blockInspection.put(player, true);
			ChatUtili.sendTranslatedMessage(player, "&aBlock inspection has been enabled.");
			}
			else {
			if (WorldControlHandler.blockInspection.get(player)){
				WorldControlHandler.blockInspection.put(player, false);
				ChatUtili.sendTranslatedMessage(player, "&aBlock inspection has been disabled.");
			}
			else {
				WorldControlHandler.blockInspection.put(player, true);
				ChatUtili.sendTranslatedMessage(player, "&aBlock inspection has been enabled.");
			}
			}
		}
		else if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a page number.");
		}
		else if (args[0].equalsIgnoreCase("lookup") || args[0].equalsIgnoreCase("l")){
			if (WorldControlHandler.blockInspectionLocation.containsKey(player)){
				ChatUtili.sendTranslatedMessage(player, "&cPlease provide a page number.");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cYou need to select a block with &4'/wc inspect'.");
			}
		}
		else if (args[0].equalsIgnoreCase("who")){
			WorldInfo worldInfo = new WorldInfo(player.getWorld());
			ChatUtili.sendSimpleTranslatedMessage(player, "&f---------- &6World Info &f--------");
			ChatUtili.sendSimpleTranslatedMessage(player, "&7World name: &a" + worldInfo.getWorldName());
			if (worldInfo.getMaxPlayers() != -1){
				ChatUtili.sendSimpleTranslatedMessage(player, "&7World Player Count: &a" + worldInfo.getPlayerCount() + "/" + worldInfo.getMaxPlayers());
			}
			else {
				ChatUtili.sendSimpleTranslatedMessage(player, "&7World Player Count: &a" + worldInfo.getPlayerCount() + "/(Infinite)");
			}
			ChatUtili.sendSimpleTranslatedMessage(player, "&f----------------------------");
		}
		else if (args[0].equalsIgnoreCase("getflag")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to view flags from.");
		}
		else if (args[0].equalsIgnoreCase("version")){
			ChatUtili.sendTranslatedMessage(player, "&7You are currently using version '&a" + plugin.getDescription().getVersion() + "&7'!");
		}
		else if (args[0].equalsIgnoreCase("modify")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to modify.");
		}
		else if (args[0].equalsIgnoreCase("rollback")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a raduis.");
		}
		else if (args[0].equalsIgnoreCase("regen-chunk")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a x and y coord.");
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
		else if (args[0].equalsIgnoreCase("set-portal")){
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a existing destation name.");
		}
		else if (args[0].equalsIgnoreCase("set-portal-dest")){
			WorldControl.setDestination(args[1].toLowerCase(), player.getLocation());
			ChatUtili.sendTranslatedMessage(player, "&aDestination '" + args[1].toLowerCase() + "' has been set at current location.");
		}
		else if (args[0].equalsIgnoreCase("delete-portal")){
			if (WorldControl.portalExists(args[1].toLowerCase())){
				WorldControl.deletePortal(args[1].toLowerCase());
				ChatUtili.sendTranslatedMessage(player, "&aPortal '" + args[1].toLowerCase() + "' has been deleted.");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cPortal '" + args[1].toLowerCase() + "' does not exist.");
			}
		}
		else if (args[0].equalsIgnoreCase("delete-portal-dest")){
			if (WorldControl.portalDestinationExists(args[1].toLowerCase())){
				WorldControl.deleteDestination(args[1].toLowerCase());
				ChatUtili.sendTranslatedMessage(player, "&aDestination '" + args[1].toLowerCase() + "' has been deleted.");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cDestination '" + args[1].toLowerCase() + "' does not exist.");
			}		
		}
		else if (args[0].equalsIgnoreCase("delete")){
			if (player.getWorld().getName().equalsIgnoreCase(args[1])){
				ChatUtili.sendTranslatedMessage(player, "&cYou cannot delete a world you are currently in.");
				return true;
			}
			
			if (WorldControl.worldFolderExists(args[1]) == false){
				ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' doesn't exist.");
				return true;
			}
			ChatUtili.sendTranslatedMessage(player, "&aDeleting world '" + args[1] + "'.");
			File file = new File(args[1]);
			if (WorldControl.worldExists(args[1])){
				WorldControl.unloadWorld(null, args[1], false);
			}
			if (WorldControl.deleteWorld(args[1], file)){
				ChatUtili.sendTranslatedMessage(player, "&aWorld '" + args[1] + "' has been deleted.");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&c Failed to delete world '" + args[1] + "'.");
			}
		}
		else if (args[0].equalsIgnoreCase("flag")){
			if (args[1].equals("__global__")){
				ChatUtili.sendTranslatedMessage(player, "&cPlease provide a flag to modify for world '" + args[1] + "'.");
			}
			else {
				if (WorldControl.worldFolderExists(args[1])){
					ChatUtili.sendTranslatedMessage(player, "&cPlease provide a flag to modify for world '" + args[1] + "'.");
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
				}
			}
		}
		else if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")){
			if (args[1].equalsIgnoreCase("1")){
				ChatUtili.sendTranslatedMessage(player, "&3----- &bWorldControl Command Page &3-----");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc create <world> &3<world type> &3<envoirment> &3<generate structures> &3<seed>", " &b- &b(Hover)", "&aThis command simply creates a world with features.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc delete <world>", " &b- &b(Hover)", "&aThis command simply deletes a world.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc exist <world>", " &b- &b(Hover)", "&aThis command simply checks if a world exists.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc load <world>", " &b- &b(Hover)", "&aThis command simply loads exisitng worlds.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc unload <world>", " &b- &b(Hover)", "&aThis command simply unloads a exisitng world.");	
				ChatUtili.sendTranslatedMessage(player, "&3----- &bPage " + args[1] + "/" + MAX_PAGE + " &3-----");
			}
			else if (args[1].equalsIgnoreCase("2")){
				ChatUtili.sendTranslatedMessage(player, "&3----- &bWorldControl Command Page &3-----");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc tp <world>", " &b- &b(Hover)", "&aThis command teleports the player to the worlds \n &aspawn location.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc list", " &b- &b(Hover)", "&aThis command list all current worlds. \n &aAnd shows their loaded status.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc createconf <world>", " &b- &b(Hover)", "&aThis command creates a config file for the specified world.");	
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc flag <world> <option> <value>", " &b- &b(Hover)", "&aThis command allows you to edit flags for worlds. (Config option's)");	
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc reload", " &b- &b(Hover)", "&aThis command reloads the entire plugin.");
				ChatUtili.sendTranslatedMessage(player, "&3----- &bPage " + args[1] + "/" + MAX_PAGE + " &3-----");
			}
			else if (args[1].equalsIgnoreCase("3")){
				ChatUtili.sendTranslatedMessage(player, "&3----- &bWorldControl Command Page &3-----");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc reload <world>", " &b- &b(Hover)", "&aThis command reloads a worlds config file.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc save <world>", " &b- &b(Hover)", "&aThis command saves a world to its world folder.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc copy <old world> <new world>", " &b- &b(Hover)", "&aThis command will copy a \n&aold world to a newly created world.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc whitelist <world>", " &b- &b(Hover)", "&aThis command toogles the whitelist on or off \n&afor the specifed world.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc whitelist <world> list", " &b- &b(Hover)", "&aThis command list all players in the whitelist for the specifed world.");	
				ChatUtili.sendTranslatedMessage(player, "&3----- &bPage " + args[1] + "/" + MAX_PAGE + " &3-----");
			}
			else if (args[1].equalsIgnoreCase("4")){
				ChatUtili.sendTranslatedMessage(player, "&3----- &bWorldControl Command Page &3-----");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc whitelist <world> set <player>", " &b- &b(Hover)", "&aThis command toogles players on or off \n&athe whitelist for the specifed world.");	
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc setlocation", " &b- &b(Hover)", "&aThis command sets the tp location for the specifed world. \n&a(Only applies to /wc tp <world>)");		
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc set-portal <portal> <destation name> &3<replacement block>", " &b- &b(Hover)", "&aThis command sets a portal.");	
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc set-portal-dest <destationn name>", " &b- &b(Hover)", "&aThis command sets a destation for a portal.");	
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc delete-portal <portal>", " &b- &b(Hover)", "&aThis command deletes a portal.");	
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc delete-portal-dest <destation name>", " &b- &b(Hover)", "&aThis command deletes a destation.");	
				ChatUtili.sendTranslatedMessage(player, "&3----- &bPage " + args[1] + "/" + MAX_PAGE + " &3-----");
			}
			else if (args[1].equalsIgnoreCase("5")){
				ChatUtili.sendTranslatedMessage(player, "&3----- &bWorldControl Command Page &3-----");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc portal-list", " &b- &b(Hover)", "&aList all the portals.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc dest-list", " &b- &b(Hover)", "&aThis command list all portal destation.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc inspect", " &b- &b(Hover)", "&aThis command checks who placed or broke a block.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc lookup <page>", " &b- &b(Hover)", "&aThis command checks a page of a inspected block (Most Recent)");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc who [player]", " &b- &b(Hover)", "&aThis command checks what world a player is in.");
				ChatUtili.sendTranslatedMessage(player, "&3----- &bPage " + args[1] + "/" + MAX_PAGE + " &3-----");
			}
			else if (args[1].equalsIgnoreCase("6")){
				ChatUtili.sendTranslatedMessage(player, "&3----- &bWorldControl Command Page &3-----");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc getflag <world> <flag>", " &b- &b(Hover)", "&aGets the flag value of a specfic world.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc modify <world> <environment> <world type>", " &b- &b(Hover)", "&aModifies a world environment and or world type \n&c&lWarning: &7When modifying worlds, houses etc will not be transferred to the new type or environment!");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc rollback <radius>", " &b- &b(Hover)", "&aRollbacks all blocks to their orginal state. (Since WCM was installed)");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc regen-chunk <x, z>", " &b- &b(Hover)", "&aRegenrates a chunk at x, z.");
				packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc version", " &b- &b(Hover)", "&aGets the plugin version.");
				ChatUtili.sendTranslatedMessage(player, "&3----- &bPage " + args[1] + "/" + MAX_PAGE + " &3-----");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cInvalid page.");
			}
		}
		else if (args[0].equalsIgnoreCase("lookup") || args[0].equalsIgnoreCase("l")){
			if (WorldControlHandler.blockInspectionLocation.containsKey(player)){
				try {
					if (WorldControlHandler.blockInspectionLocationType.get(player).equalsIgnoreCase("LIST")){
						WorldControl.messageBlockInformation(player, WorldControlHandler.blockInspectionLocation.get(player).getBlock(), Integer.parseInt(args[1]));
					}
					else {
						
					}
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(player, "&cInvalid page.");
					player.sendMessage(ChatUtili.colorConvert("&7---------- "));
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cYou need to select a block with &4'/wc inspect'.");
			}
		}
		else if (args[0].equalsIgnoreCase("who")){
			if (Bukkit.getServer().getPlayerExact(args[1]) == null){
				ChatUtili.sendTranslatedMessage(player, "&cPlayer &4" + args[1] + " &cis not online.");
				return true;
			}
			
			Player target = Bukkit.getServer().getPlayerExact(args[1]);
			
			WorldInfo worldInfo = new WorldInfo(target.getWorld());
			ChatUtili.sendSimpleTranslatedMessage(player, "&f---------- &6World Info &f--------");
			ChatUtili.sendSimpleTranslatedMessage(player, "&7Player Name: &a" + target.getName());
			ChatUtili.sendSimpleTranslatedMessage(player, "&7World name: &a" + worldInfo.getWorldName());
			if (worldInfo.getMaxPlayers() != -1){
				ChatUtili.sendSimpleTranslatedMessage(player, "&7World Player Count: &a" + worldInfo.getPlayerCount() + "/" + worldInfo.getMaxPlayers());
			}
			else {
				ChatUtili.sendSimpleTranslatedMessage(player, "&7World Player Count: &a" + worldInfo.getPlayerCount() + "/(Infinite)");
			}
			ChatUtili.sendSimpleTranslatedMessage(player, "&f----------------------------");
		}
		else if (args[0].equalsIgnoreCase("getflag")){
			if (WorldControl.worldFolderExists(args[1]) == false){
				ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
				return true;
			}
			
			ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world flag.");
		}
		else if (args[0].equalsIgnoreCase("list")){
			try {
				WorldControl.messageWorldsStatus(player, Integer.parseInt(args[1]));
			}
			catch (Exception e){
				ChatUtili.sendTranslatedMessage(player, "&cInvalid page!");
			}
		}
		else if (args[0].equalsIgnoreCase("modify")){
			if (WorldControl.worldFolderExists(args[1])){
				ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world to enviorment and world type.");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
			}
		}
		else if (args[0].equalsIgnoreCase("rollback")){
			try {
				WorldControl.rollbackBlocks(player, player.getLocation(), Integer.parseInt(args[1]));
			}
			catch (Exception e){
				ChatUtili.sendTranslatedMessage(player, "&cInvalid raduis suppilied!");
			}
		}
		else if (args[0].equalsIgnoreCase("regen-chunk")){
			try {
				player.getWorld().regenerateChunk(Integer.parseInt(args[1].split(",")[0]), Integer.parseInt(args[1].split(",")[1]));
				player.getWorld().refreshChunk(Integer.parseInt(args[1].split(",")[0]), Integer.parseInt(args[1].split(",")[1]));
				player.getWorld().unloadChunk(Integer.parseInt(args[1].split(",")[0]), Integer.parseInt(args[1].split(",")[1]));
				player.getWorld().loadChunk(Integer.parseInt(args[1].split(",")[0]), Integer.parseInt(args[1].split(",")[1]));
				ChatUtili.sendTranslatedMessage(player, "&aChunk at (x:" + args[1].split(",")[0] + " and y:" + args[1].split(",")[1] + ") has been regenerated!");
			}
			catch (Exception e){
				ChatUtili.sendTranslatedMessage(player, "&cInvalid x/z coord");
			}
		}
		else {
			packetHoverMessage.sendHoverMessage(player, prefix,  " &cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
		}
			return true;
		}
		
		if (args.length == 3){
			if (args[0].equalsIgnoreCase("create")){
				try {
					WorldControl.createWorld(player, args[1], WorldType.valueOf(args[2].toUpperCase()));
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
			else if (args[0].equalsIgnoreCase("set-portal")){
				if (WorldControl.portalDestinationExists(args[2].toLowerCase())){
					ChatUtili.sendTranslatedMessage(player, "&cPlease provide a replacement block.");
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cDestination '" + args[2] + "' does not exist.");
				}
			}
			else if (args[0].equalsIgnoreCase("flag")){
				if (WorldControl.worldFolderExists(args[1]) || args[1].equals("__global__")){
					if (args[2].equalsIgnoreCase("?")){
						ChatUtili.sendSimpleTranslatedMessage(player, "&9World Flags: &a" + WorldControl.getWorldFlagsMessage(WorldControl.getAllWorlds().get(0)));
					}
					else {
						ChatUtili.sendTranslatedMessage(player, "&cPlease provide a value.");					
					}
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
				}
			}
			else if (args[0].equalsIgnoreCase("getflag")){
				if (WorldControl.worldFolderExists(args[1]) == false){
					ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
					return true;
				}
				
				if (WorldControl.worldFlagExists(args[1], args[2].toLowerCase()) == false){
					ChatUtili.sendTranslatedMessage(player, "&7World '&c" + args[1] + "&7' does not have flag '&c" + args[2] + "&7'.");
					return true;
				}
				
				ChatUtili.sendTranslatedMessage(player, "&7World '&a" + args[1] + "&7' flag '&a" + args[2] + "&7' is: &e" + WorldControl.getWorldSettingValue(args[1], args[2].toLowerCase()));
			}
			else if (args[0].equalsIgnoreCase("modify")){
				if (WorldControl.worldFolderExists(args[1])){
					try {
						Environment.valueOf(args[2].toUpperCase());
						ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world type.");
					}
					catch (Exception e){
						ChatUtili.sendTranslatedMessage(player, "&cInvalid environment.");
					}
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
				}
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  "&cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
			}
			return true;
		}
		
		if (args.length >= 4){
			if (args[0].equalsIgnoreCase("flag")){
				if (WorldControl.worldFolderExists(args[1]) || args[1].equals("__global__")){
					if (args[1].equals("__global__")){
						for (String world: WorldControl.getAllWorlds()){
							WorldControl.setWorldFlag(player, world, args[2], args);
						}
						ChatUtili.sendTranslatedMessage(player, "&7Flag '&e" + args[2] + "&7' global value has been set!");
					}
					else {
						WorldControl.setWorldFlag(player, args[1], args[2], args);
					}
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
				}
				return true;
			}
		}
		
		if (args.length == 4){
			if (args[0].equalsIgnoreCase("create")){
				try {
					WorldControl.createWorld(player, args[1], WorldType.valueOf(args[2].toUpperCase()), Environment.valueOf(args[3].toUpperCase()));
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(player, "&cInvalid argument given.");
				}
			}
			else if (args[0].equalsIgnoreCase("whitelist")){
				WorldControl.tooglePlayerWorldWhitelist(player, args[1], args[3]);
			}
			else if (args[0].equalsIgnoreCase("set-portal")){
				try {
					if (args[0].equalsIgnoreCase("water") || args[0].equalsIgnoreCase("still_water") || args[0].equalsIgnoreCase("STATIONARY_WATER")){
						WorldControlHandler.portalCreationInfo.put(player, args[1].toLowerCase() + "," +  args[2].toLowerCase() + "," +  Material.STATIONARY_WATER.name());
						player.getInventory().addItem(new ItemStack(Material.STONE_HOE));
						ChatUtili.sendTranslatedMessage(player, "&dPlease select two locations for the portal with a stone hoe.");
					}
					else {
						if (WorldControl.materialExists(Material.getMaterial(args[3].toUpperCase()))){
							WorldControlHandler.portalCreationInfo.put(player, args[1].toLowerCase() + "," +  args[2].toLowerCase() + "," +  args[3].toUpperCase());
							player.getInventory().addItem(new ItemStack(Material.STONE_HOE));
							ChatUtili.sendTranslatedMessage(player, "&dPlease select two locations for the portal with a stone hoe.");
						}
						else {
								ChatUtili.sendTranslatedMessage(player, "&cMaterial '" + args[3] + "' is invalid.");
						}
					}
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(player, "Invalid replacement block.");
				}
			}
			else if (args[0].equalsIgnoreCase("modify")){
				if (WorldControl.worldFolderExists(args[1])){
					try {
						WorldControl.modifyWorldEnviorment(args[1], Environment.valueOf(args[2].toUpperCase()), WorldType.valueOf(args[3].toUpperCase()));
						ChatUtili.sendTranslatedMessage(player, "&aPlease &bunload &athen &bload &aworld '" + args[1] + "' for the changes to take affect!");
					}
					catch (Exception e){
						ChatUtili.sendTranslatedMessage(player, "&cInvalid world type or environment.");
					}
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cWorld '" + args[1] + "' does not exist.");
				}
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  "&cInvalid argument.", "&cPlease type &4/worldcontrol &cto find 'WorldControl' commands.");
			}
			return true;
		}
		
		if (args.length == 5){
			if (args[0].equalsIgnoreCase("create")){
				try {
					WorldControl.createWorld(player, args[1], WorldType.valueOf(args[2].toUpperCase()), Environment.valueOf(args[3].toUpperCase()), Boolean.valueOf(args[4]));
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
					WorldControl.createWorld(player, args[1], WorldType.valueOf(args[2].toUpperCase()), Environment.valueOf(args[3].toUpperCase()), Boolean.valueOf(args[4]), Long.parseLong(args[5]));
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

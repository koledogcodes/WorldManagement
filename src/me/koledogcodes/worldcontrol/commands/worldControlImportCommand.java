package me.koledogcodes.worldcontrol.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutHoverChat;

public class worldControlImportCommand implements CommandExecutor {
	
	//Constrcutor
	//private WorldControl plugin;
	public worldControlImportCommand(WorldControl i) {
		//plugin = i;
	}

	private PacketOutHoverChat packetHoverMessage = new PacketOutHoverChat();
	public static String prefix = ChatUtili.messagePrefix;
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
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc-import inv", " &b- &b(Hover)", "&aThis command simply imports the old per world inv's.");
		ChatUtili.sendTranslatedMessage(player, "&3----- &bPage 1/1 &3-----");
			return true;
		}
		
		if (args.length == 1){
		if (args[0].equalsIgnoreCase("inv")){
			WorldControl.logConsole("Debug: Inventory import is being used.");
			WorldControl.importOldInventories(sender);
			WorldControl.logConsole("Debug: Inventory import has been used.");	
		}
		else {
			packetHoverMessage.sendHoverMessage(player, prefix,  " &cInvalid argument.", "&cPlease type &4/wc-import &cto find 'WorldControlImport' commands.");
		}
			return true;
		}

		return false;
	}
	
	

}

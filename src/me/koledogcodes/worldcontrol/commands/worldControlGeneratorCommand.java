package me.koledogcodes.worldcontrol.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.koledogcodes.worldcontrol.WorldControl;
import me.koledogcodes.worldcontrol.api.WorldGenerator;
import me.koledogcodes.worldcontrol.handler.ChatUtili;
import me.koledogcodes.worldcontrol.handler.WorldControlHandler;
import me.koledogcodes.worldcontrol.wrapped.packets.PacketOutHoverChat;

public class worldControlGeneratorCommand implements CommandExecutor {
	
	public worldControlGeneratorCommand(WorldControl i) {
		
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
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc-gen <generator> <new world>", " &b- &b(Hover)", "&aThis command will simply generate custom worlds. \n&aFor example plugins like plotme.");
		packetHoverMessage.sendHoverMessage(player, prefix + " &3/wc-gen force <generator> <new world>", " &b- &b(Hover)", "&aThis command will force generate non/supported world generators. \n&c&lWarning: &7I am not responsible if something breaks!");
		ChatUtili.sendTranslatedMessage(player, "&3----- &bPage 1/1 &3-----");
			return true;
		}
		
		if (args.length == 1){
			if (WorldControl.getSupportedWorldGenerators().contains(args[0])){
				ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world name for this custom world.");
			}
			else if (args[0].equalsIgnoreCase("?")){
				ChatUtili.sendSimpleTranslatedMessage(player, getSupportedGens());
			}
			else if (args[0].equalsIgnoreCase("force")){
				ChatUtili.sendTranslatedMessage(player, "&cPlease provide a generator to use.");
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  " &cInvalid generator suppilied!", "&cPlease type &4/wc-gen &cto find 'WorldControlGenerator' commands.");
			}
			return true;
		}
		
		if (args.length == 2){
			if (WorldControl.getSupportedWorldGenerators().contains(args[0])){
				WorldControl.generateCustomWorld(player, args[1], WorldGenerator.valueOf(args[0].replaceAll("-", "_")));
			}
			else if (args[0].equalsIgnoreCase("force")){
				ChatUtili.sendTranslatedMessage(player, "&cPlease provide a world name for this custom world.");
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  " &cInvalid generator suppilied!", "&cPlease type &4/wc-gen &cto find 'WorldControlGenerator' commands.");
			}
			return true;
		}
		
		if (args.length == 3){
			if (args[0].equalsIgnoreCase("force")){
				WorldControl.forceGenerateCustomWorld(player, args[2], args[1]);
			}
			else {
				packetHoverMessage.sendHoverMessage(player, prefix,  " &cInvalid generator suppilied!", "&cPlease type &4/wc-gen &cto find 'WorldControlGenerator' commands.");
			}
			return true;
		}
		
		if (args.length >= 4){
			ChatUtili.sendSimpleTranslatedMessage(player, "&cToo many arguments.");
			return true;
		}

		return false;
	}
	
	private String getSupportedGens(){
		String m = "&6Supported Generators: ";
		
		for (String gen: WorldControl.getSupportedWorldGenerators()){
			m += WorldControl.colorTranslate("&c" + gen + ", ");
		}
		
		return m;
	}
	

}

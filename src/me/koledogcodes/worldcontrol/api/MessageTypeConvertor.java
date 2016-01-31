package me.koledogcodes.worldcontrol.api;

import me.koledogcodes.worldcontrol.WorldControl;

public class MessageTypeConvertor {
	
	public MessageTypeConvertor(WorldControl i) {
		
	}

	public static String translateType(MessageType type){
		return type.name().toLowerCase().replaceAll("_", "-");
	}
	
	public static WorldFlag deTranslateType(String type){
		return WorldFlag.valueOf(type.replaceAll("-", "_").toUpperCase());
	}
}

package me.koledogcodes.worldcontrol.wrapped.packets;

public class PacketInjector {

	public PacketInjector() {
		
	}
	
	/*
	public static void injectPlayerPacketStream(Player player){
		NetworkManager manager = ((CraftPlayer) player).getHandle().playerConnection.networkManager;
		
		if (manager.channel.pipeline().get("PacketInjector") == null){
			PacketLHandler h = new PacketLHandler(player);
			manager.channel.pipeline().addBefore("packet_handler", "PacketInjector", h);
		}
	}
	
	public static void uninjectPlayerPacketStream(Player player){
		NetworkManager manager = ((CraftPlayer) player).getHandle().playerConnection.networkManager;
		
		if (manager.channel.pipeline().get("PacketInjector") != null){
			manager.channel.pipeline().remove("PacketInjector");
		}
	}
	*/
}

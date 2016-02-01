package me.koledogcodes.worldcontrol.wrapped.packets;

import org.bukkit.entity.Player;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketListenerHandler extends ChannelDuplexHandler {
	
	@SuppressWarnings("unused")
	private Player p;

	public PacketListenerHandler(Player p) {
		this.p = p;
	}
	  
	  @Override
	  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		  super.write(ctx, msg, promise);
	  }
	  
	  @Override
	  public void channelRead(ChannelHandlerContext c, Object m) throws Exception {
	     if (m.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInBlockPlace")) {
	    	 
	     } 
	     else {
	    	 super.channelRead(c, m);
	     }
	  }
	
}
	  

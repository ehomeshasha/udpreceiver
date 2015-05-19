package com.jiuletech;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

public class UDPServerMain {

	
	
	private final EventLoopGroup group;  
    private final Bootstrap bootstrap;  
  
    public UDPServerMain(InetSocketAddress address) {  
        group = new NioEventLoopGroup();  
        bootstrap = new Bootstrap();  
        bootstrap.group(group).channel(NioDatagramChannel.class)  
                .option(ChannelOption.SO_BROADCAST, true)  
                .handler(new UDPServerHandler()).localAddress(address);  
    }  
  
    public Channel bind() {  
        return bootstrap.bind().syncUninterruptibly().channel();  
    }  
  
    public void stop() {  
        group.shutdownGracefully();  
    } 
	
	
	
	public static void main(String[] args) throws InterruptedException {
		int port = Integer.parseInt(args[0]);
		UDPServerMain server = new UDPServerMain(new InetSocketAddress(
				port));
		try {
			Channel channel = server.bind();
			System.out.println("LogEventMonitor running");
			channel.closeFuture().sync();
		} finally {
			server.stop();
		}
		
		
		
		
		
		
	}

}

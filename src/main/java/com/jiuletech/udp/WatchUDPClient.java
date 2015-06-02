package com.jiuletech.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public final class WatchUDPClient {

    static final int PORT = Integer.parseInt(System.getProperty("port", "3344"));

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioDatagramChannel.class)
             .option(ChannelOption.SO_BROADCAST, true)
             .handler(new WatchUDPClientHandler());

            Channel ch = b.bind(0).sync().channel();
            
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("55DD01110113747100014100000151180001400000001060000000000000014000217710000001F54F6B4900000000000000011423.23563030.8330", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", PORT))).sync();
            
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("55DD0007018986011474710000404300001511000000000000000000000000000000000000OV53V1.00.01516B8180000000000000000000000000000000000", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", PORT))).sync();
            
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("55DD000701137471000127200001511000000000000000000000000000000000000OV53V1.00.01516B8180000000000000000000000000000000000", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", PORT))).sync();
            
            
            if (!ch.closeFuture().await(5000)) {
                System.err.println("UDP request timed out.");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
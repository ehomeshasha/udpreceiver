/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.jiuletech;

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

/**
 * A UDP broadcast client that asks for a quote of the moment (QOTM) to {@link QuoteOfTheMomentServer}.
 *
 * Inspired by <a href="http://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html">the official
 * Java tutorial</a>.
 */
public final class QuoteOfTheMomentClient {

    static final int PORT = Integer.parseInt(System.getProperty("port", "3344"));

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioDatagramChannel.class)
             .option(ChannelOption.SO_BROADCAST, true)
             .handler(new QuoteOfTheMomentClientHandler());

            Channel ch = b.bind(0).sync().channel();

            // Broadcast the QOTM request to port 8080.
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("55DD01110113747100014100000151180001400000001060000000000000014000217710000001F54F6B4900000000000000011423.23563030.8330", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", PORT))).sync();
            
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("55DD000701147471000095500003055000000000000000000000000000000000000OV53V1.00.0154F6B044031300000000000000000000000000000", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", PORT))).sync();
            
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("55DD000701898601137471000137301000000000000000000000000000000000000OV53V1.00.06516B8180000000000000000000000000000000000", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", PORT))).sync();

            // QuoteOfTheMomentClientHandler will close the DatagramChannel when a
            // response is received.  If the channel is not closed within 5 seconds,
            // print an error message and quit.
            if (!ch.closeFuture().await(5000)) {
                System.err.println("QOTM request timed out.");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
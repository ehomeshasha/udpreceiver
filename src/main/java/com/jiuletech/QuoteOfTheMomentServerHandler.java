package com.jiuletech;

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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.Random;

import org.apache.log4j.Logger;

import com.jiuletech.common.MsgBean;
import com.jiuletech.common.MsgParser;
import com.jiuletech.mysql.DbHelper;
import com.jiuletech.mysql.MySQLRunner;
import com.jiuletech.util.GsonUtils;

public class QuoteOfTheMomentServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	
	
	private static final Logger LOG = Logger.getLogger(QuoteOfTheMomentServerHandler.class);

//    private static final Random random = new Random();

//    // Quotes from Mohandas K. Gandhi:
//    private static final String[] quotes = {
//        "Where there is love there is life.",
//        "First they ignore you, then they laugh at you, then they fight you, then you win.",
//        "Be the change you want to see in the world.",
//        "The weak can never forgive. Forgiveness is the attribute of the strong.",
//    };

//    private static String nextQuote() {
//        int quoteId;
//        synchronized (random) {
//            quoteId = random.nextInt(quotes.length);
//        }
//        return quotes[quoteId];
//    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        System.err.println(packet);
        
        ByteBuf buf = (ByteBuf) packet.copy().content();
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		
		
		
		
		LOG.info("msgbody=" + body);
		
		MsgParser msgParser = new MsgParser(body);
		MsgBean msgBean = msgParser.parse();
		
		
		//GsonUtils.print("ParsedMsgBean=", msgBean);
		LOG.info("ParsedMsgBean=" + GsonUtils.toJson(msgBean));
		
		MySQLRunner mysqlRunner = new MySQLRunner(DbHelper.getQueryRunner());
		mysqlRunner.insertMsg2Mysql(msgBean);
		
		
		ResponseHandler responseHandler = new ResponseHandler(msgBean);
		String response = responseHandler.getResponse();
		LOG.info("response="+response);
		ctx.write(new DatagramPacket(
              Unpooled.copiedBuffer(response, CharsetUtil.UTF_8), packet.sender()));
		
		
		
        
//        if ("QOTM?".equals(packet.content().toString(CharsetUtil.UTF_8))) {
//            ctx.write(new DatagramPacket(
//                    Unpooled.copiedBuffer("QOTM: " + nextQuote(), CharsetUtil.UTF_8), packet.sender()));
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // We don't close the channel because we can keep serving requests.
    }
}
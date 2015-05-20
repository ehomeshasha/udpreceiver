package com.jiuletech;


import org.apache.log4j.Logger;

import com.jiuletech.common.MsgBean;
import com.jiuletech.common.MsgParser;
import com.jiuletech.mysql.DbHelper;
import com.jiuletech.mysql.MySQLRunner;
import com.jiuletech.util.GsonUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;


public class UDPServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {
	
	
	private static final Logger LOG = Logger.getLogger(UDPServerHandler.class);
	
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		// We don't close the channel because we can keep serving requests.
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			DatagramPacket packet) throws Exception {
		// TODO Auto-generated method stub
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
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
		// System.out.println("I got it!");
	}

}
package com.jiuletech;


import com.jiuletech.mysql.DbHelper;
import com.jiuletech.mysql.MySQLRunner;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;


public class UDPServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {
	
	private MySQLRunner mysqlRunner = new MySQLRunner(DbHelper.getQueryRunner());
	
	
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
		System.out.println("body:"+body);
		//mysqlRunner.insertMysql(tbname, obj);
		
		
		
		
		System.out.println(body);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
		// System.out.println("I got it!");
	}

}
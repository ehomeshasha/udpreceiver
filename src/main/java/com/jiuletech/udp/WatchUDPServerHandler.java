package com.jiuletech.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import org.apache.log4j.Logger;

import com.jiuletech.common.MsgBean;
import com.jiuletech.common.MsgParser;
import com.jiuletech.mysql.DbHelper;
import com.jiuletech.mysql.MySQLRunner;
import com.jiuletech.util.GsonUtils;

public class WatchUDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	
	
	private static final Logger LOG = Logger.getLogger(WatchUDPServerHandler.class);


    @Override
    public void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        System.err.println(packet);
        
        ByteBuf buf = (ByteBuf) packet.copy().content();
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		
		
		
		
		LOG.info("请求消息体=" + body);
		
		MsgParser msgParser = new MsgParser(body);
		MsgBean msgBean = msgParser.parse();
		
		
		//GsonUtils.print("ParsedMsgBean=", msgBean);
		LOG.info("ParsedMsgBean=" + GsonUtils.toJson(msgBean));
		
		MySQLRunner mysqlRunner = new MySQLRunner(DbHelper.getQueryRunner());
		mysqlRunner.insertMsg2Mysql(msgBean);
		
		
		ResponseHandler responseHandler = new ResponseHandler(msgBean);
		String response = responseHandler.getResponse();
		LOG.info("响应字符串="+response);
		ctx.write(new DatagramPacket(
              Unpooled.copiedBuffer(response, CharsetUtil.UTF_8), packet.sender()));
		
        
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
package com.jiuletech.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;

import java.io.UnsupportedEncodingException;

public class ByteBufUtils {
	 public static String getBody(CompositeByteBuf bodyBuf) {
			
			CompositeByteBuf finalCompositeByteBuf = bodyBuf.consolidate();
			finalCompositeByteBuf.writerIndex(finalCompositeByteBuf.capacity());
			//System.out.println("finalCompositeByteBuf.readableBytes()="+finalCompositeByteBuf.readableBytes());
			//System.out.println("finalCompositeByteBuf.writableBytes()="+finalCompositeByteBuf.writableBytes());
			byte[] bodyBytes = getContentBytes(finalCompositeByteBuf);
			return getBody(bodyBytes);
		}
	    
	    
	    public static byte[] getContentBytes(ByteBuf content) {

			byte[] contentByte = new byte[content.readableBytes()];  
			content.readBytes(contentByte);  
			content.release();  
			return contentByte;

		}
	    public static String getBody(byte[] bodyBytes) {
			return getBody(bodyBytes,  "UTF-8");
		}
	    
	    
	    private static String getBody(byte[] bodyBytes,
				String encoding) {
			
			String body = null;
			if (bodyBytes.length == 0) {
				return null;
			}

//			if (contentEncoding == null) {
		
				try {
					body = new String(bodyBytes, encoding);
		
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
//			} else if (contentEncoding.equals(Constants.GZIP)) {
//				// 处理gzip
//				// int ss = (responseBytes[0] & 0xff) | ((responseBytes[1] & 0xff)
//				// << 8);
//				GZIPInputStream in = null;
//				try {
//					in = new GZIPInputStream(new ByteArrayInputStream(responseBytes));
//					body = StreamUtils.stream2String(in);
	//	
//				} catch (IOException e) {
	//	
//					try {
//						body = new String(responseBytes, encoding);
	//	
//					} catch (UnsupportedEncodingException e1) {
//						e1.printStackTrace();
//					}
	//	
//					e.printStackTrace();
//				} finally {
	//	
//					if (in != null) {
//						try {
//							in.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
	//	
//				}
//			}
			return body;
		}
}

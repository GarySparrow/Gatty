package transport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import common.MessageType;
import exchange.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import exchange.Request;
import exchange.Header;
import sun.rmi.runtime.Log;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter{
	private volatile ScheduledFuture<?> heartBeat;
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		Message message = (Message) msg;
		Header header = message.getHeader();
		if (header != null) {
			if (header.getType() == MessageType.LOGIN_RESP.value()) {
				if (checkInHeader(header, "keep-alive")) {
					heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 1000, TimeUnit.MILLISECONDS);
				} else {
					ctx.fireChannelRead(msg);
				}
			} else if (header.getType() == MessageType.HEARTBEAT_RESP.value()) {
				logger.info("Client receive server heart beat message : ---> " + message);
			} else {
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}


	}
	
//	@Override
//	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//		// TODO Auto-generated method stub
//		GattyMessage message = (GattyMessage) msg;
//		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
//			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
//		} else if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
//			System.out.println("Client receive server heart beat message : ---> " + message);
//		} else {
//			ctx.fireChannelRead(message);
//		}
//	}
	
	private class HeartBeatTask implements Runnable {
		private final ChannelHandlerContext ctx;
		private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

		public HeartBeatTask(final ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message heartBeat = buildHeartBeatReq();
			logger.info("Client send heart beat message to server : ---> " + heartBeat);
			ctx.writeAndFlush(heartBeat);
		}
		
		private Message buildHeartBeatReq() {
			Message message = new Request();
			Header header = new Header();
			header.setType(MessageType.HEARTBEAT_REQ.value());
			message.setHeader(header);
			return message;
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		if (heartBeat != null) {
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}

	private boolean checkInHeader(Header header, String checked) {
		Map<String, Object> attachment = header.getAttachment();
		if (attachment != null) {
			if (attachment.containsKey(checked) && attachment.get(checked).equals(true)) {
				return true;
			}
		}
		return false;
	}
}

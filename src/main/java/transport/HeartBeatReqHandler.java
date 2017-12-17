package transport;

import java.util.concurrent.TimeUnit;

import common.MessageType;
import exchange.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import exchange.Request;
import exchange.Header;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter{
	private volatile ScheduledFuture<?> heartBeat;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub

		Message message = (Message) msg;
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
		} else if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
			System.out.println("Client receive server heart beat message : ---> " + message);
		} else {
			ctx.fireChannelRead(message);
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
		
		public HeartBeatTask(final ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message heartBeat = buildHeartBeatReq();
			System.out.println("Client send heart beat message to server : ---> " + heartBeat);
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
}

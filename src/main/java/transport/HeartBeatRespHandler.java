package transport;


import common.MessageType;
import exchange.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.Invocation;
import protocol.InvokeFilter;
import protocol.URLInvoker;

import java.util.logging.Logger;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter{

	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		Message message = (Message) msg;
		
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
			logger.info("Receive transport heart beat : ---> " + message);
			Message heartBeat = buildHeartBeatResp();
			logger.info("Send heart beat response message : ---> " + heartBeat);
			ctx.writeAndFlush(heartBeat);
			URL url = (URL) message.getBody();
			new Invocation(new URLInvoker(url));
		} else {
			ctx.fireChannelRead(message);
		}
	}

	private Message buildHeartBeatResp() {
		Message message = new Response();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.fireExceptionCaught(cause);
	}
}

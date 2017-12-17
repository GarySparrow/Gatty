package transport;


import common.MessageType;
import exchange.Message;
import exchange.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import exchange.Request;
import exchange.Header;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		Message message = (Message) msg;
		
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
			System.out.println("Receive transport heart beat : ---> " + message);
			Message heartBeat = buildHeartBeatResp();
			System.out.println("Send heart beat response message : ---> " + heartBeat);
			ctx.writeAndFlush(message);
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

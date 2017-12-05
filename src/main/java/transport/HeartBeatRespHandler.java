package transport;


import common.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import exchange.Request;
import exchange.Header;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		Request message = (Request) msg;
		
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
			System.out.println("Receive transport heart beat : ---> " + message);
			Request heartBeat = buildHeartBeat();
			System.out.println("Send heart beat response message : ---> " + heartBeat);
			ctx.writeAndFlush(message);
		} else {
			ctx.fireChannelRead(message);
		}
	}

	private Request buildHeartBeat() {
		Request message = new Request();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
		return message;
	}
}

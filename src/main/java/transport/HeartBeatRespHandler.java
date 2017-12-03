package transport;


import common.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import exchange.model.GattyMessage;
import exchange.model.Header;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		GattyMessage message = (GattyMessage) msg;
		
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
			System.out.println("Receive transport heart beat : ---> " + message);
			GattyMessage heartBeat = buildHeartBeat();
			System.out.println("Send heart beat response message : ---> " + heartBeat);
			ctx.writeAndFlush(message);
		} else {
			ctx.fireChannelRead(message);
		}
	}

	private GattyMessage buildHeartBeat() {
		GattyMessage message = new GattyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
		return message;
	}
}

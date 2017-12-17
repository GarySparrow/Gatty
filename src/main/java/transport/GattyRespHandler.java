package transport;

import common.MessageType;
import exchange.Header;
import exchange.Message;
import exchange.Request;
import exchange.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GattyRespHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("resphandler " + msg.toString());
		super.channelRead(ctx, msg);
		Message response = (Message) msg;
		if (response.getHeader() != null && response.getHeader().getType() == MessageType.GATTY_REQ.value()) {
			Header header = response.getHeader();
			// handle the return
			System.out.println("receive the gatty request");
			ctx.writeAndFlush(buildGattyResponse());
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
	private Message buildGattyResponse() {
		Message response = new Response();
		Header header = new Header();
		header.setType(MessageType.GATTY_REQ.value());
		response.setHeader(header);
		return response;
	}
}

package transport;

import common.MessageType;
import exchange.Header;
import exchange.Request;
import exchange.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GattyRespHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		Response response = (Response) msg;
		if (response.getHeader() != null && response.getHeader().getType() == MessageType.GATTY_REQ.value()) {
			Header header = response.getHeader();
			// handle the return
		}
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
}

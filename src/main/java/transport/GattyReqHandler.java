package transport;

import common.MessageType;
import exchange.Message;
import exchange.Request;
import exchange.URL;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GattyReqHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		Request req = (Request) msg;
		if (req.getHeader() != null && req.getHeader().getType() == MessageType.GATTY_RESP.value()) {
			URL url = (URL) req.getBody();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
}

package transport;


import common.MessageType;
import exchange.Header;
import exchange.Message;
import exchange.Request;
import exchange.Response;
import exchange.URL;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.InvokeFilter;
import protocol.Invoker;
import protocol.URLInvoker;

import java.util.logging.Logger;

public class GattyReqHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		Message res = (Message) msg;
		if (res.getHeader() != null && res.getHeader().getType() == MessageType.GATTY_RESP.value()) {
			Object ret = res.getBody();
			logger.info(ret.toString());
			ctx.close();
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

}

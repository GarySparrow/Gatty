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
import registry.RedisRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class InvokerReqHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		Message res = (Message) msg;
		Header header = res.getHeader();
		if (header != null) {
			if (header.getType() == MessageType.INVOKE_RESP.value()) {
				Object ret = res.getBody();
				RedisRegistry.pushRet(ret);
				logger.info("receive return message: " + ret.toString());
			}
			ctx.fireChannelRead(msg);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	private boolean checkInHeader(Header header, String checked) {
		Map<String, Object> attachment = header.getAttachment();
		if (attachment != null) {
			if (attachment.containsKey(checked) && attachment.get(checked).equals(true)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}

package transport;

import common.MessageType;
import exchange.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.Invocation;
import protocol.URLInvoker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class InvokerRespHandler extends ChannelInboundHandlerAdapter{

	private ExecutorService tp = Executors.newFixedThreadPool(8);
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		Message req = (Message) msg;
		Header header = req.getHeader();
		if (header != null) {
			if (header.getType() == MessageType.INVOKE_REQ.value()) {
				// handle the return
				String urlStr = (String) req.getBody();
				URL url = URL.translate(urlStr);
				Object ret = new Invocation(new URLInvoker(url)).process(ctx);
				logger.info("send invoke response: " + ret);
				ctx.writeAndFlush(buildInvokeResponse(ret));
				ctx.close();
			} else {
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
	private Message buildInvokeResponse(Object ret) {
		Message req = new Request();
		Header header = new Header();
		header.setType(MessageType.INVOKE_REQ.value());
		req.setHeader(header);
		req.setBody(ret);
		return req;
	}


}

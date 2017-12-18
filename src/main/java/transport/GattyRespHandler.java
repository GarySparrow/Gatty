package transport;

import common.MessageType;
import exchange.Header;
import exchange.Message;
import exchange.Request;
import exchange.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GattyRespHandler extends ChannelInboundHandlerAdapter{

	private ExecutorService tp = Executors.newFixedThreadPool(8);
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		Message response = (Message) msg;
		if (response.getHeader() != null && response.getHeader().getType() == MessageType.GATTY_REQ.value()) {
			Header header = response.getHeader();
			// handle the return

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

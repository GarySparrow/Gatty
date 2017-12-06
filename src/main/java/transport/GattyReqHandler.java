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

public class GattyReqHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		Request req = (Request) msg;
		if (req.getHeader() != null && req.getHeader().getType() == MessageType.GATTY_RESP.value()) {
			URL url = (URL) req.getBody();
			Invoker invoke = new URLInvoker(url);
			
		}
	}
	
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
	private Response buildGattyResponse() {
		Response response = new Response();
		Header header = new Header();
		header.setType(MessageType.GATTY_REQ.value());
		response.setHeader(header);
		return response;
	}
}

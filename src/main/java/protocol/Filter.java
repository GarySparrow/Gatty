package protocol;

import exchange.URL;
import io.netty.channel.ChannelHandlerContext;

//white list or something need to be done before invoke.
public interface Filter {
	boolean intercept(ChannelHandlerContext ctx);
}

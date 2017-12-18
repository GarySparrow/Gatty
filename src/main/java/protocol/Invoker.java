package protocol;

import java.lang.reflect.Method;

import exchange.URL;
import io.netty.channel.ChannelHandlerContext;

public interface Invoker {
	Object invoke(ChannelHandlerContext ctx);
}

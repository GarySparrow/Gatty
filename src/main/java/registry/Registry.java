package registry;

/**
 * Created by hasee on 2017/12/23.
 */
import exchange.Request;
import exchange.URL;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import protocol.Invoker;

public interface Registry {

    void regist(URL url);

}

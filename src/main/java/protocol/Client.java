package protocol;
import common.MessageType;
import exchange.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import transport.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import common.GattyConstant;

/**
 * Created by hasee on 2017/11/26.
 */
public class Client {

	private ChannelFuture future;
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    
    public Bootstrap connect() throws Exception{
//    	try {
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast("MessageDecoder", new GattyDecoder(1024 * 1024, 4, 4, -8, 0));
					ch.pipeline().addLast("MessageEncoder", new GattyEncoder());
					ch.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(10));
					ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
					ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
					ch.pipeline().addLast("InvokerHandler", new InvokerReqHandler());
				}
			});
		return b;
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			executor.execute( () -> {
//				try {
//					TimeUnit.SECONDS.sleep(5);
//					try {
//						connect(GattyConstant.REMOTEIP, GattyConstant.PORT);
//					} catch (Exception e) {
//						// TODO: handle exception
//						e.printStackTrace();
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			});
//		}
    }

}

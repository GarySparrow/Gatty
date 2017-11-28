package others;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import connection.GattyMessageDecoder;
import connection.GattyMessageEncoder;
import connection.HeartBeatReqHandler;
import connection.LoginAuthReqHandler;

/**
 * Created by hasee on 2017/11/26.
 */
public class GattyClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    
    public void connect(String host, int port) throws Exception{
    	try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new GattyMessageDecoder(1024 * 1024,
								4, 4, -8, 0));
						ch.pipeline().addLast("MessageEncoder", new GattyMessageEncoder());
						ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
						ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
						ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
					}
				});
			ChannelFuture future = b.connect(new InetSocketAddress(host, port), 
					new InetSocketAddress(GattyConstant.LOCALIP, GattyConstant.LOCAL_PORT)).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			executor.execute( () -> {
				try {
					TimeUnit.SECONDS.sleep(1);
					try {
						connect(GattyConstant.REMOTEIP, GattyConstant.PORT);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			});
		}
    }
    
    public static void main(String[] args) throws Exception{
    	new GattyClient().connect(GattyConstant.REMOTEIP, GattyConstant.PORT);
    }
}

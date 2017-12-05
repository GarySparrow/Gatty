package protocol;
import common.GattyConstant;
import exchange.GattyDecoder;
import exchange.GattyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import transport.HeartBeatRespHandler;
import transport.LoginAuthRespHandler;

/**
 * Created by hasee on 2017/11/24.
 */
public class Server {
	public void bind() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {			
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
//						ch.pipeline().addLast(new GattyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
						ch.pipeline().addLast("MessageDecoder", new GattyDecoder());
						ch.pipeline().addLast("MessageEncoder", new GattyEncoder());
						ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
						ch.pipeline().addLast("loginAuthHandler", new LoginAuthRespHandler());
						ch.pipeline().addLast("heartBeatHandler", new HeartBeatRespHandler());
					}
				});
			ChannelFuture future = b.bind(GattyConstant.REMOTEIP, GattyConstant.PORT).sync();
			System.out.println("Gatty server start successfully : " + (GattyConstant.REMOTEIP + " : "
					+ GattyConstant.PORT));
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Server().bind();
	}
}
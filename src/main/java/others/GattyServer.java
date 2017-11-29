package others;
import connection.GattyMessageDecoder;
import connection.GattyMessageEncoder;
import connection.HeartBeatRespHandler;
import connection.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by hasee on 2017/11/24.
 */
public class GattyServer {
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
						ch.pipeline().addLast(new GattyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
						ch.pipeline().addLast("MessageEncoder", new GattyMessageEncoder());
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
		new GattyServer().bind();
	}
}

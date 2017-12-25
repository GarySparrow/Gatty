package registry;

import common.GattyConstant;
import common.MessageType;
import exchange.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import protocol.Client;
import redis.clients.jedis.Jedis;
import transport.HeartBeatReqHandler;
import transport.InvokerReqHandler;
import transport.LoginAuthReqHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by hasee on 2017/12/23.
 */
public class RedisRegistry {

    private static Logger logger = Logger.getLogger("RedisRegistry");
    private static final String urls = "urls";
    private static Queue<Request> reqs = new ConcurrentLinkedDeque<>();
    private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    static {
        Runnable check = new Runnable() {
            @Override
            public void run() {
                Request req = pop();
                if (req != null) {
                    logger.info("send request: " + req);
                    request(req);
                }
            }
        };
        service.scheduleAtFixedRate(check, 0, 2, TimeUnit.SECONDS);
    }


    public static void regist(URL url) {
        try {
            String urlStr = URL.translate(url);
            int last = urlStr.lastIndexOf("?");
            if (last == -1) {
                last = urlStr.length();
            }
            urlStr = urlStr.substring(0, last);
            if (!contains(urlStr)) {
                add(urlStr);
            }
        } catch (URLTranslateException e) {
            e.printStackTrace();
        }
    }

    private static void request(Request req) {
        try {
            String urlStr = (String)req.getBody();
            URL url = URL.translate(urlStr);
            if (contains(url)) {
                String host = url.getHost();
                int port = url.getPort();
                Bootstrap bootstrap = new Client().connect();
                ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port),
                        new InetSocketAddress(GattyConstant.LOCALIP, GattyConstant.LOCAL_PORT)).sync();
                future.channel().writeAndFlush(req);
//                future.channel().closeFuture().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void push(Request req) {
        reqs.add(req);
    }

    public static Request pop() {
        if (!reqs.isEmpty()) {
            return reqs.poll();
        }
        return null;
    }

    private static void add(URL url) {
        try {
            add(URL.translate(url));
        } catch (URLTranslateException e) {
            e.printStackTrace();
        }
    }

    private static void add(String url) {
        Jedis jedis = RedisPool.getJedis();
        jedis.sadd(urls, url);
    }

    private static boolean contains(URL url) {
        try {
            return contains(URL.translate(url));
        } catch (URLTranslateException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean contains(String url) {
        Jedis jedis = RedisPool.getJedis();
        int last = url.lastIndexOf("?");
        if (last == -1) {
            last = url.length();
        }
        url = url.substring(0, last);
        return jedis.sismember(urls, url);
    }
}

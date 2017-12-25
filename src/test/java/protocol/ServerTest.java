package protocol;

import common.GattyConstant;
import io.netty.channel.ChannelFuture;
import org.junit.Test;
import registry.RedisRegistry;
import exchange.URL;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee on 2017/12/20.
 */
public class ServerTest {

    private RedisRegistry registry = new RedisRegistry();

    @Test
    public void bind() {
        try {
            Map<String, Object> attachment = new HashMap<>();
            attachment.put("name", "Gatty");
            URL url = new URL("DEFAULT", GattyConstant.REMOTEIP,
                    GattyConstant.PORT, "HelloWorldService",
                    "sayHello", attachment);
            registry.regist(url);
            ChannelFuture future = new Server().bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

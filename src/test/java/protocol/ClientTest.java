package protocol;

import common.GattyConstant;
import common.MessageType;
import exchange.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.junit.Test;
import registry.RedisRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by hasee on 2017/12/20.
 */
public class ClientTest {

    @Test
    public void connect() {
        try {
            Map<String, Object> attachment = new HashMap<>();
            attachment.put("name", "Gatty");
            URL url = new URL("DEFAULT", GattyConstant.REMOTEIP,
                    GattyConstant.PORT, "HelloWorldService",
                    "sayHello", attachment);
            Request req = buildInvokeRequest(url);
            RedisRegistry.push(req);
            while(true) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Request buildInvokeRequest(URL url) {
        Request request = new Request();
        Map<String, Object> attachment = new HashMap<>();
//        attachment.put("keep-alived", true);
        Header header = new Header();
        header.setAttachment(attachment);
        header.setType(MessageType.INVOKE_REQ.value());
        request.setHeader(header);
        try {
            request.setBody(URL.translate(url));
        } catch (URLTranslateException e) {
            e.printStackTrace();
        }
        return request;
    }

    //test cast: DEFAULT://127.0.0.1:9090/HeloWorld:sayHello?name=Gatty
//    private class ReadListener implements Runnable {
//
//        private ChannelFuture future;
//
//        @Override
//        public void run() {
//            System.out.println("invoke your method like this:");
//            System.out.println("protocol://username:password@host:port/path");
//            Scanner scan = new Scanner(System.in);
//            while(scan.hasNext()) {
//                String url = scan.next();
//                Message req = buildGattyRequest(url);
//                future.channel().writeAndFlush(req);
//            }
//        }
//
//        private Message buildGattyRequest(String url) {
//            Message request = new Request();
//            Header header = new Header();
//            header.setType(MessageType.INVOKE_REQ.value());
//            request.setHeader(header);
//            request.setBody(url);
//            return request;
//        }
//    }
}

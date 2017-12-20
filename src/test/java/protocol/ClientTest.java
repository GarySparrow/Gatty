package protocol;

import common.GattyConstant;
import common.MessageType;
import exchange.Header;
import exchange.Message;
import exchange.Request;
import io.netty.channel.ChannelFuture;
import org.junit.Test;

import java.util.Scanner;

/**
 * Created by hasee on 2017/12/20.
 */
public class ClientTest {

    @Test
    public void connect() {
        try {
            new Client().connect(GattyConstant.REMOTEIP, GattyConstant.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //test cast: DEFAULT://127.0.0.1:9090/HeloWorld:sayHello?name=Gatty
    //practice: try with lambda;
    private class ReadListener implements Runnable {

        private ChannelFuture future;

        @Override
        public void run() {
            System.out.println("invoke your method like this:");
            System.out.println("protocol://username:password@host:port/path");
            Scanner scan = new Scanner(System.in);
            while(scan.hasNext()) {
                String url = scan.next();
                Message req = buildGattyRequest(url);
                future.channel().writeAndFlush(req);
            }
        }

        private Message buildGattyRequest(String url) {
            Message request = new Request();
            Header header = new Header();
            header.setType(MessageType.INVOKE_REQ.value());
            request.setHeader(header);
            request.setBody(url);
            return request;
        }
    }
}

package transport;
import exchange.Message;
import exchange.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import exchange.Request;
import exchange.Header;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import common.MessageType;

/**
 * Created by hasee on 2017/11/28.
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String[] whiteList = {"127.0.0.1"};
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());


//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//    	// TODO Auto-generated method stub
//    	GattyMessage message = (GattyMessage) msg;
//
//        if (message.getHeader() != null
//                && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
//            String nodeIndex = ctx.channel().remoteAddress().toString();
//            GattyMessage loginResp = null;
//            if (nodeCheck.containsKey(nodeIndex)) {
//                loginResp = buildResponse((byte) -1);
//            } else {
//                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
//                String ip = address.getAddress().getHostAddress();
//                boolean isOK = false;
//                for (String WIP : whiteList) {
//                    if (WIP.equals(ip)) {
//                        isOK = true;
//                        break;
//                    }
//                }
//                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
//                if (isOK) {
//                    nodeCheck.put(nodeIndex, true);
//                }
//            }
//            System.out.println("The login response is : " + loginResp);
//            ctx.write(loginResp);
//        } 
////        else {
////            ctx.fireChannelRead(msg);
////        }    
//    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	// TODO Auto-generated method stub
        Message message = (Message) msg;

        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            Message loginResp = null;
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whiteList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
                if (isOK) {
                    nodeCheck.put(nodeIndex, true);
                }
            }
            logger.info("The login response is : " + loginResp);
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(msg);
        }    
    }

    private Message buildResponse(byte result) {
        Message message = new Response();
        Header header = new Header();
        Map<String, Object> attachment = new HashMap<>();
//        attachment.put("keep-alive", true);
        header.setType(MessageType.LOGIN_RESP.value());
        header.setAttachment(attachment);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}

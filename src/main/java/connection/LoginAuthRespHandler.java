package connection;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import others.MessageType;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hasee on 2017/11/28.
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter {
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String[] whiteList = {"127.0.0.1"};

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
    	GattyMessage message = (GattyMessage) msg;

        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            GattyMessage loginResp = null;
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
            System.out.println("The login response is : " + loginResp);
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(msg);
        }    
    }

    private GattyMessage buildResponse(byte result) {
        GattyMessage message = new GattyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
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

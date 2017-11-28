package connection;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import others.MessageType;


/**
 * Created by hasee on 2017/11/28.
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GattyMessage message = (GattyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                ctx.close();
            } else {
                System.out.println("Login successfully :" + message);
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private GattyMessage buildLoginReq() {
        GattyMessage message = new GattyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}

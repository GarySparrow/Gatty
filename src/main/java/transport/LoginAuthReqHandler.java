package transport;
import common.MessageType;
import exchange.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import exchange.Request;
import exchange.Header;

import java.util.logging.Logger;


/**
 * Created by hasee on 2017/11/28.
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
        logger.info("sending login request");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                ctx.close();
            } else {
                logger.info("Login successfully :" + message);
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

//	@Override
//	public void read(ChannelHandlerContext ctx) throws Exception {
//		// TODO Auto-generated method stub
//		ctx.writeAndFlush(buildLoginReq());
//	}
//	
//	@Override
//	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//			// TODO Auto-generated method stub
//		GattyMessage message = (GattyMessage) msg;
//		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
//		    byte loginResult = (byte) message.getBody();
//		    if (loginResult != (byte) 0) {
//		        ctx.close();
//		    } else {
//		        System.out.println("Login successfully :" + message);
//		        ctx.writeAndFlush(msg);
//		    }
//		} else {
//		    ctx.fireChannelRead(msg);
//		}
//	}
	
    private Message buildLoginReq() {
        Message message = new Request();
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

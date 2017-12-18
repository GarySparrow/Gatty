package exchange;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import serialize.GattyMarshallingEncoder;
import serialize.MarshallingCodeCFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by hasee on 2017/12/3.
 */
public class GattyEncoder extends MessageToMessageEncoder<Message> {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private GattyMarshallingEncoder marshallingEncoder;

    public GattyEncoder() throws IOException {
        this.marshallingEncoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {

        Header header = msg.getHeader();

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(header.getCrcCode());
        buf.writeInt(header.getLength());
        buf.writeLong(header.getSessionId());
        buf.writeByte(header.getType());
        buf.writeByte(header.getPriority());

        String key = null;
        byte[] keyArray = null;
        Object value = null;

        if (header.getAttachment() != null) {
            buf.writeByte(header.getAttachment().size());
            for (Map.Entry<String, Object> param : header.getAttachment().entrySet()) {
                key = param.getKey();
                keyArray = key.getBytes("UTF-8");
                buf.writeInt(keyArray.length);
                buf.writeBytes(keyArray);
                value = param.getValue();
                marshallingEncoder.encode(ctx, value, buf);
            }
        } else {
            buf.writeInt(0);
        }

        if (msg.getBody() != null) {
            marshallingEncoder.encode(ctx, msg.getBody(), buf);
//            URL url = (URL) msg.getBody();
//            String sb = buildURL(msg);
//            marshallingEncoder.encode(ctx, sb, buf);
//
//            if (url.getAttachment() != null) {
//                buf.writeInt(url.getAttachment().size());
//                for (Map.Entry<String, Object> param : url.getAttachment().entrySet()) {
//                    key = param.getKey();
//                    keyArray = key.getBytes("UTF-8");
//                    buf.writeInt(keyArray.length);
//                    buf.writeBytes(keyArray);
//                    value = param.getValue();
//                    marshallingEncoder.encode(ctx, value, buf);
//                }
//            } else {
//                buf.writeInt(0);
//            }
        }


        int readableBytes = buf.readableBytes();
        buf.setInt(4, readableBytes);

        out.add(buf);
    }

    //copy from dubbo: protocol://username:password@class/method
    private String buildURL(Message msg) {
    	URL url = (URL) msg.getBody();
        StringBuilder sb = new StringBuilder();
        sb.append(url.getProtocol());
        sb.append("://");
        if (!"".equals(url.getUsername())) {
            sb.append(url.getUsername());
            sb.append(":");
            sb.append(url.getPassword());
            sb.append("@");
        }
        sb.append(url.getHost());
        sb.append(":");
        sb.append(url.getPort());
        sb.append("/");
        sb.append(url.getPath());
        return sb.toString();
    }
}

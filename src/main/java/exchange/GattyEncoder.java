package exchange;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import serialize.GattyMarshallingEncoder;
import serialize.MarshallingCodeCFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hasee on 2017/12/3.
 */
public class GattyEncoder extends ChannelOutboundHandlerAdapter implements Encoder {

    private GattyMarshallingEncoder marshallingEncoder;

    public GattyEncoder() throws IOException {
        this.marshallingEncoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        ByteBuf buf = encode(ctx, msg);
        ctx.write(buf);
    }


    @Override
    public ByteBuf encode(ChannelHandlerContext ctx, Object obj) throws Exception{
        Request req = (Request) obj;
        Header header = req.getHeader();
        URL url = (URL) req.getBody();
        
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(header.getCrcCode());
        buf.writeInt(header.getLength());
        buf.writeLong(header.getSessionId());
        buf.writeByte(header.getType());
        buf.writeByte(header.getPriority());

        buf.writeInt(url.getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : url.getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            buf.writeInt(keyArray.length);
            buf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(ctx, value, buf);
        }
        key = null;
        keyArray = null;
        value = null;

        String sb = buildURL(req);
        marshallingEncoder.encode(ctx, value, buf);

        return buf;
    }

    //copy from dubbo: protocol://username:password@class/method
    private String buildURL(Request req) {
    	URL url = (URL) req.getBody();
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

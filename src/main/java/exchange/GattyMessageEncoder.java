package exchange;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import serialize.GattyMarshallingEncoder;
import transport.MarshallingCodeCFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hasee on 2017/12/3.
 */
public class GattyMessageEncoder extends ChannelInboundHandlerAdapter implements Encoder {

    private GattyMarshallingEncoder marshallingEncoder;

    public GattyMessageEncoder() throws IOException {
        this.marshallingEncoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        encode(ctx, msg);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object obj) throws Exception{
        GattyMessage msg = (GattyMessage) obj;
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(msg.getHeader().getCrcCode());
        buf.writeInt(msg.getHeader().getLength());
        buf.writeLong(msg.getHeader().getSessionId());
        buf.writeByte(msg.getHeader().getType());
        buf.writeByte(msg.getHeader().getPriority());

        buf.writeInt(msg.getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getAttachment().entrySet()) {
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

        String sb = buildURL(msg);
        marshallingEncoder.encode(ctx, value, buf);
    }

    //copy from dubbo: protocol://username:password@host:port/path
    private String buildURL(GattyMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg.getProtocol());
        sb.append("://");
        if (!"".equals(msg.getUsername())) {
            sb.append(msg.getUsername());
            sb.append(":");
            sb.append(msg.getPassword());
            sb.append("@");
        }
        sb.append(msg.getHost());
        sb.append(":");
        sb.append(msg.getPort());
        sb.append("/");
        sb.append(msg.getPath());
        return sb.toString();
    }
}

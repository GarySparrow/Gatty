import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2017/11/27.
 */
public class GattyMessageEncoder extends MessageToMessageEncoder<GattyMessage>{

    MarshallingEncoder marshallingEncoder;

    public GattyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, GattyMessage msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionId());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getExternal().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getHeader().getExternal().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes();
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value, sendBuf);
        }

        key = null;
        keyArray = null;
        value = null;
        if (msg.getBody() != null) {
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        } else {
            sendBuf.writeInt(0);
            sendBuf.setInt(4, sendBuf.readableBytes());
        }
    }
}

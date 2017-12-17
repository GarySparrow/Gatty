package transport;
import exchange.Header;
import exchange.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import serialize.GattyMarshallingEncoder;
import serialize.MarshallingCodeCFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2017/11/27.
 */
public class GattyMessageEncoder extends MessageToMessageEncoder<Message> {

    private GattyMarshallingEncoder marshallingEncoder;

    public GattyMessageEncoder() throws IOException {
        this.marshallingEncoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }
        System.out.println("start encoding...");
        Header header = msg.getHeader();
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(header.getCrcCode());
        sendBuf.writeInt(header.getLength());
        sendBuf.writeLong(header.getSessionId());
        sendBuf.writeByte(header.getType());
        sendBuf.writeByte(header.getPriority());


        String key = null;
        byte[] keyArray = null;
        Object value = null;
        if (header.getAttachment() != null) {
            sendBuf.writeInt(header.getAttachment().size());
            for (Map.Entry<String, Object> param : header.getAttachment().entrySet()) {
                key = param.getKey();
                keyArray = key.getBytes("UTF-8");
                sendBuf.writeInt(keyArray.length);
                sendBuf.writeBytes(keyArray);
                value = param.getValue();
                marshallingEncoder.encode(ctx, value, sendBuf);
            }
        } else {
            sendBuf.writeInt(0);
        }

        if (msg.getBody() != null) {
            marshallingEncoder.encode(ctx, msg.getBody(), sendBuf);
        }

        int readableBytes = sendBuf.readableBytes();
        sendBuf.setInt(4, readableBytes);

        System.out.println("encode:" + sendBuf);
        out.add(sendBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}

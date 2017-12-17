package transport;
import exchange.Header;
import exchange.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import serialize.GattyMarshallingDecoder;
import serialize.MarshallingCodeCFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee on 2017/11/27.
 */
public class GattyMessageDecoder extends LengthFieldBasedFrameDecoder{

    private GattyMarshallingDecoder marshallingDecoder;

    public GattyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength
    		, int lengthAdjustment, int initialBytesToStrip) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        marshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("start decoding...");
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) return null;
        Message message = new Message();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attachment = new HashMap<>();
            int keysize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keysize = frame.readInt();
                keyArray = new byte[keysize];
                frame.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                attachment.put(key, marshallingDecoder.decode(ctx,frame));
            }
            header.setAttachment(attachment);
        }
        if (frame.readableBytes() > 0) {
            message.setBody(marshallingDecoder.decode(ctx, frame));
        }
        message.setHeader(header);
        System.out.println(message);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}

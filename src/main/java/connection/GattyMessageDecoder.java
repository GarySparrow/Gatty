package connection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.marshalling.MarshallingDecoder;

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
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) return null;
        GattyMessage message = new GattyMessage();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionId(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());
        
        int size = in.readInt();
        if (size > 0) {
            Map<String, Object> external = new HashMap<>();
            int keysize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keysize = in.readInt();
                keyArray = new byte[keysize];
                in.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                external.put(key, marshallingDecoder.decode(ctx,in));
            }
            keyArray = null;
            key = null;
            header.setExternal(external);
        }
        if (in.readableBytes() > 0) {
            message.setBody(marshallingDecoder.decode(ctx, in));
        }
        message.setHeader(header);
        return message;
    }
}

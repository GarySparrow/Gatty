//package transport;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
//import exchange.model.GattyMessage;
//import exchange.model.Header;
//import serialize.GattyMarshallingDecoder;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by hasee on 2017/11/27.
// */
//public class GattyMessageDecoder extends LengthFieldBasedFrameDecoder{
//
//    private GattyMarshallingDecoder marshallingDecoder;
//
//    public GattyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength
//    		, int lengthAdjustment, int initialBytesToStrip) throws IOException {
//        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
//        marshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
//    }
//
//    @Override
//    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
//        if (frame == null) return null;
//        GattyMessage message = new GattyMessage();
//        Header header = new Header();
//        header.setCrcCode(frame.readInt());
//        header.setLength(frame.readInt());
//        header.setSessionId(frame.readLong());
//        header.setType(frame.readByte());
//        header.setPriority(frame.readByte());
//
//        int size = frame.readInt();
//        if (size > 0) {
//            Map<String, Object> external = new HashMap<>();
//            int keysize = 0;
//            byte[] keyArray = null;
//            String key = null;
//            for (int i = 0; i < size; i++) {
//                keysize = frame.readInt();
//                keyArray = new byte[keysize];
//                frame.readBytes(keyArray);
//                key = new String(keyArray, "UTF-8");
//                external.put(key, marshallingDecoder.decode(ctx,frame));
//            }
//            keyArray = null;
//            key = null;
//            header.setExternal(external);
//        }
//        if (frame.readableBytes() > 0) {
//            message.setBody(marshallingDecoder.decode(ctx, frame));
//        }
//        message.setHeader(header);
//        return message;
//    }
//}

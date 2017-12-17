package exchange;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import serialize.GattyMarshallingDecoder;
import serialize.MarshallingCodeCFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee on 2017/12/3.
 */
public class GattyDecoder extends LengthFieldBasedFrameDecoder {

    private GattyMarshallingDecoder marshallingDecoder;

    public GattyDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength
    		, int lengthAdjustment, int initialBytesToStrip) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        marshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }

//    ByteBuf frame = (ByteBuf) super.decode(ctx, in);
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

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("decode begin...");

        ByteBuf buf = (ByteBuf) super.decode(ctx, in);

        if (buf == null) return null;

        Message msg = new Message();
        Header header = new Header();
        header.setCrcCode(buf.readInt());
        header.setLength(buf.readInt());
        header.setSessionId(buf.readLong());
        header.setType(buf.readByte());
        header.setPriority(buf.readByte());

        int size = buf.readInt();
        if (size > 0) {
            Map<String, Object> attachment = new HashMap<>();
            int keysize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keysize = buf.readInt();
                keyArray = new byte[keysize];
                buf.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                attachment.put(key, marshallingDecoder.decode(ctx,buf));
            }

            header.setAttachment(attachment);
        }


        if (buf.readableBytes() > 0) {
            Object body = marshallingDecoder.decode(ctx,buf);
            msg.setBody(body);
        }
        //copy from dubbo: protocol://username:password@host:port/path
//        if (buf.readableBytes() > 0) {
//            URL url = new URL();
//            String URL = (String) marshallingDecoder.decode(ctx,buf);
//            int start = 0, last = URL.indexOf(":");
//            String protocol = URL.substring(0, last);
//            start = last + 1;
//            String username = "";
//            String password = "";
//            if (URL.contains("@")) {
//                last = URL.indexOf(":", start);
//                username = URL.substring(start, last);
//                start = last + 1;
//                last = URL.indexOf("@");
//                password = URL.substring(start, last);
//                start = last + 1;
//            }
//            String host = "";
//            String port = "";
//            last = URL.indexOf(":", start);
//            host = URL.substring(start, last);
//            start = last + 1;
//            last = URL.indexOf("/", start);
//            port = URL.substring(start, last);
//            String path = "";
//            start = last + 1;
//            path = URL.substring(start, URL.length());
//
//
//            url.setProtocol(protocol);
//            url.setHost(host);
//            url.setUsername(username);
//            url.setPassword(password);
//            url.setPath(path);
//            url.setPort(Integer.valueOf(port));
//
//            size = buf.readInt();
//            if (size > 0) {
//                Map<String, Object> attachment = new HashMap<>();
//                int keysize = 0;
//                byte[] keyArray = null;
//                String key = null;
//                for (int i = 0; i < size; i++) {
//                    keysize = buf.readInt();
//                    keyArray = new byte[keysize];
//                    buf.readBytes(keyArray);
//                    key = new String(keyArray, "UTF-8");
//                    attachment.put(key, marshallingDecoder.decode(ctx,buf));
//                }
//
//                url.setAttachment(attachment);
//            }
//
//            msg.setBody(url);
//        }

        msg.setHeader(header);

        System.out.println("decode: " + msg);
        return msg;
    }
}

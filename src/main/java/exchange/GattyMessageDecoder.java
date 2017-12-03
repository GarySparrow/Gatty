package exchange;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import serialize.GattyMarshallingDecoder;
import transport.MarshallingCodeCFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee on 2017/12/3.
 */
public class GattyMessageDecoder extends ChannelInboundHandlerAdapter implements Decoder{

    private GattyMarshallingDecoder marshallingDecoder;

    public GattyMessageDecoder() throws IOException {
        marshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;
        GattyMessage gatty = (GattyMessage) decode(ctx, buf);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception{
        GattyMessage msg = new GattyMessage();
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
            keyArray = null;
            key = null;
            msg.setAttachment(attachment);
        }
        //copy from dubbo: protocol://username:password@host:port/path
        if (buf.readableBytes() > 0) {
            String URL = (String)marshallingDecoder.decode(ctx,buf);
            int start = 0, last = URL.indexOf(":");
            String protocol = URL.substring(0, last);
            start = last + 1;
            String username = "";
            String password = "";
            if (URL.contains("@")) {
                last = URL.indexOf(":", start);
                username = URL.substring(start, last);
                start = last + 1;
                last = URL.indexOf("@");
                password = URL.substring(start, last);
                start = last + 1;
            }
            String host = "";
            String port = "";
            last = URL.indexOf(":", start);
            host = URL.substring(start, last);
            start = last + 1;
            last = URL.indexOf("/", start);
            port = URL.substring(start, last);
            String path = "";
            start = last + 1;
            path = URL.substring(start, URL.length());


            msg.setProtocol(protocol);
            msg.setHost(host);
            msg.setUsername(username);
            msg.setPassword(password);
            msg.setPath(path);
            msg.setPort(Integer.valueOf(port));
        }
        msg.setHeader(header);

        return msg;
    }
}

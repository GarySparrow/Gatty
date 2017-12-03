package exchange;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by hasee on 2017/12/3.
 */
public interface Decoder {
    Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception;
}

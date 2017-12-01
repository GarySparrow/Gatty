package core;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;

import java.io.IOException;

/**
 * Created by hasee on 2017/11/27.
 */
public class GattyMarshallingEncoder extends MarshallingEncoder {

	public GattyMarshallingEncoder(MarshallerProvider provider) {
		super(provider);
	}
	
	@Override
	public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		super.encode(ctx, msg, out);
	}
}

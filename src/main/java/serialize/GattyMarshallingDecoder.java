package serialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Created by hasee on 2017/11/28.
 */
public class GattyMarshallingDecoder extends MarshallingDecoder {
    
	public GattyMarshallingDecoder(UnmarshallerProvider provider) {
		// TODO Auto-generated constructor stub
		super(provider);
	}
	
	public GattyMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize) {
		// TODO Auto-generated constructor stub
		super(provider, maxObjectSize);
	}
	
	@Override
	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// TODO Auto-generated method stub
		return super.decode(ctx, in);
	}
}

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;

import java.io.IOException;

/**
 * Created by hasee on 2017/11/27.
 */
public class MarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    Marshaller marshaller;

    public MarshallingEncoder() throws IOException {
        marshaller = MarshallingFactory.buildMarshlling();
    }

    protected void encode(Object msg, ByteBuf out) throws Exception {
        int lengthPos = out.writerIndex();
        out.writeBytes(LENGTH_PLACEHOLDER);
        ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
        marshaller.start(output);
        marshaller.writeObject(msg);
        marshaller.finish();
        out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
    }
}

package protocol;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by hasee on 2017/12/18.
 */
public class Invocation {
    private Invoker invoker;
    private Filter invokeFilter;

    public Invocation(Invoker invoker) {
        this(invoker, null);
    }

    public Invocation(Invoker invoker, Filter invokeFilter) {
        this.invoker = invoker;
        this.invokeFilter = invokeFilter;
    }

    public void process(ChannelHandlerContext ctx) {
        if (invokeFilter == null || invokeFilter.intercept(ctx)) {
            invoker.invoke(ctx);
        }
    }
}

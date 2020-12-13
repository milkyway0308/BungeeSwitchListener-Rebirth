package skywolf46.bsl.server.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class BCByteSendingEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        ByteBuf next = ctx.alloc().directBuffer();
        System.out.println("Writing " + byteBuf.readableBytes());
        next.writeInt(byteBuf.readableBytes());
        next.writeBytes(byteBuf);
        byteBuf.release();
        ctx.writeAndFlush(next);
    }
}

package skywolf46.bsl.client.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class BKByteRecvDecoder extends ChannelInboundHandlerAdapter {
    private int nextByte = -1;
    private ByteBuf ready;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (ready == null) {
            ready = ctx.alloc().buffer();
        }
        ready.writeBytes(buf);
        if (nextByte == -1) {
            buf.release();
            if (ready.readableBytes() < 4) {
                return;
            }
            nextByte = ready.readInt();
        }
        System.out.println("Next byte: " + nextByte);
        if (nextByte >= ready.readableBytes()) {
            boolean cleanBuffer = nextByte == ready.readableBytes();
            byte[] read = new byte[nextByte];
            nextByte = -1;
            ready.readBytes(read);
            if (cleanBuffer) {
                ready.clear();
            }
            ByteBuf next = ctx.alloc().directBuffer();
            next.writeBytes(read);
            super.channelRead(ctx, next);
        }
    }
}

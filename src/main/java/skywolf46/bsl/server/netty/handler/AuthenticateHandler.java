package skywolf46.bsl.server.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import skywolf46.bsl.global.BungeeVariables;

public class AuthenticateHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        bb.markReaderIndex();
//        System.out.println(bb.readableBytes());
        if (bb.readableBytes() > 16) {
            if (bb.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_01
                    && bb.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_02
                    && bb.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_03
                    && bb.readInt() == BungeeVariables.BUNGEE_IDENTIFIER_04) {
                removeHandler(ctx);
                injectHandler(ctx);
                writeIdentifier(ctx);
                // Inject handler

                return;
            }
        }
        bb.resetReaderIndex();
        ctx.pipeline().remove(this);
        super.channelRead(ctx, msg);
    }

    private void removeHandler(ChannelHandlerContext context) {
        try {
            while (context.pipeline().names().size() > 0) {
                context.pipeline().removeFirst();
            }
        } catch (Exception ex) {

        }
    }

    private void injectHandler(ChannelHandlerContext ctx) {
        ctx.pipeline().addFirst("recv-handler", new PacketRecvHandler());
        ctx.pipeline().addFirst("buffer-encoder", new LengthFieldPrepender(8));
        ctx.pipeline().addFirst("buffer-decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
        ctx.pipeline().addLast("exception-handler", new BCDisconnectionHandler());
    }

    private void writeIdentifier(ChannelHandlerContext ctx) {
        ByteBuf buf = Unpooled.directBuffer();
        buf.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_01);
        buf.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_02);
        buf.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_03);
        buf.writeInt(BungeeVariables.BUNGEE_IDENTIFIER_04);
        ctx.writeAndFlush(buf);
    }


    private void writeFailed(ChannelHandlerContext ctx) {

    }


}

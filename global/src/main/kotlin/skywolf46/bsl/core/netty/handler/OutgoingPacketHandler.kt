package skywolf46.bsl.core.netty.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IBSLPacket

class OutgoingPacketHandler : ChannelOutboundHandlerAdapter() {
    override fun write(ctx: ChannelHandlerContext, msg: Any?, promise: ChannelPromise?) {
        msg as IBSLPacket
        val writer = BSLCore.resolve(msg.javaClass)
        val buf = ctx.alloc().directBuffer()
        writer.write(buf, msg)
        ctx.write(msg, promise)
        promise!!.addListener {
            it.get()
            buf.release()
        }
    }
}
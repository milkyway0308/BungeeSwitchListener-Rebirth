package skywolf46.bsl.core.netty.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.enums.ListenerType
import skywolf46.bsl.core.util.asLookUp

class OutgoingPacketHandler : ChannelOutboundHandlerAdapter() {
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise?) {
        if (msg is AbstractPacketBase<*>) {
            msg.listen(ListenerType.SEND)
        }
        val writer = BSLCore.resolve(msg.javaClass)
        val buf = ctx.alloc().directBuffer()
        val range = msg.javaClass.asLookUp().toRange()
        try {
            buf.writeInt(range.first).writeInt(range.last)
            writer.writeHeaderData(buf, msg)
            writer.writeFieldData(buf, msg)
            ctx.write(buf, promise)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
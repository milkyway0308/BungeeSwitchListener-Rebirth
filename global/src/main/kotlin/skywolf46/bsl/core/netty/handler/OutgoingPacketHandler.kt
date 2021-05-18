package skywolf46.bsl.core.netty.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.enums.ListenerType
import skywolf46.bsl.core.util.asLookUp

class OutgoingPacketHandler : ChannelOutboundHandlerAdapter() {
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise?) {
        println("Preprocessing ${msg.javaClass}...")
        if(msg is AbstractPacketBase<*>){
            msg.listen(ListenerType.SEND)
        }
        println("Resolving ${msg.javaClass}...")
        val writer = BSLCore.resolve(msg.javaClass)
        println("Resolved, sending..")
        val buf = ctx.alloc().directBuffer()
        val range = msg.javaClass.asLookUp().toRange()
        buf.writeInt(range.first).writeInt(range.last)
        println("Buffer Writing..")
        writer.write(buf, msg, DataMode.HEADER)
        writer.write(buf, msg, DataMode.NON_HEADER)
        println("Writing...")
        println("${buf.readableBytes()} bytes...")
        ctx.write(buf, promise)
        println("Promise..")
//        promise!!.addListener {
//            println("Hello, world.")
//            it.get()
//            buf.release()
//        }
    }
}
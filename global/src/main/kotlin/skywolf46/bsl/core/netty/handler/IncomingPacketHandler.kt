package skywolf46.bsl.core.netty.handler

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.enums.ListenerType
import skywolf46.bsl.core.enums.ReadingMode

class IncomingPacketHandler : ChannelInboundHandlerAdapter() {
    val packetLimitation = 120000
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        msg as ByteBuf
        if (msg.readableBytes() < 8) {
            System.err.println("BSL-Core | Cannot deserialize packet : Packet corrupted (Header < 8 byte)")
            return
        }
        val range = msg.readInt()..msg.readInt()
        // Deserialization
        val lup = BSLCore.classLookup.lookUpValue(range)
        if (lup == null) {
            System.err.println("BSL-Core | Cannot deserialize packet : Structure for [${range.first}~${range.last}] not registered in server")
            msg.release()
            return
        }
        if (packetLimitation <= msg.readableBytes()) {
            System.err.println("BSL-Core | Cannot deserialize packet ${BSLCore.classLookup.lookUp(range)?.name} : Packet flooding (Overflowed ${msg.readableBytes() - packetLimitation} bytes)")
            msg.release()
            return
        }
        val pac = lup.read(msg, ReadingMode.HEADER)
        if (pac !is IBSLPacket) {
            System.err.println("BSL-Core | Deserialization core detected non-packet field; Packet will be ignored.")
            return
        }
        BSLCore.afterProcessor(pac.javaClass).afterRead.forEach {
            it.data.invoke(pac)
        }

        // Release buffer for next packet
        msg.release()
        pac.listen(ListenerType.RECEIVE)
//        super.channelRead(ctx, pac)
//        super.channelRead(ctx, msg)

    }

}
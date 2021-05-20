package skywolf46.bsl.core.impl.packet

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractContainerPacketBase
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.util.asLookUp

class PacketBroadcastPacket(range: IntRange, packet: ByteArray) :
    AbstractContainerPacketBase<PacketBroadcastPacket>(range, packet) {

    constructor() : this(0..0, byteArrayOf())

    constructor(packet: AbstractPacketBase<*>) : this(packet.asLookUp().toRange(),
        BSLCore.resolve(packet::class.java as Class<Any>).let {
            val buf = Unpooled.buffer()
            BSLCore.afterProcessor(packet.javaClass).beforeWrite.forEach {
                it.data.invoke(packet)
            }
            it.writeHeaderData(buf, packet)
            it.writeFieldData(buf, packet)
            return@let buf.array().apply {
                buf.release()
            }
        })

    val lst = mutableListOf<String>()
}
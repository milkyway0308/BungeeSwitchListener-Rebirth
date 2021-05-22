package skywolf46.bsl.core.impl.packet

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractContainerPacketBase
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.util.asLookUp

class PacketRequireProxy(range: IntRange, array: ByteArray, @BSLHeader var targetPort: Int) :
    AbstractContainerPacketBase<PacketRequireProxy>(range, array) {
    constructor() : this(0..0, byteArrayOf(), 0)

    constructor(packet: AbstractPacketBase<*>, port: Int) : this(packet.javaClass.asLookUp().toRange(),
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
        },
        port)


}
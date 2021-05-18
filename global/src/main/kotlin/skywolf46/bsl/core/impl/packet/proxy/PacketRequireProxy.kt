package skywolf46.bsl.core.impl.packet.proxy

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.enums.DataMode

class PacketRequireProxy : AbstractPacketBase<PacketRequireProxy>() {
    companion object {
        fun of(targetPort: Int, packet: IBSLPacket): PacketRequireProxy {

            BSLCore.afterProcessor(packet.javaClass).beforeWrite.forEach {
                it.data.invoke(packet)
            }
            val buf = Unpooled.buffer()

            BSLCore.resolve(packet.javaClass).apply {
                write(buf, packet, DataMode.HEADER)
                write(buf, packet, DataMode.NON_HEADER)
            }
            val proxy = PacketRequireProxy()
            proxy.targetPort = targetPort
            proxy.packets = buf.array()
            buf.release()
            return proxy
        }
    }

    @BSLHeader
    var targetPort: Int = 0

    lateinit var packets: ByteArray

    fun unwrap(): IBSLPacket {
        val buf = Unpooled.wrappedBuffer(packets)
        val range = buf.readInt()..buf.readInt()
        val packet = BSLCore.classLookup.lookUpValue(range)?.apply {
            read(buf, DataMode.HEADER)
            read(buf, DataMode.NON_HEADER)
        }
            ?: throw IllegalStateException("Cannot deserialize packet range in ${range.first}..${range.last}")
        buf.release()
        return packet as IBSLPacket
    }


}
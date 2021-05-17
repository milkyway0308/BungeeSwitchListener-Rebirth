package skywolf46.bsl.core.impl.packet.proxy

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.annotations.BSLAfterRead
import skywolf46.bsl.core.annotations.BSLHeader

class PacketRequireProxy : AbstractPacketBase<PacketRequireProxy>() {
    companion object {
        fun of(targetPort: Int, packet: IBSLPacket): PacketRequireProxy {
            val buf = Unpooled.buffer()
            BSLCore.resolve(packet.javaClass).write(buf, packet)
            val proxy = PacketRequireProxy()
            proxy.targetPort = targetPort
            proxy.packets = buf.array()
            buf.release()
            return proxy
        }
    }

    @BSLHeader
    var targetPort: Int = 0

    @BSLHeader
    lateinit var packets: ByteArray

    fun unwrap(): IBSLPacket {
        val buf = Unpooled.wrappedBuffer(packets)
        val range = buf.readInt()..buf.readInt()
        val packet = BSLCore.classLookup.lookUpValue(range)?.read(buf, false)
            ?: throw IllegalStateException("Cannot deserialize packet range in ${range.first}..${range.last}")
        return packet as IBSLPacket
    }

    @BSLAfterRead
    fun helloWorld() {

    }

}
package skywolf46.bsl.core.impl.packet

import io.netty.buffer.Unpooled
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.enums.DataMode

class PacketReplied() : AbstractPacketBase<PacketReplied>() {
    companion object {
        fun of(server: String, packet: AbstractPacketBase<*>): PacketReplied {
            val rep = PacketReplied()
            val buf = Unpooled.buffer()
            BSLCore.afterProcessor(packet.javaClass).beforeWrite.forEach {
                it.data.invoke(packet)
            }
            BSLCore.resolve(packet.javaClass).apply {
                writeHeaderData(buf, packet)
                writeFieldData(buf, packet)
            }
            rep.server = server
            rep.packet = buf.array()
            buf.release()
            return rep
        }

        fun of(server: String, packet: ByteArray): PacketReplied {
            val rep = PacketReplied()
            rep.server = server
            rep.packet = packet
            return rep
        }
    }

    @BSLHeader
    lateinit var server: String

    lateinit var packet: ByteArray

    fun unwrap(): IBSLPacket {
        val buf = Unpooled.wrappedBuffer(packet)
        val range = buf.readInt()..buf.readInt()
        val lookup = BSLCore.classLookup.lookUpValue(range)
        val packet = lookup?.readHeaderData(buf)
            ?: throw IllegalStateException("Cannot deserialize packet range in ${range.first}..${range.last}")

        packet as AbstractPacketBase<*>
        packet.header.server = header.server
        packet.header.targetName = header.targetName
        BSLCore.afterProcessor(packet.javaClass).afterRead.forEach {
            it.data.invoke(packet)
        }
        lookup.readFieldData(packet, buf)
        buf.release()
        return packet
    }
}
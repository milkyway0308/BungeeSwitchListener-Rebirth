package skywolf46.bsl.core.impl.serializer.primitive

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import java.util.*

class UUIDSerializer : IByteBufSerializer<UUID> {
    override fun ByteBuf.writePacketHeader(data: UUID) {
        writeLong(data.mostSignificantBits)
        writeLong(data.leastSignificantBits)
    }

    override fun ByteBuf.writePacketField(data: UUID) {
        // Do nothing on primitive

    }

    override fun ByteBuf.readPacketHeader(): UUID {
        return UUID(readLong(), readLong())
    }

    override fun ByteBuf.readPacketField(orig: UUID) {
        // Do nothing on primitive

    }

}
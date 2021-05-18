package skywolf46.bsl.core.abstraction

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.enums.DataMode

interface IByteBufSerializer<X : Any> {
    fun ByteBuf.writePacketHeader(data: X)
    fun ByteBuf.writeBuffer(data: X, mode: DataMode)
    fun ByteBuf.readBuffer(readMode: DataMode): X
    fun ByteBuf.readBuffer(orig: X, readMode: DataMode)


    fun write(buf: ByteBuf, data: X, mode: DataMode) {
        buf.writeBuffer(data, mode)
    }

    fun read(data: X, buf: ByteBuf, readMode: DataMode) {
        buf.readBuffer(data, readMode)
    }

    fun read(buf: ByteBuf, readMode: DataMode): X {
        return buf.readBuffer(readMode)
    }

}

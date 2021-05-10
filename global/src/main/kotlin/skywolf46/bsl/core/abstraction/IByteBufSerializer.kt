package skywolf46.bsl.core.abstraction

import io.netty.buffer.ByteBuf

interface IByteBufSerializer<X : Any> {
    fun ByteBuf.writeBuffer(data: X)
    fun ByteBuf.readBuffer(onlyHeader: Boolean): X
    fun write(buf: ByteBuf, data: X) {
        buf.writeBuffer(data)
    }

    fun read(buf: ByteBuf, onlyHeader: Boolean): X {
        return buf.readBuffer(onlyHeader)
    }
}

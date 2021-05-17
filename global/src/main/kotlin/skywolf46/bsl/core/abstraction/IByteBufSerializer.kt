package skywolf46.bsl.core.abstraction

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.enums.ReadingMode

interface IByteBufSerializer<X : Any> {
    fun ByteBuf.writeBuffer(data: X)
    fun ByteBuf.readBuffer(readMode: ReadingMode): X
    fun ByteBuf.readBuffer(orig: X, readMode: ReadingMode)

    fun write(buf: ByteBuf, data: X) {
        buf.writeBuffer(data)
    }



    fun read(data: X, buf: ByteBuf, readMode: ReadingMode){
        buf.readBuffer(data,readMode)
    }

    fun read(buf: ByteBuf, readMode: ReadingMode): X {
        return buf.readBuffer(readMode)
    }

}

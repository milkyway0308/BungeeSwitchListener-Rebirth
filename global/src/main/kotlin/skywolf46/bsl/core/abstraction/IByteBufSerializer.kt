package skywolf46.bsl.core.abstraction

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.enums.DataMode

interface IByteBufSerializer<X : Any> {
    fun ByteBuf.writePacketHeader(data: X)
    fun ByteBuf.writePacketField(data: X)
    fun ByteBuf.readPacketHeader(): X
    fun ByteBuf.readPacketField(orig: X)

    fun writeHeaderData(buf: ByteBuf, data: X) {
        buf.writePacketHeader(data)
    }


    fun writeFieldData(buf: ByteBuf, data: X) {
        buf.writePacketField(data)
    }

    fun readHeaderData(buf: ByteBuf): X {
        return buf.readPacketHeader()
    }

    fun readFieldData(data: X, buf: ByteBuf) {
        buf.readPacketField(data)
    }


    fun readFully(buf: ByteBuf): X {
        val data = readHeaderData(buf)
        readFieldData(data, buf)
        return data
    }

}

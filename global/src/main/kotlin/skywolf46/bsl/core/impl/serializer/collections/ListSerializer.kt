package skywolf46.bsl.core.impl.serializer.collections

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode
import java.lang.IllegalStateException

class ListSerializer : IByteBufSerializer<List<Any>> {
    override fun ByteBuf.writeBuffer(data: List<Any>, mode: DataMode) {
        writeInt(data.size)
        for (x in data)
            BSLCore.resolve(x.javaClass).let {
                it.write(this, x, DataMode.HEADER)
                it.write(this, x, DataMode.NON_HEADER)
            }
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): List<Any> {
        val ret = mutableListOf<Any>()
        for (x in 0 until readInt())
            ret += BSLCore.classLookup.lookUpValue(readInt()..readInt())?.let {
                it.read(this, DataMode.HEADER)
                it.read(this, DataMode.NON_HEADER)
            }
                ?: IllegalStateException("")
        return ret
    }

    override fun ByteBuf.readBuffer(orig: List<Any>, readMode: DataMode) {
        // Not support additional deserialize
    }

    override fun ByteBuf.writePacketHeader(data: List<Any>) {
        // Do nothing on primitive
    }
}
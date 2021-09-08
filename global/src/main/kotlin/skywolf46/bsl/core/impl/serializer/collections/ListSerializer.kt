package skywolf46.bsl.core.impl.serializer.collections

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.util.asLookUp
import java.lang.IllegalStateException

class ListSerializer(val baseProvider: () -> MutableList<Any>) : IByteBufSerializer<List<Any>> {
    override fun ByteBuf.writePacketHeader(data: List<Any>) {
        writeInt(data.size)
        for (x in data)
            BSLCore.resolve(x.javaClass).let {
                val range = x.asLookUp().toRange()
                writeInt(range.first).writeInt(range.last)
                it.writeHeaderData(this, x)
                it.writeFieldData(this, x)
            }
    }

    override fun ByteBuf.writePacketField(data: List<Any>) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): List<Any> {
        val ret = baseProvider()
        for (x in 0 until readInt())
            ret += BSLCore.classLookup.lookUpRangeValue(readInt()..readInt())?.readFully(this)
                ?: IllegalStateException("")
        return ret
    }

    override fun ByteBuf.readPacketField(orig: List<Any>) {
        // Not support additional deserialize
    }


}
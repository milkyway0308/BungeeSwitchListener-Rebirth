package skywolf46.bsl.core.impl.serializer.collections

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.util.asLookUp
import java.lang.IllegalStateException

class MapSerializer(val baseProvider: () -> MutableMap<Any, Any>) : IByteBufSerializer<Map<*, *>> {
    override fun ByteBuf.writePacketHeader(data: Map<*, *>) {
        writeInt(data.size)
        for ((x, y) in data) {
            if (x == null || y == null) {
                throw IllegalStateException("BSL packet write error; BSL not supports null serialization")
            }
            BSLCore.resolve(x.javaClass).let {
                val range = x.asLookUp().toRange()
                writeInt(range.first).writeInt(range.last)
                it.writeHeaderData(this, x)
                it.writeFieldData(this, x)
            }

            BSLCore.resolve(y.javaClass).let {
                val range = y.asLookUp().toRange()
                writeInt(range.first).writeInt(range.last)
                it.writeHeaderData(this, y)
                it.writeFieldData(this, y)
            }
        }
    }

    override fun ByteBuf.writePacketField(data: Map<*, *>) {
        // Do nothing on primitive
    }

    override fun ByteBuf.readPacketHeader(): Map<*, *> {
        val map = baseProvider()
        for (x in 0 until readInt()) {
            var range = readInt()..readInt()
            val key =
                BSLCore.classLookup.lookUpRangeValue(range)?.readFully(this)
                    ?: throw IllegalStateException("Cannot deserialize packet range in ${range.first}..${range.last}")
            range = readInt()..readInt()
            val value =
                BSLCore.classLookup.lookUpRangeValue(range)?.readFully(this)
                    ?: throw IllegalStateException("Cannot deserialize packet range in ${range.first}..${range.last}")
            map[key] = value
        }
        return map
    }

    override fun ByteBuf.readPacketField(orig: Map<*, *>) {
        // Not support additional deserialize
    }
}
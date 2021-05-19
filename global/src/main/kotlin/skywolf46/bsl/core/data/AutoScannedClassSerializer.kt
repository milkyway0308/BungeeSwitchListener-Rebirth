package skywolf46.bsl.core.data

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.annotations.BSLExclude
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.util.ByteBufUtil
import skywolf46.bsl.core.util.CoveredIntRange
import skywolf46.bsl.core.util.asLookUp
import java.lang.IllegalStateException
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.full.companionObjectInstance

class AutoScannedClassSerializer<X : Any>(val cls: Class<X>) : IByteBufSerializer<X> {
    private val fields = mutableListOf<OrderedClass>()
    private val headers = mutableListOf<OrderedClass>()
    private var pairRange: IntRange? = null


    private fun calculateCurrentHash() {
        scanAll(cls as Class<Any>)
        var hashCode = cls.name.hashCode()
        var hashCodeRev = cls.name.hashCode()
        for (e in fields) {
            hashCode = 31 * hashCode + e.hashCode() + e.field.name.hashCode()
            hashCodeRev = 31 * hashCodeRev + e.hashCodeRev() + e.field.name.hashCode()
        }

        for (e in headers) {
            hashCode = 31 * hashCode + e.hashCode() + e.field.name.hashCode()
            hashCodeRev = 31 * hashCodeRev + e.hashCodeRev() + e.field.name.hashCode()
        }
        pairRange = hashCode..hashCodeRev
    }

    override fun ByteBuf.writePacketHeader(data: X) {
        writeInt(pairRange!!.first).writeInt(pairRange!!.last)
    }

    override fun ByteBuf.writeBuffer(data: X, mode: DataMode) {
        if (pairRange == null) {
            calculateCurrentHash()
        }
        val mark = writerIndex()
        try {
            when (mode) {
                DataMode.HEADER -> {
                    writePacketHeader(data)
                    for (x in headers) {
                        BSLCore.resolve(x.field.type as Class<Any>).write(this, x.field.get(data), mode)
                    }
                }
                DataMode.NON_HEADER -> {
                    for (x in fields) {
                        BSLCore.resolve(x.field.type as Class<Any>).write(this, x.field.get(data), mode)
                    }
                }
            }
        } catch (e: Throwable) {
            writerIndex(mark)
            throw e
        }
    }

    override fun ByteBuf.readBuffer(readMode: DataMode): X {
        if (pairRange == null) {
            calculateCurrentHash()
        }
        val const = cls.getDeclaredConstructor()
            .apply {
                isAccessible = true
            }
            .newInstance()
        for (x in when (readMode) {
            DataMode.HEADER -> {
                if (pairRange!!.first != readInt() || pairRange!!.last != readInt()) {
                    throw IllegalStateException("Cannot read data of ${cls.name} : Validator range not equals")
                }
                headers
            }
            DataMode.NON_HEADER -> {
                fields
            }
        }) {
            x.field.set(const,
                BSLCore.resolve(x.field.type).read(this, readMode)
                    ?: throw IllegalStateException("Cannot parse ${x.field.type.name} : Serializer not exists")
            )
        }
        return const as X
    }

    override fun ByteBuf.readBuffer(orig: X, readMode: DataMode) {
        for (x in when (readMode) {
            DataMode.HEADER -> {
                if (pairRange!!.first != readInt() || pairRange!!.last != readInt()) {
                    throw IllegalStateException("Cannot read data of ${cls.name} : Validator range not equals")
                }
                headers
            }
            DataMode.NON_HEADER -> {
                fields
            }
        }) {
            x.field.set(orig,
                BSLCore.resolve(x.field.type).read(this, readMode)
                    ?: throw IllegalStateException("Cannot parse ${x.field.type.name} : Serializer not exists")
            )
        }
    }

    class OrderedClass(val clsOriginal: Class<Any>, val field: Field, val isFinal: Boolean, val isHeader: Boolean) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as OrderedClass
            if (clsOriginal != other.clsOriginal) return false
            if (isFinal != other.isFinal) return false
            if (isHeader != other.isHeader) return false
            return true
        }

        override fun hashCode(): Int {
            var result = CoveredIntRange(clsOriginal.asLookUp().toRange()).hashCode()
            result = 31 * result + isFinal.hashCode()
            result = 31 * result + isHeader.hashCode()
            return result
        }


        fun hashCodeRev(): Int {
            var result = isFinal.hashCode()
            result = 31 * result + CoveredIntRange(clsOriginal.asLookUp().toRange()).hashCode()
            result = 31 * result + isHeader.hashCode()
            return result
        }
    }

    private fun scanAll(cls: Class<Any>) {
        if (cls == Any::class.java)
            return
        val lst = mutableListOf<OrderedClass>()
        val lstHeader = mutableListOf<OrderedClass>()
        for (x in cls.declaredFields) {
            if (x.name == "Companion" && cls.kotlin.companionObjectInstance != null)
                continue
            x.isAccessible = true
            if (Modifier.isFinal(x.modifiers)) {
                println("Warning : Packet class ${cls.name} contains final parameter ${x.name}")
                continue
            }
            if (x.getDeclaredAnnotation(BSLExclude::class.java) != null) {
                continue
            }
            val xi = OrderedClass(cls,
                x,
                false,
                x.getDeclaredAnnotation(BSLHeader::class.java) != null)
            if (xi.isHeader)
                lstHeader.add(xi)
            else
                lst.add(xi)
            BSLCore.resolve(xi.field.type)
        }
        fields.addAll(0, lst)
        headers.addAll(0, lstHeader)
        if (cls.superclass != null)
            scanAll(cls.superclass)
    }


}
package skywolf46.bsl.core.data

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.annotations.BSLExclude
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.util.CoveredIntRange
import skywolf46.bsl.core.util.asLookUp
import java.lang.IllegalStateException
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.jvm.kotlinProperty

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
        if (pairRange == null) {
            calculateCurrentHash()
        }
        writeInt(pairRange!!.first).writeInt(pairRange!!.last)
        for (x in headers) {
            BSLCore.resolve(x.field.type as Class<Any>).writeHeaderData(this, x.field.get(data))
        }
    }

    override fun ByteBuf.writePacketField(data: X) {
        for (x in headers) {
            BSLCore.resolve(x.field.type as Class<Any>).let {
                it.writeFieldData(this, x.field.get(data))
            }
        }

        for (x in fields) {
            BSLCore.resolve(x.field.type as Class<Any>).let {
                it.writeHeaderData(this, x.field.get(data))
                it.writeFieldData(this, x.field.get(data))
            }
        }
    }

    override fun ByteBuf.readPacketHeader(): X {
        if (pairRange == null) {
            calculateCurrentHash()
        }
        val const = cls.getDeclaredConstructor()
            .apply {
                isAccessible = true
            }
            .newInstance()
        if (pairRange!!.first != readInt() || pairRange!!.last != readInt()) {
            throw IllegalStateException("Cannot read data of ${cls.name} : Validator range not equals")
        }
        for (x in headers) {
            x.field.set(const,
                BSLCore.resolve(x.field.type).readHeaderData(this)
                    ?: throw IllegalStateException("Cannot parse ${x.field.type.name} : Serializer not exists")
            )
        }
        return const as X
    }

    override fun ByteBuf.readPacketField(orig: X) {
        for (x in headers) {
            BSLCore.resolve(x.field.type as Class<Any>).readFieldData(x.field.get(orig), this)
        }
        for (x in fields) {
            BSLCore.resolve(x.field.type as Class<Any>).let {
                x.field.set(orig,
                    it.readFully(this)
                )
            }
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
            if (x.declaringClass.kotlin.isCompanion)
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
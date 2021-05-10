package skywolf46.bsl.core.data

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.annotations.annotations.BSLHeader
import java.lang.IllegalStateException
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class AutoScannedClassSerializer<X : Any>(val cls: Class<X>) : IByteBufSerializer<X> {
    private val fields = mutableListOf<OrderedClass>()
    private val headers = mutableListOf<OrderedClass>()
    private lateinit var pairRange: IntRange

    init {
        println("Hello World! Starting ${cls.name}")
        scanAll(cls as Class<Any>)
        calculateCurrentHash()
        println("Scan complete! ${fields.size} is in class ${cls.name}")
    }


    private fun calculateCurrentHash() {
        var hashCode = 1
        var hashCodeRev = 1
        for (e in fields) {
            hashCode = 31 * hashCode + e.hashCode()
            hashCodeRev = 31 * hashCode + e.hashCodeRev()
        }
        pairRange = hashCode..hashCodeRev
    }

    override fun ByteBuf.writeBuffer(data: X) {
        val mark = writerIndex()
        try {
            writeInt(pairRange.first).writeInt(pairRange.last)
            for (x in headers) {
                println(">>> Writing ${x.field.type}")
                BSLCore.resolve(x.field.type as Class<Any>).write(this, x.field.get(data))

            }
            for (x in fields) {
                println(">>> Writing ${x.field.type}")
                BSLCore.resolve(x.field.type as Class<Any>).write(this, x.field.get(data))
            }
        } catch (e: Throwable) {
            writerIndex(mark)
            throw e
        }
    }

    override fun ByteBuf.readBuffer(onlyHeader: Boolean): X {
        if (pairRange.first != readInt() || pairRange.last != readInt()) {
            throw IllegalStateException("Cannot read data of ${cls.name} : Validator range not equals")
        }
        val const = cls.getConstructor().newInstance()
        for (x in headers.toMutableList().also { it.addAll(fields) }) {
            x.field.set(const,
                BSLCore.classLookup.lookUpValue(x.field.type)?.read(this, onlyHeader)
                    ?: throw IllegalStateException("Cannot parse ${x.field.type.name} : Serializer not exists")
            )
        }
        return const as X
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
            var result = clsOriginal.hashCode()
            result = 31 * result + isFinal.hashCode()
            result = 31 * result + isHeader.hashCode()
            return result
        }


        fun hashCodeRev(): Int {
            var result = clsOriginal.hashCode()
            result = 31 * result + isHeader.hashCode()
            result = 31 * result + isFinal.hashCode()
            return result
        }
    }

    private fun scanAll(cls: Class<Any>) {
        if (cls == Any::class.java)
            return
        println("> Scanning ${cls.name}")
        val lst = mutableListOf<OrderedClass>()
        val lstHeader = mutableListOf<OrderedClass>()
        for (x in cls.declaredFields) {
            x.isAccessible = true
            if (Modifier.isFinal(x.modifiers)) {
                println("Warning : Packet class ${cls.name} contains final parameter ${x.name}")
                continue
            }
            println("> Registering field ${x.name} => ${x.type.name}")
            val xi = OrderedClass(cls,
                x,
                false,
                x.getDeclaredAnnotation(BSLHeader::class.java) != null)
            if (xi.isHeader)
                lstHeader.add(xi)
            else
                lst.add(xi)
        }
        fields.addAll(0, lst)
        headers.addAll(0, lstHeader)
        scanAll(cls.superclass)
    }
}
package skywolf46.bsl.core

import io.netty.buffer.ByteBuf
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.data.AutoScannedClassSerializer
import skywolf46.bsl.core.data.CancellableData
import skywolf46.bsl.core.enums.ListenerType
import skywolf46.bsl.core.impl.serializer.primitive.*
import skywolf46.bsl.core.storage.PriorityHandlerHandlerStorage
import skywolf46.bsl.core.util.StringLookup
import skywolf46.bsl.core.util.asLookUp
import java.lang.Exception

object BSLCore {
    val classLookup = StringLookup<Class<Any>, IByteBufSerializer<Any>>()
    val handlerLookup =
        PriorityHandlerHandlerStorage<ListenerType<Any>, CancellableData<AbstractPacketBase>.() -> Unit>()

    fun AbstractPacketBase.listener(
        type: ListenerType<Any>,
        listener: CancellableData<AbstractPacketBase>.() -> Unit,
        priority: Int = 0,
    ) {
        handlerLookup.appendLookup(type, listener, priority)
    }

    fun handle(type: ListenerType<Any>, packet: AbstractPacketBase) {
        val interrupter = CancellableData(packet)
        handlerLookup.of(type)?.forEach { x ->
            try {
                x.data(interrupter)
                if (interrupter.isInterrupted)
                    return
            } catch (e: Throwable) {
                System.err.println("Exception thrown while processing packet ${packet.javaClass.name}")
                e.printStackTrace()
            }
        }
    }

    fun <X : Any> resolve(type: Class<X>): IByteBufSerializer<X> {
        var lup = classLookup.lookUpValue(type)
        if (lup == null) {
            lup = AutoScannedClassSerializer(type as Class<Any>)
            classLookup.append(type.asLookUp(), lup)
        }
        return lup as IByteBufSerializer<X>
    }

    fun register(serializer: IByteBufSerializer<*>, vararg cls: Class<*>) {
        for (c in cls)
            classLookup.append(c.asLookUp().toRange(), serializer as IByteBufSerializer<Any>)
    }

    fun init() {
        register(IntSerializer(), Int::class.java, Int::class.javaPrimitiveType!!)
        register(DoubleSerializer(), Double::class.java, Double::class.javaPrimitiveType!!)
        register(FloatSerializer(), Float::class.java, Float::class.javaPrimitiveType!!)
        register(ByteSerializer(), Byte::class.java, Byte::class.javaPrimitiveType!!)
        register(BooleanSerializer(), Boolean::class.java, Boolean::class.javaPrimitiveType!!)
        register(StringSerializer(), String::class.java)
    }
}
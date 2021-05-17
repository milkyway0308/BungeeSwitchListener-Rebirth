package skywolf46.bsl.core

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.data.AutoScannedClassSerializer
import skywolf46.bsl.core.data.CancellableData
import skywolf46.bsl.core.data.ClassAfterProcessor
import skywolf46.bsl.core.enums.ListenerType
import skywolf46.bsl.core.impl.serializer.primitive.*
import skywolf46.bsl.core.storage.PriorityHandlerHandlerStorage
import skywolf46.bsl.core.util.StringLookup
import skywolf46.bsl.core.util.asLookUp
import skywolf46.extrautility.util.PriorityReference
import java.io.File
import java.lang.reflect.Method
import java.util.*
import java.util.jar.JarFile
import kotlin.collections.ArrayList

object BSLCore {
    val classLookup = StringLookup<Class<Any>, IByteBufSerializer<Any>>()
    val listenerLookup =
        StringLookup<Class<Any>, PriorityHandlerHandlerStorage<ListenerType<Any>, CancellableData<*>.() -> Unit>>()

    private val handlerLookup =
        StringLookup<Class<Any>, List<PriorityReference<Method>>>()
    val afterProcessor = mutableMapOf<Class<Any>, ClassAfterProcessor>()


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
        val prop = Properties()
        prop.load(javaClass.getResourceAsStream("system.properties"))
        println("BSLCore | ...BSL version ${prop["version"]}")
        println("BSLCore | Initializing default serializers")
        register(IntSerializer(), Int::class.java, Int::class.javaPrimitiveType!!)
        register(DoubleSerializer(), Double::class.java, Double::class.javaPrimitiveType!!)
        register(FloatSerializer(), Float::class.java, Float::class.javaPrimitiveType!!)
        register(ByteSerializer(), Byte::class.java, Byte::class.javaPrimitiveType!!)
        register(ShortSerializer(), Short::class.java, Short::class.javaPrimitiveType!!)
        register(BooleanSerializer(), Boolean::class.java, Boolean::class.javaPrimitiveType!!)
        register(StringSerializer(), String::class.java)
        register(ByteArraySerializer(), ByteArray::class.java)
    }

    fun afterProcessor(cls: Class<Any>): ClassAfterProcessor {
        return afterProcessor.computeIfAbsent(cls) {
            return@computeIfAbsent ClassAfterProcessor(cls)
        }
    }

    fun handlerList(lst: Class<Any>): MutableList<PriorityReference<Method>> {
        return handlerLookup.lookUpValueOrDefault(lst.asLookUp().toRange()) {
            object : ArrayList<PriorityReference<Method>>() {
                override fun add(element: PriorityReference<Method>): Boolean {
                    val added = super.add(element)
                    this.sortWith(naturalOrder())
                    return added
                }
            }
        } as MutableList<PriorityReference<Method>>
    }

    fun scanAll(file: File) {
        if (file.extension != "jar") {
            throw IllegalStateException("File extension is not jar")
        }
        val jf = JarFile(file)
        for (x in jf.entries()) {
            if (x.name.endsWith(".class")) {
                try {
                    val cls = Class.forName(x.name.replace("/", ".").let {
                        return@let it.substring(0, it.length - 6)
                    })
                    for (mtd in cls.declaredMethods) {
                        if (mtd.getAnnotation(BSLHandler::class.java) != null) {
                            if (mtd.parameters.size != 1) {
                                System.err.println("BSLCore | Handler ${mtd.name} in ${cls.name} requires 1 parameter")
                                continue
                            }
                            mtd.isAccessible = true
                            val handler = mtd.getAnnotation(BSLHandler::class.java)!!
                            handlerList(mtd.parameters[0].type as Class<Any>).add(PriorityReference(mtd,
                                handler.priority))
                        }
                    }
                } catch (e: Exception) {
                    continue
                }
            }
        }
    }
}


fun <X : AbstractPacketBase<X>> AbstractPacketBase<X>.listener(
    type: ListenerType<Any>,
    listener: CancellableData<X>.() -> Unit,
    priority: Int = 0,
) {
    BSLCore.listenerLookup.lookUpValueOrDefault(
        javaClass.asLookUp().toRange()
    ) { PriorityHandlerHandlerStorage() }
        .appendLookup(type, listener as CancellableData<*>.() -> Unit, priority)
}

fun <X : AbstractPacketBase<X>> AbstractPacketBase<X>.handle(type: ListenerType<Any>, packet: AbstractPacketBase<*>) {
    val interrupter = CancellableData(packet)
    BSLCore.listenerLookup.lookUpValue(packet.asLookUp().toRange())?.of(type)?.forEach { x ->
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


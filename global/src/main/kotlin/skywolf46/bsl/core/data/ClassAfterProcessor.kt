package skywolf46.bsl.core.data

import skywolf46.bsl.core.annotations.BSLAfterRead
import skywolf46.bsl.core.annotations.BSLBeforeWrite
import skywolf46.extrautility.util.PriorityReference
import java.lang.reflect.Field
import java.lang.reflect.Method

class ClassAfterProcessor(val cls: Class<Any>) {
    val beforeWrite = mutableListOf<PriorityReference<Method>>()
    val afterRead = mutableListOf<PriorityReference<Method>>()

    init {
        for (x in cls.declaredMethods) {
            x.isAccessible = true

            if (x.getAnnotation(BSLBeforeWrite::class.java) != null) {
                beforeWrite += PriorityReference(x, x.getAnnotation(BSLBeforeWrite::class.java)!!.priority)
            }
            if (x.getAnnotation(BSLAfterRead::class.java) != null) {
                afterRead += PriorityReference(x, x.getAnnotation(BSLAfterRead::class.java)!!.priority)
            }
        }
    }
}
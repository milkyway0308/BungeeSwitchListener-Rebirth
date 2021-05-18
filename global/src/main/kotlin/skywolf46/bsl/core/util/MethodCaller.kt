package skywolf46.bsl.core.util

import java.lang.reflect.Method

class MethodCaller(val mtd: Method, val instance: Any?) {
    operator fun invoke(vararg params: Any) {
        mtd.invoke(instance, *params)
    }
}
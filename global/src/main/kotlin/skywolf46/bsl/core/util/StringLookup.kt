package skywolf46.bsl.core.util

import java.util.*

open class StringLookup<KEY : Any, VALUE : Any> {
    private val map = mutableMapOf<IntRange, SafeLookup<KEY>>()
    private val mapValue = mutableMapOf<IntRange, VALUE>()

    fun append(lookup: SafeLookup<KEY>) {
        map[lookup.toRange()] = lookup
    }


    fun append(lookup: SafeLookup<KEY>, value: VALUE) {
        map[lookup.toRange()] = lookup
        mapValue[lookup.toRange()] = value
    }


    fun append(lookup: IntRange, value: VALUE) {
//        map[lookup] = lookup
        mapValue[lookup] = value
    }

    fun lookUp(hash: IntRange): KEY? {
        return map[hash]?.data
    }


    fun lookUpValue(hash: IntRange): VALUE? {
        println("Lookup..${hash.first} ${hash.last} => ${mapValue[hash]}")
        return mapValue[hash]
    }

    fun lookUpValueOrDefault(key: IntRange, def: () -> VALUE): VALUE {
        return mapValue[key] ?: run {
            val x = def()
            mapValue[key] = x
            return@run x
        }
    }

    fun lookUpValueOrDefault(key: SafeLookup<KEY>, def: () -> VALUE) = lookUpValueOrDefault(key.toRange(), def)

    fun <X> lookUpValue(cls: Class<X>) = lookUpValue(SafeLookup(cls.name, cls).toRange())
    fun lookUpValue(any: Any) = lookUpValue(any.asLookUp().toRange())
    fun lookUpValue(lookup: SafeLookup<KEY>) = lookUpValue(lookup.toRange())


    class SafeLookup<X : Any> {
        val data: X?

        var hashNormal: Int
            private set
        var hashReversed: Int
            private set

        constructor(dataKey: String, data: X) {
            this.data = data
            hashNormal = dataKey.hashCode()
            hashReversed = dataKey.reversed().hashCode()
        }

        constructor(normal: Int, rev: Int) {
            data = null
            this.hashNormal = normal
            this.hashReversed = rev
        }


        override fun hashCode(): Int {
            return Objects.hash(hashNormal, hashReversed)
        }

        override fun equals(other: Any?): Boolean {
            return other is SafeLookup<*> && other.hashNormal == hashNormal && other.hashReversed == hashReversed
        }

        fun toRange(): IntRange = hashNormal..hashReversed
    }

}

fun <T : Any> T.asLookUp() = StringLookup.SafeLookup(javaClass.name, this)

fun <T : Any> Class<T>.asLookUp(): StringLookup.SafeLookup<Class<T>> {
    println("Looking for class ${name} : ${
        StringLookup.SafeLookup(name, this).toRange().first
    }~${StringLookup.SafeLookup(name, this).toRange().last}")
    return StringLookup.SafeLookup(name, this)
}

fun IntRange.asLookUp() = StringLookup.SafeLookup<Any>(this.first, this.last)

fun <K : Any, V : Any> StringLookup<K, V>.asLookUp() = this
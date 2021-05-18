package skywolf46.bsl.core.util

import java.util.*
import kotlin.collections.HashMap

open class StringLookup<KEY : Any, VALUE : Any> {
    private val map = HashMap<CoveredIntRange, SafeLookup<KEY>>()
    private val mapValue = HashMap<CoveredIntRange, VALUE>()

    fun append(lookup: SafeLookup<KEY>) {
        map[CoveredIntRange(lookup.toRange())] = lookup
    }


    fun append(lookup: SafeLookup<KEY>, value: VALUE) {
        map[CoveredIntRange(lookup.toRange())] = lookup
        mapValue[CoveredIntRange(lookup.toRange())] = value
    }


    fun append(lookup: IntRange, value: VALUE) {
        mapValue[CoveredIntRange(lookup)] = value
    }

    fun lookUp(hash: IntRange): KEY? {
        return map[CoveredIntRange(hash)]?.data
    }


    fun lookUpValue(hash: IntRange): VALUE? {
        return mapValue[CoveredIntRange(hash)]
    }

    fun lookUpValueOrDefault(key: IntRange, def: () -> VALUE): VALUE {
        val cov = CoveredIntRange(key)
        return mapValue[cov] ?: run {
            val x = def()
            mapValue[cov] = x
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
    return StringLookup.SafeLookup(name, this)
}

fun IntRange.asLookUp() = StringLookup.SafeLookup<Any>(this.first, this.last)

fun <K : Any, V : Any> StringLookup<K, V>.asLookUp() = this

package skywolf46.bsl.core.storage

import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.util.StringLookup
import skywolf46.bsl.core.util.asLookUp
import skywolf46.extrautility.util.PriorityReference

class PriorityHandlerHandlerStorage<KEY : Any, VALUE : Any> {
    private val lookUp = StringLookup<KEY, MutableList<PriorityReference<VALUE>>>()

    fun appendLookup(key: KEY, value: VALUE, priority: Int) {
        lookUp.lookUpValueOrDefault(key.asLookUp()) {
            return@lookUpValueOrDefault object : ArrayList<PriorityReference<VALUE>>() {
                override fun add(element: PriorityReference<VALUE>): Boolean {
                    val added = super.add(element)
                    sort()
                    return added
                }
            }
        }.add(PriorityReference(value, priority))
    }

    fun of(range: IntRange) = lookUp.lookUpValue(range)

    fun of(key: KEY) = lookUp.lookUpValue(key.asLookUp().toRange())
}


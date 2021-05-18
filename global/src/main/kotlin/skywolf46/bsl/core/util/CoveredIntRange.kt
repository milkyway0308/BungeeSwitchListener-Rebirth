package skywolf46.bsl.core.util

import java.util.*

class CoveredIntRange(val range: IntRange) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoveredIntRange

        if (range != other.range) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(range.first, range.last)
    }

    override fun toString(): String {
        return range.toString()
    }
}
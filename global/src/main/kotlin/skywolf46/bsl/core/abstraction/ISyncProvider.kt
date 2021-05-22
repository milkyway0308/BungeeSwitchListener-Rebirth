package skywolf46.bsl.core.abstraction

interface ISyncProvider {
    fun sync(unit: () -> Unit)

    fun delayedSync(delayMillisecond: Long, unit: () -> Unit)
}
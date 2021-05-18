package skywolf46.bsl.core.abstraction

interface ISyncProvider {
    fun sync(unit: () -> Unit)
}
package skywolf46.bsl.core.data

data class CancellableData<X>(val data: X) {
    var isInterrupted = false
        private set

    fun interrupt() {
        isInterrupted = true
    }
}
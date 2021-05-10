package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.data.CancellableData
import skywolf46.bsl.core.enums.ListenerType

interface IListenerAttachable {
    fun <X : Any> attach(type: ListenerType<X>, priority: Int, unit: CancellableData<X>.() -> Unit)
}

fun <T : IListenerAttachable, X : Any> attach(
    type: ListenerType<X>,
    priority: Int,
    unit: CancellableData<T>.() -> Unit,
) {
    attach(type, priority, unit)
}

fun <T : IListenerAttachable> attachReceiver(priority: Int, unit: CancellableData<T>.() -> Unit) {
    attach(ListenerType.RECEIVE, priority, unit)
}

fun <T : IListenerAttachable> attachFilter(priority: Int, unit: CancellableData<T>.() -> Unit) {
    attach(ListenerType.SEND, priority, unit)
}


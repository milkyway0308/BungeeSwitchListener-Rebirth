package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.annotations.annotations.BSLHeader
import skywolf46.bsl.core.data.CancellableData
import skywolf46.bsl.core.enums.ListenerType

abstract class AbstractPacketBase : IListenerAttachable {
    @BSLHeader
    var packetSource = -1

    @BSLHeader
    var isResponse = false

    override fun <X : Any> attach(type: ListenerType<X>, priority: Int, unit: CancellableData<X>.() -> Unit) {

    }
}
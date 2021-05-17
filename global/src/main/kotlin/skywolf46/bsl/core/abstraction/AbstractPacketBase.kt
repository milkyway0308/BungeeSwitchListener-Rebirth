package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.data.CancellableData
import skywolf46.bsl.core.data.PacketHeader
import skywolf46.bsl.core.enums.ListenerType
import skywolf46.bsl.core.handle
import skywolf46.bsl.core.listener
import skywolf46.bsl.core.util.asLookUp

abstract class AbstractPacketBase<X : AbstractPacketBase<X>> : IBSLPacket, IListenerAttachable {
    lateinit var originalServer: IBSLServer
        internal set

    @BSLHeader
    var header: PacketHeader<X>? = null

    override fun <X : Any> attach(type: ListenerType<X>, priority: Int, unit: CancellableData<X>.() -> Unit) {
        listener(type as ListenerType<Any>, unit as CancellableData<*>.() -> Unit, priority)
    }

    override fun <X : Any> listen(type: ListenerType<X>) {
        handle(type as ListenerType<Any>, this)
    }
}
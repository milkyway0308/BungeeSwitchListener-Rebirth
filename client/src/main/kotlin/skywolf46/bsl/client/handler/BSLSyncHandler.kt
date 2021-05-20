package skywolf46.bsl.client.handler

import skywolf46.bsl.core.abstraction.AbstractPacketSyncRequest
import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.packet.sync.PacketDataSynchronized
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize

@BSLSideOnly(BSLSide.BUKKIT)
object BSLSyncHandler {

    @BSLHandler
    fun PacketDataSynchronized.onSyncComplete() {
        unwrap().callHandler()
    }

    @BSLHandler
    fun PacketRequestSynchronize.onSyncRequested() {
        val packet = unwrap() as AbstractPacketSyncRequest<*>
        packet.response = this
        packet.callHandler(false)
    }

    @BSLHandler(priority = Integer.MAX_VALUE)
    fun PacketRequestSynchronize.onFinalisingSyncRequested() {
        if (!isResponded) {
            deny()
        }
    }
}
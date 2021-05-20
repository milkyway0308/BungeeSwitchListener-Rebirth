package skywolf46.bsl.client.handler

import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.packet.sync.PacketDataSynchronized
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize

@BSLSideOnly(BSLSide.BUKKIT)
object BSLSyncHandler {

    @BSLHandler
    fun PacketDataSynchronized.onEvent() {
        unwrap().callHandler()
    }

}
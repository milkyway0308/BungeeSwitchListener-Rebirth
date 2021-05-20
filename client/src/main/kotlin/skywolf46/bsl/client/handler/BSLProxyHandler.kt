package skywolf46.bsl.client.handler

import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.packet.PacketReplied

@BSLSideOnly(BSLSide.BUKKIT)
object BSLProxyHandler {
    @BSLHandler
    fun PacketReplied.onProxy() {
        unwrap().callHandler()
    }
}
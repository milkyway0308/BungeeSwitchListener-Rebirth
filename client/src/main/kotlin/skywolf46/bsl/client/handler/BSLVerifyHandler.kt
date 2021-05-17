package skywolf46.bsl.client.handler

import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.impl.packets.server.security.PacketAuthenticateResult
import skywolf46.extrautility.util.log

object BSLVerifyHandler {
    @BSLHandler
    fun verifyHandler(packet: PacketAuthenticateResult) {
        if (!packet.success) {
            log("§eBSL-Core §f| §cFailed to verify client. OpenAPI only connection established.")
            return
        }
        log("§eBSL-Core §f| §aSuccessfully verified client as permission ${packet.securityLevel}")
    }
}
package skywolf46.bsl.client.handler

import org.bukkit.Bukkit
import skywolf46.bsl.client.BSLClient
import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.packet.security.PacketIntroduceSelf
import skywolf46.bsl.core.impl.packet.security.PacketAuthenticateResult
import skywolf46.bsl.core.impl.packet.security.PacketRequestAuthenticate
import skywolf46.extrautility.util.log

@BSLSideOnly(BSLSide.BUKKIT)
object BSLVerifyHandler {
    @BSLHandler
    fun PacketRequestAuthenticate.onRequestAuthenticate() {
        println("Requested! Sending..")
        header.server.send(PacketIntroduceSelf(public, BSLClient.serverName, BSLClient.systemVerify, Bukkit.getPort()))
    }

    @BSLHandler
    fun verifyHandler(packet: PacketAuthenticateResult) {
        if (!packet.success) {
            log("§eBSL-Core §f| §cFailed to verify client. OpenAPI only connection established.")
            return
        }
        log("§eBSL-Core §f| §aSuccessfully verified client as permission ${packet.securityLevel}")
    }
}
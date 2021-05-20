package skywolf46.bsl.client.handler

import org.bukkit.Bukkit
import skywolf46.bsl.client.BungeeSwitchListener
import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.packet.security.PacketIntroduceSelf
import skywolf46.bsl.core.impl.packet.security.PacketAuthenticateResult
import skywolf46.bsl.core.impl.packet.security.PacketRequestAuthenticate
import skywolf46.extrautility.util.log
import java.util.concurrent.atomic.AtomicBoolean

@BSLSideOnly(BSLSide.BUKKIT)
object BSLVerifyHandler {
    private val verified = AtomicBoolean(false)
    var isVerified
        get() = verified.get()
        internal set(value) {
            verified.set(value)
        }

    @BSLHandler
    fun PacketRequestAuthenticate.onRequestAuthenticate() {
        header.server.send(PacketIntroduceSelf(public,
            BungeeSwitchListener.serverName,
            BungeeSwitchListener.systemVerify,
            Bukkit.getPort()))
    }

    @BSLHandler
    fun verifyHandler(packet: PacketAuthenticateResult) {
        verified.set(true)
        if (!packet.success) {
            log("§eBSL-Core §f| §cFailed to verify client. OpenAPI only connection established.")
            return
        }
        log("§eBSL-Core §f| §aSuccessfully verified client as permission ${packet.securityLevel}")
    }
}
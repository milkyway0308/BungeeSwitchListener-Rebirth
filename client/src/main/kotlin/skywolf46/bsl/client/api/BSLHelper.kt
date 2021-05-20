package skywolf46.bsl.client.api

import skywolf46.bsl.client.BungeeSwitchListener
import skywolf46.bsl.client.handler.BSLVerifyHandler
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.AbstractPacketSyncRequest
import skywolf46.bsl.core.impl.packet.PacketReplied
import skywolf46.bsl.core.impl.packet.proxy.PacketRequireProxy
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize
import java.lang.IllegalStateException

object BSLHelper {

    @JvmStatic
    fun send(packet: AbstractPacketBase<*>) {
        BungeeSwitchListener.socket.send(packet)
    }

    @JvmStatic
    fun proxy(port: Int, packet: AbstractPacketBase<*>) {
        BungeeSwitchListener.socket.proxyTo(port, packet)
    }

    @JvmStatic
    fun proxy(server: String, packet: AbstractPacketBase<*>) {
        BungeeSwitchListener.socket.send(PacketReplied.of(server, packet))
    }

    @JvmStatic
    fun requestSync(packetClass: AbstractPacketSyncRequest<*>) {
        send(PacketRequestSynchronize(0, packetClass))
    }

    @JvmStatic
    fun waitUntilVerified(unit: Runnable) {
        waitUntilVerified {
            unit.run()
        }
    }

    @JvmStatic
    fun waitUntilVerified(unit: () -> Unit) {
        if (BSLVerifyHandler.isVerified) {
            unit()
            return
        }
        BSLCore.syncProvider?.sync {
            waitUntilVerified(unit)
        }
    }

}
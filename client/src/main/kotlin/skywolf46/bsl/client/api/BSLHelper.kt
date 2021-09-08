package skywolf46.bsl.client.api

import skywolf46.bsl.client.BungeeSwitchListener
import skywolf46.bsl.client.handler.BSLVerifyHandler
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.AbstractPacketSyncRequest
import skywolf46.bsl.core.impl.packet.PacketBroadcastPacket
import skywolf46.bsl.core.impl.packet.PacketReplied
import skywolf46.bsl.core.impl.packet.sync.PacketDataSynchronized
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize
import java.util.*

object BSLHelper {

    @JvmStatic
    fun send(packet: AbstractPacketBase<*>): BSLHelper {
        BungeeSwitchListener.socket.send(packet)
        return this
    }

    @JvmStatic
    fun broadcast(packet: AbstractPacketBase<*>): BSLHelper {
        send(PacketBroadcastPacket(packet))
        return this
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
        send(PacketRequestSynchronize(0, UUID.randomUUID(), packetClass))
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
        BSLCore.syncProvider?.delayedSync(delayMillisecond = 1L) {
            waitUntilVerified(unit)
        }
    }

}

private val requestPair = mutableMapOf<Class<AbstractPacketSyncRequest<*>>, Class<PacketDataSynchronized>>()

private val requestPairRev = mutableMapOf<Class<PacketDataSynchronized>, Class<AbstractPacketSyncRequest<*>>>()

fun Class<AbstractPacketSyncRequest<*>>.registerPair(cls: Class<PacketDataSynchronized>) {
    requestPair[this] = cls
    requestPairRev[cls] = this
}

fun Class<AbstractPacketSyncRequest<*>>.findResultPair(): Class<PacketDataSynchronized>? {
    return requestPair[this]
}

fun Class<PacketDataSynchronized>.findSyncPair(): Class<AbstractPacketSyncRequest<*>>? {
    return requestPairRev[this]
}

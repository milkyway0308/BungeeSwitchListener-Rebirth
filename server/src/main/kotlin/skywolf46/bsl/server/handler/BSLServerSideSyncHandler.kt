package skywolf46.bsl.server.handler

import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.BSLServerHost
import skywolf46.bsl.core.impl.packet.sync.PacketCannotSynchronize
import skywolf46.bsl.core.impl.packet.sync.PacketDataSynchronized
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize
import skywolf46.bsl.core.security.permissions.SecurityPermissions

@BSLSideOnly(BSLSide.PROXY)
object BSLServerSideSyncHandler {

    @BSLHandler(priority = Integer.MAX_VALUE)
    fun PacketRequestSynchronize.onPacketEvent() {

        // Reconstruct
        val packet = PacketRequestSynchronize(timestamp,
            header.server.getUniqueID(),
            this.range,
            this.packet)

    }

    @BSLHandler(priority = Integer.MAX_VALUE)
    fun PacketDataSynchronized.onPacketEvent() {
        // Cancel more synchronization
    }

    @BSLHandler
    fun PacketCannotSynchronize.onPacketEvent() {
        // To next server
        val list = BSLServerHost.host!!.getVerifiedServers().filter { x ->
            x.hasPermission(SecurityPermissions.ADMIN)
        }
        val index = list.indexOf(this.header.server) + 1
        if (index >= list.size) {

        }

    }
}
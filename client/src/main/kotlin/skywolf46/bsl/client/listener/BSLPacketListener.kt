package skywolf46.bsl.client.listener

import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize

object BSLPacketListener {
    @BSLHandler
    fun PacketRequestSynchronize.onPacket() {
        // Sync requested! Checking pair...
    }
}
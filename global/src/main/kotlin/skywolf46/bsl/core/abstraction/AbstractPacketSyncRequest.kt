package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.annotations.BSLExclude
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize

abstract class AbstractPacketSyncRequest<X : AbstractPacketBase<X>> : AbstractPacketBase<X>() {
    companion object {
        internal val EMPTY_SYNC = PacketRequestSynchronize()
    }

    @BSLExclude
    var response: PacketRequestSynchronize = EMPTY_SYNC

    fun isEmptyResponse() = response == EMPTY_SYNC
}
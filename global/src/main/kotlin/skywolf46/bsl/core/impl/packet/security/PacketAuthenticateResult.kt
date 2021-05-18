package skywolf46.bsl.core.impl.packet.security

import skywolf46.bsl.core.abstraction.AbstractPacketBase

class PacketAuthenticateResult : AbstractPacketBase<PacketAuthenticateResult>() {
    var success = false
    var securityLevel = "ADMIN"
}
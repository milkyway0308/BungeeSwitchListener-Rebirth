package skywolf46.bsl.core.impl.packet.security

import skywolf46.bsl.core.abstraction.AbstractPacketBase

class PacketAuthenticateResult(var success: Boolean, var securityLevel: String) :
    AbstractPacketBase<PacketAuthenticateResult>() {


    constructor() : this(false, "")
}
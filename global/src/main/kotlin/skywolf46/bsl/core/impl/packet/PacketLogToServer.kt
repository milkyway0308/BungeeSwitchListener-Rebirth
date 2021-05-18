package skywolf46.bsl.core.impl.packet

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.security.permissions.SecurityPermissions

class PacketLogToServer(var msg: String) : AbstractPacketBase<PacketLogToServer>() {
    constructor() : this("")

    override fun requirePermission(): SecurityPermissions {
        return SecurityPermissions.ADMIN
    }
}
package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.security.permissions.SecurityPermissions

interface IBSLPacket : IListenerAttachable {
    fun requirePermission(): SecurityPermissions
}
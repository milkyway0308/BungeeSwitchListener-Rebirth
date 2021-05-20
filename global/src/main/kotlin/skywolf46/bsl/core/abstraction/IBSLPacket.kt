package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.security.permissions.SecurityPermissions

interface IBSLPacket : IListenerAttachable {
    fun requirePermission(): SecurityPermissions

    fun callHandler() {
        BSLCore.syncProvider?.sync {
            BSLCore.handlerList(javaClass).forEach {
                it.data.invoke(this)
            }
        } ?: run {
            BSLCore.handlerList(javaClass).forEach {
                it.data.invoke(this)
            }
        }
    }
}
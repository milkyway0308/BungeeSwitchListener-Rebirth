package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import java.util.*

interface IBSLPacket : IListenerAttachable {

    fun requirePermission(): SecurityPermissions

    fun callHandler(requireSync: Boolean = true) {
        if (requireSync) {
            BSLCore.syncProvider?.sync {
                BSLCore.handlerList(javaClass).forEach {
                    it.data.invoke(this)
                }
            } ?: run {
                BSLCore.handlerList(javaClass).forEach {
                    it.data.invoke(this)
                }
            }
        } else {
            BSLCore.handlerList(javaClass).forEach {
                it.data.invoke(this)
            }
        }
    }

}
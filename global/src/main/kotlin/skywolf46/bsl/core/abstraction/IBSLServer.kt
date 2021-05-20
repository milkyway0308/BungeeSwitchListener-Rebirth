package skywolf46.bsl.core.abstraction

import skywolf46.bsl.core.security.permissions.SecurityPermissions
import java.net.SocketAddress

interface IBSLServer {
    fun send(packet: IBSLPacket, callBeforeWrite: Boolean) {
        send(packet, callBeforeWrite = callBeforeWrite)
    }

    fun send(vararg packet: IBSLPacket, callBeforeWrite: Boolean = true)

    fun getName(): String

    fun isLocalHost(): Boolean = false

    fun hasPermission(permission: SecurityPermissions): Boolean

    fun address(): SocketAddress
}
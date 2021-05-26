package skywolf46.bsl.core.impl

import io.netty.channel.Channel
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.impl.packet.security.PacketRequestAuthenticate
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.security.PrivateKey
import java.security.PublicKey

class BSLServerConnection(internal val chan: Channel) : IBSLServer {
    val keypair: Pair<PublicKey, PrivateKey>
    var currentPermission = mutableListOf<SecurityPermissions>()
        private set
    var serverName: String = "Unnamed Server"
    var port: Int = -1
        private set
    private val address: SocketAddress = chan.remoteAddress()

    init {
        send(PacketRequestAuthenticate.generate().apply {
            keypair = public to private!!
        })
    }

    override fun sendAll(vararg packet: IBSLPacket, callBeforeWrite: Boolean) {
        for (x in packet) {
            if (callBeforeWrite) {
                BSLCore.afterProcessor(x.javaClass).beforeWrite.forEach {
                    it.data.invoke(x)
                }
            }
            chan.writeAndFlush(x)
        }
    }

    override fun getName(): String {
        return serverName
    }

    override fun hasPermission(permission: SecurityPermissions): Boolean {
        return currentPermission.contains(permission) || currentPermission.any {
            return@any it.hasPermission(permission)
        }
    }

    override fun address(): SocketAddress {
        return address
    }

    override fun isLocalHost(): Boolean {
        if (BSLCore.isServer)
            return (chan.remoteAddress() as InetSocketAddress).address.isLoopbackAddress
        return false
    }
}
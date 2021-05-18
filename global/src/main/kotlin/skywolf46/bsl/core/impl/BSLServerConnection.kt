package skywolf46.bsl.core.impl

import io.netty.channel.Channel
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.impl.packet.security.PacketRequestAuthenticate
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import java.net.InetSocketAddress
import java.security.PrivateKey
import java.security.PublicKey

class BSLServerConnection(internal val chan: Channel) : IBSLServer {
    val keypair: Pair<PublicKey, PrivateKey>
    var currentPermission = mutableListOf<SecurityPermissions>()
    var serverName: String = "Unnamed Server"
    var port: Int = -1

    init {
        send(PacketRequestAuthenticate.generate().apply {
            keypair = public to private!!
        })
    }

    override fun send(vararg packet: IBSLPacket) {
        for (x in packet) {
            BSLCore.afterProcessor(x.javaClass).beforeWrite.forEach {
                it.data.invoke(x)
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

    override fun isLocalHost(): Boolean {
        if (BSLCore.isServer)
            return (chan.localAddress() as InetSocketAddress).address.isLoopbackAddress
        return false
    }
}
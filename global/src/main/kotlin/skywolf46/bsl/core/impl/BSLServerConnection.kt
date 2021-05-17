package skywolf46.bsl.core.impl

import io.netty.channel.Channel
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.impl.packets.server.security.PacketRequestAuthenticate
import java.security.PrivateKey
import java.security.PublicKey

class BSLServerConnection(private val chan: Channel) : IBSLServer {
    val keypair: Pair<PublicKey, PrivateKey>

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
        TODO("Not yet implemented")
    }
}
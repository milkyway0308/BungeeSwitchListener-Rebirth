package skywolf46.bsl.core.impl

import io.netty.channel.Channel
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.*
import skywolf46.bsl.core.util.asLookUp

class BSLServerHost(val chan: Channel) : IBSLProxyServer {
    override fun proxy(server: IBSLServer, vararg packet: IBSLPacket) {
        val buf = chan.alloc().directBuffer()
        for (x in packet) {
            var lu = BSLCore.classLookup.lookUpValue(x.asLookUp().toRange())
            if (lu == null) {
                // todo Auto append serializer
                lu = BSLCore.classLookup.lookUpValue(x.asLookUp().toRange())
            }
            lu!!.write(buf, x)
        }
    }

    override fun broadcast(vararg packet: IBSLPacket) {
        TODO("Not yet implemented")
    }

    override fun send(vararg packet: AbstractPacketBase) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }
}
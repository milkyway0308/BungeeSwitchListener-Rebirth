package skywolf46.bsl.core.impl

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLServer

class BSLServerConnection(address: String, port: Int) : IBSLServer{
    override fun send(vararg packet: AbstractPacketBase) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }
}
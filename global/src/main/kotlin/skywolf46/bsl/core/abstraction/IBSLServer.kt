package skywolf46.bsl.core.abstraction

interface IBSLServer {
    fun send(vararg packet: AbstractPacketBase)

    fun getName(): String
}
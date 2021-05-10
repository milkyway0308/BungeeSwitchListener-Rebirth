package skywolf46.bsl.core.abstraction

interface IBSLProxyServer : IBSLServer {

    fun proxy(server: IBSLServer, vararg packet: IBSLPacket)

    fun broadcast(vararg packet: IBSLPacket)


}
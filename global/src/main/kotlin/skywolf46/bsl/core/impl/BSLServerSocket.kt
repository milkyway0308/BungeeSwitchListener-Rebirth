package skywolf46.bsl.core.impl

import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.impl.packet.proxy.PacketRequireProxy
import skywolf46.bsl.core.netty.handler.ErrorPrintingHandler
import skywolf46.bsl.core.netty.handler.IncomingPacketHandler
import skywolf46.bsl.core.netty.handler.OutgoingPacketHandler
import skywolf46.bsl.core.security.permissions.SecurityPermissions

class BSLServerSocket(val ip: String = "localhost", val port: Int) : IBSLServer {
    private var chan: Channel? = null
    private var group: NioEventLoopGroup? = null
    private val success = mutableListOf<() -> Unit>()
    private val fail = mutableListOf<() -> Unit>()
    private val err = mutableListOf<() -> Unit>()

    fun onSuccess(unit: () -> Unit) {
        success += unit
    }

    fun onFailure(unit: () -> Unit) {
        fail += unit
    }

    fun onError(unit: () -> Unit) {
        err += unit
    }

    fun retry() {
        group = NioEventLoopGroup()
        val bootstrap = Bootstrap()
        bootstrap.group(group)
            .channel(NioSocketChannel::class.java)
            .handler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(
                        LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                        IncomingPacketHandler(this@BSLServerSocket),
                        LengthFieldPrepender(4),
                        OutgoingPacketHandler(),
                        ErrorPrintingHandler()
                    )
                }
            })
        val f: ChannelFuture = bootstrap.connect(ip, port).sync()
        this.chan = f.channel()
//            f.channel().closeFuture().sync()
    }

    fun stopServer() {
        group?.shutdownGracefully()
    }

    fun <X : AbstractPacketBase<X>> proxyTo(serverPort: Int, packet: X) {
        send(PacketRequireProxy.of(serverPort, packet))
    }

    override fun send(vararg packet: IBSLPacket) {
        for (x in packet){
            BSLCore.afterProcessor(x.javaClass).beforeWrite.forEach {
                it.data.invoke(x)
            }
            chan?.writeAndFlush(x)
        }
    }

    override fun getName(): String {
        return "BSL Client Connection"
    }

    override fun hasPermission(permission: SecurityPermissions): Boolean {
        // No use
        return false
    }
}
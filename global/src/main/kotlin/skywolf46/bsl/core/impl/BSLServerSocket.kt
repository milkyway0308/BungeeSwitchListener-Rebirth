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
import skywolf46.bsl.core.impl.packet.PacketRequireProxy
import skywolf46.bsl.core.netty.handler.ErrorPrintingHandler
import skywolf46.bsl.core.netty.handler.IncomingPacketHandler
import skywolf46.bsl.core.netty.handler.OutgoingPacketHandler
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import java.net.SocketAddress
import java.util.*

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
        try {
            group = NioEventLoopGroup()
            val bootstrap = Bootstrap()
            bootstrap.group(group)
                .channel(NioSocketChannel::class.java)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(
                            ErrorPrintingHandler(),
                            LengthFieldBasedFrameDecoder(Integer.MAX_VALUE - 600, 0, 4, 0, 4),
                            IncomingPacketHandler(this@BSLServerSocket),
                            LengthFieldPrepender(4),
                            OutgoingPacketHandler(),
                        )
                    }
                })
            val f: ChannelFuture = bootstrap.connect(ip, port).sync()
            this.chan = f.channel()
            for (x in success)
                x()
        } catch (e: Exception) {
            group?.shutdownGracefully()
            for (x in fail)
                x()
        }
//            f.channel().closeFuture().sync()
    }

    fun stopServer() {
        group?.shutdownGracefully()
    }

    fun proxyTo(serverPort: Int, packet: AbstractPacketBase<*>) {
        send(PacketRequireProxy(packet, serverPort))
    }

    override fun sendAll(vararg packet: IBSLPacket, callBeforeWrite: Boolean) {
        for (x in packet) {
            if (callBeforeWrite) {
                BSLCore.afterProcessor(x.javaClass).beforeWrite.forEach {
                    it.data.invoke(x)
                }
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


    override fun address(): SocketAddress {
        throw java.lang.IllegalStateException("Unsupported Operation")
    }

    override fun applyUniqueID(uuid: UUID) {
        TODO("Not yet implemented")
    }

    override fun getUniqueID(): UUID {
        TODO("Not yet implemented")
    }
}
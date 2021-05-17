package skywolf46.bsl.core.impl

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.abstraction.IBSLProxyServer
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.netty.handler.IncomingPacketHandler
import skywolf46.bsl.core.netty.handler.OutgoingPacketHandler

/**
 * BSL Host server
 */
class BSLServerHost(val port: Int) : IBSLProxyServer {
    private val servers = mutableMapOf<String, List<BSLServerConnection>>()
    private val serversPort = mutableMapOf<Int, BSLServerConnection>()

    init {
        val bossGroup: EventLoopGroup = NioEventLoopGroup()
        val workerGroup: EventLoopGroup = NioEventLoopGroup()
        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(
                            LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                            OutgoingPacketHandler(),
                            LengthFieldPrepender(4),
                            IncomingPacketHandler()
                        )
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
            val f: ChannelFuture = b.bind(port).sync()
//            f.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }

    override fun proxy(server: IBSLServer, vararg packet: IBSLPacket) {
        if (!servers.containsKey(server.getName())) {
            System.err.println("BSL-Host | §cPacket proxying failed - Server ${server.getName()} not registered")
            return
        }
    }

    override fun broadcast(vararg packet: IBSLPacket) {
        for (x in servers.values) {
            for (y in x)
                y.send(*packet)
        }
    }

    override fun send(vararg packet: IBSLPacket) {
        throw IllegalStateException("Host server not support sending to self")
    }

    override fun getName(): String {
        return "HostProxy"
    }

}
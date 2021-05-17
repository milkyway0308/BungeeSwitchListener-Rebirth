package skywolf46.bsl.core.impl

import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.netty.handler.IncomingPacketHandler
import skywolf46.bsl.core.netty.handler.OutgoingPacketHandler

class BSLServerSocket(val ip: String = "localhost", val port: Int) {
    private var chan: Channel? = null
    private var success = mutableListOf<() -> Unit>()
    private var fail = mutableListOf<() -> Unit>()
    private var err = mutableListOf<() -> Unit>()
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
        val workerGroup: EventLoopGroup = NioEventLoopGroup()
        try {
            val b = Bootstrap()
            b.group(workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .handler(object : ChannelInitializer<SocketChannel>() {
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
            val f: ChannelFuture = b.connect(ip, port).sync()
            this.chan = f.channel()
//            f.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
        }
    }

    fun <X : AbstractPacketBase<X>> proxyTo(serverPort: Int, packet: X) {

    }
}
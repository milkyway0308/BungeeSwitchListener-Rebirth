package skywolf46.bsl.core.impl

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.abstraction.IBSLProxyServer
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.netty.handler.ChannelActiveVerifyPacketHandler
import skywolf46.bsl.core.netty.handler.ErrorPrintingHandler
import skywolf46.bsl.core.netty.handler.IncomingPacketHandler
import skywolf46.bsl.core.netty.handler.OutgoingPacketHandler
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import java.net.SocketAddress
import java.util.*
import kotlin.collections.ArrayList

/**
 * BSL Host server
 */
class BSLServerHost(val port: Int) : IBSLProxyServer {
    companion object {
        var host: BSLServerHost? = null
            internal set
    }

    private val serverUUID = mutableMapOf<UUID, BSLServerConnection>()
    private val servers = mutableMapOf<String, BSLServerConnection>()
    private val serversPort = mutableMapOf<Int, BSLServerConnection>()
    private val serverReversed = mutableMapOf<Channel, BSLServerConnection>()
    private val nameDuplicated = mutableMapOf<String, Int>()
    private val bossGroup: EventLoopGroup
    private val workerGroup: EventLoopGroup
    private val uuid = UUID.randomUUID()

    init {
        println("BSL-Host | Server is starting on port $port")
        val time = System.currentTimeMillis()
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()

        val b = ServerBootstrap()
        b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(
                        ErrorPrintingHandler(),
                        ChannelActiveVerifyPacketHandler(),
                        LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                        IncomingPacketHandler(this@BSLServerHost),
                        LengthFieldPrepender(4),
                        OutgoingPacketHandler()
                    )
                }
            }).option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
        b.bind(port).sync()
        host = this
        println("BSL-Host | Hello, World! (Elapsed ${System.currentTimeMillis() - time}ms)")

    }

    fun stopServer() {
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
    }

    override fun proxy(server: IBSLServer, vararg packet: IBSLPacket) {
        if (!servers.containsKey(server.getName())) {
            System.err.println("BSL-Host | §cPacket proxying failed - Server ${server.getName()} not registered")
            return
        }
    }

    override fun broadcast(vararg packet: IBSLPacket) {
        for (x in packet) {
            x as AbstractPacketBase<*>
            BSLCore.afterProcessor(x.javaClass).beforeWrite.forEach {
                it.data.invoke(x)
            }
            for (server in servers.values) {
                if (server == x.header.server)
                    continue
                server.send(x, callBeforeWrite = false)
            }
        }


    }

    override fun sendAll(vararg packet: IBSLPacket, callBeforeWrite: Boolean) {
        throw IllegalStateException("Host server not support sending to self")
    }

    override fun getName(): String {
        return "HostProxy"
    }

    // Always admin
    override fun hasPermission(permission: SecurityPermissions): Boolean {
        return true
    }

    override fun address(): SocketAddress {
        throw java.lang.IllegalStateException("Unsupported Operation")
    }

    override fun applyUniqueID(uuid: UUID) {
        throw IllegalStateException("UUID applying not supported to mains erver")
    }

    override fun getUniqueID(): UUID {
        return uuid
    }

    fun fromPort(targetPort: Int): IBSLServer? {
        return serversPort[targetPort]
    }

    fun fromID(targetServer: String): BSLServerConnection? {
        return servers[targetServer]
    }

    fun addServer(name: String, server: BSLServerConnection) {
        servers[name] = server
    }


    fun addServer(port: Int, server: BSLServerConnection) {
        serversPort[port] = server
    }

    fun removeServer(channel: Channel) {
        fromChannel(channel)?.apply {
            removeServer(this)
        }
    }

    fun rename(name: String): String {
        var namer = name
        if (!servers.containsKey(namer))
            return namer
        var nameIndex = 0
        while (servers.containsKey(namer)) {
            namer = "$namer${nameIndex++}"
        }
        return namer
    }

    fun removeServer(server: IBSLServer) {
        serverReversed.remove((server as BSLServerConnection).chan)
        servers.remove(server.serverName)
        serversPort.remove(server.port)
        println("BSL-Host | Server ${server.serverName}(${
            if (server.port != -1) "localhost:${server.port}" else server.address()
        }) disconnected")
    }

    fun fromChannel(channel: Channel): IBSLServer? {
        return serverReversed[channel]
    }

    fun addServerTemporary(connection: BSLServerConnection) {
        serverReversed[connection.chan] = connection
        var uuid = UUID.randomUUID()
        while (uuid in this.serverUUID) {
            uuid = UUID.randomUUID()
        }
        connection.applyUniqueID(uuid)
    }

    fun getServers(): MutableList<IBSLServer> {
        return ArrayList(serverReversed.values)
    }

    fun getVerifiedServers(): MutableList<IBSLServer> {
        return ArrayList(serversPort.values)
    }
}
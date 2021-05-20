package skywolf46.bsl.server.handler

import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.chat.TextComponent
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.BSLServerConnection
import skywolf46.bsl.core.impl.BSLServerHost
import skywolf46.bsl.core.impl.packet.PacketLogToServer
import skywolf46.bsl.core.impl.packet.PacketReplied
import skywolf46.bsl.core.impl.packet.PacketBroadcastPacket
import skywolf46.bsl.core.impl.packet.proxy.PacketRequireProxy
import skywolf46.bsl.core.impl.packet.security.PacketAuthenticateResult
import skywolf46.bsl.core.impl.packet.security.PacketIntroduceSelf
import skywolf46.bsl.core.impl.packet.sync.PacketCannotSynchronize
import skywolf46.bsl.core.impl.packet.sync.PacketDataSynchronized
import skywolf46.bsl.core.impl.packet.sync.PacketRequestSynchronize
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import skywolf46.bsl.server.BungeeSwitchListener
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicLong

@BSLSideOnly(BSLSide.PROXY)
object BSLServerSideProxyHandler {
    private val syncTimestamp = AtomicLong()
    private val syncRequested = mutableMapOf<Long, Pair<IBSLServer, () -> Unit>>()

    @BSLHandler
    fun PacketReplied.onProxy() {
        BSLServerHost.host!!.fromID(server)?.send(this, false)
            ?: throw IllegalStateException("BSL packet write error; Cannot write proxy packet : Target server($server) not connected")
    }

    @BSLHandler
    fun PacketRequireProxy.onProxy() {
        val reply = PacketReplied.of(header.targetName, packet)
        reply.header.targetName = header.targetName
        val server = BSLServerHost.host!!.fromPort(targetPort)
        server?.send(reply)
    }

    @BSLHandler
    fun PacketBroadcastPacket.onPacket() {
        BSLServerHost.host!!.broadcast(this)
    }

    @BSLHandler
    fun PacketLogToServer.onPacket() {
        BungeeCord.getInstance().console.sendMessage(TextComponent("§eBSL-[${header.server.getName()}] | $msg"))
    }

    @BSLHandler
    fun PacketIntroduceSelf.onPacket() {
        val conn = header.server as BSLServerConnection
        val pass = getPassword(conn.keypair.second)
        val perm = BungeeSwitchListener.permissionMap[pass]
        var currentServerName = serverName
        if (perm != null) {
            currentServerName = BSLServerHost.host!!.rename(serverName)
            println("BSL-Host | Client ${header.server.address()} identified as server name $serverName(Port $port) / Permission level ${perm.first}")
            println("BSL-Host | Server name duplicated! Name of client ${header.server.address()} changed from $serverName to $currentServerName.")
            BSLServerHost.host!!.addServer(currentServerName, header.server as BSLServerConnection)
            BSLServerHost.host!!.addServer(port, header.server as BSLServerConnection)
            conn.currentPermission.clear()
            conn.currentPermission.addAll(perm.second)
            header.response(PacketAuthenticateResult(true, perm.first))
        } else {
            println("BSL-Host | Client ${header.server.address()} identified as server name $serverName, but permission rejected. Register as temporary(OpenAPI) client.")
            header.response(PacketAuthenticateResult(false, ""))
        }
    }

    @BSLHandler
    fun PacketRequestSynchronize.onServerSync() {
        val currentRequest = syncTimestamp.incrementAndGet()
        val servers = BSLServerHost.host!!.getVerifiedServers()
            .filter { x -> x != header.server && x.hasPermission(SecurityPermissions.ADMIN) }
            .toMutableList()
        val packetToSend = PacketRequestSynchronize(currentRequest, range, packet)
        val invoker: Pair<IBSLServer, () -> Unit> = header.server to {
            if (servers.isNotEmpty()) {
                servers.removeAt(0).send(packetToSend)
            } else {
                syncRequested.remove(currentRequest)
                header.server.send(PacketCannotSynchronize(0, range))
            }
        }
        syncRequested[currentRequest] = invoker
        invoker.second()
    }

    @BSLHandler
    fun PacketCannotSynchronize.onSyncFailed() {
        syncRequested[timestamp]?.second?.invoke()
            ?: throw IllegalStateException("Illegal synchronization: Timestamp ${timestamp} is not in queue")
    }

    @BSLHandler
    fun PacketDataSynchronized.onSyncSuccess() {
        syncRequested[timestamp]?.first?.send(this)
            ?: throw IllegalStateException("Illegal synchronization: Timestamp ${timestamp} is not in queue")
    }
}

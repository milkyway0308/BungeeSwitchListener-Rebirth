package skywolf46.bsl.server.handler

import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.chat.TextComponent
import skywolf46.bsl.core.annotations.BSLHandler
import skywolf46.bsl.core.annotations.BSLSideOnly
import skywolf46.bsl.core.enums.BSLSide
import skywolf46.bsl.core.impl.BSLServerConnection
import skywolf46.bsl.core.impl.BSLServerHost
import skywolf46.bsl.core.impl.packet.PacketLogToServer
import skywolf46.bsl.core.impl.packet.PacketReplied
import skywolf46.bsl.core.impl.packet.minecraft.packet.PacketBroadcastAll
import skywolf46.bsl.core.impl.packet.proxy.PacketRequireProxy
import skywolf46.bsl.core.impl.packet.security.PacketAuthenticateResult
import skywolf46.bsl.core.impl.packet.security.PacketIntroduceSelf
import skywolf46.bsl.server.BungeeSwitchListener

@BSLSideOnly(BSLSide.PROXY)
object BSLServerSideProxyHandler {
    @BSLHandler
    fun PacketReplied.onResponse() {
        unwrap()
    }

    @BSLHandler
    fun PacketRequireProxy.onProxy() {
        val reply = PacketReplied.of(header.targetName, packets)
        reply.header.targetName = header.targetName
        val server = BSLServerHost.host!!.fromPort(targetPort)
        server?.send(reply)
    }

    @BSLHandler
    fun PacketBroadcastAll.onPacket() {
        BSLServerHost.host!!.broadcast(this)
    }

    @BSLHandler
    fun PacketLogToServer.onPacket() {
        BungeeCord.getInstance().console.sendMessage(TextComponent("§eBSL - [${header.server.getName()}] | $msg"))
    }

    @BSLHandler
    fun PacketIntroduceSelf.onPacket() {
        val conn = header.server as BSLServerConnection
        val pass = getPassword(conn.keypair.second)
        val perm = BungeeSwitchListener.permissionMap[pass]
        if (perm != null) {
            conn.currentPermission.clear()
            conn.currentPermission.addAll(perm.second)
            header.response(PacketAuthenticateResult(true, perm.first))
        } else {
            header.response(PacketAuthenticateResult(false, ""))
        }
    }
}

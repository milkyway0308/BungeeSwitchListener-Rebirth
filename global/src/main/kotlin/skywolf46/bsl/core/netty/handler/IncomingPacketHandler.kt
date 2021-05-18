package skywolf46.bsl.core.netty.handler

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.abstraction.IBSLServer
import skywolf46.bsl.core.enums.ListenerType
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.core.impl.BSLServerHost
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import skywolf46.bsl.core.util.ByteBufUtil

class IncomingPacketHandler(val server: IBSLServer) : ChannelInboundHandlerAdapter() {
    val packetLimitation = 120000

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        msg as ByteBuf
        if (msg.readableBytes() < 8) {
            System.err.println("BSL-Core | Cannot deserialize packet : Packet corrupted (Header < 8 byte)")
            return
        }

        println("Incoming ${msg.readableBytes()} bytes")

        val mark = msg.readerIndex()
        println(ByteBufUtil.readAllBytes(msg).contentToString())
        msg.readerIndex(mark)
        val range = msg.readInt()..msg.readInt()
        // Deserialization
        val lup = BSLCore.classLookup.lookUpValue(range)
        if (lup == null) {
            System.err.println("BSL-Core | Cannot deserialize packet : Structure for [${range.first}~${range.last}] not registered in server")
            msg.release()
            return
        }
        if (packetLimitation <= msg.readableBytes()) {
            System.err.println("BSL-Core | Cannot deserialize packet ${BSLCore.classLookup.lookUp(range)?.name} : Packet flooding (Overflowed ${msg.readableBytes() - packetLimitation} bytes)")
            msg.release()
            return
        }
        val pac = lup.read(msg, DataMode.HEADER)
        if (pac !is IBSLPacket) {
            System.err.println("BSL-Core | Deserialization core detected non-packet field; Packet will be ignored.")
            return
        }
        BSLCore.afterProcessor(pac.javaClass).afterRead.forEach {
            it.data.invoke(pac)
        }
        if (BSLCore.isServer) {
            // Filter permission
            val serverHost = (server as BSLServerHost)
            val server = serverHost.fromChannel(ctx.channel())
            if (pac.requirePermission() != SecurityPermissions.OPEN_API && (server == null || server.hasPermission(pac.requirePermission()))) {
                System.err.println("BSL-Core | Packet ${pac.javaClass.simpleName} request from ${
                    server?.getName() ?: ctx.channel().localAddress().toString()
                } denied : Permission denied")
                msg.release()
                return
            }
            if (pac is AbstractPacketBase<*>) {
                pac.header.server = server!!
                pac.header.targetName = pac.header.server.getName()
            }
        } else {
            // Change header to proxy server
            if (pac is AbstractPacketBase<*>) {
                pac.header.server = server
            }
        }


        pac.listen(ListenerType.RECEIVE)
        val handlers = BSLCore.handlerList(pac.javaClass)
        println("Handlers: ${handlers}")
        if (handlers.isNotEmpty()) {
            lup.read(pac, msg, DataMode.NON_HEADER)
            for (x in handlers) {
                x.data.invoke(pac)
            }
        }
        // Release buffer for next packet
        msg.release()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        if (BSLCore.isServer) {
            BSLServerHost.host!!.removeServer(ctx.channel())
        }
    }

}
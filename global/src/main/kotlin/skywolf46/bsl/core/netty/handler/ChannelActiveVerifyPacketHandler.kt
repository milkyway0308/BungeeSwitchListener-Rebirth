package skywolf46.bsl.core.netty.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import skywolf46.bsl.core.impl.BSLServerConnection
import skywolf46.bsl.core.impl.BSLServerHost

class ChannelActiveVerifyPacketHandler : ChannelInboundHandlerAdapter() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.pipeline().remove(this)
        println("BSLCore | New connection request from ${ctx.channel().localAddress()}. Requesting authorize..")
        BSLServerHost.host?.addServerTemporary(BSLServerConnection(ctx.channel()))
    }
}
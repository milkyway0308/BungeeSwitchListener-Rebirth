package skywolf46.bsl.core.netty.handler

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext

class ErrorPrintingHandler : ChannelDuplexHandler(){
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable) {
        cause.printStackTrace()
    }
}
package skywolf46.bsl.core.netty.handler

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import skywolf46.bsl.core.BSLCore
import java.io.IOException

class ErrorPrintingHandler : ChannelDuplexHandler() {
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable) {
        if (cause is IOException) {
            // Suppress exception
        }
    }
}
package skywolf46.bsl.core.util

import io.netty.buffer.ByteBuf

object ByteBufUtil {
    fun readAllBytes(buf: ByteBuf): ByteArray {
        val b = ByteArray(buf.readableBytes())
        buf.readBytes(b)
        return b
    }
}
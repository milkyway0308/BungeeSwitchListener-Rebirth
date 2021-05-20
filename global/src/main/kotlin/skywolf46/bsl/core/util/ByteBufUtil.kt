package skywolf46.bsl.core.util

import io.netty.buffer.ByteBuf


fun ByteBuf.readAllBytes(): ByteArray {
    val b = ByteArray(readableBytes())
    readBytes(b)
    return b
}

fun ByteBuf.writeByteArray(arr: ByteArray) {
    writeInt(arr.size)
    writeBytes(arr)
}

fun ByteBuf.readByteArray(): ByteArray = ByteArray(readInt()).apply { readBytes(this) }

fun ByteBuf.writeString(str: String) {
    writeByteArray(str.toByteArray())
}

fun ByteBuf.readString() = String(readByteArray())

fun ByteBuf.writeIntRange(range: IntRange) {
    writeInt(range.first).writeInt(range.last)
}

fun ByteBuf.readIntRange() = readInt()..readInt()


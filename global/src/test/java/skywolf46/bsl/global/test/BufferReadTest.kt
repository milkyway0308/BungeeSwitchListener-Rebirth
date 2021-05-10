package skywolf46.bsl.global.test

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.MethodSource
import org.junit.runners.MethodSorters
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.global.test.impl.packet.TestPacket
import kotlin.reflect.KClass


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BufferReadTest {
    val sharedBuffer = buffer()

    private fun buffer(): ByteBuf {
        return Unpooled.buffer()
    }

    @Before
    fun init() {
        println("Initializing test")
        BSLCore.init()
    }

    @Test
    fun test1OnWriteObjectToBuffer() {
        val packetToWrite = TestPacket()
        val writer: IByteBufSerializer<TestPacket> = BSLCore.resolve(packetToWrite.javaClass)
        writer.write(sharedBuffer, packetToWrite)
        println(">> Packet write complete : Buffer has ${sharedBuffer.readableBytes()}")
    }

    @Test
    fun test2ReadObjectFromBuffer() {
        test1OnWriteObjectToBuffer()
        val reader = BSLCore.resolve(TestPacket::class.java)
        reader.read(sharedBuffer, false).validateEquals()
    }

    @Test
    fun write3WriteHeaderOnly() {

    }

    @Test
    fun test4ReadHeadersOnly() {

    }
}
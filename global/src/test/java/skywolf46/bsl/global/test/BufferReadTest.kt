package skywolf46.bsl.global.test

import io.netty.buffer.Unpooled
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.enums.DataMode
import skywolf46.bsl.global.test.impl.packet.TestPacket


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BufferReadTest {
    val sharedBuffer = Unpooled.buffer()

    @Before
    fun init() {
        println("Initializing test")
        BSLCore.init()
    }

    @Test
    fun test1OnWriteObjectToBuffer() {
        val packetToWrite = TestPacket()
        packetToWrite.x3VariableInner.text = "Test!"
        val writer: IByteBufSerializer<TestPacket> = BSLCore.resolve(packetToWrite.javaClass)
        writer.writeHeaderData(sharedBuffer, packetToWrite)
        println(">> Packet write complete : Buffer has ${sharedBuffer.readableBytes()}")
    }

    @Test
    fun test2ReadObjectFromBuffer() {
        test1OnWriteObjectToBuffer()
        val reader = BSLCore.resolve(TestPacket::class.java)
        reader.readHeaderData(sharedBuffer).validateEquals()
    }

    @Test
    fun test3WriteHeaderOnly() {
        val packetToWrite = TestPacket()
        packetToWrite.changeHeaders()
        val writer: IByteBufSerializer<TestPacket> = BSLCore.resolve(packetToWrite.javaClass)
        writer.writeHeaderData(sharedBuffer, packetToWrite)
        println(">> Packet write complete : Buffer has ${sharedBuffer.readableBytes()}")

    }

    @Test
    fun test4ReadHeadersOnly() {
        test3WriteHeaderOnly()
        val reader = BSLCore.resolve(TestPacket::class.java)
        reader.readHeaderData(sharedBuffer).validateHeaderEquals()
    }
}
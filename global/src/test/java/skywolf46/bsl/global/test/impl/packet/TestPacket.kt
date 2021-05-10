package skywolf46.bsl.global.test.impl.packet

import org.junit.jupiter.api.Assertions.assertEquals
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.annotations.annotations.BSLHeader
import skywolf46.bsl.global.test.impl.data.TestInnerData

class TestPacket : AbstractPacketBase() {
    @BSLHeader
    var x1Variable = 20
    var x2VariableString = "Test"
    var x3VariableInner = TestInnerData()
    var x4VariableFloat = 0.4f

    @BSLHeader
    var x5VariableDouble = 20.0

    init {
        x3VariableInner.text = "Test!"
    }

    fun validateEquals() {
        val original = TestPacket()
        assertEquals(original.x1Variable, x1Variable)
        assertEquals(original.x2VariableString, x2VariableString)
        assertEquals("Test!", x3VariableInner.text)
        assertEquals(original.x4VariableFloat, x4VariableFloat)
        assertEquals(original.x5VariableDouble, x5VariableDouble)
    }


    fun validateHeaderEquals() {
        val original = TestPacket()
        assertEquals(original.x1Variable, x1Variable)
        assertEquals(original.x2VariableString, x2VariableString)
        assertEquals(original.x4VariableFloat, x4VariableFloat)
        assertEquals(original.x5VariableDouble, x5VariableDouble)
    }


}
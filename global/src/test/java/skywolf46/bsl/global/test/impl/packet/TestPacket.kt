package skywolf46.bsl.global.test.impl.packet

import org.junit.jupiter.api.Assertions.assertEquals
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.global.test.impl.data.TestInnerData

class TestPacket : AbstractPacketBase<TestPacket>() {
    @BSLHeader
    var x1Variable = 20
    var x2VariableString = "Test"
    var x3VariableInner = TestInnerData()
    var x4VariableFloat = 0.4f

    @BSLHeader
    var x5VariableDouble = 20.0


    fun validateEquals() {
        val original = TestPacket()
        assertEquals(original.x1Variable, x1Variable)
        assertEquals(original.x2VariableString, x2VariableString)
        assertEquals("Test!", x3VariableInner.text)
        assertEquals(original.x4VariableFloat, x4VariableFloat)
        assertEquals(original.x5VariableDouble, x5VariableDouble)
    }

    fun changeHeaders() {
        x1Variable = 40
        x5VariableDouble = 60.0
        x4VariableFloat = 0.41f
        x2VariableString = "Not Deserialize target"
    }


    fun validateHeaderEquals() {
        val original = TestPacket()
        assertEquals(40, x1Variable)
        assertEquals(original.x2VariableString, x2VariableString)
        assertEquals(original.x3VariableInner.text, TestInnerData().text)
        assertEquals(original.x4VariableFloat, x4VariableFloat)
        assertEquals(60.0, x5VariableDouble)
    }


}
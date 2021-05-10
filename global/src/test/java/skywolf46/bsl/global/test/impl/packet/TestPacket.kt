package skywolf46.bsl.global.test.impl.packet

import org.junit.jupiter.api.Assertions.assertEquals
import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.IBSLPacket
import skywolf46.bsl.core.annotations.annotations.BSLHeader

class TestPacket : AbstractPacketBase() {
    @BSLHeader
    var x1Variable = 20
    var x2VariableString = "Test"
    var x3VariableFloat = 0.4f

    @BSLHeader
    var x4VariableDouble = 20.0

    fun validateEquals() {
        val original = TestPacket()
        assertEquals(original.x1Variable, x1Variable)
        assertEquals(original.x2VariableString, x2VariableString)
        assertEquals(original.x3VariableFloat, x3VariableFloat)
        assertEquals(original.x4VariableDouble, x4VariableDouble)
    }


    fun validateHeaderEquals() {
        val original = TestPacket()
        assertEquals(original.x1Variable, x1Variable)
        assertEquals(original.x2VariableString, x2VariableString)
        assertEquals(original.x3VariableFloat, x3VariableFloat)
        assertEquals(original.x4VariableDouble, x4VariableDouble)
    }

    fun changeHeaders() {
        x1Variable = 40
        x4VariableDouble = 126.0
    }

}
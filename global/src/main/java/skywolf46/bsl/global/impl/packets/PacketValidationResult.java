package skywolf46.bsl.global.impl.packets;

import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class PacketValidationResult extends AbstractPacket {
    private boolean isValidated;

    public PacketValidationResult(boolean isValidated) {
        this.isValidated = isValidated;
    }

    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    public boolean isValidated() {
        return isValidated;
    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_VALIDATION_RESULT;
    }

}

package skywolf46.bsl.global.impl.packets;

import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class PacketValidation extends AbstractPacket {
    private String id;
    private int port;

    public PacketValidation(String id, int port){
        this.id = id;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public int getID() {
        return BungeeVariables.PACKET_VALIDATION;
    }
}

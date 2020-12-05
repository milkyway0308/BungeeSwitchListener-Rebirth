package skywolf46.bsl.global.abstraction.packets.handler;

import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public abstract class PacketHandler<T extends AbstractPacket> {
    public abstract void read(AbstractPacket packet);

}

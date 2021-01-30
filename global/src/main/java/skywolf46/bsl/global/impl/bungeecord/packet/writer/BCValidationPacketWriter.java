package skywolf46.bsl.global.impl.bungeecord.packet.writer;

import io.netty.buffer.ByteBuf;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.impl.packets.PacketValidationResult;

public class BCValidationPacketWriter implements AbstractPacket.PacketWriter<PacketValidationResult> {
    @Override
    public void write(PacketValidationResult packet, ByteBuf buffer) {
        buffer.writeBoolean(packet.isValidated());
    }
}

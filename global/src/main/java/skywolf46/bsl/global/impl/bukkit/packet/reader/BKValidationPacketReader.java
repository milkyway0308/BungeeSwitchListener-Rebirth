package skywolf46.bsl.global.impl.bukkit.packet.reader;

import io.netty.buffer.ByteBuf;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.impl.packets.PacketValidationResult;

public class BKValidationPacketReader implements AbstractPacket.PacketReader<PacketValidationResult> {
    @Override
    public PacketValidationResult read(ByteBuf buf) {
        return new PacketValidationResult(buf.readBoolean());
    }
}

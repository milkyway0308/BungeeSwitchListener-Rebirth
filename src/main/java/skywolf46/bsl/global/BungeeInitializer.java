package skywolf46.bsl.global;

import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.impl.bungeecord.packet.writer.BCValidationPacketWriter;

class BungeeInitializer {
    public static void init() {
        BSLCoreAPI.getPacket(BungeeVariables.PACKET_VALIDATION).register(new BCValidationPacketWriter());
    }
}

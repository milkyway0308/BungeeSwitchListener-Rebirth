package skywolf46.bsl.global;

import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.impl.bukkit.packet.reader.BKValidationPacketReader;

class BukkitInitializer {
    public static void init() {
        BSLCoreAPI.getPacket(BungeeVariables.PACKET_VALIDATION).register(new BKValidationPacketReader());
    }
}

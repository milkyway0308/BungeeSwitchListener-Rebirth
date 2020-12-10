package skywolf46.bsl.client.api;

import org.bukkit.Bukkit;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class BSLClientAPI {

    public static int getPort() {
        return Bukkit.getPort();
    }
}

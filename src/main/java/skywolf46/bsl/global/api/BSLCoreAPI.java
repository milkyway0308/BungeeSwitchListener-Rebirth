package skywolf46.bsl.global.api;

import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class BSLCoreAPI {
    public static AbstractConsoleWriter writer() {
        return BungeeSwitchListenerCore.getWriter();
    }

    public static AbstractPacket getPacket(int id) {
        return BungeeSwitchListenerCore.getPacket(id);
    }

    public static Side getSide() {
        return BungeeSwitchListenerCore.getSide();
    }
}

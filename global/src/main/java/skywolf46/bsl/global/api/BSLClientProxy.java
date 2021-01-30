package skywolf46.bsl.global.api;

import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;

public class BSLClientProxy {
    private int port;


    public BSLClientProxy(int port) {
        this.port = port;
    }

    public void send(AbstractPacket packet) {
        if (BSLCoreAPI.getSide() == Side.SERVER)
            BSLCoreAPI.getServer(port).send(packet);
        else
            BSLCoreAPI.bungee().relay(port, packet);
    }
}

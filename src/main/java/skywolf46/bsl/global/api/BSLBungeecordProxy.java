package skywolf46.bsl.global.api;

import skywolf46.bsl.client.api.BSLClientAPI;
import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.impl.packets.PacketBroadcast;
import skywolf46.bsl.global.impl.packets.PacketRelay;
import skywolf46.bsl.global.util.BSLChannel;

public class BSLBungeecordProxy {
    public static BSLChannel channel() {
        return BungeeSwitchListenerCore.getChannel(-1);
    }

    public void broadcast(int fromPort, AbstractPacket packet) {
        if (BSLCoreAPI.getSide() == Side.SERVER) {
            packet.markBuffer();
            for (int i : BSLCoreAPI.getServers()) {
                if (i == fromPort)
                    continue;
                packet.retainBuffer();
                packet.resetBuffer();
                BSLCoreAPI.getServer(i).send(packet);
            }
            packet.releaseBuffer();
        } else {
//            System.out.println("Writing " + packet.getClass() + " / " + packet.getID());
            channel().send(new PacketBroadcast(BSLClientAPI.getPort(), packet));
        }
    }


    public void send(AbstractPacket packet) {
        channel().send(packet);
    }


    public void relay(int port, AbstractPacket packet) {
        if (BSLCoreAPI.getSide() == Side.SERVER) {
            channel().send(new PacketRelay(port, packet));
        } else {
            BSLCoreAPI.getServer(port).send(packet);
        }
    }
}

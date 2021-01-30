package skywolf46.bsl.global.api;

import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.util.BSLChannel;

import java.util.HashMap;
import java.util.List;

public class BSLCoreAPI {
    private static BSLBungeecordProxy bungee;
    private static HashMap<Integer, BSLClientProxy> proxies = new HashMap<>();

    public static AbstractConsoleWriter writer() {
        return BungeeSwitchListenerCore.getWriter();
    }

    public static AbstractConsoleWriter writer(String prefix) {
        return BungeeSwitchListenerCore.getWriter().of(prefix);
    }

    public static AbstractPacket getPacket(int id) {
        return BungeeSwitchListenerCore.getPacket(id);
    }

    public static void registerPacket(int id, AbstractPacket packet) {
        BungeeSwitchListenerCore.registerPacket(id, packet);
    }

    public static Side getSide() {
        return BungeeSwitchListenerCore.getSide();
    }

    public static BSLBungeecordProxy bungee() {
        return (bungee == null) ? new BSLBungeecordProxy() : bungee;
    }

    public static BSLClientProxy server(int port) {
        return proxies.computeIfAbsent(port, a -> new BSLClientProxy(port));
    }

    public static BSLChannel getServer(int port) {
        return BungeeSwitchListenerCore.getChannel(port);
    }


    public static List<Integer> getServers() {
        return BungeeSwitchListenerCore.getChannels();
    }

    public static int getPort() {
        return BungeeSwitchListenerCore.getPort();
    }
}

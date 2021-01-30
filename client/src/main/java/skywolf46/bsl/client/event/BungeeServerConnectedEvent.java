package skywolf46.bsl.client.event;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import skywolf46.bsl.global.api.BSLBungeecordProxy;
import skywolf46.bsl.global.api.BSLCoreAPI;

public class BungeeServerConnectedEvent extends ServerEvent {
    private static HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public BSLBungeecordProxy bungee() {
        return BSLCoreAPI.bungee();
    }
}

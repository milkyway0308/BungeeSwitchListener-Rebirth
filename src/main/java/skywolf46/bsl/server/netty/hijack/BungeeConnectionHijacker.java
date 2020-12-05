package skywolf46.bsl.server.netty.hijack;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.event.ClientConnectEvent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.*;
import org.fusesource.jansi.Ansi;
import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.util.FinalReleaser;
import skywolf46.bsl.server.BungeeSwitchListener;
import skywolf46.bsl.server.netty.handler.AuthenticateHandler;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class BungeeConnectionHijacker extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel ch) throws Exception {
        final SocketAddress remoteAddress = (ch.remoteAddress() == null) ? ch.parent().localAddress() : ch.remoteAddress();
        if (BungeeCord.getInstance().getConnectionThrottle() != null && BungeeCord.getInstance().getConnectionThrottle().throttle(remoteAddress)) {
            ch.close();
            return;
        }
        final ListenerInfo listener = ch.attr(PipelineUtils.LISTENER).get();
        if (BungeeCord.getInstance().getPluginManager().callEvent(new ClientConnectEvent(remoteAddress, listener)).isCancelled()) {
            ch.close();
            return;
        }

        PipelineUtils.BASE.initChannel(ch);

        ch.pipeline().addBefore("frame-decoder", "legacy-decoder", new LegacyDecoder());
        ch.pipeline().addAfter("frame-decoder", "packet-decoder", new MinecraftDecoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion()));
        ch.pipeline().addAfter("frame-prepender", "packet-encoder", new MinecraftEncoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion()));
        ch.pipeline().addBefore("frame-prepender", "legacy-kick", new KickStringWriter());

        ch.pipeline().get(HandlerBoss.class).setHandler(new InitialHandler(BungeeCord.getInstance(), listener));
        if (listener.isProxyProtocol()) {
            ch.pipeline().addFirst(new HAProxyMessageDecoder());
        }
        if (!BungeeSwitchListener.getConfig().isOnlyLocalHost() || ((InetSocketAddress) ch.remoteAddress()).getAddress().isLoopbackAddress()) {
            ch.pipeline().addFirst("fake-connection-detector", new AuthenticateHandler());
        }
    }

    public static void hijack() {
        try {
            Field fl = PipelineUtils.class.getField("SERVER_CHILD");
            FinalReleaser.release(fl);
            BSLCoreAPI.writer().printText("Hijacking listener...");
            fl.set(null, new BungeeConnectionHijacker());
            BSLCoreAPI.writer().printText("Listener is under control");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

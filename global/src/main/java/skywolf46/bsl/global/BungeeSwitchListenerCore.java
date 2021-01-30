package skywolf46.bsl.global;

import io.netty.channel.Channel;
import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;
import skywolf46.bsl.global.abstraction.AbstractPortProvider;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.impl.bukkit.BukkitTextWriter;
import skywolf46.bsl.global.impl.bungeecord.BungeeTextSender;
import skywolf46.bsl.global.impl.packets.*;
import skywolf46.bsl.global.thread.ChannelKeepAliveThread;
import skywolf46.bsl.global.util.BSLChannel;
import skywolf46.bsl.global.util.ByteBufUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BungeeSwitchListenerCore {
    private static Side side;
    private static final Object LOCK = new Object();
    private static AbstractConsoleWriter writer = null;
    private static HashMap<Integer, AbstractPacket> packets = new HashMap<>();
    private static HashMap<Integer, BSLChannel> channels = new HashMap<>();
    private static HashMap<Channel, BSLChannel> channelsWrap = new HashMap<>();
    private static HashMap<Channel, ChannelKeepAliveThread> channelAlive = new HashMap<>();
    private static String id;
    private static AbstractPortProvider portProvider = null;


    public static void init(Side where) {
        synchronized (LOCK) {
            try {
                throw new Exception();
            } catch (Exception ex) {
                if (!ex.getStackTrace()[1].getClassName().startsWith("skywolf46.bsl.")) {
                    throw new IllegalStateException("BungeeSwitchListener initialization called from illegal location");
                }
            }
            if (side != null) {
                throw new IllegalStateException("BungeeSwitchListenerCode side already initialized");
            }
            if (where == Side.SERVER) {
                initializeServer();
            } else if (where == Side.CLIENT) {
                initializeClient();
            } else {
                throw new IllegalStateException("Cannot initialize BukkitSwitchListenerCore with Global side");
            }
        }
    }

    public static int getPort() {
        return portProvider == null ? -1 : portProvider.getPort();
    }

    public static void setPortProvider(AbstractPortProvider portProvider) {
        BungeeSwitchListenerCore.portProvider = portProvider;
    }

    public static void setIdentify(String id) {
        BungeeSwitchListenerCore.id = id;
    }

    public static Side getSide() {
        return side;
    }


    public static AbstractConsoleWriter getWriter() {
        return writer;
    }

    public static void registerPacket(int id, AbstractPacket packet) {
        packets.put(id, packet);
    }

    public static AbstractPacket getPacket(int id) {
        return packets.get(id);
    }

    private static void initialize() {
        registerPacket(BungeeVariables.PACKET_KEEP_ALIVE, new PacketServerAlive());
        registerPacket(BungeeVariables.PACKET_VALIDATION, new PacketValidation(null, 0));
        registerPacket(BungeeVariables.PACKET_VALIDATION_RESULT, new PacketValidationResult(false));
        registerPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD, new PacketPayload(false));
        registerPacket(BungeeVariables.PACKET_RECEIVE_PAYLOAD, new PacketReceivePayload(0, false));
        registerPacket(BungeeVariables.PACKET_TRANSFER_PAYLOAD, new PacketTransferPayload(0, false));
        registerPacket(BungeeVariables.PACKET_RECONSTRUCT, new PacketReconstruct());
        registerPacket(BungeeVariables.PACKET_RELAY, new PacketRelay());
        registerPacket(BungeeVariables.PACKET_REPLY_WRAPPER, new PacketReplyable());
        registerPacket(BungeeVariables.PACKET_BROADCAST_PACKET, new PacketBroadcast());

        getPacket(BungeeVariables.PACKET_RECONSTRUCT).register(PacketReconstruct::new);
        getPacket(BungeeVariables.PACKET_RECONSTRUCT).register((packet, buf) -> {
            buf.writeBytes(((PacketReconstruct) packet).getBuffer());
        });

        getPacket(BungeeVariables.PACKET_BROADCAST_PACKET).register((AbstractPacket.PacketReader) PacketBroadcast::new);
        getPacket(BungeeVariables.PACKET_BROADCAST_PACKET).register((packet, buf) -> {
            buf.writeBytes(((PacketBroadcast) packet).getBuffer());
        });

//        getPacket(BungeeVariables.PACKET_KEEP_ALIVE).attachListener((chan, packet) -> {
//            System.out.println("Keep alive");
//        });

    }


    private static void initializeServer() {
        initialize();
        side = Side.SERVER;
        writer = new BungeeTextSender("BungeeSwitchListener");
        BSLCoreAPI.writer().printText("Writer initialized");
        // Phase 1 - Global initialization
        BSLCoreAPI.writer().printText("Initializing phase 1 - Global initialization");
        BungeeInitializer.init();
        // Phase 2 - Reader Initialization
        BSLCoreAPI.writer().printText("Initializing phase 2 - Registering readers");
        getPacket(BungeeVariables.PACKET_VALIDATION).register(buf -> new PacketValidation(ByteBufUtility.readString(buf), buf.readInt()));
        getPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD).register(buf -> {
            buf.retain();
            return new PacketPayload(buf);
        });
        getPacket(BungeeVariables.PACKET_RELAY).register((AbstractPacket.PacketReader) PacketRelay::new);
        // Phase 3 - Writer Initialization
        BSLCoreAPI.writer().printText("Initializing phase 3 - Registering writers");
        getPacket(BungeeVariables.PACKET_VALIDATION_RESULT).register((packet, buffer) -> {
            PacketValidationResult pac = (PacketValidationResult) packet;
            buffer.writeBoolean(pac.isValidated());
        });

        getPacket(BungeeVariables.PACKET_RECEIVE_PAYLOAD).register((packet, buffer) -> {
            PacketReceivePayload recv = (PacketReceivePayload) packet;
            buffer.writeInt(recv.getServerPort());
            buffer.writeBytes(recv.getBuffer());
//            recv.getBuffer().release();
        });

        getPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD).register((packet, buffer) -> {
            PacketPayload payload = (PacketPayload) packet;
            buffer.writeBytes(payload.getBuffer());
//            payload.getBuffer().release();
        });


        // Phase 4 = Listener Initialization
        BSLCoreAPI.writer().printText("Initializing phase 4 - Registering listeners");
        getPacket(BungeeVariables.PACKET_VALIDATION).attachListener((c, pac) -> {
            PacketValidation val = (PacketValidation) pac;
            try {
                BSLChannel chan = new BSLChannel(c, val.getPort());
                if (val.getId().equals(id)) {
                    BSLCoreAPI.writer().printText("Server connected from port " + val.getPort());
//                    channels.put(val.getPort(), chan);
                    registerChannel(val.getPort(), chan);
                    channelsWrap.put(c, chan);
                    chan.send(new PacketValidationResult(true));
                } else {
                    BSLCoreAPI.writer().printError("Server connection rejected from " + c.remoteAddress() + " : ID not match");
                    chan.sendSync(new PacketValidationResult(false));
                    c.disconnect();
                }
            } catch (Exception ex) {
                BSLCoreAPI.writer().printError("Server connection rejected from " + c.remoteAddress() + " : ID not match");
            }
        });

        getPacket(BungeeVariables.PACKET_RELAY).attachListener((chan, buf) -> {
            PacketRelay rel = (PacketRelay) buf;
            PacketReconstruct con = new PacketReconstruct(rel.getBuffer());
            getChannel(rel.getPort()).send(con);
        });

        getPacket(BungeeVariables.PACKET_BROADCAST_PACKET).attachListener((chan, buf) -> {
            PacketBroadcast br = (PacketBroadcast) buf;
//            br.getBuffer().markReaderIndex();
//            br.getBuffer().readInt();
//            System.out.println("Rebroadcasting Packet " + br.getBuffer().readInt());
//            br.getBuffer().resetReaderIndex();
            br.retainBuffer();
            BSLCoreAPI.bungee().broadcast(getChannel(chan).getPort(), br);
//            br.releaseBuffer();
        });
    }

    private static void initializeClient() {
        initialize();
        side = Side.CLIENT;
        writer = new BukkitTextWriter("BungeeSwitchListener");
        BSLCoreAPI.writer().printText("Writer initialized");
        // Phase 1 - Global initialization
        BungeeInitializer.init();
        // Phase 2 - Reader Initialization
        BSLCoreAPI.writer().printText("Initializing phase 2 - Registering readers");
        getPacket(BungeeVariables.PACKET_VALIDATION_RESULT).register(buf -> new PacketValidationResult(buf.readBoolean()));

        getPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD).register(buf -> {
            buf.retain();
            return new PacketPayload(buf);
        });
        // Phase 3 - Writer Initialization
        BSLCoreAPI.writer().printText("Initializing phase 3 - Registering writers");
        getPacket(BungeeVariables.PACKET_VALIDATION).register((packet, buffer) -> {
            PacketValidation valid = (PacketValidation) packet;
            ByteBufUtility.writeString(buffer, valid.getId());
            buffer.writeInt(valid.getPort());
        });

        getPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD).register((packet, buffer) -> {
            PacketPayload payload = (PacketPayload) packet;
            buffer.writeBytes(payload.getBuffer());
//            payload.getBuffer().release();
        });


        getPacket(BungeeVariables.PACKET_TRANSFER_PAYLOAD).register((packet, buffer) -> {
            PacketTransferPayload payload = (PacketTransferPayload) packet;
            buffer.writeInt(payload.getServerPort());
            buffer.writeBytes(payload.getBuffer());
//            payload.getBuffer().release();
        });

        // Phase 4 = Listener Initialization
        BSLCoreAPI.writer().printText("Initializing phase 4 - Registering listeners");
        getPacket(BungeeVariables.PACKET_VALIDATION_RESULT).attachListener((chan, packet) -> {
            PacketValidationResult result = (PacketValidationResult) packet;
            if (!result.isValidated()) {
                BSLCoreAPI.writer().printError("Server connection due to illegal id. Please recheck configuration and retry.");
                chan.disconnect();
            } else {
                BSLCoreAPI.writer().printText("Server connected!");
            }
        });

        getPacket(BungeeVariables.PACKET_RECONSTRUCT).attachListener((chan, packet) -> {
            PacketReconstruct reconstructable = (PacketReconstruct) packet;
            int recID = reconstructable.getBuffer().readInt();
            AbstractPacket pac = getPacket(recID);
            if (pac == null) {
                BSLCoreAPI.writer().printError("Cannot reconstruct packet ID " + recID);
                return;
            }
            pac.listen(chan, pac.reader().read(reconstructable.getBuffer()));
        });

        getPacket(BungeeVariables.PACKET_BROADCAST_PACKET).attachListener((chan, packet) -> {
            PacketBroadcast reconstructable = (PacketBroadcast) packet;
            reconstructable.getBuffer().readInt();
            int recID = reconstructable.getBuffer().readInt();
            AbstractPacket pac = getPacket(recID);
            if (pac == null) {
                BSLCoreAPI.writer().printError("Cannot reconstruct broadcasted packet ID " + recID);
                return;
            }
            pac.retainBuffer();
            pac.listen(chan, pac.reader().read(reconstructable.getBuffer()));
        });
    }


    public static BSLChannel getChannel(int i) {
        return channels.get(i);
    }


    public static BSLChannel getChannel(Channel c) {
        return channelsWrap.get(c);
    }

    public static void registerChannel(int port, BSLChannel chan) {
        channels.put(port, chan);
        channelAlive.put(chan.getChannel(), new ChannelKeepAliveThread(chan));
    }

    public static void unregisterChannel(int port) {
        BSLChannel cn = channels.remove(port);
        if (cn != null && channelAlive.containsKey(cn.getChannel()))
            channelAlive.remove(cn.getChannel()).stopThread();
    }

    public static List<Integer> getChannels() {
        return new ArrayList<>(channels.keySet());
    }
}

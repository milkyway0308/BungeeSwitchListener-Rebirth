package skywolf46.bsl.global;

import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.abstraction.packets.AbstractPacket;
import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.impl.bukkit.BukkitTextWriter;
import skywolf46.bsl.global.impl.bungeecord.BungeeTextSender;
import skywolf46.bsl.global.impl.packets.*;
import skywolf46.bsl.global.util.BSLChannel;
import skywolf46.bsl.global.util.ByteBufUtility;

import java.util.HashMap;

public class BungeeSwitchListenerCore {
    private static Side side;
    private static final Object LOCK = new Object();
    private static AbstractConsoleWriter writer = null;
    private static HashMap<Integer, AbstractPacket> packets = new HashMap<>();
    private static HashMap<Integer, BSLChannel> channels = new HashMap<>();
    private static String id;


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
        registerPacket(BungeeVariables.PACKET_VALIDATION, new PacketValidation(null, 0));
        registerPacket(BungeeVariables.PACKET_VALIDATION_RESULT, new PacketValidationResult(false));
        registerPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD, new PacketPayload(false));
        registerPacket(BungeeVariables.PACKET_RECEIVE_PAYLOAD, new PacketReceivePayload(0, false));
        registerPacket(BungeeVariables.PACKET_TRANSFER_PAYLOAD, new PacketTransferPayload(0, false));
    }


    private static void initializeServer() {
        initialize();
        side = Side.SERVER;
        writer = new BungeeTextSender();
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
            recv.getBuffer().release();
        });

        getPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD).register((packet, buffer) -> {
            PacketPayload payload = (PacketPayload) packet;
            buffer.writeBytes(payload.getBuffer());
            payload.getBuffer().release();
        });

        // Phase 4 = Listener Initialization
        BSLCoreAPI.writer().printText("Initializing phase 4 - Registering listeners");
        getPacket(BungeeVariables.PACKET_VALIDATION).attachListener((c, pac) -> {
            PacketValidation val = (PacketValidation) pac;
            try {
                System.out.println("Client ID " + val.getId());
                BSLChannel chan = new BSLChannel(c);
                if (val.getId().equals(id)) {
                    BSLCoreAPI.writer().printText("Server connected from port " + val.getPort());
                    channels.put(val.getPort(), chan);
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

    }

    private static void initializeClient() {
        initialize();
        side = Side.CLIENT;
        writer = new BukkitTextWriter();
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
            payload.getBuffer().release();
        });


        getPacket(BungeeVariables.PACKET_TRANSFER_PAYLOAD).register((packet, buffer) -> {
            PacketTransferPayload payload = (PacketTransferPayload) packet;
            buffer.writeInt(payload.getServerPort());
            buffer.writeBytes(payload.getBuffer());
            payload.getBuffer().release();
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
    }


}

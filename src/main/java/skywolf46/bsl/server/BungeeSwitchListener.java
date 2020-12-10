package skywolf46.bsl.server;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.BungeeVariables;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.api.BSLCoreAPI;
import skywolf46.bsl.global.impl.packets.PacketPayload;
import skywolf46.bsl.global.util.ByteBufUtility;
import skywolf46.bsl.server.command.ConnectedInfoCommand;
import skywolf46.bsl.server.configuration.BungeeSwitchConfiguration;
import skywolf46.bsl.server.netty.hijack.BungeeConnectionHijacker;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class BungeeSwitchListener extends Plugin {
    private static BungeeSwitchConfiguration config;
    private static HashMap<Integer, Channel> serverChannels = new HashMap<>();


    @Override
    public void onEnable() {
        BungeeSwitchListenerCore.init(Side.SERVER);
        BSLCoreAPI.writer().printText("BSLCore initialized to server (Bungeecord)");
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new ConnectedInfoCommand());
        File fx = new File(getDataFolder(), "config.yml");
        if (!fx.exists()) {
            BSLCoreAPI.writer().printText("Configuration file not exists. Generating new one.");
//            saveResource("config.yml", true);
            try {
                fx.getParentFile().mkdirs();
                fx.createNewFile();
                Files.copy(getResourceAsStream("bungee/config.yml"), fx.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BSLCoreAPI.writer().printText("Parsing configuration...");
        Configuration configuration = null;
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            BSLCoreAPI.writer().printError("Configuration parsing failed.");
        }
        config = new BungeeSwitchConfiguration(configuration);
        BungeeSwitchListenerCore.setIdentify(config.getAuthID());
        BungeeConnectionHijacker.hijack();
        BSLCoreAPI.getPacket(BungeeVariables.PACKET_GLOBAL_PAYLOAD)
                .attachListener((c, packet) -> {
                    PacketPayload payload = (PacketPayload) packet;
                    System.out.println(ByteBufUtility.readString(payload.getBuffer()));
                    System.out.println(((PacketPayload) packet).getBuffer().readInt());
                });
    }

    public static BungeeSwitchConfiguration getConfig() {
        return config;
    }


}

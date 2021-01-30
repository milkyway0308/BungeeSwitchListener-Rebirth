package skywolf46.bsl.client;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.bsl.client.configuration.BukkitSwitchConfiguration;
import skywolf46.bsl.client.netty.BukkitFakePlayerConnection;
import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.abstraction.AbstractPortProvider;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.api.BSLCoreAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BukkitSwitchListener extends JavaPlugin {
    private static BukkitFakePlayerConnection defConnection;
    private static BukkitSwitchConfiguration configuration;

    @Override
    public void onEnable() {
        BungeeSwitchListenerCore.init(Side.CLIENT);
        BungeeSwitchListenerCore.setPortProvider(new AbstractPortProvider() {
            @Override
            public int getPort() {
                return Bukkit.getPort();
            }
        });
        BSLCoreAPI.writer().printText("BSLCore initialized with Client(Bukkit)");
        File fx = new File(getDataFolder(), "config.yml");
        if (!fx.exists()) {
            BSLCoreAPI.writer().printText("Configuration file not exists. Generating new one.");
//            saveResource("config.yml", true);
            try {
                fx.getParentFile().mkdirs();
                fx.createNewFile();
                Files.copy(getResource("bukkit/config.yml"), fx.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BSLCoreAPI.writer().printText("Parsing configuration...");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(fx);
        configuration = new BukkitSwitchConfiguration(conf);
        BungeeSwitchListenerCore.setIdentify(configuration.getIdentify());
        BSLCoreAPI.writer().printText("Creating connection with bungeecord...");
        defConnection = new BukkitFakePlayerConnection();
    }

    public static BukkitSwitchConfiguration getConfiguration() {
        return configuration;
    }
}

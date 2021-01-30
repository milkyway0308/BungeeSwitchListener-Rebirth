package skywolf46.bsl.client.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

public class BukkitSwitchConfiguration {
    private String identify;
    private String ip;
    private int port;

    public BukkitSwitchConfiguration(YamlConfiguration conf){
        identify = conf.getString("System Identify Code");
        port = conf.getInt("Bungeecord Port");
        ip = conf.getString("Bungeecord IP");
    }

    public String getIP() {
        return ip;
    }

    public String getIdentify() {
        return identify;
    }

    public int getPort() {
        return port;
    }
}

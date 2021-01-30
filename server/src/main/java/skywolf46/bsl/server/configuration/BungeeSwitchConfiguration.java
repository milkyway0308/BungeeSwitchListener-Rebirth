package skywolf46.bsl.server.configuration;

import net.md_5.bungee.config.Configuration;

public class BungeeSwitchConfiguration {
    private String authID;
    private boolean onlyLocalHost;

    public BungeeSwitchConfiguration(Configuration configuration) {
        this.authID = configuration.getString("System Identify Code");
        this.onlyLocalHost = configuration.getBoolean("Enable localhost-only connection");
    }

    public String getAuthID() {
        return authID;
    }

    public boolean isOnlyLocalHost() {
        return onlyLocalHost;
    }
}

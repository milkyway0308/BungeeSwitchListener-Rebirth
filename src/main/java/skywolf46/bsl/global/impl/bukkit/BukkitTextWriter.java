package skywolf46.bsl.global.impl.bukkit;

import org.bukkit.Bukkit;
import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;

public class BukkitTextWriter extends AbstractConsoleWriter {
    @Override
    public void printText(String x) {
        Bukkit.getConsoleSender().sendMessage("§aBungeeSwitchListener §f| §f" + x);
    }

    @Override
    public void printError(String x) {
        Bukkit.getConsoleSender().sendMessage("§aBungeeSwitchListener §f| §c" + x);
    }
}

package skywolf46.bsl.global.impl.bukkit;

import org.bukkit.Bukkit;
import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;

public class BukkitTextWriter extends AbstractConsoleWriter {
    private String prefix;

    public BukkitTextWriter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void printText(String x) {
        Bukkit.getConsoleSender().sendMessage("§a" + prefix + " §f| §f" + x);
    }

    @Override
    public void printError(String x) {
        Bukkit.getConsoleSender().sendMessage("§a" + prefix + " §f| §c" + x);
    }

    @Override
    public AbstractConsoleWriter of(String prefix) {
        return new BukkitTextWriter(prefix);
    }
}

package skywolf46.bsl.global.impl.bungeecord;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import org.fusesource.jansi.Ansi;
import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;

public class BungeeTextSender extends AbstractConsoleWriter {

    public void printText(String x) {
        BungeeCord.getInstance().getConsole().sendMessage(
                new TextComponent(
                        Ansi.ansi().fg(Ansi.Color.GREEN)
                                .a("BungeeSwitchListener")
                                .fg(Ansi.Color.WHITE)
                                .a(" | ")
                                .fg(Ansi.Color.DEFAULT)
                                .a(x)
                                .toString()
                )
        );
    }


    public void printError(String x) {
        BungeeCord.getInstance().getConsole().sendMessage(
                new TextComponent(
                        Ansi.ansi().fg(Ansi.Color.GREEN)
                                .a("BungeeSwitchListener")
                                .fg(Ansi.Color.WHITE)
                                .a(" | ")
                                .fg(Ansi.Color.RED)
                                .a(x)
                                .toString()
                )
        );
    }
}

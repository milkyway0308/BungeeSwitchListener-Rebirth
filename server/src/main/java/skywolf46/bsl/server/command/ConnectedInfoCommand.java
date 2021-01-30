package skywolf46.bsl.server.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.abstraction.AbstractConsoleWriter;
import skywolf46.bsl.global.abstraction.enums.Side;
import skywolf46.bsl.global.api.BSLCoreAPI;

import java.util.List;

public class ConnectedInfoCommand extends Command {
    private static AbstractConsoleWriter writer = BSLCoreAPI.writer("BSL-Command");

    public ConnectedInfoCommand() {
        super("bslservers", "bsl.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if (BSLCoreAPI.getSide() == Side.SERVER) {
            List<Integer> servers = BungeeSwitchListenerCore.getChannels();
            writer.printText("Connected server (" + servers.size() + ")");
            for (int x : BungeeSwitchListenerCore.getChannels()) {
                writer.printText("- Port " + x);
            }
        } else {
            BSLCoreAPI.writer("BSL-Command").printError("Client side server list is not supported");
        }
    }
}

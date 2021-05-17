package skywolf46.bsl.client.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import skywolf46.bsl.client.BSLClient

class BSLListener : Listener {
    @EventHandler
    fun PlayerLoginEvent.onEvent() {
        when (BSLClient.status) {
            0 -> {
                kickMessage = "§eBSL-Core\nBSL is enabling..."
                result = PlayerLoginEvent.Result.KICK_WHITELIST
            }
            1 -> {
                if (player.isOp || player.hasPermission("bsl.admin")) {
                    player.sendMessage("§cConnected with §fAdmin bypass§c; Be careful, system data can be corrupted in unconnected server")
                    return
                }
                kickMessage = "§eBSL-Core\nBSL failure; Cannot connect to host"
                result = PlayerLoginEvent.Result.KICK_WHITELIST
            }
            2 -> {
                if (player.isOp || player.hasPermission("bsl.admin")) {
                    player.sendMessage("§cConnected with §fAdmin bypass§c; Be careful, system data can be corrupted in error occured server")
                    return
                }
                kickMessage = "§eBSL-Core\nBSL failure; System error. Connection will blocked until system restored."
                result = PlayerLoginEvent.Result.KICK_WHITELIST
            }
        }
    }
}
package skywolf46.bsl.server

import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.entity.Player

class BSLServer : Plugin() {
    var port = 44019
    override fun onEnable() {
        println("BSL | Host server starting on ${port}")
    }
}
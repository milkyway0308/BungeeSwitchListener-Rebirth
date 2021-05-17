package skywolf46.bsl.client

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import skywolf46.bsl.client.listener.BSLListener
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.impl.BSLServerSocket
import skywolf46.extrautility.util.log
import skywolf46.extrautility.util.register
import skywolf46.extrautility.util.schedule
import java.io.File

class BSLClient : JavaPlugin() {
    companion object {
        lateinit var systemVerify: String
            private set
        lateinit var ip: String
            private set
        var port: Int = -1
            private set
        lateinit var socket: BSLServerSocket
            private set
        var status = 0
            private set
    }

    override fun onEnable() {
        BSLCore.init()
        with(File("config.yml")) {
            if (!exists())
                saveResource("config.yml", true)
            val load = YamlConfiguration.loadConfiguration(this)
            systemVerify = load.getString("System Identify Code")
            ip = load.getString("Bungeecord IP")
            port = load.getInt("Bungeecord Port")
        }
        BSLListener().register()
        socket = BSLServerSocket(ip = ip, port = port)
        socket.onFailure {
            log("§bBSL-§cCritical §f| §cConnection failure! Blocking connection, Retry until connected.")
            status = 1
            schedule(20L) { socket.retry() }
        }
        socket.onError {
            status = 2
            log("§bBSL-§cCritical §f| §cSystem failure! Blocking connection until restored")
            log("§bBSL-§cCritical §f| §4Server requires restart!")

            for (x in Bukkit.getOnlinePlayers()) {
                x.kickPlayer("§bBSL - §cCritical \n§7System failure / Server will blocked until restored")
            }
        }
        socket.onSuccess {
            status = 3
            log("§bBSL-Core §f| §fTry to verify self..")

        }
        socket.retry()
    }


}
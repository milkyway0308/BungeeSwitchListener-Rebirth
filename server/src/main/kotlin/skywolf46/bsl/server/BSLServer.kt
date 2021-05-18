package skywolf46.bsl.server

import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import skywolf46.bsl.core.BSLCore
import skywolf46.bsl.core.impl.BSLServerHost
import skywolf46.bsl.core.security.permissions.SecurityPermissions
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class BSLServer : Plugin() {
    var port = 44019
    val permissionMap = mutableMapOf<String, Pair<String, List<SecurityPermissions>>>()

    override fun onEnable() {
        BSLCore.isServer = true
        BSLCore.init(getResourceAsStream("system.properties"))
        BSLCore.scanAll(file)
        with(File(dataFolder, "config.yml")) {
            if (!exists()) {
                parentFile.mkdirs()
                createNewFile()
                Files.copy(getResourceAsStream("bungee/config.yml"), toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            val configuration: Configuration =
                ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(this)
            port = configuration.getInt("Server Port", 57719)
            if (configuration.getSection("System Codes") != null) {
                val sector = configuration.getSection("System Codes")
                for (x in sector.keys) {
                    val permSection = sector.getSection(x) ?: continue
                    if (!permSection.contains("Name") || !permSection.contains("Permission"))
                        continue
                    val permissions = mutableListOf<SecurityPermissions>()
                    for (perm in permSection.getStringList("Permission")) {
                        try {
                            permissions += SecurityPermissions.valueOf(perm)
                        } catch (_: Exception) {
                            // Ignore exception
                        }
                    }
                    permissionMap[x] = permSection.getString("Name") to permissions
                    println("BSLServer | Registered permission ${permSection.getString("Name")} to $permissions")
                }
            }
            BSLServerHost(port)
            println("BSL | Host server starting on $port")
        }
    }

    override fun onDisable() {
        BSLServerHost.host?.stopServer()
    }
}
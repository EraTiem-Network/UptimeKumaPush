package net.eratiem.uptimekumapush

import java.net.URI

class ConfigManager(plugin: UptimeKumaPushPlugin) {
    companion object {
        lateinit var INSTANCE: ConfigManager
    }

    var pushUrl: URI

    init {
        INSTANCE = this

        plugin.saveDefaultConfig()

        val config = plugin.config
        pushUrl = config.getString("KumaPushUrl")?.let { URI(it) }!!
    }
}
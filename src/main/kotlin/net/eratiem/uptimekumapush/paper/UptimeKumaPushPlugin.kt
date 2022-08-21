package net.eratiem.uptimekumapush.paper

import net.eratiem.uptimekumapush.tools.ConfigManager
import net.eratiem.uptimekumapush.tools.Tools
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class UptimeKumaPushPlugin : JavaPlugin() {
    private lateinit var task: BukkitTask

    override fun onEnable() {
        try {
            ConfigManager(this.dataFolder)
        } catch (e: Exception) {
            logger.warning("Failed to load config! Please check your config.yml\n${e.stackTraceToString()}")
            server.pluginManager.disablePlugin(this)
            return
        }

        task = server.scheduler.runTaskTimerAsynchronously(
            this, Tools.pushToKuma(logger), 0, 60L
        )

        logger.info("UptimeKumaPush up and running!")
    }

    override fun onDisable() {
        if (::task.isInitialized) {
            task.cancel()
        }
        logger.info("UptimeKumaPush disabled")
    }
}
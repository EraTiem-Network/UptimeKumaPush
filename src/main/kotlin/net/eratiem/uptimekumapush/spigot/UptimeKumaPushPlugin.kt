package net.eratiem.uptimekumapush.spigot

import net.eratiem.eralogger.tools.EraLogger
import net.eratiem.uptimekumapush.tools.ConfigManager
import net.eratiem.uptimekumapush.tools.Tools
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.slf4j.Logger

class UptimeKumaPushPlugin : JavaPlugin() {
    private lateinit var task: BukkitTask
    private lateinit var logger: EraLogger

    override fun onEnable() {
        try {
            ConfigManager(this.dataFolder)
            logger = EraLogger.getInstance(name, getLogger() as Logger)
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
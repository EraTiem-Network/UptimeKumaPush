package net.eratiem.uptimekumapush.bungee

import net.eratiem.eralogger.tools.EraLogger
import net.eratiem.uptimekumapush.tools.ConfigManager
import net.eratiem.uptimekumapush.tools.Tools
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.scheduler.ScheduledTask
import org.slf4j.Logger
import java.util.concurrent.TimeUnit.SECONDS

class UptimeKumaPushPlugin : Plugin() {
    private lateinit var task: ScheduledTask
    private lateinit var logger: EraLogger

    override fun onEnable() {
        try {
            ConfigManager(this.dataFolder)
            logger = EraLogger.getInstance("UptimeKumaPush", getLogger() as Logger)
        } catch (e: Exception) {
            logger.warning("Failed to load config! Please check your config.yml\n${e.stackTraceToString()}")
            this.onDisable()
            return
        }

        task = proxy.scheduler.schedule(
            this, Tools.pushToKuma(logger), 0, 60L, SECONDS
        )

        logger.info("UptimeKumaPush up and running!")
    }

    override fun onDisable() {
        if (::task.isInitialized)
            task.cancel()
        logger.info("UptimeKumaPush disabled")
        EraLogger.destroyInstance("UptimeKumaPush")
    }
}
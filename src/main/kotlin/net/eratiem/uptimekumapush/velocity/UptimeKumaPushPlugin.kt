package net.eratiem.uptimekumapush.velocity

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.scheduler.ScheduledTask
import net.eratiem.eralogger.tools.EraLogger
import net.eratiem.uptimekumapush.tools.ConfigManager
import net.eratiem.uptimekumapush.tools.Tools
import org.slf4j.Logger
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Plugin(
    id = "uptimekumapush", name = "UptimeKumaPush", version = "1.2.0",
    description = "A Plugin to Push Uptime-Data to Kuma", authors = ["Motzkiste"],
    dependencies = [Dependency(id = "kotlinprovider"), Dependency(id = "eralogger")]
)
class UptimeKumaPushPlugin @Inject constructor(
    private val server: ProxyServer,
    logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {
    private var task: ScheduledTask? = null
    private val logger: EraLogger = EraLogger.getInstance("UptimeKumaPush", logger)

    @Subscribe
    fun onProxyInitializeEvent(event: ProxyInitializeEvent) {
        try {
            ConfigManager(dataDirectory.toFile())

            task = server.scheduler
                .buildTask(this, Tools.pushToKuma(logger))
                .repeat(1L, TimeUnit.MINUTES)
                .schedule()

            logger.info("UptimeKumaPushVelocity is enabled")
        } catch (e: Exception) {
            logger.warning("Failed to load config! Please check your config.yml | ${e.message}\n${e.stackTraceToString()}")
        }
    }

    @Subscribe
    fun onProxyShutdownEvent(event: ProxyShutdownEvent) {
        task?.cancel()

        logger.info("UptimeKumaPushVelocity is disabled")
    }
}
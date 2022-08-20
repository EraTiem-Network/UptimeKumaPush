package net.eratiem.uptimekumapush.velocity

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.scheduler.ScheduledTask
import net.eratiem.uptimekumapush.tools.ConfigManager
import net.eratiem.uptimekumapush.tools.Tools
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import javax.inject.Inject

@Plugin(
    id = "uptimekumapush", name = "UptimeKumaPush", version = "1.0.0",
    description = "A Plugin to Push Uptime-Data to Kuma", authors = ["Motzkiste"],
    dependencies = [Dependency(id = "KotlinProvider")]
)
class UptimeKumaPushPlugin @Inject constructor(
    server: ProxyServer,
    private val logger: Logger,
    @DataDirectory dataDirectory: Path
) {
    private val task: ScheduledTask

    init {
        ConfigManager(dataDirectory.toFile())

        task = server.scheduler
            .buildTask(this, Tools.pushToKuma(logger))
            .repeat(1L, TimeUnit.MINUTES)
            .schedule()

        logger.info("UptimeKumaPushVelocity is enabled")
    }

    @Subscribe
    fun onProxyShutdownEvent(event: ProxyShutdownEvent) {
        task.cancel()

        logger.info("UptimeKumaPushVelocity is disabled")
    }
}
package net.eratiem.uptimekumapush

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.logging.Level.FINE

class UptimeKumaPushPlugin : JavaPlugin() {
    private val KUMA_MESSAGE = "{\"ok\":true}"
    private lateinit var task: BukkitTask

    override fun onEnable() {
        try {
            ConfigManager(this)
        } catch (e: Exception) {
            logger.warning("Failed to load config! Please check your config.yml")
            server.pluginManager.disablePlugin(this)
            return
        }

        task = server.scheduler.runTaskTimerAsynchronously(
            this,
            pushToKuma, 0, 60L
        )

        logger.info("UptimeKumaPush up and running!")
    }

    override fun onDisable() {
        if (::task.isInitialized) {
            task.cancel()
        }
        logger.info("UptimeKumaPush disabled")
    }

    private val pushToKuma = Runnable {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(ConfigManager.INSTANCE.pushUrl)
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (KUMA_MESSAGE == response.body())
            server.logger.fine("Successfully pushed to Uptime Kuma")
        else
            server.logger.info("Error push to Uptime Kuma")
    }
}
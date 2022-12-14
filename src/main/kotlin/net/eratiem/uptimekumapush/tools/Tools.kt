package net.eratiem.uptimekumapush.tools

import net.eratiem.eralogger.tools.EraLogger
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class Tools {
    companion object {
        private val KUMA_MESSAGE = "{\"ok\":true}"

        fun pushToKuma(logger: EraLogger) = Runnable {
            val client = HttpClient.newHttpClient()
            val request = HttpRequest.newBuilder()
                .uri(ConfigManager.config.pushUrl)
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            if (KUMA_MESSAGE == response.body())
                logger.debug("Successfully pushed to Uptime Kuma")
            else
                logger.info("Error push to Uptime Kuma")
        }
    }
}
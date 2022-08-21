package net.eratiem.uptimekumapush.tools

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.net.URI

class ConfigManager(dataFolder: File) {
    companion object {
        lateinit var config: Config
    }

    init {
        val configFile = File(dataFolder, "config.yml")

        // create config file if not exists
        if (!configFile.exists()) {
            dataFolder.mkdirs()
            ConfigManager::class.java.getResource("/config.yml")?.let { configFile.writeText(it.readText()) }
        }

        val configMap: Map<String, String> = Yaml().load(FileInputStream(configFile))

        config = Config(
            pushUrl = URI(configMap["pushUrl"]!!)
        )
    }

    data class Config(val pushUrl: URI)
}
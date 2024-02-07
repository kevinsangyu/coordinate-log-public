package xyz.mlhmz.mcserverinformation.coordinatelog.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {
    private ConfigUtil() {}

    public static ConfigurationSection getOrCreateSection(FileConfiguration config, String path) {
        ConfigurationSection section;
        if (config.isConfigurationSection(path)) {
            section = config.getConfigurationSection(path);
        } else {
            section = config.createSection(path);
        }
        return section;
    }

    public static ConfigurationSection getOrCreateSection(ConfigurationSection existingSection, String path) {
        ConfigurationSection section;
        if (existingSection.isConfigurationSection(path)) {
            section = existingSection.getConfigurationSection(path);
        } else {
            section = existingSection.createSection(path);
        }
        return section;
    }
}

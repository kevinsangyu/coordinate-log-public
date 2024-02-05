package xyz.mlhmz.mcserverinformation.coordinatelog.services;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntryServiceImpl {
    public static final String LOGS_ENTRIES_KEY = "logsentries";
    public static final String SEPARATOR = ".";
    public static final String LOGS_KEY = "logs";
    CoordinateLog plugin;

    public EntryServiceImpl(CoordinateLog plugin) {
        this.plugin = plugin;
    }

    public void saveEntry(Entry entry) {
        FileConfiguration config = plugin.getConfig();
        String title = entry.getTitle();
        boolean entryExisting = config.getStringList(LOGS_KEY).stream().anyMatch(title::equalsIgnoreCase);
        if (!entryExisting) {
            config.getStringList(LOGS_KEY).add(entry.getTitle());
            ConfigurationSection section = config.createSection(LOGS_ENTRIES_KEY + SEPARATOR + entry.getTitle());
            section.set("title", entry.getTitle());
            section.set("player", entry.getPlayer().toString());
            section.set("location", entry.getLocation());
        }
        plugin.saveConfig();
    }

    public Entry loadEntry(String title) {
        FileConfiguration config = plugin.getConfig();
        return getEntryFromConfig(config, title);
    }

    public List<Entry> loadEntries() {
        FileConfiguration config = plugin.getConfig();
        List<String> logsList = config.getStringList(LOGS_KEY);
        return logsList.stream().map(entry -> getEntryFromConfig(config, entry)).collect(Collectors.toList());
    }

    private Entry getEntryFromConfig(FileConfiguration config, String title) {
        ConfigurationSection configurationSection = config.getConfigurationSection(LOGS_ENTRIES_KEY + SEPARATOR + title);
        return new Entry(configurationSection.getString("title"), UUID.fromString(configurationSection.getString("player")), configurationSection.getLocation("location"));
    }
}

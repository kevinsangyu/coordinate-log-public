package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntryStoreImpl {
    public static final String LOGS_ENTRIES_KEY = "logsentries";
    public static final String SEPARATOR = ".";
    public static final String LOGS_KEY = "logs";
    public static final String INDEX_FIELD_KEY = "index";
    public static final String TITLE_FIELD_KEY = "title";
    public static final String PLAYER_FIELD_KEY = "player";
    public static final String LOCATION_FIELD_KEY = "location";
    private final CoordinateLog plugin;
    private final PlayerCountStore countStore;

    public EntryStoreImpl(CoordinateLog plugin) {
        this.plugin = plugin;
        countStore = CoordinateLog.getInstance(PlayerCountStore.class);
    }

    public void saveEntry(Entry entry) {
        FileConfiguration config = plugin.getConfig();
        String title = entry.getTitle();
        boolean entryExisting = config.getStringList(LOGS_KEY).stream().anyMatch(title::equalsIgnoreCase);
        if (!entryExisting) {
            config.getStringList(LOGS_KEY).add(entry.getTitle());
            ConfigurationSection section = config.createSection(LOGS_ENTRIES_KEY + SEPARATOR + entry.getTitle());
            UUID playerUUID = entry.getPlayer();
            section.set(INDEX_FIELD_KEY, countStore.incrementAndGetCount(playerUUID));
            section.set(TITLE_FIELD_KEY, entry.getTitle());
            section.set(PLAYER_FIELD_KEY, playerUUID.toString());
            section.set(LOCATION_FIELD_KEY, entry.getLocation());
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
        return new Entry(
                configurationSection.getLong(INDEX_FIELD_KEY),
                configurationSection.getString(TITLE_FIELD_KEY),
                UUID.fromString(configurationSection.getString(PLAYER_FIELD_KEY)),
                configurationSection.getLocation(LOCATION_FIELD_KEY));
    }
}

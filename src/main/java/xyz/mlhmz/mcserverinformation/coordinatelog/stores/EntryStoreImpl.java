package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;
import xyz.mlhmz.mcserverinformation.coordinatelog.utils.ConfigUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntryStoreImpl implements EntryStore {
    public static final String LOGS_ENTRIES_KEY = "logsentries";
    public static final String SEPARATOR = ".";
    public static final String LOGS_KEY = "logs";
    public static final String INDEX_FIELD_KEY = "index";
    public static final String TITLE_FIELD_KEY = "title";
    public static final String PLAYER_FIELD_KEY = "player";
    public static final String LOCATION_FIELD_KEY = "location";
    public static final String PLAYER_LOGS = "playerLogs";
    public static final String ENTRIES_FIELD_KEY = "entries";
    private final CoordinateLog plugin;
    private final PlayerCountStore countStore;

    public EntryStoreImpl(CoordinateLog plugin) {
        this.plugin = plugin;
        countStore = CoordinateLog.getInstance(PlayerCountStore.class);
    }

    public void saveEntry(Entry entry) {
        FileConfiguration config = plugin.getConfig();

        String playerSectionPath = PLAYER_LOGS + SEPARATOR + entry.getPlayer();

        ConfigurationSection section = ConfigUtil.getOrCreateSection(config, playerSectionPath);

        // Get player and index
        UUID player = entry.getPlayer();
        long index = countStore.incrementAndGetCount(player);

        // Persist index into long list, use set to avoid duplications
        Set<Long> logs = new HashSet<>(section.getLongList(LOGS_KEY));
        logs.add(index);
        section.set(LOGS_KEY, logs.stream().toList());

        // Persist data into section
        ConfigurationSection entrySection = ConfigUtil.getOrCreateSection(section, ENTRIES_FIELD_KEY + SEPARATOR + index);
        entrySection.set(INDEX_FIELD_KEY, index);
        entrySection.set(TITLE_FIELD_KEY, entry.getTitle());
        entrySection.set(PLAYER_FIELD_KEY, player.toString());
        entrySection.set(LOCATION_FIELD_KEY, entry.getLocation());

        plugin.saveConfig();
    }


    public List<Entry> loadEntries(Player player) {
        FileConfiguration config = plugin.getConfig();
        List<Long> logsList = config.getLongList(PLAYER_LOGS + SEPARATOR + player.getUniqueId() + SEPARATOR + LOGS_KEY);
        return logsList.stream().map(index -> getEntryFromConfig(config, player.getUniqueId(), index)).collect(Collectors.toList());
    }

    private Entry getEntryFromConfig(FileConfiguration config, UUID uuid, Long index) {
        ConfigurationSection configurationSection = config.getConfigurationSection(
                String.join(SEPARATOR, PLAYER_LOGS, uuid.toString(), ENTRIES_FIELD_KEY, Long.toString(index)
                ));
        return new Entry(
                configurationSection.getLong(INDEX_FIELD_KEY),
                configurationSection.getString(TITLE_FIELD_KEY),
                UUID.fromString(configurationSection.getString(PLAYER_FIELD_KEY)),
                configurationSection.getLocation(LOCATION_FIELD_KEY));
    }
}

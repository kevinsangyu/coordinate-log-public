package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.objects.Page;
import xyz.mlhmz.mcserverinformation.coordinatelog.utils.ConfigUtil;

import java.util.*;

public class EntryStoreImpl implements EntryStore {
    public static final String SEPARATOR = ".";
    public static final String LOGS_KEY = "logs";
    public static final String PLAYER_LOGS = "playerLogs";
    public static final String ENTRIES_FIELD_KEY = "entries";
    public static final int PAGINATION_SIZE = 5;
    private final CoordinateLog plugin;
    private final PlayerCountStore countStore;

    public EntryStoreImpl(CoordinateLog plugin) {
        this.plugin = plugin;
        countStore = CoordinateLog.getInstance(PlayerCountStore.class);
    }

    public Entry saveEntry(Entry entry) {
        FileConfiguration config = plugin.getConfig();

        String playerSectionPath = PLAYER_LOGS + SEPARATOR + entry.getPlayer();

        ConfigurationSection section = ConfigUtil.getOrCreateSection(config, playerSectionPath);

        // Get player and index
        UUID player = entry.getPlayer();
        long index = countStore.incrementAndGetCount(player);
        entry.setIndex(index);

        // Persist index into long list, use set to avoid duplications
        Set<Long> logs = new HashSet<>(section.getLongList(LOGS_KEY));
        logs.add(index);
        section.set(LOGS_KEY, logs.stream().toList());

        // Persist data into section
        ConfigurationSection entries = ConfigUtil.getOrCreateSection(section, ENTRIES_FIELD_KEY);

        entry.setIndex(index);
        entries.set(Long.toString(index), entry);

        plugin.saveConfig();

        return entry;
    }


    public Page<Entry> loadEntries(Player player, int page) {
        FileConfiguration config = plugin.getConfig();
        List<Long> logsList = config.getLongList(PLAYER_LOGS + SEPARATOR + player.getUniqueId() + SEPARATOR + LOGS_KEY);
        List<Entry> entries = logsList.stream()
                .map(index -> getEntryFromConfig(config, player.getUniqueId(), index))
                .sorted(Comparator.comparingLong(Entry::getIndex).reversed())
                .toList();
        return Page.of(entries, page, PAGINATION_SIZE);
    }

    private Entry getEntryFromConfig(FileConfiguration config, UUID uuid, Long index) {
        ConfigurationSection configurationSection = ConfigUtil.getOrCreateSection(config,
                String.join(SEPARATOR, PLAYER_LOGS, uuid.toString(), ENTRIES_FIELD_KEY
                ));
        return (Entry) configurationSection.get(Long.toString(index));
    }
}

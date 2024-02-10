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

        ConfigurationSection section = getPlayersSection(config, entry.getPlayer());
        long index = getIndexAndApplyIntoEntry(entry);
        persistIndexIntoDuplicationAvoidanceSet(section, index);
        persistDataIntoEntriesSection(entry, section, index);
        plugin.saveConfig();
        return entry;
    }

    private long getIndexAndApplyIntoEntry(Entry entry) {
        UUID player = entry.getPlayer();
        long index = countStore.incrementAndGetCount(player);
        entry.setIndex(index);
        return index;
    }

    private void persistIndexIntoDuplicationAvoidanceSet(ConfigurationSection section, long index) {
        Set<Long> logs = new HashSet<>(section.getLongList(LOGS_KEY));
        logs.add(index);
        section.set(LOGS_KEY, logs.stream().toList());
    }

    private void persistDataIntoEntriesSection(Entry entry, ConfigurationSection section, long index) {
        ConfigurationSection entries = ConfigUtil.getOrCreateSection(section, ENTRIES_FIELD_KEY);
        entry.setIndex(index);
        entries.set(Long.toString(index), entry);
    }


    public Page<Entry> loadEntries(Player player, int page) {
        FileConfiguration config = plugin.getConfig();
        List<Long> logsList = getPlayersLogsList(config, player.getUniqueId());
        List<Entry> entries = logsList.stream()
                .map(index -> getEntryFromConfig(config, player.getUniqueId(), index))
                .sorted(Comparator.comparingLong(Entry::getIndex).reversed())
                .toList();
        return Page.of(entries, page, PAGINATION_SIZE);
    }

    @Override
    public boolean deleteEntry(Player player, long index) {
        FileConfiguration config = plugin.getConfig();
        List<Long> logsList = getPlayersLogsList(config, player.getUniqueId());
        Optional<Long> result = logsList.stream().filter(entry -> entry == index).findFirst();
        if (result.isPresent()) {
            ConfigurationSection playersSection = getPlayersSection(config, player.getUniqueId());
            ConfigurationSection entries = ConfigUtil.getOrCreateSection(playersSection, ENTRIES_FIELD_KEY);
            entries.set(Long.toString(index), null);
            List<Long> filteredList = logsList.stream().filter(entry -> entry != index).toList();
            playersSection.set(LOGS_KEY, filteredList);
            plugin.saveConfig();
            return true;
        }
        return false;
    }

    private List<Long> getPlayersLogsList(FileConfiguration config, UUID player) {
        ConfigurationSection playersSection = getPlayersSection(config, player);
        return playersSection.getLongList(LOGS_KEY);
    }

    private static ConfigurationSection getPlayersSection(FileConfiguration config, UUID player) {
        String playerSectionPath = PLAYER_LOGS + SEPARATOR + player;
        return ConfigUtil.getOrCreateSection(config, playerSectionPath);
    }

    private Entry getEntryFromConfig(FileConfiguration config, UUID uuid, Long index) {
        ConfigurationSection configurationSection = ConfigUtil.getOrCreateSection(config,
                String.join(SEPARATOR, PLAYER_LOGS, uuid.toString(), ENTRIES_FIELD_KEY
                ));
        return (Entry) configurationSection.get(Long.toString(index));
    }
}

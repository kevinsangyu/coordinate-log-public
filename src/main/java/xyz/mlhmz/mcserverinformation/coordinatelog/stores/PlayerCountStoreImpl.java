package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.PlayerCount;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerCountStoreImpl implements PlayerCountStore {
    public static final String COUNTS_SECTION_KEY = "counts";
    public static final String SEPARATOR = ".";
    public static final String COUNT_FIELD_KEY = "count";
    public static final String COUNT_PLAYERS_FIELD_KEY = "countPlayers";
    private final CoordinateLog plugin;

    public PlayerCountStoreImpl(CoordinateLog plugin) {
        this.plugin = plugin;
    }

    @Override
    public int incrementAndGetCount(UUID player) {
        FileConfiguration config = plugin.getConfig();
        Set<String> countPlayers = new HashSet<>(plugin.getConfig().getStringList(COUNT_PLAYERS_FIELD_KEY));
        boolean entryNew = countPlayers.add(player.toString());
        config.set(COUNT_PLAYERS_FIELD_KEY, countPlayers.stream().toList());
        PlayerCount playerCount;
        String path = COUNTS_SECTION_KEY + SEPARATOR + player;
        if (entryNew) {
            ConfigurationSection section = config.createSection(path);
            section.set(COUNT_FIELD_KEY, 1);
            playerCount = new PlayerCount(player, 1);
        } else {
            ConfigurationSection section = config.getConfigurationSection(path);
            int count = section.getInt(COUNT_FIELD_KEY) + 1;
            playerCount = new PlayerCount(player, count);
            section.set(COUNT_FIELD_KEY, count);
        }
        plugin.saveConfig();
        return playerCount.getCount();
    }
}

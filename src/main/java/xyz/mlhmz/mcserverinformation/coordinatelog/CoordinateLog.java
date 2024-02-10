package xyz.mlhmz.mcserverinformation.coordinatelog;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlhmz.mcserverinformation.coordinatelog.mc.commands.LogCommand;
import xyz.mlhmz.mcserverinformation.coordinatelog.mc.listeners.CoordinateEventListener;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.EntryStore;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.EntryStoreImpl;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.PlayerCountStore;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.PlayerCountStoreImpl;

import java.util.HashMap;
import java.util.Map;

public final class CoordinateLog extends JavaPlugin {
    public static final String PLUGIN_PREFIX = "&aCoordinateLog&7: ";
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        return clazz.cast(instances.get(clazz));
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        initializeInstancesMap();
        addCommandExecutors();
        addListeners();
    }

    private void addListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new CoordinateEventListener(), this);
    }

    private void addCommandExecutors() {
        getCommand("clog").setExecutor(new LogCommand());
    }

    private void initializeInstancesMap() {
        instances.put(PlayerCountStore.class, new PlayerCountStoreImpl(this));
        instances.put(EntryStore.class, new EntryStoreImpl(this));
    }
}

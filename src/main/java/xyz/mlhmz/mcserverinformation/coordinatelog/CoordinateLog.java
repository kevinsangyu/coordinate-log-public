package xyz.mlhmz.mcserverinformation.coordinatelog;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mlhmz.mcserverinformation.coordinatelog.services.EntryService;
import xyz.mlhmz.mcserverinformation.coordinatelog.services.EntryServiceImpl;

import java.util.HashMap;
import java.util.Map;

public final class CoordinateLog extends JavaPlugin {
    private static Map<Class<?>, Object> instances = new HashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        return clazz.cast(instances.get(clazz));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instances.put(EntryService.class, new EntryServiceImpl(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

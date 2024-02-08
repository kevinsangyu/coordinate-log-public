package xyz.mlhmz.mcserverinformation.coordinatelog.mc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.EntryStore;

public class CoordinateEventListener implements Listener {
    EntryStore entryStore;

    public CoordinateEventListener() {
        entryStore = CoordinateLog.getInstance(EntryStore.class);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        entryStore.saveEntry(new Entry("Death at", event.getEntity().getUniqueId(), event.getEntity().getLocation()));
    }
}

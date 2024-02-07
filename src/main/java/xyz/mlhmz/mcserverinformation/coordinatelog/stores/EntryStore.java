package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import org.bukkit.entity.Player;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;

import java.util.List;

public interface EntryStore {
    void saveEntry(Entry entry);

    List<Entry> loadEntries(Player player);
}

package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import org.bukkit.entity.Player;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.objects.Page;

public interface EntryStore {
    Entry saveEntry(Entry entry);

    Page<Entry> loadEntries(Player player, int page);

    boolean deleteEntry(Player player, long index);
}

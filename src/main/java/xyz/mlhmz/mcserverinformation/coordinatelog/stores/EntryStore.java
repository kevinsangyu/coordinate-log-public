package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;

import java.util.List;

public interface EntryStore {
    void saveEntry(Entry entry);

    Entry loadEntry(String title);

    List<Entry> loadEntries();
}

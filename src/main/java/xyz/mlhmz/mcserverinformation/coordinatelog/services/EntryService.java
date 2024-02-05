package xyz.mlhmz.mcserverinformation.coordinatelog.services;

import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;

import java.util.List;

public interface EntryService {
    void saveEntry(Entry entry);

    Entry loadEntry(String title);

    List<Entry> loadEntries();
}

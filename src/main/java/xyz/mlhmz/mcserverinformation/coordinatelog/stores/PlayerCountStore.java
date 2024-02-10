package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import java.util.UUID;

public interface PlayerCountStore {
    int incrementAndGetCount(UUID player);
}

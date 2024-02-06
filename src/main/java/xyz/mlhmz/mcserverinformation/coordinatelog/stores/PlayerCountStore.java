package xyz.mlhmz.mcserverinformation.coordinatelog.stores;

import java.util.UUID;

public interface PlayerCountStore {
    long incrementAndGetCount(UUID player);
}

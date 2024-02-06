package xyz.mlhmz.mcserverinformation.coordinatelog.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerCount {
    private UUID player;
    private long count;
}

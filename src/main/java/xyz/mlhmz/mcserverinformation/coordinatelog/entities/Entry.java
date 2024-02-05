package xyz.mlhmz.mcserverinformation.coordinatelog.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Entry {
    private String title;
    private UUID player;
    private Location location;
}

package xyz.mlhmz.mcserverinformation.coordinatelog.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entry {
    private long index;
    private String title;
    private UUID player;
    private Location location;
}

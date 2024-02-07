package xyz.mlhmz.mcserverinformation.coordinatelog.entities;

import lombok.*;
import org.bukkit.Location;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Entry {
    private long index;
    @NonNull
    private String title;
    @NonNull
    private UUID player;
    @NonNull
    private Location location;
}

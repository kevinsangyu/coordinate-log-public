package xyz.mlhmz.mcserverinformation.coordinatelog.entities;

import lombok.*;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@SerializableAs("Entry")
public class Entry implements ConfigurationSerializable {
    public static final String INDEX_FIELD_KEY = "index";
    public static final String TITLE_FIELD_KEY = "title";
    public static final String PLAYER_FIELD_KEY = "player";
    public static final String LOCATION_FIELD_KEY = "location";

    private long index;
    @NonNull
    private String title;
    @NonNull
    private UUID player;
    @NonNull
    private Location location;

    /**
     * Deserialization of the Object
     * <br/>
     * The unused Warning will be suppressed here, because this constructor is only used by the Plugin API on Runtime
     */
    @SuppressWarnings("unused")
    public Entry(Map<String, Object> result) {
        this.index = (long) result.get(INDEX_FIELD_KEY);
        this.title = (String) result.get(TITLE_FIELD_KEY);
        this.player = UUID.fromString((String) result.get(PLAYER_FIELD_KEY));
        this.location = (Location) result.get(LOCATION_FIELD_KEY);
    }

    @Override
    @NonNull
    public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put(INDEX_FIELD_KEY, index);
        result.put(TITLE_FIELD_KEY, title);
        result.put(PLAYER_FIELD_KEY, player.toString());
        result.put(LOCATION_FIELD_KEY, location);
        return result;
    }


}

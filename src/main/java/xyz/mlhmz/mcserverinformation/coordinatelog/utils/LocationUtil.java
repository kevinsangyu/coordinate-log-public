package xyz.mlhmz.mcserverinformation.coordinatelog.utils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class LocationUtil {

    public static final String X_DIMENSION = "X";
    public static final String Y_DIMENSION = "Y";
    public static final String Z_DIMENSION = "Z";

    private LocationUtil() {
    }

    public static Integer parseBlockCoordinate(Player player, String value, String dimension) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else if (value.equals("~")) {
            return getCurrentPlayerCoordinate(player, dimension);
        } else if (StringUtils.isNumeric(value)) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
    }

    private static Integer getCurrentPlayerCoordinate(Player player, String dimension) {
        return switch (dimension) {
            case X_DIMENSION -> player.getLocation().getBlockX();
            case Y_DIMENSION -> player.getLocation().getBlockY();
            case Z_DIMENSION -> player.getLocation().getBlockZ();
            default -> null;
        };
    }
}

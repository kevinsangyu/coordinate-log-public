package xyz.mlhmz.mcserverinformation.coordinatelog.utils;

import org.bukkit.ChatColor;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;

import java.util.Objects;

public class ChatUtil {
    private ChatUtil() {
    }

    public static String translate(String... text) {
        return translateWithoutPrefix(CoordinateLog.PLUGIN_PREFIX, String.join("", text));
    }

    public static String translateWithoutPrefix(String... text) {
        return ChatColor.translateAlternateColorCodes('&', String.join("", text));
    }

    public static String translateAndPrettyPrintEntry(Entry entry) {
        return ChatUtil.translateWithoutPrefix(String.format("&a%d &7- &2%s&7: X: &a%d&7, Y: &a%d&7, Z: &a%d&7 - World: &a%s",
                entry.getIndex(),
                entry.getTitle(),
                entry.getLocation().getBlockX(),
                entry.getLocation().getBlockY(),
                entry.getLocation().getBlockZ(),
                Objects.requireNonNull(entry.getLocation().getWorld()).getName()
        ));
    }
}

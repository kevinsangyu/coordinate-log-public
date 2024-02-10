package xyz.mlhmz.mcserverinformation.coordinatelog.mc.commands;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.EntryStore;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.objects.Page;
import xyz.mlhmz.mcserverinformation.coordinatelog.utils.ChatUtil;

import java.util.Optional;

import static xyz.mlhmz.mcserverinformation.coordinatelog.utils.LocationUtil.*;

public class LogCommand implements CommandExecutor {
    @Override
    public boolean onCommand(
            @NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String @NonNull[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players are allowed to execute this command!");
            return true;
        }

        if (args.length == 0) {
            sendWrongUsageMessage(commandSender, label);
            return false;
        }

        EntryStore entryStore = CoordinateLog.getInstance(EntryStore.class);
        switch (args[0]) {
            case "add" -> addEntry(player, args, entryStore, label);
            case "list" -> listEntries(player, entryStore, args);
            case "remove" -> removeEntry(player, args, entryStore, label);
            default -> {
                sendWrongUsageMessage(commandSender, label);
                return false;
            }
        }
        return true;
    }

    private void removeEntry(Player player, String[] args, EntryStore entryStore, String label) {
        if (args.length >= 2 && StringUtils.isNumeric(args[1])) {
            int index = Integer.parseInt(args[1]);
            boolean success = entryStore.deleteEntry(player, index);
            player.sendMessage(ChatUtil.translate(success ? String.format("The entry &a%d&7 was successfully deleted.", index) : "&cThe requested entry was not found."));
        } else {
            player.sendMessage("Wrong usage: Use /{} remove <index>".replace("{}", label));
        }
    }

    private void addEntry(Player player, String[] args, EntryStore entryStore, String label) {
        Optional<Location> location = Optional.empty();
        if (args.length == 2) {
            location = Optional.of(player.getLocation());
        } else if (args.length == 5) {
            location = parseCoordinateInput(player, args);
        }
        location.ifPresentOrElse(
                object -> {
                    Entry entry = entryStore.saveEntry(new Entry(args[1], player.getUniqueId(), object));
                    Location entryLocation = entry.getLocation();
                    player.sendMessage(
                            ChatUtil.translate(
                                    String.format(
                                            "Entry &a%s&7 added with the index &a%d&7 at X: &a%d&7 Y: &a%d&7 Z: &a%d&7 in World &a%s&7",
                                            entry.getTitle(),
                                            entry.getIndex(),
                                            entryLocation.getBlockX(),
                                            entryLocation.getBlockY(),
                                            entryLocation.getBlockZ(),
                                            entryLocation.getWorld().getName()
                                    )
                            )
                    );
                },
                () -> player.sendMessage(ChatUtil.translate("Wrong usage: Use /{} add <identifier> or /{} add <identifier> <x> <y> <z>".replace("{}", label)))
        );
    }

    private static Optional<Location> parseCoordinateInput(Player player, String[] args) {
        Integer x = parseBlockCoordinate(player, args[2], X_DIMENSION);
        Integer y = parseBlockCoordinate(player, args[3], Y_DIMENSION);
        Integer z = parseBlockCoordinate(player, args[4], Z_DIMENSION);
        if (x != null && y != null && z != null) {
            return Optional.of(new Location(player.getWorld(), x, y, z));
        } else {
            return Optional.empty();
        }
    }

    private void listEntries(Player player, EntryStore entryStore, String[] args) {
        Page<Entry> entries;
        if (args.length >= 2 && StringUtils.isNumeric(args[1])) {
            entries = entryStore.loadEntries(player, Integer.parseInt(args[1]));
        } else {
            entries = entryStore.loadEntries(player, 1);
        }
        player.sendMessage(ChatUtil.translateWithoutPrefix(String.format("&7List of entries &a%d/&a%d:", entries.getPageNumber(), entries.getAllPages())));
        entries.getEntries().forEach(entry -> player.sendMessage(ChatUtil.translateAndPrettyPrintEntry(entry)));
    }

    private void sendWrongUsageMessage(CommandSender commandSender, String label) {
        commandSender.sendMessage(ChatUtil.translate(String.format("Wrong usage: Use /%s <add|list|remove>", label)));
    }
}

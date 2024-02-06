package xyz.mlhmz.mcserverinformation.coordinatelog.mc.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mlhmz.mcserverinformation.coordinatelog.CoordinateLog;
import xyz.mlhmz.mcserverinformation.coordinatelog.entities.Entry;
import xyz.mlhmz.mcserverinformation.coordinatelog.stores.EntryStore;
import xyz.mlhmz.mcserverinformation.coordinatelog.utils.ChatUtil;

import java.util.List;

public class LogCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Only players are allowed to execute this command!");
            return true;
        }

        if (args == null || args.length == 0) {
            sendWrongUsageMessage(commandSender, label);
            return false;
        }

        EntryStore entryStore = CoordinateLog.getInstance(EntryStore.class);
        switch(args[0]) {
            case "add" -> addEntry(player, args, entryStore);
            case "list" -> listEntries(player, entryStore);
            case "remove" -> removeEntry(player, args, entryStore);
            default -> {
                sendWrongUsageMessage(commandSender, label);
                return false;
            }
        }
        return true;
    }

    private void removeEntry(Player player, String[] args, EntryStore entryStore) {
        // TODO: Build this feature
        player.sendMessage(ChatUtil.translate("This operation is not supported yet..."));
    }

    private void addEntry(Player player, String[] args, EntryStore entryStore) {
        Location location;
        if (StringUtils.isNumeric(args[1]) && StringUtils.isNumeric(args[2]) && StringUtils.isNumeric(args[3])) {
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);
            location = new Location(player.getWorld(), x, y, z);
        } else {
            location = player.getLocation();
        }
        Entry entry = new Entry();
        entry.setPlayer(player.getUniqueId());
        entry.setLocation(location);
        entryStore.saveEntry(entry);
    }

    private void listEntries(Player player, EntryStore entryStore) {
        player.sendMessage(ChatUtil.translateWithoutPrefix("&a&bList of entries&7:"));
        List<Entry> entries = entryStore.loadEntries();
        entries.forEach(entry -> player.sendMessage(ChatUtil.translateAndPrettyPrintEntry(entry)));
    }

    private void sendWrongUsageMessage(CommandSender commandSender, String label) {
        commandSender.sendMessage(ChatUtil.translate(String.format("Wrong usage: Use /%s <add|list|remove>", label)));
    }
}

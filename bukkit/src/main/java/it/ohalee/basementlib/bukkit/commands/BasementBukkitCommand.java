package it.ohalee.basementlib.bukkit.commands;

import it.ohalee.basementlib.api.bukkit.BasementBukkit;
import it.ohalee.basementlib.api.server.BukkitServer;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.spigotmc.SpigotConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class BasementBukkitCommand implements CommandExecutor {

    private final BasementBukkit basement;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basement.command.basement")) {
            sender.sendMessage(SpigotConfig.unknownCommandMessage);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "/basement servers - View server list");
            sender.sendMessage(ChatColor.AQUA + "/basement reload - Reload config");
            sender.sendMessage(ChatColor.AQUA + "/basement info - Show current server ID");
            return true;
        }

        if (args[0].equalsIgnoreCase("servers")) {
            Collection<BukkitServer> serverList = basement.getServerManager().getServers();
            sender.sendMessage(ChatColor.AQUA + "Servers (" + serverList.size() + ")");

            List<BukkitServer> toSort = new ArrayList<>(serverList);
            toSort.sort(Comparator.comparing(BukkitServer::getName));
            for (BukkitServer server : toSort) {
                sender.sendMessage(ChatColor.DARK_AQUA + "  " + server.getName() + " (" + server.getOnline() + ") - " + server.getStatus().name());
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            basement.reloadConfig();
            sender.sendMessage(ChatColor.DARK_AQUA + "Config reloaded!");
        } else if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Server: " + ChatColor.AQUA + basement.getServerID());
        } else {
            sender.sendMessage(ChatColor.RED + "Not valid");
        }

        return true;
    }
}

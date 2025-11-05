package com.example.rtpqueue;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RtpQueuePlusCommand implements CommandExecutor {
    private final RtpQueuePlusPlugin plugin;

    public RtpQueuePlusCommand(RtpQueuePlusPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                // Reload command - requires admin permission
                if (!sender.hasPermission("rtpqueueplus.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }
                
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully!");
                return true;
            } else if (args[0].equalsIgnoreCase("leave")) {
                // Leave command - requires player and use permission
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                if (!player.hasPermission("rtpqueueplus.use")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }
                
                RtpQueuePlusListener listener = plugin.getListener();
                if (listener != null) {
                    listener.leaveQueue(player);
                }
                return true;
            }
        }
        
        // Default command - open GUI
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        if (!player.hasPermission("rtpqueueplus.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }
        
        RtpQueuePlusGUI.openQueueGUI(player);
        return true;
    }
} 
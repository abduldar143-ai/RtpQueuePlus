package com.example.rtpqueue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Random;

public class RtpQueuePlusListener implements Listener {
    private final RtpQueuePlusPlugin plugin;
    private final Map<String, Queue<Player>> queues = new HashMap<>();
    private final Set<UUID> frozenPlayers = new HashSet<>();
    private final Set<UUID> inDuel = new HashSet<>();

    public RtpQueuePlusListener(RtpQueuePlusPlugin plugin) {
        this.plugin = plugin;
        plugin.setListener(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String guiTitle = plugin.getConfig().getString("gui.title", "1v1 Queue");
        if (!event.getView().getTitle().equals(guiTitle)) return;
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        String queueType = getQueueTypeFromItem(clicked);
        if (queueType == null) return;
        String worldName = getWorldFromItem(clicked);
        player.closeInventory();
        queues.putIfAbsent(queueType, new LinkedList<>());
        Queue<Player> queue = queues.get(queueType);
        if (queue.contains(player)) {
            player.sendMessage(ChatColor.RED + "You are already in the queue for " + queueType + "!");
            return;
        }
        queue.add(player);
        player.sendMessage(ChatColor.GREEN + "You have joined the queue for " + queueType + " arena!");
        player.sendMessage(ChatColor.YELLOW + "Use /1v1 leave to leave the queue.");
        if (queue.size() >= 2) {
            Player p1 = queue.poll();
            Player p2 = queue.poll();
            startDuel(queueType, p1, p2, worldName);
        }
    }

    private String getQueueTypeFromItem(ItemStack item) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) plugin.getConfig().getList("gui.items");
        if (items != null) {
            for (Map<String, Object> obj : items) {
                Material mat = Material.valueOf(obj.get("material").toString());
                if (item.getType() == mat) {
                    return obj.get("queue").toString();
                }
            }
        }
        return null;
    }

    private String getWorldFromItem(ItemStack item) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) plugin.getConfig().getList("gui.items");
        if (items != null) {
            for (Map<String, Object> obj : items) {
                Material mat = Material.valueOf(obj.get("material").toString());
                if (item.getType() == mat) {
                    return obj.get("world").toString();
                }
            }
        }
        return "world";
    }

    private void startDuel(String queueType, Player p1, Player p2, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            p1.sendMessage(ChatColor.RED + "World not found!");
            p2.sendMessage(ChatColor.RED + "World not found!");
            return;
        }
        
        // Get a random location for the duel
        Location centerLocation = getRandomLocation(world);
        
        // Calculate positions 10 blocks apart, facing each other
        Location loc1 = centerLocation.clone().add(5, 0, 0); // 5 blocks to the right
        Location loc2 = centerLocation.clone().add(-5, 0, 0); // 5 blocks to the left
        
        // Set players to face each other
        loc1.setYaw(180); // Player 1 faces west (towards player 2)
        loc2.setYaw(0);   // Player 2 faces east (towards player 1)
        
        inDuel.add(p1.getUniqueId());
        inDuel.add(p2.getUniqueId());
        frozenPlayers.add(p1.getUniqueId());
        frozenPlayers.add(p2.getUniqueId());
        
        p1.teleport(loc1);
        p2.teleport(loc2);
        
        Bukkit.broadcastMessage(ChatColor.GOLD + "1v1 duel started between " + p1.getName() + " and " + p2.getName() + "!");
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            frozenPlayers.remove(p1.getUniqueId());
            frozenPlayers.remove(p2.getUniqueId());
            p1.sendMessage(ChatColor.GREEN + "Fight!");
            p2.sendMessage(ChatColor.GREEN + "Fight!");
        }, 60L); // 3 seconds
    }

    private Location getRandomLocation(World world) {
        var config = plugin.getConfig().getConfigurationSection("teleport");
        int minDistance = config.getInt("min_distance", 1000);
        int maxDistance = config.getInt("max_distance", 5000);
        int minY = config.getInt("min_y", 60);
        int maxY = config.getInt("max_y", 120);
        boolean safeTeleport = config.getBoolean("safe_teleport", true);
        
        Random random = new Random();
        int x = random.nextInt(maxDistance - minDistance) + minDistance;
        int z = random.nextInt(maxDistance - minDistance) + minDistance;
        int y = random.nextInt(maxY - minY) + minY;
        
        Location location = new Location(world, x, y, z);
        
        if (safeTeleport) {
            location = world.getHighestBlockAt(location).getLocation().add(0, 1, 0);
        }
        
        return location;
    }

    public void leaveQueue(Player player) {
        boolean found = false;
        for (Map.Entry<String, Queue<Player>> entry : queues.entrySet()) {
            Queue<Player> queue = entry.getValue();
            if (queue.remove(player)) {
                player.sendMessage(ChatColor.GREEN + "You have left the queue for " + entry.getKey() + " arena!");
                found = true;
            }
        }
        if (!found) {
            player.sendMessage(ChatColor.RED + "You are not in any queue!");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player.getUniqueId())) {
            if (event.getFrom().getX() != event.getTo().getX() ||
                event.getFrom().getY() != event.getTo().getY() ||
                event.getFrom().getZ() != event.getTo().getZ()) {
                event.setTo(event.getFrom());
            }
        }
    }
} 
package com.example.rtpqueue;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class RtpQueuePlusGUI {
    public static void openQueueGUI(Player player) {
        var config = RtpQueuePlusPlugin.getInstance().getConfig();
        String title = config.getString("gui.title", "1v1 Queue");
        int rows = config.getInt("gui.rows", 1);
        int size = rows * 9;
        Inventory gui = Bukkit.createInventory(null, size, title);

        ConfigurationSection itemsSection = config.getConfigurationSection("gui.items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                // Not used, as items is a list, not a section
            }
        } else {
            List<Map<String, Object>> items = (List<Map<String, Object>>) config.getList("gui.items");
            if (items != null) {
                for (Map<String, Object> obj : items) {
                    {
                        Material mat = Material.valueOf(obj.get("material").toString());
                        String name = obj.get("name").toString();
                        int slot = Integer.parseInt(obj.get("slot").toString());
                        ItemStack item = new ItemStack(mat);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(org.bukkit.ChatColor.translateAlternateColorCodes('&', name));
                        item.setItemMeta(meta);
                        gui.setItem(slot, item);
                    }
                }
            }
        }
        player.openInventory(gui);
    }
} 
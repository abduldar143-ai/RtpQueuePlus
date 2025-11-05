package com.example.rtpqueue;

import org.bukkit.plugin.java.JavaPlugin;

public class RtpQueuePlusPlugin extends JavaPlugin {
    private static RtpQueuePlusPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("1v1").setExecutor(new RtpQueuePlusCommand(this));
        getCommand("rtpqueue").setExecutor(new RtpQueuePlusCommand(this));
        getServer().getPluginManager().registerEvents(new RtpQueuePlusListener(this), this);
    }

    private RtpQueuePlusListener listener;

    public static RtpQueuePlusPlugin getInstance() {
        return instance;
    }

    public void setListener(RtpQueuePlusListener listener) {
        this.listener = listener;
    }

    public RtpQueuePlusListener getListener() {
        return listener;
    }
} 
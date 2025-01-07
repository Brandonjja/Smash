package com.brandonjja.smash;

import org.bukkit.plugin.java.JavaPlugin;

public class Smash extends JavaPlugin {

    private static Smash instance;

    @Override
    public void onEnable() {
        instance = this;
        SmashCore.init();
    }

    @Override
    public void onDisable() {
        instance = null;
        SmashCore.shutdown();
    }

    public static Smash getInstance() {
        return instance;
    }
}

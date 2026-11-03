package dev.arteshow.essentials;

import org.bukkit.plugin.java.JavaPlugin;

public final class Essentials extends JavaPlugin {

    @Override 
    public void onEnable() {
        getLogger().info("Essentials enabled");
    }

    @Override 
    public void onDisable() {
        getLogger().info("Essentials disabled");
    }
}
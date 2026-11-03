package dev.arteshow.essentials;

import org.bukkit.plugin.java.JavaPlugin;

import dev.arteshow.essentials.commands.EssentialsCommand;

public final class Essentials extends JavaPlugin {

    @Override 
    public void onEnable() {
        getCommand("es").setExecutor(new EssentialsCommand());

        getLogger().info("Essentials enabled");
    }

    @Override 
    public void onDisable() {
        getLogger().info("Essentials disabled");
    }
}
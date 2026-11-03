package dev.arteshow.essentials;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import dev.arteshow.essentials.commands.EssentialsCommand;
import dev.arteshow.essentials.storage.Database;
import dev.arteshow.essentials.storage.MigrationManager;

public final class Essentials extends JavaPlugin {

    @Override 
    public void onEnable() {
        getCommand("es").setExecutor(new EssentialsCommand());

        try {
            Database db = new Database(getDataFolder());
            db.open();

            MigrationManager migrationManager = new MigrationManager(db.getConnection());
            migrationManager.applyMigrations();
            
        } catch (SQLException e) {
            getLogger().severe("Failed to initialize database: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }

        getLogger().info("Essentials enabled");
    }

    @Override 
    public void onDisable() {
        getLogger().info("Essentials disabled");
    }
}
package dev.arteshow.essentials;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import dev.arteshow.essentials.commands.EssentialsCommand;
import dev.arteshow.essentials.storage.Database;
import dev.arteshow.essentials.storage.MigrationManager;
import dev.arteshow.essentials.storage.repositories.PlayerPositionRepository;
import dev.arteshow.essentials.storage.repositories.PlayerSettingsRepository;

public final class Essentials extends JavaPlugin {

    private PlayerSettingsRepository playerSettingsRepository;
    private PlayerPositionRepository playerPositionRepository;

    private Database database;

    @Override
    public void onEnable() {
        getCommand("es").setExecutor(new EssentialsCommand());

        try {
            database = new Database(getDataFolder());

            database.open();

            MigrationManager migrationManager = new MigrationManager(database.getConnection());
            migrationManager.applyMigrations();

        } catch (SQLException e) {
            getLogger().severe("Failed to initialize database: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }

        playerSettingsRepository
                = new PlayerSettingsRepository(database.getConnection());

        playerPositionRepository
                = new PlayerPositionRepository(database.getConnection());

        getLogger().info("Essentials enabled");
    }

    @Override
    public void onDisable() {
        try {
            if (database.getConnection() != null) {
                database.close();
            }
        } catch (SQLException e) {
            getLogger().severe("Failed to close database: " + e.getMessage());

        }

        getLogger().info("Essentials disabled");
    }
}

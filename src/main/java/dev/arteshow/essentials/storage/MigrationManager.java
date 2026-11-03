package dev.arteshow.essentials.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dev.arteshow.essentials.storage.migrations.V1__InitialSchema;
import dev.arteshow.essentials.storage.migrations.V2__FixPlayerPositions;

public final class MigrationManager {

    private final Connection connection;
    private final List<Migration> migrations = new ArrayList<Migration>();

    public MigrationManager(Connection connection) {
        this.connection = connection;

        registerMigrations();
    }

    private void registerMigrations() {
        migrations.add(new V1__InitialSchema());
        migrations.add(new V2__FixPlayerPositions());
    }

    private boolean isApplied(int version) throws SQLException {
        try (PreparedStatement statement
                = connection.prepareStatement(
                        "SELECT version FROM schema_migrations WHERE version = ?"
                )) {

                    statement.setInt(1, version);

                    try (ResultSet result = statement.executeQuery()) {
                        return result.next();
                    }
                }
    }

    public void createMigrationTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS schema_migrations (\n"
                    + "    version INTEGER PRIMARY KEY,\n"
                    + "    name TEXT NOT NULL\n"
                    + ");");
        }
    }

    private void recordMigration(int version, String name) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO schema_migrations (version, name) VALUES (?, ?)")) {

            statement.setInt(1, version);
            statement.setString(2, name);

            statement.executeUpdate();
        }
    }

    public void applyMigrations() throws SQLException {
        createMigrationTable();

        for (Migration migration : migrations) {
            if (!isApplied(migration.getVersion())) {
                migration.execute(connection);

                recordMigration(migration.getVersion(), migration.getName());
            }
        }
    }
}

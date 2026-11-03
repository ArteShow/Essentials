
package dev.arteshow.essentials.storage.migrations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dev.arteshow.essentials.storage.Migration;

public final class V2__FixPlayerPositions implements Migration {

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public String getName() {
        return "FixPlayerPositions";
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        if (hasColumn(connection, "uuid") && hasColumn(connection, "world")) {
            return;
        }

        if (tableExists(connection, "player_positions_legacy_v1")) {
            throw new SQLException("Legacy player_positions table already exists!");
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                "ALTER TABLE player_positions RENAME TO player_positions_legacy_v1;"
            );

            statement.executeUpdate(
                "CREATE TABLE player_positions (\n"
                + "    uuid TEXT PRIMARY KEY NOT NULL,\n"
                + "    world TEXT NOT NULL,\n"
                + "    x REAL NOT NULL,\n"
                + "    y REAL NOT NULL,\n"
                + "    z REAL NOT NULL,\n"
                + "    yaw REAL NOT NULL,\n"
                + "    pitch REAL NOT NULL\n"
                + ");"
            );
        }
    }

    private boolean hasColumn(Connection connection, String columnName)
            throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("PRAGMA table_info(player_positions)")) {

            while (result.next()) {
                if (columnName.equalsIgnoreCase(result.getString("name"))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean tableExists(Connection connection, String tableName)
            throws SQLException {
        try (var statement = connection.prepareStatement(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?")) {
            statement.setString(1, tableName);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
}

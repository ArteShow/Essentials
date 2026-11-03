package dev.arteshow.essentials.storage.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import dev.arteshow.essentials.storage.Migration;

public final class V1__InitialSchema implements Migration {

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getName() {
        return "InitialSchema";
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_settings (\n"
                    + "    uuid TEXT PRIMARY KEY NOT NULL,\n"
                    + "    restore_position INTEGER NOT NULL DEFAULT 1\n"
                    + "        CHECK (restore_position IN (0, 1))\n"
                    + ");");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_positions (\n"
                    + "    uuid TEXT PRIMARY KEY NOT NULL,\n"
                    + "    world TEXT NOT NULL,\n"
                    + "    x REAL NOT NULL,\n"
                    + "    y REAL NOT NULL,\n"
                    + "    z REAL NOT NULL,\n"
                    + "    yaw REAL NOT NULL,\n"
                    + "    pitch REAL NOT NULL\n"
                    + ");");
        }
    }
}

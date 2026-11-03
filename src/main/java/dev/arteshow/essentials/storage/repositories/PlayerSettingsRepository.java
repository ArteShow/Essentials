
package dev.arteshow.essentials.storage.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class PlayerSettingsRepository {

    private final Connection connection;

    public PlayerSettingsRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean getRestorePosition(UUID uuid) throws SQLException {
        String sql = """
                SELECT restore_position
                FROM player_settings
                WHERE uuid = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("restore_position") == 1;
                }

                // Default value for players without saved settings
                return true;
            }
        }
    }

    public void setRestorePosition(UUID uuid, boolean enabled) throws SQLException {
        String sql = """
                INSERT INTO player_settings (uuid, restore_position)
                VALUES (?, ?)
                ON CONFLICT(uuid) DO UPDATE SET
                    restore_position = excluded.restore_position
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setInt(2, enabled ? 1 : 0);

            statement.executeUpdate();
        }
    }
}

package dev.arteshow.essentials.storage.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import dev.arteshow.essentials.storage.models.PlayerPosition;

public final class PlayerPositionRepository {

    private final Connection connection;

    public PlayerPositionRepository(Connection connection) {
        this.connection = connection;
    }

    public void savePosition(UUID uuid, PlayerPosition position) throws SQLException {
        String sql = """
                INSERT INTO player_positions (uuid, world, x, y, z, yaw, pitch)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(uuid) DO UPDATE SET
                    world = excluded.world,
                    x = excluded.x,
                    y = excluded.y,
                    z = excluded.z,
                    yaw = excluded.yaw,
                    pitch = excluded.pitch
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, position.getWorld());
            statement.setDouble(3, position.getX());
            statement.setDouble(4, position.getY());
            statement.setDouble(5, position.getZ());
            statement.setFloat(6, position.getYaw());
            statement.setFloat(7, position.getPitch());

            statement.executeUpdate();
        }
    }

    public Optional<PlayerPosition> getPosition(UUID uuid) throws SQLException {
        String sql = """
                SELECT world, x, y, z, yaw, pitch
                FROM player_positions
                WHERE uuid = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return Optional.empty();
                }

                String world = result.getString("world");

                if (world == null) {
                    return Optional.empty();
                }

                PlayerPosition position = new PlayerPosition(world,
                        result.getDouble("x"),
                        result.getDouble("y"),
                        result.getDouble("z"),
                        result.getFloat("yaw"),
                        result.getFloat("pitch"));

                return Optional.of(position);
            }
        }
    }

    public void deletePosition(UUID uuid) throws SQLException {
        String sql = "DELETE FROM player_positions WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        }
    }
}

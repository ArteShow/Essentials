package dev.arteshow.essentials.storage.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class PlayerPositionRepository {

    private final Connection connection;

    public PlayerPositionRepository(Connection connection) {
        this.connection = connection;
    }

    public void savePosition(UUID uuid, Location location) throws SQLException {
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
            statement.setString(2, location.getWorld().getName());
            statement.setDouble(3, location.getX());
            statement.setDouble(4, location.getY());
            statement.setDouble(5, location.getZ());
            statement.setFloat(6, location.getYaw());
            statement.setFloat(7, location.getPitch());

            statement.executeUpdate();
        }
    }

    public Optional<Location> getPosition(UUID uuid) throws SQLException {
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

                World world = Bukkit.getWorld(result.getString("world"));

                if (world == null) {
                    return Optional.empty();
                }

                Location location = new Location(
                        world,
                        result.getDouble("x"),
                        result.getDouble("y"),
                        result.getDouble("z"),
                        result.getFloat("yaw"),
                        result.getFloat("pitch")
                );

                return Optional.of(location);
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

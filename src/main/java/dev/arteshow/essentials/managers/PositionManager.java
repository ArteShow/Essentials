package dev.arteshow.essentials.managers;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.arteshow.essentials.storage.models.PlayerPosition;
import dev.arteshow.essentials.storage.repositories.PlayerPositionRepository;
import dev.arteshow.essentials.storage.repositories.PlayerSettingsRepository;

public final class PositionManager implements Listener {

    private final JavaPlugin plugin;
    private final PlayerPositionRepository positionRepository;
    private final PlayerSettingsRepository settingsRepository;

    public PositionManager(
            JavaPlugin plugin,
            PlayerPositionRepository positionRepository,
            PlayerSettingsRepository settingsRepository) {

        this.plugin = plugin;
        this.positionRepository = positionRepository;
        this.settingsRepository = settingsRepository;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();

        PlayerPosition position = new PlayerPosition(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                synchronized (positionRepository) {
                    positionRepository.savePosition(uuid, position);
                }
            } catch (SQLException e) {
                plugin.getLogger().severe(
                        "Could not save position for " + uuid + ": " + e.getMessage()
                );
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                boolean restoreEnabled;
                Optional<PlayerPosition> savedPosition;

                synchronized (positionRepository) {
                    restoreEnabled = settingsRepository.getRestorePosition(uuid);

                    if (!restoreEnabled) {
                        return;
                    }

                    savedPosition = positionRepository.getPosition(uuid);
                }

                if (savedPosition.isEmpty()) {
                    return;
                }

                PlayerPosition position = savedPosition.get();

                Bukkit.getScheduler().runTask(plugin, () -> {
                    Player player = Bukkit.getPlayer(uuid);

                    if (player == null || !player.isOnline()) {
                        return;
                    }

                    World world = Bukkit.getWorld(position.getWorld());

                    if (world == null) {
                        plugin.getLogger().warning(
                                "Cannot restore position for " + uuid
                                + ": world " + position.getWorld() + " is not loaded."
                        );
                        return;
                    }

                    Location location = new Location(
                            world,
                            position.getX(),
                            position.getY(),
                            position.getZ(),
                            position.getYaw(),
                            position.getPitch()
                    );

                    player.teleport(location);
                });

            } catch (SQLException e) {
                plugin.getLogger().severe(
                        "Could not restore position for " + uuid + ": " + e.getMessage()
                );
            }
        });
    }
}

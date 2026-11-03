
package dev.arteshow.essentials.commands.subCommands.settings;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.arteshow.essentials.commands.SubCommand;
import dev.arteshow.essentials.storage.repositories.PlayerPositionRepository;
import dev.arteshow.essentials.storage.repositories.PlayerSettingsRepository;

public final class RestorePositionCommand implements SubCommand {

    private final JavaPlugin plugin;
    private final PlayerSettingsRepository settingsRepository;
    private final PlayerPositionRepository positionRepository;

    public RestorePositionCommand(
            JavaPlugin plugin,
            PlayerSettingsRepository settingsRepository,
            PlayerPositionRepository positionRepository) {

        this.plugin = plugin;
        this.settingsRepository = settingsRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public String getName() {
        return "restorePosition";
    }

    @Override
    public String getDescription() {
        return "Toggles position restoration";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Keep on abusing it...");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /es settings restorePosition <true|false>");
            return true;
        }

        String value = args[0].toLowerCase();

        if (!value.equals("true") && !value.equals("false")) {
            player.sendMessage("Please enter true or false.");
            return true;
        }

        boolean enabled = Boolean.parseBoolean(value);
        UUID uuid = player.getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                synchronized (positionRepository) {
                    settingsRepository.setRestorePosition(uuid, enabled);
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.sendMessage(
                            "Position restoration is now " +
                            (enabled ? "enabled." : "disabled.")
                    );
                });

            } catch (SQLException e) {
                plugin.getLogger().severe(
                        "Could not update position setting: " + e.getMessage()
                );

                Bukkit.getScheduler().runTask(plugin, () ->
                        player.sendMessage("Could not update your settings.")
                );
            }
        });

        return true;
    }
}

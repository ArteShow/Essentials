
package dev.arteshow.essentials.commands.subCommands.settings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import dev.arteshow.essentials.commands.SubCommand;
import dev.arteshow.essentials.storage.repositories.PlayerPositionRepository;
import dev.arteshow.essentials.storage.repositories.PlayerSettingsRepository;

public final class SettingsCommand implements SubCommand {

    private final Map<String, SubCommand> commands = new HashMap<>();

    public SettingsCommand(
            JavaPlugin plugin,
            PlayerSettingsRepository settingsRepository,
            PlayerPositionRepository positionRepository) {

        registerCommand(new RestorePositionCommand(
                plugin,
                settingsRepository,
                positionRepository
        ));
    }

    private void registerCommand(SubCommand command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public Map<String, SubCommand> getCommands() {
        return commands;
    }

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String getDescription() {
        return "Manage player settings";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eSettings: /es settings restorePosition <true|false>");
            return true;
        }

        SubCommand subCommand = commands.get(args[0].toLowerCase());

        if (subCommand == null) {
            sender.sendMessage("§cUnknown setting. Use /es settings.");
            return true;
        }

        return subCommand.execute(
                sender,
                Arrays.copyOfRange(args, 1, args.length)
        );
    }
}

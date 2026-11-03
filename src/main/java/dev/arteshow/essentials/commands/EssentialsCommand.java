package dev.arteshow.essentials.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import dev.arteshow.essentials.commands.subCommands.HelpCommand;
import dev.arteshow.essentials.commands.subCommands.settings.SettingsCommand;
import dev.arteshow.essentials.storage.repositories.PlayerPositionRepository;
import dev.arteshow.essentials.storage.repositories.PlayerSettingsRepository;

public final class EssentialsCommand implements CommandExecutor {

    private final HashMap<String, SubCommand> commands = new HashMap<>();

    public EssentialsCommand(
            PlayerSettingsRepository playerSettingsRepository,
            PlayerPositionRepository playerPositionRepository,
            JavaPlugin plugin
    ) {
        registerCommand(new HelpCommand());
        registerCommand(new HubCommand());

        registerCommand(new SettingsCommand(
                plugin,
                playerSettingsRepository,
                playerPositionRepository
        ));
    }

    private void registerCommand(SubCommand command) {
        commands.put(command.getName(), command);
    }

    public HashMap<String, SubCommand> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return commands.get("help").execute(sender, args);
        }

        SubCommand subcommand = commands.get(args[0].toLowerCase());
        if (subcommand == null) {
            sender.sendMessage("§cUnknown subcommand! Use /es help.");
            return false;
        }

        return subcommand.execute(
                sender,
                Arrays.copyOfRange(args, 1, args.length)
        );
    }
}

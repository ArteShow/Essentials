package dev.arteshow.essentials.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev.arteshow.essentials.commands.subCommands.HelpCommand;

public final class EssentialsCommand implements CommandExecutor{
    private final HashMap<String, SubCommand> commands = new HashMap<>();

    public EssentialsCommand() {
        registerCommand(new HelpCommand());
    }

    private void registerCommand(SubCommand command) {
        commands.put(command.getName(), command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return commands.get("help").execute(sender, args);
        }

        SubCommand subcommand = commands.get(args[0].toLowerCase());
        if (subcommand == null) {
            sender.sendMessage("§cUnknown subcommand! Use /tribbox help.");
            return false;
        }

        return subcommand.execute(sender, args);
    }
}

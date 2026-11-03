package dev.arteshow.essentials.commands.subCommands;

import org.bukkit.command.CommandSender;

import dev.arteshow.essentials.commands.SubCommand;

public class HelpCommand implements SubCommand{
    @Override 
    public String getName() {
        return "help";
    }

    @Override 
    public String getDescription() {
        return "Shows all available commands";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("------ Essentials Help ------");
        sender.sendMessage("/es help - Shows all available commands");
        sender.sendMessage("/es hub - Teleports you to hub");
        sender.sendMessage("/es restorePosition - Toggles position restoration");

        return true;
    }
}

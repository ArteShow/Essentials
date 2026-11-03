package dev.arteshow.essentials.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    String getName();
    String getDescription();

    boolean execute(CommandSender sender, String[] args);
}


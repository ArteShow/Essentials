
package dev.arteshow.essentials.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import dev.arteshow.essentials.commands.subCommands.settings.SettingsCommand;

public final class EssentialTabCompleter implements TabCompleter {

    private final Map<String, SubCommand> commands;

    public EssentialTabCompleter(Map<String, SubCommand> commands) {
        this.commands = commands;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            for (String name : commands.keySet()) {
                if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(name);
                }
            }
        } else if (args.length == 2
                && args[0].equalsIgnoreCase("settings")) {

            SubCommand settings = commands.get("settings");

            if (settings instanceof SettingsCommand settingsCommand) {
                for (String name : settingsCommand.getCommands().keySet()) {
                    if (name.startsWith(args[1].toLowerCase())) {
                        suggestions.add(name);
                    }
                }
            }

        } else if (args.length == 3
                && args[0].equalsIgnoreCase("settings")
                && args[1].equalsIgnoreCase("restorePosition")) {

            for (String value : List.of("true", "false")) {
                if (value.startsWith(args[2].toLowerCase())) {
                    suggestions.add(value);
                }
            }
        }

        return suggestions;
    }
}

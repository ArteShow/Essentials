package dev.arteshow.essentials.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mvplugins.multiverse.core.MultiverseCoreApi;

public class HubCommand implements SubCommand {

    public String getName() {
        return "hub";
    }

    public String getDescription() {
        return "Teleports player to the hub";
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            MultiverseCoreApi core = MultiverseCoreApi.get();

            var hub = core.getWorldManager()
                    .getLoadedWorld("main")
                    .getOrNull();

            if (hub == null) {
                sender.sendMessage("§cHub world is not loaded!");
                return false;
            }

            boolean success = player.teleport(hub.getSpawnLocation());

            if (success) {
                player.sendMessage("§aTeleported to the hub!");
            } else {
                player.sendMessage("§cTeleportation failed!");
            }
            
        } else {
            sender.sendMessage("Alr bro, stop the abuse");
            return false;
        }

        return true;
    }
}

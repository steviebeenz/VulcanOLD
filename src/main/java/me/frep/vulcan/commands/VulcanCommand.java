package me.frep.vulcan.commands;

import me.frep.vulcan.utilities.UtilColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VulcanCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(UtilColor.red + "You can't execute this command from console!");
            return true;
        }
        return true;
    }
}

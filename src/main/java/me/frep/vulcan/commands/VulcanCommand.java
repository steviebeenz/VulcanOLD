package me.frep.vulcan.commands;

import me.frep.vulcan.utilities.UtilColor;
import me.frep.vulcan.utilities.UtilGUI;
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
        Player p = (Player)sender;
        if (!p.hasPermission("vulcan.admin")) {
            sender.sendMessage(UtilColor.red + "You don't have permission to execute this command!");
            return true;
        }
        if (args.length == 1 && args[0].equals("gui") || args.length == 1 && args[0].equals("menu")) {
            UtilGUI.getInstance().openVulcanMain(p);
        }
        return true;
    }
}

package me.frep.vulcan.commands;

import me.frep.vulcan.Vulcan;
import me.frep.vulcan.utilities.UtilColor;
import me.frep.vulcan.utilities.UtilConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(UtilColor.red + "You can't execute this command from console!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("vulcan.alerts")) {
            p.sendMessage(UtilColor.red + "You don't have permission to run this command.");
            return true;
        }
        if (Vulcan.alertsEnabled.contains(p)) {
            Vulcan.alertsEnabled.remove(p);
            p.sendMessage(UtilColor.translate(UtilConfig.getInstance().getConfig().getString("alerts.toggled-off-message")
                    .replaceAll("%prefix%", UtilConfig.getInstance().getConfig().getString("prefix"))));
            return true;
        } else {
            if (!Vulcan.alertsEnabled.contains(p)) {
                Vulcan.alertsEnabled.add(p);
                p.sendMessage(UtilColor.translate(UtilConfig.getInstance().getConfig().getString("alerts.toggled-on-message")
                        .replaceAll("%prefix%", UtilConfig.getInstance().getConfig().getString("prefix"))));
                return true;
            }
        }
        return true;
    }
}
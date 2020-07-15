package me.frep.vulcan.utilities;

import org.bukkit.ChatColor;

public class UtilColor {

    public static String red = ChatColor.RED.toString();

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

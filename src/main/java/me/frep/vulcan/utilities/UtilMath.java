package me.frep.vulcan.utilities;

import org.bukkit.Location;

import java.text.DecimalFormat;

public class UtilMath {

    public static double trim(int degree, double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = format + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.parseDouble(twoDForm.format(d).replaceAll(",", "."));
    }

    public static double getHorizontalDistance(Location one, Location two) {
        final double x = Math.abs(Math.abs(one.getX()) - Math.abs(two.getX()));
        final double z = Math.abs(Math.abs(one.getZ()) - Math.abs(two.getZ()));
        return Math.sqrt(x * x + z * z);
    }
}
package me.frep.vulcan.utilities;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.List;

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

    public static boolean isScientificNotation(Float f) {
        return (f.toString().contains("E"));
    }

    public static boolean isScientificNotation(Double d) {
        return (d.toString().contains("E"));
    }

    public static double getStandardDeviationLong(List<Long> longList) {
        double sum = 0.0, deviation = 0.0;
        int length = longList.size();
        for (float num : longList)
            sum += num;
        double mean = sum / length;
        for (double num : longList)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    public static double getStandardDeviationFloat(List<Float> floatList) {
        double sum = 0.0, deviation = 0.0;
        int length = floatList.size();
        for (float num : floatList)
            sum += num;
        double mean = sum / length;
        for (double num : floatList)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }
}
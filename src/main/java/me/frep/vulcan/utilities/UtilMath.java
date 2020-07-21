package me.frep.vulcan.utilities;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    public static double getStandardDeviationDouble(List<Double> doubleList) {
        double sum = 0.0, deviation = 0.0;
        int length = doubleList.size();
        for (double num : doubleList)
            sum += num;
        double mean = sum / length;
        for (double num : doubleList)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    public static double getStandardDeviationInteger(List<Integer> integerList) {
        double sum = 0.0, deviation = 0.0;
        int length = integerList.size();
        for (double num : integerList)
            sum += num;
        double mean = sum / length;
        for (double num : integerList)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    //credits for these utilities @elevated
    public static double getSkewness(final Collection<? extends Number> data) {
        double sum = 0;
        int count = 0;
        final List<Double> numbers = Lists.newArrayList();
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;

            numbers.add(number.doubleValue());
        }
        Collections.sort(numbers);
        final double mean = sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2;
        final double variance = getVariance(data);
        return 3 * (mean - median) / variance;
    }

    public static double getVariance(final Collection<? extends Number> data) {
        int count = 0;
        double sum = 0.0;
        double variance = 0.0;
        double average;
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;
        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }
        return variance;
    }

    public static double getKurtosis(final Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;
        for (Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        if (count < 3.0) {
            return 0.0;
        }
        final double efficiencyFirst = count * (count + 1.0) / ((count - 1.0) * (count - 2.0) * (count - 3.0));
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / ((count - 2.0) * (count - 3.0));
        final double average = sum / count;
        double variance = 0.0;
        double varianceSquared = 0.0;

        for (final Number number : data) {
            variance += Math.pow(average - number.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number.doubleValue(), 4.0);
        }
        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }

    public static long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }

    public static boolean isNumeric(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }
}
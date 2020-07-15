package me.frep.vulcan.utilities;

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
}

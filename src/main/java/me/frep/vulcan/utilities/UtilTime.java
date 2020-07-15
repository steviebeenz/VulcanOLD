package me.frep.vulcan.utilities;

public class UtilTime {

    public static long timeNow() {
        return System.currentTimeMillis();
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static long elapsed(long startTime) {
        return System.currentTimeMillis() - startTime;
    }
}

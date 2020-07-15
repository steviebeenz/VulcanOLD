package me.frep.vulcan.utilities;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.entity.Player;

public class UtilLag {

    public static double getTPS() {
        return PacketEvents.getAPI().getServerUtils().getCurrentTPS();
    }

    public static double[] getRecentTPS() {
        return PacketEvents.getAPI().getServerUtils().getRecentTPS();
    }

    public static Integer getPing(Player p) {
        return PacketEvents.getAPI().getPlayerUtils().getPing(p);
    }
}
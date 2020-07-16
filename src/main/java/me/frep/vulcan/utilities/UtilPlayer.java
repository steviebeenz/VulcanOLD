package me.frep.vulcan.utilities;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ClientVersion;
import org.bukkit.entity.Player;

public class UtilPlayer {

    public static ClientVersion getClientVersion(Player p) {
        return PacketEvents.getAPI().getPlayerUtils().getClientVersion(p);
    }
}
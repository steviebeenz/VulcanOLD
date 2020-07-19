package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsE extends Check {

    public BadPacketsE() {
        super("BadPacketsE", "Bad Packets (Type E)", CheckType.PLAYER, true, true, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.WINDOW_CLICK) {
            if (data.isSprinting) data.badPacketsEVerbose++;
            if (data.badPacketsEVerbose > 1) {
                flag(p, null);
                data.badPacketsEVerbose = 0;
            }
        }
        if (e.getPacketId() == PacketType.Client.FLYING) {
            if (data.badPacketsEVerbose > 0) data.badPacketsEVerbose = 0;
        }
    }
}
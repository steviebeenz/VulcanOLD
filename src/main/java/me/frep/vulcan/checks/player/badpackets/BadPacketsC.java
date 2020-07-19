package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsC extends Check {

    public BadPacketsC() {
        super("BadPacketsC", "Bad Packets (Type C)", CheckType.PLAYER, true, true, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            if (data.isSneaking && data.isSprinting && !data.isNearWeb(2)
                    || !data.isSneaking && p.isSneaking() && data.isSprinting && !data.isNearWeb(2)) data.badPacketsCStreak++;
            else {
                if (data.badPacketsCStreak > 0) data.badPacketsCStreak = 0;
            }
            if (data.badPacketsCStreak > 3) {
                flag(p, null);
                data.badPacketsCStreak = 0;
            }
        }
    }
}

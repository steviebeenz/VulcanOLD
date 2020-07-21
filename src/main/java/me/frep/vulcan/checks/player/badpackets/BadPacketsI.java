package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsI extends Check {

    public BadPacketsI() {
        super("BadPacketsI", "Bad Packets (Type I)", CheckType.PLAYER, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            if (data.isBelowBlock || data.isNearClimbable(2) || !elapsed(data.lastJoin, 4000) || !elapsed(data.lastTeleport, 4000)) return;
            double y = p.getLocation().getY();
            double deltaY = Math.abs(data.badPacketsILastY - y);
            if (deltaY == 0) return;
            if (deltaY == data.badPacketsILastDeltaY) data.badPacketsIVerbose++;
            else {
                if (data.badPacketsIVerbose > 0) data.badPacketsIVerbose--;
            }
            if (data.badPacketsIVerbose > 1) {
                flag(p, null);
                data.badPacketsIVerbose = 0;
            }
            data.badPacketsILastDeltaY = deltaY;
            data.badPacketsILastY = y;
        }
    }
}

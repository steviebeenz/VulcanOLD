package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsH extends Check {

    public BadPacketsH() {
        super("BadPacketsH", "Bad Packets (Type H)", CheckType.PLAYER, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            if (data.isOnGround && p.isOnGround() || !elapsed(data.lastJoin, 4000) || !elapsed(data.lastTeleport, 4000)) return;
            if (p.getLocation().getY() % 1 == 0 && data.airTicks > data.badPacketsHLastAirTicks
                && packet.getY() == data.badPacketsHLastY && data.airTicks > 30 && data.badPacketsHLastAirTicks > 30) data.badPacketsHVerbose++;
            else {
                if (data.badPacketsHVerbose > 0) data.badPacketsHVerbose--;
            }
            if (data.badPacketsHVerbose > 8) {
                data.badPacketsHVerbose = 0;
                flag(p, null);
            }
            data.badPacketsHLastAirTicks = data.airTicks;
            data.badPacketsHLastY = packet.getY();
        }
    }
}

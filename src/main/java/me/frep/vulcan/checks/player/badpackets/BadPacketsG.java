package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsG extends Check {

    public BadPacketsG() {
        super("BadPacketsG", "Bad Packets (Type G)", CheckType.PLAYER, true, true, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.LOOK) {
            WrappedPacketInFlying.WrappedPacketInLook packet = new WrappedPacketInFlying.WrappedPacketInLook(e.getNMSPacket());
            float pitch = packet.getPitch();
            float yaw = packet.getYaw();
            if (pitch == data.badPacketsGLastPitch && yaw == data.badPacketsGLastYaw) data.badPacketsGVerbose++;
            else {
                if (data.badPacketsGVerbose > 0) data.badPacketsGVerbose--;
            }
            if (data.badPacketsGVerbose > 3) {
                data.badPacketsGVerbose = 0;
                flag(p, null);
            }
            data.badPacketsGLastYaw = yaw;
            data.badPacketsGLastPitch = pitch;
        }
    }
}

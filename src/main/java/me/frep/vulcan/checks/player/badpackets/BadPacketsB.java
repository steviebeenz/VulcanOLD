package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsB extends Check {

    public BadPacketsB() {
        super("BadPacketsB", "Bad Packets (Type B)", CheckType.PLAYER, true, true, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying.WrappedPacketInLook lookPacket = new WrappedPacketInFlying.WrappedPacketInLook(e.getNMSPacket());
            WrappedPacketInFlying.WrappedPacketInPosition_Look posLookPacket = new WrappedPacketInFlying.WrappedPacketInPosition_Look(e.getNMSPacket());
            if (lookPacket.getPitch() > 90.01 || posLookPacket.getPitch() > 90.01) {
                flag(p, null);
            }
        }
    }
}
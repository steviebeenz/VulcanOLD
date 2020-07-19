package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsD extends Check {

    public BadPacketsD() {
        super("BadPacketsD", "Bad Packets (Type D)", CheckType.PLAYER, true, true, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            data.badPacketsDHasSwung = true;
        }
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (!data.badPacketsDHasSwung) {
                data.badPacketsDStreak++;
            } else {
                if (data.badPacketsDStreak > 0) data.badPacketsDStreak--;
            }
            if (data.badPacketsDStreak > 2) {
                flag(p, null);
                data.badPacketsDStreak = 0;
            }
        }
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            data.badPacketsDHasSwung = false;
        }
    }
}
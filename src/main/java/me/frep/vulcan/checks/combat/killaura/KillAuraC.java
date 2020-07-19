package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraC extends Check {

    public KillAuraC() {
        super("KillAuraC", "Kill Aura (Type C)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.LOOK) {
            WrappedPacketInFlying.WrappedPacketInLook packet = new WrappedPacketInFlying.WrappedPacketInLook(e.getNMSPacket());
            if (p.getVehicle() != null || elapsed(data.lastAttackPacket, 2000)) return;
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            if (yaw == data.killAuraCLastYaw && pitch == data.killAuraCLastPitch
                    && yaw != 0 && pitch != 0 && data.killAuraCLastYaw != 0 && data.killAuraCLastPitch != 0) data.killAuraCVerbose++;
            if (data.killAuraCVerbose > 1) { flag(p, null); data.killAuraCVerbose = 0; }
            data.killAuraCLastYaw = yaw;
            data.killAuraCLastPitch = pitch;
        }
    }
}
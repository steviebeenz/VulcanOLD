package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilMath;
import org.bukkit.entity.Player;

public class KillAuraP extends Check {

    public KillAuraP() {
        super("KillAuraP", "Kill Aura (Type P)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            float deltaYaw = Math.abs(yaw - data.killAuraPLastYaw);
            float deltaPitch = Math.abs(pitch - data.killAuraPLastPitch);
            double speed = data.lastGroundSpeed;
            double accel = Math.abs(speed - data.killAuraPLastSpeed);
            if (yaw == 0 || pitch == 0 || p.getNearbyEntities(1.5, 1.5, 1.5).isEmpty() || elapsed(data.lastAttackPacket, 500)) return;
            if (deltaYaw > 10 && deltaPitch > 10) {
                if (UtilMath.isScientificNotation(accel)) flag(p, null);
            }
            data.killAuraPLastSpeed = speed;
            data.killAuraPLastYaw = yaw;
            data.killAuraPLastPitch = pitch;
        }
    }
}

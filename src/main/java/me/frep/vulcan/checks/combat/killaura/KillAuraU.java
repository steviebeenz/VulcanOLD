package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class KillAuraU extends Check {

    public KillAuraU() {
        super("KillAuraU", "Kill Aura (Type U)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            if (yaw == 0 || pitch == 0 || elapsed(data.lastAttackPacket, 100)) return;
            float deltaYaw = Math.abs(yaw - data.killAuraULastYaw);
            float deltaPitch = Math.abs(pitch - data.killAuraULastPitch);
            if (deltaYaw > 0) data.killAuraUDeltaYaws.add(deltaYaw);
            if (deltaPitch < 13) data.killAuraUDeltaPitches.add(deltaPitch);
            if (data.killAuraUDeltaPitches.size() > 25 || data.killAuraUDeltaPitches.size() > 25) {
                double pitchDeviation = UtilMath.getStandardDeviationFloat(data.killAuraUDeltaPitches);
                double yawDeviation = UtilMath.getStandardDeviationFloat(data.killAuraUDeltaYaws);
                boolean containsZero = false;
                if (data.killAuraUDeltaPitches.contains(0F)) containsZero = true;
                if (yawDeviation > 3 && yawDeviation < 4.5 && pitchDeviation > 3 && pitchDeviation < 4.5 && !containsZero) data.killAuraUVerbose++;
                else {
                    if (data.killAuraUVerbose > 0) data.killAuraUVerbose--;
                }
                if (data.killAuraUVerbose > 1) {
                    flag(p, null);
                    data.killAuraUVerbose = 0;
                }
                data.killAuraUDeltaPitches.clear();
                data.killAuraUDeltaYaws.clear();
            }
            data.killAuraULastYaw = yaw;
            data.killAuraULastPitch = pitch;
        }
    }
}

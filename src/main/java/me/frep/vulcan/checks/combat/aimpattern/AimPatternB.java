package me.frep.vulcan.checks.combat.aimpattern;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class AimPatternB extends Check {

    public AimPatternB() {
        super("AimPatternB", "Aim Pattern (Type B)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            if (yaw == 0 || pitch == 0) return;
            float deltaPitch = Math.abs(pitch - data.aimPatternBLastPitch);
            float deltaYaw = Math.abs(yaw - data.aimPatternBLastYaw);
            if (deltaYaw > 1.1 && deltaPitch == 0 && pitch < 89) data.aimPatternBVerbose++;
            else {
                if (data.aimPatternBVerbose > 0) data.aimPatternBVerbose -= 3;
            }
            if (data.aimPatternBVerbose > 12) {
                flag(p, null);
                data.aimPatternBVerbose = 0;
            }
            data.aimPatternBLastDeltaYaw = deltaYaw;
            data.aimPatternBLastDeltaPitch = deltaPitch;
            data.aimPatternBLastYaw = yaw;
            data.aimPatternBLastPitch = pitch;
        }
    }
}
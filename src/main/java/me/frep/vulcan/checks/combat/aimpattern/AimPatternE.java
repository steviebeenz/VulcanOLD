package me.frep.vulcan.checks.combat.aimpattern;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilMath;
import org.bukkit.entity.Player;

public class AimPatternE extends Check {

    public AimPatternE() {
        super("AimPatternE", "Aim Pattern (Type E)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            if (yaw == 0 || pitch == 0 || p.isOp()) return;
            float deltaPitch = Math.abs(pitch - data.aimPatternELastPitch);
            float deltaYaw = Math.abs(yaw - data.aimPatternELastYaw);
            float deltaDeltaYaw = Math.abs(deltaYaw - data.aimPatternELastDeltaYaw);
            float deltaDeltaPitch = Math.abs(deltaPitch - data.aimPatternELastDeltaPitch);
            float deltaDeltaDeltaPitch = Math.abs(deltaDeltaPitch - data.aimPatternELastDeltaPitch);
            float deltaDeltaDeltaYaw = Math.abs(deltaDeltaYaw - data.aimPatternELastDeltaDeltaYaw);
            if (UtilMath.isScientificNotation(deltaDeltaDeltaYaw) && deltaDeltaDeltaPitch < .1) data.aimPatternEVerbose++;
            else {
                if (data.aimPatternEVerbose > 0) data.aimPatternEVerbose -= 2;
            }
            if (data.aimPatternEVerbose > 8) {
                flag(p, null);
                data.aimPatternEVerbose = 0;
            }
            data.aimPatternELastDeltaDeltaPitch = deltaDeltaPitch;
            data.aimPatternELastDeltaDeltaYaw = deltaDeltaYaw;
            data.aimPatternELastDeltaYaw = deltaYaw;
            data.aimPatternELastDeltaPitch = deltaPitch;
            data.aimPatternELastYaw = yaw;
            data.aimPatternELastPitch = pitch;
        }
    }
}

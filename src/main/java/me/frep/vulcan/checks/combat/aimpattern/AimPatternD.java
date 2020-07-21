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

public class AimPatternD extends Check {

    public AimPatternD() {
        super("AimPatternD", "Aim Pattern (Type D)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            if (yaw == 0 || pitch == 0 || p.getName().equals("frep")) return;
            float deltaPitch = Math.abs(pitch - data.aimPatternDLastPitch);
            float deltaYaw = Math.abs(yaw - data.aimPatternDLastYaw);
            if (deltaYaw < .1 && UtilMath.isScientificNotation(deltaPitch)) data.aimPatternDVerbose++;
            else {
                if (data.aimPatternDVerbose > 0) data.aimPatternDVerbose--;
            }
            if (data.aimPatternDVerbose > 6) {
                flag(p, null);
                data.aimPatternDVerbose = 0;
            }
            data.aimPatternDLastDeltaYaw = deltaYaw;
            data.aimPatternDLastDeltaPitch = deltaPitch;
            data.aimPatternDLastYaw = yaw;
            data.aimPatternDLastPitch = pitch;
        }
    }
}

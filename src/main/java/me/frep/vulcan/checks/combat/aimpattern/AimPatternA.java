package me.frep.vulcan.checks.combat.aimpattern;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class AimPatternA extends Check {

    public AimPatternA() {
        super("AimPatternA", "Aim Pattern (Type A)", CheckType.COMBAT, true, false, 2);
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
            float deltaPitch = Math.abs(pitch - data.aimPatternALastPitch);
            float deltaYaw = Math.abs(yaw - data.aimPatternALastYaw);
            if (deltaYaw == data.aimPatternALastDeltaYaw && pitch < 90 && deltaYaw > .03 && deltaYaw != 0 && data.aimPatternALastDeltaYaw != 0) data.aimPatternAVerbose++;
            else {
                if (data.aimPatternAVerbose > 0) data.aimPatternAVerbose--;
            }
            if (data.aimPatternAVerbose > 5) {
                flag(p, null);
                data.aimPatternAVerbose = 0;
            }
            data.aimPatternALastDeltaYaw = deltaYaw;
            data.aimPatternALastDeltaPitch = deltaPitch;
            data.aimPatternALastYaw = yaw;
            data.aimPatternALastPitch = pitch;
        }
    }
}

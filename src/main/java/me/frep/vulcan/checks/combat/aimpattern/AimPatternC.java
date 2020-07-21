package me.frep.vulcan.checks.combat.aimpattern;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class AimPatternC extends Check {

    public AimPatternC() {
        super("AimPatternC", "Aim Pattern (Type C)", CheckType.COMBAT, true, false, 2);
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
            float deltaPitch = Math.abs(pitch - data.aimPatternCLastPitch);
            float deltaYaw = Math.abs(yaw - data.aimPatternCLastYaw);
            if (deltaYaw % .5 == 0 && deltaYaw != 0) data.aimPatternCVerbose++;
            else {
                if (data.aimPatternCVerbose > 0) data.aimPatternCVerbose--;
            }
            if (data.aimPatternCVerbose > 4) {
                flag(p, null);
                data.aimPatternCVerbose = 0;
            }
            data.aimPatternCLastDeltaYaw = deltaYaw;
            data.aimPatternCLastDeltaPitch = deltaPitch;
            data.aimPatternCLastYaw = yaw;
            data.aimPatternCLastPitch = pitch;
        }
    }
}
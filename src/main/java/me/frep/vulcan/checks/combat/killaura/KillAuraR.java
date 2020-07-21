package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilMath;
import org.bukkit.entity.Player;

public class KillAuraR extends Check {

    public KillAuraR() {
        super("KillAuraR", "Kill Aura (Type R)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            float pitch = p.getLocation().getPitch();
            float yaw = p.getLocation().getYaw();
            double deltaPitch = Math.abs(pitch - data.killAuraRLastPitch);
            double deltaYaw = Math.abs(yaw - data.killAuraRLastYaw);
            double accel = Math.abs(data.lastGroundSpeed - data.killAuraRLastSpeed);
            if (UtilMath.isScientificNotation(accel)) data.killAuraRVerbose++;
            if (accel == 0 || accel > .001 || deltaYaw == 0 || deltaPitch <= 1) data.killAuraRVerbose = 0;
            if (data.killAuraRVerbose > 3) {
                flag(p, null);
                data.killAuraRVerbose = 0;
            }
            data.killAuraRLastYaw = yaw;
            data.killAuraRLastPitch = pitch;
            data.killAuraRLastSpeed = data.lastGroundSpeed;
        }
        if (e.getPacketId() == PacketType.Client.FLYING) {
            data.killAuraRLastSpeed = 0;
            data.killAuraAVerbose = 0;
        }
    }
}

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

public class KillAuraM extends Check {

    public KillAuraM() {
        super("KillAuraM", "Kill Aura (Type M)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            float pitch = packet.getPitch();
            float yaw = packet.getYaw();
            if (yaw == 0 || pitch == 0 || elapsed(data.lastAttackPacket, 500)) return;
            double deltaPitch = Math.abs(pitch - data.killAuraMLastPitch);
            double deltaYaw = Math.abs(yaw - data.killAuraMLastYaw);
            if (deltaYaw > 1 && UtilMath.isScientificNotation(deltaPitch)) data.killAuraMVerbose++;
            else {
                if (data.killAuraMVerbose > 0) data.killAuraMVerbose--;
            }
            if (data.killAuraMVerbose > 3) {
                flag(p, null);
                data.killAuraMVerbose = 0;
            }
            data.killAuraMLastPitch = pitch;
            data.killAuraMLastYaw = yaw;
        }
    }
}
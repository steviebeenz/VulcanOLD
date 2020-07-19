package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraE extends Check {

    public KillAuraE() {
        super("KillAuraE", "Kill Aura (Type E)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            float yaw = packet.getYaw();
            float pitch = packet.getPitch();
            if (yaw == 0 || pitch == 0 || elapsed(data.lastAttackPacket, 1000)) return;
            if (yaw == data.killAuraELastYaw && pitch != data.killAuraELastPitch) data.killAuraEVerbose++;
            else { data.killAuraEVerbose = 0; }
            if (data.killAuraEVerbose > 8) {
                flag(p, null);
                data.killAuraEVerbose = 0;
            }
            data.killAuraELastYaw = yaw;
            data.killAuraELastPitch = pitch;
        }
    }
}
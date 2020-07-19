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
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

public class KillAuraS extends Check {

    public KillAuraS() {
        super("KillAuraS", "Kill Aura (Type S)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            long delay = UtilTime.timeNow() - data.killAuraSLastAttack;
            data.killAuraSDelays.add(delay);
            if (data.killAuraSDelays.size() > 10) {
                Collections.sort(data.killAuraSDelays);
                long range = data.killAuraSDelays.get(data.killAuraSDelays.size() - 1) - data.killAuraSDelays.get(0);
                data.killAuraSRanges.add(range);
                data.killAuraSDelays.clear();
            }
            if (data.killAuraSRanges.size() >= 3) {
                double deviation = UtilMath.getStandardDeviationLong(data.killAuraSRanges);
                if (deviation < 1.5 || UtilMath.isScientificNotation(deviation)) flag(p, "dev=" + deviation);
                data.killAuraSRanges.clear();
            }
            data.killAuraSLastAttack = UtilTime.timeNow();
        }
        if (e.getPacketId() == PacketType.Client.FLYING) {
            data.killAuraSRanges.clear();
            data.killAuraSDelays.clear();
        }
    }
}

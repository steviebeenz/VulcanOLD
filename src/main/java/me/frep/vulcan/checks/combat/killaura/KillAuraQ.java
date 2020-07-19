package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilCheck;
import me.frep.vulcan.utilities.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class KillAuraQ extends Check {

    public KillAuraQ() {
        super("KillAuraQ", "Kill Aura (Type Q)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            Entity entity = packet.getEntity();
            double range = UtilCheck.getReach(p, entity);
            Vector vec = entity.getLocation().clone().toVector().setY(0.0).subtract(p.getEyeLocation().clone().toVector().setY(0.0));
            float angle = p.getEyeLocation().getDirection().angle(vec);
            data.killAuraQAngles.add(angle);
            data.killAuraQRange.add(range);
            double deviation = UtilMath.getStandardDeviationFloat(data.killAuraQAngles);
            if (data.killAuraQAngles.size() > 20) {
                double sum = 0;
                for (double reach : data.killAuraQRange) sum += reach;
                double averageRange = sum / data.killAuraQRange.size();
                if (deviation < .001 && averageRange > 3.3) flag(p, "deviation=" + deviation + "avgRange=" + averageRange);
                data.killAuraQAngles.clear();
                data.killAuraQRange.clear();
            }
        }
    }
}
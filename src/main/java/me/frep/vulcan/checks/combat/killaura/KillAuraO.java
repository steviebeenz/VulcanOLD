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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class KillAuraO extends Check {

    public KillAuraO() {
        super("KillAuraO", "Kill Aura (Type O)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            double offset = Math.abs(UtilCheck.getOffset(p.getLocation(), p.getEyeHeight(), (LivingEntity)packet.getEntity()));
            double maxOffset = 200;
            maxOffset += data.lastPing / .5;
            if (offset > maxOffset) data.killAuraOVerbose++;
            else {
                if (data.killAuraOVerbose > 0) data.killAuraOVerbose--;
            }
            if (data.killAuraOVerbose > 4) {
                data.killAuraOVerbose = 0;
                flag(p, offset + " > " + maxOffset);
            }
        }
    }
}

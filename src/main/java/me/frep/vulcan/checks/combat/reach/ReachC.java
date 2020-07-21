package me.frep.vulcan.checks.combat.reach;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilCheck;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.entity.Player;

public class ReachC extends Check {

    public ReachC() {
        super("ReachC", "Reach (Type C)", CheckType.COMBAT, true, true, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK) || !(packet.getEntity() instanceof Player)) return;
            Player damager = e.getPlayer();
            Player victim = (Player) packet.getEntity();
            PlayerData dataDamager = getDataManager().getPlayerData(damager);
            PlayerData dataVictim = getDataManager().getPlayerData(victim);
            if (dataVictim == null) return;
            double reach = UtilCheck.getReach(damager, victim);
            double yDiff = UtilCheck.getYDiff(damager, victim);
            double yawDiff = UtilCheck.getYawDiff(damager, victim);
            double maxReach = 3.0;
            if (yawDiff > 100) maxReach += yawDiff * .01;
            if (dataDamager.isSprinting || dataDamager.hasSpeed()) maxReach += .12;
            maxReach += dataDamager.lastGroundSpeed * .2;
            maxReach += yDiff * .42;
            maxReach += ((dataDamager.lastPing + dataVictim.lastPing) / 2) * .001;
            if (reach > maxReach) {
                dataDamager.reachCVerbose++;
                dataDamager.reachCLastVerbose = UtilTime.timeNow();
            } else {
                if (dataDamager.reachCLastVerbose > 0) dataDamager.reachCVerbose--;
            }
            if (dataDamager.reachCVerbose > 4) {
                flag(damager, reach + " > " + maxReach);
                dataDamager.reachCVerbose = 0;
                dataDamager.reachCLastVerbose = UtilTime.timeNow();
            }
            if (elapsed(dataDamager.reachCLastVerbose, 10000)) {
                dataDamager.reachCVerbose = 0;
                dataDamager.reachCLastVerbose = UtilTime.timeNow();
            }
        }
    }
}

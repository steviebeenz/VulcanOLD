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
import org.bukkit.entity.Player;

public class ReachE extends Check {

    public ReachE() {
        super("ReachE", "Reach (Type E)", CheckType.COMBAT, true, true, 8);
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
            if (yawDiff > 100) reach -= yawDiff * .01;
            if (dataDamager.isSprinting || dataDamager.hasSpeed()) reach -= .12;
            reach -= dataDamager.lastGroundSpeed * .2;
            reach -= yDiff * .25;
            reach -= ((dataDamager.lastPing + dataVictim.lastPing) / 2) * .001;
            dataDamager.reachEReaches.add(reach);
            if (dataDamager.reachEReaches.size() >= 35) {
                double sum = 0;
                for (double reaches : dataDamager.reachEReaches) {
                    sum += reaches;
                }
                double average = sum / dataDamager.reachEReaches.size();
                if (average > 3.0) flag(damager, "avg=" + average);
                dataDamager.reachEReaches.clear();
            }
        }
    }
}

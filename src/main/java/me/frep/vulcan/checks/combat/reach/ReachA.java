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

public class ReachA extends Check {

    public ReachA() {
        super("ReachA", "Reach (Type A)", CheckType.COMBAT, true, true, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK) || !(packet.getEntity() instanceof Player)) return;
            Player damager = e.getPlayer();
            Player victim = (Player)packet.getEntity();
            PlayerData dataDamager = getDataManager().getPlayerData(damager);
            PlayerData dataVictim = getDataManager().getPlayerData(victim);
            double reach = UtilCheck.getReach(damager, victim);
            double yawDiff = UtilCheck.getYawDiff(damager, victim);
            double maxReach = 3.0;
            if (reach > 7) return;
            if (yawDiff > 100) maxReach += yawDiff * .001;
            if (dataDamager.isSprinting) maxReach += .125;
            maxReach += (dataDamager.lastPing + dataVictim.lastPing) * .0025;
            if (elapsed(dataDamager.reachALastVerbose, 20000)) {
                dataDamager.reachAVerbose = 0;
                dataDamager.reachALastVerbose = UtilTime.timeNow();
            }
            if (dataDamager.isOnGround && dataVictim.isOnGround && dataDamager.airTicks == 0 && dataDamager.airTicks == 0 && reach > maxReach) {
                dataDamager.reachAVerbose++;
                dataDamager.reachALastVerbose = UtilTime.timeNow();
            }
            if (dataDamager.reachAVerbose > 2) {
                flag(damager, reach + " > " + maxReach);
                dataDamager.reachAVerbose = 0;
                dataDamager.reachALastVerbose = UtilTime.timeNow();
            }
        }
    }
}
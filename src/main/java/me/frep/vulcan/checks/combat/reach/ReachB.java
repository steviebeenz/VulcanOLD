package me.frep.vulcan.checks.combat.reach;

import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilCheck;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ReachB extends Check {

    public ReachB() {
        super("ReachB", "Reach (Type B)", CheckType.COMBAT, true, true, 8);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player || !e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))) return;
        Player damager = (Player)e.getDamager();
        Player victim = (Player)e.getEntity();
        PlayerData dataDamager = getDataManager().getPlayerData(damager);
        PlayerData dataVictim = getDataManager().getPlayerData(victim);
        if (dataVictim == null) return;
        double reach = UtilCheck.getReach(damager, victim);
        double yawDiff = UtilCheck.getYawDiff(damager, victim);
        double yDiff = UtilCheck.getYDiff(damager, victim);
        double maxReach = 3.0;
        if (yawDiff > 100) maxReach += yawDiff * .01;
        maxReach += yDiff * .42;
        maxReach += ((dataDamager.lastPing + dataVictim.lastPing) / 2) * .001;
        if (dataDamager.isSprinting || dataDamager.hasSpeed()) maxReach += .2;
        if (reach > maxReach) {
            dataDamager.reachBVerbose++;
            dataDamager.reachBLastVerbose = UtilTime.timeNow();
        }
        if (reach < maxReach && dataDamager.reachBVerbose > 0) dataDamager.reachBVerbose -= .5;
        if (dataDamager.reachBVerbose > 3) {
            flag(damager, reach + " > " + maxReach);
            dataDamager.reachBVerbose = 0;
            dataDamager.reachBLastVerbose = UtilTime.timeNow();
        }
        if (elapsed(dataDamager.reachBLastVerbose, 15000)) {
            dataDamager.reachBVerbose = 0;
            dataDamager.reachBLastVerbose = UtilTime.timeNow();
        }
    }
}
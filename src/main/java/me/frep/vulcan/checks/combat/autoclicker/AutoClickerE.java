package me.frep.vulcan.checks.combat.autoclicker;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilMath;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.entity.Player;

public class AutoClickerE extends Check {

    public AutoClickerE() {
        super("AutoClickerE", "Auto Clicker (Type E)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            if (data.isDigging || data.isPlacing) return;
            data.autoClickerESwings++;
            if (data.autoClickerECPS.size() > 0 && elapsed(data.autoClickerECPS.get(1), 1000)) {
                int cps = data.autoClickerECPS.size();
                if (cps > 6) data.autoClickerELastCPSs.add(cps);
                data.autoClickerECPS.clear();
                data.autoClickerECPS.put(1, UtilTime.timeNow());
            }
            if (data.autoClickerELastCPSs.size() > 8) {
                double deviation = UtilMath.getStandardDeviationInteger(data.autoClickerELastCPSs);
                double skewness = UtilMath.getSkewness(data.autoClickerELastCPSs);
                double variance = UtilMath.getVariance(data.autoClickerELastCPSs);
                double kurtosis = UtilMath.getKurtosis(data.autoClickerELastCPSs);
                if (deviation < .6 && skewness < 0 && kurtosis < 300 && variance < 5) flag(p, "dev=" + deviation + " skew=" + skewness + " kurt=" + kurtosis + " var=" + variance);
                data.autoClickerELastCPSs.clear();
            }
            data.autoClickerECPS.put(data.autoClickerESwings, UtilTime.timeNow());
        }
    }
}

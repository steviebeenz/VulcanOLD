package me.frep.vulcan.checks.combat.autoclicker;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilMath;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AutoClickerF extends Check {

    public AutoClickerF() {
        super("AutoClickerF", "Auto Clicker (Type F)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            if (data.isDigging || data.isPlacing) return;
            long delay = UtilTime.timeNow() - data.autoClickerFLastSwing;
            if (delay > 10000) data.autoClickerFLastSwing = UtilTime.timeNow();
            data.autoClickerFDelays.add(delay);
            if (data.autoClickerFDelays.size() > 10) {
                double skewness = UtilMath.getSkewness(data.autoClickerFDelays);
                data.autoClickerFSkewness.add(skewness);
                data.autoClickerFDelays.clear();
            }
            if (data.autoClickerFSkewness.size() > 2) {
                double deviation = UtilMath.getStandardDeviationDouble(data.autoClickerFSkewness);
                if (UtilMath.isScientificNotation(deviation)) data.autoClickerFVerbose++;
                else {
                    if (data.autoClickerFVerbose > 0) data.autoClickerFVerbose--;
                }
                if (data.autoClickerFVerbose > 3) {
                    flag(p, null);
                    data.autoClickerFVerbose = 0;
                }
                data.autoClickerFSkewness.clear();
            }
            data.autoClickerFLastSwing = UtilTime.timeNow();
        }
    }
}

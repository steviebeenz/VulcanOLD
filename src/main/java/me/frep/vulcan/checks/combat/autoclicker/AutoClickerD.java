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

public class AutoClickerD extends Check {

    public AutoClickerD() {
        super("AutoClickerD", "Auto Clicker (Type D)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            if (data.isDigging || data.isPlacing) return;
            data.autoClickerDSwings++;
            if (data.autoClickerDCPS.size() > 0 && elapsed(data.autoClickerDCPS.get(1), 1000)) {
                int cps = data.autoClickerDCPS.size();
                if (cps > 6) data.autoClickerDLastCPSs.add(cps);
                data.autoClickerDCPS.clear();
                data.autoClickerDCPS.put(1, UtilTime.timeNow());
            }
            if (data.autoClickerDLastCPSs.size() > 2) {
                double deviation = UtilMath.getStandardDeviationInteger(data.autoClickerDLastCPSs);
                if (deviation > 2) flag(p, "dev=" + deviation);
                data.autoClickerDLastCPSs.clear();
            }
            data.autoClickerDCPS.put(data.autoClickerDSwings, UtilTime.timeNow());
        }
    }
}

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

public class AutoClickerC extends Check {

    public AutoClickerC() {
        super("AutoClickerC", "Auto Clicker (Type C)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            if (data.isDigging || data.isPlacing) return;
            long delay = System.currentTimeMillis() - data.autoClickerCLastSwing;
            if (elapsed(data.autoClickerCLastSwing, 10000)) data.autoClickerCDelays.clear();
            if (delay > 10000) data.autoClickerCLastSwing = UtilTime.timeNow();
            if (data.autoClickerCDelays.size() > 40) {
                double deviation = UtilMath.getStandardDeviationLong(data.autoClickerCDelays);
                if (deviation < 14) flag(p, "deviation=" + deviation);
                data.autoClickerCDelays.clear();
            }
            data.autoClickerCDelays.add(delay);
            data.autoClickerCLastSwing = UtilTime.timeNow();
        }
    }
}

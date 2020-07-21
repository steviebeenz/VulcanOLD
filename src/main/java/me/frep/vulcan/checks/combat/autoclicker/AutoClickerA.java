package me.frep.vulcan.checks.combat.autoclicker;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.entity.Player;

public class AutoClickerA extends Check {

    public AutoClickerA() {
        super("AutoClickerA", "Auto Clicker (Type A)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            if (data.isDigging || data.isPlacing) return;
            data.autoClickerASwings++;
            if (data.autoClickerACPS.size() > 0 && elapsed(data.autoClickerACPS.get(1), 1000)) {
                int cps = data.autoClickerACPS.size();
                data.lastCPS = cps;
                if (cps > 15) {
                    flag(p, "cps=" + cps);
                }
                data.autoClickerACPS.clear();
                data.autoClickerACPS.put(1, UtilTime.timeNow());
            }
            data.autoClickerACPS.put(data.autoClickerASwings, UtilTime.timeNow());
        }
    }
}

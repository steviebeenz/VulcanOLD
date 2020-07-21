package me.frep.vulcan.checks.combat.autoclicker;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.entity.Player;

public class AutoClickerB extends Check {

    public AutoClickerB() {
        super("AutoClickerB", "Auto Clicker (Type B)", CheckType.COMBAT, true, false, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            long delay = UtilTime.timeNow() - data.autoClickerBLastAttack;
            if (delay == 0) data.autoClickerBVerbose++;
            if (data.autoClickerBVerbose > 3) {
                flag(p, null);
                data.autoClickerBVerbose = 0;
            }
            data.autoClickerBLastAttack = UtilTime.timeNow();
        }
    }
}

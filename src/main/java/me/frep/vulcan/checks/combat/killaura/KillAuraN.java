package me.frep.vulcan.checks.combat.killaura;

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

public class KillAuraN extends Check {

    public KillAuraN() {
        super("KillAuraN", "Kill Aura (Type N)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.WINDOW_CLICK) {
            data.killAuraNLastClick = UtilTime.timeNow();
        }
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            double delta = Math.abs(UtilTime.timeNow() - data.killAuraNLastClick);
            if (delta == 0) data.killAuraNVerbose++;
            else {
                if (data.killAuraNVerbose > 0) data.killAuraNVerbose --;
            }
            if (data.killAuraNVerbose > 3) {
                data.killAuraNVerbose = 0;
                flag(p, null);
            }
        }
        if (e.getPacketId() == PacketType.Client.FLYING) {
            data.killAuraNLastClick = 0;
            data.killAuraNVerbose = 0;
        }
    }
}

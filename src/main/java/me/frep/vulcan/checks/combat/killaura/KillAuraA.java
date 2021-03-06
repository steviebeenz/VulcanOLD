package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilLag;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.entity.Player;

public class KillAuraA extends Check {

    public KillAuraA() {
        super("KillAuraA", "Kill Aura (Type A)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            long delta = UtilTime.timeNow() - data.lastMovePacket;
            if (delta < 5 && UtilLag.getPing(p) < 400 && UtilLag.getTPS() > 19.5) data.killAuraAVerbose++;
            else {
                if (data.killAuraAVerbose > 0) data.killAuraAVerbose--;
            }
            if (data.killAuraAVerbose > 5) {
                flag(p, null);
                data.killAuraAVerbose = 0;
            }
        }
    }
}
package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraB extends Check {

    public KillAuraB() {
        super("KillAuraB", "Kill Aura (Type B)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (data.isDigging) data.killAuraBVerbose++;
            else {
                if (data.killAuraBVerbose > 0) data.killAuraBVerbose--;
            }
            if (data.killAuraBVerbose > 2) {
                flag(p, null);
                data.killAuraBVerbose = 0;
            }
        }
    }
}

package me.frep.vulcan.checks.combat.criticals;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class CriticalsB extends Check {

    public CriticalsB() {
        super("CriticalsB", "Criticals (Type B)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (data.isNearSlab(2) || data.isBelowBlock) return;
            if (p.getLocation().getY() % 1 == 0 && !data.isOnGround && !p.isOnGround() && data.airTicks > 0 && !data.isBelowBlock) data.criticalsBVerbose++;
            else {
                if (data.criticalsBVerbose > 0) data.criticalsBVerbose--;
            }
            if (data.criticalsBVerbose > 2) {
                flag(p, null);
                data.criticalsBVerbose = 0;
            }
        }
    }
}

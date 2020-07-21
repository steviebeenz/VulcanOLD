package me.frep.vulcan.checks.combat.criticals;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilMath;
import org.bukkit.entity.Player;

public class CriticalsC extends Check {

    public CriticalsC() {
        super("CriticalsC", "Criticals (Type C)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (p.getLocation().getY() % 1 == 0 && !data.isOnGround && !p.isOnGround() && data.airTicks > 30
                    && UtilMath.isScientificNotation(p.getFallDistance())) flag(p, null);
        }
    }
}

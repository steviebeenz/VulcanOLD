package me.frep.vulcan.checks.combat.criticals;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class CriticalsD extends Check {

    public CriticalsD() {
        super("CriticalsD", "Criticals (Type D)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (data.isNearSlab(2) || data.isBelowBlock) return;
            if (!data.isOnGround && !p.isOnGround() && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()
                    && data.airTicks > 50) data.criticalsDVerbose++;
            else {
                if (data.criticalsDVerbose > 0) data.criticalsDVerbose--;
            }
            if (data.criticalsDVerbose > 2) {
                flag(p, null);
                data.criticalsDVerbose = 0;
            }
        }
    }
}

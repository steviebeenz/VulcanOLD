package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.enums.minecraft.PlayerDigType;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraG extends Check {

    public KillAuraG() {
        super("KillAuraG", "Kill Aura (Type G)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            data.killAuraGSentRelease = false;
        }
        if (e.getPacketId() == PacketType.Client.BLOCK_DIG) {
            WrappedPacketInBlockDig packet = new WrappedPacketInBlockDig(e.getNMSPacket());
            if (packet.getDigType().equals(PlayerDigType.RELEASE_USE_ITEM)) data.killAuraGSentRelease = true;
        }
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (data.killAuraGSentRelease) data.killAuraGVerbose++;
            if (data.killAuraGVerbose > 2) {
                flag(p, null);
                data.killAuraGVerbose = 0;
            }
        }
    }
}

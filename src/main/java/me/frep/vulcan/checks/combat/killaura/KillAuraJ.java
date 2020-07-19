package me.frep.vulcan.checks.combat.killaura;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.enums.minecraft.PlayerAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraJ extends Check {

    public KillAuraJ() {
        super("KillAuraJ", "Kill Aura (Type J)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ENTITY_ACTION) {
            WrappedPacketInEntityAction packet = new WrappedPacketInEntityAction(e.getNMSPacket());
            if (packet.getAction() == PlayerAction.START_SPRINTING
                || packet.getAction() == PlayerAction.STOP_SPRINTING
                || packet.getAction() == PlayerAction.START_SNEAKING
                || packet.getAction() == PlayerAction.STOP_SNEAKING) data.killAuraJSentAction = true;
        }
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            data.killAuraJSentAction = false;
        }
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (data.killAuraJSentAction) flag(p, null);
        }
    }
}

package me.frep.vulcan.checks.combat.reach;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.Vulcan;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import org.bukkit.entity.Player;

public class ReachA extends Check {

    public ReachA(Vulcan vulcan) {
        super("ReachA", "Reach (Type A)", CheckType.COMBAT, vulcan);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!(packet.getEntity() instanceof Player || !packet.getAction().equals(EntityUseAction.ATTACK))) return;
            Player damager = e.getPlayer();
            Player victim = (Player) packet.getEntity();
            double distance = damager.getLocation().distance(victim.getLocation());
            if (distance > 5) {
                flag(damager, "reach: " + distance);
            }
        }
    }
}
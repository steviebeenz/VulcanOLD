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

public class KillAuraA extends Check {

    private int streak;
    private boolean wasSprinting;

    public KillAuraA() {
        super("KillAuraA", "Kill Aura (Type A)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!(packet.getEntity() instanceof Player || !packet.getAction().equals(EntityUseAction.ATTACK))) return;
            Player p = e.getPlayer();
            PlayerData data = getDataManager().getPlayerData(p);
        }
    }
}
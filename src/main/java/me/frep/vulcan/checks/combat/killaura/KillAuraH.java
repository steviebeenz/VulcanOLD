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

public class KillAuraH extends Check {

    public KillAuraH() {
        super("KillAuraH", "Kill Aura (Type H)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.CLOSE_WINDOW) {
            data.killAuraHClosedWindow = true;
        }
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            data.killAuraHClosedWindow = false;
        }
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (data.killAuraHClosedWindow) flag(p, null);
        }
    }
}

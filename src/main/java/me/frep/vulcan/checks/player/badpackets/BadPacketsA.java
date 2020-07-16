package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.abilities.WrappedPacketInAbilities;
import io.github.retrooper.packetevents.packetwrappers.out.abilities.WrappedPacketOutAbilities;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;

public class BadPacketsA extends Check {

    public BadPacketsA() {
        super("BadPacketsA", "Bad Packets (Type A)", CheckType.PLAYER, true, true, 2);
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Server.ABILITIES) {
            WrappedPacketOutAbilities packet = new WrappedPacketOutAbilities(e.getNMSPacket());
            if (packet.isFlightAllowed()) data.badPacketsAServerSent = true;
        }
        if (e.getPacketId() == PacketType.Client.ABILITIES) {
            WrappedPacketInAbilities packet = new WrappedPacketInAbilities(e.getNMSPacket());
            if (packet.isFlightAllowed()) data.badPacketsAClientSent = true;
        } else {
            data.badPacketsAServerSent = data.badPacketsAClientSent = false;
        }
        if (!data.badPacketsAServerSent && data.badPacketsAClientSent) {
            flag(p, null);
        }
    }
}
package me.frep.vulcan.checks.player.badpackets;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BadPacketsF extends Check {

    public BadPacketsF() {
        super("BadPacketsF", "Bad Packets (Type F)", CheckType.PLAYER, true, true, 2);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            double maxSpeed = .23;
            for (PotionEffect effect : p.getActivePotionEffects()) if (effect.getType().equals(PotionEffectType.SPEED)) maxSpeed += (effect.getAmplifier() + 1) * .07;
            if (data.isNearIce(2)) maxSpeed += .08;
            if (p.getWalkSpeed() > .21) maxSpeed += p.getWalkSpeed() / .25;
            if (data.lastGroundSpeed > maxSpeed && !data.isSprinting && !p.isSprinting()) data.badPacketsFStreak++;
            else {
                if (data.badPacketsFStreak > 0 || data.airTicks > 0) data.badPacketsFStreak--;
            }
            if (data.badPacketsFStreak > 6) {
                flag(p, null);
                data.badPacketsFStreak = 0;
            }
        }
        if (e.getPacketId() == PacketType.Client.FLYING) {
            data.badPacketsFStreak = 0;
        }
    }
}
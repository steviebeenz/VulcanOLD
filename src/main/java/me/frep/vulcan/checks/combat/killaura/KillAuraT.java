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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KillAuraT extends Check {

    public KillAuraT() {
        super("KillAuraT", "Kill Aura (Type T)", CheckType.COMBAT, true, false, 8);
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (!packet.getAction().equals(EntityUseAction.ATTACK)) return;
            if (data.isOnGround) return;
            double airSpeed = data.lastAirSpeed;
            double maxSpeed = .29;
            if (p.getWalkSpeed() > .21) maxSpeed += p.getWalkSpeed() / 1.25;
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.SPEED)) maxSpeed += (effect.getAmplifier() + 1) * .065;
            }
            if (data.isNearIce(2)) maxSpeed += .075;
            if (airSpeed > maxSpeed) data.killAuraTVerbose++;
            else {
                if (data.killAuraTVerbose > 0) data.killAuraTVerbose--;
            }
            if (data.killAuraTVerbose > 3) {
                flag(p, null);
                data.killAuraTVerbose = 0;
            }
        }
    }
}

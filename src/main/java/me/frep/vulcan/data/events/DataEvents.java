package me.frep.vulcan.data.events;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.minecraft.EntityUseAction;
import io.github.retrooper.packetevents.enums.minecraft.PlayerDigType;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcan.Vulcan;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.data.PlayerData;
import me.frep.vulcan.utilities.UtilLag;
import me.frep.vulcan.utilities.UtilMath;
import me.frep.vulcan.utilities.UtilTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataEvents implements Listener, PacketListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("vulcan.alerts")) Vulcan.instance.alertsEnabled.add(p);
        Vulcan.instance.getDataManager().createPlayerData(p);
        PlayerData data = Vulcan.instance.getDataManager().getPlayerData(p);
        data.lastJoin = UtilTime.timeNow();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData data = Vulcan.instance.getDataManager().getPlayerData(p);
        Vulcan.instance.getDataManager().removePlayerData(data);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        PlayerData data = Vulcan.instance.getDataManager().getPlayerData(p);
        data.lastWorldChange = UtilTime.timeNow();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = Vulcan.instance.getDataManager().getPlayerData(p);
        double groundSpeed = UtilMath.getHorizontalDistance(e.getFrom(), e.getTo());
        if (data.isOnGround) data.lastGroundSpeed = groundSpeed;
        if (!data.isOnGround) data.lastGroundSpeed = 0;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player)e.getPlayer();
        PlayerData data = Vulcan.instance.getDataManager().getPlayerData(p);
        data.isInGUI = true;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        PlayerData data = Vulcan.instance.getDataManager().getPlayerData(p);
        data.isInGUI = false;
    }

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        if (Check.bannedPlayers.containsKey(p.getUniqueId())) return;
        PlayerData data = Vulcan.instance.getDataManager().getPlayerData(p);
        if (e.getPacketId() == PacketType.Client.ENTITY_ACTION) {
            WrappedPacketInEntityAction packet = new WrappedPacketInEntityAction(e.getNMSPacket());
            switch (packet.getAction()) {
                case START_SPRINTING:
                    data.isSprinting = true;
                    break;
                case STOP_SPRINTING:
                    data.isSprinting = false;
                    break;
                case START_SNEAKING:
                    data.isSneaking = true;
                    break;
                case STOP_SNEAKING:
                    data.isSneaking = false;
                    break;
            }
        }
        if (e.getPacketId() == PacketType.Client.BLOCK_PLACE) {
            data.isPlacing = true;
        }
        if (e.getPacketId() == PacketType.Client.BLOCK_DIG) {
            WrappedPacketInBlockDig packet = new WrappedPacketInBlockDig(e.getNMSPacket());
            if (packet.getDigType().equals(PlayerDigType.START_DESTROY_BLOCK)) {
                data.isDigging = true;
            } else if (packet.getDigType().equals(PlayerDigType.ABORT_DESTROY_BLOCK) || packet.getDigType().equals(PlayerDigType.STOP_DESTROY_BLOCK)) {
                data.isDigging = false;
            }
        }
        if (e.getPacketId() == PacketType.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (packet.getAction() != EntityUseAction.ATTACK) return;
            data.lastAttackPacket = UtilTime.timeNow();
        }
        if (PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying packet = new WrappedPacketInFlying(e.getNMSPacket());
            data.isPlacing = false;
            data.lastPing = UtilLag.getPing(p);
            data.lastMovePacket = UtilTime.timeNow();
            if (packet.isOnGround()) {
                data.isOnGround = true;
                data.lastOnGround = UtilTime.timeNow();
                data.groundTicks++;
                data.airTicks = 0;
                if (data.groundTicks > 5000) data.groundTicks = 0;
            } else {
                if (!packet.isOnGround()) {
                    data.isOnGround = false;
                    data.lastInAir = UtilTime.timeNow();
                    data.groundTicks = 0;
                    data.airTicks++;
                    if (data.airTicks > 5000) data.airTicks = 0;
                }
            }
        }
        if (e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            data.lastSwingPacket = UtilTime.timeNow();
        }
    }
}
package me.frep.vulcan.data;

import org.bukkit.entity.Player;

public class PlayerData {

    public Player player;

    public int groundTicks, airTicks;

    public double lastGroundSpeed, lastAirSpeed;

    public long lastWorldChange, lastAttackPacket;

    public boolean isSprinting, isSneaking, isDigging, isOnGround, isMoving;

    /* Bad Packets (Type A) */
    public boolean badPacketsAClientSent, badPacketsAServerSent;
    /* Bad Packets (Type A) */

    public PlayerData(Player player) {
        this.player = player;
    }
}

package me.frep.vulcan.data;

import me.frep.vulcan.utilities.UtilBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {

    public Player player;

    public int groundTicks, airTicks, lastPing;

    public double lastGroundSpeed;

    public long lastWorldChange, lastAttackPacket, lastSwingPacket, lastMovePacket, lastJoin, lastOnGround, lastInAir;

    public boolean isSprinting, isSneaking, isDigging, isOnGround, isPlacing, isInGUI;

    /* Reach (Type A) */
    public int reachAVerbose;
    public long reachALastVerbose;
    /* Reach (Type A) */

    /* Reach (Type B) */
    public int reachBVerbose;
    public long reachBLastVerbose;
    /* Reach (Type B) */

    /* Bad Packets (Type A) */
    public boolean badPacketsAClientSent, badPacketsAServerSent;
    /* Bad Packets (Type A) */

    /* Bad Packets (Type C) */
    public int badPacketsCStreak;
    /* Bad Packets (Type C) */

    /* Bad Packets (Type D) */
    public int badPacketsDStreak;
    public boolean badPacketsDHasSwung;
    /* Bad Packets (Type D) */

    /* Bad Packets (Type E) */
    public int badPacketsEVerbose;
    /* Bad Packets (Type E) */

    /* Bad Packets (Type F) */
    public int badPacketsFStreak;
    /* Bad Packets (Type F) */

    /* Kill Aura (Type A) */
    public int killAuraAVerbose;
    /* Kill Aura (Type A) */

    /* Kill Aura (Type B) */
    public int killAuraBVerbose;
    /* Kill Aura (Type B) */

    /* Kill Aura (Type C) */
    public int killAuraCVerbose;
    public float killAuraCLastPitch, killAuraCLastYaw;
    /* Kill Aura (Type C) */

    /* Kill Aura (Type D) */
    public int killAuraDVerbose;
    /* Kill Aura (Type D) */

    /* Kill Aura (Type E) */
    public int killAuraEVerbose;
    public float killAuraELastPitch, killAuraELastYaw;
    /* Kill Aura (Type E) */

    /* Kill Aura (Type F) */
    public boolean killAuraFInventoryOpen;
    /* Kill Aura (Type F) */

    /* Kill Aura (Type G) */
    public boolean killAuraGSentRelease;
    public int killAuraGVerbose;
    /* Kill Aura (Type G) */

    /* Kill Aura (Type H) */
    public boolean killAuraHClosedWindow;
    /* Kill Aura (Type H) */

    /* Kill Aura (Type J) */
    public boolean killAuraJSentAction;
    /* Kill Aura (Type J) */

    /* Kill Aura (Type K) */
    public int killAuraKStreak;
    /* Kill Aura (Type K) */

    /* Kill Aura (Type L) */
    public int killAuraLStreak;
    /* Kill Aura (Type L) */

    /* Kill Aura (Type M) */
    public int killAuraMVerbose;
    public float killAuraMLastPitch, killAuraMLastYaw;
    /* Kill Aura (Type M) */

    /* Kill Aura (Type N) */
    public float killAuraNLastClick, killAuraNVerbose;
    /* Kill Aura (Type N) */

    /* Kill Aura (Type O) */
    public int killAuraOVerbose;
    /* Kill Aura (Type O) */

    /* Kill Aura (Type P) */
    public float killAuraPLastYaw, killAuraPLastPitch;
    public double killAuraPLastSpeed;
    /* Kill Aura (Type P) */

    /* Kill Aura (Type Q) */
    public int killAuraQVerbose;
    public List<Float> killAuraQAngles = new ArrayList<>();
    public List<Double> killAuraQRange = new ArrayList<>();
    /* Kill Aura (Type Q) */

    /* Kill Aura (Type R) */
    public double killAuraRLastSpeed;
    public int killAuraRVerbose;
    public float killAuraRLastPitch, killAuraRLastYaw;
    /* Kill Aura (Type R) */

    /* Kill Aura (Type S) */
    public long killAuraSLastAttack;
    public List<Long> killAuraSDelays = new ArrayList<>();
    public List<Long> killAuraSRanges = new ArrayList<>();
    /* Kill Aura (Type S) */

    public boolean isNearWeb(int radius) {
        for (Block b : UtilBlock.getNearbyBlocks(player.getLocation(), radius)) {
            if (UtilBlock.isWeb(b)) return true;
        }
        return false;
    }

    public boolean isNearFence(int radius) {
        for (Block b: UtilBlock.getNearbyBlocks(player.getLocation(), radius)) {
            if (UtilBlock.isFence(b)) return true;
        }
        return false;
    }

    public boolean isNearSlab(int radius) {
        for (Block b: UtilBlock.getNearbyBlocks(player.getLocation(), radius)) {
            if (UtilBlock.isSlab(b)) return true;
        }
        return false;
    }

    public boolean isNearStair(int radius) {
        for (Block b: UtilBlock.getNearbyBlocks(player.getLocation(), radius)) {
            if (UtilBlock.isStair(b)) return true;
        }
        return false;
    }

    public boolean isNearSlime(int radius) {
        for (Block b: UtilBlock.getNearbyBlocks(player.getLocation(), radius)) {
            if (UtilBlock.isSlime(b)) return true;
        }
        return false;
    }

    public boolean hasSpeed() {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) return true;
            }
        return false;
    }

    public boolean isNearIce(int radius) {
        for (Block b: UtilBlock.getNearbyBlocks(player.getLocation(), radius)) {
            if (UtilBlock.isIce(b)) return true;
        }
        return false;
    }

    public PlayerData(Player player) {
        this.player = player;
    }
}

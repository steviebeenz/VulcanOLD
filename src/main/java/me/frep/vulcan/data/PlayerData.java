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

    public int groundTicks, airTicks, lastPing, lastCPS, badPacketsEVerbose, badPacketsFStreak, badPacketsGVerbose, badPacketsDStreak,
            badPacketsCStreak, badPacketsHLastAirTicks, badPacketsHVerbose, criticalsAVerbose, criticalsBVerbose, criticalsDVerbose,
            killAuraAVerbose, killAuraBVerbose, killAuraCVerbose, badPacketsIVerbose, killAuraDVerbose, killAuraEVerbose, killAuraGVerbose,
            killAuraKStreak, killAuraLStreak, killAuraMVerbose, killAuraOVerbose, killAuraRVerbose, killAuraTVerbose, aimPatternAVerbose,
            aimPatternBVerbose, aimPatternCVerbose, aimPatternDVerbose, aimPatternEVerbose, autoClickerASwings, reachAVerbose, reachBVerbose, reachCVerbose, killAuraNVerbose,
            autoClickerBVerbose, autoClickerDSwings, autoClickerESwings, killAuraUVerbose, autoClickerFVerbose;

    public double lastGroundSpeed, lastAirSpeed, badPacketsILastY, badPacketsILastDeltaY, badPacketsHLastY, killAuraPLastSpeed, killAuraRLastSpeed;

    public long lastWorldChange, lastAttackPacket, lastSwingPacket, lastMovePacket, lastJoin, lastOnGround, lastInAir, lastTeleport, killAuraSLastAttack,
            reachALastVerbose, reachBLastVerbose, reachCLastVerbose, autoClickerBLastAttack, autoClickerCLastSwing, autoClickerFLastSwing;

    public boolean badPacketsAClientSent, badPacketsAServerSent, badPacketsDHasSwung, killAuraFInventoryOpen, killAuraGSentRelease,
            killAuraHClosedWindow, killAuraJSentAction, isSprinting, isSneaking, isDigging, isOnGround, isPlacing, isInGUI, isBelowBlock;

    public float badPacketsGLastYaw, badPacketsGLastPitch, killAuraCLastPitch, killAuraCLastYaw, killAuraELastPitch, killAuraELastYaw,
            killAuraMLastPitch, killAuraMLastYaw, killAuraNLastClick, killAuraPLastYaw, killAuraPLastPitch, killAuraRLastPitch, killAuraRLastYaw,
            aimPatternALastYaw, aimPatternALastPitch, aimPatternALastDeltaYaw, aimPatternALastDeltaPitch, aimPatternBLastYaw, aimPatternBLastPitch,
            aimPatternBLastDeltaYaw, aimPatternBLastDeltaPitch, aimPatternCLastYaw, aimPatternCLastPitch, aimPatternCLastDeltaYaw, aimPatternCLastDeltaPitch,
            aimPatternDLastYaw, aimPatternDLastPitch, aimPatternDLastDeltaYaw, aimPatternDLastDeltaPitch, aimPatternELastYaw, aimPatternELastPitch,
            aimPatternELastDeltaYaw, aimPatternELastDeltaPitch, aimPatternELastDeltaDeltaPitch, aimPatternELastDeltaDeltaYaw, killAuraULastPitch,
            killAuraULastYaw;

    public List<Float> killAuraQAngles = new ArrayList<>();
    public List<Double> killAuraQRange = new ArrayList<>();
    public List<Long> killAuraSDelays = new ArrayList<>();
    public List<Long> killAuraSRanges = new ArrayList<>();
    public Map<Integer, Long> autoClickerACPS = new HashMap<>();
    public Map<Integer, Long> autoClickerDCPS = new HashMap<>();
    public Map<Integer, Long> autoClickerECPS = new HashMap<>();
    public List<Integer> autoClickerDLastCPSs = new ArrayList<>();
    public List<Integer> autoClickerELastCPSs = new ArrayList<>();
    public List<Double> reachDReaches = new ArrayList<>();
    public List<Double> reachEReaches = new ArrayList<>();
    public List<Long> autoClickerCDelays = new ArrayList<>();
    public List<Float> killAuraUDeltaPitches = new ArrayList<>();
    public List<Float> killAuraUDeltaYaws = new ArrayList<>();
    public List<Long> autoClickerFDelays = new ArrayList<>();
    public List<Double> autoClickerFSkewness = new ArrayList<>();

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

    public boolean isNearClimbable(int radius) {
        for (Block b: UtilBlock.getNearbyBlocks(player.getLocation(), radius)) {
            if (UtilBlock.isClimbable(b)) return true;
        }
        return false;
    }

    public PlayerData(Player player) {
        this.player = player;
    }
}

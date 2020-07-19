package me.frep.vulcan.utilities;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class UtilBlock {

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        final List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static boolean isWeb(Block b) {
        return b.getType().toString().contains("WEB");
    }

    public static boolean isFence(Block b) {
        return b.getType().toString().contains("FENCE)");
    }

    public static boolean isStair(Block b) {
        return b.getType().toString().contains("STAIR");
    }

    public static boolean isSlab(Block b) {
        return b.getType().toString().contains("SLAB") || b.getType().toString().contains("STEP");
    }

    public static boolean isSlime(Block b) {
        return b.getType().toString().contains("SLIME");
    }

    public static boolean isIce(Block b) {
        return b.getType().toString().contains("ICE");
    }
}

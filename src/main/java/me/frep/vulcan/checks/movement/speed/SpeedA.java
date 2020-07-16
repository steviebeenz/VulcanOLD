package me.frep.vulcan.checks.movement.speed;

import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import me.frep.vulcan.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedA extends Check {

    public SpeedA() {
        super("SpeedA", "Speed (Type A)", CheckType.MOVEMENT, true, true, 8);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = getDataManager().getPlayerData(p);
    }
}
package me.frep.vulcan.checks.movement.speed;

import me.frep.vulcan.Vulcan;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedA extends Check {

    public SpeedA() {
        super("SpeedA", "Speed (Type A)", CheckType.MOVEMENT, true, true, 8, 120000);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().distance(e.getTo()) > .3) {
            flag(p, null);
        }
    }
}
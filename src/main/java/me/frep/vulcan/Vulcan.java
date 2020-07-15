package me.frep.vulcan;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.manager.EventManager;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.combat.reach.ReachA;
import me.frep.vulcan.checks.movement.speed.SpeedA;
import me.frep.vulcan.commands.AlertsCommand;
import me.frep.vulcan.utilities.UtilConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Vulcan extends JavaPlugin {

    public static Vulcan instance;
    public static List<Check> checks = new ArrayList<>();
    public static List<Player> alertsEnabled = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        PacketEvents.load();
        PacketEvents.start(this);
        registerCommands();
        UtilConfig.getInstance().generateConfig();
        UtilConfig.getInstance().generateChecksConfig();
        enableAlerts();
        registerChecks();
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
        instance = null;
    }

    private void registerChecks() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        EventManager em = PacketEvents.getAPI().getEventManager();
        checks.add(new SpeedA(this));
        pm.registerEvents(new SpeedA(this), this);
        checks.add(new ReachA(this));
        em.registerListener(new ReachA(this));
    }

    private void registerCommands() {
        getCommand("alerts").setExecutor(new AlertsCommand());
    }

    private void enableAlerts() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("vulcan.alerts")) {
                alertsEnabled.add(p);
            }
        }
    }
}
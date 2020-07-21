package me.frep.vulcan;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.manager.EventManager;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.combat.aimpattern.*;
import me.frep.vulcan.checks.combat.autoclicker.*;
import me.frep.vulcan.checks.combat.criticals.CriticalsA;
import me.frep.vulcan.checks.combat.criticals.CriticalsB;
import me.frep.vulcan.checks.combat.criticals.CriticalsC;
import me.frep.vulcan.checks.combat.criticals.CriticalsD;
import me.frep.vulcan.checks.combat.killaura.*;
import me.frep.vulcan.checks.combat.reach.*;
import me.frep.vulcan.checks.movement.speed.SpeedA;
import me.frep.vulcan.checks.player.badpackets.*;
import me.frep.vulcan.checks.player.hackedclient.HackedClientA;
import me.frep.vulcan.commands.AlertsCommand;
import me.frep.vulcan.commands.VulcanCommand;
import me.frep.vulcan.data.DataManager;
import me.frep.vulcan.data.events.DataEvents;
import me.frep.vulcan.utilities.UtilConfig;
import me.frep.vulcan.utilities.UtilGUI;
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
    private DataManager dataManager = new DataManager();

    @Override
    public void onEnable() {
        instance = this;
        PacketEvents.load();
        PacketEvents.start(this);
        UtilConfig.getInstance().generateConfig();
        UtilConfig.getInstance().generateChecksConfig();
        registerCommands();
        enableAlerts();
        registerChecks();
        reloadPlayerData();
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
        instance = null;
    }

    private void registerChecks() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        EventManager em = PacketEvents.getAPI().getEventManager();
        checks.add(new AimPatternA());
        em.registerListener(new AimPatternA());
        checks.add(new AimPatternB());
        em.registerListener(new AimPatternB());
        checks.add(new AimPatternC());
        em.registerListener(new AimPatternC());
        checks.add(new AimPatternD());
        em.registerListener(new AimPatternD());
        checks.add(new AimPatternE());
        em.registerListener(new AimPatternE());
        checks.add(new AutoClickerA());
        em.registerListener(new AutoClickerA());
        checks.add(new AutoClickerB());
        em.registerListener(new AutoClickerB());
        checks.add(new AutoClickerC());
        em.registerListener(new AutoClickerC());
        checks.add(new AutoClickerD());
        em.registerListener(new AutoClickerD());
        checks.add(new AutoClickerE());
        em.registerListener(new AutoClickerE());
        checks.add(new AutoClickerF());
        em.registerListener(new AutoClickerF());
        checks.add(new ReachA());
        em.registerListener(new ReachA());
        checks.add(new ReachB());
        pm.registerEvents(new ReachB(), this);
        checks.add(new ReachC());
        em.registerListener(new ReachC());
        checks.add(new ReachD());
        em.registerListener(new ReachD());
        checks.add(new ReachE());
        em.registerListener(new ReachE());
        checks.add(new KillAuraA());
        em.registerListener(new KillAuraA());
        checks.add(new KillAuraB());
        em.registerListener(new KillAuraB());
        checks.add(new KillAuraC());
        em.registerListener(new KillAuraC());
        checks.add(new KillAuraD());
        em.registerListener(new KillAuraD());
        checks.add(new KillAuraE());
        em.registerListener(new KillAuraE());
        checks.add(new KillAuraF());
        em.registerListener(new KillAuraF());
        checks.add(new KillAuraG());
        em.registerListener(new KillAuraG());
        checks.add(new KillAuraH());
        em.registerListener(new KillAuraH());
        checks.add(new KillAuraI());
        em.registerListener(new KillAuraI());
        checks.add(new KillAuraJ());
        em.registerListener(new KillAuraJ());
        checks.add(new KillAuraK());
        em.registerListener(new KillAuraK());
        checks.add(new KillAuraL());
        em.registerListener(new KillAuraL());
        checks.add(new KillAuraM());
        em.registerListener(new KillAuraM());
        checks.add(new KillAuraN());
        em.registerListener(new KillAuraN());
        checks.add(new KillAuraO());
        em.registerListener(new KillAuraO());
        checks.add(new KillAuraP());
        em.registerListener(new KillAuraP());
        checks.add(new KillAuraQ());
        em.registerListener(new KillAuraQ());
        checks.add(new KillAuraR());
        em.registerListener(new KillAuraR());
        checks.add(new KillAuraS());
        em.registerListener(new KillAuraS());
        checks.add(new KillAuraT());
        em.registerListener(new KillAuraT());
        checks.add(new KillAuraU());
        em.registerListener(new KillAuraU());
        checks.add(new CriticalsA());
        em.registerListener(new CriticalsA());
        checks.add(new CriticalsB());
        em.registerListener(new CriticalsB());
        checks.add(new CriticalsC());
        em.registerListener(new CriticalsC());
        checks.add(new CriticalsD());
        em.registerListener(new CriticalsD());
        checks.add(new BadPacketsA());
        em.registerListener(new BadPacketsA());
        checks.add(new BadPacketsB());
        em.registerListener(new BadPacketsB());
        checks.add(new BadPacketsC());
        em.registerListener(new BadPacketsC());
        checks.add(new BadPacketsD());
        em.registerListener(new BadPacketsD());
        checks.add(new BadPacketsE());
        em.registerListener(new BadPacketsE());
        checks.add(new BadPacketsF());
        em.registerListener(new BadPacketsF());
        checks.add(new BadPacketsG());
        em.registerListener(new BadPacketsG());
        checks.add(new BadPacketsH());
        em.registerListener(new BadPacketsH());
        checks.add(new BadPacketsI());
        em.registerListener(new BadPacketsI());

        checks.add(new SpeedA());
        pm.registerEvents(new SpeedA(), this);
        checks.add(new HackedClientA());
        pm.registerEvents(new HackedClientA(), this);

        em.registerListener(new DataEvents());
        pm.registerEvents(new DataEvents(), this);
        pm.registerEvents(new UtilGUI(), this);
    }

    private void registerCommands() {
        getCommand("vulcan").setExecutor(new VulcanCommand());
        getCommand("alerts").setExecutor(new AlertsCommand());
    }

    private void enableAlerts() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("vulcan.alerts")) {
                alertsEnabled.add(p);
            }
        }
    }

    private void reloadPlayerData() {
        dataManager.getDataPlayers().clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            dataManager.createPlayerData(p);
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
package me.frep.vulcan.checks;

import io.github.retrooper.packetevents.event.PacketListener;
import me.frep.vulcan.Vulcan;
import me.frep.vulcan.data.DataManager;
import me.frep.vulcan.utilities.UtilColor;
import me.frep.vulcan.utilities.UtilConfig;
import me.frep.vulcan.utilities.UtilLag;
import me.frep.vulcan.utilities.UtilMath;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Check implements Listener, PacketListener {

    private Map<UUID, Map<Check, Integer>> violations = new HashMap<>();
    public static Map<UUID, Check> bannedPlayers = new HashMap<>();

    private String identifier;
    private String name;
    private CheckType checkType;
    public static Vulcan vulcan;
    private boolean enabled;
    private boolean bannable;
    private Integer maxViolations;

    public Check(String identifier, String name, CheckType checkType, boolean enabled, boolean bannable, int maxViolations) {
        this.identifier = identifier;
        this.name = name;
        this.checkType = checkType;
        this.vulcan = Vulcan.instance;
        if (!getChecksConfig().isConfigurationSection("checks." + getType() + "." + getIdentifier())) {
            getChecksConfig().set("checks." + getType() + "." + getIdentifier() + ".enabled", enabled);
            getChecksConfig().set("checks." + getType() + "." + getIdentifier() + ".bannable", bannable);
            getChecksConfig().set("checks." + getType() + "." + getIdentifier() + ".maxViolations", maxViolations);
        }
        this.enabled =  getChecksConfig().getBoolean("checks." + getType() + "." + getIdentifier() + ".enabled");
        this.bannable =  getChecksConfig().getBoolean("checks." + getType() + "." + getIdentifier() + ".bannable");
        this.maxViolations = getChecksConfig().getInt("checks." + getType() + "." + getIdentifier() + ".maxViolations");
        UtilConfig.getInstance().saveChecksConfig();
    }

    public Vulcan getVulcan() {
        return this.vulcan;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getName() {
        return this.name;
    }

    public CheckType getType() {
        return this.checkType;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isBannable() {
        return this.bannable;
    }

    public Integer getMaxViolations() {
        return this.maxViolations;
    }

    public void addViolation(Check check, Player p) {
        Map<Check, Integer> vl = new HashMap<>();
        if (violations.containsKey(p.getUniqueId())) {
            vl = violations.get(p.getUniqueId());
        }
        if (!vl.containsKey(check)) {
            vl.put(check, 1);
        } else {
            vl.put(check, vl.get(check) + 1);
        }
        violations.put(p.getUniqueId(), vl);
    }

    public Integer getViolations(Check check, Player p) {
        if (violations.containsKey(p.getUniqueId())) {
            if (violations.get(p.getUniqueId()).containsKey(check)) {
                return violations.get(p.getUniqueId()).get(check);
            }
        }
        return 0;
    }

    public void flag(Player p, String info) {
        if (p.hasPermission("vulcan.bypass") || p.getAllowFlight()
                || p.getGameMode().equals(GameMode.CREATIVE )||!isEnabled() || bannedPlayers.containsKey(p.getUniqueId())) return;
        addViolation(this, p);
        int vl = getViolations(this, p);
        if (getConfig().getBoolean("alerts.show-in-console")) {
            System.out.println(getConfig().getString("alerts.console-message")
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%info%", info)
                    .replaceAll("%check%", this.getName())
                    .replaceAll("%vl%", Integer.toString(vl))
                    .replaceAll("%tps%", Double.toString(UtilMath.trim(2, UtilLag.getTPS())))
                    .replaceAll("%ping%", Integer.toString(UtilLag.getPing(p))));
        }
        for (Player staff : Vulcan.alertsEnabled) {
            TextComponent alertMessage = new TextComponent(UtilColor.translate(getConfig().getString("alerts.message")
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%vl%", Integer.toString(vl))
                    .replaceAll("%check%", this.getName())
                    .replaceAll("%prefix%", getConfig().getString("prefix"))
                    .replaceAll("%tps%", Double.toString(UtilMath.trim(2, UtilLag.getTPS())))
                    .replaceAll("%ping%", Integer.toString(UtilLag.getPing(p)))));
            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    UtilColor.translate(getConfig().getString("alerts.click-command")
                            .replaceAll("%player%", p.getName()))));
            StringBuilder builder = new StringBuilder();
            int listSize = getConfig().getStringList("alerts.hover").size();
            int i = 1;
            for (String hoverMessages : getConfig().getStringList("alerts.hover")) {
                if (hoverMessages.contains("%info%") && info == null) {
                    i++;
                    continue;
                }
                if (i == listSize) {
                    builder.append(hoverMessages);
                } else {
                    builder.append(hoverMessages + "\n");
                }
                i++;
            }
            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(UtilColor.translate(builder.toString()
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%info%", info)
                    .replaceAll("%vl%", Integer.toString(vl))
                    .replaceAll("%tps%", Double.toString(UtilMath.trim(2, UtilLag.getTPS())))
                    .replaceAll("%ping%", Integer.toString(UtilLag.getPing(p))
                            .replaceAll("%check%", this.getName())))).create()));
            staff.spigot().sendMessage(alertMessage);
        }
        if (vl > this.getMaxViolations() - 1 && this.isBannable()) {
            bannedPlayers.put(p.getUniqueId(), Check.this);
            clearViolations(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player staff : Vulcan.alertsEnabled) {
                        for (String punishmentMessage : getConfig().getStringList("punishments.message")) {
                            staff.sendMessage(UtilColor.translate(punishmentMessage.replaceAll("%player%", p.getName())
                                    .replaceAll("%info%", info)
                                    .replaceAll("%prefix%", getConfig().getString("prefix"))
                                    .replaceAll("%check%", Check.this.getName())
                                    .replaceAll("%vl%", Integer.toString(Check.this.getMaxViolations()))
                                    .replaceAll("%tps%", Double.toString(UtilMath.trim(2, UtilLag.getTPS())))
                                    .replaceAll("%ping%", Integer.toString(UtilLag.getPing(p)))));
                        }
                    }
                    for (String broadcastMessages : getConfig().getStringList("punishments.broadcast")) {
                        Bukkit.broadcastMessage(UtilColor.translate(broadcastMessages.replaceAll("%player%", p.getName())
                                .replaceAll("%check%", Check.this.getName())));
                    }
                    if (!getConfig().getStringList("punishments.commands").equals("")) {
                        for (String punishmentCommands : getConfig().getStringList("punishments.commands")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), UtilColor.translate(punishmentCommands.replaceAll("%player%", p.getName())
                                    .replaceAll("%check%", Check.this.getName())));
                        }
                    }
                    Bukkit.getScheduler().runTaskLater(Vulcan.instance, () -> bannedPlayers.remove(p.getUniqueId()), 20);
                }
            }.runTask(Vulcan.instance);
        }
    }

    public boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    private FileConfiguration getChecksConfig(){
        return UtilConfig.getInstance().getChecksConfig();
    }

    private FileConfiguration getConfig(){
        return UtilConfig.getInstance().getConfig();
    }

    private void clearViolations(Player p) {
        violations.remove(p.getUniqueId());
    }

    public DataManager getDataManager() {
        return Vulcan.instance.getDataManager();
    }
}

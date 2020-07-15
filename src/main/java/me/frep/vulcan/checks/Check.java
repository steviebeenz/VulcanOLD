package me.frep.vulcan.checks;

import io.github.retrooper.packetevents.event.PacketListener;
import me.frep.vulcan.Vulcan;
import me.frep.vulcan.utilities.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Check implements Listener, PacketListener {

    private Map<UUID, Map<Check, Integer>> violations = new HashMap<>();
    public Map<UUID, Map<Check, Long>> violationReset = new HashMap<>();

    private String identifier;
    private String name;
    private CheckType checkType;
    public static Vulcan vulcan;
    private boolean enabled;
    private boolean bannable;
    private Integer maxViolations;
    private long violationResetTime;

    public Check(String identifier, String name, CheckType checkType, Vulcan vulcan) {
        this.identifier = identifier;
        this.name = name;
        this.checkType = checkType;
        this.vulcan = vulcan;
        this.enabled = UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + getType() + "." + getIdentifier() + ".enabled");
        this.bannable = UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + getType() + "." + getIdentifier() + ".bannable");
        this.maxViolations =  UtilConfig.getInstance().getChecksConfig().getInt("checks." + getType() + "." + getIdentifier() + ".maxViolations");
        this.violationResetTime =  UtilConfig.getInstance().getChecksConfig().getLong("checks." + getType() + "." + getIdentifier() + ".violationResetTime");
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

    public Long getViolationResetTime() {
        return this.violationResetTime;
    }

    public void setEnabled(boolean enabled) {
        UtilConfig.getInstance().getChecksConfig().set("checks." + getType() + "." + getIdentifier() + ".enabled", enabled);
        UtilConfig.getInstance().saveChecksConfig();
        this.enabled = enabled;
    }

    public void setViolationResetTime(long violationResetTime) {
        this.violationResetTime = violationResetTime;
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

    public void setViolationResetTime(Check check, Player p, long time) {
        Map<Check, Long> map = new HashMap<>();
        if (violationReset.containsKey(p.getUniqueId())) {
            map = violationReset.get(p.getUniqueId());
        }
        map.put(check, time);
        violationReset.put(p.getUniqueId(), map);
    }

    public void flag(Player p, String info) {
        if (p.hasPermission("vulcan.bypass") || !isEnabled()) return;
        addViolation(this, p);
        setViolationResetTime(this, p, UtilTime.timeNow() + this.getViolationResetTime());
        int vl = getViolations(this, p);
        for (Player staff : Vulcan.alertsEnabled) {
            TextComponent alertMessage = new TextComponent(UtilColor.translate(UtilConfig.getInstance().getConfig().getString("alerts.message")
                    .replaceAll("%player%", p.getName())
                    .replaceAll("%vl%", Integer.toString(vl))
                    .replaceAll("%check%", this.getName())
                    .replaceAll("%prefix%", UtilConfig.getInstance().getConfig().getString("prefix"))
                    .replaceAll("%tps%", Double.toString(UtilMath.trim(2, UtilLag.getTPS())))
                    .replaceAll("%ping%", Integer.toString(UtilLag.getPing(p)))));
            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    UtilColor.translate(UtilConfig.getInstance().getConfig().getString("alerts.click-command")
                        .replaceAll("%player%", p.getName()))));
            StringBuilder builder = new StringBuilder();
            int listSize = UtilConfig.getInstance().getConfig().getStringList("alerts.hover").size();
            int i = 1;
            for (String hoverMessages : UtilConfig.getInstance().getConfig().getStringList("alerts.hover")) {
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
            if (UtilConfig.getInstance().getConfig().getBoolean("alerts.show-in-console")) {
                System.out.println(UtilConfig.getInstance().getConfig().getString("alerts.console-message")
                        .replaceAll("%player%", p.getName())
                        .replaceAll("%info%", info)
                        .replaceAll("%check%", this.getName())
                        .replaceAll("%vl%", Integer.toString(vl))
                        .replaceAll("%tps%", Double.toString(UtilMath.trim(2, UtilLag.getTPS())))
                        .replaceAll("%ping%", Integer.toString(UtilLag.getPing(p))));
            }
            staff.spigot().sendMessage(alertMessage);
        }
    }
}

package me.frep.vulcan.utilities;

import io.github.retrooper.packetevents.packet.PacketType;
import me.frep.vulcan.Vulcan;
import me.frep.vulcan.checks.Check;
import me.frep.vulcan.checks.CheckType;
import net.md_5.bungee.api.ChatColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.*;

public class UtilGUI implements Listener {

    public UtilGUI() {}
    private static UtilGUI instance = new UtilGUI();
    public static UtilGUI getInstance() {
        return instance;
    }

    private List<String> noLore = new ArrayList<>();
    private ItemStack glass = item(UMaterial.valueOf("GRAY_STAINED_GLASS_PANE").getItemStack(), "&r", noLore);
    private ItemStack back = item(UMaterial.valueOf("REDSTONE").getItemStack(), "&cBack", noLore);

    String version = Vulcan.instance.getDescription().getVersion();

    private Inventory vulcanMain;

    private Inventory setMaxVLCheckTypes;
    private Inventory setMaxVLMovementChecks;
    private Inventory setMaxVLPlayerChecks;
    private Inventory setMaxVLOtherChecks;

    private Inventory toggleCheckTypes;
    private Map<Integer, Inventory> toggleCombatChecks = new HashMap<>();
    private Map<UUID, Integer> currentPage = new HashMap<>();

    public void initializeMain() {
        vulcanMain = Bukkit.createInventory(null, 27, UtilColor.translate("&cVulcan AntiCheat"));
        for (int i = 0; i < 27; i++) vulcanMain.setItem(i, glass);
        List<String> infoLore = new ArrayList<>();
        infoLore.add(UtilColor.translate("&r"));
        infoLore.add(UtilColor.translate("&cVulcan AntiCheat v" + version + " by frep"));
        infoLore.add(UtilColor.translate("&cDiscord: frep#2778"));
        infoLore.add(UtilColor.translate("&cSupport Discord: https://discord.gg/2ucUSUq"));
        vulcanMain.setItem(13, item(UMaterial.valueOf("BOOK").getItemStack(), "&cInformation", infoLore));
        List<String> checksLore = new ArrayList<>();
        checksLore.add(UtilColor.translate("&7Toggle checks and automatic bans"));
        vulcanMain.setItem(10, item(UMaterial.valueOf("ENDER_EYE").getItemStack(), "&7Checks", checksLore));
        List<String> maxVLLore = new ArrayList<>();
        maxVLLore.add(UtilColor.translate("Set max violation of checks"));
        vulcanMain.setItem(11, item(UMaterial.valueOf("ANVIL").getItemStack(), "&7Set Max Violations", maxVLLore));
    }

    public void initializeSetMaxVLCheckTypes() {
        setMaxVLCheckTypes = Bukkit.createInventory(null, 27, UtilColor.translate("&c[Set Max VL] Check Types"));
        for (int i = 0; i < 27; i++) setMaxVLCheckTypes.setItem(i, glass);
        setMaxVLCheckTypes.setItem(10, item(UMaterial.valueOf("DIAMOND_SWORD").getItemStack(), "&cCombat Checks", noLore));
        setMaxVLCheckTypes.setItem(12, item(UMaterial.valueOf("FEATHER").getItemStack(), "&cMovement Checks", noLore));
        setMaxVLCheckTypes.setItem(14, item(UMaterial.valueOf("STONE").getItemStack(), "&cPlayer Checks", noLore));
        setMaxVLCheckTypes.setItem(16, item(UMaterial.valueOf("NAME_TAG").getItemStack(), "&cOther Checks", noLore));
        setMaxVLCheckTypes.setItem(22, item(UMaterial.valueOf("ARROW").getItemStack(), "&cGo Back", noLore));
    }

    public void initializeToggleCheckTypes() {
        toggleCheckTypes = Bukkit.createInventory(null, 27, UtilColor.translate("&c[Toggle] Check Types"));
        for (int i = 0; i < 27; i++) toggleCheckTypes.setItem(i, glass);
        toggleCheckTypes.setItem(10, item(UMaterial.valueOf("DIAMOND_SWORD").getItemStack(), "&cCombat Checks", noLore));
        toggleCheckTypes.setItem(12, item(UMaterial.valueOf("FEATHER").getItemStack(), "&cMovement Checks", noLore));
        toggleCheckTypes.setItem(14, item(UMaterial.valueOf("STONE").getItemStack(), "&cPlayer Checks", noLore));
        toggleCheckTypes.setItem(16, item(UMaterial.valueOf("NAME_TAG").getItemStack(), "&cOther Checks", noLore));
        toggleCheckTypes.setItem(22, item(UMaterial.valueOf("ARROW").getItemStack(), "&cGo Back", noLore));
    }

    public void initializeSetMaxVLMovement() {
        setMaxVLMovementChecks = Bukkit.createInventory(null, 54, UtilColor.translate("&c[Set Max VL] Movement Checks"));
        int i = 0;
        for (Check check : Vulcan.checks) {
            if (check.getType().equals(CheckType.MOVEMENT)) {
                if (check.isEnabled()) {
                    setMaxVLMovementChecks.setItem(i, item(UMaterial.valueOf("GREEN_STAINED_GLASS_PANE").getItemStack(), UtilColor.translate("&a" + check.getName()), noLore));
                } else {
                    setMaxVLMovementChecks.setItem(i, item(UMaterial.valueOf("RED_STAINED_GLASS_PANE").getItemStack(), UtilColor.translate("&c" + check.getName()), noLore));
                }
                i++;
            }
        }
    }

    public void initializeSetMaxVLPlayer() {
        setMaxVLPlayerChecks = Bukkit.createInventory(null, 54, UtilColor.translate("&c[Set Max VL] Player Checks"));
        int i = 0;
        for (Check check : Vulcan.checks) {
            if (check.getType().equals(CheckType.PLAYER)) {
                if (check.isEnabled()) {
                    setMaxVLPlayerChecks.setItem(i, item(UMaterial.valueOf("GREEN_STAINED_GLASS_PANE").getItemStack(), UtilColor.translate("&a" + check.getName()), noLore));
                } else {
                    setMaxVLPlayerChecks.setItem(i, item(UMaterial.valueOf("RED_STAINED_GLASS_PANE").getItemStack(), UtilColor.translate("&c" + check.getName()), noLore));
                }
                i++;
            }
        }
    }

    public void initializeSetMaxVLOther() {
        setMaxVLOtherChecks = Bukkit.createInventory(null, 54, UtilColor.translate("&c[Set Max VL] Other Checks"));
        int i = 0;
        for (Check check : Vulcan.checks) {
            if (check.getType().equals(CheckType.OTHER)) {
                if (check.isEnabled()) {
                    setMaxVLOtherChecks.setItem(i, item(UMaterial.valueOf("GREEN_STAINED_GLASS_PANE").getItemStack(), UtilColor.translate("&a" + check.getName()), noLore));
                } else {
                    setMaxVLOtherChecks.setItem(i, item(UMaterial.valueOf("RED_STAINED_GLASS_PANE").getItemStack(), UtilColor.translate("&c" + check.getName()), noLore));
                }
                i++;
            }
        }
    }

    public void initializeToggleCombatChecks() {
        int get = 0;
        List<Check> checksList = new ArrayList<>();
        int combatChecks = 0;
        for (Check check : Vulcan.instance.checks) {
            if (check.getType().equals(CheckType.COMBAT)) {
                combatChecks++;
                checksList.add(check);
            }
        }
        int maxP = (int) Math.ceil((double) combatChecks / (double) 45);
        for (int page = 1; page < maxP + 1; page++) {
            Inventory inv = Bukkit.createInventory(null, 54, UtilColor.translate("&c[Toggle] Combat Checks [" + page + "]"));
            int slot = 0;
            for (int items = 0; items < 45; items++) {
                if (get >= combatChecks) break;
                Check check = checksList.get(get);
                if (UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".enabled")
                        && UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".bannable")) {
                    inv.setItem(slot, enabledItem(check));
                }
                if (!UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".enabled")
                        && !UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".bannable")) {
                    inv.setItem(slot, disabledItem(check));
                }
                if (UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".enabled")
                        && !UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".bannable")) {
                    inv.setItem(slot, enabledDisabledItem(check));
                }
                if (!UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".enabled")
                        && UtilConfig.getInstance().getChecksConfig().getBoolean("checks." + check.getType() + "." + check.getIdentifier() + ".bannable")) {
                    inv.setItem(slot, disabledEnabledItem(check));
                }
                slot++;
                get++;
            }
            for (int i = slot; i < 45; i++) {
                ItemStack c = item(UMaterial.valueOf("GRAY_STAINED_GLASS_PANE").getItemStack(), "&r", noLore);
                inv.setItem(i, c);
            }
            ItemStack previous = item(UMaterial.valueOf("ARROW").getItemStack(), "&cPrevious Page", noLore);
            inv.setItem(46, previous);
            ItemStack next = item(UMaterial.valueOf("ARROW").getItemStack(), "&aNext Page", noLore);
            inv.setItem(51, next);
            inv.setItem(53, back);
            toggleCombatChecks.put(page, inv);
        }
    }

    public void openVulcanMain(Player p) {
        if (vulcanMain == null) initializeMain();
        p.openInventory(vulcanMain);
    }

    public void openCheckTypesSetMaxVL(Player p) {
        if (setMaxVLCheckTypes == null) initializeSetMaxVLCheckTypes();
        p.openInventory(setMaxVLCheckTypes);
    }

    public void openSetVLMovement(Player p) {
        if (setMaxVLMovementChecks == null) initializeSetMaxVLMovement();
        p.openInventory(setMaxVLMovementChecks);
    }

    public void openSetVLPlayer(Player p) {
        if (setMaxVLPlayerChecks == null) initializeSetMaxVLPlayer();
        p.openInventory(setMaxVLPlayerChecks);
    }

    public void openSetVLOther(Player p) {
        if (setMaxVLOtherChecks == null) initializeSetMaxVLOther();
        p.openInventory(setMaxVLOtherChecks);
    }

    public void openCheckTypesToggle(Player p) {
        if (toggleCheckTypes == null) initializeToggleCheckTypes();
        p.openInventory(toggleCheckTypes);
    }

    public void openToggleCombat(Player p, int page) {
        if (!toggleCombatChecks.containsKey(page - 1)) initializeToggleCombatChecks();
        currentPage.put(p.getUniqueId(), page);
        p.openInventory(toggleCombatChecks.get(page));
    }

    public ItemStack item(ItemStack m, String displayName, List<String> lore) {
        ItemStack i = m;
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(UtilColor.translate(displayName));
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }

    public ItemStack enabledItem(Check check) {
        ItemStack i = UMaterial.valueOf("GREEN_STAINED_GLASS_PANE").getItemStack();
        List<String> lore = new ArrayList<>();
        lore.add(UtilColor.translate("&7Enabled: &2&l✔"));
        lore.add(UtilColor.translate("&7Bannable: &2&l✔"));
        lore.add(UtilColor.translate("&r"));
        lore.add(UtilColor.translate("&7Actions:"));
        lore.add(UtilColor.translate(" &8» &7Left-Click: Enable/Disable"));
        lore.add(UtilColor.translate(" &8» &7Right-Click: Enable/Disable bannable"));
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(UtilColor.translate("&a" + check.getName()));
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }

    public ItemStack disabledItem(Check check) {
        ItemStack i = UMaterial.valueOf("RED_STAINED_GLASS_PANE").getItemStack();
        List<String> lore = new ArrayList<>();
        lore.add(UtilColor.translate("&7Enabled: &4&l✘"));
        lore.add(UtilColor.translate("&7Bannable: &4&l✘"));
        lore.add(UtilColor.translate("&r"));
        lore.add(UtilColor.translate("&7Actions:"));
        lore.add(UtilColor.translate(" &8» &7Left-Click: Enable/Disable"));
        lore.add(UtilColor.translate(" &8» &7Right-Click: Enable/Disable bannable"));
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(UtilColor.translate("&c" + check.getName()));
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }

    public ItemStack enabledDisabledItem(Check check) {
        ItemStack i = UMaterial.valueOf("ORANGE_STAINED_GLASS_PANE").getItemStack();
        List<String> lore = new ArrayList<>();
        lore.add(UtilColor.translate("&7Enabled: &2&l✔"));
        lore.add(UtilColor.translate("&7Bannable: &4&l✘"));
        lore.add(UtilColor.translate("&r"));
        lore.add(UtilColor.translate("&7Actions:"));
        lore.add(UtilColor.translate(" &8» &7Left-Click: Enable/Disable"));
        lore.add(UtilColor.translate(" &8» &7Right-Click: Enable/Disable bannable"));
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(UtilColor.translate("&c" + check.getName()));
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }

    public ItemStack disabledEnabledItem(Check check) {
        ItemStack i = UMaterial.valueOf("ORANGE_STAINED_GLASS_PANE").getItemStack();
        List<String> lore = new ArrayList<>();
        lore.add(UtilColor.translate("&7Enabled: &4&l✘"));
        lore.add(UtilColor.translate("&7Bannable: &2&l✔"));
        lore.add(UtilColor.translate("&r"));
        lore.add(UtilColor.translate("&7Actions:"));
        lore.add(UtilColor.translate(" &8» &7Left-Click: Enable/Disable"));
        lore.add(UtilColor.translate(" &8» &7Right-Click: Enable/Disable bannable"));
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(UtilColor.translate("&c" + check.getName()));
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        if (e.getView().getTitle().equals(UtilColor.translate("&cVulcan AntiCheat"))) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.ANVIL)) openCheckTypesSetMaxVL(p);
            if (e.getCurrentItem().getType().equals(UMaterial.valueOf("ENDER_EYE").getItemStack().getType())) openCheckTypesToggle(p);
        }
        if (e.getView().getTitle().equals(UtilColor.translate("&c[Toggle] Check Types"))) {
            if (e.getCurrentItem().getType().equals(Material.ARROW)) openVulcanMain(p);
            if (e.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) openToggleCombat(p, 1);
            e.setCancelled(true);
        }
        if (e.getView().getTitle().startsWith(UtilColor.translate("&c[Toggle] Combat Checks"))) {
            e.setCancelled(true);
            Check check = getCheckByName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("Type", "").replaceAll("[#]", "")));
            if (check == null || e.getCurrentItem() == null) return;
            if (e.getCurrentItem().equals(enabledItem(check))) {
                Bukkit.broadcastMessage("enabled item");
                if (e.getClick().equals(ClickType.RIGHT)) {
                    UtilConfig.getInstance().getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".bannable", false);
                    UtilConfig.getInstance().saveChecksConfig();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            initializeToggleCombatChecks();
                            p.openInventory(toggleCombatChecks.get(currentPage.get(p.getUniqueId())));
                        }
                    }.runTaskLater(Vulcan.instance, 5);
                }
            }
            if (e.getCurrentItem().equals(disabledItem(check))) {
                Bukkit.broadcastMessage("disabld item");
                if (e.getClick().equals(ClickType.LEFT)) {
                    e.setCurrentItem(enabledDisabledItem(check));
                    UtilConfig.getInstance().getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".enabled", true);
                    UtilConfig.getInstance().saveChecksConfig();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            initializeToggleCombatChecks();
                            p.openInventory(toggleCombatChecks.get(currentPage.get(p.getUniqueId())));
                        }
                    }.runTaskLater(Vulcan.instance, 5);
                }
            }
            if (e.getCurrentItem().equals(enabledDisabledItem(check))) {
                Bukkit.broadcastMessage("enabledDisabledItem");
                if (e.getAction().equals(ClickType.LEFT)) {
                    e.setCurrentItem(disabledItem(check));
                    UtilConfig.getInstance().getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".enabled", false);
                    UtilConfig.getInstance().getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".bannable", false);
                    UtilConfig.getInstance().saveChecksConfig();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            initializeToggleCombatChecks();
                            p.openInventory(toggleCombatChecks.get(currentPage.get(p.getUniqueId())));
                        }
                    }.runTaskLater(Vulcan.instance, 5);
                }
                if (e.getAction().equals(ClickType.RIGHT)) {
                    e.setCurrentItem(enabledItem(check));
                    UtilConfig.getInstance().getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".bannable", true);
                    UtilConfig.getInstance().saveChecksConfig();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            initializeToggleCombatChecks();
                            p.openInventory(toggleCombatChecks.get(currentPage.get(p.getUniqueId())));
                        }
                    }.runTaskLater(Vulcan.instance, 5);
                }
            }
            if (e.getCurrentItem().equals(disabledEnabledItem(check))) {
                Bukkit.broadcastMessage("disabled enabled item");
                if (e.getClick().equals(ClickType.LEFT)) {
                    e.setCurrentItem(enabledItem(check));
                    UtilConfig.getInstance().getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".enabled", true);
                    UtilConfig.getInstance().saveChecksConfig();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            initializeToggleCombatChecks();
                            p.openInventory(toggleCombatChecks.get(currentPage.get(p.getUniqueId())));
                        }
                    }.runTaskLater(Vulcan.instance, 5);
                }
                if (e.getClick().equals(ClickType.RIGHT)) {
                    e.setCurrentItem(disabledItem(check));
                    UtilConfig.getInstance().getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".bannable", false);
                    UtilConfig.getInstance().saveChecksConfig();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            initializeToggleCombatChecks();
                            p.openInventory(toggleCombatChecks.get(currentPage.get(p.getUniqueId())));
                        }
                    }.runTaskLater(Vulcan.instance, 5);
                }
            }
        }
        if (e.getView().getTitle().equals(UtilColor.translate("&c[Set Max VL] Check Types"))) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.FEATHER)) openSetVLMovement(p);
            if (e.getCurrentItem().getType().equals(Material.STONE)) openSetVLPlayer(p);
            if (e.getCurrentItem().getType().equals(Material.NAME_TAG)) openSetVLOther(p);
            if (e.getCurrentItem().getType().equals(Material.ARROW)) openVulcanMain(p);
        }
        if (e.getView().getTitle().equals(UtilColor.translate("&c[Set Max VL] Combat Checks"))) {
            e.setCancelled(true);
            new AnvilGUI.Builder().onComplete((player, text) -> {
                        if(text.equalsIgnoreCase("you")) {
                            player.sendMessage("You have magical powers!");
                            return AnvilGUI.Response.close();
                        } else {
                            return AnvilGUI.Response.text("Incorrect.");
                        }
                    })
                    .preventClose()
                    .text("Max Violations:")
                    .item(new ItemStack(UMaterial.valueOf("PAPER").getItemStack().getType()))
                    .title("Enter your answer.")
                    .plugin(Vulcan.instance)
                    .open(p);
        }
    }

    public Check getCheckByName(String string) {
        for (Check check : Vulcan.instance.checks) {
            if (check.getIdentifier().equalsIgnoreCase(string)) {
                return check;
            }
        }
        return null;
    }
}

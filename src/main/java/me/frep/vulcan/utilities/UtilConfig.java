package me.frep.vulcan.utilities;

import me.frep.vulcan.Vulcan;
import me.frep.vulcan.checks.Check;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class UtilConfig {

    public UtilConfig() {}
    private static UtilConfig instance = new UtilConfig();
    public static UtilConfig getInstance() {
        return instance;
    }

    private File configFile;
    private FileConfiguration config;

    private File checksFile;
    private FileConfiguration checksConfig;

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getChecksConfig() {
        return checksConfig;
    }

    public void generateConfig() {
        configFile = new File(Vulcan.instance.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            Vulcan.instance.saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void generateChecksConfig() {
        checksFile = new File(Vulcan.instance.getDataFolder(), "checks.yml");
        if (!checksFile.exists()) {
            checksFile.getParentFile().mkdirs();
            Vulcan.instance.saveResource("checks.yml", false);
        }
        checksConfig = new YamlConfiguration();
        try {
            checksConfig.load(checksFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        for (Check check : Vulcan.checks) {
            if (!getChecksConfig().isConfigurationSection("checks." + check.getType() + "." + check.getIdentifier())) {
                getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".enabled", check.isEnabled());
                getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".bannable", check.isBannable());
                getChecksConfig().set("checks." + check.getType() + "." + check.getIdentifier() + ".maxViolations", check.getMaxViolations());
                saveChecksConfig();
            }
        }
        getConfig().options().copyDefaults(true);
        saveChecksConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveChecksConfig() {
        try {
            checksConfig.save(checksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
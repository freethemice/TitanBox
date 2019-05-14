package com.firesoftitan.play.titanbox.managers;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static Config config = new Config("plugins" + File.separator + "TitanBox" + File.separator  + "config.yml");
    private static int modules_SecondarySize = 5;
    private static int router_BufferSize = 10;
    private static int router_Speed = 1;
    private static int router_BigMax = 45;
    private static long router_LagTime = 250;
    private static boolean defaultBlock = false;
    private static boolean adminBlock= false;
    private static boolean opBlock = false;
    private static boolean stoneBreak = false;
    private static boolean lag_items_enabled = true;
    private static boolean lag_monsters_enabled = true;
    private static boolean remove_androids = true;
    private static boolean remove_cargo = true;
    private static int lag_items_max = 150;
    private static int lag_monsters_max = 1000;
    private static boolean removeUnnecessaryDamageTags = false;
    private static String adminAlerts = "";
    private static List<String> playerDeathCommands = new ArrayList<String>();
    private static List<String> plugingCommands = new ArrayList<String>();
    private static List<String> oreBreakCommands = new ArrayList<String>();
    private static List<String> defaultCommands = new ArrayList<String>();
    private static List<String> adminCommands = new ArrayList<String>();
    private static List<String> opCommands = new ArrayList<String>();
    public static void reload()
    {
        config.reload();
    }
    public static void save()
    {
        config.save();
    }
    public static void loadConfig()
    {

        if (!ConfigManager.config.contains("settings.slimefun.remove_androids"))
        {
            ConfigManager.config.setValue("settings.slimefun.remove_androids", remove_androids);
        }
        remove_androids = ConfigManager.config.getBoolean("settings.slimefun.remove_androids");

        if (!ConfigManager.config.contains("settings.slimefun.remove_cargo"))
        {
            ConfigManager.config.setValue("settings.slimefun.remove_cargo", remove_androids);
        }
        remove_cargo = ConfigManager.config.getBoolean("settings.slimefun.remove_cargo");

        if (!ConfigManager.config.contains("settings.module.linking.secondarysize"))
        {
            ConfigManager.config.setValue("settings.module.linking.secondarysize", modules_SecondarySize);
        }
        modules_SecondarySize = ConfigManager.config.getInt("settings.module.linking.secondarysize");

        if (!ConfigManager.config.contains("settings.router.bigMax"))
        {
            ConfigManager.config.setValue("settings.router.bigMax", router_BigMax);
        }
        if (!ConfigManager.config.contains("settings.router.speed"))
        {
            ConfigManager.config.setValue("settings.router.speed", router_Speed);
        }
        if (!ConfigManager.config.contains("settings.router.bufferSize"))
        {
            ConfigManager.config.setValue("settings.router.bufferSize", router_BufferSize);
        }
        if (!ConfigManager.config.contains("settings.router.lagtime"))
        {
            ConfigManager.config.setValue("settings.router.lagtime", router_LagTime);
        }
        router_BigMax = ConfigManager.config.getInt("settings.router.bigMax");
        router_Speed = ConfigManager.config.getInt("settings.router.speed");
        router_BufferSize = ConfigManager.config.getInt("settings.router.bufferSize");
        router_LagTime = ConfigManager.config.getLong("settings.router.lagtime");

        if (!config.contains("settings.commands.plugins"))
        {
            plugingCommands.add("/qs silent");
            plugingCommands.add("/qs price");
            config.setValue("settings.commands.plugins.bypass", plugingCommands);
        }
        if (!config.contains("settings.commands.default"))
        {
            defaultCommands.add("/home");
            defaultCommands.add("/spawn");
            defaultCommands.add("/message");
            defaultCommands.add("/msg");
            defaultCommands.add("/w");
            defaultCommands.add("/replay");
            defaultCommands.add("/r");
            defaultCommands.add("/sf guide");
            config.setValue("settings.commands.default.block", false);
            config.setValue("settings.commands.default.bypass", defaultCommands);
        }
        if (!config.contains("settings.commands.admin")) {
            adminCommands.addAll(defaultCommands);
            adminCommands.add("/poll");
            adminCommands.add("/tp");
            adminCommands.add("/announcement");
            adminCommands.add("/broadcast");
            adminCommands.add("/warning");
            adminCommands.add("/reboot");
            adminCommands.add("/override");
            adminCommands.add("/reply");
            adminCommands.add("/message");
            adminCommands.add("/es");
            adminCommands.add("/mutechat");
            adminCommands.add("/socialspy");
            adminCommands.add("/staffchat");
            adminCommands.add("/sc");
            adminCommands.add("/ss");
            adminCommands.add("/clearchat");
            adminCommands.add("/commandspy");
            adminCommands.add("/cs");
            adminCommands.add("/formats");
            adminCommands.add("/ping");
            adminCommands.add("/warn");
            adminCommands.add("/warninfo");
            adminCommands.add("/categories");
            adminCommands.add("/givepaper");
            adminCommands.add("/openinv");
            adminCommands.add("/openender");
            adminCommands.add("/searchinv");
            adminCommands.add("/searchender");
            adminCommands.add("/searchenchant");
            adminCommands.add("/anycontainer");
            adminCommands.add("/silentcontainer");
            adminCommands.add("/announce");
            adminCommands.add("/reports");
            adminCommands.add("/matrix");
            config.setValue("settings.commands.admin.block", false);
            config.setValue("settings.commands.admin.bypass", adminCommands);
        }
        if (!config.contains("settings.commands.op"))
        {
            opCommands.addAll(adminCommands);
            opCommands.add("/gamemode");
            opCommands.add("/deop");
            config.setValue("settings.commands.op.block", false);
            config.setValue("settings.commands.op.bypass", opCommands);
        }
        if (!config.contains("settings.commands.ore"))
        {
            oreBreakCommands.add("coins drop <x>,<y>,<z>,<world> <amount> 1");
            config.setValue("settings.commands.ore.break", oreBreakCommands);
            config.setValue("settings.commands.ore.stone", false);
        }

        if (!config.contains("settings.commands.player.death"))
        {
            playerDeathCommands.add("coins drop <name> <amount> 1");
            playerDeathCommands.add("coins drop <uuid> <amound> 1");
            playerDeathCommands.add("@player &4You lost &f<amount> &4coins. &7(abount $7 each)");
            config.setValue("settings.commands.player.death", playerDeathCommands);
        }

        if (!config.contains("settings.removeUnnecessaryDamageTags")) {
            config.setValue("settings.removeUnnecessaryDamageTags", true);
        }
        if (!config.contains("settings.alerts.user")) {
            config.setValue("settings.alerts.user", "username");
        }
        if (!config.contains("settings.lag.items.enabled")) {
            config.setValue("settings.lag.items.enabled", true);
        }
        if (!config.contains("settings.lag.items.max")) {
            config.setValue("settings.lag.items.max", 150);
        }
        if (!config.contains("settings.lag.monsters.enabled")) {
            config.setValue("settings.lag.monsters.enabled", true);
        }
        if (!config.contains("settings.lag.monsters.max")) {
            config.setValue("settings.lag.monsters.max", 1000);
        }
        removeUnnecessaryDamageTags = config.getBoolean("settings.removeUnnecessaryDamageTags");
        plugingCommands = config.getStringList("settings.commands.plugins.bypass");
        defaultBlock = config.getBoolean("settings.commands.default.block");
        defaultCommands = config.getStringList("settings.commands.default.bypass");
        adminBlock = config.getBoolean("settings.commands.admin.block");
        adminCommands = config.getStringList("settings.commands.admin.bypass");
        opBlock = config.getBoolean("settings.commands.op.block");
        opCommands = config.getStringList("settings.commands.op.bypass");
        oreBreakCommands = config.getStringList("settings.commands.ore.break");
        playerDeathCommands = config.getStringList("settings.commands.player.death");
        stoneBreak = config.getBoolean("settings.commands.ore.stone");
        adminAlerts = config.getString("settings.alerts.user");
        lag_items_enabled = config.getBoolean("settings.lag.items.enabled");
        lag_items_max = config.getInt("settings.lag.items.max");
        lag_monsters_enabled = config.getBoolean("settings.lag.monsters.enabled");
        lag_monsters_max = config.getInt("settings.lag.monsters.max");
        ConfigManager.config.save();

    }

    public static boolean isRemove_androids() {
        return remove_androids;
    }

    public static boolean isRemove_cargo() {
        return remove_cargo;
    }

    public static boolean isLag_items_enabled() {
        return lag_items_enabled;
    }

    public static boolean isLag_monsters_enabled() {
        return lag_monsters_enabled;
    }

    public static int getLag_monsters_max() {
        return lag_monsters_max;
    }

    public static int getLag_items_max() {
        return lag_items_max;
    }

    public static boolean isAdminBlock() {
        return adminBlock;
    }

    public static boolean isDefaultBlock() {
        return defaultBlock;
    }

    public static boolean isOpBlock() {
        return opBlock;
    }

    public static boolean isRemoveUnnecessaryDamageTags() {
        return removeUnnecessaryDamageTags;
    }

    public static boolean isStoneBreak() {
        return stoneBreak;
    }

    public static List<String> getPlayerDeathCommands() {
        return playerDeathCommands;
    }

    public static List<String> getOreBreakCommands() {
        return oreBreakCommands;
    }

    public static List<String> getPlugingCommands() {
        return plugingCommands;
    }

    public static List<String> getDefaultCommands() {
        return defaultCommands;
    }

    public static String getAdminAlerts() {
        return adminAlerts;
    }

    public static List<String> getAdminCommands() {
        return adminCommands;
    }

    public static List<String> getOpCommands() {
        return opCommands;
    }

    public static int getRouter_BigMax() {
        return router_BigMax;
    }

    public static int getRouter_Speed() {
        return router_Speed;
    }

    public static int getRouter_BufferSize() {
        return router_BufferSize;
    }

    public static long getRouter_LagTime() {
        return router_LagTime;
    }

    public static int getModules_SecondarySize() {
        return modules_SecondarySize;
    }
}

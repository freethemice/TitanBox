package com.firesoftitan.play.titanbox.enums;
import com.firesoftitan.play.titanbox.TitanBox;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public enum RanksEnum {
    RANK_1(1, "rank_1", 1, 45, 100, 25, 0.5f, 2),
    RANK_2(2, "rank_2", 1, 100, 150, 50, 1.0f, 2),
    RANK_3(3, "rank_3", 2, 150, 200, 75, 1.5f, 5),
    RANK_4(4, "rank_4", 2, 200, 250, 100, 2.0f, 5),
    RANK_5(5, "rank_5", 3, 250, 300, 125, 2.5f, 8),
    RANK_6(6, "rank_6", 3, 300, 350, 150, 3.0f, 8),
    RANK_7(7, "rank_7", 4, 350, 400, 175, 3.5f, 11);
    private final String name;
    private final Integer value;
    private final Integer amount;
    private final Integer max;
    private final Integer min;
    private final Integer efficiency;
    private final double drops;
    private final Integer turrets;
    RanksEnum(int value, String name, int amount, int min, int max, int efficiency, double drops, int turrets) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        this.efficiency = efficiency;
        this.amount = amount;
        this.drops = drops;
        this.turrets = turrets;
    }

    public double getDrops() {
        return drops;
    }

    public Integer getTurrets() {
        return turrets;
    }

    public static RanksEnum valueOf(int value)
    {
        for(RanksEnum buttonenum: RanksEnum.values())
        {
            if (buttonenum.value == value)
            {
                return buttonenum;
            }
        }
        return RanksEnum.RANK_1;
    }
    public static RanksEnum valueOf(UUID player)
    {
        return valueOf(Bukkit.getOfflinePlayer(player));
    }
    public static RanksEnum valueOf(OfflinePlayer player)
    {
        RegisteredServiceProvider<Permission> rsp = TitanBox.instants.getServer().getServicesManager().getRegistration(Permission.class);
        Permission permission = rsp.getProvider();
        int highest = 1;
        RanksEnum ranksEnumR = RanksEnum.RANK_1;
        for(RanksEnum ranksEnum: RanksEnum.values())
        {
            if (ranksEnum.value > highest) {
                if (permission.playerHas(null, player,"titanbox." + ranksEnum.name) ||
                        permission.playerHas("world", player,"titanbox." + ranksEnum.name)) {
                    ranksEnumR = ranksEnum;
                    highest = ranksEnum.value;
                }
            }
        }
        return ranksEnumR;
    }
    public static RanksEnum valueOf(Player player)
    {
        int highest = 1;
        RanksEnum ranksEnumR = RanksEnum.RANK_1;
        for(RanksEnum ranksEnum: RanksEnum.values())
        {
            if (ranksEnum.value > highest) {
                if (player.hasPermission("titanbox." + ranksEnum.name)) {
                    ranksEnumR = ranksEnum;
                    highest = ranksEnum.value;
                }
            }
        }
        return ranksEnumR;
    }
    public Integer getAmount() {
        return amount;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}

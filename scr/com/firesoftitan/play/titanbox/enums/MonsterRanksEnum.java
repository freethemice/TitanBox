package com.firesoftitan.play.titanbox.enums;

import com.firesoftitan.play.titanbox.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

public enum MonsterRanksEnum {
    DEFAULT(0, "Default", ChatColor.RESET, 0, 0),
    NORMAL(0, "Normal", ChatColor.DARK_BLUE, 1, 1),
    LUCKY(1, "Lucky", ChatColor.YELLOW, 10, 10),
    ECLIPSE(2, "Eclipse",ChatColor.AQUA, 14.2, 15),
    TITAN(3, "Titan", ChatColor.DARK_RED, 18.8, 20);

    private final String name;
    private final Integer index;
    private final ChatColor chatColor;
    private final Double damage;
    private final Double health;

    MonsterRanksEnum(int index, String name, ChatColor color, double damage, double health) {
        this.name = name;
        this.index = index;
        this.chatColor = color;
        this.damage = damage;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Double getDamage() {
        return damage;
    }

    public Double getHealth() {
        return health;
    }

    public Integer getIndex() {
        return index;
    }
    public static MonsterRanksEnum getMonsterType(Entity entity)
    {
        if (entity instanceof Monster)
        {
            Monster monster = (Monster)entity;
            if (monster.isCustomNameVisible()) {
                for (MonsterRanksEnum monsterRanksEnum : MonsterRanksEnum.values()) {
                    if (monster.getCustomName().contains(monsterRanksEnum.getName())) {
                        return monsterRanksEnum;
                    }
                }
            }
        }
        return MonsterRanksEnum.DEFAULT;
    }
    public static MonsterRanksEnum getRandomType()
    {
        Random random = new Random(System.currentTimeMillis());
        if (random.nextInt(1000) < 150)
        {
            return MonsterRanksEnum.NORMAL;
        }
        if (random.nextInt(1000) < 150)
        {
            return MonsterRanksEnum.LUCKY;
        }
        if (random.nextInt(1000) < 150)
        {
            return MonsterRanksEnum.ECLIPSE;
        }
        return MonsterRanksEnum.TITAN;
    }
    public static MonsterRanksEnum getGearLevel(ItemStack armor)
    {
        if (Utilities.isArmor(armor))
        {
            int maxLevel = 0;
            Map<Enchantment, Integer> enchants = armor.getEnchantments();
            for(Enchantment enchantment: enchants.keySet())
            {
                int level = armor.getEnchantmentLevel(enchantment);
                if (level > maxLevel)
                {
                    maxLevel = level;
                }

            }
            if (maxLevel < 10) return MonsterRanksEnum.NORMAL;
            if (maxLevel  < 13) return MonsterRanksEnum.LUCKY;
            if (maxLevel  < 23) return MonsterRanksEnum.ECLIPSE;
            return MonsterRanksEnum.TITAN;
        }
        return MonsterRanksEnum.NORMAL;
    }
}
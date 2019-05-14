package com.firesoftitan.play.titanbox.enums;

import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ItemToBlockEnum {
    QUARTZ(0, "QUARTZ", 4, new ItemStack(Material.QUARTZ), new ItemStack(Material.QUARTZ_BLOCK)),
    BRICK(1, "BRICK", 4, new ItemStack(Material.BRICK), new ItemStack(Material.BRICKS)),
    NETHER_BRICK(2, "NETHER_BRICK", 4, new ItemStack(Material.NETHER_BRICK), new ItemStack(Material.NETHER_BRICKS)),
    CLAY(3, "CLAY", 4, new ItemStack(Material.CLAY_BALL), new ItemStack(Material.CLAY)),
    SNOW(4, "SNOW", 4, new ItemStack(Material.SNOWBALL), new ItemStack(Material.SNOW_BLOCK)),
    GLOWSTONE(5, "GLOWSTONE", 4, new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.GLOWSTONE)),
    SAND(6, "SAND", 4, new ItemStack(Material.SAND), new ItemStack(Material.SANDSTONE)),
    RED_SAND(7, "RED_SAND", 4, new ItemStack(Material.RED_SAND), new ItemStack(Material.REDSTONE)),
    WOOL(8, "WOOL", 4, new ItemStack(Material.STRING), new ItemStack(Material.WHITE_WOOL)),
    COAL(9, "COAL", 9, new ItemStack(Material.COAL), new ItemStack(Material.COAL_BLOCK)),
    IRON(10, "IRON", 9, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_BLOCK)),
    GOLD(11, "GOLD", 9, new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_BLOCK)),
    DIAMOND(12, "DIAMOND", 9, new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND_BLOCK)),
    LAPIS_LAZULI(13, "LAPIS_LAZULI", 9, new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.LAPIS_BLOCK)),
    REDSTONE(14, "REDSTONE", 9, new ItemStack(Material.REDSTONE), new ItemStack(Material.REDSTONE_BLOCK)),
    EMERALD(15, "EMERALD", 9, new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD_BLOCK)),
    HAY(16, "HAY", 9, new ItemStack(Material.WHEAT), new ItemStack(Material.HAY_BLOCK)),
    MELON(17, "MELON", 9, new ItemStack(Material.MELON_SLICE), new ItemStack(Material.MELON)),
    SLIME(18, "SLIME", 9, new ItemStack(Material.SLIME_BALL), new ItemStack(Material.SLIME_BLOCK)),
    GOLD_24K(19, "GOLD_24K", 9, SlimefunItems.GOLD_24K, SlimefunItems.GOLD_24K_BLOCK),
    GOLD_NUGGET(20, "GOLD_NUGGET", 9, new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.GOLD_INGOT)),
    IRON_NUGGET(20, "IRON_NUGGET", 9, new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.IRON_INGOT));

    private final String name;
    private final Integer value;
    private final Integer count;
    private final ItemStack item;
    private final ItemStack block;
    ItemToBlockEnum(int value, String name, int count, ItemStack item, ItemStack block) {
        this.name = name;
        this.value = value;
        this.count = count;
        this.item = item;
        this.block = block;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public Integer getCount() {
        return count;
    }

    public ItemStack getBlock() {
        return block.clone();
    }
}

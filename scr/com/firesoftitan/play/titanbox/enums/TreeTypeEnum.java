package com.firesoftitan.play.titanbox.enums;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public enum TreeTypeEnum {
    OAK(TreeType.TREE, null, TreeType.BIG_TREE, "Oak", Material.OAK_LOG, Material.OAK_SAPLING, Material.STRIPPED_OAK_LOG),
    SPRUCE(TreeType.REDWOOD, TreeType.MEGA_REDWOOD, TreeType.TALL_REDWOOD, "Spruce", Material.SPRUCE_LOG, Material.SPRUCE_SAPLING, Material.STRIPPED_SPRUCE_LOG),
    BIRCH(TreeType.BIRCH, null, TreeType.TALL_BIRCH,"Birch", Material.BIRCH_LOG, Material.BIRCH_SAPLING, Material.STRIPPED_BIRCH_LOG),
    JUNGLE(TreeType.SMALL_JUNGLE, TreeType.JUNGLE, null, "Jungle", Material.JUNGLE_LOG,Material.JUNGLE_SAPLING, Material.STRIPPED_JUNGLE_LOG),
    ACACIA(TreeType.ACACIA, null, null, "Acacia", Material.ACACIA_LOG, Material.ACACIA_SAPLING, Material.STRIPPED_ACACIA_LOG),
    DARK_OAK(TreeType.DARK_OAK, null, null, "Dark Oak", Material.DARK_OAK_LOG,Material.DARK_OAK_SAPLING, Material.STRIPPED_DARK_OAK_LOG);

    private final TreeType type;
    private final TreeType bigtype;
    private final TreeType talltype;
    private final String name;
    private final Material log;
    private final Material sapling;
    private final Material stripped;
    TreeTypeEnum(TreeType treeType, TreeType big, TreeType tall,String name, Material material, Material sapling, Material stripped) {
        this.type = treeType;
        this.bigtype = big;
        this.talltype = tall;
        this.name = name;
        this.log = material;
        this.sapling = sapling;
        this.stripped = stripped;
    }
    public static TreeTypeEnum getFromMaterial(Material material, short data)
    {
        for(TreeTypeEnum tte: TreeTypeEnum.values())
        {
            if (material == tte.log)
            {
                return tte;
            }
            if (material == tte.getSapling())
            {
                return tte;
            }
        }
        return null;
    }

    public Material getStripped() {
        return stripped;
    }

    public Material getSapling() {
        return sapling;
    }

    public static TreeTypeEnum getFromBlock(Block block)
    {
        for(TreeTypeEnum tte: TreeTypeEnum.values())
        {
            if (block.getType() == tte.log)
            {
                return tte;
            }
            if (block.getType() == tte.getSapling())
            {
                return tte;
            }
        }
        return null;
    }
    public String getName() {
        return name;
    }
    public ItemStack getItemStack()
    {
        return new ItemStack(log, 1);
    }
    public Material getLog() {
        return log;
    }

    public TreeType getBigtype() {
        return bigtype;
    }

    public TreeType getTalltype() {
        return talltype;
    }

    public TreeType getType() {
        return type;
    }
}
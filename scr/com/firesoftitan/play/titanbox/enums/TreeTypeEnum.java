package com.firesoftitan.play.titanbox.enums;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public enum TreeTypeEnum {
    OAK(TreeType.TREE, null, TreeType.BIG_TREE, "Oak", Material.LOG,(short)0, (short)0),
    SPRUCE(TreeType.REDWOOD, TreeType.MEGA_REDWOOD, TreeType.TALL_REDWOOD, "Spruce", Material.LOG,(short)1, (short)1),
    BIRCH(TreeType.BIRCH, null, TreeType.TALL_BIRCH,"Birch", Material.LOG,(short)2, (short)2),
    JUNGLE(TreeType.SMALL_JUNGLE, TreeType.JUNGLE, null, "Jungle", Material.LOG,(short)3, (short)3),
    ACACIA(TreeType.ACACIA, null, null, "Acacia", Material.LOG_2,(short)0, (short)0),
    DARK_OAK(TreeType.DARK_OAK, null, null, "Acacia", Material.LOG_2,(short)0, (short)0);

    private final TreeType type;
    private final TreeType bigtype;
    private final TreeType talltype;
    private final String name;
    private final Material log;
    private final short blockdata;
    private final short spalingdata;
    TreeTypeEnum(TreeType treeType, TreeType big, TreeType tall,String name, Material material, short blockdata, short spalingdata) {
        this.type = treeType;
        this.bigtype = big;
        this.talltype = tall;
        this.name = name;
        this.log = material;
        this.blockdata = blockdata;
        this.spalingdata = spalingdata;
    }
    public static TreeTypeEnum getFromMaterial(Material material, short data)
    {
        for(TreeTypeEnum tte: TreeTypeEnum.values())
        {
            if (material == tte.log && data == tte.getBlockdata())
            {
                return tte;
            }
            if (material == Material.SAPLING && data == tte.getSpalingdata())
            {
                return tte;
            }
        }
        return null;
    }

    public static TreeTypeEnum getFromBlock(Block block)
    {
        for(TreeTypeEnum tte: TreeTypeEnum.values())
        {
            if (block.getType() == tte.log && block.getData() == tte.getBlockdata())
            {
                return tte;
            }
            if (block.getType() == Material.SAPLING && block.getData() == tte.getSpalingdata())
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
        return new ItemStack(log, 1, blockdata);
    }
    public Material getLog() {
        return log;
    }

    public short getBlockdata() {
        return blockdata;
    }

    public short getSpalingdata() {
        return spalingdata;
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
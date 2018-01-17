package com.firesoftitan.play.titanbox.enums;

import com.firesoftitan.play.titanbox.modules.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ModuleTypeEnum {
    Inventory("Inventory", "Linking Module", new ItemStack(Material.BOOKSHELF, 1)),
    Cobblestone("Cobblestone", "Cobblestone Module", new ItemStack(Material.COBBLESTONE, 1)),
    Bucket("Bucket", "Bucket Module", new ItemStack(Material.BUCKET, 1)),
    Fishing("Fishing", "Fishing Module", new ItemStack(Material.FISHING_ROD, 1)),
    Lumberjack("Lumberjack", "Lumberjack Module", new ItemStack(Material.DIAMOND_AXE, 1)),
    Ice("Ice", "Ice Module", new ItemStack(Material.ICE, 1)),
    Item("Item", "Item Module", new ItemStack(Material.HOPPER, 1)),
    Killer("Killer", "Killer Module", new ItemStack(Material.DIAMOND_SWORD, 1)),
    Infernal("Infernal", "Infernal Module", new ItemStack(Material.NETHER_WART_BLOCK, 1)),
    Farm("Farm", "Farm Module", new ItemStack(Material.DIAMOND_HOE, 1));

    private final String type;
    private final String title;
    private final ItemStack crafter;
    ModuleTypeEnum(String type, String title, ItemStack crafter) {
        this.type = type;
        this.title = title;
        this.crafter = crafter;
    }

    public String getTitle() {
        return ChatColor.DARK_BLUE + title;
    }

    public ItemStack getCrafter() {
        return crafter.clone();
    }

    public String getType()
    {
        return this.type;
    }
    public MainModule getNew() {
        if (this.type.equals("Inventory"))
        {
            return new InventoryModule();
        }
        if (this.type.equals("Cobblestone"))
        {
            return new CobblestoneGenModule();
        }
        if (this.type.equals("Bucket"))
        {
            return new BucketsModule();
        }
        if (this.type.equals("Fishing"))
        {
            return new FisherModule();
        }
        if (this.type.equals("Lumberjack"))
        {
            return new LumberjackModule();
        }
        if (this.type.equals("Ice"))
        {
            return new IceModule();
        }
        if (this.type.equals("Item"))
        {
            return new ItemModule();
        }
        if (this.type.equals("Killer"))
        {
            return new KillerModule();
        }
        if (this.type.equals("Infernal"))
        {
            return new InfernalModule();
        }
        if (this.type.equals("Farm"))
        {
            return new FarmModule();
        }
        return new MainModule();
    }
}
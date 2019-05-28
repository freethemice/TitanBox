package com.firesoftitan.play.titanbox.custom;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.MenuItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.SeasonCategory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Daniel on 12/28/2016.
 */
public class CustomCategories
{
    public static final Category STORAGE_ROUTING = new Category(new CustomItem(new ItemStack(Material.CHEST), "&5Storage and Routing", new String[] { "", "&a >Click to open" }), 5);
    public static final Category STORAGE_MODULES = new Category(new CustomItem(new ItemStack(Material.PAPER), "&5Routing Modules", new String[] { "", "&a >Click to open" }), 5);
    public static final Category SLIMEFUN_RESOURCES = new Category(new CustomItem(new ItemStack(Material.GOLD_INGOT), "&5Resources", new String[] { "", "&a >Click to open" }), 5);
    public static final Category SLIMEFUN_PARTS = new Category(new CustomItem(new ItemStack(Material.ANVIL), "&5Parts and Toys", new String[] { "", "&a >Click to open" }), 5);
    public static final SeasonCategory SLIMEFUN_BLANK = new SeasonCategory(18, 0, new MenuItem(Material.NETHER_STAR, "TRASH", 0, ChatColor.translateAlternateColorCodes('&', "&chelp &aSanta")));
    public static final Category SLIMEFUN_TITAN_GEAR = new Category(new CustomItem(new ItemStack(Material.DIAMOND_CHESTPLATE), "&4Gear", new String[] { "", "&a >Click to open" }), 5);
    public static final Category SLIMEFUN_TITAN_TOOLS = new Category(new CustomItem(new ItemStack(Material.DIAMOND_SWORD), "&4Tools and Weapons", new String[] { "", "&a >Click to open" }), 5);
    public static final Category SLIMEFUN_FREE_THINGS = new Category(new CustomItem(new ItemStack(Material.END_CRYSTAL), "&4Energy to Items", new String[] { "", "&a >Click to open" }), 5);
    public static final Category SLIMEFUN_RECOVERY = new Category(new CustomItem(new ItemStack(Material.ANVIL), "&4Recovery", new String[] { "", "&a >Click to open" }), 5);
    public static final Category TITAN_EGGS = new Category(new MenuItem(Material.PIG_SPAWN_EGG, "&7Spawn Eggs", 0, "open"), 5);
    public static Category ELECTRICITY = null;

    static {
        try {
            ELECTRICITY = new LockedCategory(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU4NDQzMmFmNmYzODIxNjcxMjAyNThkMWVlZThjODdjNmU3NWQ5ZTQ3OWU3YjBkNGM3YjZhZDQ4Y2ZlZWYifX19"), "&bEnergy and Electricity", "", "&a> Click to open"), 5, Categories.MACHINES_1);
        } catch (Exception e) {

        }
    }
}

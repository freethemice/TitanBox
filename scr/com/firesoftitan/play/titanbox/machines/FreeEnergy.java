package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.FreeEnergyContainer;
import com.firesoftitan.play.titanbox.enums.RanksEnum;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FreeEnergy extends FreeEnergyContainer {;
    public double poweroutput = 0;
    private static HashMap<String, List<String>> housings = new HashMap<String, List<String>>();
    public FreeEnergy(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, double poweroutput) {
        super(category, item, name, recipeType, recipe);
        this.poweroutput = poweroutput;
        SlimefunItemsManager.addFreeEnergies(item.clone());
    }
    public static List<String> getLocationInForceField(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            return tmp;
        }
        return new ArrayList<String>();
    }
    public static void removeFromForceField(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            tmp.remove(Utilities.serializeLocation(location));

            housings.put(ffH.getId(), tmp);
        }
    }
    public static void addToForceField(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            boolean contains = false;
            for (String s: tmp)
            {
                if (s.equalsIgnoreCase(Utilities.serializeLocation(location)))
                {
                    contains = true;
                }
            }
            if (!contains)
            {
                tmp.add(Utilities.serializeLocation(location));
            }
            housings.put(ffH.getId(), tmp);
        }
    }
    public static boolean isInForceField(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            return true;
        }
        return false;
    }
    public static int sizeForceFieldHas(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            return tmp.size();
        }
        return 0;
    }
    public static RanksEnum getRankInfoFromForceFieldFor(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            if (ffH.isAdmin()) return RanksEnum.RANK_7;
            RanksEnum ranksEnum = RanksEnum.valueOf(ffH.getOwner());
            return ranksEnum;
        }
        return RanksEnum.RANK_1;
    }
    public static boolean checkForceFieldFor(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            if (!tmp.contains(Utilities.serializeLocation(location)))
            {
                return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public String getInventoryTitle() {
        return "&bFree Energy";
    }

    @Override
    public ItemStack getProgressBar() {
        try {
            return CustomSkull.getItem(SlimefunItemsManager.INFO);
        } catch (Exception e) {
            return new ItemStack(Material.ANVIL);
        }
    }

    @Override
    public void registerDefaultRecipes() {}

    @Override
    public int getEnergyConsumption() {
        return 0;
    }

    public int getSpeed() {
        return 0;
    }

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        if (!checkForceFieldFor(b.getLocation()))
        {
            boolean inField = false;
            int size = 0;
            RanksEnum ranksEnum = RanksEnum.RANK_1;
            if (isInForceField(b.getLocation()))
            {
                inField = true;
                size = sizeForceFieldHas(b.getLocation());
                ranksEnum = getRankInfoFromForceFieldFor(b.getLocation());
                if (size < ranksEnum.getValue())
                {
                   addToForceField(b.getLocation());
                   return;
                }
            }
            ItemStack item = getProgressBar().clone();
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.RED + "Offline");
            List<String> lore = new ArrayList<String>();
            if (!inField) lore.add("Must be in a force Field.");
            if (inField) {
                lore.add("Online: " + size + " out of " + ranksEnum.getValue() + "");
                lore.add("Location are:");
                for (String loc : getLocationInForceField(b.getLocation())) {
                    lore.add(ChatColor.GRAY + "-" + loc.replace(";", ", "));
                }
            }
            im.setLore(lore);
            item.setItemMeta(im);
            BlockStorage.getInventory(b).replaceExistingItem(22, item);
            return;
        }

        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0 && getSpeed() < 10) {
                ItemStack item = getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, (int) poweroutput));
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList<String>();
                lore.add(MachineHelper.getProgress(timeleft, (int) poweroutput));
                lore.add("Generating: " + poweroutput + "J/s");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                 progress.put(b, timeleft - 1);
            }
            else
            {

                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            MachineRecipe r = new MachineRecipe(4, new ItemStack[0], new ItemStack[] {new ItemStack(Material.LIME_TERRACOTTA)});
            processing.put(b, r);
            progress.put(b, (int) poweroutput);

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "FREE_ENERGY";
    }

}

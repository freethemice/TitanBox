package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.ForceFieldContainer;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForceField extends ForceFieldContainer {

    private HashMap<String, Integer> maxSizes = new HashMap<String, Integer>();
    private HashMap<String, Long> maxSizesUpdates = new HashMap<String, Long>();
    public ForceField(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

    }

    @Override
    public String getInventoryTitle() {
        return "&bForceField";
    }

    @Override
    public ItemStack getProgressBar() {
        return SlimefunItemsManager.INFO_BLOCK.clone();
    }

    @Override
    public void registerDefaultRecipes() {}

    @Override
    public int getEnergyConsumption() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        ForceFieldManager ffH = WorldManager.getWorldHolder(b.getLocation().getWorld()).getFieldAt(b.getLocation());
        if (ffH == null) return;;
        try {
            ItemStack item = getProgressBar().clone();
            ItemMeta im = item.getItemMeta();
            List<String> lore = new ArrayList<String>();
            OfflinePlayer player = Bukkit.getOfflinePlayer(ffH.getOwner());
            im.setDisplayName(ChatColor.BLUE + "Owner: " + ChatColor.WHITE + player.getName());
            lore.add(ChatColor.BLUE + "Min: " + ChatColor.WHITE + (int)ffH.getMin());
            lore.add(ChatColor.BLUE + "Max: " + ChatColor.WHITE + (int)ffH.getMax());
            lore.add(ChatColor.BLUE + "Efficiency: " + ChatColor.WHITE + (int)ffH.getEfficiency());
            lore.add(ChatColor.BLUE + "Currect Size: "  + ChatColor.WHITE + (int)ffH.getSize());
            lore.add(ChatColor.GRAY + "for every 32 J/s, the field will");
            lore.add(ChatColor.GRAY + "increase by the Efficiency Number");
            im.setLore(lore);
            item.setItemMeta(im);
            BlockStorage.getInventory(b).replaceExistingItem(27, item);



            double charge = ChargableBlock.getCharge(b);
            int sizeF = (int) (charge / 32);
            sizeF++;
            int sizeMax = (int) (sizeF * ffH.getEfficiency());
            if (sizeMax < ffH.getMin()) sizeMax = (int) ffH.getMin();
            if (sizeMax > ffH.getMax()) sizeMax = (int) ffH.getMax();

            updateSizeTick(b, ffH, sizeMax);

            sizeF = (int) ((double)sizeMax/ ffH.getEfficiency());
            sizeF--;
            ChargableBlock.addCharge(b, (int) -(32*sizeF));
        } catch (Exception e) {
            //e.printStackTrace();
            ffH.setSize(ffH.getMin());
        }


    }

    private void updateSizeTick(Block b, ForceFieldManager ffH, int sizeMax) {
        String id = Utilities.serializeLocation(b.getLocation());
        int oldSize = 0;
        long lastUpdate = 0;

        if (maxSizesUpdates.containsKey(id))
        {
            lastUpdate = maxSizesUpdates.get(id);
        }
        if (maxSizes.containsKey(id))
        {
            oldSize = maxSizes.get(id);
        }
        if (sizeMax > oldSize)
        {
            if (ffH.getSize() < sizeMax)
            {
                ffH.setSize(sizeMax);
            }
            maxSizes.put(id, sizeMax);
        }
        if (System.currentTimeMillis() - lastUpdate > 1*60*1000)
        {
            maxSizesUpdates.put(id, System.currentTimeMillis());
            ffH.setSize(maxSizes.get(id));
            maxSizes.remove(id);
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "FORCE_FIELD";
    }
}

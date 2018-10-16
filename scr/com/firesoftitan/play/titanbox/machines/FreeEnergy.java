package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.containers.FreeEnergyContainer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class FreeEnergy extends FreeEnergyContainer {;
    public double poweroutput = 0;
    public FreeEnergy(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, double poweroutput) {
        super(category, item, name, recipeType, recipe);
        this.poweroutput = poweroutput;
    }
    @Override
    public String getInventoryTitle() {
        return "&bFree Energy";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.ANVIL);
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
            /*if (!fits(b, r.getOutput())) return;
            for (int slot: getInputSlots()) {
                BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
            }*/
            processing.put(b, r);
            progress.put(b, (int) poweroutput);

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "FREE_ENERGY";
    }

}

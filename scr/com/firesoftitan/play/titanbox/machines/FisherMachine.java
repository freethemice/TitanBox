package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.containers.PickThreeContainer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class FisherMachine extends PickThreeContainer {;
    public FisherMachine(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.border_mat_1 = null;
        this.border_mat_2 = null;
        this.border_mat_3 = null;
        setupUnit();
    }

    @Override
    public String getInventoryTitle() {
        return "&bFisher Machine";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_AXE);
    }

    @Override
    public void registerDefaultRecipes() {}

    public abstract int getSpeed();

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0 && getSpeed() < 10) {
                ItemStack item = getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, processing.get(b).getTicks()));
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList<String>();
                lore.add(MachineHelper.getProgress(timeleft, processing.get(b).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else progress.put(b, timeleft - 1);
            }
            else if (ChargableBlock.isChargable(b)) {
                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                ChargableBlock.addCharge(b, -getEnergyConsumption());

                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            if (b.getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.WATER)
            {
                ItemStack adding = new ItemStack(Material.COD, 1);
                Random random = new Random(System.currentTimeMillis());
                if (random.nextInt(100) < 9)
                {
                    adding = new ItemStack(Material.SALMON, 1);
                }
                if (random.nextInt(100) < 7)
                {
                    adding = new ItemStack(Material.TROPICAL_FISH, 1);
                }
                if (random.nextInt(100) < 2)
                {
                    adding = new ItemStack(Material.PUFFERFISH, 1);
                }

                adding = adding.clone();
                MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
                if (!fits(b, r.getOutput())) return;
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "FISHER_MACHINE";
    }

}

package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.containers.BContainer;
import com.firesoftitan.play.titanbox.enums.TreeTypeEnum;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
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

public abstract class SaplingFactory extends BContainer {;
    public SaplingFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

    }

    @Override
    public String getInventoryTitle() {
        return "&bSapling Factory";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_HOE);
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

            for (int slot: getInputSlots()) {
                if (!TitanBox.isEmpty(BlockStorage.getInventory(b).getItemInSlot(slot))) {
                    if (TitanBox.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.BONE_MEAL))) {
                        Random number = new Random(System.currentTimeMillis());
                        Material saplingType = BlockStorage.getInventory(b).getItemInSlot(slot).getType();
                        ItemStack adding = new ItemStack(Material.OAK_SAPLING, 1 + number.nextInt(10));
                        Random rnd = new Random(System.currentTimeMillis());
                        int pick = rnd.nextInt(TreeTypeEnum.values().length);
                        adding = new ItemStack(TreeTypeEnum.values()[pick].getSapling(), 1 + number.nextInt(10));
                        adding = adding.clone();
                        MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
                        if (!fits(b, r.getOutput())) return;
                        BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
                        processing.put(b, r);
                        progress.put(b, r.getTicks());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "SAPLING_FACTORY";
    }

}

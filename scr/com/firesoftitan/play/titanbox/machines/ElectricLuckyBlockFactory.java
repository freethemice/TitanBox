package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.containers.TitanAContainer;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public abstract class ElectricLuckyBlockFactory extends TitanAContainer {


    public ElectricLuckyBlockFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&bElectric Lucky Block Factory";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_SHOVEL);
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
                if (processing.get(b).getOutput() == null)
                {
                    progress.remove(b);
                    processing.remove(b);
                    return;
                }
                ChargableBlock.addCharge(b, -getEnergyConsumption());

                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {;
            for (int slot: getInputSlots()) {
                ItemStack check;
                int amount = 28;
                check = new ItemStack(Material.GOLD_INGOT, amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                amount = 3;
                check = new ItemStack(Material.GOLD_BLOCK, amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_4K.clone();
                amount = 28;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_6K.clone();
                amount = 24;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_8K.clone();
                amount = 20;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_10K.clone();
                amount = 16;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_12K.clone();
                amount = 12;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_14K.clone();
                amount = 11;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_16K.clone();
                amount = 10;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_18K.clone();
                amount = 9;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_20K.clone();
                amount = 8;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_22K.clone();
                amount = 7;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_24K.clone();
                amount = 6;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
                check = me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GOLD_24K_BLOCK.clone();
                amount = 1;
                check.setAmount(amount);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
                    makeBlock(b, slot, amount);
                    break;
                }
            }
        }
    }

    private void makeBlock(Block b, int slot, int amount) {
        ItemStack adding = SlimefunItemsManager.LuckyBlock;

        MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] {adding});
        if (!fits(b, r.getOutput())) return;
        BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), amount));
        processing.put(b, r);
        progress.put(b, r.getTicks());
        return;
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_LUCKY_BLOCK_FACTORY";
    }

}

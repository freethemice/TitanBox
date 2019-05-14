package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.PickThreeContainer;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public abstract class TNTFactory extends PickThreeContainer {;
    public TNTFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.border_mat_1 = Material.SAND;
        this.border_info_1.add(ChatColor.BLUE + "Sand");
        this.border_info_1.add(ChatColor.BLUE + "Red Sand");
        this.border_mat_2 = null;
        this.border_mat_3 = Material.GUNPOWDER;
        this.border_info_3.add(ChatColor.BLUE + "Gunpowder");
        setupUnit();
    }

    @Override
    public String getInventoryTitle() {
        return "&bTNT Factory";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_SWORD);
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

                BlockStorage.getInventory(b).replaceExistingItem(31, item);

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

                BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {

            int slot1 = getInputSlots()[0];
            int slot2 = getInputSlots()[1];
            if (!Utilities.isEmpty(BlockStorage.getInventory(b).getItemInSlot(slot1)) && !Utilities.isEmpty(BlockStorage.getInventory(b).getItemInSlot(slot2))) {
                if ((Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), new ItemStack(Material.SAND)) && Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot2), new ItemStack(Material.GUNPOWDER))) ||
                        (Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), new ItemStack(Material.RED_SAND)) && Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot2), new ItemStack(Material.GUNPOWDER)))) {
                    if (BlockStorage.getInventory(b).getItemInSlot(slot1).getAmount() >= 4 && BlockStorage.getInventory(b).getItemInSlot(slot2).getAmount() >= 5)
                    {
                        ItemStack adding = new ItemStack(Material.TNT, 1);
                        adding = adding.clone();
                        MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
                        if (!fits(b, r.getOutput())) return;
                        BlockStorage.getInventory(b).replaceExistingItem(slot1, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot1), 4));
                        BlockStorage.getInventory(b).replaceExistingItem(slot2, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot2), 5));
                        processing.put(b, r);
                        progress.put(b, r.getTicks());
                    }
                }
            }

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "TNT_FACTORY";
    }

}

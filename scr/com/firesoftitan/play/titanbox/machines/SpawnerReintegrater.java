package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.PickThreeContainer;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class SpawnerReintegrater extends PickThreeContainer {;
    public SpawnerReintegrater(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.border_mat_1 = Material.SPAWNER;
        this.border_info_1.add("&4\u2620 Blank Reinforced Spawner");
        this.border_mat_2 = null;

        this.border_mat_3 = Material.ZOMBIE_SPAWN_EGG;
        this.border_info_3.add("&4\u2620 Any Spawn Egg");
        setupUnit();
    }

    @Override
    public String getInventoryTitle() {
        return "&bSpawner Reintegrater";
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

                BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "Waiting for items..."));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            ItemStack slotItem_1 = BlockStorage.getInventory(b).getItemInSlot(getInputSlots()[0]);
            ItemStack slotItem_2 = BlockStorage.getInventory(b).getItemInSlot(getInputSlots()[1]);
            if (!Utilities.isEmpty(slotItem_1) && !Utilities.isEmpty(slotItem_2)) {
                    if (Utilities.isItemEqual(slotItem_1, SlimefunItemsManager.REPAIRED_SPAWNER_BLANK.clone()) || Utilities.isItemEqual(slotItem_1, SlimefunItems.REPAIRED_SPAWNER.clone())) {
                        if (Utilities.isSpawnEgg(slotItem_2))
                        {
                            ItemStack Spawner = Utilities.getSpawner(slotItem_2);
                            if (Spawner != null)
                            {
                                MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{Spawner.clone()});
                                if (!fits(b, new ItemStack[]{Spawner.clone()})) return;
                                BlockStorage.getInventory(b).replaceExistingItem(getInputSlots()[0], InvUtils.decreaseItem(slotItem_1, 1));
                                BlockStorage.getInventory(b).replaceExistingItem(getInputSlots()[1], InvUtils.decreaseItem(slotItem_2, 1));
                                processing.put(b, r);
                                progress.put(b, r.getTicks());
                            }
                        }
                    }
            }
            BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "Waiting for items..."));
        }
    }


    @Override
    public String getMachineIdentifier() {
        return "SPAWNER_REINTEGRATER";
    }

}


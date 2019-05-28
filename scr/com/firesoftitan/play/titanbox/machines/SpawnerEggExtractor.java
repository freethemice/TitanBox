package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.PickThreeContainer;
import com.firesoftitan.play.titanbox.managers.EggsManager;
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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class SpawnerEggExtractor extends PickThreeContainer {;
    public SpawnerEggExtractor(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.border_mat_1 = null;
        this.border_mat_2 = Material.SPAWNER;
        this.border_info_2.add("&4\u2620 Reinforced Spawner w/ Mob Type");
        this.border_mat_3 = null;
        setupUnit();
    }

    @Override
    public String getInventoryTitle() {
        return "&bSpawner Egg Extractor";
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
            for (int slot: getInputSlots()) {
                ItemStack slotItem = BlockStorage.getInventory(b).getItemInSlot(slot);
                if (!Utilities.isEmpty(slotItem)) {
                    if (Utilities.getName(slotItem, false).equals(ChatColor.translateAlternateColorCodes('&', "&bReinforced Spawner"))) {
                        if (!Utilities.isItemEqual(slotItem, SlimefunItemsManager.REPAIRED_SPAWNER_BLANK.clone()))
                        {
                            String tempType = Utilities.getSpawnerType(slotItem);
                            ItemStack eggStack = EggsManager.getEgg(tempType);
                            if (!Utilities.isEmpty(eggStack)) {
                                MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{SlimefunItems.REPAIRED_SPAWNER.clone(), eggStack});
                                if (!fits(b, new ItemStack[]{SlimefunItemsManager.REPAIRED_SPAWNER_BLANK.clone(), eggStack})) return;
                                BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(slotItem, 1));
                                processing.put(b, r);
                                progress.put(b, r.getTicks());
                            }
                        }
                    }
                }
            }
            BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "Waiting for items..."));
        }
    }


    @Override
    public String getMachineIdentifier() {
        return "SPAWNER_EGG_EXTRACTOR";
    }

}


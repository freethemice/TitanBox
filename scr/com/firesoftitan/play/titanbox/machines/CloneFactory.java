package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
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

public abstract class CloneFactory extends AContainer {;
    public CloneFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

    }

    @Override
    public String getInventoryTitle() {
        return "&bClone Factory";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.NETHER_STAR);
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
            int[] inputSlots = getInputSlots();
            int slug = -1;
            int slot = -1;
            if (TitanBox.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(inputSlots[0]), SlimefunItemsHolder.MINING_SLUDGE))
            {
                slug = inputSlots[0];
                slot = inputSlots[1];
            } else if (TitanBox.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(inputSlots[1]), SlimefunItemsHolder.MINING_SLUDGE))
            {
                slug = inputSlots[1];
                slot = inputSlots[0];
            }
            if (slot > -1) {
                ItemStack mySlotItem = BlockStorage.getInventory(b).getItemInSlot(slot);
                if (!TitanBox.isEmpty(mySlotItem)) {
                    if (!mySlotItem.hasItemMeta()) {
                        if (!TitanBox.isTool(mySlotItem) && !TitanBox.isWeapon(mySlotItem) && !TitanBox.isArmor(mySlotItem) && !TitanBox.isExpensive(mySlotItem) && !TitanBox.hasCustomName(mySlotItem)) {
                            if (mySlotItem.getMaxStackSize() > 1) {
                                //BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
                                Random number = new Random(System.currentTimeMillis());
                                ItemStack adding = new ItemStack(mySlotItem.getType(), 1 + number.nextInt(6), mySlotItem.getDurability());
                                adding = adding.clone();
                                MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
                                if (!fits(b, r.getOutput())) return;
                                BlockStorage.getInventory(b).replaceExistingItem(slug, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slug), 1));
                                processing.put(b, r);
                                progress.put(b, r.getTicks());
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "CLONE_FACTORY";
    }

}

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
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public abstract class ElectricLuckyBlockGrinder extends TitanAContainer {

    public ElectricLuckyBlockGrinder(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&bElectric Lucky Block Grinder";
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
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckyBlock, 3, 1)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckyAxe, 1, 9)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckySword, 1, 6)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckyPickaxe, 1, 9)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckyHelmet, 1, 15)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckyChestplate, 1, 24)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckyLeggings, 1, 15)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.LuckyBoots, 1, 12)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.ZeroLuckyBlock, 6, 1)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.UnLuckyBlock, 9, 1)) break;
                if (sendToMachine(b, slot, SlimefunItemsManager.PandorasBox, 1, 9)) break;




            }
        }
    }

    private boolean sendToMachine(Block b, int slot, ItemStack check, int amount, int amountout) {
        check = check.clone();
        check.setAmount(amount);
        if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true)) {
            makeBlock(b, slot, amount, amountout);
            return true;
        }
        return false;
    }

    private void makeBlock(Block b, int slot, int amount, int OutAmount) {
        ItemStack adding = SlimefunItemsManager.LuckyNugget.clone();
        adding.setAmount(OutAmount);
        if (SlimefunStartup.chance(100, 1)) adding.setAmount(adding.getAmount() + 1);
        if (SlimefunStartup.chance(100, 1)) adding.setAmount(adding.getAmount() + 1);
        if (SlimefunStartup.chance(100, 1)) adding.setAmount(adding.getAmount() + 1);
        MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] {adding});
        if (!fits(b, r.getOutput())) return;
        BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), amount));
        processing.put(b, r);
        progress.put(b, r.getTicks());
        return;
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_LUCKY_BLOCK_GRINDER";
    }

}

package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.PickThreeContainer;
import com.firesoftitan.play.titanbox.enums.ItemToBlockEnum;
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

public abstract class BlockAssemblerDisassembler extends PickThreeContainer {;
    public BlockAssemblerDisassembler(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.border_mat_1 = null;
        this.border_mat_2 = Material.QUARTZ;
        this.border_mat_3 = null;
        for(ItemToBlockEnum block: ItemToBlockEnum.values())
        {
            if (block.getName().equals("GOLD_24K"))
            {
                this.border_info_2.add(ChatColor.BLUE + "24k Gold" + ChatColor.WHITE + " or " + ChatColor.BLUE + "24k Gold Ingot");
            }
            else
            {
                this.border_info_2.add(ChatColor.BLUE + Utilities.fixCapitalization(block.getBlock().getType().name()) + ChatColor.WHITE + " or " + ChatColor.BLUE + Utilities.fixCapitalization(block.getItem().getType().name()));
            }
        }
        setupUnit();
    }

    @Override
    public String getInventoryTitle() {
        return "&bBlock Assembler And Disassembler";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_PICKAXE);
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
            if (!Utilities.isEmpty(BlockStorage.getInventory(b).getItemInSlot(slot1))) {
                for(ItemToBlockEnum itemToBlock: ItemToBlockEnum.values())
                {
                    checkAndProcess(slot1, itemToBlock.getCount(), b, itemToBlock.getItem(), itemToBlock.getBlock());
                }
            }

        }
    }
    private void checkAndProcess(int slot1, int count, Block b, ItemStack small, ItemStack block)
    {
        checkAndProcessToBig(slot1, count, b, small, block);
        checkAndProcessToSmall(slot1, count, b, small, block);
    }
    private void checkAndProcessToSmall(int slot1, int count, Block b, ItemStack small, ItemStack block)
    {
        if ((Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), block))) {
            if (BlockStorage.getInventory(b).getItemInSlot(slot1).getAmount() >= 1)
            {
                ItemStack adding = small.clone();
                adding.setAmount(count);
                adding = adding.clone();
                MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
                if (!fits(b, r.getOutput())) return;
                BlockStorage.getInventory(b).replaceExistingItem(slot1, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot1), 1));
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }
    private void checkAndProcessToBig(int slot1, int count, Block b, ItemStack small, ItemStack block)
    {
        if ((Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), small))) {
            if (BlockStorage.getInventory(b).getItemInSlot(slot1).getAmount() >= count)
            {
                ItemStack adding = block.clone();
                adding.setAmount(1);
                adding = adding.clone();
                MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
                if (!fits(b, r.getOutput())) return;
                BlockStorage.getInventory(b).replaceExistingItem(slot1, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot1), count));
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }
    @Override
    public String getMachineIdentifier() {
        return "BLOCK_ASSEMBLER_DISASSEMBLER";
    }

}

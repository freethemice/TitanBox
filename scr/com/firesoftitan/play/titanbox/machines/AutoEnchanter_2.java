package com.firesoftitan.play.titanbox.machines;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoEnchanter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AutoEnchanter_2 extends AutoEnchanter {
    private long timepass = 0;
    public AutoEnchanter_2(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }
    @Override
    public String getInventoryTitle() {
        return "&5Auto-Enchanter II";
    }

    @Override
    protected void tick(Block b) {
        super.tick(b);
        if (this.isProcessing(b)) {
            int timeleft = this.progress.get(b);
            int timePassed = getTimePassed(b);
            if (timeleft > 0) {

                ItemStack item = this.getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, this.processing.get(b).getTicks()));
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList<String>();
                lore.add(MachineHelper.getProgress(timeleft, this.processing.get(b).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < this.getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -this.getEnergyConsumption());
                    this.progress.put(b, timeleft - timePassed);
                }
                else this.progress.put(b, timeleft - timePassed);
            }
        }

    }
    public int getTimePassed(Block b)
    {
        try {
            Long first = timepass;
            Long passed = System.currentTimeMillis() - first;
            passed = passed / 500; // 1/2 seconds
            int out = Integer.parseInt(passed + "");
            timepass = System.currentTimeMillis();
            if (out < 1) {
                out = 1;
            }
            return out;
        }
        catch (Exception e)
        {
            return Integer.MAX_VALUE;
        }
    }
    @Override
    public String getMachineIdentifier() {
        return "AUTO_ENCHANTER_II";
    }

}

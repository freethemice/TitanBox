package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class XPPlate extends AContainer {

    public XPPlate(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&bXP Plate";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.BEDROCK);
    }

    @Override
    public void registerDefaultRecipes() {}

    public abstract int getSpeed();

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        try {
            if (b.getType() != Material.CARPET)
            {
                BlockStorage.clearBlockInfo(b);
                return;
            }
            if (ChargableBlock.isChargable(b)) {
                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                if (b.getLocation().getWorld().getEnvironment() != World.Environment.NORMAL) return;
                List<Entity> EList = TitanBox.instants.getNearbyEntities(b.getLocation(), 3);
                for(Entity ent: EList)
                {
                    if (ent instanceof Player)
                    {
                        if (ent.getLocation().getWorld().getName().equals(b.getLocation().getWorld().getName()) && ent.getLocation().getBlockX() == b.getLocation().getBlockX() && ent.getLocation().getBlockY() == b.getLocation().getBlockY() && ent.getLocation().getBlockZ() == b.getLocation().getBlockZ() )
                        {
                            b.getLocation().getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                            for (int slot : getInputSlots()) {
                                ItemStack flask = BlockStorage.getInventory(b).getItemInSlot(slot);
                                if (!TitanBox.isEmpty(flask)) {
                                    if (flask.getType() == Material.EXP_BOTTLE) {
                                        int amount = flask.getAmount();
                                        int xp = amount * 7;

                                        BlockStorage.getInventory(b).replaceExistingItem(slot, null);
                                        //payfor it
                                        ChargableBlock.addCharge(b, -getEnergyConsumption());

                                        //play effects
                                        //b.getLocation().getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);

                                        ((Player) ent).giveExp(xp);
                                    }
                                }

                            }
                        }
                    }
                }



            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "XP_PLATE";
    }

}

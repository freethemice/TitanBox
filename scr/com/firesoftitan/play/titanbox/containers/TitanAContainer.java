package com.firesoftitan.play.titanbox.containers;

import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import org.bukkit.inventory.ItemStack;

public abstract class TitanAContainer extends AContainer {
    public TitanAContainer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
        SlimefunItemsManager.addTitanItem(item.clone());
    }

    public TitanAContainer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, id, recipeType, recipe, recipeOutput);
        SlimefunItemsManager.addTitanItem(recipeOutput.clone());
    }

    public abstract String getInventoryTitle();

    public abstract ItemStack getProgressBar();

    public abstract void registerDefaultRecipes();

    public abstract int getEnergyConsumption();

    public abstract int getSpeed();

    public abstract String getMachineIdentifier();
}

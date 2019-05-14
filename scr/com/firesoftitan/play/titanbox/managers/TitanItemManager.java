package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.Utilities;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.inventory.ItemStack;

public class TitanItemManager extends SlimefunItem{
    public TitanItemManager(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
        checkRequirments(item, recipe);
    }

    public TitanItemManager(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, id, recipeType, recipe, recipeOutput);
        SlimefunItemsManager.addTitanItem(recipeOutput.clone());
        checkRequirments(recipeOutput, recipe);
    }

    public TitanItemManager(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, String[] keys, Object[] values) {
        super(category, item, id, recipeType, recipe, recipeOutput, keys, values);
        SlimefunItemsManager.addTitanItem(recipeOutput.clone());
        checkRequirments(recipeOutput, recipe);
    }

    public TitanItemManager(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
        super(category, item, id, recipeType, recipe, keys, values);
        checkRequirments(item, recipe);
    }

    public TitanItemManager(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, boolean hidden) {
        super(category, item, id, recipeType, recipe, hidden);
        checkRequirments(item, recipe);
    }

    private void checkRequirments(ItemStack item, ItemStack[] recipe) {
        SlimefunItemsManager.addTitanItem(item.clone());

        for(ItemStack part: recipe)
        {
            if (Utilities.isItemEqual(part, SlimefunItemsManager.FORCE_FIELD_RESONATOR))
            {
                SlimefunItemsManager.addForceFieldRequired(item.clone());
                break;
            }
        }
    }
}

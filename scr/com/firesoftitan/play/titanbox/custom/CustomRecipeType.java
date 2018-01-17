package com.firesoftitan.play.titanbox.custom;

import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.Material;

/**
 * Created by Daniel on 12/28/2016.
 */
public class CustomRecipeType {

    public static RecipeType TitanStone = new RecipeType(SlimefunItemsHolder.TitanStone);

    public static final RecipeType BEDROCK_DRILL = new RecipeType(new CustomItem(Material.IRON_BLOCK, "&aBedrock Drill", 1, new String[] { "", "&a&oObtain it by mining bedrock using the Bedrock Drill with Bedrock Drill Head" }));
}
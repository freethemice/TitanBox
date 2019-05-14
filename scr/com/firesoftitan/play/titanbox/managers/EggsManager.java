package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.custom.CustomCategories;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class EggsManager
{
  private static Config config = new Config("plugins" + File.separator + "TitanBox" + File.separator  + "egg_config.yml");
  private static HashMap<String, ItemStack> basket_of_eggs = new HashMap<String, ItemStack>();
  public static ItemStack getEgg(String type)
  {
    return basket_of_eggs.get(type.toLowerCase());
  }
  public static void loadConfig()
  {
    ItemStack eggItem;
    ItemMeta eggMeta;
    for (String key : config.getKeys("SpawnEggs"))
    {
      List<?> EggRecipe = config.getStringList("SpawnEggs." + key + ".Recipe");
      boolean addText = true;
      if (EggRecipe.size() == 3)
      {
        String[] Row1 = ((String)EggRecipe.get(0)).split(", ");
        String[] Row2 = ((String)EggRecipe.get(1)).split(", ");
        String[] Row3 = ((String)EggRecipe.get(2)).split(", ");
        if ((Row1.length != 3) || (Row2.length != 3) || (Row3.length != 3))
        {
          System.out.println("[TitanBox] Invalid Crafting Recipe Specified for " + key + " EGG");
          throw new IllegalArgumentException();
        }
        Material eggType = Material.getMaterial(key+ "_SPAWN_EGG", false);
        if (eggType == null)
        {
          System.out.println("[TitanBox] Can't find recipe for " + key + " (" + key+ "_SPAWN_EGG" + ")");
          addText = false;
        }
        if (eggType != null) {
          eggItem = new ItemStack(eggType, 1);
          eggMeta = eggItem.getItemMeta();
          if ((eggMeta instanceof SpawnEggMeta)) {
            if (config.contains("SpawnEggs." + key + ".Lore")) {
              eggMeta.setLore(config.getStringList("SpawnEggs." + key + ".Lore"));
            }
            if (config.contains("SpawnEggs." + key + ".Glow")) {
              if (config.getBoolean("SpawnEggs." + key + ".Glow")) {
                eggMeta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
                eggMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
              }
            }
          }
          eggItem.setItemMeta(eggMeta);

          ItemStack mat1 = SlimefunItemsManager.JAR_OF_LIFE_FORCE.clone();
          ItemStack mat2 = new ItemStack(Material.getMaterial(Row1[1]));
          ItemStack mat3 = SlimefunItemsManager.JAR_OF_SOULS.clone();
          ItemStack mat4 = new ItemStack(Material.getMaterial(Row2[0]));
          ItemStack mat5 = new ItemStack(Material.getMaterial(Row2[1]));
          ItemStack mat6 = new ItemStack(Material.getMaterial(Row2[2]));
          ItemStack mat7 = SlimefunItemsManager.JAR_OF_SOULS.clone();
          ItemStack mat8 = new ItemStack(Material.getMaterial(Row3[1]));
          ItemStack mat9 = SlimefunItemsManager.JAR_OF_LIFE_FORCE.clone();

          ItemStack Reward = eggItem.clone();
          Reward.setAmount(1);
          new TitanItemManager(CustomCategories.TITAN_EGGS, eggItem.clone(), key, RecipeType.ANCIENT_ALTAR, new ItemStack[] {
                  mat1, mat2, mat3,
                  mat4, mat5, mat6,
                  mat7, mat8, mat9
          }, Reward.clone()).register();
          basket_of_eggs.put(key.toLowerCase(), eggItem.clone());
        }
      }
      else
      {
        System.out.println("[TitanBox] Invalid Crafting Recipe Specified for " + key + " EGG");
        throw new IllegalArgumentException();
      }
      if (addText) {
        System.out.println("[TitanBox] Added recipe for " + key + " EGG");
      }
    }
  }
}

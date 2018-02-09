package com.firesoftitan.play.titanbox;

import com.firesoftitan.play.titanbox.custom.CustomCategories;
import com.firesoftitan.play.titanbox.custom.CustomRecipeType;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.holders.ItemHolder;
import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import com.firesoftitan.play.titanbox.items.TitanTalisman;
import com.firesoftitan.play.titanbox.machines.*;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class SlimefunSetup {

    public static Map<String, ItemStack> recipesV = new HashMap<String, ItemStack>();
    public SlimefunSetup()
    {
        SlimefunItemsHolder.TitanStone = makeTitanStone();


        registerItems();

        setupVanillaCraft();


    }
    public void registerItems()
    {


        ItemStack Reward =  new ItemStack(SlimefunItemsHolder.TitanStone);
        Reward.setAmount(16);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.TitanStone, "TitanStone", RecipeType.ANCIENT_ALTAR, new ItemStack[] {me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ESSENCE_OF_AFTERLIFE, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.POWER_CRYSTAL}, Reward.clone()).register();
        Reward =  new ItemStack(SlimefunItemsHolder.TITAN_MOTOR);
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsHolder.TITAN_MOTOR, "TitanMotor", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot,null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ELECTRO_MAGNET,null, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot}, Reward.clone()).register();
        Reward =  new ItemStack(SlimefunItemsHolder.ECLIPSE_COIL);
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsHolder.ECLIPSE_COIL, "EclipseCoil", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ELECTRIC_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot}, Reward.clone()).register();


        setupLuckySet();

        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.EclipseNugget, "EclipseNugget", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.TitanStone, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();
        setupEclipseSet();

        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.TitanNugget, "TitanNugget", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TitanStone, SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot, null}).register();
        setupTitanSet();


        new ElectricLuckyBlockGrinder(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_LUCKY_BLOCK_GRINDER, "ELECTRIC_LUCKY_BLOCK_GRINDER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.ELECTRIC_LUCKY_BLOCK_FACTORY,  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.TITAN_MOTOR,  SlimefunItemsHolder.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);
        new ElectricLuckyBlockFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_LUCKY_BLOCK_FACTORY, "ELECTRIC_LUCKY_BLOCK_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, new ItemStack(Material.DISPENSER, 1),  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.TITAN_MOTOR,  SlimefunItemsHolder.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 12;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoDust(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST, "ELECTRIC_COBLE_TO_DUST", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ELECTRIC_DUST_WASHER,  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.TITAN_MOTOR,  SlimefunItemsHolder.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 10;
            }

            @Override
            public int getSpeed() {
                return 2;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoIngot(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT, "ELECTRIC_COBLE_TO_INGOT", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ELECTRIC_INGOT_FACTORY,  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.TITAN_MOTOR,  SlimefunItemsHolder.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 2;
            }
        }.registerChargeableBlock(true, 512);


        new ElectricCobbletoDust(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST_2, "ELECTRIC_COBLE_TO_DUST_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsHolder.EclipseIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST,  SlimefunItemsHolder.EclipseIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT,  SlimefunItemsHolder.EclipseIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 10;
            }

            @Override
            public int getSpeed() {
                return 10;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoIngot(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT_2, "ELECTRIC_COBLE_TO_INGOT_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsHolder.EclipseIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT,  SlimefunItemsHolder.EclipseIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT,  SlimefunItemsHolder.EclipseIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HARDENED_METAL_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 10;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoDust(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST_3, "ELECTRIC_COBLE_TO_DUST_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsHolder.TitanIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST_2,  SlimefunItemsHolder.TitanIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT,  SlimefunItemsHolder.TitanIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 10;
            }

            @Override
            public int getSpeed() {
                return 20;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoIngot(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT_3, "ELECTRIC_COBLE_TO_INGOT_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsHolder.TitanIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT_2,  SlimefunItemsHolder.TitanIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT,  SlimefunItemsHolder.TitanIngot,  me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 20;
            }
        }.registerChargeableBlock(true, 512);



        new AutomatedVanillaCraftingChamber(CustomCategories.ELECTRICITY, SlimefunItemsHolder.AUTOMATED_VANILLA_CRAFTING_CHAMBER, "AUTOMATED_VANILLA_CRAFTING_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.WORKBENCH), null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.CARGO_MOTOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.COPPER_INGOT, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.CARGO_MOTOR, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public int getEnergyConsumption() {
                return 5;
            }
        }.registerChargeableBlock(true, 256);

        new AncientAltarCrafter(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ANCIENT_ALTAR_CRAFTER, "ANCIENT_ALTAR_CRAFTER_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ANCIENT_PEDESTAL, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.CARGO_MOTOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ANCIENT_ALTAR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.CARGO_MOTOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ANCIENT_PEDESTAL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ELECTRIC_MOTOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ANCIENT_PEDESTAL}) {

            @Override
            public int getEnergyConsumption() {
                return 25;
            }
        }.registerChargeableBlock(true, 256);

        new AutomatedAncientAltarCrafter(CustomCategories.ELECTRICITY, SlimefunItemsHolder.AUTOMATED_ANCIENT_ALTAR_CRAFTER, "AUTOMATED_ANCIENT_ALTAR_CRAFTER_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.WORKBENCH), null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.CARGO_MOTOR, SlimefunItemsHolder.ANCIENT_ALTAR_CRAFTER, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.CARGO_MOTOR, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public int getEnergyConsumption() {
                return 25;
            }
        }.registerChargeableBlock(true, 256);
        new FreeFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.BONE_FACTORY, "BONE_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_SKELETON, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_SKELETON, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.REPAIRED_SPAWNER_SKELETON},
                Material.BONE) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 50);

        new FreeFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.IRON_FACTORY, "IRON_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_VILLAGER, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_VILLAGER, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.REPAIRED_SPAWNER_VILLAGER},
                Material.IRON_INGOT) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 50);
        new FreeFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.XP_FACTORY, "XP_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_PIG, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_PIG, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.REPAIRED_SPAWNER_PIG},
                new CustomItem(Material.EXP_BOTTLE, "&aFlask of Knowledge", 0)) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 50);
        new FreeFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.SLIME_FACTORY, "SLIME_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_SLIME, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_SLIME, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.REPAIRED_SPAWNER_SLIME},
                Material.SLIME_BALL) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 50);
        new FreeFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.BLAZE_FACTORY, "BLAZE_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_BLAZE, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_BLAZE, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.REPAIRED_SPAWNER_BLAZE},
                Material.BLAZE_ROD) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 50);
        new FreeFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.ENDERMAN_FACTORY, "ENDERMAN_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_ENDERMAN, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.REPAIRED_SPAWNER_ENDERMAN, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.REPAIRED_SPAWNER_ENDERMAN},
                Material.ENDER_PEARL) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 50);
        new CharcoalFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.CHARCOAL_FACTORY, "CHARCOAL_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, new ItemStack(Material.FURNACE), SlimefunItemsHolder.ECLIPSE_COIL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GILDED_IRON, SlimefunItemsHolder.TITAN_MOTOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 50);
        new CharcoalFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.CHARCOAL_FACTORY_2, "CHARCOAL_FACTORY_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.CHARCOAL_FACTORY, SlimefunItemsHolder.ECLIPSE_COIL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GILDED_IRON, SlimefunItemsHolder.TITAN_MOTOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 10;
            }

        }.registerChargeableBlock(true, 50);
        new CharcoalFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.CHARCOAL_FACTORY_3, "CHARCOAL_FACTORY_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.CHARCOAL_FACTORY_2, SlimefunItemsHolder.ECLIPSE_COIL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GILDED_IRON, SlimefunItemsHolder.TITAN_MOTOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 20;
            }

        }.registerChargeableBlock(true, 50);
        new NuggettoIngotFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.NUGGETTOINGOT, "NUGGET_TO_IGNOT", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, new ItemStack(Material.BOOKSHELF), SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,  new ItemStack(Material.DISPENSER),  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.LuckyIngot,   new ItemStack(Material.BOOKSHELF),  SlimefunItemsHolder.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);
        new IngotUpFactory(CustomCategories.ELECTRICITY, SlimefunItemsHolder.INGOTUP, "INGOT_UP", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, new ItemStack(Material.WORKBENCH), SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,  new ItemStack(Material.DISPENSER),  SlimefunItemsHolder.LuckyIngot,  SlimefunItemsHolder.LuckyIngot,   new ItemStack(Material.BOOKSHELF),  SlimefunItemsHolder.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);

        new XPPlate(CustomCategories.ELECTRICITY, SlimefunItemsHolder.XPPLATE, "XP_PLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {
                        SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.DIAMOND_WRITING_PLATE, SlimefunItemsHolder.TITAN_MOTOR,
                        SlimefunItemsHolder.EMERALD_WRITING_PLATE, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.EMERALD_WRITING_PLATE,
                        SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.DIAMOND_WRITING_PLATE, SlimefunItemsHolder.TITAN_MOTOR}) {

            @Override
            public int getEnergyConsumption() {
                return 15;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 100);
        new VoidMiner(CustomCategories.ELECTRICITY, SlimefunItemsHolder.VOIDMINNER, "VOID_MINER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.TITAN_MOTOR}) {

            @Override
            public int getEnergyConsumption() {
                return 350;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 1000);
        new BedrockDrill(CustomCategories.ELECTRICITY, SlimefunItemsHolder.BEDROCKDRILL, "BEDROCKD_RILL", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.ECLIPSE_COIL}) {

            @Override
            public int getEnergyConsumption() {
                return 200;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 500);

        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, new CustomItem(new ItemStack(Material.BEDROCK), "&8Bedrock"), "BEDROCK", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.BEDROCK_DUST }).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.BEDROCK_DUST, "BEDROCK_DUST", CustomRecipeType.BEDROCK_DRILL, new ItemStack[] { null }).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsHolder.BEDROCK_DRILL, "BEDROCK_DRILL", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_PLATE, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_PLATE, SlimefunItemsHolder.LuckyNugget, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_PLATE, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_PLATE, null }, SlimefunItemsHolder.BEDROCK_DRILL.clone() ).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsHolder.LASER_CHARGE, "LASER_CHARGE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, new ItemStack(Material.REDSTONE), me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, null }, SlimefunItemsHolder.LASER_CHARGE.clone()).register();

        new AGenerator(CustomCategories.ELECTRICITY, SlimefunItemsHolder.RAREORE_GENERATOR, "RAREORE_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.ECLIPSE_COIL, new ItemStack(Material.FURNACE), SlimefunItemsHolder.ECLIPSE_COIL, SlimefunItemsHolder.VOID_PARTICLES, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.VOID_PARTICLES, SlimefunItemsHolder.VOID_PARTICLES_NEGATIVE, SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.VOID_PARTICLES_POSITIVE}) {

            @Override
            public void registerDefaultRecipes() {
                registerFuel(new MachineFuel(32, new MaterialData(Material.DIAMOND, (byte) 0).toItemStack(1)));
                registerFuel(new MachineFuel(320, new ItemStack(Material.DIAMOND_BLOCK)));
                registerFuel(new MachineFuel(64, new MaterialData(Material.EMERALD, (byte) 0).toItemStack(1)));
                registerFuel(new MachineFuel(640, new ItemStack(Material.EMERALD_BLOCK)));
            }

            @Override
            public ItemStack getProgressBar() {
                return new ItemStack(Material.FLINT_AND_STEEL);
            }

            @Override
            public String getInventoryTitle() {
                return "&cRare Ore Generator";
            }

            @Override
            public int getEnergyProduction() {
                return 32;
            }

        }.registerUnrechargeableBlock(true, 256);

        new SlimefunItem(CustomCategories.ELECTRICITY, SlimefunItemsHolder.THERMAL_GENERATOR, "THERMAL_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HEATING_COIL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.SOLAR_GENERATOR_4, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HEATING_COIL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.LARGE_CAPACITOR, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.REINFORCED_ALLOY_INGOT, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HEATING_COIL, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.SOLAR_GENERATOR_4, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.HEATING_COIL})
                .register(true, new EnergyTicker() {

                    @Override
                    public double generateEnergy(Location l, SlimefunItem item, Config data) {
                        try {
                            if (l == null) {
                                return 0;
                            }
                            Location lavaCheck = l.clone().add(0, -1, 0);
                            Location AirCheck = l.clone().add(0, 1, 0);
                            boolean Run = true;
                            boolean explode = false;
                            if (l.getChunk().isLoaded()) {
                                try {
                                    for (int x = -1; x < 2; x++) {
                                        for (int z = -1; z < 2; z++) {
                                            if (lavaCheck.clone().add(x, 0, z).getBlock().getType() != Material.STATIONARY_LAVA) {
                                                Run = false;
                                            }
                                            if (AirCheck.clone().add(x, 0, z).getBlock().getType() != Material.AIR) {
                                                explode = true;
                                            }
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (Run && !explode) {
                                double past = 256 * (1D - (l.getBlockY() / 100D));
                                return past * 2;
                            } else {
                                if (explode && Run) {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {

                                        @Override
                                        public void run() {
                                            AirCheck.getWorld().createExplosion(AirCheck.add(0, 7, 0).clone(), 6);
                                            BlockStorage.clearBlockInfo(l);

                                            for (int y = 0; y < 100; y++) {
                                                for (int x = -1; x < 2; x++) {
                                                    for (int z = -1; z < 2; z++) {
                                                        Location exp = l.clone().add(x, y, z);
                                                        if (BlockStorage.hasBlockInfo(exp)) {
                                                            BlockStorage.clearBlockInfo(exp);
                                                        }
                                                        AirCheck.getWorld().getBlockAt(exp).setType(Material.AIR);
                                                    }
                                                }
                                            }
                                        }
                                    }, 20);

                                }
                                return 0;
                            }
                        }
                        catch (Exception e)
                        {
                            return 0;
                        }
                    }
                    //8192
                    @Override
                    public boolean explode(Location l) {
                        return false;
                    }

                });
        ChargableBlock.registerChargableBlock("THERMAL_GENERATOR", 8192, false);

        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.FREE_ENERGY_I, "FREE_ENERGY_I", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {null, null, null,
                    null, null, null,
                    null, null, new ItemStack(Material.BARRIER, 1, (short)1)})
            .register(true, new EnergyTicker() {

                @Override
                public double generateEnergy(Location l, SlimefunItem item, Config data) {
                    try {
                        return 500;
                    }
                    catch (Exception e)
                    {
                        return 0;
                    }
                }
                //8192
                @Override
                public boolean explode(Location l) {
                    return false;
                }

            });

        ChargableBlock.registerChargableBlock("FREE_ENERGY_I", 8192, false);

        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.FREE_ENERGY_II, "FREE_ENERGY_II", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {null, null, null,
                    null, null, null,
                    null, null, new ItemStack(Material.BARRIER, 1, (short)2)})
            .register(true, new EnergyTicker() {

                @Override
                public double generateEnergy(Location l, SlimefunItem item, Config data) {
                    try {
                        return 1000;
                    }
                    catch (Exception e)
                    {
                        return 0;
                    }
                }
                //8192
                @Override
                public boolean explode(Location l) {
                    return false;
                }

            });

        ChargableBlock.registerChargableBlock("FREE_ENERGY_II", 8192, false);

        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.FREE_ENERGY_III, "FREE_ENERGY_III", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, null, null,
                        null, null, null,
                        null, null, new ItemStack(Material.BARRIER, 1, (short)3)})
                .register(true, new EnergyTicker() {

                    @Override
                    public double generateEnergy(Location l, SlimefunItem item, Config data) {
                        try {
                            return 2000;
                        }
                        catch (Exception e)
                        {
                            return 0;
                        }
                    }
                    //8192
                    @Override
                    public boolean explode(Location l) {
                        return false;
                    }

                });

        ChargableBlock.registerChargableBlock("FREE_ENERGY_III", 8192, false);

        new TitanTalisman(SlimefunItemsHolder.TALISMAN_VOID, "TALISMAN_VOID",
                new ItemStack[] {SlimefunItemsHolder.VOID_PARTICLES, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.VOID_PARTICLES, SlimefunItemsHolder.TitanStone, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.TALISMAN, SlimefunItemsHolder.TitanStone, SlimefunItemsHolder.VOID_PARTICLES, SlimefunItemsHolder.BEDROCK_DUST, SlimefunItemsHolder.VOID_PARTICLES},
                false, false, "void", new PotionEffect[0])
                .register(true);

        Slimefun.registerResearch(new Research(79001, "Thermal Power Plant", 89), SlimefunItemsHolder.THERMAL_GENERATOR);
        Slimefun.registerResearch(new Research(79002, "Ancient Altar Crafter", 75), SlimefunItemsHolder.ANCIENT_ALTAR_CRAFTER);
        Slimefun.registerResearch(new Research(79003, "Vanilla Auto Crafter", 25), SlimefunItemsHolder.AUTOMATED_VANILLA_CRAFTING_CHAMBER);
        Slimefun.registerResearch(new Research(79004, "Automated Ancient Altar Crafter", 25), SlimefunItemsHolder.AUTOMATED_ANCIENT_ALTAR_CRAFTER);


        Slimefun.registerResearch(new Research(7500, "Titan Stone", 250), new ItemStack[] { SlimefunItemsHolder.TitanStone });

        Slimefun.registerResearch(new Research(7505, "Lucky Nugget", 50), new ItemStack[] { SlimefunItemsHolder.LuckyNugget,  SlimefunItemsHolder.LuckyNuggetB});

        Slimefun.registerResearch(new Research(7513, "Eclipse Nugget", 50), new ItemStack[] { SlimefunItemsHolder.EclipseNugget, SlimefunItemsHolder.EclipseNuggetB });
        Slimefun.registerResearch(new Research(7514, "Eclipse Axe", 100), new ItemStack[] { SlimefunItemsHolder.EclipseAxe });
        Slimefun.registerResearch(new Research(7515, "Eclipse Sword", 125), new ItemStack[] { SlimefunItemsHolder.EclipseSword });
        Slimefun.registerResearch(new Research(7516, "Eclipse Pickaxe", 175), new ItemStack[] { SlimefunItemsHolder.EclipsePickaxe });
        Slimefun.registerResearch(new Research(7517, "Eclipse Helmet", 100), new ItemStack[] { SlimefunItemsHolder.EclipseHelmet });
        Slimefun.registerResearch(new Research(7518, "Eclipse Chestplate", 175), new ItemStack[] { SlimefunItemsHolder.EclipseChestplate });
        Slimefun.registerResearch(new Research(7519, "Eclipse Leggings", 150), new ItemStack[] { SlimefunItemsHolder.EclipseLeggings });
        Slimefun.registerResearch(new Research(7520, "Eclipse Boots", 100), new ItemStack[] { SlimefunItemsHolder.EclipseBoots });

        Slimefun.registerResearch(new Research(7521, "Titan Nugget", 50), new ItemStack[] { SlimefunItemsHolder.TitanNugget, SlimefunItemsHolder.TitanNuggetB });
        Slimefun.registerResearch(new Research(7522, "Titan Axe", 100), new ItemStack[] { SlimefunItemsHolder.TitanAxe });
        Slimefun.registerResearch(new Research(7523, "Titan Sword", 125), new ItemStack[] { SlimefunItemsHolder.TitanSword });
        Slimefun.registerResearch(new Research(7524, "Titan Pickaxe", 175), new ItemStack[] { SlimefunItemsHolder.TitanPickaxe });
        Slimefun.registerResearch(new Research(7525, "Titan Helmet", 100), new ItemStack[] { SlimefunItemsHolder.TitanHelmet });
        Slimefun.registerResearch(new Research(7526, "Titan Chestplate", 175), new ItemStack[] { SlimefunItemsHolder.TitanChestplate });
        Slimefun.registerResearch(new Research(7527, "Titan Leggings", 150), new ItemStack[] { SlimefunItemsHolder.TitanLeggings });
        Slimefun.registerResearch(new Research(7528, "Titan Boots", 100), new ItemStack[] { SlimefunItemsHolder.TitanBoots });

        Slimefun.registerResearch(new Research(7529, "Lucky Ingot", 50), new ItemStack[] { SlimefunItemsHolder.LuckyIngot });
        Slimefun.registerResearch(new Research(7530, "Eclipse Ingot", 50), new ItemStack[] { SlimefunItemsHolder.EclipseIngot });
        Slimefun.registerResearch(new Research(7531, "Titan Ingot", 50), new ItemStack[] { SlimefunItemsHolder.TitanIngot });

        Slimefun.registerResearch(new Research(7532, "Electric Cobble to", 25), new ItemStack[] { SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST_3, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT_3, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST_2, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT_2, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_DUST, SlimefunItemsHolder.ELECTRIC_COBBLE_TO_INGOT, SlimefunItemsHolder.ELECTRIC_LUCKY_BLOCK_FACTORY, SlimefunItemsHolder.ELECTRIC_LUCKY_BLOCK_GRINDER });
        Slimefun.registerResearch(new Research(7536, "Titan Disenchanter", 50), new ItemStack[] { SlimefunItemsHolder.TITAN_AUTO_DISENCHANTER });
        Slimefun.registerResearch(new Research(7537, "Titan Motor", 50), new ItemStack[] { SlimefunItemsHolder.TITAN_MOTOR, SlimefunItemsHolder.ECLIPSE_COIL });
        Slimefun.registerResearch(new Research(7538, "Titan Charcoal", 25), new ItemStack[] { SlimefunItemsHolder.CHARCOAL_FACTORY, SlimefunItemsHolder.NUGGETTOINGOT, SlimefunItemsHolder.INGOTUP, SlimefunItemsHolder.CHARCOAL_FACTORY_2, SlimefunItemsHolder.CHARCOAL_FACTORY_3, SlimefunItemsHolder.BONE_FACTORY, SlimefunItemsHolder.IRON_FACTORY, SlimefunItemsHolder.XP_FACTORY, SlimefunItemsHolder.SLIME_FACTORY, SlimefunItemsHolder.ENDERMAN_FACTORY, SlimefunItemsHolder.BLAZE_FACTORY});
        Slimefun.registerResearch(new Research(7539, "Titan End Game", 125), new ItemStack[] { SlimefunItemsHolder.BEDROCKDRILL, SlimefunItemsHolder.VOIDMINNER });
        Slimefun.registerResearch(new Research(7540, "Titan Talisman", 50), new ItemStack[] { SlimefunItemsHolder.TALISMAN_VOID});
        Slimefun.registerResearch(new Research(7541, "Rare Ore Power", 89), SlimefunItemsHolder.RAREORE_GENERATOR);

        Slimefun.registerResearch(new Research(7577, "X-Ray Helmet", 100), new ItemStack[] { SlimefunItemsHolder.X_RAY_HELMEY });


        setupTitanBox();

    }
    private void setupTitanBox()
    {
        new SlimefunItem(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("A"), "Storage Unit, Size:" + ItemHolder.UNIT_A.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.LOG), new ItemStack(Material.WOOD_STEP), new ItemStack(Material.LOG),
                new ItemStack(Material.LOG), me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BATTERY, new ItemStack(Material.LOG),
                new ItemStack(Material.LOG), new ItemStack(Material.LOG), new ItemStack(Material.LOG)}).register();
        new SlimefunItem(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("B"), "Storage Unit, Size:" + ItemHolder.UNIT_B.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BRONZE_INGOT, TitanBox.instants.getItem("A"), me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BRONZE_INGOT,
                        me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BRONZE_INGOT, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BATTERY, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BRONZE_INGOT,
                        me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BRONZE_INGOT, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BRONZE_INGOT, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BRONZE_INGOT}).register();
        new SlimefunItem(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("C"), "Storage Unit, Size:" + ItemHolder.UNIT_C.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, TitanBox.instants.getItem("B"), SlimefunItemsHolder.LuckyIngot,
                        SlimefunItemsHolder.LuckyIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BATTERY, SlimefunItemsHolder.LuckyIngot,
                        SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();
        new SlimefunItem(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("D"), "Storage Unit, Size:" + ItemHolder.UNIT_D.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.EclipseIngot, TitanBox.instants.getItem("C"), SlimefunItemsHolder.EclipseIngot,
                        SlimefunItemsHolder.EclipseIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BATTERY, SlimefunItemsHolder.EclipseIngot,
                        SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot}).register();
        new SlimefunItem(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("E"), "Storage Unit, Size:" + ItemHolder.UNIT_E.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.TitanIngot, TitanBox.instants.getItem("D"), SlimefunItemsHolder.TitanIngot,
                        SlimefunItemsHolder.TitanIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.BATTERY, SlimefunItemsHolder.TitanIngot,
                        SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot}).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsHolder.DIAMOND_WRITING_PLATE, "diamondwritingplate", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND),
                    new ItemStack(Material.DIAMOND), new ItemStack(Material.PAPER), new ItemStack(Material.DIAMOND),
                    new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND)}).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsHolder.EMERALD_WRITING_PLATE, "emeraldwritingplate", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD),
                        new ItemStack(Material.EMERALD), new ItemStack(Material.PAPER), new ItemStack(Material.EMERALD),
                        new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD)}).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsHolder.ENDER_WRITING_PLATE, "enderwritingplate", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL),
                        new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.PAPER), new ItemStack(Material.ENDER_PEARL),
                        new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL)}).register();

        for(ModuleTypeEnum mte: ModuleTypeEnum.values())
        {
            new SlimefunItem(CustomCategories.STORAGE_MODULES, TitanBox.instants.getItem("module", mte.getType()), ChatColor.stripColor(mte.getTitle()), RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[] {SlimefunItemsHolder.DIAMOND_WRITING_PLATE, SlimefunItemsHolder.DIAMOND_WRITING_PLATE, SlimefunItemsHolder.DIAMOND_WRITING_PLATE,
                            SlimefunItemsHolder.ENDER_WRITING_PLATE, mte.getCrafter(), SlimefunItemsHolder.ENDER_WRITING_PLATE,
                            SlimefunItemsHolder.EMERALD_WRITING_PLATE, SlimefunItemsHolder.EMERALD_WRITING_PLATE, SlimefunItemsHolder.EMERALD_WRITING_PLATE}).register();
        }

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("router"), "Item Routing Router", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT,
                        new ItemStack(Material.WORKBENCH), new ItemStack(Material.BOOKSHELF), new ItemStack(Material.WORKBENCH),
                        SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("killer"), "Killer Block", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,
                        me.mrCookieSlime.Slimefun.Lists.SlimefunItems.STEEL_INGOT, new ItemStack(Material.DIAMOND_SWORD), me.mrCookieSlime.Slimefun.Lists.SlimefunItems.STEEL_INGOT,
                        SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("water"), "Water Pump", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,
                        new ItemStack(Material.WATER_BUCKET), me.mrCookieSlime.Slimefun.Lists.SlimefunItems.FLUID_PUMP, new ItemStack(Material.WATER_BUCKET),
                        SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("lava"), "Lava Pump", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,
                        new ItemStack(Material.LAVA_BUCKET), me.mrCookieSlime.Slimefun.Lists.SlimefunItems.FLUID_PUMP, new ItemStack(Material.LAVA_BUCKET),
                        SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("ice"), "Ice Extractor", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,
                        me.mrCookieSlime.Slimefun.Lists.SlimefunItems.STEEL_INGOT, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.FREEZER, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.STEEL_INGOT,
                        SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("item"), "Item Sucker", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,
                        me.mrCookieSlime.Slimefun.Lists.SlimefunItems.STEEL_INGOT, new ItemStack(Material.HOPPER), me.mrCookieSlime.Slimefun.Lists.SlimefunItems.STEEL_INGOT,
                        SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, Elevator.getMeAsDrop(), "Elevator", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.IRON_INGOT), new ItemStack(Material.PISTON_BASE), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.PISTON_BASE), new ItemStack(Material.IRON_INGOT)}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, BackpackRecover.getMeAsDrop(), "Backpack Recover", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,
                        new ItemStack(Material.WOOL), new ItemStack(Material.WORKBENCH), new ItemStack(Material.WOOL),
                        SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();

        new SlimefunItem(CustomCategories.STORAGE_ROUTING, StorageRecover.getMeAsDrop(), "Storage Recover", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,
                    new ItemStack(Material.WOOL), new ItemStack(Material.DISPENSER), new ItemStack(Material.WOOL),
                    SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();

        new SlimefunItem(CustomCategories.ELECTRICITY, NetworkMonitor.getMeAsDrop(), "NetworkMonitor", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT,
                        new ItemStack(Material.GLASS), new ItemStack(Material.REDSTONE_LAMP_OFF), new ItemStack(Material.GLASS),
                        SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT}).register();


        Slimefun.registerResearch(new Research(7578, "Advanced Storage", 50), new ItemStack[] { TitanBox.instants.getItem("C"), TitanBox.instants.getItem("D"), TitanBox.instants.getItem("E") });
        Slimefun.registerResearch(new Research(7579, "Advanced Coding", 25), new ItemStack[] { SlimefunItemsHolder.DIAMOND_WRITING_PLATE, SlimefunItemsHolder.EMERALD_WRITING_PLATE, SlimefunItemsHolder.ENDER_WRITING_PLATE });

        ItemStack[] AllModules = new ItemStack[ModuleTypeEnum.values().length];
        int i =0;
        for(ModuleTypeEnum mte: ModuleTypeEnum.values())
        {
            AllModules[i] = TitanBox.instants.getItem("module", mte.getType());
            i++;
        }
        Slimefun.registerResearch(new Research(7580, "Modules", 25), AllModules);
        Slimefun.registerResearch(new Research(7581, "Advanced Fluid Dynamics", 35), new ItemStack[] { Pumps.getMeAsDrop("killer"), Pumps.getMeAsDrop("water"), Pumps.getMeAsDrop("lava"), Pumps.getMeAsDrop("ice"), Pumps.getMeAsDrop("item")});
        Slimefun.registerResearch(new Research(7582, "The Flaws In Backpacks", 5), new ItemStack[] { BackpackRecover.getMeAsDrop(), StorageRecover.getMeAsDrop() });
    }
    private void setupTitanSet() {
        ItemStack Reward =  new ItemStack(SlimefunItemsHolder.TitanNugget);
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.TitanNuggetB, "TitanNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {null,null, SlimefunItemsHolder.TitanAxe, SlimefunItemsHolder.TitanBoots, SlimefunItemsHolder.TitanChestplate, SlimefunItemsHolder.TitanHelmet, SlimefunItemsHolder.TitanLeggings, SlimefunItemsHolder.TitanSword, SlimefunItemsHolder.TitanPickaxe}).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.TitanNugget, "TitanNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.TitanAxe, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.TitanNugget, "TitanNugget ", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.TitanBoots, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.TitanNugget, "TitanNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.TitanChestplate, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.TitanNugget, "TitanNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.TitanHelmet, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.TitanNugget, "TitanNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.TitanLeggings, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.TitanNugget, "TitanNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.TitanSword, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.TitanNugget, "TitanNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.TitanPickaxe, null, null, null, null, null, null, null, null}, Reward).register();

        Reward = SlimefunItemsHolder.TitanNugget.clone();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.TitanIngot, "TitanIgnot", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsHolder.TitanAxe, "TitanAxe", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, null, SlimefunItemsHolder.TitanIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsHolder.TitanSword, "TitanSword", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsHolder.TitanIngot, null, null, SlimefunItemsHolder.TitanIngot,null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsHolder.TitanPickaxe, "TitanPickaxe", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsHolder.TitanHelmet, "TitanHelmet", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, null, SlimefunItemsHolder.TitanIngot, null, null, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsHolder.TitanChestplate, "TitanChestplate", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.TitanIngot, null, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsHolder.TitanLeggings, "TitanLeggings", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot,null, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, null, SlimefunItemsHolder.TitanIngot}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsHolder.TitanBoots, "TitanBoots", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, null, null, SlimefunItemsHolder.TitanIngot, null, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, null, SlimefunItemsHolder.TitanIngot}).register();

        //new SlimefunItem(CustomCategories.SLIMEFUN_MORE, SlimefunItemsHolder.X_RAY_HELMEY, "XRayHelmet", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.CARGO_MOTOR, SlimefunItemsHolder.TitanIngot, SlimefunItemsHolder.TitanIngot, null, SlimefunItemsHolder.TitanIngot, null, null, null}).register();
    }
    private void setupEclipseSet() {
        ItemStack Reward =  new ItemStack(SlimefunItemsHolder.EclipseNugget);
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.EclipseNuggetB, "EclipseNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {null,null, SlimefunItemsHolder.EclipseAxe, SlimefunItemsHolder.EclipseBoots, SlimefunItemsHolder.EclipseChestplate, SlimefunItemsHolder.EclipseHelmet, SlimefunItemsHolder.EclipseLeggings, SlimefunItemsHolder.EclipseSword, SlimefunItemsHolder.EclipsePickaxe}).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.EclipseNugget, "EclipseNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.EclipseAxe, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.EclipseNugget, "EclipseNugget ", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.EclipseBoots, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.EclipseNugget, "EclipseNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.EclipseChestplate, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.EclipseNugget, "EclipseNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.EclipseHelmet, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.EclipseNugget, "EclipseNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.EclipseLeggings, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.EclipseNugget, "EclipseNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.EclipseSword, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.EclipseNugget, "EclipseNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.EclipsePickaxe, null, null, null, null, null, null, null, null}, Reward).register();

        Reward = SlimefunItemsHolder.EclipseNugget.clone();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.EclipseIngot, "EclipseIgnot", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_ECLIPSE, SlimefunItemsHolder.EclipseAxe, "EclipseAxe", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_ECLIPSE, SlimefunItemsHolder.EclipseSword, "EclipseSword", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsHolder.EclipseIngot, null, null, SlimefunItemsHolder.EclipseIngot,null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_ECLIPSE, SlimefunItemsHolder.EclipsePickaxe, "EclipsePickaxe", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_ECLIPSE, SlimefunItemsHolder.EclipseHelmet, "EclipseHelmet", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot, null, null, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_ECLIPSE, SlimefunItemsHolder.EclipseChestplate, "EclipseChestplate", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_ECLIPSE, SlimefunItemsHolder.EclipseLeggings, "EclipseLeggings", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot,null, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_ECLIPSE, SlimefunItemsHolder.EclipseBoots, "EclipseBoots", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, null, null, SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot, SlimefunItemsHolder.EclipseIngot, null, SlimefunItemsHolder.EclipseIngot}).register();

    }
    private void setupLuckySet() {
        ItemStack Reward = SlimefunItemsHolder.LuckyBlock.clone();

        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.LuckyNugget.clone(), "LuckyNugget", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        Reward =  new ItemStack(SlimefunItemsHolder.LuckyNugget);
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.LuckyNuggetB, "LuckyNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {null,null, SlimefunItemsHolder.LuckyAxe, SlimefunItemsHolder.LuckyBoots, SlimefunItemsHolder.LuckyChestplate, SlimefunItemsHolder.LuckyHelmet, SlimefunItemsHolder.LuckyLeggings, SlimefunItemsHolder.LuckySword, SlimefunItemsHolder.LuckyPickaxe}).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.LuckyNugget, "LuckyNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.LuckyAxe, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.LuckyNugget, "LuckyNugget ", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.LuckyBoots, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.LuckyNugget, "LuckyNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.LuckyChestplate, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.LuckyNugget, "LuckyNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.LuckyHelmet, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.LuckyNugget, "LuckyNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.LuckyLeggings, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.LuckyNugget, "LuckyNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.LuckySword, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsHolder.LuckyNugget, "LuckyNugget", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsHolder.LuckyPickaxe, null, null, null, null, null, null, null, null}, Reward).register();

        Reward = SlimefunItemsHolder.LuckyNugget.clone();
        Reward.setAmount(3);
        new SlimefunItem(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsHolder.LuckyIngot, "LuckyIgnot", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        new SlimefunItem(CustomCategories.SLIMEFUN_LUCKY, SlimefunItemsHolder.LuckyAxe, "LuckyAxe", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, null, SlimefunItemsHolder.LuckyIngot, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_LUCKY, SlimefunItemsHolder.LuckySword, "LuckySword", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsHolder.LuckyIngot, null, null, SlimefunItemsHolder.LuckyIngot,null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_LUCKY, SlimefunItemsHolder.LuckyPickaxe, "LuckyPickaxe", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null, null, me.mrCookieSlime.Slimefun.Lists.SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_LUCKY, SlimefunItemsHolder.LuckyHelmet, "LuckyHelmet", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, null, SlimefunItemsHolder.LuckyIngot, null, null, null}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_LUCKY, SlimefunItemsHolder.LuckyChestplate, "LuckyChestplate", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.LuckyIngot, null, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_LUCKY, SlimefunItemsHolder.LuckyLeggings, "LuckyLeggings", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot,null, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, null, SlimefunItemsHolder.LuckyIngot}).register();
        new SlimefunItem(CustomCategories.SLIMEFUN_LUCKY, SlimefunItemsHolder.LuckyBoots, "LuckyBoots", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, null, null, SlimefunItemsHolder.LuckyIngot, null, SlimefunItemsHolder.LuckyIngot, SlimefunItemsHolder.LuckyIngot, null, SlimefunItemsHolder.LuckyIngot}).register();
    }
    public static void setupVanillaCraft()
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, new Runnable() {
            @Override
            public void run() {

                Iterator iterator2 = Bukkit.recipeIterator();
                while (iterator2.hasNext()) {
                    Recipe r = (Recipe) iterator2.next();
                    if (r instanceof ShapelessRecipe)
                    {
                        ShapelessRecipe SR = (ShapelessRecipe)r;
                        List<ItemStack> spless =  SR.getIngredientList();
                        String myName = "";
                        Boolean goodRec = false;
                        int lasto = 0;
                        for (int o = 0; o < spless.size();o++ )
                        {
                            if (spless.get(o) == null || spless.get(o).getType() == Material.AIR)
                            {
                                myName = myName + "null" + ChatColor.GRAY;
                            }
                            else {
                                goodRec = true;
                                myName = myName + spless.get(o).getType().toString() + ":" + spless.get(o).getDurability() + ChatColor.GRAY;
                            }
                            lasto = o;
                        }
                        lasto++;
                        for (int o = lasto; o < 9;o++ )
                        {
                            myName = myName + "null" + ChatColor.GRAY;
                        }
                        if (goodRec) {
                            recipesV.put(myName, r.getResult());
                        }
                    }
                    if (r instanceof ShapedRecipe)
                    {
                        ShapedRecipe SR = (ShapedRecipe)r;
                        String[] shapeS = SR.getShape();
                        Map<Character, ItemStack> MapCM = SR.getIngredientMap();
                        ItemStack[] Reci = {null, null, null, null, null, null, null, null, null};
                        String myName = "";
                        Character[] key = new Character[9];
                        int counter = 0;
                        int[] yH = {0,1,2,3,4,5,6,7,8};//{0,3,6,1,4,7,2,5,8};
                        String teShape = "";
                        for (int o = 0; o < shapeS.length;o++ )
                        {
                            shapeS[o] = shapeS[o] + "***********";
                            shapeS[o] = shapeS[o].substring(0, 3);

                            for (int p = 0; p < shapeS[o].length();p++ )
                            {
                                key[yH[counter]] = shapeS[o].charAt(p);
                                counter++;
                            }
                            teShape = teShape + shapeS[o]  + "<>";
                        }
                        for (int o = shapeS.length; o < 3;o++ )
                        {
                            String missed = "***";

                            for (int p = 0; p < missed.length();p++ )
                            {
                                key[yH[counter]] = missed.charAt(p);
                                counter++;
                            }
                            teShape = teShape + "XXX"  + "<>";
                        }
                        Short Dura = SR.getResult().getDurability();

                        boolean goodRec = false;
                        for (int o = 0; o < 9;o++ )
                        {

                            Reci[o] = MapCM.get(key[o]);

                            if (Reci[o] == null || Reci[o].getType() == Material.AIR)
                            {
                                myName = myName + "null" + ChatColor.GRAY;
                            }
                            else {
                                goodRec = true;
                                myName = myName + Reci[o].getType().toString() + ":" + Reci[o].getDurability() + ChatColor.GRAY;
                            }
                        }
                        if (goodRec) {
                            recipesV.put(myName, r.getResult());
                        }
                    }
                }
                System.out.println("[Slimefun4]: All vinilla recipes are loaded!");
            }
        }, 300);
    }


    private ItemStack makeTitanStone() {
//        getCommand("pm").setTabCompleter(new TabComplete());
        ItemStack Soulbound = new ItemStack(Material.NETHER_WART_BLOCK);
        ItemMeta Soulboundmeta =  Soulbound.getItemMeta();
        Soulboundmeta.setDisplayName(ChatColor.AQUA + "Titan Stone");
        Soulboundmeta.addEnchant(Enchantment.LUCK, 10, true);
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GOLD + "Forged in the heart of Admin Mountain.");
        Soulboundmeta.setLore(lore);
        Soulbound.setItemMeta(Soulboundmeta);
        return Soulbound;
    }
}

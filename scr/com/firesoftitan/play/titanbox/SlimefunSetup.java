package com.firesoftitan.play.titanbox;

import com.firesoftitan.play.titanbox.custom.CustomCategories;
import com.firesoftitan.play.titanbox.enums.ItemEnum;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.managers.ConfigManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.TitanItemManager;
import com.firesoftitan.play.titanbox.items.TitanTalisman;
import com.firesoftitan.play.titanbox.machines.*;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutoAnvil;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyTicker;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class SlimefunSetup {

    public static Map<String, ItemStack> recipesV = new HashMap<String, ItemStack>();
    public SlimefunSetup()
    {
        SlimefunItemsManager.TitanStone = makeTitanStone();

        if (ConfigManager.isRemove_cargo()) removeAllCargo();

        if (ConfigManager.isRemove_androids()) removeAllAndroids();

        registerItems();

        setupVanillaCraft();


    }
    private void removeAllAndroids()
    {

        removeSlimefunItem("PROGRAMMABLE_ANDROID");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_MINER");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_FARMER");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_WOODCUTTER");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_FISHERMAN");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_BUTCHER");
        removeSlimefunItem("ANDROID_INTERFACE_ITEMS");
        removeSlimefunItem("ANDROID_INTERFACE_FUEL");

        removeSlimefunItem("PROGRAMMABLE_ANDROID_2");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_2_FARMER");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_2_FISHERMAN");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_2_BUTCHER");

        removeSlimefunItem("PROGRAMMABLE_ANDROID_3");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_3_FISHERMAN");
        removeSlimefunItem("PROGRAMMABLE_ANDROID_3_BUTCHER");

        removeSlimefunItem("CROP_GROWTH_ACCELERATOR");
    }
    private void removeAllCargo()
    {
        removeSlimefunItem("CARGO_MANAGER");
        removeSlimefunItem("CARGO_NODE");
        removeSlimefunItem("CARGO_NODE_INPUT");
        removeSlimefunItem("CARGO_NODE_OUTPUT");
        removeSlimefunItem("CARGO_NODE_OUTPUT_ADVANCED");
    }
    private void removeSlimefunItem(String ID)
    {
        try {
            SlimefunItem slimefunItem = SlimefunItem.getByID(ID);
            SlimefunItem.map_id.remove(slimefunItem.getID());
            for(int i = 0; i < SlimefunItem.items.size(); i++)
            {
                SlimefunItem item = SlimefunItem.items.get(i);
                if (item.getID() == slimefunItem.getID())
                {
                    SlimefunItem.items.remove(i);
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Error removing: " + ID);
        }
    }
    public void registerItems()
    {


        ItemStack Reward =  new ItemStack(SlimefunItemsManager.TitanStone);
        Reward.setAmount(16);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.TitanStone, "TITANSTONE", RecipeType.ANCIENT_ALTAR, new ItemStack[] {SlimefunItems.POWER_CRYSTAL, SlimefunItems.POWER_CRYSTAL, SlimefunItems.POWER_CRYSTAL, SlimefunItems.POWER_CRYSTAL, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.POWER_CRYSTAL, SlimefunItems.POWER_CRYSTAL, SlimefunItems.POWER_CRYSTAL, SlimefunItems.POWER_CRYSTAL}, Reward.clone()).register();

        Reward =  new ItemStack(SlimefunItemsManager.EMPTY_JAR);
        Reward.setAmount(7);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.EMPTY_JAR, "EMPTY_JAR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_METAL_INGOT ,SlimefunItems.HARDENED_GLASS, null,SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS, SlimefunItems.HARDENED_GLASS}, Reward.clone()).register();


        Reward =  new ItemStack(SlimefunItemsManager.TITAN_MOTOR);
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.TITAN_MOTOR, "TITANMOTOR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot,null, SlimefunItems.ELECTRO_MAGNET,null, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot}, Reward.clone()).register();
        Reward =  new ItemStack(SlimefunItemsManager.ECLIPSE_COIL);
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.ECLIPSE_COIL, "ECLIPSECOIL", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItems.ELECTRIC_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot}, Reward.clone()).register();

        Reward =  new ItemStack(SlimefunItemsManager.FORCE_FIELD_RESONATOR);
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.FORCE_FIELD_RESONATOR, "FORCE_FIELD_RESONATOR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {SlimefunItems.HOLOGRAM_PROJECTOR, SlimefunItems.GILDED_IRON, SlimefunItems.HOLOGRAM_PROJECTOR,SlimefunItems.COOLING_UNIT, SlimefunItems.HEATING_COIL,SlimefunItems.COOLING_UNIT, SlimefunItems.GILDED_IRON, SlimefunItems.HOLOGRAM_PROJECTOR, SlimefunItems.GILDED_IRON}, Reward.clone()).register();

        Reward =  new ItemStack(SlimefunItemsManager.DATA_CENTER);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.DATA_CENTER, "DATACENTER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.STONE), new ItemStack(Material.REDSTONE), new ItemStack(Material.STONE), new ItemStack(Material.BOOKSHELF), new ItemStack(Material.EMERALD), new ItemStack(Material.BOOKSHELF), new ItemStack(Material.STONE), new ItemStack(Material.REDSTONE), new ItemStack(Material.STONE)},
                Reward.clone()).register();

        Reward =  new ItemStack(SlimefunItemsManager.FLY_ORB);
        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.FLY_ORB, "FLY_ORB", RecipeType.MAGIC_WORKBENCH,
                new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.CARBONADO_JETPACK,  SlimefunItemsManager.ECLIPSE_COIL,  SlimefunItemsManager.TitanIngot,  SlimefunItemsManager.TITAN_MOTOR,  SlimefunItemsManager.TitanIngot},
                Reward.clone()).register();

        Reward =  new ItemStack(SlimefunItemsManager.STORAGE_MOVER);
        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.STORAGE_ROUTING, SlimefunItemsManager.STORAGE_MOVER, "STORAGE_MOVER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.ENDER_PEARL), null,  null, new ItemStack(Material.SHULKER_BOX), null, null, new ItemStack(Material.ENDER_PEARL), null},
                Reward.clone()).register();

        Reward =  new ItemStack(SlimefunItemsManager.LOOT_KEY);
        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.LOOT_KEY, "LOOT_KEY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.DIAMOND), SlimefunItems.STONE_CHUNK, null, new ItemStack(Material.DIAMOND), SlimefunItems.STONE_CHUNK, null, new ItemStack(Material.EMERALD), null},
                Reward.clone()).register();

        Reward =  new ItemStack(SlimefunItemsManager.WILD_HOE);
        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.WILD_HOE, "WILD_HOE", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.ENDER_PEARL), null, null, new ItemStack(Material.STICK), null, null, new ItemStack(Material.STICK), null},
                Reward.clone()).register();
        Reward =  new ItemStack(SlimefunItemsManager.WILD_HOE_II);


        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.WILD_HOE_II, "WILD_HOE_II", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItemsManager.WILD_HOE, null, null, new ItemStack(Material.IRON_INGOT), null, null, new ItemStack(Material.IRON_INGOT), null},
                Reward.clone()).register();
        Reward =  new ItemStack(SlimefunItemsManager.WILD_HOE_III);
        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.WILD_HOE_III, "WILD_HOE_III", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItemsManager.WILD_HOE_II, null, null, new ItemStack(Material.DIAMOND), null, null, new ItemStack(Material.DIAMOND), null},
                Reward.clone()).register();
        Reward =  new ItemStack(SlimefunItemsManager.X_RAY_HELMET);
        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.X_RAY_HELMET, "X_RAY_HELMET", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, new ItemStack(Material.DIAMOND_HELMET), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, new ItemStack(Material.GLASS_PANE), SlimefunItemsManager.LuckyIngot, null, null, null},
                Reward.clone()).register();
        Reward =  new ItemStack(SlimefunItemsManager.CAVE_VIEW_HELMET);
        Reward.setAmount(1);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.CAVE_VIEW_HELMET, "CAVE_VIEW_HELMET", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, new ItemStack(Material.GOLDEN_HELMET), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, new ItemStack(Material.GLASS_PANE), SlimefunItemsManager.LuckyIngot, null, null, null},
                Reward.clone()).register();


        Reward =  new ItemStack(Material.GUNPOWDER);
        Reward.setAmount(8);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, Reward, "GUNPOWERDER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.CHARCOAL), SlimefunItems.SULFATE, new ItemStack(Material.CHARCOAL), SlimefunItemsManager.SALTPETER, new ItemStack(Material.CHARCOAL),  SlimefunItemsManager.SALTPETER, new ItemStack(Material.CHARCOAL), SlimefunItems.SULFATE, new ItemStack(Material.CHARCOAL)},
                Reward.clone()).register();


        setupLuckySet();

        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.EclipseNugget, "ECLIPSENUGGET", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.TitanStone, SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot, null}).register();
        setupEclipseSet();

        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.TitanNugget, "TITANNUGGET", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TitanStone, SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot, null}).register();
        setupTitanSet();

        new ElectricLuckyBlockGrinder(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_LUCKY_BLOCK_GRINDER, "ELECTRIC_LUCKY_BLOCK_GRINDER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItems.ELECTRIC_ORE_GRINDER, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.INGOTUP,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.LuckyIngot,  SlimefunItems.ELECTRIC_ORE_GRINDER,  SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 300;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);
        new ElectricLuckyBlockFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_LUCKY_BLOCK_FACTORY, "ELECTRIC_LUCKY_BLOCK_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.ELECTRIC_LUCKY_BLOCK_GRINDER,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.EclipseIngot,  SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 300;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoDust(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST, "ELECTRIC_COBLE_TO_DUST", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItems.ELECTRIC_DUST_WASHER,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.TITAN_MOTOR,  SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 100;
            }

            @Override
            public int getSpeed() {
                return 2;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoIngot(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT, "ELECTRIC_COBLE_TO_INGOT", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItems.ELECTRIC_INGOT_FACTORY,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.TITAN_MOTOR,  SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 200;
            }

            @Override
            public int getSpeed() {
                return 2;
            }
        }.registerChargeableBlock(true, 512);


        new ElectricCobbletoDust(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST_2, "ELECTRIC_COBLE_TO_DUST_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsManager.EclipseIngot, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST,  SlimefunItemsManager.EclipseIngot,  SlimefunItems.HARDENED_METAL_INGOT,  SlimefunItemsManager.EclipseIngot,  SlimefunItems.HARDENED_METAL_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 200;
            }

            @Override
            public int getSpeed() {
                return 10;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoIngot(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT_2, "ELECTRIC_COBLE_TO_INGOT_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsManager.EclipseIngot, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT,  SlimefunItemsManager.EclipseIngot,  SlimefunItems.HARDENED_METAL_INGOT,  SlimefunItemsManager.EclipseIngot,  SlimefunItems.HARDENED_METAL_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 300;
            }

            @Override
            public int getSpeed() {
                return 10;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoDust(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST_3, "ELECTRIC_COBLE_TO_DUST_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsManager.TitanIngot, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST_2,  SlimefunItemsManager.TitanIngot,  SlimefunItems.REINFORCED_ALLOY_INGOT,  SlimefunItemsManager.TitanIngot,  SlimefunItems.REINFORCED_ALLOY_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 300;
            }

            @Override
            public int getSpeed() {
                return 20;
            }
        }.registerChargeableBlock(true, 512);

        new ElectricCobbletoIngot(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT_3, "ELECTRIC_COBLE_TO_INGOT_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsManager.TitanIngot, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT_2,  SlimefunItemsManager.TitanIngot,  SlimefunItems.REINFORCED_ALLOY_INGOT,  SlimefunItemsManager.TitanIngot,  SlimefunItems.REINFORCED_ALLOY_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 400;
            }

            @Override
            public int getSpeed() {
                return 20;
            }
        }.registerChargeableBlock(true, 512);



        /*new AutomatedVanillaCraftingChamber(CustomCategories.ELECTRICITY, SlimefunItemsManager.AUTOMATED_VANILLA_CRAFTING_CHAMBER, "AUTOMATED_VANILLA_CRAFTING_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.CRAFTING_TABLE), null, SlimefunItems.CARGO_MOTOR, SlimefunItems.COPPER_INGOT, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public int getEnergyConsumption() {
                return 5;
            }
        }.registerChargeableBlock(true, 256);*/

        new AncientAltarCrafter(CustomCategories.ELECTRICITY, SlimefunItemsManager.ANCIENT_ALTAR_CRAFTER, "ANCIENT_ALTAR_CRAFTER_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.ANCIENT_PEDESTAL, null, SlimefunItems.CARGO_MOTOR, SlimefunItems.ANCIENT_ALTAR, SlimefunItems.CARGO_MOTOR, SlimefunItems.ANCIENT_PEDESTAL, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ANCIENT_PEDESTAL}) {

            @Override
            public int getEnergyConsumption() {
                return 250;
            }
        }.registerChargeableBlock(true, 256);

        new AutomatedAncientAltarCrafter(CustomCategories.ELECTRICITY, SlimefunItemsManager.AUTOMATED_ANCIENT_ALTAR_CRAFTER, "AUTOMATED_ANCIENT_ALTAR_CRAFTER_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, new ItemStack(Material.CRAFTING_TABLE), null, SlimefunItems.CARGO_MOTOR, SlimefunItemsManager.ANCIENT_ALTAR_CRAFTER, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.ELECTRIC_MOTOR, null}) {

            @Override
            public int getEnergyConsumption() {
                return 250;
            }
        }.registerChargeableBlock(true, 256);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.BONE_FACTORY, "BONE_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.REPAIRED_SPAWNER_SKELETON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.REPAIRED_SPAWNER_SKELETON},
                Material.BONE, World.Environment.NORMAL) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);

        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.IRON_FACTORY, "IRON_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.REPAIRED_SPAWNER_VILLAGER, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.REPAIRED_SPAWNER_VILLAGER},
                Material.IRON_INGOT, World.Environment.NORMAL) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.XP_FACTORY, "XP_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.REPAIRED_SPAWNER_PIG, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.REPAIRED_SPAWNER_PIG},
                new CustomItem(Material.EXPERIENCE_BOTTLE, "&aFlask of Knowledge", 0), World.Environment.NORMAL) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.SLIME_FACTORY, "SLIME_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.REPAIRED_SPAWNER_SLIME, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.REPAIRED_SPAWNER_SLIME},
                Material.SLIME_BALL, World.Environment.NORMAL) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.BLAZE_FACTORY, "BLAZE_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.REPAIRED_SPAWNER_BLAZE, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.REPAIRED_SPAWNER_BLAZE},
                Material.BLAZE_ROD, World.Environment.NETHER) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.ENDERMAN_FACTORY, "ENDERMAN_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.REPAIRED_SPAWNER_ENDERMAN, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.REPAIRED_SPAWNER_ENDERMAN},
                Material.ENDER_PEARL, World.Environment.THE_END) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.NETHERRACK_FACTORY, "NETHERRACK_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL,  new ItemStack(Material.NETHERRACK), SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.FREE_ENERGY_I, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.FREE_ENERGY_I},
                Material.NETHERRACK, World.Environment.NETHER) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.SOULSAND_FACTORY, "SOULSAND_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL,  new ItemStack(Material.SOUL_SAND), SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.FREE_ENERGY_I, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.FREE_ENERGY_I},
                Material.SOUL_SAND, World.Environment.NETHER) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.NETHERWART_FACTORY, "NETHERWART_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, new ItemStack(Material.NETHER_WART), SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.FREE_ENERGY_II, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.FREE_ENERGY_II},
                Material.NETHER_WART, World.Environment.NETHER) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.WITHERSKULL_FACTORY, "WITHERSKULL_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                                SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.ECLIPSE_COIL,
                                SlimefunItemsManager.REPAIRED_SPAWNER_WITHER, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.REPAIRED_SPAWNER_WITHER},
                new ItemStack(Material.WITHER_SKELETON_SKULL, 1), World.Environment.NETHER) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);
        new FreeFactory(CustomCategories.SLIMEFUN_FREE_THINGS, SlimefunItemsManager.QUARTZ_FACTORY, "QUARTZ_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL,  new ItemStack(Material.QUARTZ), SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItemsManager.FREE_ENERGY_I, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.FREE_ENERGY_I},
                Material.QUARTZ, World.Environment.NETHER) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 5000;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 10100);

        new ForceField(CustomCategories.ELECTRICITY, SlimefunItemsManager.FORCE_FIELD_BLUE, "FORCE_FIELD",  RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, new ItemStack(Material.NETHER_STAR), SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {
            @Override
            public int getSpeed() {
                return 15;
            }
        }.registerChargeableBlock(32000);

        new HeadRecovery(CustomCategories.SLIMEFUN_RECOVERY, SlimefunItemsManager.HEAD_RECOVERY, "HEAD_RECOVERY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new CharcoalFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.CHARCOAL_FACTORY, "CHARCOAL_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.ECLIPSE_COIL, new ItemStack(Material.FURNACE), SlimefunItemsManager.ECLIPSE_COIL,
                        SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new CharcoalFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.CHARCOAL_FACTORY_2, "CHARCOAL_FACTORY_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.CHARCOAL_FACTORY, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 10;
            }

        }.registerChargeableBlock(true, 200);
        new CharcoalFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.CHARCOAL_FACTORY_3, "CHARCOAL_FACTORY_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.CHARCOAL_FACTORY_2, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 20;
            }

        }.registerChargeableBlock(true, 200);

        new SpawnerEggExtractor(CustomCategories.ELECTRICITY, SlimefunItemsManager.SPAWNER_EGG_EXTRACTOR, "SPAWNER_EGG_EXTRACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItems.CARGO_MOTOR, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.LuckyIngot}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);

        new SpawnerReintegrater(CustomCategories.ELECTRICITY, SlimefunItemsManager.SPAWNER_REINTEGRATER, "SPAWNER_REINTEGRATER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItems.DUCT_TAPE, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.LuckyIngot}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);

        new LifeForceExtractor(CustomCategories.ELECTRICITY, SlimefunItemsManager.LIFE_FORCE_EXTRACTOR, "LIFE_FORCE_EXTRACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItems.BLADE_OF_VAMPIRES, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.LuckyIngot}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);


        new SoulExtractor(CustomCategories.ELECTRICITY, SlimefunItemsManager.SOUL_EXTRACTOR, "SOUL_EXTRACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItems.SOULBOUND_SHOVEL, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.LuckyIngot}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);

        new TNTFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.TNT_FACTORY, "TNT_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, new ItemStack(Material.GUNPOWDER), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, new ItemStack(Material.FLINT_AND_STEEL), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, new ItemStack(Material.GUNPOWDER), SlimefunItemsManager.LuckyIngot}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new BlockAssemblerDisassembler(CustomCategories.ELECTRICITY, SlimefunItemsManager.BLOCK_ASSEMBLER_DISASSEMBLER, "BLOCK_ASSEMBLER_DISASSEMBLER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, new ItemStack(Material.PISTON), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, new ItemStack(Material.SLIME_BLOCK), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, new ItemStack(Material.PISTON), SlimefunItemsManager.LuckyIngot}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new LuckyBlockOpener(CustomCategories.ELECTRICITY, SlimefunItemsManager.LUCKY_BLOCK_OPENER, "LUCKY_BLOCK_OPENER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.PandorasBox, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyBlock, new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItemsManager.LuckyBlock, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.PandorasBox, SlimefunItemsManager.LuckyIngot}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);

        new SaplingFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.SAPLING_FACTORY, "SAPLING_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.EclipseIngot, new ItemStack(Material.GOLDEN_HOE), SlimefunItemsManager.EclipseIngot, SlimefunItems.GILDED_IRON, SlimefunItemsManager.LuckyIngot, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new SaplingFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.SAPLING_FACTORY_2, "SAPLING_FACTORY_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.SAPLING_FACTORY, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.EclipseIngot, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 10;
            }

        }.registerChargeableBlock(true, 200);
        new SaplingFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.SAPLING_FACTORY_3, "SAPLING_FACTORY_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.SAPLING_FACTORY_2, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 20;
            }

        }.registerChargeableBlock(true, 200);
        new ElectricRenamer(CustomCategories.ELECTRICITY, SlimefunItemsManager.ELECTRIC_RENAMER, "ELECTRIC_RENAMER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, new ItemStack(Material.NAME_TAG), SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new LogFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.LOG_FACTORY, "LOG_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.EclipseIngot, new ItemStack(Material.GOLDEN_AXE), SlimefunItemsManager.EclipseIngot, SlimefunItems.GILDED_IRON, SlimefunItemsManager.LuckyIngot, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new LogFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.LOG_FACTORY_2, "LOG_FACTORY_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.LOG_FACTORY, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.EclipseIngot, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 10;
            }

        }.registerChargeableBlock(true, 200);
        new LogFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.LOG_FACTORY_3, "LOG_FACTORY_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.LOG_FACTORY_2, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 20;
            }

        }.registerChargeableBlock(true, 200);
        new FisherMachine(CustomCategories.ELECTRICITY, SlimefunItemsManager.FISHER_MACHINE, "FISHER_MACHINE", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, new ItemStack(Material.FISHING_ROD), SlimefunItemsManager.EclipseIngot, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new StrippedLogFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.STRIPPED_LOG_FACTORY, "STRIPPED_LOG_FACTORY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, new ItemStack(Material.DIAMOND_AXE), SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }

        }.registerChargeableBlock(true, 200);
        new StrippedLogFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.STRIPPED_LOG_FACTORY_2, "STRIPPED_LOG_FACTORY_2", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.STRIPPED_LOG_FACTORY, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 10;
            }

        }.registerChargeableBlock(true, 200);
        new StrippedLogFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.STRIPPED_LOG_FACTORY_3, "STRIPPED_LOG_FACTORY_3", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.STRIPPED_LOG_FACTORY_2, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItems.GILDED_IRON, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.GILDED_IRON}) {

            @Override
            public void registerDefaultRecipes() {
            }

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 20;
            }

        }.registerChargeableBlock(true, 200);

        new NuggettoIngotFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.NUGGETTOINGOT, "NUGGET_TO_IGNOT", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, new ItemStack(Material.BOOKSHELF), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,  new ItemStack(Material.DISPENSER),  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.LuckyIngot,   new ItemStack(Material.BOOKSHELF),  SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);
        new IngotUpFactory(CustomCategories.ELECTRICITY, SlimefunItemsManager.INGOTUP, "INGOT_UP", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, new ItemStack(Material.CRAFTING_TABLE), SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.NUGGETTOINGOT,  SlimefunItemsManager.LuckyIngot,  SlimefunItemsManager.LuckyIngot,   new ItemStack(Material.BOOKSHELF),  SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 512);

        new XPPlate(CustomCategories.ELECTRICITY, SlimefunItemsManager.XPPLATE, "XP_PLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {
                        SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.DIAMOND_WRITING_PLATE, SlimefunItemsManager.TITAN_MOTOR,
                        SlimefunItemsManager.EMERALD_WRITING_PLATE, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.EMERALD_WRITING_PLATE,
                        SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.DIAMOND_WRITING_PLATE, SlimefunItemsManager.TITAN_MOTOR}) {

            @Override
            public int getEnergyConsumption() {
                return 150;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 200);


        new TitanItemManager(CustomCategories.ELECTRICITY, SlimefunItemsManager.THERMAL_GENERATOR, "THERMAL_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.HEATING_COIL, SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.HEATING_COIL, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HEATING_COIL, SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.HEATING_COIL})
                .register(true, new EnergyTicker() {

                    @Override
                    public double generateEnergy(Location l, SlimefunItem item, Config data) {
                        try {
                            if (l == null) {
                                return 0;
                            }
                            if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) return 0D;
                            Location lavaCheck = l.clone().add(0, -1, 0);
                            Location AirCheck = l.clone().add(0, 1, 0);
                            boolean Run = true;
                            boolean explode = false;
                            if (Utilities.isLoaded(l)) {
                                try {
                                    for (int x = -1; x < 2; x++) {
                                        for (int z = -1; z < 2; z++) {
                                            if (lavaCheck.clone().add(x, 0, z).getBlock().getType() != Material.LAVA) {
                                                Run = false;
                                            }
                                            if (!Utilities.isAir(AirCheck.clone().add(x, 0, z).getBlock().getType())) {
                                                explode = true;
                                            }
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) return 0D;
                            if (Run && !explode) {
                                double past = 2500 * (1D - (l.getBlockY() / 100D));
                                if (past < 100) past = 100;
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

        new FreeEnergy(CustomCategories.ELECTRICITY, SlimefunItemsManager.FREE_ENERGY_I, "FREE_ENERGY_I", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.TITAN_MOTOR,
                    SlimefunItems.NUCLEAR_REACTOR, SlimefunItemsManager.THERMAL_GENERATOR, SlimefunItems.NETHERSTAR_REACTOR,
                    SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.TITAN_MOTOR}, 500)
            .register(true, new EnergyTicker() {

                @Override
                public double generateEnergy(Location l, SlimefunItem item, Config data) {
                    if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) return 0D;
                    try {
                        if (FreeEnergy.checkForceFieldFor(l)) return 500;
                        return 0;
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

        new FreeEnergy(CustomCategories.ELECTRICITY, SlimefunItemsManager.FREE_ENERGY_II, "FREE_ENERGY_II", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.TITAN_MOTOR,
                    SlimefunItems.GILDED_IRON, SlimefunItemsManager.FREE_ENERGY_I, SlimefunItems.GILDED_IRON,
                    SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.TITAN_MOTOR}, 1000)
            .register(true, new EnergyTicker() {

                @Override
                public double generateEnergy(Location l, SlimefunItem item, Config data) {
                    if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) return 0D;
                    try {
                        if (FreeEnergy.checkForceFieldFor(l)) return 1000;
                        return 0;
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

        new FreeEnergy(CustomCategories.ELECTRICITY, SlimefunItemsManager.FREE_ENERGY_III, "FREE_ENERGY_III", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.TITAN_MOTOR,
                        SlimefunItems.GILDED_IRON, SlimefunItemsManager.FREE_ENERGY_II, SlimefunItems.GILDED_IRON,
                        SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.FORCE_FIELD_RESONATOR, SlimefunItemsManager.TITAN_MOTOR}, 2000)
                .register(true, new EnergyTicker() {

                    @Override
                    public double generateEnergy(Location l, SlimefunItem item, Config data) {
                        try {
                            if (FreeEnergy.checkForceFieldFor(l)) return 2000;
                            return 0;
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

        new TitanTalisman(SlimefunItemsManager.TALISMAN_VOID, "TALISMAN_VOID",
                new ItemStack[] {SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.ECLIPSE_COIL, SlimefunItemsManager.TITAN_MOTOR,
                        Pumps.getMeAsDrop("item"), SlimefunItems.TALISMAN, Pumps.getMeAsDrop("item"),
                        SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.TITAN_MOTOR},
                false, false, "void", new PotionEffect[0])
                .register(true);
        new AutoDisenchanter_2(CustomCategories.ELECTRICITY, SlimefunItemsManager.AUTO_DISENCHANTER_2, "AUTO_DISENCHANTER_II", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT,
                        SlimefunItems.CARGO_MOTOR, SlimefunItems.AUTO_DISENCHANTER,  SlimefunItems.CARGO_MOTOR,
                        SlimefunItems.REINFORCED_ALLOY_INGOT,  SlimefunItemsManager.TITAN_MOTOR,  SlimefunItems.REINFORCED_ALLOY_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 500;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 1000);

        new AutoEnchanter_2(CustomCategories.ELECTRICITY, SlimefunItemsManager.AUTO_ENCHANTER_2, "AUTO_ENCHANTER_II", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItemsManager.TITAN_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT,
                        SlimefunItems.CARGO_MOTOR, SlimefunItems.AUTO_ENCHANTER,  SlimefunItems.CARGO_MOTOR,
                        SlimefunItems.REINFORCED_ALLOY_INGOT,  SlimefunItemsManager.TITAN_MOTOR,  SlimefunItems.REINFORCED_ALLOY_INGOT}) {

            @Override
            public int getEnergyConsumption() {
                return 500;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(true, 1000);


        Slimefun.registerResearch(new Research(79001, "Thermal Power Plant", 89), SlimefunItemsManager.THERMAL_GENERATOR, SlimefunItemsManager.FREE_ENERGY_I, SlimefunItemsManager.FREE_ENERGY_II, SlimefunItemsManager.FREE_ENERGY_III);
        Slimefun.registerResearch(new Research(79002, "Ancient Altar Crafter", 75), SlimefunItemsManager.ANCIENT_ALTAR_CRAFTER);
        Slimefun.registerResearch(new Research(79003, "Vanilla Auto Crafter", 25), SlimefunItemsManager.AUTOMATED_VANILLA_CRAFTING_CHAMBER);
        Slimefun.registerResearch(new Research(79004, "Automated Ancient Altar Crafter", 25), SlimefunItemsManager.AUTOMATED_ANCIENT_ALTAR_CRAFTER);


        Slimefun.registerResearch(new Research(7501, "Titan Stone", 50), new ItemStack[] { SlimefunItemsManager.TitanStone });

        Slimefun.registerResearch(new Research(7505, "Lucky Nugget", 50), new ItemStack[] { SlimefunItemsManager.LuckyNugget,  SlimefunItemsManager.LuckyNuggetB});

        Slimefun.registerResearch(new Research(7513, "Eclipse Nugget", 50), new ItemStack[] { SlimefunItemsManager.EclipseNugget, SlimefunItemsManager.EclipseNuggetB });
        Slimefun.registerResearch(new Research(7514, "Eclipse Axe", 100), new ItemStack[] { SlimefunItemsManager.EclipseAxe });
        Slimefun.registerResearch(new Research(7515, "Eclipse Sword", 125), new ItemStack[] { SlimefunItemsManager.EclipseSword });
        Slimefun.registerResearch(new Research(7516, "Eclipse Pickaxe", 175), new ItemStack[] { SlimefunItemsManager.EclipsePickaxe });
        Slimefun.registerResearch(new Research(7517, "Eclipse Helmet", 100), new ItemStack[] { SlimefunItemsManager.EclipseHelmet });
        Slimefun.registerResearch(new Research(7518, "Eclipse Chestplate", 175), new ItemStack[] { SlimefunItemsManager.EclipseChestplate });
        Slimefun.registerResearch(new Research(7519, "Eclipse Leggings", 150), new ItemStack[] { SlimefunItemsManager.EclipseLeggings });
        Slimefun.registerResearch(new Research(7520, "Eclipse Boots", 100), new ItemStack[] { SlimefunItemsManager.EclipseBoots });

        Slimefun.registerResearch(new Research(7521, "Titan Nugget", 50), new ItemStack[] { SlimefunItemsManager.TitanNugget, SlimefunItemsManager.TitanNuggetB });
        Slimefun.registerResearch(new Research(7522, "Titan Axe", 100), new ItemStack[] { SlimefunItemsManager.TitanAxe });
        Slimefun.registerResearch(new Research(7523, "Titan Sword", 125), new ItemStack[] { SlimefunItemsManager.TitanSword });
        Slimefun.registerResearch(new Research(7524, "Titan Pickaxe", 175), new ItemStack[] { SlimefunItemsManager.TitanPickaxe });
        Slimefun.registerResearch(new Research(7525, "Titan Helmet", 100), new ItemStack[] { SlimefunItemsManager.TitanHelmet });
        Slimefun.registerResearch(new Research(7526, "Titan Chestplate", 175), new ItemStack[] { SlimefunItemsManager.TitanChestplate });
        Slimefun.registerResearch(new Research(7527, "Titan Leggings", 150), new ItemStack[] { SlimefunItemsManager.TitanLeggings });
        Slimefun.registerResearch(new Research(7528, "Titan Boots", 100), new ItemStack[] { SlimefunItemsManager.TitanBoots });

        Slimefun.registerResearch(new Research(7529, "Lucky Ingot", 50), new ItemStack[] { SlimefunItemsManager.LuckyIngot });
        Slimefun.registerResearch(new Research(7530, "Eclipse Ingot", 50), new ItemStack[] { SlimefunItemsManager.EclipseIngot });
        Slimefun.registerResearch(new Research(7531, "Titan Ingot", 50), new ItemStack[] { SlimefunItemsManager.TitanIngot });

        Slimefun.registerResearch(new Research(7532, "Electric Cobble to", 25), new ItemStack[] { SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST_3, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT_3, SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST_2, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT_2, SlimefunItemsManager.ELECTRIC_COBBLE_TO_DUST, SlimefunItemsManager.ELECTRIC_COBBLE_TO_INGOT, SlimefunItemsManager.ELECTRIC_LUCKY_BLOCK_FACTORY, SlimefunItemsManager.ELECTRIC_LUCKY_BLOCK_GRINDER });

        Slimefun.registerResearch(new Research(7537, "Titan Motor", 50), new ItemStack[] { SlimefunItemsManager.TITAN_MOTOR, SlimefunItemsManager.ECLIPSE_COIL });
        Slimefun.registerResearch(new Research(7538, "An easy life", 250), new ItemStack[] { SlimefunItemsManager.BLOCK_ASSEMBLER_DISASSEMBLER, SlimefunItemsManager.TNT_FACTORY, SlimefunItemsManager.LUCKY_BLOCK_OPENER, SlimefunItemsManager.CHARCOAL_FACTORY, SlimefunItemsManager.NUGGETTOINGOT, SlimefunItemsManager.INGOTUP, SlimefunItemsManager.CHARCOAL_FACTORY_2, SlimefunItemsManager.CHARCOAL_FACTORY_3, SlimefunItemsManager.BONE_FACTORY, SlimefunItemsManager.IRON_FACTORY, SlimefunItemsManager.XP_FACTORY, SlimefunItemsManager.SLIME_FACTORY, SlimefunItemsManager.ENDERMAN_FACTORY, SlimefunItemsManager.BLAZE_FACTORY, SlimefunItemsManager.TITAN_AUTO_ANVIL, SlimefunItemsManager.LIFE_FORCE_EXTRACTOR, SlimefunItemsManager.SPAWNER_EGG_EXTRACTOR, SlimefunItemsManager.SPAWNER_REINTEGRATER});
        Slimefun.registerResearch(new Research(7540, "Titan Talisman", 100), new ItemStack[] { SlimefunItemsManager.TALISMAN_VOID});

        Slimefun.registerResearch(new Research(7583, "X-Ray Helmet", 25), new ItemStack[] { SlimefunItemsManager.X_RAY_HELMET, SlimefunItemsManager.CAVE_VIEW_HELMET});

        Slimefun.registerResearch(new Research(9578, "Painful gas", 200), new ItemStack[] { SlimefunItemsManager.WITHERSKULL_FACTORY, SlimefunItemsManager.NETHERWART_FACTORY, SlimefunItemsManager.NETHERRACK_FACTORY, SlimefunItemsManager.QUARTZ_FACTORY, SlimefunItemsManager.SOULSAND_FACTORY});
        Slimefun.registerResearch(new Research(9579, "Dark Memes", 150), new ItemStack[] { SlimefunItemsManager.AUTO_DISENCHANTER_2, SlimefunItemsManager.AUTO_ENCHANTER_2, SlimefunItemsManager.TITAN_AUTO_ANVIL, SlimefunItemsManager.TITAN_ITEM_RECOVERY, SlimefunItemsManager.TITAN_ITEM_RECOVERY_2, SlimefunItemsManager.TITAN_DEATH_RECOVERY, SlimefunItemsManager.TITAN_DEATH_RECOVERY_2});


        setupTitanBox();

    }
    private void setupTitanBox()
    {
        new TitanItemManager(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("A"), "STORAGE_UNIT_SIZE_" + ItemEnum.UNIT_A.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_SLAB), new ItemStack(Material.OAK_LOG),
                new ItemStack(Material.OAK_LOG), SlimefunItems.BATTERY, new ItemStack(Material.OAK_LOG),
                new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_LOG)}).register();
        new TitanItemManager(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("B"), "STORAGE_UNIT_SIZE_" + ItemEnum.UNIT_B.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.BRONZE_INGOT, TitanBox.instants.getItem("A"), SlimefunItems.BRONZE_INGOT,
                        SlimefunItems.BRONZE_INGOT, SlimefunItems.BATTERY, SlimefunItems.BRONZE_INGOT,
                        SlimefunItems.BRONZE_INGOT, SlimefunItems.BRONZE_INGOT, SlimefunItems.BRONZE_INGOT}).register();
        new TitanItemManager(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("C"), "STORAGE_UNIT_SIZE_" + ItemEnum.UNIT_C.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, TitanBox.instants.getItem("B"), SlimefunItemsManager.LuckyIngot,
                        SlimefunItemsManager.LuckyIngot, SlimefunItems.BATTERY, SlimefunItemsManager.LuckyIngot,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();
        new TitanItemManager(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("D"), "STORAGE_UNIT_SIZE_" + ItemEnum.UNIT_D.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, TitanBox.instants.getItem("C"), SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.EclipseIngot, SlimefunItems.BATTERY, SlimefunItemsManager.EclipseIngot,
                        SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot}).register();
        new TitanItemManager(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("E"), "STORAGE_UNIT_SIZE_" + ItemEnum.UNIT_E.getSize(), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.TitanIngot, TitanBox.instants.getItem("D"), SlimefunItemsManager.TitanIngot,
                        SlimefunItemsManager.TitanIngot, SlimefunItems.BATTERY, SlimefunItemsManager.TitanIngot,
                        SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot}).register();

        ItemStack rewards = SlimefunItemsManager.DIAMOND_WRITING_PLATE.clone();
        rewards.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.DIAMOND_WRITING_PLATE, "DIAMONDWRITINGPLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND),
                    new ItemStack(Material.DIAMOND), new ItemStack(Material.PAPER), new ItemStack(Material.DIAMOND),
                    new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND)}, rewards).register();

        rewards = SlimefunItemsManager.EMERALD_WRITING_PLATE.clone();
        rewards.setAmount(9);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.EMERALD_WRITING_PLATE, "EMERALDWRITINGPLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD),
                        new ItemStack(Material.EMERALD), new ItemStack(Material.PAPER), new ItemStack(Material.EMERALD),
                        new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD)}, rewards).register();

        rewards = SlimefunItemsManager.ENDER_WRITING_PLATE.clone();
        rewards.setAmount(6);
        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, SlimefunItemsManager.ENDER_WRITING_PLATE, "ENDERWRITINGPLATE", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL),
                        new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.PAPER), new ItemStack(Material.ENDER_PEARL),
                        new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.ENDER_PEARL)}, rewards).register();

        for(ModuleTypeEnum mte: ModuleTypeEnum.values())
        {
            new TitanItemManager(CustomCategories.STORAGE_MODULES, TitanBox.instants.getItem("module", mte.getType()), ChatColor.stripColor(mte.getTitle()), RecipeType.ENHANCED_CRAFTING_TABLE,
                    new ItemStack[] {SlimefunItemsManager.DIAMOND_WRITING_PLATE, SlimefunItemsManager.DIAMOND_WRITING_PLATE, SlimefunItemsManager.DIAMOND_WRITING_PLATE,
                            SlimefunItemsManager.ENDER_WRITING_PLATE, mte.getCrafter(), SlimefunItemsManager.ENDER_WRITING_PLATE,
                            SlimefunItemsManager.EMERALD_WRITING_PLATE, SlimefunItemsManager.EMERALD_WRITING_PLATE, SlimefunItemsManager.EMERALD_WRITING_PLATE}).register();
        }

        new TitanItemManager(CustomCategories.STORAGE_ROUTING, TitanBox.instants.getItem("router"), "ITEM_ROUTING_ROUTER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT,
                        new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.BOOKSHELF), new ItemStack(Material.CRAFTING_TABLE),
                        SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT}).register();

        new TitanItemManager(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("killer"), "KILLER_BLOCK", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        SlimefunItems.STEEL_INGOT, new ItemStack(Material.DIAMOND_SWORD), SlimefunItems.STEEL_INGOT,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();

        new TitanItemManager(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("water"), "WATER_PUMP", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        new ItemStack(Material.WATER_BUCKET), SlimefunItems.FLUID_PUMP, new ItemStack(Material.WATER_BUCKET),
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();

        new TitanItemManager(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("lava"), "LAVA_PUMP", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        new ItemStack(Material.LAVA_BUCKET), SlimefunItems.FLUID_PUMP, new ItemStack(Material.LAVA_BUCKET),
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();

        new TitanItemManager(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("ice"), "ICE_EXTRACTOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        SlimefunItems.STEEL_INGOT, SlimefunItems.FREEZER, SlimefunItems.STEEL_INGOT,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();

        new TitanItemManager(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("item"), "ITEM_SUCKER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        SlimefunItems.STEEL_INGOT, new ItemStack(Material.HOPPER), SlimefunItems.STEEL_INGOT,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();

        new TitanItemManager(CustomCategories.STORAGE_ROUTING, Pumps.getMeAsDrop("placer"), "TB_BLOCK_PLACER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        SlimefunItems.STEEL_INGOT, new ItemStack(Material.DISPENSER), SlimefunItems.STEEL_INGOT,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();


        new AutoAnvil(CustomCategories.ELECTRICITY, SlimefunItemsManager.TITAN_AUTO_ANVIL, "TITAN_AUTO_ANVIL", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        SlimefunItems.STEEL_INGOT, SlimefunItems.AUTO_ANVIL_2, SlimefunItems.STEEL_INGOT,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getRepairFactor() {
                return 1;
            }

            @Override
            public int getEnergyConsumption() {
                return 160;
            }

        }.registerChargeableBlock(true, 256);

        new DeathRecovery(CustomCategories.SLIMEFUN_RECOVERY, SlimefunItemsManager.TITAN_DEATH_RECOVERY, "TITAN_DEATH_RECOVERY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.LuckyIngot,
                        SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItemsManager.TitanStone, SlimefunItems.ESSENCE_OF_AFTERLIFE,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.LuckyIngot}) {

            @Override
            public int getSpeedFactor() {
                return 100;
            }

            @Override
            public int getEnergyConsumption() {
                return 660;
            }

        }.registerChargeableBlock(true, 3000);

        new DeathRecovery(CustomCategories.SLIMEFUN_RECOVERY, SlimefunItemsManager.TITAN_DEATH_RECOVERY_2, "TITAN_DEATH_RECOVERY_II", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.TitanIngot,
                        SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItemsManager.TITAN_DEATH_RECOVERY, SlimefunItems.ESSENCE_OF_AFTERLIFE,
                        SlimefunItemsManager.TitanIngot, SlimefunItemsManager.JAR_OF_LIFE_FORCE, SlimefunItemsManager.TitanIngot}) {

            @Override
            public int getSpeedFactor() {
                return 10;
            }

            @Override
            public int getEnergyConsumption() {
                return 660;
            }

        }.registerChargeableBlock(true, 3000);

        new ItemRecovery(CustomCategories.SLIMEFUN_RECOVERY, SlimefunItemsManager.TITAN_ITEM_RECOVERY, "TITAN_ITEM_RECOVERY", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot,
                        SlimefunItems.DUCT_TAPE, SlimefunItemsManager.TITAN_AUTO_ANVIL, SlimefunItems.DUCT_TAPE,
                        SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot}) {

            @Override
            public int getSpeedFactor() {
                return 100;
            }

            @Override
            public int getEnergyConsumption() {
                return 660;
            }

        }.registerChargeableBlock(true, 3000);

        new ItemRecovery(CustomCategories.SLIMEFUN_RECOVERY, SlimefunItemsManager.TITAN_ITEM_RECOVERY_2, "TITAN_ITEM_RECOVERY_II", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot,
                        SlimefunItems.DUCT_TAPE, SlimefunItemsManager.TITAN_ITEM_RECOVERY, SlimefunItems.DUCT_TAPE,
                        SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot}) {

            @Override
            public int getSpeedFactor() {
                return 10;
            }

            @Override
            public int getEnergyConsumption() {
                return 660;
            }

            @Override
            public String getMachineIdentifier() {
                return "TITAN_ITEM_RECOVERY_II";
            }

            @Override
            public String getInventoryTitle() {
                return "Titan Item Recovery II";
            }

        }.registerChargeableBlock(true, 3000);

        new TitanItemManager(CustomCategories.SLIMEFUN_PARTS, Elevator.getMeAsDrop(), "ELEVATOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {new ItemStack(Material.IRON_INGOT), new ItemStack(Material.PISTON), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.PISTON), new ItemStack(Material.IRON_INGOT)}).register();

        new TitanItemManager(CustomCategories.SLIMEFUN_RECOVERY, BackpackRecover.getMeAsDrop(), "BACKPACK_RECOVER", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                        SlimefunItemsManager.JAR_OF_LIFE_FORCE, new ItemStack(Material.CRAFTING_TABLE), SlimefunItemsManager.JAR_OF_LIFE_FORCE,
                        SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();

        new TitanItemManager(CustomCategories.SLIMEFUN_RECOVERY, StorageRecover.getMeAsDrop(), "STORAGE_RECOVER", RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,
                    SlimefunItemsManager.JAR_OF_LIFE_FORCE, new ItemStack(Material.DISPENSER), SlimefunItemsManager.JAR_OF_LIFE_FORCE,
                    SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();

        new TitanItemManager(CustomCategories.ELECTRICITY, NetworkMonitor.getMeAsDrop(), "NETWORKMONITOR", RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT,
                        new ItemStack(Material.GLASS), new ItemStack(Material.REDSTONE_LAMP), new ItemStack(Material.GLASS),
                        SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_INGOT}).register();


        Slimefun.registerResearch(new Research(7578, "Advanced Storage", 50), new ItemStack[] { TitanBox.instants.getItem("C"), TitanBox.instants.getItem("D"), TitanBox.instants.getItem("E") });
        Slimefun.registerResearch(new Research(7579, "Advanced Coding", 25), new ItemStack[] { SlimefunItemsManager.DIAMOND_WRITING_PLATE, SlimefunItemsManager.EMERALD_WRITING_PLATE, SlimefunItemsManager.ENDER_WRITING_PLATE });

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
        ItemStack Reward =  new ItemStack(SlimefunItemsManager.TitanNugget);
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGET ", RecipeType.ORE_CRUSHER, new ItemStack[] {null,null, SlimefunItemsManager.TitanAxe, SlimefunItemsManager.TitanBoots, SlimefunItemsManager.TitanChestplate, SlimefunItemsManager.TitanHelmet, SlimefunItemsManager.TitanLeggings, SlimefunItemsManager.TitanSword, SlimefunItemsManager.TitanPickaxe}).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGET  ", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.TitanAxe, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGETA", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.TitanBoots, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGETB", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.TitanChestplate, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGETC", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.TitanHelmet, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGETD", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.TitanLeggings, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGETE", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.TitanSword, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.TitanNugget.clone(), "TITANNUGGETF", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.TitanPickaxe, null, null, null, null, null, null, null, null}, Reward).register();

        Reward = SlimefunItemsManager.TitanNugget.clone();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.TitanIngot, "TITANIGNOT", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.TitanAxe, "TITANAXE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, null, SlimefunItemsManager.TitanIngot, SlimefunItems.MAGNESIUM_INGOT, null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.TitanSword, "TITANSWORD", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsManager.TitanIngot, null, null, SlimefunItemsManager.TitanIngot,null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.TitanPickaxe, "TITANPICKAXE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, null, SlimefunItems.MAGNESIUM_INGOT, null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.TitanHelmet, "TITANHELMET", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, null, SlimefunItemsManager.TitanIngot, null, null, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.TitanChestplate, "TITANCHESTPLATE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.TitanIngot, null, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.TitanLeggings, "TITANLEGGINGS", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot,null, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, null, SlimefunItemsManager.TitanIngot}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.TitanBoots, "TITANBOOTS", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, null, null, SlimefunItemsManager.TitanIngot, null, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, null, SlimefunItemsManager.TitanIngot}).register();

        //new TitanItemManager(CustomCategories.SLIMEFUN_MORE, SlimefunItemsManager.X_RAY_HELMET, "XRayHelmet", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.TitanIngot, SlimefunItemsManager.CARGO_MOTOR, SlimefunItemsManager.TitanIngot, SlimefunItemsManager.TitanIngot, null, SlimefunItemsManager.TitanIngot, null, null, null}).register();
    }
    private void setupEclipseSet() {
        ItemStack Reward =  new ItemStack(SlimefunItemsManager.EclipseNugget);
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.EclipseNugget.clone(), "EclipseNugget ", RecipeType.ORE_CRUSHER, new ItemStack[] {null,null, SlimefunItemsManager.EclipseAxe, SlimefunItemsManager.EclipseBoots, SlimefunItemsManager.EclipseChestplate, SlimefunItemsManager.EclipseHelmet, SlimefunItemsManager.EclipseLeggings, SlimefunItemsManager.EclipseSword, SlimefunItemsManager.EclipsePickaxe}).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.EclipseNugget.clone(), "ECLIPSENUGGET  ", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.EclipseAxe, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.EclipseNugget.clone(), "ECLIPSENUGGETA", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.EclipseBoots, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.EclipseNugget.clone(), "ECLIPSENUGGETB", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.EclipseChestplate, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.EclipseNugget.clone(), "ECLIPSENUGGETC", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.EclipseHelmet, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.EclipseNugget.clone(), "ECLIPSENUGGETD", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.EclipseLeggings, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.EclipseNugget.clone(), "ECLIPSENUGGETE", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.EclipseSword, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.EclipseNugget.clone(), "ECLIPSENUGGETF", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.EclipsePickaxe, null, null, null, null, null, null, null, null}, Reward).register();

        Reward = SlimefunItemsManager.EclipseNugget.clone();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.EclipseIngot, "ECLIPSEIGNOT", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.EclipseAxe, "ECLIPSEAXE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot, SlimefunItems.MAGNESIUM_INGOT, null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.EclipseSword, "ECLIPSESWORD", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsManager.EclipseIngot, null, null, SlimefunItemsManager.EclipseIngot,null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.EclipsePickaxe, "ECLIPSEPICKAXE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, null, SlimefunItems.MAGNESIUM_INGOT, null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.EclipseHelmet, "ECLIPSEHELMET", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot, null, null, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.EclipseChestplate, "ECLIPSECHESTPLATE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.EclipseLeggings, "ECLIPSELEGGINGS", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot,null, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.EclipseBoots, "ECLIPSEBOOTS", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, null, null, SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot, SlimefunItemsManager.EclipseIngot, null, SlimefunItemsManager.EclipseIngot}).register();

    }
    private void setupLuckySet() {
        ItemStack Reward = SlimefunItemsManager.LuckyBlock.clone();

        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGET", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        Reward =  new ItemStack(SlimefunItemsManager.LuckyNugget);
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGET ", RecipeType.ORE_CRUSHER, new ItemStack[] {null,null, SlimefunItemsManager.LuckyAxe, SlimefunItemsManager.LuckyBoots, SlimefunItemsManager.LuckyChestplate, SlimefunItemsManager.LuckyHelmet, SlimefunItemsManager.LuckyLeggings, SlimefunItemsManager.LuckySword, SlimefunItemsManager.LuckyPickaxe}).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGETA", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.LuckyAxe, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGETB", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.LuckyBoots, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(5);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGETC", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.LuckyChestplate, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGETD", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.LuckyHelmet, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(4);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGETE", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.LuckyLeggings, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGETF", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.LuckySword, null, null, null, null, null, null, null, null}, Reward).register();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_BLANK, SlimefunItemsManager.LuckyNugget.clone(), "LUCKYNUGGETG", RecipeType.ORE_CRUSHER, new ItemStack[] {SlimefunItemsManager.LuckyPickaxe, null, null, null, null, null, null, null, null}, Reward).register();

        Reward = SlimefunItemsManager.LuckyNugget.clone();
        Reward.setAmount(3);
        new TitanItemManager(CustomCategories.SLIMEFUN_RESOURCES, SlimefunItemsManager.LuckyIngot, "LUCKYIGNOT", RecipeType.COMPRESSOR,  new ItemStack[] {Reward, null, null, null, null, null, null, null, null}).register();

        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.LuckyAxe, "LUCKYAXE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot, SlimefunItems.MAGNESIUM_INGOT, null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.LuckySword, "LUCKYSWORD", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, SlimefunItemsManager.LuckyIngot, null, null, SlimefunItemsManager.LuckyIngot,null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_TOOLS, SlimefunItemsManager.LuckyPickaxe, "LUCKYPICKAXE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, null, SlimefunItems.MAGNESIUM_INGOT, null, null, SlimefunItems.MAGNESIUM_INGOT, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.LuckyHelmet, "LUCKYHELMET", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot, null, null, null}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.LuckyChestplate, "LUCKYCHESTPLATE", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.LuckyLeggings, "LUCKYLEGGINGS", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot,null, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot}).register();
        new TitanItemManager(CustomCategories.SLIMEFUN_TITAN_GEAR, SlimefunItemsManager.LuckyBoots, "LUCKYBOOTS", RecipeType.MAGIC_WORKBENCH,  new ItemStack[] {null, null, null, SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot, SlimefunItemsManager.LuckyIngot, null, SlimefunItemsManager.LuckyIngot}).register();
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

package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.machines.FreeFactory;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 12/28/2016.
 */
public class SlimefunItemsManager {
    private static List<FreeFactory> FreeFactories = new ArrayList<FreeFactory>();
    private static List<ItemStack> FreeEnergies = new ArrayList<ItemStack>();
    private static List<ItemStack> TitanSuff = new ArrayList<ItemStack>();
    private static List<ItemStack> ForceFieldRequired = new ArrayList<ItemStack>();


    public static void addTitanItem(ItemStack item)
    {
        TitanSuff.add(item.clone());
    }
    public static void addFreeEnergies(ItemStack item)
    {
        FreeEnergies.add(item.clone());
    }
    public static void addFreeFactories(FreeFactory item)
    {
        FreeFactories.add(item);
    }
    public static void addForceFieldRequired(ItemStack item)
    {
        ForceFieldRequired.add(item.clone());
    }
    public static boolean isForceFieldRequired(ItemStack itemStack)
    {
        for (ItemStack item: ForceFieldRequired)
        {
            if (Utilities.isItemEqual(item, itemStack))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isTitanItem(ItemStack itemStack)
    {
        for (ItemStack item: TitanSuff)
        {
            if (Utilities.isItemEqual(item, itemStack))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isFreeEnergy(ItemStack itemStack)
    {
        for (ItemStack item: FreeEnergies)
        {
            if (Utilities.isItemEqual(item, itemStack))
            {
                return true;
            }
        }
        return false;
    }
    public static FreeFactory getFreeFactory(ItemStack itemStack)
    {
        for (FreeFactory item: FreeFactories)
        {
            if (Utilities.isItemEqual(item.getMe(), itemStack))
            {
                return item;
            }
        }
        return null;
    }
    public static boolean isFreeFactory(ItemStack itemStack)
    {
        for (FreeFactory item: FreeFactories)
        {
            if (Utilities.isItemEqual(item.getMe(), itemStack))
            {
                return true;
            }
        }
        return false;
    }
    public static ItemStack TitanStone;



    public static ItemStack JAR_OF_LIFE_FORCE;
    public static ItemStack JAR_OF_SOULS;
    public static ItemStack EMPTY_JAR;
    public static ItemStack LIFE_FORCE_EXTRACTOR;
    public static ItemStack SPAWNER_EGG_EXTRACTOR;
    public static ItemStack SPAWNER_REINTEGRATER;
    public static ItemStack SOUL_EXTRACTOR;

    public static ItemStack LuckyNuggetB = new CustomItem(Material.GOLD_NUGGET, "&eLucky Nugget", new String[] { "DAMAGE_ALL-1"}, 0);
    public static ItemStack EclipseNuggetB = new CustomItem(Material.GHAST_TEAR, "&bEclipse Nugget", new String[] { "DAMAGE_ALL-3"}, 0);
    public static ItemStack TitanNuggetB = new CustomItem(Material.REDSTONE, "&4Titan Nugget", new String[] { "DAMAGE_ALL-5"}, 0);

    public static ItemStack LuckyNugget = new CustomItem(Material.GOLD_NUGGET, "&e&lLucky Nugget", new String[] { "DAMAGE_ALL-1"}, 0);
    public static ItemStack EclipseNugget = new CustomItem(Material.GHAST_TEAR, "&b&lEclipse Nugget", new String[] { "DAMAGE_ALL-3"}, 0);
    public static ItemStack TitanNugget = new CustomItem(Material.REDSTONE, "&4&lTitan Nugget", new String[] { "DAMAGE_ALL-5"}, 0);

    public static ItemStack LuckyIngot = new CustomItem(Material.GOLD_INGOT, "&e&lLucky Ingot", new String[] { "DAMAGE_ALL-2"}, 0);
    public static ItemStack EclipseIngot = new CustomItem(Material.IRON_INGOT, "&b&lEclipse Ingot", new String[] { "DAMAGE_ALL-6"}, 0);
    public static ItemStack TitanIngot = new CustomItem(Material.BRICK, "&4&lTitan Ingot", new String[] { "DAMAGE_ALL-10"}, 0);

    public static ItemStack LuckySword = new CustomItem(Material.GOLDEN_SWORD, "&e&lLucky Sword", new String[] { "DAMAGE_ALL-10", "LOOT_BONUS_MOBS-10", "FIRE_ASPECT-5", "DURABILITY-10" }, 0);
    public static ItemStack LuckyPickaxe = new CustomItem(Material.GOLDEN_PICKAXE, "&e&lLucky Pickaxe", new String[] { "DIG_SPEED-10", "LOOT_BONUS_BLOCKS-10", "DURABILITY-10" }, 0);
    public static ItemStack LuckyAxe = new CustomItem(Material.GOLDEN_AXE, "&e&lLucky Axe", new String[] { "DAMAGE_ALL-10", "DIG_SPEED-10", "LOOT_BONUS_BLOCKS-10", "DURABILITY-10" }, 0);
    public static ItemStack LuckyHelmet = new CustomItem(Material.DIAMOND_HELMET, "&e&lLucky Helmet", new String[] { "PROTECTION_ENVIRONMENTAL-10", "PROTECTION_PROJECTILE-10", "PROTECTION_EXPLOSIONS-10", "THORNS-10", "DURABILITY-10" }, 0);
    public static ItemStack LuckyChestplate = new CustomItem(Material.DIAMOND_CHESTPLATE, "&e&lLucky Chestplate", new String[] { "PROTECTION_ENVIRONMENTAL-10", "PROTECTION_PROJECTILE-10", "PROTECTION_EXPLOSIONS-10", "THORNS-10", "DURABILITY-10" }, 0);
    public static ItemStack LuckyLeggings = new CustomItem(Material.DIAMOND_LEGGINGS, "&e&lLucky Leggings", new String[] { "PROTECTION_ENVIRONMENTAL-10", "PROTECTION_PROJECTILE-10", "PROTECTION_EXPLOSIONS-10", "THORNS-10", "DURABILITY-10" }, 0);
    public static ItemStack LuckyBoots = new CustomItem(Material.DIAMOND_BOOTS, "&e&lLucky Boots", new String[] { "PROTECTION_ENVIRONMENTAL-10", "PROTECTION_PROJECTILE-10", "PROTECTION_EXPLOSIONS-10", "THORNS-10", "DURABILITY-10" }, 0);
    public static ItemStack LuckyBlock;
    public static ItemStack ZeroLuckyBlock;
    public static ItemStack UnLuckyBlock;
    public static ItemStack PandorasBox;


    public static ItemStack EclipseSword = new CustomItem(Material.IRON_SWORD, "&b&lEclipse Sword", new String[] { "DAMAGE_ALL-13", "LOOT_BONUS_MOBS-13", "FIRE_ASPECT-7", "DURABILITY-13" }, 0);
    public static ItemStack EclipsePickaxe = new CustomItem(Material.IRON_PICKAXE, "&b&lEclipse Pickaxe", new String[] { "DIG_SPEED-13", "LOOT_BONUS_BLOCKS-13", "DURABILITY-13" }, 0);
    public static ItemStack EclipseAxe = new CustomItem(Material.IRON_AXE, "&b&lEclipse Axe", new String[] { "DAMAGE_ALL-13", "DIG_SPEED-13", "LOOT_BONUS_BLOCKS-13", "DURABILITY-13" }, 0);
    public static ItemStack EclipseHelmet = new CustomItem(Material.DIAMOND_HELMET, "&b&lEclipse Helmet", new String[] { "PROTECTION_ENVIRONMENTAL-13", "PROTECTION_PROJECTILE-13", "PROTECTION_EXPLOSIONS-13", "THORNS-13", "DURABILITY-13" }, 0);
    public static ItemStack EclipseChestplate = new CustomItem(Material.DIAMOND_CHESTPLATE, "&b&lEclipse Chestplate", new String[] { "PROTECTION_ENVIRONMENTAL-13", "PROTECTION_PROJECTILE-13", "PROTECTION_EXPLOSIONS-13", "THORNS-13", "DURABILITY-13" }, 0);
    public static ItemStack EclipseLeggings = new CustomItem(Material.DIAMOND_LEGGINGS, "&b&lEclipse Leggings", new String[] { "PROTECTION_ENVIRONMENTAL-13", "PROTECTION_PROJECTILE-13", "PROTECTION_EXPLOSIONS-13", "THORNS-13", "DURABILITY-13" }, 0);
    public static ItemStack EclipseBoots = new CustomItem(Material.DIAMOND_BOOTS, "&b&lEclipse Boots", new String[] { "PROTECTION_ENVIRONMENTAL-13", "PROTECTION_PROJECTILE-13", "PROTECTION_EXPLOSIONS-13", "THORNS-13", "DURABILITY-13" }, 0);

    public static ItemStack TitanSword = new CustomItem(Material.DIAMOND_SWORD, "&4&lTitan Sword", new String[] { "DAMAGE_ALL-23", "LOOT_BONUS_MOBS-23", "FIRE_ASPECT-23", "DURABILITY-23" }, 0);
    public static ItemStack TitanPickaxe = new CustomItem(Material.DIAMOND_PICKAXE, "&4&lTitan Pickaxe", new String[] { "DIG_SPEED-23", "LOOT_BONUS_BLOCKS-23", "DURABILITY-23" }, 0);
    public static ItemStack TitanAxe = new CustomItem(Material.DIAMOND_AXE, "&4&lTitan Axe", new String[] { "DAMAGE_ALL-23", "DIG_SPEED-23", "LOOT_BONUS_BLOCKS-23", "DURABILITY-23" }, 0);
    public static ItemStack TitanHelmet = new CustomItem(Material.DIAMOND_HELMET, "&4&lTitan Helmet", new String[] { "PROTECTION_ENVIRONMENTAL-23", "PROTECTION_PROJECTILE-23", "PROTECTION_EXPLOSIONS-23", "THORNS-23", "DURABILITY-23" }, 0);
    public static ItemStack TitanChestplate = new CustomItem(Material.DIAMOND_CHESTPLATE, "&4&lTitan Chestplate", new String[] { "PROTECTION_ENVIRONMENTAL-23", "PROTECTION_PROJECTILE-23", "PROTECTION_EXPLOSIONS-23", "THORNS-23", "DURABILITY-23" }, 0);
    public static ItemStack TitanLeggings = new CustomItem(Material.DIAMOND_LEGGINGS, "&4&lTitan Leggings", new String[] { "PROTECTION_ENVIRONMENTAL-23", "PROTECTION_PROJECTILE-23", "PROTECTION_EXPLOSIONS-23", "THORNS-23", "DURABILITY-23" }, 0);
    public static ItemStack TitanBoots = new CustomItem(Material.DIAMOND_BOOTS, "&4&lTitan Boots", new String[] { "PROTECTION_ENVIRONMENTAL-23", "PROTECTION_PROJECTILE-23", "PROTECTION_EXPLOSIONS-23", "THORNS-23", "DURABILITY-23" }, 0);

    public static ItemStack ANCIENT_ALTAR_CRAFTER_BLOCK = Utilities.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWZkMDE2NzY4OTcxNWRmMWFhNTA1NWE2M2VhNmI4YmE2NTZlMmI0YjgxZmNjYWI1M2MzZTIxMDhkODBiODFjIn19fQ==");
    public static ItemStack AUTOMATED_VANILLA_CRAFTING_CHAMBER = new CustomItem(new MaterialData(Material.CRAFTING_TABLE), "&eAutomated Vanilla Crafting Chamber", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &710 J/Item");
    public static ItemStack ANCIENT_ALTAR_CRAFTER = new CustomItem(ANCIENT_ALTAR_CRAFTER_BLOCK, "&6Ancient Altar Crafter", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &7500 J/Item");
    public static ItemStack AUTOMATED_ANCIENT_ALTAR_CRAFTER = new CustomItem(new MaterialData(Material.CRAFTING_TABLE), "&6Automated Ancient Altar Crafter", "", "&6Advanced Machine", "&8\u21E8 &e\u26A1 &750 J/Item");
    public static ItemStack THERMAL_GENERATOR;
    public static ItemStack FREE_ENERGY_I;
    public static ItemStack FREE_ENERGY_II;
    public static ItemStack FREE_ENERGY_III;
    public static ItemStack BONE_FACTORY;
    public static ItemStack XP_FACTORY;
    public static ItemStack IRON_FACTORY;
    public static ItemStack SLIME_FACTORY;
    public static ItemStack BLAZE_FACTORY;
    public static ItemStack ENDERMAN_FACTORY;
    public static ItemStack NETHERWART_FACTORY;
    public static ItemStack NETHERRACK_FACTORY;
    public static ItemStack SOULSAND_FACTORY;
    public static ItemStack WITHERSKULL_FACTORY;
    public static ItemStack QUARTZ_FACTORY;
    public static ItemStack ELECTRIC_COBBLE_TO_DUST;
    public static ItemStack ELECTRIC_COBBLE_TO_INGOT;
    public static ItemStack ELECTRIC_COBBLE_TO_DUST_2;
    public static ItemStack ELECTRIC_COBBLE_TO_INGOT_2;
    public static ItemStack ELECTRIC_COBBLE_TO_DUST_3;
    public static ItemStack ELECTRIC_COBBLE_TO_INGOT_3;
    public static ItemStack ELECTRIC_LUCKY_BLOCK_FACTORY;
    public static ItemStack ELECTRIC_LUCKY_BLOCK_GRINDER;
    public static ItemStack CHARCOAL_FACTORY;
    public static ItemStack CHARCOAL_FACTORY_2;
    public static ItemStack CHARCOAL_FACTORY_3;
    public static ItemStack ELECTRIC_RENAMER;
    public static ItemStack LOG_FACTORY;
    public static ItemStack LOG_FACTORY_2;
    public static ItemStack LOG_FACTORY_3;
    public static ItemStack STRIPPED_LOG_FACTORY;
    public static ItemStack STRIPPED_LOG_FACTORY_2;
    public static ItemStack STRIPPED_LOG_FACTORY_3;
    public static ItemStack BLOCK_ASSEMBLER_DISASSEMBLER;
    public static ItemStack TNT_FACTORY;
    public static ItemStack LUCKY_BLOCK_OPENER;
    public static ItemStack SAPLING_FACTORY;
    public static ItemStack SAPLING_FACTORY_2;
    public static ItemStack SAPLING_FACTORY_3;
    public static ItemStack FISHER_MACHINE;
    public static ItemStack TITAN_AUTO_ANVIL;
    public static ItemStack TITAN_ITEM_RECOVERY;
    public static ItemStack TITAN_ITEM_RECOVERY_2;

    public static ItemStack TITAN_DEATH_RECOVERY;
    public static ItemStack TITAN_DEATH_RECOVERY_2;


    public static ItemStack HEAD_RECOVERY;
    public static ItemStack FORCE_FIELD_YELLOW;
    public static ItemStack FORCE_FIELD_GREEN;
    public static ItemStack FORCE_FIELD_BLUE;
    public static String FORCE_FIELD_YELLOW_S = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRkYTAzMWY0NGZlM2VhOGFhNzJmODdiN2M4ZGIyNjlmMmJiYTA3YjRkOGRjMWVhOTRhYjdjMGYxMzBlOTA1ZSJ9fX0=";
    public static String FORCE_FIELD_GREEN_S = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhlZDNmYjVjMDMwMDE3NjRkOGIxMmM1MWQxMThmNjJlYmZmOWVjNGQxNWFhMzc2N2I5YzgwYTVkNTMzNTJhYiJ9fX0=";
    public static String FORCE_FIELD_BLUE_S = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDc1MmJkNjcyMWQyMjk1NWQ0ZGNkZjIwM2Y2ZjZiMzdhMTBhMzI4ZjI0ZWRhMjJiNmFhZWJiOWIzN2NkMTFhMiJ9fX0=";
    public static String STORAGE_MOVER_S = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UxNTcyYTEzNjg2Yjc5NDdkZmFkMDhkMGI2ZGUxM2I2ZDUzM2JjZDk5YzMzMDk0NGIxNDBkNDdhNmUyOWJlYSJ9fX0=";
    public static ItemStack SALTPETER = new CustomItem(Material.SUGAR, "&6Saltpeter", 0);
    public static ItemStack COMMAND_CENTER;
    public static ItemStack DATA_CENTER;
    public static ItemStack STORAGE_MOVER;
    public static ItemStack FLY_ORB = new CustomItem(new ItemStack(Material.SLIME_BALL, 1), "&5Fly Orb",  new String[] {"&4End Game", "&8\u21E8 &7Place in Hotbar to allow flying" });
    public static ItemStack LOOT_KEY = new CustomItem(new ItemStack(Material.TRIPWIRE_HOOK, 1), "&6Loot Bank Key");
    public static ItemStack WILD_HOE = new CustomItem(new ItemStack(Material.WOODEN_HOE, 1), "&5Wild Teleporter",  new String[] {"&4End Game", "&8\u21E8 &7Click To Teleport To The Wild, Will Break", "&8\u21E8 &710K Blocks From Spawn." });
    public static ItemStack WILD_HOE_II = new CustomItem(new ItemStack(Material.IRON_HOE, 1), "&5Wild Teleporter II",  new String[] {"&4End Game", "&8\u21E8 &7Click To Teleport To The Wild, Will Break", "&8\u21E8 &725K Blocks From Spawn."});
    public static ItemStack WILD_HOE_III = new CustomItem(new ItemStack(Material.DIAMOND_HOE, 1), "&5Wild Teleporter III",  new String[] {"&4End Game", "&8\u21E8 &7Click To Teleport To The Wild, Will Break", "&8\u21E8 &750K Blocks From Spawn."});
    public static String MAILBOX = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWIyODE1Yjk5YzEzYmZjNTViNGM1YzI5NTlkMTU3YTYyMzNhYjA2MTg2NDU5MjMzYmMxZTRkNGY3ODc5MmM2OSJ9fX0=";
    public static String AUCTIONHOUSE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc0MTBjMDdiZmJiNDE0NTAwNGJmOTE4YzhkNjMwMWJkOTdjZTEzMjcwY2UxZjIyMWQ5YWFiZWUxYWZkNTJhMyJ9fX0=";
    public static String DATACENTER = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYxZGZkZGE3MTAyMmJmYWQ3OWVhYmJmYTU2YTE1MTE2NDdiMjAyYWQxMTM2ZmY5N2UzMWEwNmRiYWMxMGVjYiJ9fX0=";
    public static String COMMANDCENTER = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc3MTJjYTY1NTEyODcwMWVhM2U1ZjI4ZGRkNjllNmE4ZTYzYWRmMjgwNTJjNTFiMmZkNWFkYjUzOGUxIn19fQ==";
    public static String TURRET = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzVmZDc1MTZkZGJjODFhOWM4MWMxZDllMWMyYzk4ODBkMjNhZTE2M2IzZmIyMTZlZTBjYzQzOTE3YTg4MjgifX19";
    public static String INFO = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGExNjAzOGJjOGU2NTE4YWZhOTE0OThkYWI3Njc1YzAxY2IzMWExMjVkMjFjNDliODYxMjk0ZDM5ZTFjNTYwYyJ9fX0=";
    public static ItemStack INFO_BLOCK;
    //
    public static ItemStack ECLIPSE_COIL;
    public static ItemStack TITAN_MOTOR;
    public static ItemStack NUGGETTOINGOT;
    public static ItemStack INGOTUP;

    public static ItemStack REPAIRED_SPAWNER_BLANK = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bEmpty"});
    public static ItemStack REPAIRED_SPAWNER_SKELETON = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bSkeleton"});
    public static ItemStack REPAIRED_SPAWNER_BLAZE = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bBlaze"});
    public static ItemStack REPAIRED_SPAWNER_VILLAGER = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bVillager"});
    public static ItemStack REPAIRED_SPAWNER_PIG = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bPig"});
    public static ItemStack REPAIRED_SPAWNER_SLIME = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bSlime"});
    public static ItemStack REPAIRED_SPAWNER_ENDERMAN = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bEnderman"});
    public static ItemStack REPAIRED_SPAWNER_WITHER = new CustomItem(Material.SPAWNER, "&bReinforced Spawner", 0, new String[]{"&7Type: &bWither Skeleton"});

    public static ItemStack FORCE_FIELD_RESONATOR;

    public static ItemStack AUTO_ENCHANTER_2 = new CustomItem(new ItemStack(Material.ENCHANTING_TABLE, 1), "&5Auto Enchanter II",  new String[] {"", "&4End Game Machine", "&8\u21E8 &e\u26A1 &7500 J/s"});
    public static ItemStack AUTO_DISENCHANTER_2 = new CustomItem(new ItemStack(Material.ENCHANTING_TABLE, 1), "&5Auto Disenchanter II",  new String[] {"", "&4End Game Machine", "&8\u21E8 &e\u26A1 &7500 J/s"});

    public static ItemStack XPPLATE = new CustomItem(new ItemStack(Material.CYAN_CARPET, 1), "&bXP Plate", new String[] {"&f-Stand on plate to get XP", "&8\u21E8 &e\u26A1 &7300 J/s"});
    public static ItemStack DIAMOND_WRITING_PLATE  = new CustomItem(new ItemStack(Material.LIGHT_BLUE_CARPET, 1), "&bDiamond Writing Plate", new String[] {"&f-A Strong plate for writing complex codes"});
    public static ItemStack EMERALD_WRITING_PLATE  = new CustomItem(new ItemStack(Material.LIME_CARPET, 1), "&eEmerald Writing Plate", new String[] {"&f-A Strong plate for writing complex codes"});
    public static ItemStack ENDER_WRITING_PLATE  = new CustomItem(new ItemStack(Material.CYAN_CARPET, 1), "&3Ender Writing Plate", new String[] {"&f-A plate for writing teleporting codes"});

    public static ItemStack TALISMAN_VOID = new CustomItem(Material.EMERALD, "&4Titan Talisman of the Void", 0, new String[] {"", "&rOne Talisman can send", "&ritems to your storage untis", "&rfrom your inventory"});

    public static ItemStack X_RAY_HELMET = new CustomItem(Material.LEATHER_HELMET, "&b&lX-Ray Helmet", 0);
    public static ItemStack CAVE_VIEW_HELMET = new CustomItem(Material.LEATHER_HELMET, "&b&lCave View Helmet", 0);


    static {
        try {
            INFO_BLOCK =new CustomItem(CustomSkull.getItem(INFO), "Info Block");
            JAR_OF_LIFE_FORCE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ3MjkyNzNhYjdlZjM0MDllMjQzZThkYTc0YzFiZjM4NjNkMzE1OTIxYmJkYWMzNGM0NmRkMTI2NzVkMzAyMSJ9fX0="), "&4\u2620 Jar Of Life Force");
            JAR_OF_SOULS = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFmODVhOTJiZWRkOWE4NTcxY2Y1YTA2NzU4YTc1YjllYjU1NmE2MzkxNTk2MTlmZGQ1OTJkYzRhMzM0YWE1In19fQ=="), "&4\u2620 Jar Of Souls");
            EMPTY_JAR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y1NmEyNmE5NDljMTMzYWI5NTAyMTFkZGRhZmViMjU1MWMwNTJlMWNmZmY2MTcyZmM2OGNkYTMyMTliMDg0In19fQ=="), "&4\u2620 Empty Reinforced Jar", "&7\u21E8 One time use.");
            LIFE_FORCE_EXTRACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ2MTIwNDk5YjMyNDYzOWNkOWExNzc5OWVhMWRmZmYzNzNlNzlhZmUxZjBkOGI1MzI4Y2Y0Nzg5NmM0Nzc1In19fQ=="), "&3Life Force Extractor", "", "&4End-Game Machine", "&7\u2600 World: &fNether", "&7\u21E8 Extracts from Nether blocks", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            SPAWNER_EGG_EXTRACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTA5ODNjNTZjMjZjYzI3M2FhZGIwNWM3NGRjZmJjNjE2ZjY5YTdjMWRiY2JlYzEwMmZmZDhiNjNhZTZiNWYifX19"), "&3Spawner Egg Extractor", "", "&4End-Game Machine", "&7\u21E8 Extracts Spawn Egg From Spawner", "&7\u21E8 Gives: Empty Spawner and Spawn Egg.", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            SPAWNER_REINTEGRATER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGZhMWVmNDdkYWVjZmJkYTdlNTUwNWExYmE2NTc5MjZmNjIxMjQ3Yzg3NjFjZDQ2YzkwNzczNjY2MWJiZSJ9fX0="), "&3Spawner Reintegrater", "", "&4End-Game Machine", "&7\u21E8 Sets Spawner Type", "&7\u21E8 Needs: Empty Spawner and Spawn Egg.", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            SOUL_EXTRACTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNlODMwZjIwZDBkZmIyNDI3Nzk3NTFmN2E1YzY4ZTY3ODg4OGY3Mjc1ODYxNDY1NGI2NTU2MjVjZjk0MzI0MyJ9fX0="), "&3Soul Extractor", "", "&4End-Game Machine", "&7\u2600 World: &fNether", "&7\u21E8 Extracts from Soul Sand", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            FORCE_FIELD_RESONATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ZmNGI4ZThkZWFmYzYwYzMzMTlmNDQ5ZTU1ODFiMzNlZmMyYzFmYjZjYThiZGJlNGM3MjI3NjIxYjhiNyJ9fX0="), "&3Force Field Resonator", "&7Any machine using this part", "&7Will need to be in a force field to work");
            TNT_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBlNGM2NzBjOWIyYzE5YTQ4YTkyMzExYThkN2Y4MmEzOTY1YmMyOTdhMjIwYzg5ZTE2NjgyNTQxN2U4In19fQ=="), "&3TNT Factory", "", "&4End-Game Machine", "&6Needs: Sand, Gunpowder", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            BLOCK_ASSEMBLER_DISASSEMBLER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTAxODRlNGNhNDBmNzdjN2I4NjMwOWVhZTRlYWNiNjhjNzczOWYyMDZmMGM0OTQxMmY1MDdhMTAyOGMzMTZkOCJ9fX0="), "&3Block Assembler And Disassembler", "", "&4End-Game Machine", "&6Turns Items into Blocks or Vice Versa", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            LUCKY_BLOCK_OPENER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2NDY0YTViYTExZTFlNTlmMDk0OGEzZDk1ODQ2NjU0MjUzYmYyODIyYzZiMWMxYjNhNGEzZmQzMWJhNGYifX19"), "&3Lucky Block Opener", "", "&4End-Game Machine", "&6Opens All Blocks 5 Blocks Above Machine",  "&4 Lucky Blocks Can Cause Griefing", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            TITAN_DEATH_RECOVERY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU1Zjc4ZGRhZDVkNWVkZGUzYjNiZDg1YjIwYzgxMDVmZDkxMzIzNTU0OTFiM2Y0YzI2ZjRjMWE2MGQ5ZDMwOCJ9fX0="), "&7Titan Death Recovery", "", "&4End-Game Machine", "&8\u21E8 &7When You Die, Places Item In Storage Units and", "&8\u21E8 &7Then Saves Whats Left", "&8\u21E8 &7Requires: Force Field", "&8\u21E8 &7Allowed: One Per Player", "&8\u21E8 &e\u26A1 &71320 J/s");
            TITAN_DEATH_RECOVERY_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU1Zjc4ZGRhZDVkNWVkZGUzYjNiZDg1YjIwYzgxMDVmZDkxMzIzNTU0OTFiM2Y0YzI2ZjRjMWE2MGQ5ZDMwOCJ9fX0="), "&7Titan Death Recovery II", "", "&4End-Game Machine", "&8\u21E8 &7When You Die, Places Item In Storage Units and", "&8\u21E8 &7Then Saves Whats Left", "&8\u21E8 &7Speed: x100", "&8\u21E8 &7Requires: Force Field", "&8\u21E8 &7Allowed: One Per Player",  "&8\u21E8 &7Most Remove Tier I First","&8\u21E8 &e\u26A1 &71320 J/s");

            TITAN_ITEM_RECOVERY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDkwOTdjYTU5MzM0MDIwZTRjMTZkNWJhMzZjYjI0NTgwZWU2NmM5YzJmNDI1YTNiNTY1NGZkYzQ3YjhkYjZmNiJ9fX0="), "&7Titan Item Recovery", "", "&4End-Game Machine", "&8\u21E8 &7Saves Items When They Break", "&8\u21E8 &7Repair Factor: 100%", "&8\u21E8 &7Requires: Force Field", "&8\u21E8 &7Allowed: One Per Player", "&8\u21E8 &e\u26A1 &71320 J/s");
            TITAN_ITEM_RECOVERY_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDkwOTdjYTU5MzM0MDIwZTRjMTZkNWJhMzZjYjI0NTgwZWU2NmM5YzJmNDI1YTNiNTY1NGZkYzQ3YjhkYjZmNiJ9fX0="), "&7Titan Item Recovery II", "", "&4End-Game Machine", "&8\u21E8 &7Saves Items When They Break", "&8\u21E8 &7Speed: x100", "&8\u21E8 &7Repair Factor: 100%", "&8\u21E8 &7Requires: Force Field", "&8\u21E8 &7Allowed: One Per Player",  "&8\u21E8 &7Most Remove Tier I First","&8\u21E8 &e\u26A1 &71320 J/s");
            TITAN_AUTO_ANVIL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI0MjVhYTNkOTQ2MThhODdkYWM5Yzk0ZjM3N2FmNmNhNDk4NGMwNzU3OTY3NGZhZDkxN2Y2MDJiN2JmMjM1In19fQ=="), "&7Titan Auto Anvil", "", "&4End-Game Machine", "&8\u21E8 &7Repair Factor: 100%", "&8\u21E8 &7Requires: Duct Tape","&8\u21E8 &e\u26A1 &7320 J/s");
            STORAGE_MOVER = new CustomItem(CustomSkull.getItem(STORAGE_MOVER_S), "&5Storage Mover",  new String[] {"&8\u21E8 &7Right Click Vanilla Storage to move it.", "&8\u21E8 &7Left Click Vanilla Storage to fill it.", "&8\u21E8 Holds Unlimited Items", "&8\u21E8 Clears All content on server restart." });
            HEAD_RECOVERY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFmOTZiZmM5MDVjNDY4OTY5OGMwOWNkMmNiYjgxODgyNTE0NmMxYmM2MTg0OTQwMzNlODA3N2NiOWE3MCJ9fX0="), "&3Slimefun Head Recovery", "", "&4End-Game Machine", "&6Needs: Broke Slimefun Heads", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            DATA_CENTER = new CustomItem(CustomSkull.getItem(DATACENTER), "&5Data Center",  new String[] {"&4End Game", "&8\u21E8 &7shows you information, just click in hand."});
            COMMAND_CENTER = new CustomItem(CustomSkull.getItem(COMMANDCENTER), "&5Data Center",  new String[] {"&4End Game", "&8\u21E8 &7shows you information, just click in hand."});
            FORCE_FIELD_YELLOW = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRkYTAzMWY0NGZlM2VhOGFhNzJmODdiN2M4ZGIyNjlmMmJiYTA3YjRkOGRjMWVhOTRhYjdjMGYxMzBlOTA1ZSJ9fX0="), "&cForce Field &7(&eI&7)", "", "&4End-Game Protection", "&8\u21E8 &7Size: 15+ Blocks Radius ", "&8\u21E8 &e\u26A1 &7Up To &732,000 J/s", "&7Recommend to be on its own energy net.");
            FORCE_FIELD_GREEN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhlZDNmYjVjMDMwMDE3NjRkOGIxMmM1MWQxMThmNjJlYmZmOWVjNGQxNWFhMzc2N2I5YzgwYTVkNTMzNTJhYiJ9fX0="), "&cForce Field &7(&eI&7)", "", "&4End-Game Protection", "&8\u21E8 &7Size: 15+ Blocks Radius ", "&8\u21E8 &e\u26A1 &7Up To &732,000 J/s", "&7Recommend to be on its own energy net.");
            FORCE_FIELD_BLUE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDc1MmJkNjcyMWQyMjk1NWQ0ZGNkZjIwM2Y2ZjZiMzdhMTBhMzI4ZjI0ZWRhMjJiNmFhZWJiOWIzN2NkMTFhMiJ9fX0="), "&cForce Field &7(&eI&7)", "", "&4End-Game Protection", "&8\u21E8 &7Size: 15+ Blocks Radius ", "&8\u21E8 &e\u26A1 &7Up To &732,000 J/s", "&7Recommend to be on its own energy net.");
            ECLIPSE_COIL = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2UzYmM0ODkzYmE0MWEzZjczZWUyODE3NGNkZjRmZWY2YjE0NWU0MWZlNmM4MmNiN2JlOGQ4ZTk3NzFhNSJ9fX0="), "&dEclipse Coil");
            TITAN_MOTOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ=="), "&dTitan Motor");
            THERMAL_GENERATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM3NDUyZTFjMTYzZWMyMWJmMjlkNDZmM2Q5YmI2MzU3ZTFjMDUwMDAzNDE4MTM5M2M4NDI3NjIxZjMxM2ZjNyJ9fX0="), "&cThermal Generator", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &78192 J Buffer", "&8\u21E8 &e\u26A1 &7100 to 5000 J/s", "&bRequires:","&63x3 of lava below", "&63x3 of Air above", "&4Could Exploded!", "&7Must be lower than y=100.", "&7Lower it is the more power it makes.");
            FREE_ENERGY_I = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE3ZDI3MTdkMTBiZWM0MTU5ZThmMWQzMzM5ZWFlMzUyMjk3YTYwNzE0M2E5NmIyOWU4ZWMyODczN2UwZDkifX19"), "&cFree Energy &7(&eI&7)", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &78192 J Buffer", "&8\u21E8 &e\u26A1 &7500 J/s", "&bRequires:","&6Nothing");
            FREE_ENERGY_II = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE3ZDI3MTdkMTBiZWM0MTU5ZThmMWQzMzM5ZWFlMzUyMjk3YTYwNzE0M2E5NmIyOWU4ZWMyODczN2UwZDkifX19"), "&cFree Energy &7(&eII&7)", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &78192 J Buffer", "&8\u21E8 &e\u26A1 &71000 J/s", "&bRequires:","&6Nothing");
            FREE_ENERGY_III = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE3ZDI3MTdkMTBiZWM0MTU5ZThmMWQzMzM5ZWFlMzUyMjk3YTYwNzE0M2E5NmIyOWU4ZWMyODczN2UwZDkifX19"), "&cFree Energy &7(&eIII&7)", "", "&4End-Game Generator", "&8\u21E8 &e\u26A1 &78192 J Buffer", "&8\u21E8 &e\u26A1 &72000 J/s", "&bRequires:","&6Nothing");
            ZeroLuckyBlock = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjNiNzEwYjA4YjUyM2JiYTdlZmJhMDdjNjI5YmEwODk1YWQ2MTEyNmQyNmM4NmJlYjM4NDU2MDNhOTc0MjZjIn19fQ=="), "&rLucky Block", new String[]{"&7Luck: &r0"});
            LuckyBlock = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjNiNzEwYjA4YjUyM2JiYTdlZmJhMDdjNjI5YmEwODk1YWQ2MTEyNmQyNmM4NmJlYjM4NDU2MDNhOTc0MjZjIn19fQ=="), "&rVery lucky Block", new String[]{"&7Luck: &a+80"});
            UnLuckyBlock = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjNiNzEwYjA4YjUyM2JiYTdlZmJhMDdjNjI5YmEwODk1YWQ2MTEyNmQyNmM4NmJlYjM4NDU2MDNhOTc0MjZjIn19fQ=="), "&rVery unlucky Block", new String[]{"&7Luck: &c-80"});
            PandorasBox = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZjN2RkZTUxMjg3MWJkNjA3Yjc3ZTY2MzVhZDM5ZjQ0ZjJkNWI0NzI5ZTYwMjczZjFiMTRmYmE5YTg2YSJ9fX0="), "&5Pandora\'s Box", new String[]{"&7Luck: &c&oERROR"});
            ELECTRIC_COBBLE_TO_DUST = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTU0OTQ3ZGU3ZjUyNTk4MjU1ZDZhZmVlOWQ3N2JlZmFkOWI0ZjI0YzBjNDY2M2QyOGJjZGY4YTY0NTdmMzQifX19"), "&3Electric Cobble to Dust", "", "&4End-Game Machine", "&6Has a small chance of Lucky and Eclipse Nuggets", "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &7200 J/s");
            ELECTRIC_COBBLE_TO_INGOT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRkYmNiN2UxZmJlY2VhOWE3MzUwNDM2Y2JiZWEyYjQ5NmY3NGMyOTcyMDRmMWJiOWFjYzM4NzhkNTQyY2NiIn19fQ=="), "&3Electric Cobble to Ingot", "", "&4End-Game Machine", "&6Has a small chance of Lucky and Eclipse Ingots", "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &7400 J/s");
            ELECTRIC_COBBLE_TO_DUST_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTU0OTQ3ZGU3ZjUyNTk4MjU1ZDZhZmVlOWQ3N2JlZmFkOWI0ZjI0YzBjNDY2M2QyOGJjZGY4YTY0NTdmMzQifX19"), "&3Electric Cobble to Dust &7(&eII&7)", "", "&4End-Game Machine", "&6Has a small chance of Lucky and Eclipse Nuggets", "Gives 3 dust instead of 1", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &7400 J/s");
            ELECTRIC_COBBLE_TO_INGOT_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRkYmNiN2UxZmJlY2VhOWE3MzUwNDM2Y2JiZWEyYjQ5NmY3NGMyOTcyMDRmMWJiOWFjYzM4NzhkNTQyY2NiIn19fQ=="), "&3Electric Cobble to Ingot &7(&eII&7)", "", "&4End-Game Machine", "&6Has a small chance of Lucky and Eclipse Ingots", "Gives 3 Ingots instead of 1", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &7600 J/s");
            ELECTRIC_COBBLE_TO_DUST_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTU0OTQ3ZGU3ZjUyNTk4MjU1ZDZhZmVlOWQ3N2JlZmFkOWI0ZjI0YzBjNDY2M2QyOGJjZGY4YTY0NTdmMzQifX19"), "&3Electric Cobble to Dust &7(&eIII&7)", "", "&4End-Game Machine", "&6Has a small chance of Lucky and Eclipse Nuggets", "Gives 5 dust instead of 1", "&8\u21E8 &7Speed: 20x", "&8\u21E8 &e\u26A1 &7600 J/s");
            ELECTRIC_COBBLE_TO_INGOT_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRkYmNiN2UxZmJlY2VhOWE3MzUwNDM2Y2JiZWEyYjQ5NmY3NGMyOTcyMDRmMWJiOWFjYzM4NzhkNTQyY2NiIn19fQ=="), "&3Electric Cobble to Ingot &7(&eIII&7)", "", "&4End-Game Machine", "&6Has a small chance of Lucky and Eclipse Ingots", "Gives 5 Ingots instead of 1", "&8\u21E8 &7Speed: 20x", "&8\u21E8 &e\u26A1 &7800 J/s");
            ELECTRIC_LUCKY_BLOCK_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI1ZDhiOWEzYTk0MjFkY2VkYjE3ZDcxZTNhODg0ZDk1ZWM1MDM4YzgzOGNlMTllZDZkOGU5NmM1YjIzZWQ3In19fQ=="), "&3Electric Lucky Block Factory", "", "&4End-Game Machine", "&6Will take any Gold Ingot", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7600 J/s");
            ELECTRIC_LUCKY_BLOCK_GRINDER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFkNTQyNGQ5OTAzOTUzODQzNTI2YTdjNDE2ODY2ZTdkNzk1MDFjODhjZTdjZGFiZWVlNTI4NGVhMzlmIn19fQ=="), "&3Electric Lucky Block Grinder", "", "&4End-Game Machine", "&6Will almost anything Lucky or Not", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7600 J/s");
            CHARCOAL_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE3ZDJhN2ZiYjRkMzdiNGQ1M2ZlODc3NTcxMjhlNWVmNjZlYzIzZDdmZjRmZTk5NDQ1NDZkYmM4Y2U3NzcifX19"), "&3Charcoal Factory", "", "&4End-Game Machine", "&6Needs: Sapling", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            CHARCOAL_FACTORY_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE3ZDJhN2ZiYjRkMzdiNGQ1M2ZlODc3NTcxMjhlNWVmNjZlYzIzZDdmZjRmZTk5NDQ1NDZkYmM4Y2U3NzcifX19"), "&3Charcoal Factory II", "", "&4End-Game Machine", "&6Needs: Sapling", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &7300 J/s");
            CHARCOAL_FACTORY_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE3ZDJhN2ZiYjRkMzdiNGQ1M2ZlODc3NTcxMjhlNWVmNjZlYzIzZDdmZjRmZTk5NDQ1NDZkYmM4Y2U3NzcifX19"), "&3Charcoal Factory III", "", "&4End-Game Machine", "&6Needs: Sapling", "&8\u21E8 &7Speed: 20x", "&8\u21E8 &e\u26A1 &7300 J/s");
            ELECTRIC_RENAMER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDAwOTFjNzdiOTM5YjI1NjVkOTY2OWFlZDJmZGMxOTdhYzlkZTkwZjdjNGU5NmZiMmY4ZWIxNDZkOGExMWZhNSJ9fX0="), "&3Electric Renamer", "", "&4End-Game Machine", "&6Needs: Renamed Name Tags", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            LOG_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg4YzQ3MzllMjI4M2FhYTYyODE5NTk0ODZkMTQ5ZjMwNmI2ZTY3MjlhNmExYjNiZjljODUzYTIyYTkifX19="), "&3Log Factory", "", "&4End-Game Machine", "&6Needs: Sapling", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            LOG_FACTORY_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg4YzQ3MzllMjI4M2FhYTYyODE5NTk0ODZkMTQ5ZjMwNmI2ZTY3MjlhNmExYjNiZjljODUzYTIyYTkifX19="), "&3Log Factory II", "", "&4End-Game Machine", "&6Needs: Sapling", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &7300 J/s");
            LOG_FACTORY_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg4YzQ3MzllMjI4M2FhYTYyODE5NTk0ODZkMTQ5ZjMwNmI2ZTY3MjlhNmExYjNiZjljODUzYTIyYTkifX19="), "&3Log Factory III", "", "&4End-Game Machine", "&6Needs: Sapling", "&8\u21E8 &7Speed: 20x", "&8\u21E8 &e\u26A1 &7300 J/s");
            STRIPPED_LOG_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk2M2EyZmY3MDBkZDc0ZjI5NzlhNzU4ZWM1ZDNkODE5MGM1YWJlMTU1Yjc4NTIwNmYzZDMyNjk5NTE3YTMzYSJ9fX0="), "&3Stripped Log Factory", "", "&4End-Game Machine", "&6Needs: Sapling or Logs", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            STRIPPED_LOG_FACTORY_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk2M2EyZmY3MDBkZDc0ZjI5NzlhNzU4ZWM1ZDNkODE5MGM1YWJlMTU1Yjc4NTIwNmYzZDMyNjk5NTE3YTMzYSJ9fX0="), "&3Stripped Log Factory II", "", "&4End-Game Machine", "&6Needs: Sapling or Logs", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &7300 J/s");
            STRIPPED_LOG_FACTORY_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk2M2EyZmY3MDBkZDc0ZjI5NzlhNzU4ZWM1ZDNkODE5MGM1YWJlMTU1Yjc4NTIwNmYzZDMyNjk5NTE3YTMzYSJ9fX0="), "&3Stripped Log Factory III", "", "&4End-Game Machine", "&6Needs: Sapling or Logs", "&8\u21E8 &7Speed: 20x", "&8\u21E8 &e\u26A1 &7300 J/s");
            FISHER_MACHINE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWYxMmUwZGZlNjhhZWY3Y2I1YzA2Yzc3Y2YyNzIyMzBhNWNkNjgyYmM0NTJjYjY5OWIyMTc3ZGY1ZTZhZjY0In19fQ=="), "&3Fisher Machine", "", "&4End-Game Machine", "&6Place over water.", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            SAPLING_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmMWQxNTMyODNhZmMxNTE4ZDdhYTZjMDliNmVlYzQyZmNlNmY0MGJhODZiYWEzY2U0NmIyNmI2NTdlOWY5ZiJ9fX0="), "&3Sapling Factory", "", "&4End-Game Machine", "&6Needs: Bone Meal", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            SAPLING_FACTORY_2 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmMWQxNTMyODNhZmMxNTE4ZDdhYTZjMDliNmVlYzQyZmNlNmY0MGJhODZiYWEzY2U0NmIyNmI2NTdlOWY5ZiJ9fX0="), "&3Sapling Factory II", "", "&4End-Game Machine", "&6Needs: Bone Meal", "&8\u21E8 &7Speed: 10x", "&8\u21E8 &e\u26A1 &7300 J/s");
            SAPLING_FACTORY_3 = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmMWQxNTMyODNhZmMxNTE4ZDdhYTZjMDliNmVlYzQyZmNlNmY0MGJhODZiYWEzY2U0NmIyNmI2NTdlOWY5ZiJ9fX0="), "&3Sapling Factory III", "", "&4End-Game Machine", "&6Needs: Bone Meal", "&8\u21E8 &7Speed: 20x", "&8\u21E8 &e\u26A1 &7300 J/s");


            NUGGETTOINGOT = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2RmZmQ5NWM4ZmNkYmFlZDllOWU5YmU0N2VhMWE0MWVlMTIyOWQ4OWI1YzZkNDJhMWY3YjJmN2YxMWJkMTEyMiJ9fX0="), "&3Nugget to Ingot", "", "&4End-Game Machine", "&6Works with Lucky, Eclipse, Titan", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            INGOTUP = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDViZTRiYTgyODYxZWZmY2Q4ZGNjNWMyNDY5YjllZThkNGUwMjFiMDYwZGFhZjM0ODMxOWUwMjAxMTgyOCJ9fX0="), "&3Ingot Up", "", "&4End-Game Machine", "&6Works with Lucky, Eclipse", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &7300 J/s");
            BONE_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJiNDMzOTFhOWRiZmY5ZWFkYWUwN2I2ODYyNTk1YzkxZDA0YzU5MzhlMjNjMjg1YWM2MGM0Yjg3NjliMjQifX19"), "&3Bone Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNormal" );
            IRON_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjEzMWEzNmU3MGZmYWE3Y2E3ZTY3MmFlNmFjMjBiN2ZjMWU0NTdjNDNhOGUxMDY5ZTdiMTRlY2RiODU3NiJ9fX0="), "&3Iron Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNormal" );
            XP_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5YTI3ODE3YWJmODhhMmY2ZjIzMDEyYzc1ODY5MmNkOWJjZmY5OGE4ZGZjMGVmNjk1NzljNDQyZjZhYTZkZiJ9fX0="), "&3XP Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNormal" );
            SLIME_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzNlOGQ3NDZlNGJjMWZjMjlkNWUzYmQ1MzIyYjI5ODZjYmFmODZiMDY2ZmE4MzYxOGJmMjQ2ZmRmMzczIn19fQ=="), "&3Slime Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNormal" );
            BLAZE_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNjZmQ5NGU5MjVlYWI0MzMwYTc2OGFmY2FlNmMxMjhiMGEyOGUyMzE0OWVlZTQxYzljNmRmODk0YzI0ZjNkZSJ9fX0="), "&3Blaze Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNether");
            ENDERMAN_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBkN2FiMmMxYzhiNDlmMWM2ZTgxMmExOTBlOWQyYTg3N2U4NjE0ZmM1YzVmMmNlNjNhNWE1ZDhhYzZiMGQ0In19fQ=="), "&3Ender Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fThe End" );
            NETHERRACK_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliOTkyMzhkOWY4MTM0MDE1ZDc0MzQxYjE3MTE1YmUzMGYxMjVhMzdlYTQ5MDMyYjhjOWUzYjc2OWNmN2VlMSJ9fX0="), "&3Netherrack Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNether");
            NETHERWART_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2IxZWEwNzJjY2E1ZmQxZTJiNzlkYWYwMjY3MDczZDIzZmRiNTA2NzVkNmFjMTViYWJlM2E3MzVjYzM4Yzc1In19fQ=="), "&3Netherwart Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNether" );
            SOULSAND_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk5NGUzZWJkZTlhZmEzZjlmOWM3ZWY4NzlhYWZmN2Y2YmY0NTNiNDNkZjE3NTk2MjJhZWYwZWM0YWFmYzJhZiJ9fX0="), "&3Soulsand Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNether" );
            WITHERSKULL_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFmZjVjOTE4M2Y5ZmVlZWI3ZjRiNjFjNDhjYjM1N2Q0MDE1OGYyMmQ3YTUzOTczMWM1MzFjM2QyZDg3In19fQ=="), "&3Wither Skull Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNether" );
            QUARTZ_FACTORY = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE3NGFjNGQxZjljYmQxMDdkNGFhODU4YTIzMmM0NDkxNTU3OWFhY2EzMmFhMmFmOTU0NTlmY2EwYWFjOWIifX19"), "&3Quartz Factory", "", "&4End-Game Machine", "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &710,000 J/s", "&7\u2600 World: &fNether" );

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}

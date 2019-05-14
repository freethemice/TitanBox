package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.PickThreeContainer;
import com.firesoftitan.play.titanbox.managers.NPCManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.SlimefunLuckyBlocks.SlimefunLuckyBlocks;
import me.mrCookieSlime.SlimefunLuckyBlocks.Surprise;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class LuckyBlockOpener extends PickThreeContainer {
    public LuckyBlockOpener(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.border_mat_1 = null;
        this.border_mat_2 = Material.GOLD_BLOCK;
        this.border_info_2.add(ChatColor.BLUE + "Very Lucky Block");
        this.border_info_2.add(ChatColor.BLUE + "Lucky Block");
        this.border_info_2.add(ChatColor.BLUE + "UnLucky Block");
        this.border_info_2.add(ChatColor.BLUE + "Pandora's box");
        this.border_mat_3 = null;
        setupUnit();
    }

    @Override
    public String getInventoryTitle() {
        return "&bLucky Block Opener";
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
                pushItems(b, new ItemStack[] {SlimefunItems.STONE_CHUNK});
                try {
                    Field f = SlimefunLuckyBlocks.class.getDeclaredField("luckylist"); //NoSuchFieldException
                    f.setAccessible(true);
                    Map<Surprise.LuckLevel, List<Surprise>> iWantThis = (Map<Surprise.LuckLevel, List<Surprise>>) f.get(SlimefunLuckyBlocks.class);
                    List<Surprise> Lucky = iWantThis.get(Surprise.LuckLevel.LUCKY);
                    List<Surprise> UnLucky = iWantThis.get(Surprise.LuckLevel.UNLUCKY);
                    List<Surprise> Pandora = iWantThis.get(Surprise.LuckLevel.PANDORA);
                    List<Surprise> BothLuck = new ArrayList<Surprise>();
                    BothLuck.addAll(Lucky);
                    BothLuck.addAll(UnLucky);
                    Random rnd = new Random(System.currentTimeMillis());
                    Location target = b.getLocation().clone().add(0, 5 ,0);

                    if (Utilities.isItemEqual(processing.get(b).getOutput()[0], SlimefunItemsManager.LuckyBlock)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                CraftPlayer opCr = NPCManager.getCraftNPC(target);
                                Surprise surprise = Lucky.get(rnd.nextInt(Lucky.size()));
                                surprise.activate(opCr, target);
                            }
                        }.runTaskLater(TitanBox.instants, 1);

                    }
                    else if (Utilities.isItemEqual(processing.get(b).getOutput()[0], SlimefunItemsManager.ZeroLuckyBlock)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                CraftPlayer opCr = NPCManager.getCraftNPC(target);
                                Surprise surprise = BothLuck.get(rnd.nextInt(BothLuck.size()));
                                surprise.activate(opCr, target);
                            }
                        }.runTaskLater(TitanBox.instants, 1);

                    }
                    else if (Utilities.isItemEqual(processing.get(b).getOutput()[0], SlimefunItemsManager.UnLuckyBlock)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                CraftPlayer opCr = NPCManager.getCraftNPC(target);
                                Surprise surprise = UnLucky.get(rnd.nextInt(UnLucky.size()));
                                surprise.activate(opCr, target);
                            }
                        }.runTaskLater(TitanBox.instants, 1);

                    }
                    else if (Utilities.isItemEqual(processing.get(b).getOutput()[0], SlimefunItemsManager.PandorasBox)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                CraftPlayer opCr = NPCManager.getCraftNPC(target);
                                Surprise surprise = Pandora.get(rnd.nextInt(Pandora.size()));
                                surprise.activate(opCr, target);
                            }
                        }.runTaskLater(TitanBox.instants, 1);

                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {

            int slot1 = getInputSlots()[0];
            if (!Utilities.isEmpty(BlockStorage.getInventory(b).getItemInSlot(slot1))) {
                if (Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), SlimefunItemsManager.LuckyBlock) ||
                        Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), SlimefunItemsManager.ZeroLuckyBlock) ||
                        Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), SlimefunItemsManager.UnLuckyBlock) ||
                        Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot1), SlimefunItemsManager.PandorasBox)){

                        ItemStack adding = BlockStorage.getInventory(b).getItemInSlot(slot1).clone();
                        adding.setAmount(1);
                        MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
                        if (!fits(b, new ItemStack[] {SlimefunItems.STONE_CHUNK})) return;
                        BlockStorage.getInventory(b).replaceExistingItem(slot1, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot1), 1));
                        processing.put(b, r);
                        progress.put(b, r.getTicks());
                }
            }

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "LUCKY_BLOCK_OPENER";
    }

}

package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.containers.MinerContainer;
import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ElectricMiner extends MinerContainer {;
    public static Config miners = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "miners.yml");

    public ElectricMiner(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

    }

    @Override
    public String getInventoryTitle() {
        return "&bElectric Miner";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_SHOVEL);
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

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else progress.put(b, timeleft - 1);
            }
            else if (ChargableBlock.isChargable(b)) {
                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                if (processing.get(b).getOutput() == null)
                {
                    progress.remove(b);
                    return;
                }
                ChargableBlock.addCharge(b, -getEnergyConsumption());

                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(TitanBox.instants, new Runnable() {
                @Override
                public void run() {
                    ItemStack checker = null;
                    if (TitanBox.isEmpty(BlockStorage.getInventory(b).getItemInSlot(10))) {
                        return;
                    }
                    if (BlockStorage.getInventory(b).getItemInSlot(10).getAmount() < 40)
                    {
                        return;
                    }
                    checker = SlimefunItemsHolder.DRILL_ROD.clone();
                    checker.setAmount(1);
                    if (!SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(11), checker, true)) {
                        return;
                    }
                    checker = SlimefunItems.REACTOR_COOLANT_CELL;
                    checker.setAmount(1);
                    if (!SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(37), checker, true)) {
                        return;
                    }
                    checker = new ItemStack(Material.TNT);
                    checker.setAmount(1);
                    if (!SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(38), checker, true)) {
                        return;
                    }
                    ItemStack walls = BlockStorage.getInventory(b).getItemInSlot(10).clone();
                    if ((walls.getType() == Material.PLAYER_HEAD))
                    {
                        return;
                    }
                    if (!walls.getType().isBlock())
                    {
                        return;
                    }

                    BlockStorage.getInventory(b).replaceExistingItem(10, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(10), 40));
                    BlockStorage.getInventory(b).replaceExistingItem(11, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(11), 1));
                    BlockStorage.getInventory(b).replaceExistingItem(37, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(37), 1));
                    BlockStorage.getInventory(b).replaceExistingItem(38, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(38), 1));

                    //for (int slot: getInputSlots()) {
                        //if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.COBBLESTONE, 8), true)) {

                    ItemStack adding = SlimefunItemsHolder.MINING_SLUDGE.clone();
                    adding.setAmount(7);
                    List<ItemStack> Oreoutput = new ArrayList<ItemStack>();
                    Oreoutput.add(adding);
                    ItemStack pickaxe = null;
                    if (!TitanBox.isEmpty(BlockStorage.getInventory(b).getItemInSlot(49)))
                    {
                        pickaxe = BlockStorage.getInventory(b).getItemInSlot(49).clone();
                        if (pickaxe.getType() != Material.DIAMOND_PICKAXE && pickaxe.getType() == Material.IRON_PICKAXE || pickaxe.getType() == Material.STONE_PICKAXE || pickaxe.getType() == Material.GOLDEN_PICKAXE || pickaxe.getType() == Material.WOODEN_PICKAXE)
                        {
                            pickaxe = null;
                        }
                    }

                    int y = 0;

                    for (y = 1; y < 255; y++)
                    {
                        Location loc = b.getLocation().add(0, -1 * y, 0);
                        if (loc.getBlockY() > 1) {
                            if (loc.getBlock().getType() != Material.END_ROD) {
                                break;
                            }
                        }
                    }
                    Location depthCheck  = b.getLocation().clone().add(0, -1 *y, 0);
                    if (depthCheck.getBlockY()  <  5)
                    {
                        return;
                    }
                    for(int x = -5; x < 6; x++)
                    {
                        for(int z = -5; z < 6; z++)
                        {
                            Location loc = b.getLocation().add(x, -1 *y, z);
                            Block block = loc.getBlock();
                            if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                                if (y > 5) {
                                    Collection<ItemStack> drops = block.getDrops();
                                    if (!TitanBox.isEmpty(pickaxe))
                                    {
                                        int f = pickaxe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                        for(ItemStack drop: drops)
                                        {
                                            if (drop.getType() == Material.DIAMOND || drop.getType() == Material.IRON_ORE || drop.getType() == Material.GOLD_ORE || drop.getType() == Material.COAL || drop.getType() == Material.LAPIS_LAZULI || drop.getType() == Material.REDSTONE || drop.getType() == Material.QUARTZ || drop.getType() == Material.EMERALD) {
                                                drop.setAmount(drop.getAmount() + f);
                                            }
                                        }
                                    }
                                    Oreoutput.addAll(drops);
                                    block.setType(Material.AIR);
                                    if (x == -5 || z == -5 || x == 5 || z == 5)
                                    {
                                        block.setType(walls.getType());
                                    }
                                }
                                if (x == 0 && z == 0)
                                {
                                    miners.setValue(block.getLocation().toString() + ".set", true);
                                    block.setType(Material.END_ROD);
                                }

                            }
                        }
                    }
                    MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], Oreoutput.toArray(new ItemStack[Oreoutput.size()]));
                    //if (!fits(b, r.getOutput())) return;
                    //BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 8));
                    processing.put(b, r);
                    progress.put(b, r.getTicks());

                }
                    //}
                //}
            }, 1);

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_MINER";
    }

}

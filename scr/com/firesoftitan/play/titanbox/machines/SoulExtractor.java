package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.PickThreeContainer;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class SoulExtractor extends PickThreeContainer {;
    public SoulExtractor(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.border_mat_1 = null;
        this.border_mat_2 = Material.PAPER;
        this.border_info_2.add("&4\u2620 Empty Reinforced Jar");
        this.border_mat_3 = null;
        setupUnit();
    }

    @Override
    public String getInventoryTitle() {
        return "&bSoul Extractor";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_AXE);
    }

    @Override
    public void registerDefaultRecipes() {}

    public abstract int getSpeed();

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        if (b.getWorld().getEnvironment() != World.Environment.NETHER)
        {
            ItemStack item = getProgressBar().clone();
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.RED + "Offline");
            List<String> lore = new ArrayList<String>();
            lore.add("Only works in a " + Utilities.fixCapitalization(World.Environment.NETHER.name()) + " like world.");
            im.setLore(lore);
            item.setItemMeta(im);
            BlockStorage.getInventory(b).replaceExistingItem(31, item);
            return;
        }
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

                BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "Waiting for jar..."));
                ItemStack loreItem = processing.get(b).getOutput()[0];
                Location location = Utilities.deserializeLocation(Utilities.getLore(loreItem).get(0));
                new BukkitRunnable()
                {

                    @Override
                    public void run() {
                        Block block = location.getBlock();
                        block.setType(loreItem.getType());
                    }
                }.runTask(TitanBox.instants);
                pushItems(b, new ItemStack[] {SlimefunItemsManager.JAR_OF_SOULS.clone()});

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            for (int slot: getInputSlots()) {
                if (!Utilities.isEmpty(BlockStorage.getInventory(b).getItemInSlot(slot))) {
                    if (Utilities.isItemEqual(BlockStorage.getInventory(b).getItemInSlot(slot), SlimefunItemsManager.EMPTY_JAR)) {
                        Random random = new Random(System.currentTimeMillis());
                        int ix = random.nextInt(6) - 3;
                        int iy = random.nextInt(6) - 3;
                        int iz = random.nextInt(6) - 3;
                        Location location = b.getLocation().clone().add(ix, iy, iz);
                        if (location.getBlock().getType() == Material.SOUL_SAND) {
                            ItemStack adding = new ItemStack(Material.SAND, 1);
                            sendForProcessing(b, location, adding, slot);
                            return;
                        }
                        else
                        {
                            BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new MaterialData(Material.CLOCK), "Searching for Souls...."));
                            return;
                        }
                    }
                }
            }
            BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "Waiting for jar..."));

        }
    }

    private void sendForProcessing(Block b, Location location, ItemStack adding, int slot) {
        adding = Utilities.addLore(adding, Utilities.serializeLocation(location));
        adding = adding.clone();
        MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
        if (!fits(b, new ItemStack[] {SlimefunItemsManager.JAR_OF_LIFE_FORCE.clone()})) return;
        BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
        processing.put(b, r);
        progress.put(b, r.getTicks());
    }

    @Override
    public String getMachineIdentifier() {
        return "SOUL_EXTRACTOR";
    }

}


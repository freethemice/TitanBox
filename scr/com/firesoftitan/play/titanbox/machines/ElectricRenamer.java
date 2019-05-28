package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.RenamerContainer;
import com.firesoftitan.play.titanbox.managers.NPCManager;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class ElectricRenamer extends RenamerContainer {
    private HashMap<Block, Long> wandTimer = new HashMap<Block, Long>();
    public ElectricRenamer(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

    }

    @Override
    public String getInventoryTitle() {
        return "&bElctric Renamer";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.NAME_TAG);
    }

    @Override
    public void registerDefaultRecipes() {}

    public abstract int getSpeed();

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        if (ChargableBlock.isChargable(b)) {
            if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
            ChargableBlock.addCharge(b, -getEnergyConsumption());
        }

        List<Entity> renameThese = NPCManager.getNearbyEntities(b.getLocation(), 5);
        int[] AllSlots = getInputSlots();
        Random rnd = new Random(System.currentTimeMillis());
        int animails = 0;
        int monsters = 0;
        int animailsold = 0;
        for(Entity entity: renameThese)
        {
            if (entity instanceof Animals) {
                if (entity.getCustomName() == null) {
                    entity.teleport(b.getLocation());
                    animails++;
                    int myname = AllSlots[rnd.nextInt(AllSlots.length)];
                    ItemStack itemStack = BlockStorage.getInventory(b).getItemInSlot(myname);
                    if (!Utilities.isEmpty(itemStack)) {
                        if (itemStack.getType() == Material.NAME_TAG) {
                            String name = Utilities.getName(itemStack);
                            entity.setCustomName(name);
                        }
                    }
                }
                else
                {
                    animailsold++;
                }
            }
            if (entity instanceof Monster)
            {
                monsters++;
            }
        }
        Long l = System.currentTimeMillis();
        if (wandTimer.containsKey(b))
        {
            l = wandTimer.get(b);
        }
        int timeleft = (int) (System.currentTimeMillis() - l);
        if (timeleft > 1*60*60*1000)
        {
            wandTimer.remove(b);
        }

        ItemStack item = getProgressBar().clone();
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(" ");
        List<String> lore = new ArrayList<String>();
        lore.add("Not Named: " + animails);
        lore.add("Named: " + animailsold);
        lore.add("Monsters: " + monsters);
        lore.add("Next Wand: " + Utilities.formatTime(timeleft));
        im.setLore(lore);
        item.setItemMeta(im);
        BlockStorage.getInventory(b).replaceExistingItem(22, item);
        if (!wandTimer.containsKey(b)) {
            item = new ItemStack(Material.LEAD);
            im = item.getItemMeta();
            String MyLocation = b.getLocation().getWorld().getName() + "_" + b.getLocation().getBlockX() + "_" + b.getLocation().getBlockY() + "_" + b.getLocation().getBlockZ();
            im.setDisplayName(ChatColor.GREEN + "Wand for: " + ChatColor.WHITE + MyLocation);
            lore = new ArrayList<String>();
            lore.add(ChatColor.GRAY + "With Wand Click On Unnamed Animal to Teleport To This Machine");
            lore.add(ChatColor.GRAY + "Only available once every hour.");
            im.setLore(lore);
            item.setItemMeta(im);
            BlockStorage.getInventory(b).replaceExistingItem(31, item);
            wandTimer.put(b, System.currentTimeMillis());
        }

    }

    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_RENAMER";
    }

}

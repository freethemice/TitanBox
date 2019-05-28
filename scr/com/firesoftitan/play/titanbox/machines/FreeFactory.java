package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.FreeContainer;
import com.firesoftitan.play.titanbox.enums.RanksEnum;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class FreeFactory extends FreeContainer {;
    private ItemStack freeType = null;
    private ItemStack me = null;
    private World.Environment environment;
    private String myID = "FREE_FACTORY";
    private static HashMap<String, List<String>> housings = new HashMap<String, List<String>>();
    public FreeFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, Material freeType, World.Environment environment) {
        super(category, item, name, recipeType, recipe);
        this.freeType = new ItemStack(freeType, 1);
        this.environment = environment;
        this.me = item.clone();
        myID= name;
        SlimefunItemsManager.addFreeFactories(this);
    }
    public FreeFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack freeType, World.Environment environment) {
        super(category, item, name, recipeType, recipe);
        this.freeType = freeType.clone();
        this.me = item.clone();
        myID= name;
        this.environment = environment;
        SlimefunItemsManager.addFreeFactories(this);
    }

    public ItemStack getMe() {
        return me;
    }

    public static void removeFromForceField(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            if (!tmp.contains(Utilities.serializeLocation(location)))
            {
                tmp.remove(Utilities.serializeLocation(location));
            }
            housings.put(ffH.getId(), tmp);
        }
    }
    public static void addToForceField(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            if (!tmp.contains(Utilities.serializeLocation(location)))
            {
                tmp.add(Utilities.serializeLocation(location));
            }
            housings.put(ffH.getId(), tmp);
        }
    }
    public static boolean isInForceField(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            return true;
        }
        return false;
    }
    public static int sizeForceFieldHas(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            return tmp.size();
        }
        return 0;
    }
    public static RanksEnum getRankInfoFromForceFieldFor(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            RanksEnum ranksEnum = RanksEnum.valueOf(ffH.getOwner());
            return ranksEnum;
        }
        return RanksEnum.RANK_1;
    }
    public static boolean checkForceFieldFor(Location location)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
        if (ffH !=null) {
            List<String> tmp = housings.get(ffH.getId());
            if (tmp == null)
            {
                tmp = new ArrayList<String>();
            }
            if (!tmp.contains(Utilities.serializeLocation(location)))
            {
                return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public String getInventoryTitle() {
        return "&bFree Factory";
    }

    @Override
    public ItemStack getProgressBar() {
        try {
            return CustomSkull.getItem(SlimefunItemsManager.INFO);
        } catch (Exception e) {
            return new ItemStack(Material.GOLDEN_SHOVEL);
        }
    }

    @Override
    public void registerDefaultRecipes() {}

    public abstract int getSpeed();

    public World.Environment getEnvironment() {
        return environment;
    }

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        if (b.getWorld().getEnvironment() != this.environment)
        {
            ItemStack item = getProgressBar().clone();
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.RED + "Offline");
            List<String> lore = new ArrayList<String>();
            lore.add("Only works in a " + Utilities.fixCapitalization(this.environment.name()) + " like world.");
            im.setLore(lore);
            item.setItemMeta(im);
            BlockStorage.getInventory(b).replaceExistingItem(22, item);
            return;
        }
        if (!checkForceFieldFor(b.getLocation()))
        {
            boolean inField = false;
            int size = 0;
            RanksEnum ranksEnum = RanksEnum.RANK_1;
            if (isInForceField(b.getLocation()))
            {
                inField = true;
                size = sizeForceFieldHas(b.getLocation());
                ranksEnum = getRankInfoFromForceFieldFor(b.getLocation());
                if (size < ranksEnum.getTurrets() * 3)
                {
                    addToForceField(b.getLocation());
                    return;
                }
            }
            ItemStack item = getProgressBar().clone();
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.RED + "Offline");
            List<String> lore = new ArrayList<String>();
            if (!inField) lore.add("Must be in a force Field.");
            if (inField) lore.add("You have " + size + " owner's rank allows " + ranksEnum.getTurrets()* 3 + " per Force Field");
            im.setLore(lore);
            item.setItemMeta(im);
            BlockStorage.getInventory(b).replaceExistingItem(22, item);
            return;
        }
        else {
            if (isProcessing(b)) {
                int timeleft = progress.get(b);
                if (timeleft > 0 && getSpeed() < 10) {
                    ItemStack item = getProgressBar().clone();
                    item.setDurability(MachineHelper.getDurability(item, timeleft, processing.get(b).getTicks()));
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName(" ");
                    List<String> lore = new ArrayList<String>();
                    if (ChargableBlock.isChargable(b)) {
                        if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                            lore.add("Not enough power!");
                        }
                    }
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
                    } else progress.put(b, timeleft - 1);
                } else if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());

                    BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));
                    pushItems(b, processing.get(b).getOutput());

                    progress.remove(b);
                    processing.remove(b);
                }
            } else {
                Random number = new Random(System.currentTimeMillis());
                ItemStack adding = freeType.clone();
                adding.setAmount(1 + number.nextInt(10));

                MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{adding});
            /*if (!fits(b, r.getOutput())) return;
            for (int slot: getInputSlots()) {
                BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
            }*/
                processing.put(b, r);
                progress.put(b, r.getTicks());

            }
        }
    }

    @Override
    public String getMachineIdentifier() {
        return myID;
    }

}

package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public abstract class IngotUpFactory extends AContainer {

    private static final int[] border = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private static final int[] border_in_A = new int[]{9, 10, 18, 27, 28, };
    private static final int[] border_in_B = new int[]{11, 12, 21, 29, 30};
    private static final int[] border_out = new int[]{14, 15, 16, 17, 23, 26, 32, 33, 34, 35};

    public IngotUpFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "&bIngot Up Factory";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_SHOVEL);
    }

    @Override
    public void registerDefaultRecipes() {}

    @Override
    public abstract int getSpeed();
    protected void constructMenu(BlockMenuPreset preset) {
        int[] var2 = border;
        int var3 = var2.length;

        int var4;
        int i;
        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE), " ", new String[0]), new ChestMenu.MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        var2 = border_in_A;
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.YELLOW_STAINED_GLASS_PANE), " ", new String[0]), new ChestMenu.MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        var2 = border_in_B;
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.YELLOW_STAINED_GLASS_PANE), " ", new String[0]), new ChestMenu.MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        var2 = border_out;
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.ORANGE_STAINED_GLASS_PANE), " ", new String[0]), new ChestMenu.MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        preset.addItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " ", new String[0]), new ChestMenu.MenuClickHandler() {
            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                return false;
            }
        });
        var2 = this.getOutputSlots();
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
                }
            });
        }

    }


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
        else {;

                int slot = 19;
                int slot2 = 20;
                ItemStack check, check2;
                int amount = 8;
                check = SlimefunItemsHolder.LuckyIngot.clone();
                check2 = SlimefunItemsHolder.TitanStone.clone();
                check.setAmount(amount);
                check2.setAmount(1);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true) && SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot2), check2, true)) {
                    ItemStack adding = SlimefunItemsHolder.EclipseNugget;
                    adding.setAmount(1);
                    MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] {adding});
                    if (!fits(b, r.getOutput())) return;
                    BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), amount));
                    BlockStorage.getInventory(b).replaceExistingItem(slot2, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot2), 1));
                    processing.put(b, r);
                    progress.put(b, r.getTicks());

                }
                amount = 4;
                check = SlimefunItemsHolder.EclipseIngot.clone();
                check2 = SlimefunItemsHolder.TitanStone.clone();
                check.setAmount(amount);
                check2.setAmount(1);
                if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), check, true) && SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot2), check2, true)) {
                    ItemStack adding = SlimefunItemsHolder.TitanNugget;
                    adding.setAmount(1);
                    MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[] {adding});
                    if (!fits(b, r.getOutput())) return;
                    BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), amount));
                    BlockStorage.getInventory(b).replaceExistingItem(slot2, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot2), 1));
                    processing.put(b, r);
                    progress.put(b, r.getTicks());

                }

        }
    }

    @Override
    public String getMachineIdentifier() {
        return "INGOT_UP_FACTORY";
    }

}

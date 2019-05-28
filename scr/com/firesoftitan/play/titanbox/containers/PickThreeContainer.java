//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.firesoftitan.play.titanbox.containers;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.managers.TitanItemManager;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.Map.Entry;

public abstract class PickThreeContainer extends TitanItemManager {
    public static Map<Block, MachineRecipe> processing = new HashMap();
    public static Map<Block, Integer> progress = new HashMap();
    protected List<MachineRecipe> recipes = new ArrayList();
    private static final int[] border = {27, 28, 34, 35, 36, 37, 38, 42, 43, 44};
    private static final int[] border_in_1 = {0, 1, 2, 9, 11, 18, 19, 20};
    private static final int[] border_in_2 = {3, 4, 5, 12, 14, 21, 22, 23};
    private static final int[] border_in_3 = {6, 7, 8, 15, 17, 24, 25, 26};
    protected Material border_mat_1  =Material.CYAN_STAINED_GLASS_PANE;
    protected Material border_mat_2 = Material.CYAN_STAINED_GLASS_PANE;
    protected Material border_mat_3 = Material.CYAN_STAINED_GLASS_PANE;
    protected List<String> border_info_1 = new ArrayList<String>();
    protected List<String> border_info_2 = new ArrayList<String>();
    protected List<String> border_info_3 = new ArrayList<String>();

    private static final int[] border_out = {38, 42, 29, 30, 31, 32, 33};


    public PickThreeContainer(Category category, ItemStack item, final String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

    }
    protected void setupUnit()
    {
        BlockMenuPreset var10001 = new BlockMenuPreset(this.getID(), this.getInventoryTitle()) {
            public void init() {
                PickThreeContainer.this.constructMenu(this);
            }

            public void newInstance(BlockMenu menu, Block b) {
            }

            public boolean canOpen(Block b, Player p) {
                if ((Utilities.hasBuildRights(p, b.getLocation()))) {
                    return true;
                }
                return false;
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow.equals(ItemTransportFlow.INSERT)?PickThreeContainer.this.getInputSlots():PickThreeContainer.this.getOutputSlots();
            }

        };
        registerBlockHandler(getID(), new SlimefunBlockHandler() {
            public void onPlace(Player p, Block b, SlimefunItem item) {
            }

            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                BlockMenu inv = BlockStorage.getInventory(b);
                if(inv != null) {
                    int[] var6 = PickThreeContainer.this.getInputSlots();
                    int var7 = var6.length;

                    int var8;
                    int slot;
                    for(var8 = 0; var8 < var7; ++var8) {
                        slot = var6[var8];
                        if(inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }

                    var6 = PickThreeContainer.this.getOutputSlots();
                    var7 = var6.length;

                    for(var8 = 0; var8 < var7; ++var8) {
                        slot = var6[var8];
                        if(inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }
                }

                PickThreeContainer.progress.remove(b);
                PickThreeContainer.processing.remove(b);
                return true;
            }
        });
        this.registerDefaultRecipes();
    }

    protected void constructMenu(BlockMenuPreset preset) {
        int[] var2 = border;
        int var3 = var2.length;

        int var4;
        int i;
        Material mat;
        String[] lore;
        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE), " ", new String[0]), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }
        var2 = border_in_1;
        var3 = var2.length;
        mat = border_mat_1;
        lore =  border_info_1.toArray(new String[border_info_1.size()]);
        if (border_mat_1 == null)
        {
            mat = Material.CYAN_STAINED_GLASS_PANE;
            preset.addItem(10, new CustomItem(mat, " ", new String[0]), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }
        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            if (border_mat_1 != null) {
                if (var4 == 1) {
                    mat = border_mat_1;
                } else {
                    mat = Material.CYAN_STAINED_GLASS_PANE;
                }
            }
            preset.addItem(i, new CustomItem(mat, ChatColor.GREEN + "This slot needs the following:", lore), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }


        var2 = border_in_2;
        var3 = var2.length;
        mat = border_mat_2;
        lore =  border_info_2.toArray(new String[border_info_2.size()]);
        if (border_mat_2 == null)
        {
            mat = Material.CYAN_STAINED_GLASS_PANE;
            preset.addItem(13, new CustomItem(mat, " ", new String[0]), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }
        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            if (border_mat_2 != null) {
                if (var4 == 1) {
                    mat = border_mat_2;
                } else {
                    mat = Material.CYAN_STAINED_GLASS_PANE;
                }
            }
            preset.addItem(i, new CustomItem(mat, ChatColor.GREEN + "This slot needs the following:", lore), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        var2 = border_in_3;
        var3 = var2.length;
        mat = border_mat_3;
        lore =  border_info_3.toArray(new String[border_info_3.size()]);
        if (border_mat_3 == null)
        {
            mat = Material.CYAN_STAINED_GLASS_PANE;
            preset.addItem(16, new CustomItem(mat, " ", new String[0]), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }
        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            if (border_mat_3 != null) {
                if (var4 == 1) {
                    mat = border_mat_3;
                } else {
                    mat = Material.CYAN_STAINED_GLASS_PANE;
                }
            }
            preset.addItem(i, new CustomItem(mat, ChatColor.GREEN + "This slot needs the following:", lore), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        var2 = border_out;
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.ORANGE_STAINED_GLASS_PANE), " ", new String[0]), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        preset.addItem(31, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " ", new String[0]), new MenuClickHandler() {
            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                return false;
            }
        });
        var2 = this.getOutputSlots();
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
                }
            });
        }

    }

    public abstract String getInventoryTitle();

    public abstract ItemStack getProgressBar();

    public abstract void registerDefaultRecipes();

    public abstract int getEnergyConsumption();

    public abstract int getSpeed();

    public abstract String getMachineIdentifier();

    public int[] getInputSlots() {
        List<Integer> inputSlots = new ArrayList<Integer>();
        if (border_mat_1 != null) inputSlots.add(10);
        if (border_mat_2 != null) inputSlots.add(13);
        if (border_mat_3 != null) inputSlots.add(16);
        int[] ret = new int[inputSlots.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = inputSlots.get(i);
        return ret;
    }

    public int[] getOutputSlots() {
        return new int[]{39, 40, 41};
    }

    public MachineRecipe getProcessing(Block b) {
        return (MachineRecipe)processing.get(b);
    }

    public boolean isProcessing(Block b) {
        return this.getProcessing(b) != null;
    }

    public void registerRecipe(MachineRecipe recipe) {
        recipe.setTicks(recipe.getTicks() / this.getSpeed());
        this.recipes.add(recipe);
    }

    public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
        this.registerRecipe(new MachineRecipe(seconds, input, output));
    }

    private Inventory inject(Block b) {
        int size = BlockStorage.getInventory(b).toInventory().getSize();
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, size);

        for(int i = 0; i < size; ++i) {
            inv.setItem(i, new CustomItem(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
        }

        int[] var8 = this.getOutputSlots();
        int var5 = var8.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            int slot = var8[var6];
            inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
        }

        return inv;
    }

    public boolean fits(Block b, ItemStack[] items) {
        return this.inject(b).addItem(items).isEmpty();
    }

    public void pushItems(Block b, ItemStack[] items) {
        Inventory inv = this.inject(b);
        inv.addItem(items);
        int[] var4 = this.getOutputSlots();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            int slot = var4[var6];
            BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
        }

    }

    public void register(boolean slimefun) {
        this.addItemHandler(new ItemHandler[]{new BlockTicker() {
            public void tick(Block b, SlimefunItem sf, Config data) {
                PickThreeContainer.this.tick(b);
            }

            public void uniqueTick() {
            }

            public boolean isSynchronized() {
                return false;
            }
        }});
        super.register(slimefun);
    }

    protected void tick(Block b) {
        if(this.isProcessing(b)) {
            int r = ((Integer)progress.get(b)).intValue();
            if(r > 0) {
                ItemStack found = this.getProgressBar().clone();
                found.setDurability(MachineHelper.getDurability(found, r, ((MachineRecipe)processing.get(b)).getTicks()));
                ItemMeta im = found.getItemMeta();
                im.setDisplayName(" ");
                ArrayList entry = new ArrayList();
                entry.add(MachineHelper.getProgress(r, ((MachineRecipe)processing.get(b)).getTicks()));
                entry.add("");
                entry.add(MachineHelper.getTimeLeft(r / 2));
                im.setLore(entry);
                found.setItemMeta(im);
                BlockStorage.getInventory(b).replaceExistingItem(31, found);
                if(ChargableBlock.isChargable(b)) {
                    if(ChargableBlock.getCharge(b) < this.getEnergyConsumption()) {
                        return;
                    }

                    ChargableBlock.addCharge(b, -this.getEnergyConsumption());
                    progress.put(b, Integer.valueOf(r - 1));
                } else {
                    progress.put(b, Integer.valueOf(r - 1));
                }
            } else {
                BlockStorage.getInventory(b).replaceExistingItem(31, new CustomItem(new MaterialData(Material.BLUE_STAINED_GLASS_PANE), " ", new String[0]));
                this.pushItems(b, ((MachineRecipe)processing.get(b)).getOutput());
                progress.remove(b);
                processing.remove(b);
            }
        } else {
            MachineRecipe var14 = null;
            HashMap var15 = new HashMap();
            Iterator var16 = this.recipes.iterator();

            while(var16.hasNext()) {
                MachineRecipe var17 = (MachineRecipe)var16.next();
                ItemStack[] var6 = var17.getInput();
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    ItemStack input = var6[var8];
                    int[] var10 = this.getInputSlots();
                    int var11 = var10.length;

                    for(int var12 = 0; var12 < var11; ++var12) {
                        int slot = var10[var12];
                        if(SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), input, true)) {
                            var15.put(Integer.valueOf(slot), Integer.valueOf(input.getAmount()));
                            break;
                        }
                    }
                }

                if(var15.size() == var17.getInput().length) {
                    var14 = var17;
                    break;
                }

                var15.clear();
            }

            if(var14 != null) {
                if(!this.fits(b, var14.getOutput())) {
                    return;
                }

                var16 = var15.entrySet().iterator();

                while(var16.hasNext()) {
                    Entry var18 = (Entry)var16.next();
                    BlockStorage.getInventory(b).replaceExistingItem(((Integer)var18.getKey()).intValue(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(((Integer)var18.getKey()).intValue()), ((Integer)var18.getValue()).intValue()));
                }

                processing.put(b, var14);
                progress.put(b, Integer.valueOf(var14.getTicks()));
            }
        }

    }
}

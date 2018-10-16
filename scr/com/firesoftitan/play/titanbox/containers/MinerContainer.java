//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.firesoftitan.play.titanbox.containers;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
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

public abstract class MinerContainer extends SlimefunItem {
    public static Map<Block, MachineRecipe> processing = new HashMap();
    public static Map<Block, Integer> progress = new HashMap();
    protected List<MachineRecipe> recipes = new ArrayList();
    private static final int[] border = new int[]{4, 13, 22, 31, 40};
    private static final int[] border_in = new int[]{0, 1, 2, 3, 9 ,18 ,19, 20, 21, 12, 27, 28, 29, 30, 36, 45, 46, 47, 48 ,39};
    private static final int[] border_out = new int[]{5, 6, 7, 8 ,14, 23, 32, 41, 50, 51, 52, 53, 44, 35, 26, 17};

    public MinerContainer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
        BlockMenuPreset var10001 = new BlockMenuPreset(id, this.getInventoryTitle()) {
            public void init() {
                MinerContainer.this.constructMenu(this);
            }

            public void newInstance(BlockMenu menu, Block b) {
            }

            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true);
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow.equals(ItemTransportFlow.INSERT) ? MinerContainer.this.getInputSlots() : MinerContainer.this.getOutputSlots();
            }
        };
        registerBlockHandler(id, new SlimefunBlockHandler() {
            public void onPlace(Player p, Block b, SlimefunItem item) {
            }

            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                BlockMenu inv = BlockStorage.getInventory(b);
                if (inv != null) {
                    int[] var6 = MinerContainer.this.getInputSlots();
                    int var7 = var6.length;

                    int var8;
                    int slot;
                    for(var8 = 0; var8 < var7; ++var8) {
                        slot = var6[var8];
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }

                    var6 = MinerContainer.this.getOutputSlots();
                    var7 = var6.length;

                    for(var8 = 0; var8 < var7; ++var8) {
                        slot = var6[var8];
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }
                }

                MinerContainer.progress.remove(b);
                MinerContainer.processing.remove(b);
                return true;
            }
        });
        this.registerDefaultRecipes();
    }

    public MinerContainer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, id, recipeType, recipe, recipeOutput);
        BlockMenuPreset var10001 = new BlockMenuPreset(id, this.getInventoryTitle()) {
            public void init() {
                MinerContainer.this.constructMenu(this);
            }

            public void newInstance(BlockMenu menu, Block b) {
            }

            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true);
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow.equals(ItemTransportFlow.INSERT) ? MinerContainer.this.getInputSlots() : MinerContainer.this.getOutputSlots();
            }
        };
        registerBlockHandler(id, new SlimefunBlockHandler() {
            public void onPlace(Player p, Block b, SlimefunItem item) {
            }

            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                int[] var5 = MinerContainer.this.getInputSlots();
                int var6 = var5.length;

                int var7;
                int slot;
                for(var7 = 0; var7 < var6; ++var7) {
                    slot = var5[var7];
                    if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
                    }
                }

                var5 = MinerContainer.this.getOutputSlots();
                var6 = var5.length;

                for(var7 = 0; var7 < var6; ++var7) {
                    slot = var5[var7];
                    if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
                    }
                }

                MinerContainer.processing.remove(b);
                MinerContainer.progress.remove(b);
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
        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE), " ", new String[0]), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        var2 = border_in;
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            i = var2[var4];
            preset.addItem(i, new CustomItem(new MaterialData(Material.CYAN_STAINED_GLASS_PANE), " ", new String[0]), new MenuClickHandler() {
                public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                    return false;
                }
            });
        }

        preset.addItem(1, new CustomItem(new MaterialData(Material.BROWN_STAINED_GLASS_PANE), "Material Used For Walls", new String[0]), new MenuClickHandler() {
            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                return false;
            }
        });
        preset.addItem(2, new CustomItem(new MaterialData(Material.PURPLE_STAINED_GLASS_PANE), "Drill Rods", new String[0]), new MenuClickHandler() {
            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                return false;
            }
        });
        preset.addItem(28, new CustomItem(new MaterialData(Material.WHITE_STAINED_GLASS_PANE), "Reactor Coolant", new String[0]), new MenuClickHandler() {
            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                return false;
            }
        });
        preset.addItem(29, new CustomItem(new MaterialData(Material.RED_STAINED_GLASS_PANE), "TNT", new String[0]), new MenuClickHandler() {
            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                return false;
            }
        });
        preset.addItem(40, new CustomItem(new MaterialData(Material.GREEN_STAINED_GLASS_PANE), "Pickaxe to Simulate", new String[0]), new MenuClickHandler() {
            public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
                return false;
            }
        });
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

        preset.addItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " ", new String[0]), new MenuClickHandler() {
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
        return new int[]{10, 11, 37, 38, 49};
    }

    public int[] getOutputSlots() {
        return new int[]{15, 16, 24, 25, 33, 34, 42, 43};
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
                MinerContainer.this.tick(b);
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
        if (this.isProcessing(b)) {
            int timeleft = ((Integer)progress.get(b)).intValue();
            if (timeleft > 0) {
                ItemStack item = this.getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, ((MachineRecipe)processing.get(b)).getTicks()));
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList();
                lore.add(MachineHelper.getProgress(timeleft, ((MachineRecipe)processing.get(b)).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);
                BlockStorage.getInventory(b).replaceExistingItem(22, item);
                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < this.getEnergyConsumption()) {
                        return;
                    }

                    ChargableBlock.addCharge(b, -this.getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                } else {
                    progress.put(b, timeleft - 1);
                }
            } else {
                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " ", new String[0]));
                this.pushItems(b, (ItemStack[])((MachineRecipe)processing.get(b)).getOutput().clone());
                progress.remove(b);
                processing.remove(b);
            }
        } else {
            MachineRecipe r = null;
            Map<Integer, Integer> found = new HashMap();
            Iterator var16 = this.recipes.iterator();

            while(var16.hasNext()) {
                MachineRecipe recipe = (MachineRecipe)var16.next();
                ItemStack[] var6 = recipe.getInput();
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    ItemStack input = var6[var8];
                    int[] var10 = this.getInputSlots();
                    int var11 = var10.length;

                    for(int var12 = 0; var12 < var11; ++var12) {
                        int slot = var10[var12];
                        if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), input, true)) {
                            found.put(slot, input.getAmount());
                            break;
                        }
                    }
                }

                if (found.size() == recipe.getInput().length) {
                    r = recipe;
                    break;
                }

                found.clear();
            }

            if (r != null) {
                if (!this.fits(b, r.getOutput())) {
                    return;
                }

                var16 = found.entrySet().iterator();

                while(var16.hasNext()) {
                    Entry<Integer, Integer> entry = (Entry)var16.next();
                    BlockStorage.getInventory(b).replaceExistingItem(((Integer)entry.getKey()).intValue(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(((Integer)entry.getKey()).intValue()), ((Integer)entry.getValue()).intValue()));
                }

                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }

    }
}

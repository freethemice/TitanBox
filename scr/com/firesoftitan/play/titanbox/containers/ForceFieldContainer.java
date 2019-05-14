//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.firesoftitan.play.titanbox.containers;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.PrivacyEnum;
import com.firesoftitan.play.titanbox.enums.RanksEnum;
import com.firesoftitan.play.titanbox.guis.PickAPlayerGUI;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.TitanItemManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.PlayerProtectionManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import com.firesoftitan.play.titanbox.runnables.ChatGetterRunnable;
import com.firesoftitan.play.titanbox.runnables.PickAPlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
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
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.Map.Entry;

public abstract class ForceFieldContainer extends TitanItemManager {
    public static Map<Block, MachineRecipe> processing = new HashMap();
    public static Map<Block, Integer> progress = new HashMap();
    protected List<MachineRecipe> recipes = new ArrayList();
    protected static final int[] friends = {4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 40, 41, 42, 43, 44};
    private static final int[] border = {4, 5, 6, 7, 8, 13, 14, 15, 16, 17, 22, 23, 24, 25, 26, 27, 28, 29, 31, 32, 33, 34, 35, 36, 37, 38, 40, 41, 42, 43, 44};
    private static final int[] border_in = {0, 1, 2, 9, 18, 19, 20, 11};
    private static final int[] border_out = {3, 12, 21, 30, 39};

    public ForceFieldContainer(Category category, ItemStack item, final String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        BlockMenuPreset var10001 = new BlockMenuPreset(name, this.getInventoryTitle()) {
            public void init() {
                ForceFieldContainer.this.constructMenu(this);
            }

            public void newInstance(BlockMenu menu, Block b) {

                    menu.addMenuOpeningHandler(new MenuOpeningHandler() {
                        @Override
                        public void onOpen(Player player) {
                            World world = b.getLocation().getWorld();
                            WorldManager WH = WorldManager.getWorldHolder(world);
                            ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                            int place = 0;
                            if (ffH == null)
                            {
                                player.sendMessage("Your Force Field is Broken =(");
                                return;
                            }
                            for (UUID friend : ffH.getFriends()) {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(friend);
                                try {
                                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta meta = (SkullMeta) skull.getItemMeta();
                                    meta.setOwningPlayer(offlinePlayer);
                                    skull.setItemMeta(meta);
                                    menu.replaceExistingItem(friends[place], new CustomItem(skull.clone(), "&f" + offlinePlayer.getName(), "&fClick to remove this friend"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                menu.addMenuClickHandler(friends[place], new AdvancedMenuClickHandler() {
                                    public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                                        return false;
                                    }

                                    public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {

                                        if (ffH.isFriend(friend)) {
                                            ffH.removeFriend(friend);
                                        }
                                        for (int var4 = 0; var4 < friends.length; ++var4) {
                                            BlockStorage.getInventory(b).replaceExistingItem(friends[var4], new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE), " ", new String[0]));
                                        }
                                        return false;
                                    }
                                });
                                place++;
                            }
                            if (ffH.isAdmin()) {
                                try {
                                    menu.replaceExistingItem(28, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU5NGNhNWRjNWM4NWRiM2I0YTkwZDQ4NTkzMmJlZGU1ZmJkZjQwMjNmYzRmYmZmNmZlMTRiZTQwOWMxZjk3In19fQ=="), "&e> This is a Admin Force Field"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                try {
                                    menu.replaceExistingItem(28, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3OTkxOGU1MmYzZjhmMmNhYmJiZWFjNmE5NzY4MWYyZjhhYTEwYzBiMmU4MTg1OTI4ODVhNGEwZTlkMjI3In19fQ=="), "&e> This is a Normal Force Field"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            menu.replaceExistingItem(29, new CustomItem(new ItemStack(Material.NAME_TAG),  "&fClick To Name Field", "&f" + ffH.getName(), ""));

                            if (ffH.getPrivacy() == PrivacyEnum.FRIENDS) {
                                menu.replaceExistingItem(36, new CustomItem(SlimefunItemsManager.FORCE_FIELD_BLUE.clone(), "&fClick To Change Privacy", "&fFriends: Only Friends", ""));
                            }
                            if (ffH.getPrivacy() == PrivacyEnum.PRIVATE) {
                                menu.replaceExistingItem(36, new CustomItem(SlimefunItemsManager.FORCE_FIELD_GREEN.clone(), "&fClick To Change Privacy", "&fPrivate: Only You", ""));
                            }
                            if (ffH.getPrivacy() == PrivacyEnum.PUBLIC) {
                                menu.replaceExistingItem(36, new CustomItem(SlimefunItemsManager.FORCE_FIELD_YELLOW.clone(), "&fClick To Change Privacy", "&fPublic: Every One", ""));
                            }
                        }
                    });

                try {
                    menu.replaceExistingItem(10, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFjYTg5MWE3MDE1Y2JiYTA2ZTYxYzYwMDg2MTA2OWZhNzg3MGRjZjliMzViNGZlMTU4NTBmNGIyNWIzY2UwIn19fQ=="),  "&fClick Here To Add Friend", "&fThis will add from friends list"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menu.addMenuClickHandler(10, new AdvancedMenuClickHandler() {
                        public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                            return false;
                        }

                        public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {

                            p.closeInventory();
                            World world = b.getLocation().getWorld();
                            WorldManager WH = WorldManager.getWorldHolder(world);
                            ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                            PickAPlayerGUI pickAPlayerGUI = new PickAPlayerGUI(p, new PickAPlayerRunnable() {
                                @Override
                                public void run() {
                                    UUID uuid = this.getPlayerPicked();
                                    if (!ffH.isFriend(uuid)) {
                                        ffH.addFriend(uuid);
                                    }
                                    for (int var4 = 0; var4 < friends.length; ++var4) {
                                        BlockStorage.getInventory(b).replaceExistingItem(friends[var4], new CustomItem(new MaterialData(Material.GRAY_STAINED_GLASS_PANE), " ", new String[0]));
                                    }
                                }
                            });
                            TitanBox.instants.pickPlayer.put(p.getUniqueId(), pickAPlayerGUI);
                            pickAPlayerGUI.buildGUI();
                            pickAPlayerGUI.open();
                            return false;
                        }
                    });

                try {
                    menu.replaceExistingItem(3, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJmYThmMzhjN2IyMjA5NjYxOWMzYTZkNjQ5OGI0MDU1MzBlNDhkNWQ0ZjkxZTJhYWNlYTU3ODg0NGQ1YzY3In19fQ=="),  "&3Maximum Area", "&6Place Upgrade Device Here", "&7This will upgrade max area the force field", "&7can be powered to."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menu.addMenuClickHandler(3, new AdvancedMenuClickHandler() {
                    public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                        return false;
                    }

                    public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                        if (Utilities.isEmpty(e.getCursor())) return false;
                        RanksEnum ranksEnum = RanksEnum.valueOf(p);
                        String name = Utilities.getName(e.getCursor());
                        World world = b.getLocation().getWorld();
                        WorldManager WH = WorldManager.getWorldHolder(world);
                        ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                        Random randomizer = new Random(System.currentTimeMillis());
                        int sizeofUpgrade = randomizer.nextInt(50);
                        sizeofUpgrade = sizeofUpgrade + 5;

                        ForceFieldManager forceFieldManager = WH.canPlaceNewForceField(ffH.getLocation(), ffH, (int) (ffH.getMax() + 5));
                        if (forceFieldManager != null) {
                            p.sendMessage(ChatColor.RED + "You are too close to anthor player to upgrade your force field's max anymore.");
                            p.sendMessage(ChatColor.GRAY + "Located at: " + forceFieldManager.getLocation().getBlockX() + "," + forceFieldManager.getLocation().getBlockY() + "," + forceFieldManager.getLocation().getBlockZ());
                            String oName = Bukkit.getOfflinePlayer( forceFieldManager.getOwner()).getName();
                            if (forceFieldManager.isAdmin()) oName = "Admin";
                            p.sendMessage(ChatColor.GRAY + "Owner: " + oName);
                            return false;
                        }
                        if (ffH.getMax() > ranksEnum.getMax())
                        {
                            p.sendMessage("Your efficiency is at max, move up in rank to increase!");
                            return false;
                        }
                        if (TitanBox.isUpgradeDevice(p, e.getCursor())) {
                            e.setCursor(InvUtils.decreaseItem(e.getCursor(), 1));
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (offlinePlayer != null) {
                                UUID uuid = offlinePlayer.getUniqueId();
                                if (uuid != null) {
                                    e.setCursor(InvUtils.decreaseItem(e.getCursor(), 1));
                                    ffH.setMax(ffH.getMax() + sizeofUpgrade);
                                    p.sendMessage("Your Force Field's max area was upgrade by " + sizeofUpgrade);
                                    p.closeInventory();
                                }
                            }
                        }

                        return false;
                    }
                });
                try {
                    menu.replaceExistingItem(12, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzYmNhZjZkMjY3OWQ4ZDdkOWJmNmE0NzRhNDhhNzdhOGU5MTc0N2ExMDg0YzA5MjU2ZWJjODZjYjc0ODExIn19fQ=="),  "&3Efficiency", "&6Place Upgrade Device Here", "&7This will upgrade efficiency of the force field", "&7So it will take less power to increase size."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menu.addMenuClickHandler(12, new AdvancedMenuClickHandler() {
                    public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                        return false;
                    }

                    public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                        if (Utilities.isEmpty(e.getCursor())) return false;
                        RanksEnum ranksEnum = RanksEnum.valueOf(p);
                        String name = Utilities.getName(e.getCursor());
                        World world = b.getLocation().getWorld();
                        WorldManager WH = WorldManager.getWorldHolder(world);
                        ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                        if (ffH.getEfficiency() > ranksEnum.getEfficiency())
                        {
                            p.sendMessage("Your efficiency is at max, move up in rank to increase!");
                            return false;
                        }
                        if (TitanBox.isUpgradeDevice(p, e.getCursor())) {
                            e.setCursor(InvUtils.decreaseItem(e.getCursor(), 1));
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (offlinePlayer != null) {
                                UUID uuid = offlinePlayer.getUniqueId();
                                if (uuid != null) {
                                    e.setCursor(InvUtils.decreaseItem(e.getCursor(), 1));
                                    Random randomizer = new Random(System.currentTimeMillis());
                                    int sizeofUpgrade = randomizer.nextInt(5);
                                    sizeofUpgrade = sizeofUpgrade + 1;
                                    ffH.setEfficiency(ffH.getEfficiency() + sizeofUpgrade);
                                    p.sendMessage("Your Force Field's efficiency was upgrade by " + sizeofUpgrade);
                                    p.closeInventory();
                                }
                            }
                        }

                        return false;
                    }
                });
                try {
                    menu.replaceExistingItem(21, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRkODQ0ZmVlMjRkNWYyN2RkYjY2OTQzODUyOGQ4M2I2ODRkOTAxYjc1YTY4ODlmZTc0ODhkZmM0Y2Y3YTFjIn19fQ=="),  "&3Minimum Area", "&6Place Upgrade Device Here", "&7This will upgrade min area", "&7the force field can go down to."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menu.addMenuClickHandler(21, new AdvancedMenuClickHandler() {
                    public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                        return false;
                    }

                    public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                        if (Utilities.isEmpty(e.getCursor())) return false;
                        RanksEnum ranksEnum = RanksEnum.valueOf(p);
                        String name = Utilities.getName(e.getCursor());
                        World world = b.getLocation().getWorld();
                        WorldManager WH = WorldManager.getWorldHolder(world);
                        ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                        if (ffH.getMin() > ffH.getMax() - (ffH.getMax()/4) || ffH.getMin() > ranksEnum.getMin())
                        {
                            p.sendMessage("Your Force Field's min is at max, Upgrade Force Field's max to increase min more, or move up in rank to increase!");
                            return false;
                        }
                        ForceFieldManager forceFieldManager = WH.canPlaceNewForceField(ffH.getLocation(), ffH, (int) (ffH.getMin() + 5));
                        if (forceFieldManager != null) {
                            p.sendMessage(ChatColor.RED + "You are too close to anthor player to upgrade your force field's min anymore.");
                            p.sendMessage(ChatColor.GRAY + "Located at: " + forceFieldManager.getLocation().getBlockX() + "," + forceFieldManager.getLocation().getBlockY() + "," + forceFieldManager.getLocation().getBlockZ());
                            String oName = Bukkit.getOfflinePlayer( forceFieldManager.getOwner()).getName();
                            if (forceFieldManager.isAdmin()) oName = "Admin";
                            p.sendMessage(ChatColor.GRAY + "Owner: " + oName);
                            return false;
                        }
                        if (TitanBox.isUpgradeDevice(p, e.getCursor())) {
                            e.setCursor(InvUtils.decreaseItem(e.getCursor(), 1));
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (offlinePlayer != null) {
                                UUID uuid = offlinePlayer.getUniqueId();
                                if (uuid != null) {
                                    e.setCursor(InvUtils.decreaseItem(e.getCursor(), 1));
                                    Random randomizer = new Random(System.currentTimeMillis());
                                    int sizeofUpgrade = randomizer.nextInt(4);
                                    sizeofUpgrade = sizeofUpgrade + 1;
                                    ffH.setMin(ffH.getMin() + sizeofUpgrade);;
                                    p.sendMessage("Your Force Field's min was upgrade by " + sizeofUpgrade);
                                    p.closeInventory();
                                }
                            }
                        }

                        return false;
                    }
                });
                    menu.addMenuClickHandler(28, new MenuClickHandler() {

                        @Override
                        public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                            World world = b.getLocation().getWorld();
                            WorldManager WH = WorldManager.getWorldHolder(world);
                            ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                            if (!ffH.isAdmin()) {
                                if (p.hasPermission("titanbox.admin")) {
                                    ffH.setAdmin(true);
                                    try {
                                        menu.replaceExistingItem(28, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU5NGNhNWRjNWM4NWRiM2I0YTkwZDQ4NTkzMmJlZGU1ZmJkZjQwMjNmYzRmYmZmNmZlMTRiZTQwOWMxZjk3In19fQ=="), "&e> This is a Admin Force Field"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    p.sendMessage(ChatColor.RED + "This is now an Admin Force Field.");
                                    PlayerProtectionManager ppH = PlayerProtectionManager.getPlayer(ffH.getOwner());
                                    if (ppH != null)
                                    {
                                        ppH.remove(ffH.getLocation());
                                    }
                                    TitanBox.instants.adminList.add(ffH);

                                }
                            }
                            else
                            {
                                if (p.hasPermission("titanbox.admin")) {
                                    ffH.setAdmin(false);
                                    try {
                                        menu.replaceExistingItem(28, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY3OTkxOGU1MmYzZjhmMmNhYmJiZWFjNmE5NzY4MWYyZjhhYTEwYzBiMmU4MTg1OTI4ODVhNGEwZTlkMjI3In19fQ=="), "&e> This is a Normal Force Field"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    p.sendMessage(ChatColor.RED + "This is now an Normal Force Field.");
                                    PlayerProtectionManager ppH = PlayerProtectionManager.getPlayer(ffH.getOwner());
                                    if (ppH != null)
                                    {
                                        ppH.add(ffH);
                                    }
                                    TitanBox.instants.adminList.remove(ffH);
                                }
                            }
                            return false;
                        }
                    });
                menu.addMenuClickHandler(27, new MenuClickHandler() {

                    @Override
                    public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                        if (p.hasPermission("titanbox.admin"))
                        {
                            p.closeInventory();
                            new BukkitRunnable()
                            {

                                @Override
                                public void run() {
                                    TitanBox.instants.getNextMessage(p, new ChatGetterRunnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                int I = Integer.parseInt(this.getChat());
                                                World world = b.getLocation().getWorld();
                                                WorldManager WH = WorldManager.getWorldHolder(world);
                                                ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                                                ffH.setMin(I);
                                                ffH.setMax(I);
                                                ffH.setSize(I);

                                            } catch (Exception e) {

                                            }
                                        }
                                    }, "Enter Size Of Field");
                                }
                                }.runTaskLater(TitanBox.instants, 5);
                        }
                        return false;
                    }
                });
                menu.addMenuClickHandler(29, new MenuClickHandler() {

                    @Override
                    public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {

                        p.closeInventory();
                        new BukkitRunnable()
                        {

                            @Override
                            public void run() {
                                TitanBox.instants.getNextMessage(p, new ChatGetterRunnable() {
                                    @Override
                                    public void run() {
                                        try
                                        {
                                            World world = b.getLocation().getWorld();
                                            WorldManager WH = WorldManager.getWorldHolder(world);
                                            ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                                            ffH.setName(this.getChat());

                                        }
                                        catch (Exception e)
                                        {

                                        }
                                    }
                                }, "Enter Name");
                            }
                        }.runTaskLater(TitanBox.instants, 5);

                        return false;
                    }
                });
                menu.addMenuClickHandler(36, new MenuClickHandler() {

                    @Override
                    public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
                        try
                        {
                            World world = b.getLocation().getWorld();
                            WorldManager WH = WorldManager.getWorldHolder(world);
                            ForceFieldManager ffH = WH.getFieldAt(b.getLocation());
                            int current = ffH.getPrivacy().getValue();
                            current++;
                            if (current >= PrivacyEnum.values().length)
                            {
                                current = 0;
                            }
                            ffH.setPrivacy(PrivacyEnum.valueOf(current));
                            if (ffH.getPrivacy() == PrivacyEnum.FRIENDS) {
                                menu.replaceExistingItem(36, new CustomItem(SlimefunItemsManager.FORCE_FIELD_BLUE.clone(), "&fClick To Change Privacy", "&fFriends: Only Friends", ""));
                            }
                            if (ffH.getPrivacy() == PrivacyEnum.PRIVATE) {
                                menu.replaceExistingItem(36, new CustomItem(SlimefunItemsManager.FORCE_FIELD_GREEN.clone(), "&fClick To Change Privacy", "&fPrivate: Only You", ""));
                            }
                            if (ffH.getPrivacy() == PrivacyEnum.PUBLIC) {
                                menu.replaceExistingItem(36, new CustomItem(SlimefunItemsManager.FORCE_FIELD_YELLOW.clone(), "&fClick To Change Privacy", "&fPublic: Every One", ""));
                            }

                        }
                        catch (Exception e)
                        {

                        }

                        return false;
                    }
                });
            }


            public boolean canOpen(Block b, Player p) {
                if ((Utilities.hasBuildRights(p, b.getLocation()))) {
                    return true;
                }
                return false;
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow.equals(ItemTransportFlow.INSERT)?ForceFieldContainer.this.getInputSlots():ForceFieldContainer.this.getOutputSlots();
            }

        };
        registerBlockHandler(name, new SlimefunBlockHandler() {
            public void onPlace(Player p, Block b, SlimefunItem item) {
            }

            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                BlockMenu inv = BlockStorage.getInventory(b);
                if(inv != null) {
                    int[] var6 = ForceFieldContainer.this.getInputSlots();
                    int var7 = var6.length;

                    int var8;
                    int slot;
                    for(var8 = 0; var8 < var7; ++var8) {
                        slot = var6[var8];
                        if(inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }

                    var6 = ForceFieldContainer.this.getOutputSlots();
                    var7 = var6.length;

                    for(var8 = 0; var8 < var7; ++var8) {
                        slot = var6[var8];
                        if(inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        }
                    }
                }

                ForceFieldContainer.progress.remove(b);
                ForceFieldContainer.processing.remove(b);
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
        return new int[]{};
    }

    public int[] getOutputSlots() {
        return new int[]{};
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
                ForceFieldContainer.this.tick(b);
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
                BlockStorage.getInventory(b).replaceExistingItem(22, found);
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
                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLUE_STAINED_GLASS_PANE), " ", new String[0]));
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

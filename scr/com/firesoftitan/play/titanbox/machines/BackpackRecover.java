package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ItemEnum;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BackpackRecover {
    public static Config recovers = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "recovers.yml");

    public  BackpackRecover()
    {


    }
    public static ItemStack getMeAsDrop()
    {
        ItemStack BackpackRecover = Utilities.getSkull(ItemEnum.BACKPACK.getTexute());
        BackpackRecover = Utilities.changeName(BackpackRecover, ChatColor.YELLOW + "Backpack Recover");
        BackpackRecover = Utilities.addLore(BackpackRecover, ChatColor.WHITE + "Recovers Slimefun Backpacks", ChatColor.WHITE + "Must have new backpack with same slots", ChatColor.WHITE + "in your inventory");

        return BackpackRecover;
    }
    public static void openGui(Player player)
    {
        Inventory recover = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Backpack Recover");
        ItemStack[] PacksList = getBackpackList(player.getUniqueId());
        if (PacksList != null) {
            for (int i = 0; i < PacksList.length; i++) {
                recover.setItem(i, PacksList[i].clone());
            }

            player.openInventory(recover);
        }else
        {
            player.sendMessage(ChatColor.RED + "You have no lost backpacks.");
        }

    }
    public static ItemStack[] getBackpackList(UUID uuid)
    {
        try {
            File yml = new File("data-storage/Slimefun/Players/" + uuid.toString() + ".yml");
            if (yml.exists()) {
                Config cfg = new Config(yml);
                Set<String> keys = cfg.getKeys("backpacks");
                ItemStack[] list = new ItemStack[keys.size()];
                int i = 0;
                for (String key : keys) {
                    int size = cfg.getInt("backpacks." + key + ".size");
                    ItemStack backpack = null;
                    switch (size) {
                        case 9:
                            backpack = SlimefunItems.BACKPACK_SMALL.clone();
                            break;
                        case 18:
                            backpack = SlimefunItems.BACKPACK_MEDIUM.clone();
                            break;
                        case 27:
                            backpack = SlimefunItems.BACKPACK_LARGE.clone();
                            break;
                        case 36:
                            backpack = SlimefunItems.WOVEN_BACKPACK.clone();
                            break;
                        case 45:
                            backpack = SlimefunItems.GILDED_BACKPACK.clone();
                            break;
                    }
                    if (backpack != null) {
                        for (int line = 0; line < backpack.getItemMeta().getLore().size(); line++) {
                            if (backpack.getItemMeta().getLore().get(line).equals(ChatColor.translateAlternateColorCodes('&', "&7ID: <ID>"))) {
                                ItemMeta im = backpack.getItemMeta();
                                List<String> lore = im.getLore();
                                lore.set(line, lore.get(line).replace("<ID>", uuid.toString() + "#" + key));
                                im.setLore(lore);
                                backpack.setItemMeta(im);
                                break;
                            }
                        }
                        list[i] = backpack.clone();
                        i++;
                    }
                }
                return list;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    public static int getBackPackSize(ItemStack item)
    {
        try {
            if (SlimefunItems.BACKPACK_SMALL.clone().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                return 9;
            }
            if (SlimefunItems.BACKPACK_MEDIUM.clone().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                return 18;
            }
            if (SlimefunItems.BACKPACK_LARGE.clone().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                return 27;
            }
            if (SlimefunItems.WOVEN_BACKPACK.clone().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                return 36;
            }
            if (SlimefunItems.GILDED_BACKPACK.clone().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                return 45;
            }
            if (SlimefunItems.BOUND_BACKPACK.clone().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                return 36;
            }
            if (SlimefunItems.COOLER.clone().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                return 27;
            }
            return -1;
        }
        catch (Exception e)
        {
            return  -1;
        }
    }
    public static int getBackPackId(ItemStack item)
    {
        try {
            for (String line : item.getItemMeta().getLore()) {
                if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&7ID: ")) && line.contains("#")) {
                    try {
                        return Integer.parseInt(line.split("#")[1]);
                    } catch (NumberFormatException x) {
                        return -1;
                    }
                }
            }
            return -1;
        }
        catch (Exception e)
        {
            return  -1;
        }
    }
    public static void onInventoryClickEvent(InventoryClickEvent event) {

        String name = event.getView().getTitle();
        if (name.equals(ChatColor.YELLOW + "Backpack Recover"))
        {
            event.setCancelled(true);
            if (event.getRawSlot() > -1 && event.getRawSlot() < 54)
            {
                ItemStack item = event.getClickedInventory().getItem(event.getRawSlot());
                if (!Utilities.isEmpty(item))
                {
                    int getsize = getBackPackSize(item);
                    for (int i = 0; i < 36; i++)
                    {
                        ItemStack check = event.getWhoClicked().getInventory().getItem(i);
                        int Size = getBackPackSize(check);
                        if (Size > -1)
                        {
                            if (getsize == Size) {
                                int id = getBackPackId(item);
                                for (int line = 0; line < check.getItemMeta().getLore().size(); line++) {
                                    if (check.getItemMeta().getLore().get(line).contains(ChatColor.translateAlternateColorCodes('&', "&7ID:"))) {
                                        ItemMeta im = check.getItemMeta();
                                        List<String> lore = im.getLore();
                                        lore.set(line, lore.get(line).replace(lore.get(line), ChatColor.GRAY + "ID: " + event.getWhoClicked().getUniqueId().toString() + "#" + id));
                                        im.setLore(lore);
                                        ItemStack check2 = check.clone();
                                        check2.setAmount(1);
                                        check2.setItemMeta(im);
                                        if (check.getAmount() < 2) {
                                            event.getWhoClicked().getInventory().setItem(i, null);
                                        } else {
                                            check.setAmount(check.getAmount() - 1);
                                            event.getWhoClicked().getInventory().setItem(i, check);
                                        }
                                        event.getWhoClicked().getInventory().addItem(check2.clone());
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    event.getWhoClicked().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Can't find new backpack thats the right size to copy to. ");
                    return;
                }
            }
        }

    }
    public static boolean onBlockRemoveEvent(Block block)
    {
        String key = getLocationKey(block.getLocation());
        if (key != null) {
            if (recovers.contains("recovers." + key)) {
                return true;
            }
        }
        return false;
    }
    public static void onBlockBreakEvent(BlockBreakEvent event)
    {
        Block Broken = event.getBlock();
        if ((Utilities.hasBuildRights(event.getPlayer(), Broken.getLocation()) ) ) {
            String key = getLocationKey(Broken.getLocation());
            if (key != null) {
                if (recovers.contains("recovers."  + key)) {
                    recovers.setValue(key, null);
                    //event.setDropItems(false);
                    //ItemStack recover = BackpackRecover.getMeAsDrop();
                    //event.getPlayer().getInventory().addItem(recover);
                    event.getPlayer().closeInventory();
                }
            }
        }
    }
    public static String getLocationKey(Location loca)
    {
        if (loca != null)
        {
            return  loca.getWorld().getName() + "_" + loca.getBlockX() + "_" + loca.getBlockY() + "_" + loca.getBlockZ();
        }
        return null;
    }
    public static void saveRecovers()
    {
        recovers.save();
    }
    public static void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                Block Broken = event.getClickedBlock();
                if ((Utilities.hasBuildRights(event.getPlayer(), Broken.getLocation(), true))) {
                    String key = getLocationKey(Broken.getLocation());
                    if (key != null) {
                        if (recovers.contains("recovers." + key)) {
                            openGui(event.getPlayer());
                        }
                    }
                }
            }
        }
    }
    public static boolean onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Backpack Recover")) {
                        Block Placed = event.getBlockPlaced();
                        if ((Utilities.hasBuildRights(event.getPlayer(), Placed.getLocation())) ) {
                            String key = getLocationKey(Placed.getLocation());
                            if (key != null) {
                                recovers.setValue("recovers." + key, true);
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

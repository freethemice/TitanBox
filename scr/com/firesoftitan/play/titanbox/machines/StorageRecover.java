package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ItemEnum;
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

public class StorageRecover {

    public StorageRecover()
    {


    }
    public static ItemStack getMeAsDrop()
    {
        ItemStack BackpackRecover = Utilities.getSkull(ItemEnum.STORAGE.getTexute());
        BackpackRecover = Utilities.changeName(BackpackRecover, ChatColor.YELLOW + "Storage Recover");
        BackpackRecover = Utilities.addLore(BackpackRecover, ChatColor.WHITE + "Recovers TitanBox Storage Units", ChatColor.WHITE + "Must have new Storage with same size or greater.", ChatColor.WHITE + "in your inventory");

        return BackpackRecover;
    }
    public static void openGui(Player player)
    {
        Utilities.scanInventory(player);
        Inventory guiRecover = Bukkit.createInventory(null, 54, "TitanBox: Recover for " + player.getName());
        for(StorageUnit tmp: StorageUnit.StorageById.values())
        {
            if (tmp.getOwner().toString().equals(player.getUniqueId().toString()))
            {
                if (tmp.getLocation() == null)
                {
                    guiRecover.addItem(tmp.getMeAsDrop());
                }
                else
                {
                    Utilities.placeSkull(tmp.getLocation().getBlock(), tmp.getMe().getTexute());
                }
            }
        }
        player.openInventory(guiRecover);

    }
    public static void onInventoryClickEvent(InventoryClickEvent event) {

        if (event.getView().getTitle().startsWith("TitanBox: Recover for "))
        {
            event.setCancelled(true);

            ItemStack item = event.getInventory().getItem(event.getRawSlot());
            if (!Utilities.isEmpty(item))
            {
                String id = ChatColor.stripColor(item.getItemMeta().getLore().get(0));
                StorageUnit tmp = null;
                if (StorageUnit.StorageById.containsKey(id)) {
                    tmp = StorageUnit.StorageById.get(id);
                }
                if (tmp !=null) {
                    event.getWhoClicked().getInventory().addItem(item.clone());
                }
            }


        }
    }
    public static boolean onBlockRemoveEvent(Block block)
    {
        String key = getLocationKey(block.getLocation());
        if (key != null) {
            if (BackpackRecover.recovers.contains("recovers.storage."  + key)) {
                return true;
            }
        }
        return false;
    }
    public static void onBlockBreakEvent(BlockBreakEvent event)
    {
        Block Broken = event.getBlock();
        if ((Utilities.hasBuildRights(event.getPlayer(),  Broken.getLocation())) ) {
            String key = getLocationKey(Broken.getLocation());
            if (key != null) {
                if (BackpackRecover.recovers.contains("recovers.storage."  + key)) {
                    BackpackRecover.recovers.setValue(key, null);
                    //event.setDropItems(false);
                    //ItemStack recover = StorageRecover.getMeAsDrop();
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
    public static void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                Block Broken = event.getClickedBlock();
                if ((Utilities.hasBuildRights(event.getPlayer(), Broken.getLocation(), true)) ) {
                    String key = getLocationKey(Broken.getLocation());
                    if (key != null) {
                        if (BackpackRecover.recovers.contains("recovers.storage." + key)) {
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
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Storage Recover")) {
                        Block Placed = event.getBlockPlaced();
                        if ((Utilities.hasBuildRights(event.getPlayer(), Placed.getLocation())) ) {
                            String key = getLocationKey(Placed.getLocation());
                            if (key != null) {
                                BackpackRecover.recovers.setValue("recovers.storage." + key, true);
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

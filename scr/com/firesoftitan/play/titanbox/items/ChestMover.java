package com.firesoftitan.play.titanbox.items;

import com.firesoftitan.play.titanbox.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChestMover {

    private HashMap<UUID, List<ItemStack>> inventoryHolder;
    public ChestMover()
    {
        inventoryHolder = new HashMap<UUID, List<ItemStack>>();

    }
    public boolean hasSaved(Player player)
    {
        return inventoryHolder.containsKey(player.getUniqueId());
    }
    public void removeInventory(Player player, Block block)
    {
        if (Utilities.hasBuildRights(player, block.getLocation())) {
            Inventory inventory = Utilities.getVanillaInventoryFor(block);
            if (inventory != null) {
                List<ItemStack> tmp = new ArrayList<ItemStack>();
                if (inventoryHolder.containsKey(player.getUniqueId())) {
                    tmp = inventoryHolder.get(player.getUniqueId());
                }
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack item = null;
                    if (inventory.getItem(i) != null) {
                        item = inventory.getItem(i).clone();
                        tmp.add(item);
                    }
                }
                if (tmp.size() > 0) {
                    inventory.clear();
                    inventoryHolder.put(player.getUniqueId(), tmp);
                    player.sendMessage(ChatColor.GREEN + "Storage saved.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Nothing was added.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "No Storage found.");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Access Denied To Force Field Area!!");
            return;
        }
    }
    public void placeInventory(Player player, Block block)
    {
        if (Utilities.hasBuildRights(player, block.getLocation())) {
            Inventory inventory = Utilities.getVanillaInventoryFor(block);
            if (inventory != null) {
                List<ItemStack> tmp = null;
                if (inventoryHolder.containsKey(player.getUniqueId())) {
                    tmp = inventoryHolder.get(player.getUniqueId());
                }
                if (tmp != null) {
                    int t = 0;
                    while (tmp.size() > 0) {
                        ItemStack item = tmp.get(0).clone();
                        inventory.addItem(item);
                        tmp.remove(0);
                        if (inventory.firstEmpty() < 0) {
                            break;
                        }
                    }
                    if (tmp.size() == 0) {
                        inventoryHolder.remove(player.getUniqueId());
                        player.sendMessage(ChatColor.GREEN + "Store Mover now empty.");
                    } else {
                        inventoryHolder.put(player.getUniqueId(), tmp);
                        player.sendMessage(ChatColor.GREEN + "Store Mover has " + tmp.size() + " more items.");
                    }
                }
            } else {
                List<ItemStack> tmp = null;
                if (inventoryHolder.containsKey(player.getUniqueId())) {
                    tmp = inventoryHolder.get(player.getUniqueId());
                }
                if (tmp == null) {
                    player.sendMessage(ChatColor.RED + "No Storage found.");
                } else {
                    player.sendMessage(ChatColor.RED + "You have " + tmp.size() + " items stored.");
                }
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Access Denied To Force Field Area!!");
            return;
        }
    }
}

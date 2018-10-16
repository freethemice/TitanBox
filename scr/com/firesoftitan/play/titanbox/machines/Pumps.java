package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.holders.ItemHolder;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Pumps {

    public static Config pumps = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "pumps.yml");

    public Pumps()
    {

    }

    private static Block findTopLiquidBlock(Block clicked, Material liquid)
    {

        if (liquid == Material.WATER || liquid == Material.LAVA) {
            int topMostWaterAt = -1;
            for (int y = 0; y < 10; y++) {
                Location soUp = clicked.getLocation().clone();
                soUp = soUp.add(0, y, 0).clone();
                Block searching = soUp.getBlock();
                if (searching.getType() == liquid) {
                    topMostWaterAt = y;
                } else {
                    if (topMostWaterAt > -1) {
                        return clicked.getLocation().add(0, topMostWaterAt, 0).getBlock();
                    }
                }
            }
        }
        else
        {
            return clicked.getLocation().add(0, -1, 0).getBlock();
        }
        return null;
    }
    public static String getPumpType(Location loc)
    {
        String locationKey = loc.toString().replace("\\.", "+");
        if (pumps.contains("pumps." + locationKey)) {
            return pumps.getString("pumps." + locationKey);
        }
        return null;
    }
    public static Boolean getLiquid(Location loc, String Type)
    {
        if (getPumpType(loc) == null)
        {
            return false;
        }
        if (loc.getBlock().getType() == Material.PLAYER_WALL_HEAD)
        {
            Block block = loc.getBlock();
            String texture ="";
            try {
                texture = CustomSkull.getTexture(block);
            } catch (Exception e) {

            }
            block.setType(Material.PLAYER_HEAD);
            try {
                CustomSkull.setSkull(block, texture);
            } catch (Exception e) {

            }
        }
        if (loc.getBlock().getType() != Material.PLAYER_HEAD)
        {
            return false;
        }
        Material typeOfMat = Material.WATER;
        if (Type.equals("Lava"))
        {
            typeOfMat = Material.LAVA;
        }
        if (Type.equals("Ice"))
        {
            typeOfMat = Material.ICE;
        }
        if (Type.equals("Packed Ice"))
        {
            typeOfMat = Material.PACKED_ICE;
        }
        if (Type.equals("Item"))
        {
            typeOfMat = Material.AIR;
            return true;
        }

        if (Type.equals("Killer"))
        {
            typeOfMat = Material.AIR;
            return true;
        }
        String locationKey = loc.toString().replace("\\.", "+");
        if (pumps.contains("pumps." + locationKey)) {
            if (loc.clone().add(0, -1, 0).getBlock().getType() == typeOfMat) {
                return true;
            }
        }
        return false;
    }
    public static void savePumps()
    {
        pumps.save();
    }
    @EventHandler
    public static void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.hasItemMeta())
        {
            if (mainHand.getItemMeta().hasDisplayName())
            {


                String typeOf = "Water";
                ItemStack check = getMeAsDrop(typeOf);
                if (TitanBox.isItemEqual(mainHand, check)) {
                    checkPumpPlacement(event, typeOf);
                }

                typeOf = "Lava";
                check = getMeAsDrop(typeOf);
                if (TitanBox.isItemEqual(mainHand, check)) {
                    checkPumpPlacement(event, typeOf);
                }

                typeOf = "Ice";
                check = getMeAsDrop(typeOf);
                if (TitanBox.isItemEqual(mainHand, check)) {
                    checkPumpPlacement(event, typeOf);
                }

                typeOf = "Item";
                check = getMeAsDrop(typeOf);
                if (TitanBox.isItemEqual(mainHand, check)) {
                    checkPumpPlacement(event, typeOf);
                }

                typeOf = "Killer";
                check = getMeAsDrop(typeOf);
                if (TitanBox.isItemEqual(mainHand, check)) {
                    checkPumpPlacement(event, typeOf);
                }
            }
        }
    }

    public static void checkPumpPlacement(BlockPlaceEvent event, String typeOf) {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Material typeOfMat = Material.WATER;
        String typeOfPump = ItemHolder.WATERPUMP.getTexute();
        if (typeOf.equals("Lava"))
        {
            typeOfMat = Material.LAVA;
            typeOfPump = ItemHolder.LAVAPUMP.getTexute();
        }
        if (typeOf.equals("Ice"))
        {
            typeOfMat = Material.ICE;
            typeOfPump = ItemHolder.ICEPUMP.getTexute();
        }
        if (typeOf.equals("Item"))
        {
            typeOfMat = Material.AIR;
            typeOfPump = ItemHolder.ITEMPUMP.getTexute();
        }

        if (typeOf.equals("Killer"))
        {
            typeOfMat = Material.AIR;
            typeOfPump = ItemHolder.KILLERPUMP.getTexute();
        }
        event.setCancelled(true);
        Material finalTypeOfMat = typeOfMat;
        String finalTypeOfPump = typeOfPump;
        Bukkit.getScheduler().scheduleSyncDelayedTask(TitanBox.instants, new Runnable() {
            @Override
            public void run() {
                Block waterBlock = findTopLiquidBlock(event.getBlock(), finalTypeOfMat);
                if (waterBlock != null) {
                    if (waterBlock.getType() == finalTypeOfMat ||  finalTypeOfMat == Material.AIR) {
                        Block above = waterBlock.getLocation().add(0, 1, 0).getBlock();
                        if (above.getType() == Material.AIR) {
                            TitanBox.placeSkull(above, finalTypeOfPump);
                            String locationKey = above.getLocation().toString().replace("\\.", "+");
                            pumps.setValue("pumps." + locationKey, typeOf);
                            if (mainHand.getAmount() < 2)
                            {
                                player.getInventory().setItemInMainHand(null);
                            }
                            else
                            {
                                ItemStack nextHand = mainHand.clone();
                                nextHand.setAmount(mainHand.getAmount() - 1);
                                player.getInventory().setItemInMainHand(nextHand.clone());

                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: There is something over the water source.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: Have to place over " + typeOf.toLowerCase() + ", Click on the Source " + typeOf + " Block to place above");
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: Have to place over " + typeOf.toLowerCase() + ", Click on the Source " + typeOf + " Block to place above");;
                }
            }
        }, 2);


    }
    public static boolean onBlockRemoveEvent(Block block)
    {
        String locationKey = block.getLocation().toString().replace("\\.", "+");
        if (pumps.contains("pumps." + locationKey)) {
            return true;
        }
        return false;
    }
    @EventHandler
    public static void onBlockBreakEvent(BlockBreakEvent event)
    {
        if ((GriefPrevention.instance.allowBreak(event.getPlayer(), event.getBlock(), event.getBlock().getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
            String locationKey = event.getBlock().getLocation().toString().replace("\\.", "+");
            if (pumps.contains("pumps." + locationKey)) {

                String whatType = pumps.getString("pumps." + locationKey);
                if (whatType != null) {
                    ItemStack placeMe = getMeAsDrop(whatType);
                    pumps.setValue("pumps." + locationKey, null);

                    if (placeMe != null) {
                        event.setDropItems(false);
                        event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), placeMe);
                        event.getPlayer().closeInventory();
                    }
                }
            }
        }
    }
    public static ItemStack getMeAsDrop(String whatType)
    {
        List<String> lore = new ArrayList<String>();
        ItemHolder me = null;
        String name = "";
        if (whatType.equalsIgnoreCase("water")) {
            me = ItemHolder.WATERPUMP;
            name = ChatColor.YELLOW + "Water" + " Pump";
            lore.add(ChatColor.WHITE + "Click On Water Source To Place");
            lore.add(ChatColor.WHITE + "Links with Modules in the I.R.R.");

        }
        if (whatType.equalsIgnoreCase("lava"))
        {
            me = ItemHolder.LAVAPUMP;
            name = ChatColor.YELLOW + "Lava" + " Pump";
            lore.clear();
            lore.add(ChatColor.WHITE + "Click On Lava Source To Place");
            lore.add(ChatColor.WHITE + "Links with Modules in the I.R.R.");
        }
        if (whatType.equalsIgnoreCase("ice"))
        {
            me = ItemHolder.ICEPUMP;
            name = ChatColor.YELLOW + "Ice" + " Extractor";
            lore.clear();
            lore.add(ChatColor.WHITE + "Click On Ice To Place");
            lore.add(ChatColor.WHITE + "Links with Modules in the I.R.R.");
        }
        if (whatType.equalsIgnoreCase("item"))
        {
            me = ItemHolder.ITEMPUMP;
            name = ChatColor.YELLOW + "Item Sucker";
            lore.clear();
            lore.add(ChatColor.WHITE + "Place anywhere will suck up item near it.");
            lore.add(ChatColor.WHITE + "Links with Modules in the I.R.R.");
        }
        if (whatType.equalsIgnoreCase("killer"))
        {
            me = ItemHolder.KILLERPUMP;
            name = ChatColor.YELLOW + "Killer Block";
            lore.clear();
            lore.add(ChatColor.WHITE + "Place anywhere will kill mobs near it.");
            lore.add(ChatColor.WHITE + "Links with Modules in the I.R.R.");
        }
        if (me != null) {
            ItemStack placeMe = me.getItem();
            placeMe = TitanBox.changeName(placeMe, name);
            placeMe = TitanBox.addLore(placeMe,  lore);
            return placeMe.clone();
        }
        return null;
    }
}

package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.holders.ItemHolder;
import com.firesoftitan.play.titanbox.runnables.NetworkRunnable;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
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

import java.util.HashSet;
import java.util.Set;

public class NetworkMonitor {

    public NetworkMonitor()
    {


    }
    public static ItemStack getMeAsDrop()
    {
        ItemStack BackpackRecover = TitanBox.getSkull(ItemHolder.NETWORK.getTexute());
        BackpackRecover = TitanBox.changeName(BackpackRecover, ChatColor.YELLOW + "Network Monitor");
        BackpackRecover = TitanBox.addLore(BackpackRecover, ChatColor.WHITE + "Show you detail status of your Energy Network", ChatColor.WHITE + "Must be place 1 block above energy regulator.");

        return BackpackRecover;
    }
    public static void openGui(Player player, Location loc)
    {
        loc = loc.add(0, -1, 0).clone();
        Inventory guiRecover = Bukkit.createInventory(null, 9, "Network Monitor");

        double[] check = getEnergyInfo(loc);

        double here = check[2] + check[4];
        Long startTime = System.currentTimeMillis();
        NetworkRunnable NR = new NetworkRunnable();
        NR.loc = loc.clone();
        NR.startTime = startTime;
        NR.myInv = guiRecover;
        NR.currentData = check;
        int myId = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, NR, 1, 1);
        NR.myId = myId;
        player.openInventory(guiRecover);
//        player.openInventory(guiRecover);

    }
    public static double[] getEnergyInfo(Location loc)
    {
        Set<Location> input = new HashSet<Location>();
        Set<Location> storage = new HashSet<Location>();
        Set<Location> output = new HashSet<Location>();

        double supplyMax = 0.0D;
        double demandMax = 0.0D;
        double batteriesMax = 0.0D;
        double supplyMin = 0.0D;
        double demandMin = 0.0D;
        double batteriesMin = 0.0D;
        double totalCosumption = 0.0D;
        double totalGenerated = 0.0D;


        if (EnergyNet.scan(loc, EnergyNet.Axis.UNKNOWN, new HashSet<Location>(), input, storage, output, supplyMax, demandMax).isEmpty()) {
            //No Network
        }
        else
        {

            for (final Location source: input) {

                totalCosumption = totalCosumption  + getEnergyConsumption(source);
                totalGenerated = totalGenerated + getEnergyProduction(source);
                int capacity = getSafeMaxCharge(source);
                int charge = getSafeCharge(source);
                supplyMax = supplyMax + capacity;
                supplyMin = supplyMin + charge;


            }
            for (Location battery: storage) {
                int capacity = getSafeMaxCharge(battery);
                int charge = getSafeCharge(battery);
                demandMin = demandMin + charge;
                demandMax = demandMax + capacity;
                totalCosumption = totalCosumption  + getEnergyConsumption(battery);
                totalGenerated = totalGenerated + getEnergyProduction(battery);
            }
            for (Location destination: output) {
                int capacity = getSafeMaxCharge(destination);
                int charge = getSafeCharge(destination);
                batteriesMax = batteriesMax + capacity;
                batteriesMin = batteriesMin + charge;
                totalCosumption = totalCosumption  + getEnergyConsumption(destination);
                totalGenerated = totalGenerated + getEnergyProduction(destination);
            }

        }
        double[] tmp = new double[8];
        tmp[0] = supplyMin;
        tmp[1] = supplyMax;
        tmp[2] = demandMin;
        tmp[3] = demandMax;
        tmp[4] = batteriesMin;
        tmp[5] = batteriesMax;
        tmp[6] = totalCosumption;
        tmp[7] = totalGenerated;
        return tmp;
    }

    public static int getSafeCharge(Location source) {
        try {
            return ChargableBlock.getCharge(source);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int getSafeMaxCharge(Location source) {
        try {
            return ChargableBlock.getMaxCharge(source);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int getEnergyProduction(Location loc) {
        try {
            String id = BlockStorage.getBlockInfo(loc, "id");
            SlimefunItem slItem = SlimefunItem.getByID(id);
            if (slItem != null) {
                if (slItem instanceof AGenerator) {
                    AGenerator EnergeyBlock = (AGenerator) slItem;
                    return EnergeyBlock.getEnergyProduction() * 2;
                } else {
                    if (slItem.getEnergyTicker() != null) {
                        double power = slItem.getEnergyTicker().generateEnergy(loc, slItem, null);
                        return (int) (power);
                    }
                }
            }
            return 0;
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    public static int getEnergyConsumption(Location loc)
    {
        String id = BlockStorage.getBlockInfo(loc, "id");
        SlimefunItem slItem = SlimefunItem.getByID(id);
        if (slItem instanceof AContainer) {
            AContainer EnergeyBlock = (AContainer) slItem;
            return EnergeyBlock.getEnergyConsumption() *2;
        }
        return 0;
    }
    public static void onInventoryClickEvent(InventoryClickEvent event) {

        if (event.getInventory().getName().startsWith("Network Monitor"))
        {
            event.setCancelled(true);
        }
    }
    public static boolean onBlockRemoveEvent(Block block)
    {
        String key = getLocationKey(block.getLocation());
        if (key != null) {
            if (BackpackRecover.recovers.contains("network.storage."  + key)) {
                return true;
            }
        }
        return false;
    }
    public static void onBlockBreakEvent(BlockBreakEvent event)
    {
        Block Broken = event.getBlock();
        if ((GriefPrevention.instance.allowBreak(event.getPlayer(), Broken, Broken.getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
            String key = getLocationKey(Broken.getLocation());
            if (key != null) {
                if (BackpackRecover.recovers.contains("network.storage."  + key)) {
                    BackpackRecover.recovers.setValue(key, null);
                   /* event.setDropItems(false);
                    ItemStack recover = NetworkMonitor.getMeAsDrop();
                    event.getPlayer().getInventory().addItem(recover);*/
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
                if ((GriefPrevention.instance.allowBuild(event.getPlayer(), Broken.getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                    String key = getLocationKey(Broken.getLocation());
                    if (key != null) {
                        if (BackpackRecover.recovers.contains("network.storage." + key)) {
                            openGui(event.getPlayer(), event.getClickedBlock().getLocation());
                        }
                    }
                }
            }
        }
    }
    public static void onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Network Monitor")) {
                        Block Placed = event.getBlockPlaced();
                        if ((GriefPrevention.instance.allowBuild(event.getPlayer(), Placed.getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                            String key = getLocationKey(Placed.getLocation());
                            if (key != null) {
                                BackpackRecover.recovers.setValue("network.storage." + key, true);
                            }
                        }
                    }
                }
            }
        }
    }
}

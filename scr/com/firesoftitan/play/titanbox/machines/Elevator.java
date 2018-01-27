package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class Elevator {

    public static HashMap<String, List<Integer>> elevatorByLocation = new HashMap<String, List<Integer>>();
    public static HashMap<String, Long> spamTimes = new HashMap<String, Long>();
    public static Config elevators = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "elevator.yml");
    public Elevator()
    {

            //Collections.sort(lList);
    }
    public static String getKey(Location loc)
    {
        return loc.getWorld().toString() + "_" + loc.getBlockX() + "_" + loc.getBlockZ();
    }
    public static void saveElevators()
    {
        elevators.save();
    }
    public static void saveAllElevators()
    {
        elevators.setValue("elevators", null);
        for(String keys: elevatorByLocation.keySet())
        {
            List<Integer> myList = elevatorByLocation.get(keys);
            if (myList.size() > 0)
            {
                elevators.setValue("elevators." + keys, myList);
            }

        }
    }
    public static void loadAllElevators()
    {
        if (elevators.contains("elevators")) {
            Set<String> keys = elevators.getKeys("elevators");
            for (String key : keys) {
                List<Integer> myList =elevators.getIntList("elevators." + key);
                elevatorByLocation.put(key, myList);
            }
        }
    }
    public static void onPlayerMoveEvent(PlayerMoveEvent event) {
        Location to = event.getTo();
        Location from = event.getFrom();
        Location elevator = to.clone().add(0 , -1, 0);
        String key = getKey(elevator);
        if (elevatorByLocation.containsKey(key)) {
            List<Integer> floors = elevatorByLocation.get(key);
            Long spam = Long.valueOf(0);
            String sUUID = event.getPlayer().getUniqueId().toString();
            if (spamTimes.containsKey(sUUID))
            {
                spam = spamTimes.get(sUUID);
            }
            if (spam + 1000 < System.currentTimeMillis()) {
                event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "You are on a Elevator. Sneak To Move Down, Jump to go Up");
            }
            spamTimes.put(sUUID, System.currentTimeMillis());
            for (int i = 0; i < floors.size(); i++) {
                if (floors.get(i) == elevator.getY()) {
                    if (to.getY() != from.getY() && event.getPlayer().isOnGround()) {
                        if (i + 1 < floors.size()) {
                            int Y = floors.get(i + 1);
                            Location nextfloor = new Location(elevator.getWorld(), event.getPlayer().getLocation().getX(), Y + 1, event.getPlayer().getLocation().getZ());
                            event.getPlayer().teleport(nextfloor);
                        }
                        else
                        {
                            event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "You are on the highest floor");
                        }
                    }
                    return;
                }

            }

        }

    }
    public static void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        Location elevator = event.getPlayer().getLocation().clone().add(0 , -1, 0);
        String key = getKey(elevator);
        if (elevatorByLocation.containsKey(key)) {
            List<Integer> floors = elevatorByLocation.get(key);
            for (int i = 0; i < floors.size(); i++) {
                if (floors.get(i) == elevator.getY()) {
                    if (event.getPlayer().isSneaking() && event.getPlayer().isOnGround())
                    {
                        if (i - 1 > -1) {
                            int Y = floors.get(i - 1);
                            Location nextfloor = new Location(elevator.getWorld(), event.getPlayer().getLocation().getX(), Y + 1, event.getPlayer().getLocation().getZ());
                            event.getPlayer().teleport(nextfloor);
                        }
                        else
                        {
                            event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "You are on the lowest floor");
                        }
                    }
                    return;
                }

            }

        }
    }
    public static void onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Elevator")) {
                        Block Placed = event.getBlockPlaced();
                        if ((GriefPrevention.instance.allowBuild(event.getPlayer(), Placed.getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                            List<Integer> floors = new ArrayList<Integer>();
                            String key = getKey(Placed.getLocation());
                            if (elevatorByLocation.containsKey(key))
                            {
                                floors = elevatorByLocation.get(key);
                            }
                            if (!floors.contains(Placed.getY())) {
                                floors.add(Placed.getY());
                                floors = removeDuplicatesAndOrder(floors);
                                elevatorByLocation.put(key, floors);
                                saveAllElevators();
                            }
                        }
                    }
                }
            }
        }
    }
    public static List<Integer> removeDuplicatesAndOrder(List<Integer> floor)
    {
        Set<Integer> hs = new HashSet<>();
        hs.addAll(floor);
        floor.clear();
        floor.addAll(hs);
        Collections.sort(floor);
        return floor;
    }
    public static boolean onBlockRemoveEvent(Block block)
    {
        String key = getKey(block.getLocation());
        if (elevatorByLocation.containsKey(key)) {
            return true;
        }
        return false;
    }
    public static void onBlockBreakEvent(BlockBreakEvent event)
    {
        Block Broken = event.getBlock();
        if ((GriefPrevention.instance.allowBreak(event.getPlayer(), Broken, Broken.getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
            String key = getKey(Broken.getLocation());
            if (elevatorByLocation.containsKey(key)) {
                List<Integer> floors = elevatorByLocation.get(key);
                for (int i = 0; i < floors.size(); i++) {
                    if (floors.get(i) == Broken.getY()) {
                        floors.remove(i);
                        elevatorByLocation.put(key, floors);
                        saveAllElevators();
                        /*event.setDropItems(false);
                        ItemStack elevator = Elevator.getMeAsDrop();
                        event.getPlayer().getInventory().addItem(elevator);*/
                        return;
                    }
                }
            }
        }
    }
    public static ItemStack getMeAsDrop()
    {
        ItemStack elevator = new ItemStack(Material.SILVER_GLAZED_TERRACOTTA);
        elevator = TitanBox.changeName(elevator, ChatColor.YELLOW + "Elevator");
        elevator = TitanBox.addLore(elevator, ChatColor.WHITE + "Links to other elevators",  ChatColor.WHITE + "Directly above or below", ChatColor.WHITE + "Jump to go up", ChatColor.WHITE + "Sneak to go down");
        return elevator;

    }
}

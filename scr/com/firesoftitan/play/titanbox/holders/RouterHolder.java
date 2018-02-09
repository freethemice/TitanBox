package com.firesoftitan.play.titanbox.holders;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import com.firesoftitan.play.titanbox.machines.ItemRoutingRouter;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.IRRUserRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class RouterHolder {
    public static Config routing = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "routing.yml");
    public static HashMap<String, ItemRoutingRouter> routersByLocation = new HashMap<String, ItemRoutingRouter>();
    public static HashMap<String, ItemRoutingRouter> routersByID = new HashMap<String, ItemRoutingRouter>();
    public static HashMap<String, ItemRoutingRouter> routersByOwner = new HashMap<String, ItemRoutingRouter>();
    public static List<String> bufferList = new ArrayList<String>();
    public static int bufferSize = 10;
    public static int speed = 1;
    public static int bigMax = 45;

    public static long lastTime = 0;
    public static long lagTime = 250;
    public static String name = "Item Routing Router";
    public static HashMap<String, IRRUserRunnable> bufferListT = new HashMap<String, IRRUserRunnable>();

    public RouterHolder()
    {

    }
    public static void tick()
    {

    }
    public static void tickold()
    {
        if (bufferList.size() == 0)
        {
            if (System.currentTimeMillis() > lastTime) {
                 //System.out.println("Starting Run of " + routersByID.size());
            }
            for(String key: routersByID.keySet())
            {
                ItemRoutingRouter locofRouter = RouterHolder.routersByID.get(key);
                if (RouterHolder.routersByLocation.containsKey(locofRouter.getLocation().toString())) {
                    if (locofRouter.getLocation().getChunk().isLoaded()) {
                        Player player = Bukkit.getPlayer(locofRouter.getOwner());
                        if (player != null) {
                            if (player.isOnline()) {
                                if (System.currentTimeMillis() > lastTime) {
                                  //  System.out.println("Adding Router: " +player.getName());
                                }
                                bufferList.add(key);
                                checkPower(locofRouter.getLocation());
                            }
                        }
                    }
                }
            }
        }
        if (bufferList.size() > 0) {
            if (RouterHolder.routersByID.get(bufferList.get(0)) != null)
            {
                Location locofRouter = RouterHolder.routersByID.get(bufferList.get(0)).getLocation();
                int charge = getCharge(locofRouter);
                if (locofRouter.getBlock().getType() != Material.SKULL) {
                    routersByID.remove(bufferList.get(0));
                    RouterHolder.routing.setValue("router." + bufferList.get(0), null);
                    bufferList.remove(0);
                    return;
                }
                if (getCharge(locofRouter) < 2) {
                    if (System.currentTimeMillis() > lastTime) {
                        //System.out.println("No Power On Router: " + locofRouter.toString());
                    }
                    bufferList.remove(0);
                    return;
                }
                setCharge(locofRouter, charge - 1);
            }
        }
        for(int i = 0; i < bufferSize; i++)
        {
            if (bufferList.size() > 0) {
                ItemRoutingRouter me = routersByID.get(bufferList.get(0));
                if (me !=null) {
                    if (me.hasModules()) {
                        me.setLastTick();
                        UUID owner = me.getOwner();
                        MainModule mh = me.getNextBuffered();
                        if (mh != null)
                        {
                            if (mh.isLoaded()) {
                                mh.runMe(owner);
                            }
                        }
                    }
                }
                bufferList.remove(0);

            }
        }
        if (bufferList.size() ==0)
        {
            if (System.currentTimeMillis() > lastTime) {
                //System.out.println("Done with run");
                lastTime = System.currentTimeMillis() + 300000;
            }
        }
    }
    public static  void setCharge(Location location, int amount)
    {
        BlockStorage.addBlockInfo(location, "energy-charge", amount + "");
    }
    public static  void setCapacity(Location location, int amount)
    {
        BlockStorage.addBlockInfo(location, "energy-capacity", amount + "");
    }
    public static void reDrawRouter(Player player)
    {
        try {
            ItemRoutingRouter tmp = RouterHolder.routersByOwner.get(player.getUniqueId().toString());
            if (tmp != null) {
                if (tmp.getLocation() != null) {
                    TitanBox.placeSkull(tmp.getLocation().getBlock(), ItemHolder.ROUTER.getTexute());
                }
            }
        }
        catch (Exception e)
        {

        }
    }
    public static int getCharge(Location location)
    {
        int c = 0;
        if (location == null)
        {
            return  c;
        }
        try {
            c = Integer.parseInt(BlockStorage.getBlockInfo(location).getString("energy-charge"));
        }
        catch (Exception e)
        {

        }
        return c;

    }
    public static int getCapacity(Location location)
    {
        int c = 0;
        if (location == null)
        {
            return  c;
        }
        try {
            c = Integer.parseInt(BlockStorage.getBlockInfo(location).getString("energy-capacity"));
        }
        catch (Exception e)
        {

        }
        return c;

    }
    public static void checkPower(Location location)
    {

        String ID = BlockStorage.getBlockInfo(location, "id");
        String energy =  BlockStorage.getBlockInfo(location, "energy-capacity");
        if (ID == null || energy == null)
        {
            addEnergy(location);
        }
    }
    public static void addEnergy(Location location) {
        BlockStorage.addBlockInfo(location, "id", "FOTStorageUnti");
        BlockStorage.addBlockInfo(location, "energy-capacity", "45");
    }
    public static void onPlayerInteractEvent(PlayerInteractEvent event) {


        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String location = event.getClickedBlock().getLocation().toString();
            if (RouterHolder.routersByLocation.containsKey(location)) {
                ItemRoutingRouter tmpRL = RouterHolder.routersByLocation.get(location);
                checkPower(tmpRL.getLocation());
                ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
                if (!TitanBox.isEmpty(mainHand)) {
                    MainModule mh = MainModule.getModulefromItem(mainHand);
                    if (mh != null) {
                        if (mh.getModuleid() != null) {
                            List<MainModule> tmp = tmpRL.getModules();
                            for (int i = 0; i < tmp.size(); i++) {
                                MainModule tmpMH = tmp.get(i);
                                if (tmpMH.getModuleid().equals(mh.getModuleid())) {
                                    tmp.remove(i);
                                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module removed from router." + ChatColor.WHITE + "(" + tmp.size() + "/" + bigMax + ")");
                                    tmpRL.SaveMe();
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            int max = Math.min(bigMax, RouterHolder.routersByLocation.get(location).getMax());
                            if (tmp.size() >= max) {
                                event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.RED + "Only " + ChatColor.WHITE + max + ChatColor.RED + " is allowed in this unit.");
                                event.setCancelled(true);
                                return;
                            }
                            tmp.add(mh);
                            event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module added to router. " + ChatColor.WHITE + "(" + tmp.size() + "/" + max + ")");
                            tmpRL.SaveMe();
                            event.setCancelled(true);
                            event.getPlayer().getInventory().setItemInMainHand(null);
                            return;
                        }
                    }
                }

                ItemStack item = mainHand;
                if (item != null) {
                    if (item.hasItemMeta()) {
                        if (item.getItemMeta().hasDisplayName()) {
                            if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Upgrade Device")) {
                                event.setCancelled(true);
                                if (!TitanBox.hasBarcodeGood(item))
                                {
                                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "This is an invalid device! Most likely an old one.");
                                    return;
                                }
                                String barcode = TitanBox.readBarcode(item);
                                if (barcode == null)
                                {
                                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "This is an invalid device! Most likely an old one.");
                                    return;
                                }
                                Random rnd = new Random(System.currentTimeMillis());
                                int addint = rnd.nextInt(9) + 1;
                                int newSLots = Math.min(tmpRL.getMax() + addint, bigMax);
                                if (tmpRL.getMax() >= bigMax) {
                                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Upgrade maxed out at " + ChatColor.WHITE + bigMax);
                                    return;
                                }
                                boolean barcodeTrue = Boolean.valueOf(barcode);
                                if (barcodeTrue)
                                {
                                    TitanBox.duppedAlert(event.getPlayer(), item);
                                    return;
                                }
                                TitanBox.setBarcodeTrue(item, event.getPlayer());


                                tmpRL.setMax(newSLots);
                                tmpRL.SaveMe();
                                if (mainHand.getAmount() < 2) {
                                    event.getPlayer().getInventory().setItemInMainHand(null);
                                } else {
                                    mainHand.setAmount(mainHand.getAmount() - 1);
                                    event.getPlayer().getInventory().setItemInMainHand(mainHand);
                                }
                                event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Upgrade completed! " + ChatColor.WHITE + tmpRL.getMax() + ChatColor.GREEN + "/" + ChatColor.WHITE + bigMax);
                                return;
                            }
                        }
                    }
                }
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    tmpRL.showGUI(event.getPlayer());
                }


            }
        }
    }
    public static void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory().getName().startsWith(RouterHolder.name)) {
            event.setCancelled(true);
            String key = event.getInventory().getName().replace(RouterHolder.name + ":", "");
            if (RouterHolder.routersByID.containsKey(key)) {
                ItemRoutingRouter found =  RouterHolder.routersByID.get(key);
                if (event.getRawSlot() > -1 && event.getRawSlot() < 54) {
                    buttonGUIs button = found.getGui().getButton(event.getSlot());
                    found.onInventoryClickEvent(event, button);
                }
                if (event.getRawSlot() > 53 && event.getRawSlot() < 90) {
                    found.onInventoryClickEvent(event, null);
                }
            }
        }
    }
    public static void loadAllRouters()
    {
        if (!RouterHolder.routing.contains("settings.bigMax"))
        {
            RouterHolder.routing.setValue("settings.bigMax", bigMax);
        }
        if (!RouterHolder.routing.contains("settings.speed"))
        {
            RouterHolder.routing.setValue("settings.speed", speed);
        }
        if (!RouterHolder.routing.contains("settings.bufferSize"))
        {
            RouterHolder.routing.setValue("settings.bufferSize", bufferSize);
        }
        if (!RouterHolder.routing.contains("settings.lagtime"))
        {
            RouterHolder.routing.setValue("settings.lagtime", lagTime);
        }

        bigMax = RouterHolder.routing.getInt("settings.bigMax");
        speed = RouterHolder.routing.getInt("settings.speed");
        bufferSize = RouterHolder.routing.getInt("settings.bufferSize");
        lagTime = RouterHolder.routing.getLong("settings.lagtime");

        RouterHolder.routersByID.clear();
        RouterHolder.routersByLocation.clear();
        RouterHolder.routersByOwner.clear();
        if (RouterHolder.routing.contains("router")) {
            Set<String> keys = RouterHolder.routing.getKeys("router");
            for (String key : keys) {
                ItemRoutingRouter.loadRRH(key);
            }
        }
        //System.out.println(RouterHolder.routersByID.size() + "," + RouterHolder.routersByLocation.size() + "," + RouterHolder.routersByOwner.size());
        RouterHolder.routing.save();
    }
    public static void onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    String key = RouterHolder.getNewIDString();
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Upgrade Device"))
                    {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.RED + "Can't place this block!");
                    }
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + name)) {
                        if ((GriefPrevention.instance.allowBuild(event.getPlayer(), event.getBlockPlaced().getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                            ItemRoutingRouter makingme = new ItemRoutingRouter(key);
                            makingme.setOwner(event.getPlayer().getUniqueId());
                            makingme.setLocation(event.getBlockPlaced().getLocation().clone());
                            if (RouterHolder.routersByOwner.containsKey(makingme.getOwner().toString())) {
                                makingme = RouterHolder.routersByOwner.get(makingme.getOwner().toString());
                            }
                            if (!RouterHolder.routersByLocation.containsKey(makingme.getLocation().toString())) {
                                makingme.setLocation(event.getBlockPlaced().getLocation().clone());
                                RouterHolder.routersByID.put(makingme.getID(), makingme);
                                RouterHolder.routersByLocation.put(makingme.getLocation().toString(), makingme);
                                RouterHolder.routersByOwner.put(makingme.getOwner().toString(), makingme);
                                makingme.SaveMe();
                                Bukkit.getScheduler().scheduleSyncDelayedTask(TitanBox.instants, new Runnable() {
                                    @Override
                                    public void run() {
                                        addEnergy(event.getBlock().getLocation());
                                    }
                                }, 1);

                                if (RouterHolder.routersByOwner.containsKey(makingme.getID()))
                                {
                                    ItemRoutingRouter person = RouterHolder.routersByOwner.get(makingme.getID());
                                    if (person != null) {
                                        IRRUserRunnable tmp = new IRRUserRunnable();
                                        tmp.setItemRoutingRouter(person);
                                        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, tmp, 1000, RouterHolder.speed);
                                        tmp.setTimerID(id);
                                        RouterHolder.bufferListT.put(makingme.getID(), tmp);
                                        System.out.println("[Player Setup: " + event.getPlayer().getName() + "]: Router found, id:" + id + " will start in 1 second.");
                                    }
                                    else
                                    {
                                        System.out.println("[Player Setup: " + event.getPlayer().getName() + "]: Router found, loading error.");
                                    }
                                }
                                else
                                {
                                    System.out.println("[Player Setup: " + event.getPlayer().getName() + "]: Player doesn't have a router to load.");
                                }
                            } else {
                                event.setCancelled(true);
                                ItemRoutingRouter tmpL = RouterHolder.routersByOwner.get(makingme.getOwner().toString());
                                TitanBox.placeSkull(tmpL.getLocation().getBlock(), ItemHolder.ROUTER.getTexute());
                                event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.RED + "Only 1 Item Routing Router Allowed Per Player. Your Router Is At " + ChatColor.WHITE + tmpL.getLocation().getWorld().getName() + ", " + tmpL.getLocation().getBlockX() + ", " + tmpL.getLocation().getBlockY() + ", " + tmpL.getLocation().getBlockZ());

                            }
                        }
                    }
                }
            }
        }
    }
    public static String getNewIDString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random(System.currentTimeMillis());
        while (salt.length() < 36) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        if (RouterHolder.routersByID.containsKey(saltStr))
        {
            return getNewIDString();
        }
        return saltStr;

    }
    public static boolean onBlockRemoveEvent(Block block)
    {
        String location = block.getLocation().toString();
        if (RouterHolder.routersByLocation.containsKey(location)) {
            return true;
        }
        return false;
    }
    public static void onBlockBreakEvent(BlockBreakEvent event) {
        try {
            if ((GriefPrevention.instance.allowBreak(event.getPlayer(), event.getBlock(), event.getBlock().getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                String location = event.getBlock().getLocation().toString();
                if (RouterHolder.routersByLocation.containsKey(location)) {
                    ItemRoutingRouter tmpRRH = RouterHolder.routersByLocation.get(location);
                    if (tmpRRH.getModules().size() == 0) {
                        event.setDropItems(false);
                        RouterHolder.routersByLocation.remove(location);
                        //RouterHolder.routersByID.remove(tmpRRH.getID());
                        //RouterHolder.routersByOwner.remove(tmpRRH.getOwner().toString());
                        //RouterHolder.routing.setValue("router." + tmpRRH.getID(), null);
                        BlockStorage._integrated_removeBlockInfo(event.getBlock().getLocation(), true);

                        ItemHolder me = ItemHolder.ROUTER;
                        if (me != null) {
                            ItemStack drop = me.getItem();
                            drop = TitanBox.changeName(drop, ChatColor.YELLOW + "Item Routing Router");
                            drop = TitanBox.addLore(drop, "Links: " + ChatColor.WHITE + "Slimefun, Chest, and Storage units", ChatColor.WHITE + "45 J/s");
                            event.setDropItems(false);
                            event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), drop);
                            event.getPlayer().closeInventory();
                        }


                    } else {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.RED + "You must remove all modules before removing this!");
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void saveRoutes()
    {
        routing.save();
    }
}

package com.firesoftitan.play.titanbox.holders;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import com.firesoftitan.play.titanbox.machines.ItemRoutingRouter;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.IRRUserRunnable;
import com.firesoftitan.play.titansql.CallbackResults;
import com.firesoftitan.play.titansql.DataTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.Table;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class RouterHolder {
    //public static Config routing = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "routing.yml");
    public static Table routingSQL = new Table("tb_routing");
    public static HashMap<String, Location> routersByLocation = new HashMap<String, Location>();
    public static HashMap<String, ItemRoutingRouter> routersByID = new HashMap<String, ItemRoutingRouter>();
    public static HashMap<UUID, ItemRoutingRouter> routersByOwner = new HashMap<UUID, ItemRoutingRouter>();
    public static List<String> bufferList = new ArrayList<String>();
    public static int bufferSize = 10;
    public static int speed = 1;
    public static int bigMax = 45;

    public static long lastTime = 0;
    public static long lagTime = 250;
    public static String name = "Item Routing Router";
    public static HashMap<UUID, IRRUserRunnable> bufferListT = new HashMap<UUID, IRRUserRunnable>();
    private static Config locations = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "irrloc.yml");

    public RouterHolder()
    {

    }
    public static void addRouterLocation(Location location)
    {
        routersByLocation.put(location.toString(), location);
        String key = location.getWorld().getName() + "-" + location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ();
        locations.setValue(key, Utilities.encode(location));
        locations.save();
    }
    public static void removeRouterLocation(Location location) {
        routersByLocation.remove(location.toString());
        String key = location.getWorld().getName() + "-" + location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ();
        locations.setValue(key, null);
        locations.save();
    }

    public static void tick()
    {

    }
    /*public static void tickold()
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
    }*/

    public static void onPlayerInteractEvent(PlayerInteractEvent event) {


        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String location = event.getClickedBlock().getLocation().toString();
            if (RouterHolder.routersByLocation.containsKey(location)) {
                ItemRoutingRouter tmpRL = RouterHolder.routersByOwner.get(event.getPlayer().getUniqueId());
                ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
                if (!TitanBox.isEmpty(mainHand)) {
                    MainModule mh = MainModule.getModulefromItem(mainHand);
                    if (mh != null) {
                        if (mh.getModuleid() != null && mh.ready()) { // && mh.getLink() != null
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
                            int max = Math.min(bigMax, tmpRL.getMax());
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
    public static void loadConfig()
    {
        if (!TitanBox.config.contains("settings.router.bigMax"))
        {
            TitanBox.config.setValue("settings.router.bigMax", bigMax);
        }
        if (!TitanBox.config.contains("settings.router.speed"))
        {
            TitanBox.config.setValue("settings.router.speed", speed);
        }
        if (!TitanBox.config.contains("settings.router.bufferSize"))
        {
            TitanBox.config.setValue("settings.router.bufferSize", bufferSize);
        }
        if (!TitanBox.config.contains("settings.router.lagtime"))
        {
            TitanBox.config.setValue("settings.router.lagtime", lagTime);
        }

        bigMax = TitanBox.config.getInt("settings.router.bigMax");
        speed = TitanBox.config.getInt("settings.router.speed");
        bufferSize = TitanBox.config.getInt("settings.router.bufferSize");
        lagTime = TitanBox.config.getLong("settings.router.lagtime");


        for (String loc: RouterHolder.locations.getKeys()) {
            Location location = Utilities.decodeLocation(RouterHolder.locations.getString(loc));
            RouterHolder.routersByLocation.put(location.toString(), location);
        }
    }
    public static void setupTable()
    {
        routingSQL.addDataType("id", DataTypeEnum.CHARARRAY, true, false, true);
        routingSQL.addDataType("modules", DataTypeEnum.STRINGLIST, false, false, false);
        routingSQL.addDataType("owner", DataTypeEnum.UUID, false, false, false);
        routingSQL.addDataType("size", DataTypeEnum.INTEGER, false, false, false);
        routingSQL.addDataType("location", DataTypeEnum.LOCATION, false, false, false);
        routingSQL.createTable();

    }
    public static void loadAllRouters() {
        loadConfig();

        setupTable();

        RouterHolder.routersByID.clear();
        RouterHolder.routersByOwner.clear();

        loadRouters();
    }
    public static void loadRouters()
    {

        Utilities.reTryLoad(routingSQL, RouterHolder.class, "loadRouters", "Routers");
        routingSQL.search(new CallbackResults() {
            @Override
            public void onResult(List<HashMap<String, ResultData>> results) {
                if (results != null && results.size() >0) {
                    for (HashMap<String, ResultData> result : results) {
                        ItemRoutingRouter.loadRRH(result);
                    }
                }
                System.out.println("[TitanBox: Routers Loaded: " + results.size());
                Utilities.doneTryLoading(routingSQL);
                //Elevator.loadAllElevators();
            }
        });


        //System.out.println(RouterHolder.routersByID.size() + "," + RouterHolder.routersByLocation.size() + "," + RouterHolder.routersByOwner.size());
        //RouterHolder.routing.save();

    }
    public static void onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {

                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Upgrade Device"))
                    {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.RED + "Can't place this block!");
                    }
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + name)) {
                        if ((GriefPrevention.instance.allowBuild(event.getPlayer(), event.getBlockPlaced().getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                            addRouterLocation(event.getBlockPlaced().getLocation());

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
                    removeRouterLocation(event.getBlock().getLocation());
                    /*
                    ItemHolder me = ItemHolder.ROUTER;
                    if (me != null) {
                        ItemStack drop = me.getItem();
                        drop = TitanBox.changeName(drop, ChatColor.YELLOW + "Item Routing Router");
                        drop = TitanBox.addLore(drop, "Links: " + ChatColor.WHITE + "Slimefun, Chest, and Storage units", ChatColor.WHITE + "45 J/s");
                        event.setDropItems(false);
                        event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), drop);
                        event.getPlayer().closeInventory();
                    }*/
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

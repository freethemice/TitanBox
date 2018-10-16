package com.firesoftitan.play.titanbox.listeners;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import com.firesoftitan.play.titanbox.machines.*;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.IRRUserRunnable;
import com.firesoftitan.play.titanbox.shops.MainShops;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

import java.util.*;


/**
 * Created by Daniel on 9/12/2017.
 */
public class ListenerMain implements Listener {

    public HashMap<UUID, ItemStack[]> playerListSaves = new HashMap<UUID, ItemStack[]>();
    public HashMap<String, Long> lastedPressed = new HashMap<String, Long>();
    public ListenerMain()
    {

    }
    public void registerEvents(){
        PluginManager pm = TitanBox.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanBox.instants);
    }

    @EventHandler
    public void  onBlockPhysicsEvent(BlockPhysicsEvent event)
    {
        try {
            if (!event.isCancelled()) {
                if (event.getBlock() != null) {
                    if (BlockStorage.hasBlockInfo(event.getBlock())) {
                        String id = BlockStorage.getBlockInfo(event.getBlock(), "id");
                        if (id != null) {
                            if (event.getBlock().getType() == Material.AIR) {
                                //System.out.println("[TitanBox]: Deleting slimefun air block");
                                //BlockStorage.clearBlockInfo(event.getBlock());
                            }
                            if (!id.equals("")) {//if (id.equals("XP_PLATE")) {

                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("C");
        }

    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event)
    {
        ItemStack[] check  =event.getInventory().getContents();
        for(ItemStack e: check)
        {
            if (!TitanBox.isEmpty(e))
            {
                if (TitanBox.isItemEqual(e, SlimefunItemsHolder.MINING_SLUDGE) || TitanBox.isItemEqual(e, SlimefunItemsHolder.VOID_PARTICLES) || TitanBox.isItemEqual(e, SlimefunItemsHolder.VOID_PARTICLES_NEGATIVE) || TitanBox.isItemEqual(e, SlimefunItemsHolder.VOID_PARTICLES_POSITIVE))
                {
                    event.setCancelled(true);
                }
            }
        }

    }
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        String myUUID = event.getPlayer().getUniqueId().toString();
        if (!lastedPressed.containsKey(myUUID))
        {
            lastedPressed.put(myUUID, Long.valueOf(0));
        }
        Long lastPressed = lastedPressed.get(myUUID);
        if (lastPressed + 250 < System.currentTimeMillis()) {
            StorageUnit.onPlayerInteractEvent(event);

            MainModule.onPlayerInteractEvent(event);

            RouterHolder.onPlayerInteractEvent(event);

            BackpackRecover.onPlayerInteractEvent(event);

            StorageRecover.onPlayerInteractEvent(event);

            NetworkMonitor.onPlayerInteractEvent(event);
        }
        lastedPressed.put(myUUID, System.currentTimeMillis());

        //runVoidTalisman(event.getPlayer());

    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event)
    {
        if (!event.isCancelled()) {
            StorageUnit.onBlockBreakEvent(event);
            RouterHolder.onBlockBreakEvent(event);
            Pumps.onBlockBreakEvent(event);
            Elevator.onBlockBreakEvent(event);
            BackpackRecover.onBlockBreakEvent(event);
            StorageRecover.onBlockBreakEvent(event);
            NetworkMonitor.onBlockBreakEvent(event);
        }
        if (event.getBlock().getType() == Material.END_ROD)
        {
            Location loc = event.getBlock().getLocation().clone();
            if (ElectricMiner.miners.contains(loc.toString() + ".set"))
            {
                event.setDropItems(false);
                ItemStack recover = SlimefunItemsHolder.DRILL_ROD_BROKEN;
                event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), recover);
                ElectricMiner.miners.setValue(loc.toString(), null);
            }
        }
    }
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event)
    {
        StorageUnit.onInventoryCloseEvent(event);
        //runVoidTalisman((Player) event.getPlayer());
    }
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {

        //runVoidTalisman(event.getPlayer());
    }
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        Location ofBlock = event.getBlockPlaced().getLocation();
        if (event.getBlockReplacedState().getType() == Material.AIR)
        {
            if (BlockStorage.hasBlockInfo(ofBlock))
            {
                BlockStorage._integrated_removeBlockInfo(ofBlock, true);
            }
        }
        if (!event.isCancelled()) {
            StorageUnit.onBlockPlaceEvent(event);
            RouterHolder.onBlockPlaceEvent(event);
            Pumps.onBlockPlaceEvent(event);
            Elevator.onBlockPlaceEvent(event);
            BackpackRecover.onBlockPlaceEvent(event);
            StorageRecover.onBlockPlaceEvent(event);
            NetworkMonitor.onBlockPlaceEvent(event);

            if (event.getItemInHand() != null) {
                ItemStack tM = event.getItemInHand().clone();
                if (tM.hasItemMeta()) {
                    if (tM.getItemMeta().hasDisplayName()) {
                        if (tM.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Super Item Mover"))
                        {
                            event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: Can't place SIM.");
                            event.setCancelled(true);
                        }
                        for (ModuleTypeEnum e : ModuleTypeEnum.values()) {
                            if (tM.getItemMeta().getDisplayName().equals(e.getTitle()))
                            {
                                event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: Can't place modules.");
                                event.setCancelled(true);
                            }
                        }

                    }
                }
            }
            if (TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.ELECTRIC_MINER))
            {
                int placingy = event.getBlock().getY();
                int Top = event.getBlock().getWorld().getHighestBlockYAt( event.getBlock().getLocation());
                System.out.println(placingy + "," + Top);
                if (Top < 45)
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GOLD + "The ground is to low here.");
                    event.setCancelled(true);
                }
                if (placingy != Top)
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GOLD + "Can only be placed on surface, make sure there are no trees over head.");
                    event.setCancelled(true);
                }
            }
            if (TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.DRILL_ROD) || TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.DRILL_ROD_BROKEN) || TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.DIAMOND_WRITING_PLATE) || TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.EMERALD_WRITING_PLATE) || TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.ENDER_WRITING_PLATE))
            {
                event.setCancelled(true);
            }
            if (TitanBox.instants.checkforTitanStone(event.getItemInHand()))
            {
                event.setCancelled(true);
                if (event.getPlayer() != null) {
                    event.getPlayer().sendMessage(ChatColor.RED + "[WARNING]: " + ChatColor.GOLD + "The Titan Stone Can't Be Placed On The Ground It Would Truely Kill Us All!!!");
                }
            }


        }
    }
    /*
    @EventHandler(priority= EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityExplode(EntityExplodeEvent e) {
        Iterator<Block> blocks = e.blockList().iterator();
        while (blocks.hasNext()) {
            Block block = blocks.next();
            SlimefunItem item = BlockStorage.check(block);
            if (item != null) {
                blocks.remove();
                if (!item.getName().equalsIgnoreCase("HARDENED_GLASS") && !item.getName().equalsIgnoreCase("WITHER_PROOF_OBSIDIAN") && !item.getName().equalsIgnoreCase("WITHER_PROOF_GLASS") && !item.getName().equalsIgnoreCase("FORCEFIELD_PROJECTOR") && !item.getName().equalsIgnoreCase("FORCEFIELD_RELAY")) {
                    boolean success = true;
                    if (SlimefunItem.blockhandler.containsKey(item.getName())) {
                        success = SlimefunItem.blockhandler.get(item.getName()).onBreak(null, block, item, UnregisterReason.EXPLODE);
                    }
                    if (success) {
                        BlockStorage.clearBlockInfo(block);
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof Wither) {
            SlimefunItem item = BlockStorage.check(e.getBlock());
            if (item != null) {
                if (item.getID().equals("WITHER_PROOF_OBSIDIAN")) e.setCancelled(true);
            }
        }
    }*/
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity()  != null) {
            LivingEntity entity = event.getEntity();

            if (event.getEntity() instanceof Player) {
                Player Dieing = (Player) event.getEntity();
                PlayerInventory CheckInv = Dieing.getInventory();
                Boolean saving = false;
                ;
                for (int i = 0; i < CheckInv.getSize(); i++) {
                    if (TitanBox.instants.checkforTitanStone(CheckInv.getItem(i))) {
                        event.getDrops().clear();
                        saving = true;
                        break;
                    }
                }
                if (saving == true) {
                    ItemStack[] tmpSave = new ItemStack[CheckInv.getSize()];
                    for (int i = 0; i < CheckInv.getSize(); i++) {
                        if (CheckInv.getItem(i) != null) {
                            tmpSave[i] = CheckInv.getItem(i).clone();
                        }
                    }
                    playerListSaves.put(Dieing.getUniqueId(), tmpSave);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        Elevator.onPlayerToggleSneakEvent(event);
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event)
    {
        if (!Utilities.isDoneLoading (RouterHolder.routingSQL))
        {
            try {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Server Still loading databases!");
            } catch (Exception e) {

            }
            return;
        }
    }
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event)
    {
        if (!Utilities.isDoneLoading (RouterHolder.routingSQL))
        {
            try {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server Still loading databases!");
                event.getPlayer().kickPlayer("Server Still loading databases!");
            } catch (Exception e) {

            }
            return;
        }
        System.out.println("[Player Login: " + event.getPlayer().getName() + "]: Setting up");
        UUID myUUDI = event.getPlayer().getUniqueId();
        List<StorageUnit> tmpOwner = StorageUnit.StorageByOwner.get(myUUDI);
        if (tmpOwner == null) tmpOwner = new ArrayList<StorageUnit>();
        for(StorageUnit key: tmpOwner)
        {
            if (key.getOwner().equals(myUUDI))
            {
                StorageUnit.checkPower(key);
            }
        }
        System.out.println("[Player Login: " + event.getPlayer().getName() + "]: checked " + tmpOwner.size() + " storage units, are now online!");

        if (RouterHolder.routersByOwner.containsKey(myUUDI))
        {
            ItemRoutingRouter person = RouterHolder.routersByOwner.get(myUUDI);
            if (person != null) {
                IRRUserRunnable tmp = new IRRUserRunnable();
                tmp.setItemRoutingRouter(person);
                tmp.startCountDown();
                int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, tmp, 1000, RouterHolder.speed);
                tmp.setTimerID(id);
                RouterHolder.bufferListT.put(myUUDI, tmp);
                System.out.println("[Player Login: " + event.getPlayer().getName() + "]: Router found, id:" + id + " will start in 1 second.");
            }
            else
            {
                System.out.println("[Player Login: " + event.getPlayer().getName() + "]: Router found, loading error.");
            }
        }
        else
        {
            String key = RouterHolder.getNewIDString();
            ItemRoutingRouter makingme = new ItemRoutingRouter(key);
            makingme.setOwner(event.getPlayer().getUniqueId());
            makingme.SaveMe();
            RouterHolder.routersByID.put(makingme.getID(), makingme);
            RouterHolder.routersByOwner.put(makingme.getOwner(), makingme);
            System.out.println("[Player Login: " + event.getPlayer().getName() + "]: New Router Created");
            ItemRoutingRouter person = RouterHolder.routersByOwner.get(myUUDI);
            if (person != null) {
                IRRUserRunnable tmp = new IRRUserRunnable();
                tmp.setItemRoutingRouter(person);
                tmp.startCountDown();
                int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, tmp, 1000, RouterHolder.speed);
                tmp.setTimerID(id);
                RouterHolder.bufferListT.put(myUUDI, tmp);
                System.out.println("[Player Login: " + event.getPlayer().getName() + "]: Router found, id:" + id + " will start in 1 second.");
            }
            else
            {
                System.out.println("[Player Login: " + event.getPlayer().getName() + "]: Router found, loading error.");
            }
            //makingme.setLocation(event.getBlockPlaced().getLocation().clone());
        }



        StorageUnit.reDrawStorage(event.getPlayer());
        System.out.println("[Player Login: " + event.getPlayer().getName() + "]: " + "Redrew storage units.");

    }
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        System.out.println("[Player Quiting: " + event.getPlayer().getName() + "]: Unloading...");
        UUID myUUDI = event.getPlayer().getUniqueId();
        if (RouterHolder.bufferListT.containsKey(myUUDI))
        {
            IRRUserRunnable tmp = RouterHolder.bufferListT.get(myUUDI);
            if (tmp != null)
            {
                int id = tmp.getTimerID();
                Bukkit.getScheduler().cancelTask(id);
                System.out.println("[Player Quiting: " + event.getPlayer().getName() + "]: Router found, Thread stopped");
                RouterHolder.bufferListT.remove(myUUDI);
                System.out.println("[Player Quiting: " + event.getPlayer().getName() + "]: Router unload successful.");
            }
            else
            {
                System.out.println("[Player Quiting: " + event.getPlayer().getName() + "]: Router found, unloading error.");
            }
        }
        else
        {
            System.out.println("[Player Quiting: " + event.getPlayer().getName() + "]: No router to remove.");
        }


    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Elevator.onPlayerMoveEvent(event);

    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {

        try {
            if (event.getInventory().getTitle() != null) {
                if (event.getInventory().getTitle().startsWith("Shops: ")) {
                    try {
                        if (event.getRawSlot() > -1  && event.getRawSlot() < 54)
                        {
                            ItemStack clicked = event.getClickedInventory().getItem(event.getRawSlot());
                            if (!TitanBox.isEmpty(clicked))
                            {
                                String name = TitanBox.getName(clicked);
                                TitanBox.instants.mainShops.openShop((Player) event.getWhoClicked(),name);
                                return;
                            }
                        }
                    } catch (Exception e) {

                    } finally {
                        event.setCancelled(true);
                    }
                }
            }


            MainShops.onInventoryClickEvent(event);

            StorageUnit.onInventoryClickEvent(event);

            MainModule.onInventoryClickEvent(event);

            RouterHolder.onInventoryClickEvent(event);

            BackpackRecover.onInventoryClickEvent(event);

            StorageRecover.onInventoryClickEvent(event);

            NetworkMonitor.onInventoryClickEvent(event);

            if (!event.isCancelled()) {
                if (event.getRawSlot() == 2 && event.getWhoClicked() instanceof Player && event.getInventory().getType() == InventoryType.ANVIL) {
                    ItemStack check = event.getInventory().getContents()[0];
                    if (!TitanBox.isEmpty(check)) {
                        if (check.hasItemMeta()) {
                            if (check.getItemMeta().hasDisplayName()) {
                                if (!ChatColor.stripColor(check.getItemMeta().getDisplayName()).equals(check.getItemMeta().getDisplayName())) {
                                    if (check.getType() != Material.TRIPWIRE_HOOK) {
                                        event.setCancelled(true);
                                        event.getWhoClicked().sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "Thats look fancy, lets not mess with it!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

}

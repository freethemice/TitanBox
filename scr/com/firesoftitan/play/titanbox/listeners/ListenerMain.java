package com.firesoftitan.play.titanbox.listeners;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import com.firesoftitan.play.titanbox.items.TitanTalisman;
import com.firesoftitan.play.titanbox.machines.*;
import com.firesoftitan.play.titanbox.modules.MainModule;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


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
    private void runVoidTalisman(Player player) {
        HashMap<Integer,ItemStack> findTally = TitanTalisman.checkFor(player, SlimefunItem.getByID("TALISMAN_VOID"));
        if (findTally != null)
        {
            if (findTally.size() > 0)
            {
                Inventory playerinv =  player.getInventory();
                for(int i = 0; i < 36; i++)
                {
                    ItemStack item = playerinv.getItem(i);
                    if (!TitanBox.isEmpty(item)) {
                        ItemStack leftOver = TitanBox.addItemToStorage(player.getUniqueId(), item);
                        if (leftOver != null) {
                            if (leftOver.getAmount() != playerinv.getItem(i).getAmount()) {
                                playerinv.setItem(i, leftOver.clone());
                            }
                        } else {
                            playerinv.setItem(i, null);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void  onBlockPhysicsEvent(BlockPhysicsEvent event)
    {
        if (!event.isCancelled()) {
            String id = BlockStorage.getBlockInfo(event.getBlock(), "id");
            if (id != null) {
                if (event.getBlock().getType() == Material.AIR)
                {
                    System.out.println("Deleting");
                    BlockStorage.clearBlockInfo(event.getBlock());
                }
                if (!id.equals("")) {//if (id.equals("XP_PLATE")) {

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

        runVoidTalisman(event.getPlayer());

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
    }
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event)
    {
        StorageUnit.onInventoryCloseEvent(event);
        runVoidTalisman((Player) event.getPlayer());
    }
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {

        runVoidTalisman(event.getPlayer());
    }
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event)
    {

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

            if (TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.DIAMOND_WRITING_PLATE) || TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.EMERALD_WRITING_PLATE) || TitanBox.isItemEqual(event.getItemInHand(), SlimefunItemsHolder.ENDER_WRITING_PLATE))
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
    public void onPlayerLoginEvent(PlayerLoginEvent event)
    {
        String myUUDI = event.getPlayer().getUniqueId().toString();
        List<StorageUnit> tmpOwner = new ArrayList<StorageUnit>();
        for(StorageUnit key: StorageUnit.StorageById.values())
        {
            if (key.getOwner().toString().equals(myUUDI))
            {
                tmpOwner.add(key);
            }
        }
        StorageUnit.StorageByOwner.put(myUUDI, tmpOwner);

        StorageUnit.reDrawStorage(event.getPlayer());

        RouterHolder.reDrawRouter(event.getPlayer());

    }
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        String myUUDI = event.getPlayer().getUniqueId().toString();
        StorageUnit.StorageByOwner.remove(myUUDI);

    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Elevator.onPlayerMoveEvent(event);

    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
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

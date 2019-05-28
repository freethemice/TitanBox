package com.firesoftitan.play.titanbox.listeners;

import com.firesoftitan.play.titanbox.Events.ArmorEquipEvent;
import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.custom.CustomCategories;
import com.firesoftitan.play.titanbox.enums.ArmorTypeEnum;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.enums.MonsterRanksEnum;
import com.firesoftitan.play.titanbox.enums.RanksEnum;
import com.firesoftitan.play.titanbox.guis.DataCenterGUI;
import com.firesoftitan.play.titanbox.guis.MailboxGUI;
import com.firesoftitan.play.titanbox.guis.PickAPlayerGUI;
import com.firesoftitan.play.titanbox.items.ChestMover;
import com.firesoftitan.play.titanbox.machines.*;
import com.firesoftitan.play.titanbox.managers.*;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.PlayerProtectionManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.IRRUserRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import java.util.*;


/**
 * Created by Daniel on 9/12/2017.
 */
public class MainListener implements Listener {

    public HashMap<UUID, ItemStack[]> playerListSaves = new HashMap<UUID, ItemStack[]>();
    public HashMap<String, Long> lastedPressed = new HashMap<String, Long>();
    public MainListener()
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
    public void onPlayerItemBreakEvent(PlayerItemBreakEvent event)
    {
        Utilities.saveBrokenItem(event.getPlayer(), event.getBrokenItem());
    }


    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event)
    {
        /*
        ItemStack[] check  =event.getInventory().getContents();
        for(ItemStack e: check)
        {
            if (!TitanBox.isEmpty(e))
            {
                if (TitanBox.isItemEqual(e, SlimefunItemsManager.MINING_SLUDGE) || TitanBox.isItemEqual(e, SlimefunItemsManager.VOID_PARTICLES) || TitanBox.isItemEqual(e, SlimefunItemsManager.VOID_PARTICLES_NEGATIVE) || TitanBox.isItemEqual(e, SlimefunItemsManager.VOID_PARTICLES_POSITIVE))
                {
                    event.setCancelled(true);
                }
            }
        }
*/
    }
    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        ItemStack itemStack = player.getInventory().getItemInMainHand().clone();
        if (!Utilities.isEmpty(itemStack))
        {
            if (Utilities.getName(itemStack, false).startsWith(ChatColor.GREEN + "Wand for: " + ChatColor.WHITE ))
            {
                if (entity != null && !(entity instanceof Player) && (entity instanceof Animals)&& !(entity instanceof Monster)) {
                    if (entity instanceof Tameable)
                    {
                        if (((Tameable)entity).getOwner() != null) return;
                    }
                    try {
                        String name = Utilities.getName(itemStack);
                        name = name.replace("Wand for: ", "");
                        String[] locationData = name.split("_");
                        World world = Bukkit.getWorld(locationData[0]);
                        Location blockLoc = new Location(world, Integer.parseInt(locationData[1]), Integer.parseInt(locationData[2]), Integer.parseInt(locationData[3]));
                        if (entity.getCustomName() == null) entity.teleport(blockLoc);
                    } finally {
                        event.setCancelled(true);
                    }
                }


            }
        }
    }

    private HashMap<UUID, Long> wildTPTime = new HashMap<UUID, Long>();
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {

        Action action = event.getAction();
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock(); //null returned here means interacting with air
        Location ofBlock  = null;
        if (clickedBlock != null) ofBlock = clickedBlock.getLocation();
        Material clickedBlockType = null;
        if(clickedBlock != null)
        {
            clickedBlockType = clickedBlock.getType();
        }
        else
        {
            clickedBlockType = Material.AIR;
        }
        if (Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), SlimefunItemsManager.STORAGE_MOVER))
        {
            ChestMover chestMover = TitanBox.instants.chestMover;
            if (action == Action.LEFT_CLICK_BLOCK)
            {
                chestMover.placeInventory(player, event.getClickedBlock());
            }
            if (action == Action.RIGHT_CLICK_BLOCK)
            {
                chestMover.removeInventory(player, event.getClickedBlock());
            }
            event.setCancelled(true);
            return;
        }
        if(	clickedBlock != null)
        {
            if (action == Action.RIGHT_CLICK_BLOCK)
            {
                boolean isHolder = clickedBlock.getState() instanceof InventoryHolder;
                if (clickedBlockType == Material.PLAYER_HEAD)
                {
                    Block underChest = clickedBlock.getLocation().clone().add(0, -1, 0).getBlock();
                    if (underChest.getType().toString().endsWith("_FENCE"))
                    {
                        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
                        if (ffH == null)
                        {
                            player.sendMessage(ChatColor.RED + "This machines need to be in a force field to work.");
                            return;
                        }
                        try {
                            if (CustomSkull.getTexture(clickedBlock).equals(SlimefunItemsManager.MAILBOX)) {
                                //This is a mailBox
                                MailboxGUI mailboxGUI =  MailboxGUI.getMailBox(event.getPlayer().getUniqueId());
                                mailboxGUI.buildGui(event.getPlayer());
                                event.setCancelled(true);
                                return;
                            }
                            if (CustomSkull.getTexture(clickedBlock).equals(SlimefunItemsManager.AUCTIONHOUSE)) {
                                //This is a mailBox
                                TitanBox.bypassCommands.add(player.getUniqueId());
                                Bukkit.dispatchCommand(event.getPlayer(), "ah");
                                event.setCancelled(true);
                                return;
                            }
                            if (CustomSkull.getTexture(clickedBlock).equals(SlimefunItemsManager.DATACENTER)) {
                                DataCenterGUI dataCenterGUI = DataCenterGUI.getDataCenter(event.getPlayer().getUniqueId());
                                if (player.isSneaking())
                                {
                                    dataCenterGUI.buildCommands();
                                }
                                else {
                                    dataCenterGUI.buildGUI();
                                }
                                dataCenterGUI.open();
                                event.setCancelled(true);
                                return;
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }


        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            ItemStack tester = SlimefunItemsManager.WILD_HOE.clone();
            tester.setDurability(event.getPlayer().getInventory().getItemInMainHand().getDurability());
            ItemStack tester2 = SlimefunItemsManager.WILD_HOE_II.clone();
            tester2.setDurability(event.getPlayer().getInventory().getItemInMainHand().getDurability());
            ItemStack tester3 = SlimefunItemsManager.WILD_HOE_III.clone();
            tester3.setDurability(event.getPlayer().getInventory().getItemInMainHand().getDurability());
            if (Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), tester) || Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), tester2) || Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), tester3)) {
                int size = 0;
                short durab = 60;
                if (Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), tester))
                {
                    size = 10000;
                    durab = Material.WOODEN_HOE.getMaxDurability();
                }
                if (Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), tester2))
                {
                    size = 25000;
                    durab = Material.IRON_HOE.getMaxDurability();
                }
                if (Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), tester3))
                {
                    size = 50000;
                    durab = Material.DIAMOND_HOE.getMaxDurability();
                }
                long timestarted = 0;
                event.setCancelled(true);
                ItemStack hoe = event.getPlayer().getInventory().getItemInMainHand();
                if (wildTPTime.containsKey(player.getUniqueId())) {
                    timestarted = wildTPTime.get(player.getUniqueId());
                }
                if (System.currentTimeMillis() - timestarted < 15000) {
                    player.sendMessage(ChatColor.RED + Utilities.getName(tester) + " is still warm from the teleport, give it a few seconds to cool down");
                    return;
                }
                hoe.setDurability((short) (hoe.getDurability() + 10));
                if (hoe.getDurability() >= durab) {
                    event.getPlayer().getInventory().setItemInMainHand(null);
                }

                Location location = Utilities.getRandomLocation(player.getWorld(), size);
                wildTPTime.put(player.getUniqueId(), System.currentTimeMillis());
                Utilities.startTeleport(player, location.clone());
                return;
            }
            if (Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), SlimefunItemsManager.DATA_CENTER) || Utilities.isItemEqual(event.getPlayer().getInventory().getItemInMainHand(), SlimefunItemsManager.COMMAND_CENTER)) {
                DataCenterGUI dataCenterGUI = DataCenterGUI.getDataCenter(event.getPlayer());
                if (player.isSneaking())
                {
                    dataCenterGUI.buildCommands();
                }
                else {
                    dataCenterGUI.buildGUI();
                }
                dataCenterGUI.open();
                event.setCancelled(true);
                return;
            }
        }
        String myUUID = event.getPlayer().getUniqueId().toString();
        if (!lastedPressed.containsKey(myUUID))
        {
            lastedPressed.put(myUUID, Long.valueOf(0));
        }
        Long lastPressed = lastedPressed.get(myUUID);
        if (lastPressed + 250 < System.currentTimeMillis()) {
            StorageUnit.onPlayerInteractEvent(event);

            MainModule.onPlayerInteractEvent(event);

            RouterManager.onPlayerInteractEvent(event);

            BackpackRecover.onPlayerInteractEvent(event);

            StorageRecover.onPlayerInteractEvent(event);

            NetworkMonitor.onPlayerInteractEvent(event);
        }
        lastedPressed.put(myUUID, System.currentTimeMillis());

        //runVoidTalisman(event.getPlayer());

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockBreakEvent(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        Location location = null;
        World world =  null;
        if (block != null)
        {
            location = block.getLocation();
            world = location.getWorld();
        }

        int pluginsAfterMe = 0;
        RegisteredListener[] allRL = event.getHandlers().getRegisteredListeners();
        boolean found = false;
        for(int i = 0; i < allRL.length; i++)
        {
            if (found)
            {
                pluginsAfterMe++;
            }
            if (allRL[i].getPlugin() == TitanBox.instants && allRL[i].getListener() == this)
            {
                found = true;
            }

        }

        PlayerManager playerManager = PlayerManager.getPlayer(event.getPlayer());
        if (playerManager.hasTitanItemRecovery())
        {
            if (Utilities.areEqual(playerManager.getTitanItemRecovery(), location))
            {
                playerManager.removeTitanItemRecovery();
            }
        }
        if (playerManager.hasTitanDeathRecovery())
        {
            if (Utilities.areEqual(playerManager.getTitanDeathRecovery(), location))
            {
                playerManager.removeTitanDeathRecovery();
            }
        }
        if (FreeEnergy.checkForceFieldFor(location))
        {
            FreeEnergy.removeFromForceField(location);
        }

        if ((Utilities.hasBuildRights(event.getPlayer(), location))) {
            if (block.getState() instanceof CreatureSpawner)
            {
                if (BlockStorage.hasBlockInfo(location)) {
                    event.setCancelled(true);
                    CreatureSpawner spawner = (CreatureSpawner) block.getState();
                    String type = Utilities.fixCapitalization(spawner.getSpawnedType().name());
                    ItemStack spawnerItem = Utilities.getSpawner(type);
                    world.dropItemNaturally(location, spawnerItem.clone());
                    block.setType(Material.AIR);
                    BlockStorage._integrated_removeBlockInfo(location, true);
                }

            }

            if (block.getType() == Material.PLAYER_HEAD)
            {
                try {
                    if (CustomSkull.getTexture(block).equals(SlimefunItemsManager.MAILBOX) || CustomSkull.getTexture(block).equals(SlimefunItemsManager.AUCTIONHOUSE) | CustomSkull.getTexture(block).equals(SlimefunItemsManager.DATACENTER))
                    {
                        block.setType(Material.AIR);
                        event.setCancelled(true);
                    }
                    if (CustomSkull.getTexture(block).equals(SlimefunItemsManager.TURRET))
                    {
                        block.setType(Material.AIR);
                        event.setCancelled(true);
                        ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location, 500);
                        if (ffH != null)
                        {
                            RanksEnum rank = RanksEnum.valueOf(event.getPlayer());
                            ffH.removeTurret(location);
                            event.getPlayer().sendMessage(ChatColor.RED + "You have " + ffH.getTurretCount() + " out of " +  rank.getTurrets() + " turrets");
                        }
                    }
                } catch (Exception e) {

                }
            }
            WorldManager worldManager = WorldManager.getWorldHolder(location.getWorld());
            if (worldManager.getFieldAt(location) != null) {
                ForceFieldManager forceFieldManager = worldManager.getFieldAt(location);
                PlayerProtectionManager ppH = PlayerProtectionManager.getPlayer(forceFieldManager.getOwner());
                forceFieldManager.saved();
                worldManager.removeForceField(location);
                ppH.remove(forceFieldManager.getLocation());
                int count = ppH.count();
                event.getPlayer().sendMessage(ChatColor.GREEN + "You have " + count + " out of " + RanksEnum.valueOf(event.getPlayer()).getValue() + " Force Fields, Move up in rank to get more.");
                event.setCancelled(true);
                block.getWorld().dropItem(location, SlimefunItemsManager.FORCE_FIELD_BLUE.clone());
                BlockStorage.clearBlockInfo(block);
                block.setType(Material.AIR);

                return;
            }
        }
        if (!event.isCancelled()) {
            StorageUnit.onBlockBreakEvent(event);
            RouterManager.onBlockBreakEvent(event);
            Pumps.onBlockBreakEvent(event);
            Elevator.onBlockBreakEvent(event);
            BackpackRecover.onBlockBreakEvent(event);
            StorageRecover.onBlockBreakEvent(event);
            NetworkMonitor.onBlockBreakEvent(event);



            if ((Utilities.hasBuildRights(event.getPlayer(), location)) ) {
                if (block.getType() == Material.STONE) {
                    if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) {
                        Random random = new Random(System.currentTimeMillis());
                        ItemStack itemStack = SlimefunItemsManager.SALTPETER.clone();
                        int amount = random.nextInt(4);
                        amount++;
                        itemStack.setAmount(amount);
                        if (random.nextInt(1000) < 100) {
                            event.getPlayer().getWorld().dropItem(location, itemStack.clone());
                        }
                        if (ConfigManager.isStoneBreak()) {
                            TitanBox.runCommands(event.getPlayer(), 1, location, ConfigManager.getOreBreakCommands());
                            int ExpOrbs = (int) 4;
                            if (block.getType() == Material.STONE) ExpOrbs = 10;
                            ExperienceOrb orb = null;
                            ((ExperienceOrb) world.spawn(event.getPlayer().getLocation(), ExperienceOrb.class)).setExperience(ExpOrbs);
                        }
                    }
                }
                if (block.getType().name().endsWith("_ORE")) {
                    TitanBox.runCommands(event.getPlayer(), 1, location, ConfigManager.getOreBreakCommands());
                    int ExpOrbs = (int) 4;
                    if (block.getType() == Material.STONE) ExpOrbs = 10;
                    ExperienceOrb orb = null;
                    ((ExperienceOrb) world.spawn(event.getPlayer().getLocation(), ExperienceOrb.class)).setExperience(ExpOrbs);
                }
            }
        }
    }
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event)
    {
        Player p = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        Utilities.scanInventory(p);
        Utilities.scanInventory(inventory);

    }
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event)
    {
        StorageUnit.onInventoryCloseEvent(event);
    }
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        if (Utilities.hasNBTTag(itemStack, "Damage")) {
            ItemStack fixed = Utilities.removeNBTTag(itemStack, "Damage");
            item.setItemStack(fixed);
        }
        if (Utilities.checkAlterProbe(itemStack))
        {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        if (Utilities.hasNBTTag(itemStack, "Damage")) {
            ItemStack fixed = Utilities.removeNBTTag(itemStack, "Damage");
            item.setItemStack(fixed);
        }
        if (Utilities.checkAlterProbe(itemStack))
        {
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        Block block  = event.getBlock();
        Block blockPlaced = event.getBlockPlaced();
        Location location = block.getLocation().clone();
        ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();

        if (SlimefunItemsManager.isForceFieldRequired(mainHand)) {
            ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
            if (ffH == null) {
                player.sendMessage(ChatColor.RED + "This machines need to be in a force field to work.");
                event.setCancelled(true);
                return;
            }
        }
        if (SlimefunItemsManager.isFreeFactory(mainHand))
        {
            FreeFactory check = SlimefunItemsManager.getFreeFactory(mainHand);
            if (location.getWorld().getEnvironment() != check.getEnvironment())
            {
                player.sendMessage(ChatColor.RED + "This machines need to be in placed in a " + Utilities.fixCapitalization(check.getEnvironment().name()) + " like world.");
                event.setCancelled(true);
                return;
            }

        }
        if (Utilities.isItemEqual(mainHand, SlimefunItemsManager.TITAN_ITEM_RECOVERY) || Utilities.isItemEqual(mainHand, SlimefunItemsManager.TITAN_ITEM_RECOVERY_2))
        {
            if (PlayerManager.getPlayer(player).hasTitanItemRecovery()) {
                player.sendMessage(ChatColor.RED + "Only one per player");
                event.setCancelled(true);
                return;
            }
        }
        if (Utilities.isItemEqual(mainHand, SlimefunItemsManager.TITAN_DEATH_RECOVERY) || Utilities.isItemEqual(mainHand, SlimefunItemsManager.TITAN_DEATH_RECOVERY_2))
        {
            if (PlayerManager.getPlayer(player).hasTitanDeathRecovery()) {
                player.sendMessage(ChatColor.RED + "Only one per player");
                event.setCancelled(true);
                return;
            }
        }
        if (Utilities.isItemEqual(mainHand, SlimefunItemsManager.STORAGE_MOVER))
        {
            event.setCancelled(true);
            return;
        }
        if (Utilities.isItemEqual(player.getInventory().getItemInMainHand(), SlimefunItemsManager.DATA_CENTER)) {

            event.setCancelled(true);
            return;
        }
        if (Utilities.isItemEqual(event.getItemInHand(), SlimefunItemsManager.DIAMOND_WRITING_PLATE) || Utilities.isItemEqual(event.getItemInHand(), SlimefunItemsManager.EMERALD_WRITING_PLATE) || Utilities.isItemEqual(event.getItemInHand(), SlimefunItemsManager.ENDER_WRITING_PLATE))
        {
            event.setCancelled(true);
            return;
        }
        if (TitanBox.instants.checkforTitanStone(event.getItemInHand()))
        {
            event.setCancelled(true);
            if (player != null) {
                player.sendMessage(ChatColor.RED + "[WARNING]: " + ChatColor.GOLD + "The Titan Stone Can't Be Placed On The Ground It Would Truely Kill Us All!!!");
            }
            return;
        }
        if (event.getItemInHand() != null) {
            ItemStack tM = event.getItemInHand().clone();
            if (tM.hasItemMeta()) {
                if (tM.getItemMeta().hasDisplayName()) {
                    if (tM.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Super Item Mover"))
                    {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: Can't place SIM.");
                        event.setCancelled(true);
                        return;
                    }
                    for (ModuleTypeEnum e : ModuleTypeEnum.values()) {
                        if (tM.getItemMeta().getDisplayName().equals(e.getTitle()))
                        {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: Can't place modules.");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if (blockPlaced.getType() == Material.CHEST || blockPlaced.getType() == Material.DROPPER || blockPlaced.getType() == Material.FURNACE)
        {
            Location under = location.clone().add(0 ,-1, 0);
            if (under.getBlock().getType().toString().endsWith("_FENCE")) {
                ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
                if (ffH == null)
                {
                    player.sendMessage(ChatColor.RED + "This machines need to be in a force field to work.");
                    event.setCancelled(true);
                    return;
                }
                if (blockPlaced.getType() == Material.CHEST) {
                    BlockFace targetFace = ((org.bukkit.material.DirectionalContainer) block.getState().getData()).getFacing();
                    Utilities.fenceMachinMaker(block, location, targetFace, SlimefunItemsManager.MAILBOX);
                    return;
                }
                if (blockPlaced.getType() == Material.DROPPER)
                {
                    BlockFace targetFace = ((org.bukkit.material.DirectionalContainer) block.getState().getData()).getFacing();
                    Utilities.fenceMachinMaker(block, location, targetFace, SlimefunItemsManager.AUCTIONHOUSE);
                    return;
                }
                if (blockPlaced.getType() == Material.FURNACE)
                {
                    BlockFace targetFace = ((org.bukkit.material.DirectionalContainer) block.getState().getData()).getFacing();
                    Utilities.fenceMachinMaker(block, location, targetFace, SlimefunItemsManager.DATACENTER);
                    return;
                }

            }

        }
        if (blockPlaced.getType() == Material.PISTON)
        {
            Location under = location.clone().add(0 ,-1, 0);
            if (under.getBlock().getType().toString().endsWith("_FENCE")) {
                ForceFieldManager ffH = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location, 500);
                ForceFieldManager ffH2 = WorldManager.getWorldHolder(location.getWorld()).getFieldIn(location);
                if ((ffH == null) || (ffH2 != null)) {
                    player.sendMessage(ChatColor.RED + "You can't place this in a force field.");
                    event.setCancelled(true);
                    return;
                }
                if ((ffH == null) || (ffH2 == null)) {
                    player.sendMessage(ChatColor.RED + "You must place this with in 500 block from a force field.");
                    event.setCancelled(true);
                    return;
                }
                if (!player.getUniqueId().equals(ffH.getOwner())) {
                    player.sendMessage(ChatColor.RED + "Only the owner of the force field area can place turrets.");
                    event.setCancelled(true);
                    return;
                }
                if (ffH.isAdmin()) {
                    player.sendMessage(ChatColor.RED + "Are you crazy, you can't place turrets in an admin force field area.");
                    event.setCancelled(true);
                    return;
                }
                RanksEnum rank = RanksEnum.valueOf(player);
                if (ffH.getTurretCount() >= rank.getTurrets())
                {
                    player.sendMessage(ChatColor.RED + "You have the maximum amount for this rank and force field: " + ffH.getTurretCount() + "/" +  rank.getTurrets());
                    event.setCancelled(true);
                    return;
                }
                if (blockPlaced.getType() == Material.PISTON) {
                    BlockFace targetFace = ((org.bukkit.material.Directional) block.getState().getData()).getFacing();
                    Utilities.fenceMachinMaker(block, location, targetFace, SlimefunItemsManager.TURRET);
                    ffH.addTurret(location);
                    player.sendMessage(ChatColor.RED + "You have " + ffH.getTurretCount() + " out of " +  rank.getTurrets() + " turrets");
                }

            }
        }

        if (event.getBlockReplacedState().getType() == Material.AIR)
        {
            if (BlockStorage.hasBlockInfo(location))
            {
                BlockStorage._integrated_removeBlockInfo(location, true);
            }
        }
        if ((Utilities.hasBuildRights(player, location)) ) {

            if (Utilities.isItemEqual(event.getItemInHand(), SlimefunItemsManager.FORCE_FIELD_YELLOW) || Utilities.isItemEqual(event.getItemInHand(), SlimefunItemsManager.FORCE_FIELD_BLUE) || Utilities.isItemEqual(event.getItemInHand(), SlimefunItemsManager.FORCE_FIELD_GREEN)) {
                WorldManager worldManager = WorldManager.getWorldHolder(location.getWorld());
                ForceFieldManager ffH = new ForceFieldManager(player.getUniqueId(), location);
                ffH.setSize(15);
                ffH.setMin(15);
                ffH.setEfficiency(1);
                ffH.setMax(15 * 4);
                ForceFieldManager forceFieldManager = worldManager.canPlaceNewForceField(location, ffH);
                if (forceFieldManager != null) {
                    player.sendMessage(ChatColor.RED + "You are too close to anthor player to place a new force field.");
                    player.sendMessage(ChatColor.GRAY + "Located at: " + forceFieldManager.getLocation().getBlockX() + "," + forceFieldManager.getLocation().getBlockY() + "," + forceFieldManager.getLocation().getBlockZ());
                    String oName = Bukkit.getOfflinePlayer( forceFieldManager.getOwner()).getName();
                    if (forceFieldManager.isAdmin()) oName = "Admin";
                    player.sendMessage(ChatColor.GRAY + "Owner: " + oName);
                    event.setCancelled(true);
                    return;
                }
                PlayerProtectionManager ppH = PlayerProtectionManager.getPlayer(ffH.getOwner());
                int count = ppH.count();
                RanksEnum ranksEnum = RanksEnum.valueOf(player);
                if (count >= ranksEnum.getAmount())
                {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You are only allowed " + ranksEnum.getAmount() + " Force Field(s), Move up in rank to get more.");
                    return;
                }
                ppH.add(ffH);
                count++;
                player.sendMessage(ChatColor.GREEN + "You have " + count + " out of " + ranksEnum.getAmount() + " Force Field(s), Move up in rank to get more.");
                String idSQL = worldManager.getNewIDString();
                ffH.setId(idSQL);
                worldManager.addNewForceField(ffH);
                return;
            }
        }
        if (!event.isCancelled()) {
            if (StorageUnit.onBlockPlaceEvent(event)) return;
            if (RouterManager.onBlockPlaceEvent(event)) return;
            if (Pumps.onBlockPlaceEvent(event)) return;
            if (Elevator.onBlockPlaceEvent(event)) return;
            if (BackpackRecover.onBlockPlaceEvent(event)) return;
            if (StorageRecover.onBlockPlaceEvent(event)) return;
            if (NetworkMonitor.onBlockPlaceEvent(event)) return;
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
                List<ItemStack> stackList = event.getDrops();

                Boolean saving = false;
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
                else
                {
                    if (Utilities.hasBrokenRecovery(Dieing)) {
                        for(int i = 0; i < stackList.size(); i++)
                        {
                            ItemStack item = stackList.get(i);
                            if (!Utilities.isEmpty(item)) {
                                ItemStack leftOver = Utilities.addItemToStorage(entity.getUniqueId(), item);
                                if (leftOver != null) {
                                    if (leftOver.getAmount() != stackList.get(i).getAmount()) {
                                        stackList.remove(i);
                                        stackList.add(i, leftOver.clone());
                                    }
                                } else {
                                    stackList.remove(i);
                                    stackList.add(i, new ItemStack(Material.AIR));
                                }
                            }
                        }
                        stackList.remove(new ItemStack(Material.AIR));

                        for (int i = 0; i < stackList.size(); i++) {
                            boolean saved = Utilities.saveDeathItem(Dieing, stackList.get(i));
                            if (!saved)
                            {
                                entity.getWorld().dropItem(entity.getLocation(), stackList.get(i));
                            }
                        }
                        List<ItemStack> drops = new ArrayList<ItemStack>();
                        drops.addAll(event.getDrops());
                        event.getDrops().clear();

                        for(ItemStack itemStack: drops)
                        {
                            if (Utilities.isItemEqual(SlimefunItems.SOULBOUND_AXE, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_BOOTS, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_BOW, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_CHESTPLATE, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_ELYTRA, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_HELMET, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_HOE, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_LEGGINGS, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_PICKAXE, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_SHOVEL, itemStack, false) ||
                                    Utilities.isItemEqual(SlimefunItems.SOULBOUND_SWORD, itemStack, false)) {
                                event.getDrops().add(itemStack);
                                System.out.println("Added");
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        try {
            Elevator.onPlayerToggleSneakEvent(event);

            Player player = event.getPlayer();
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (!Utilities.isEmpty(itemStack))
                {
                    String texture = CustomSkull.getTexture(itemStack);
                    if (texture !=  null) {
                        if (texture.equals(SlimefunItemsManager.DATACENTER) && event.isSneaking()) {
                            itemStack = CustomSkull.getItem(itemStack, SlimefunItemsManager.COMMANDCENTER);
                            inventory.setItem(i, itemStack.clone());
                        }
                        else if (texture.equals(SlimefunItemsManager.COMMANDCENTER) && !event.isSneaking()) {
                            itemStack = CustomSkull.getItem(itemStack, SlimefunItemsManager.DATACENTER);
                            inventory.setItem(i, itemStack.clone());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event)
    {
        if (!Utilities.isDoneLoading (RouterManager.routingSQL))
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
    public void onEntitySpawnEvent(EntitySpawnEvent event)
    {
        if (event.getEntity() instanceof Monster)
        {
            MonsterRanksEnum myRank  = MonsterRanksEnum.getMonsterType(event.getEntity());
            if (myRank == MonsterRanksEnum.DEFAULT) {
                LivingEntity LE = (LivingEntity) event.getEntity();
                double cH = LE.getHealth();
                String name = LE.getName();
                MonsterRanksEnum monsterRanksEnum = MonsterRanksEnum.DEFAULT;
                List<Entity> playerFinder = LE.getNearbyEntities(100, 100, 100);
                for(Entity entity: playerFinder)
                {
                    if (entity instanceof Player)
                    {
                        PlayerManager playerManager = PlayerManager.getPlayer((Player)entity);
                        MonsterRanksEnum ranksEnum = playerManager.getHighestType();
                        if (ranksEnum.getIndex() > monsterRanksEnum.getIndex())
                        {
                            monsterRanksEnum = ranksEnum;
                        }
                        if (monsterRanksEnum == MonsterRanksEnum.DEFAULT) monsterRanksEnum = ranksEnum;
                    }
                }
                if (monsterRanksEnum == MonsterRanksEnum.DEFAULT) monsterRanksEnum = MonsterRanksEnum.getRandomType();
                LE.setMaxHealth(cH * monsterRanksEnum.getHealth());
                LE.setHealth(LE.getMaxHealth());
                LE.setCustomName(monsterRanksEnum.getChatColor() + monsterRanksEnum.getName() + ":" + ChatColor.WHITE + name);
                LE.setCustomNameVisible(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player)) {
            MonsterRanksEnum monsterRanksEnum = MonsterRanksEnum.getMonsterType(event.getDamager());
            if (monsterRanksEnum != MonsterRanksEnum.DEFAULT) {
                if (((LivingEntity) event.getEntity()).getHealth() > MonsterRanksEnum.TITAN.getHealth()*20 + 100)
                {
                    event.getEntity().remove();
                }
                double s = event.getDamage();
                if (s > 2.5f) s = 2.5f;
                double out = s * monsterRanksEnum.getDamage(); //(10 + rnd.nextInt(10) + 1);
                // 10 for lucky
                // 14.2 eclipse
                // 18.8 titan

                event.setDamage(out);

                /*if (event.getEntity() instanceof Player) {
                    System.out.println("---------------");
                    System.out.println(s);
                    System.out.println(out);
                    System.out.println(event.getFinalDamage());
                    System.out.println("---------------");
                }*/
            }
        }
        else
        {
            //System.out.println(((LivingEntity)event.getEntity()).getHealth() + "/" + ((LivingEntity)event.getEntity()).getMaxHealth() );

        }
        if (event.getEntity() instanceof LivingEntity) {
            if (event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth()) {
                Random random = new Random(System.currentTimeMillis());
                int myR = random.nextInt(1000);
                RanksEnum rank = RanksEnum.RANK_1;
                if (event.getDamager() != null && event.getDamager().getType() == EntityType.PLAYER) {
                    rank = RanksEnum.valueOf((Player) event.getDamager());
                }
                MonsterRanksEnum monsterRanksEnum = MonsterRanksEnum.getMonsterType(event.getEntity());
                int myRmin = 40;
                int myRmax = myRmin + (int) (10 * rank.getDrops()) * monsterRanksEnum.getIndex();
                if (myR > myRmin && myR < myRmax) {
                    List<String> nameSCanGive = new ArrayList<String>();
                    for(String name: Slimefun.listIDs())
                    {
                        if (SlimefunItem.getByID(name).getCategory() == Categories.RESOURCES
                                || SlimefunItem.getByID(name).getCategory() == Categories.ARMOR
                                 || SlimefunItem.getByID(name).getCategory() == CustomCategories.SLIMEFUN_RESOURCES
                                || SlimefunItem.getByID(name).getCategory() == Categories.FOOD
                                || SlimefunItem.getByID(name).getCategory() == Categories.MAGIC
                                || SlimefunItem.getByID(name).getCategory() == Categories.TECH_MISC
                                || SlimefunItem.getByID(name).getCategory() == Categories.TALISMANS_1
                                || SlimefunItem.getByID(name).getCategory() == Categories.WEAPONS
                                || SlimefunItem.getByID(name).getCategory() == Categories.MAGIC_ARMOR
                                || SlimefunItem.getByID(name).getCategory() == Categories.ARMOR
                                )
                        {
                            nameSCanGive.add(name);
                        }
                    }
                    int item = random.nextInt(nameSCanGive.size());
                    ItemStack toDrop = SlimefunItem.getByID(nameSCanGive.get(item)).getItem();
                    if (event.getEntity().getType() != EntityType.PLAYER) {
                        World world = event.getEntity().getWorld();
                        world.dropItem(event.getEntity().getLocation(), toDrop.clone());
                    }

                }
            }
        }

    }
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event)
    {
        TitanBox.instants.updateChecker.messageUpdate(event.getPlayer());
        new PlayerManager(event.getPlayer());
        if (!Utilities.isDoneLoading (RouterManager.routingSQL))
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
        if (!TitanBox.settings.contains("settings." + myUUDI.toString() + ".particle"))
        {
            TitanBox.settings.setValue("settings." + myUUDI.toString() + ".particle", true);
        }

        MailboxGUI.setupNewBox(myUUDI);

        BulkPackageManager.checkUndeliveredPackages(event.getPlayer());

        List<StorageUnit> tmpOwner = StorageUnit.StorageByOwner.get(myUUDI);
        if (tmpOwner == null) {
            tmpOwner = new ArrayList<StorageUnit>();
            StorageUnit.StorageByOwner.put(myUUDI, tmpOwner);
            System.out.println("[Player Login: " + event.getPlayer().getName() + "]: New Storage Unit Database created for user");
        }
        for(StorageUnit key: tmpOwner)
        {
            if (key.getOwner().equals(myUUDI))
            {
                StorageUnit.checkPower(key);
            }
        }
        System.out.println("[Player Login: " + event.getPlayer().getName() + "]: checked " + tmpOwner.size() + " storage units, are now online!");

        if (RouterManager.routersByOwner.containsKey(myUUDI))
        {
            ItemRoutingRouter person = RouterManager.routersByOwner.get(myUUDI);
            if (person != null) {
                IRRUserRunnable tmp = new IRRUserRunnable();
                tmp.setItemRoutingRouter(person);
                tmp.startCountDown();
                int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, tmp, 1000, ConfigManager.getRouter_Speed());
                tmp.setTimerID(id);
                RouterManager.bufferListT.put(myUUDI, tmp);
                System.out.println("[Player Login: " + event.getPlayer().getName() + "]: Router found, id:" + id + " will start in 1 second.");
            }
            else
            {
                System.out.println("[Player Login: " + event.getPlayer().getName() + "]: Router found, loading error.");
            }
        }
        else
        {
            String key = RouterManager.getNewIDString();
            ItemRoutingRouter makingme = new ItemRoutingRouter(key);
            makingme.setOwner(event.getPlayer().getUniqueId());
            makingme.needSaving();
            RouterManager.routersByID.put(makingme.getID(), makingme);
            RouterManager.routersByOwner.put(makingme.getOwner(), makingme);
            System.out.println("[Player Login: " + event.getPlayer().getName() + "]: New Router Created");
            ItemRoutingRouter person = RouterManager.routersByOwner.get(myUUDI);
            if (person != null) {
                IRRUserRunnable tmp = new IRRUserRunnable();
                tmp.setItemRoutingRouter(person);
                tmp.startCountDown();
                int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, tmp, 1000, ConfigManager.getRouter_Speed());
                tmp.setTimerID(id);
                RouterManager.bufferListT.put(myUUDI, tmp);
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
        DataCenterGUI.removeDataCenter(myUUDI);
        System.out.println("[Player Quiting: " + event.getPlayer().getName() + "]: Data Center unload successful.");
        if (RouterManager.bufferListT.containsKey(myUUDI))
        {
            IRRUserRunnable tmp = RouterManager.bufferListT.get(myUUDI);
            if (tmp != null)
            {
                int id = tmp.getTimerID();
                Bukkit.getScheduler().cancelTask(id);
                System.out.println("[Player Quiting: " + event.getPlayer().getName() + "]: Router found, Thread stopped");
                RouterManager.bufferListT.remove(myUUDI);
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
    public void onArmorEquipEvent(ArmorEquipEvent event)
    {

        if (event.getType() == ArmorTypeEnum.HELMET) {
            Utilities.checkSFHelmet(event.getPlayer(), event.getNewArmorPiece());
        }
        PlayerManager playerManager = PlayerManager.getPlayer(event.getPlayer());
        if (Utilities.isEmpty(event.getNewArmorPiece()))
        {
            playerManager.removeArmor(event.getType());
        }
        else
        {
            playerManager.addArmor(event.getNewArmorPiece());
        }
        /*
        System.out.println("ArmorEquipEvent - " + event.getMethod());
        System.out.println("Type: " + event.getType());
        System.out.println("Slot: " + event.getType().getSlot());
        System.out.println("New: " + (event.getNewArmorPiece() != null ? event.getNewArmorPiece().getType() : "null"));
        System.out.println("Old: " + (event.getOldArmorPiece() != null ? event.getOldArmorPiece().getType() : "null"));
        */
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        try {
            InventoryView inventoryView = event.getView();
            if (inventoryView != null) {
                if (inventoryView.getTitle() != null) {
                    if (inventoryView.getTitle().equals("Mail Box")) {
                        if (event.getRawSlot() > -1 && event.getRawSlot() < 90) {
                            event.setCancelled(true);
                            MailboxGUI.clickEvent(event.getRawSlot(), (Player) event.getWhoClicked(), event);
                        }
                        return;
                    }
                    if (inventoryView.getTitle().equals("My Force Field List") || inventoryView.getTitle().equals("My Command List")) {
                        try {
                            event.setCancelled(true);
                            DataCenterGUI dataCenterGUI = DataCenterGUI.getDataCenter(event.getWhoClicked().getUniqueId());
                            dataCenterGUI.onInventoryClickEvent(event.getRawSlot(), event);
                            return;
                        } catch (Exception e) {

                        }
                    }
                    if (inventoryView.getTitle().equals("Pick A Player")) {
                        try {
                            event.setCancelled(true);
                            PickAPlayerGUI gui = TitanBox.instants.pickPlayer.get(event.getWhoClicked().getUniqueId());
                            gui.onInventoryClickEvent(event.getRawSlot(), event);
                            return;
                        } catch (Exception e) {

                        }
                    }
                }
            }

            StorageUnit.onInventoryClickEvent(event);

            MainModule.onInventoryClickEvent(event);

            RouterManager.onInventoryClickEvent(event);

            BackpackRecover.onInventoryClickEvent(event);

            StorageRecover.onInventoryClickEvent(event);

            NetworkMonitor.onInventoryClickEvent(event);

            if (!event.isCancelled()) {
                if (event.getRawSlot() == 2 && event.getWhoClicked() instanceof Player && event.getInventory().getType() == InventoryType.ANVIL) {
                    ItemStack check = event.getInventory().getContents()[0];
                    if (!Utilities.isEmpty(check)) {
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

package com.firesoftitan.play.titanbox.listeners;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.managers.ConfigManager;
import com.firesoftitan.play.titanbox.managers.NPCManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.PlayerProtectionManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import com.firesoftitan.play.titanbox.runnables.ChatGetterRunnable;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProtectionListener implements Listener {


    public ProtectionListener()
    {

    }
    public void registerEvents(){
        PluginManager pm = TitanBox.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanBox.instants);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDropEvent(PlayerDropItemEvent event){
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (Utilities.isDeathItem(itemStack))
        {
            event.getItemDrop().remove();
            return;
        }
        if (isPlayerProtected(event, event.getPlayer())) return;
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent event){
        ForceFieldManager ffH = WorldManager.getWorldHolder(event.getLocation().getWorld()).getFieldIn(event.getLocation());

        if (ffH != null)
        {
            if (ffH.isAdmin()) {
                event.setCancelled(true);
            }
        }

    }



    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeathEvent(PlayerDeathEvent event){
        RegisteredServiceProvider<Economy> rsp = TitanBox.instants.getServer().getServicesManager().getRegistration(Economy.class);
        Economy econ = rsp.getProvider();
        double toatal = econ.getBalance(event.getEntity());
        Random random = new Random(System.currentTimeMillis());
        int amound = random.nextInt(500);
        if (amound>toatal)
        {
            amound = (int) toatal;
            econ.withdrawPlayer(event.getEntity(), toatal);
        }
        else
        {
            econ.withdrawPlayer(event.getEntity(), amound);
        }
        amound = amound / 7;
        TitanBox.runCommands(event.getEntity(), amound, event.getEntity().getLocation(), ConfigManager.getPlayerDeathCommands());
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (TitanBox.instants.chatPlayer.containsKey(event.getPlayer().getUniqueId()))
        {
            event.setCancelled(true);
            ChatGetterRunnable chatGetterRunnable = TitanBox.instants.chatPlayer.get(event.getPlayer().getUniqueId());
            chatGetterRunnable.setChat(event.getMessage());
            chatGetterRunnable.runTask(TitanBox.instants);
            TitanBox.instants.chatPlayer.remove(event.getPlayer().getUniqueId());
            return;
        }
    }

    List<RegisteredListener> commandListers = new ArrayList<RegisteredListener>();
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){

        try {
            RegisteredListener[] allRL = event.getHandlers().getRegisteredListeners();
            boolean found = false;
            for(int i = 0; i < allRL.length; i++)
            {
                if (found)
                {
                    commandListers.add(allRL[i]);
                    event.getHandlers().unregister(allRL[i]);
                }
                if (allRL[i].getPlugin() == TitanBox.instants)
                {
                    found = true;
                }
            }

            if (event.getMessage().startsWith("/help") && !event.getPlayer().hasPermission("titanbox.admin"))
            {
                if (ConfigManager.isDefaultBlock()) {
                    String CommandsAre = "";
                    for (String command : ConfigManager.getDefaultCommands()) {
                        if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())) {
                            callUnregEvetns(event);
                            return;
                        }
                        CommandsAre = CommandsAre + command + ", ";
                    }
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.GRAY + "Commands Are:" + CommandsAre);
                    return;
                }
            }

            if (event.getMessage().startsWith("/help") && event.getPlayer().hasPermission("titanbox.admin"))
            {
                if (ConfigManager.isAdminBlock()) {
                    String CommandsAre = "";
                    for (String command : ConfigManager.getAdminCommands()) {
                        if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())) {
                            callUnregEvetns(event);
                            return;
                        }
                        CommandsAre = CommandsAre + command + ", ";
                    }
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.GRAY + "Commands Are:" + CommandsAre);
                    return;
                }
            }

            if (event.getMessage().startsWith("/help") && event.getPlayer().isOp())
            {
                if (ConfigManager.isOpBlock()) {
                    String CommandsAre = "";
                    for (String command : ConfigManager.getOpCommands()) {
                        if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())) {
                            callUnregEvetns(event);
                            return;
                        }
                        CommandsAre = CommandsAre + command + ", ";
                    }
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.GRAY + "Commands Are:" + CommandsAre);
                    return;
                }
            }


            if (TitanBox.bypassCommands.contains(event.getPlayer().getUniqueId()))
            {
                TitanBox.bypassCommands.remove(event.getPlayer().getUniqueId());
                callUnregEvetns(event);
                return;
            }




            for (String command : ConfigManager.getPlugingCommands()) {
                if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())) {
                    callUnregEvetns(event);
                    return;
                }
            }

            if (!event.getPlayer().hasPermission("titanbox.admin")) {
                if (ConfigManager.isDefaultBlock()) {
                    String CommandsAre = "";
                    for (String command : ConfigManager.getDefaultCommands()) {
                        if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())) {
                            callUnregEvetns(event);
                            return;
                        }
                        CommandsAre = CommandsAre + command + ", ";
                    }
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "Not Approved Command");
                    event.getPlayer().sendMessage(ChatColor.GRAY + "Commands Are:" + CommandsAre);
                    return;
                }
            }
            else if (!event.getPlayer().isOp())
            {
                if (ConfigManager.isAdminBlock()) {
                    String CommandsAre = "";
                    for (String command : ConfigManager.getAdminCommands()) {
                        if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())) {
                            callUnregEvetns(event);
                            return;
                        }
                        CommandsAre = CommandsAre + command + ", ";
                    }
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "Not Approved Admin Command");
                    event.getPlayer().sendMessage(ChatColor.GRAY + "Commands Are:" + CommandsAre);
                    return;
                }
            }
            else
            {
                if (ConfigManager.isOpBlock()) {
                    String CommandsAre = "";
                    for (String command : ConfigManager.getOpCommands()) {
                        if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())) {
                            callUnregEvetns(event);
                            return;
                        }
                        CommandsAre = CommandsAre + command + ", ";
                    }
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "Not Approved Op Command");
                    event.getPlayer().sendMessage(ChatColor.GRAY + "Commands Are:" + CommandsAre);
                    return;
                }
            }
            callUnregEvetns(event);

        } catch (IllegalArgumentException e) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "Server has no commands =(");
        }
    }

    private void callUnregEvetns(PlayerCommandPreprocessEvent event) {
        for (int i = 0; i < commandListers.size(); i++)
        {
            try {
                commandListers.get(i).callEvent(event);
            } catch (EventException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Location ofBlock = event.getBlockPlaced().getLocation();
        Player player = event.getPlayer();
        ItemStack inhand = event.getItemInHand().clone();
        if (!Utilities.isItemEqual(inhand, SlimefunItemsManager.FORCE_FIELD_BLUE)) {
            if (isPlayerProtected(event, player)) return;
        }
        if (isProtected(event, ofBlock, player)) return;

    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent event)
    {
        if (event.getEntity().getType() == EntityType.PLAYER)
        {
            Player player1 = (Player) event.getEntity();
            if (isPlayerProtected(event, player1)) return;
            WorldManager WH = WorldManager.getWorldHolder(player1.getLocation().getWorld());
            ForceFieldManager ffH = WH.getFieldIn(player1.getLocation());
            if (ffH != null)
            {
                if (ffH.isAdmin())
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeathEvent(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if (entity.getType() != EntityType.PLAYER)
        {
            WorldManager WH = WorldManager.getWorldHolder(entity.getLocation().getWorld());
            ForceFieldManager ffH = WH.getFieldIn(entity.getLocation());
            if (ffH != null)
            {
                if (ffH.isAdmin())
                {
                    event.setDroppedExp(0);
                    event.getDrops().clear();
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {


        if ((event.getDamager() instanceof  Player)) {
            Player player = (Player) event.getDamager();
            if (isPlayerProtected(event, player)) return;
        }
        if ((event.getEntity() instanceof  Player)) {
            Player player = (Player) event.getEntity();
            if (isPlayerProtected(event, player)) return;
        }
        if ((event.getDamager() instanceof  Player) && (event.getEntity() instanceof Player))
        {
            Player player1 = (Player) event.getDamager();
            Player player2 = (Player) event.getEntity();
            WorldManager WH = WorldManager.getWorldHolder(event.getEntity().getLocation().getWorld());
            if (WH.getFieldIn(player1.getLocation()) != null || WH.getFieldIn(player2.getLocation()) != null)
            {
                player1.sendMessage(ChatColor.RED + "Access Denied To Force Field Area!!");
                event.setCancelled(true);
                return;
            }
        }
        if (!isMonster(event.getEntity()) && !(event.getEntity() instanceof Player))
        {
            if (event.getDamager() instanceof  Player)
            {
                Player player1 = (Player) event.getDamager();
                if (isProtected(event, player1.getLocation(), player1)) return;
            }
            else
            {
                //This is for Monster attacking non monsert, like villager
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Location ofBlock = event.getBlock().getLocation();
        Player player = event.getPlayer();
        if (isPlayerProtected(event, player)) return;
        if (isProtected(event, ofBlock, player)) return;

    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        Location ofBlock = event.getRightClicked().getLocation();
        Player player = event.getPlayer();
        if (isPlayerProtected(event, player)) return;
        if (isProtected(event, ofBlock, player)) return;
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        Location ofBlock = event.getItem().getLocation();
        Player player = event.getPlayer();
        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
        if (ffH != null) {
            if (ffH.isAdmin()) return;
        }
        if (isProtected(event, ofBlock, player)) return;
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent event)
    {
        if (event.getCaught() != null) {
            Location ofBlock = event.getCaught().getLocation();
            Player player = event.getPlayer();
            if (isPlayerProtected(event, player)) return;
            if (isProtected(event, ofBlock, player)) return;
        }
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!isPlayerProtected(null, (Player) event.getWhoClicked())) {
            Inventory yours = event.getClickedInventory();
            if (yours != null) {
                for (int i = 0; i < yours.getSize(); i++) {
                    ItemStack itemStack = yours.getItem(i);
                    if (Utilities.isDeathItem(itemStack))
                    {
                        yours.setItem(i, null);
                    }
                }
            }
        }
    }
    private boolean isPlayerProtected(Cancellable event, Player player) {
        if (NPCManager.isNPC(player)) return false;
        PlayerProtectionManager playerProtectionManager = PlayerProtectionManager.getPlayer(player);
        if (playerProtectionManager.count() < 1)
        {
            if (event != null) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You must place a force field before you can do anything!!");
            }
            return true;
        }
        Inventory inventory = player.getInventory();
        for(int i = 0; i < 10; i++)
        {
            if (Utilities.isDeathItem(inventory.getItem(i)))
            {
                if (event != null) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You can't do anything as long as the Death Feather is in your hot bar");
                }
                return true;
            }

        }
        return false;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event)
    {
        Location ofBlock = event.getRightClicked().getLocation();
        Player player = event.getPlayer();
        if (isPlayerProtected(event, player)) return;
        if (isProtected(event, ofBlock, player)) return;
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBucketEmpty (PlayerBucketEmptyEvent event) {
        Location ofBlock = event.getBlockClicked().getLocation();
        Player player = event.getPlayer();
        if (isPlayerProtected(event, player)) return;
        if (isProtected(event, ofBlock, player)) return;
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBucketFill (PlayerBucketFillEvent event)
    {
        Location ofBlock = event.getBlockClicked().getLocation();
        Player player = event.getPlayer();
        if (isPlayerProtected(event, player)) return;
        if (isProtected(event, ofBlock, player)) return;
    }
    private void checkGoldenAxe(Player p, PlayerInteractEvent event) {
        ForceFieldManager ffH = WorldManager.getWorldHolder(p.getWorld()).getFieldIn(p.getLocation());
        if (ffH != null)
        {
            if (!ffH.hasRights(p.getUniqueId())) {
                if (p.hasPermission("buildersassistant.allowed"))
                {
                    if (p.getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE) {
                        p.sendMessage(ChatColor.RED + "Can't use Golden Axe Here.");
                        //p.getInventory().setItemInMainHand(null);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerInteract(PlayerInteractEvent event)
    {
        checkGoldenAxe(event.getPlayer(), event);
        Action action = event.getAction();
        Player player = event.getPlayer();
        ItemStack inhand = event.getItem();
        Block clickedBlock = event.getClickedBlock(); //null returned here means interacting with air
        if (clickedBlock == null) return;
        if (clickedBlock.getLocation() == null) return;

        if(action == Action.LEFT_CLICK_AIR)
        {
            if (clickedBlock.getType() == Material.CHEST || clickedBlock.getType() == Material.TRAPPED_CHEST)
            {
                if (isProtected(event, clickedBlock.getLocation(), player)) return;
            }
            return;
        }
        if(action == Action.PHYSICAL) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Utilities.isItemEqual(inhand, SlimefunItemsManager.FORCE_FIELD_BLUE))
        {

        }
        else {
            if (!event.getClickedBlock().getType().name().endsWith("_DOOR") && !event.getClickedBlock().getType().name().endsWith("_TRAPDOOR")) {
                if (isPlayerProtected(event, player)) return;
            }
        }
        Location ofBlock  = clickedBlock.getLocation();
        Material clickedBlockType = null;
        if(clickedBlock != null)
        {
            clickedBlockType = clickedBlock.getType();
        }
        else
        {
            clickedBlockType = Material.AIR;
        }

        if(	clickedBlock != null)
        {
          if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
          {
              boolean isHolder = clickedBlock.getState() instanceof InventoryHolder;
              if (isHolder || clickedBlockType == Material.CAULDRON || clickedBlockType == Material.JUKEBOX || clickedBlockType == Material.ANVIL || clickedBlockType == Material.CAKE)
              {
                  ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
                  if (ffH != null)
                  {
                      if (ffH.isAdmin())
                      {
                          if (clickedBlockType == Material.CHEST)
                          {
                              return;
                          }
                      }
                  }
                  if (isProtected(event, ofBlock, player)) return;
              }
          }
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onVehicleDamage (VehicleDamageEvent event)
    {
        Location ofBlock = event.getVehicle().getLocation();
        if (event.getAttacker() instanceof  Player) {
            Player player = (Player) event.getAttacker();
            if (isProtected(event, ofBlock, player)) return;
        }

    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPaintingPlace(HangingPlaceEvent event)
    {
        Location ofBlock = event.getEntity().getLocation();
        Player player = (Player) event.getPlayer();
        if (isPlayerProtected(event, player)) return;
        if (isProtected(event, ofBlock, player)) return;

    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onHangingBreak(HangingBreakEvent event)
    {
        Location ofBlock = event.getEntity().getLocation();
        if (event instanceof HangingBreakByEntityEvent)
        {
        HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent)event;
            if (entityEvent.getRemover() instanceof  Player) {
                Player player = (Player) entityEvent.getRemover();
                if (isPlayerProtected(event, player)) return;
                if (isProtected(event, ofBlock, player)) return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityPickup(EntityChangeBlockEvent event)
    {
        Location ofBlock = event.getEntity().getLocation();
        if (isInField(event, ofBlock)) return;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityInteract(EntityInteractEvent event)
    {
        Material material = event.getBlock().getType();
        if(material == Material.FARMLAND)
        {
            event.setCancelled(true);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onZombieBreakDoor(EntityBreakDoorEvent event)
    {
        Location ofBlock = event.getBlock().getLocation();
        if (isInField(event, ofBlock)) return;
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        ForceFieldManager ffH = WorldManager.getWorldHolder(e.getLocation().getWorld()).getFieldIn(e.getLocation());
        if (ffH != null)
        {
            if (ffH.isAdmin() || e.getEntityType() == EntityType.CREEPER) {
                e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getEntity().getLocation(), 0);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityChangeBLock(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.ENDERMAN) {
            event.setCancelled(true);
        } else if (event.getEntityType() == EntityType.SILVERFISH) {
            event.setCancelled(true);
        } else if (event.getEntityType() == EntityType.RABBIT) {
            event.setCancelled(true);
        }

        //don't allow the wither to break blocks, when the wither is determined, too expensive to constantly check for claimed blocks
        else if (event.getEntityType() == EntityType.WITHER) {
            event.setCancelled(true);
        }

        //don't allow crops to be trampled, except by a player with build permission
        else if (event.getTo() == Material.DIRT && event.getBlock().getType() == Material.FARMLAND) {
            if (event.getEntityType() != EntityType.PLAYER) {
                event.setCancelled(true);
            } else {
                Player player = (Player) event.getEntity();
                Block block = event.getBlock();
                if (isProtected(event, block.getLocation(), player)) return;
            }
        }
        else if (event.getEntityType() == EntityType.FALLING_BLOCK)
        {

            //sand cannon ??
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityFormBlock(EntityBlockFormEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            Player player = (Player) entity;
            if (isProtected(event, event.getBlock().getLocation(), player)) return;
        }
    }

    private boolean isMonster(Entity entity)
    {
        if(entity instanceof Monster) return true;

        EntityType type = entity.getType();
        if(type == EntityType.GHAST || type == EntityType.MAGMA_CUBE || type == EntityType.SHULKER || type == EntityType.POLAR_BEAR) return true;

        if(type == EntityType.RABBIT)
        {
            Rabbit rabbit = (Rabbit)entity;
            if(rabbit.getRabbitType() == Rabbit.Type.THE_KILLER_BUNNY) return true;
        }

        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockSpread (BlockSpreadEvent event)
    {
        event.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgnite (BlockIgniteEvent igniteEvent)
    {
        if (igniteEvent.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL &&  igniteEvent.getCause() != BlockIgniteEvent.IgniteCause.LIGHTNING)
        {
            igniteEvent.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockFromTo (BlockFromToEvent spreadEvent)
    {
        if (spreadEvent.getToBlock() == null) return;
        if (spreadEvent.getBlock() == null) return;
        if (spreadEvent.getToBlock().getType() == Material.PLAYER_WALL_HEAD || spreadEvent.getToBlock().getType() == Material.PLAYER_HEAD)
        {
            //slimefun head glitch fix, from water breaking heads
            spreadEvent.setCancelled(true);
            return;
        }
        WorldManager WH = WorldManager.getWorldHolder(spreadEvent.getToBlock().getLocation().getWorld());
        ForceFieldManager ffHTo = WH.getFieldIn(spreadEvent.getToBlock().getLocation());
        ForceFieldManager ffHfrom = WH.getFieldIn(spreadEvent.getBlock().getLocation());
        if (ffHfrom != null && ffHTo == null) return;
        if (ffHfrom == null && ffHTo != null)
        {
            spreadEvent.setCancelled(true);
            return;
        }
        if (ffHfrom == null || ffHTo == null) return;

        if (!Utilities.encode(ffHfrom.getLocation()).equals(Utilities.encode(ffHTo.getLocation())))
        {
            spreadEvent.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPistonExtend (BlockPistonExtendEvent event)
    {
        if (event.getDirection() == BlockFace.DOWN || event.getDirection() == BlockFace.UP) return;
        WorldManager WH = WorldManager.getWorldHolder(event.getBlock().getLocation().getWorld());
        if (WH.getFieldIn(event.getBlock().getLocation()) == null) {
            for (Block movedBlock : event.getBlocks()) {
                if (WH.getFieldIn(movedBlock.getLocation()) !=null)
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPistonRetract (BlockPistonRetractEvent event)
    {
        if (event.getDirection() == BlockFace.DOWN || event.getDirection() == BlockFace.UP) return;
        WorldManager WH = WorldManager.getWorldHolder(event.getBlock().getLocation().getWorld());
        if (WH.getFieldIn(event.getBlock().getLocation()) == null) {
            for (Block movedBlock : event.getBlocks()) {
                if (WH.getFieldIn(movedBlock.getLocation()) !=null)
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }

    private boolean isInField(Cancellable event, Location ofBlock) {
        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
        if (ffH != null)
        {
            event.setCancelled(true);
            return true;
        }
        return false;
    }
    private boolean isProtected(Cancellable event, Location ofBlock, Player player) {
        if (player == null) return false;
        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
        if (ffH != null)
        {
            if (!ffH.hasRights(player.getUniqueId()))
            {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Access Denied To Force Field Area!!");
                return true;
            }
        }
        return false;
    }

}

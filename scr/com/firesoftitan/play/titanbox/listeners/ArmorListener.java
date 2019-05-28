package com.firesoftitan.play.titanbox.listeners;


import com.firesoftitan.play.titanbox.Events.ArmorEquipEvent;
import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ArmorTypeEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Jul 30, 2015 6:43:34 PM
 */
public class ArmorListener implements Listener{

    private final List<String> blockedMaterials;

    public ArmorListener(){
        this.blockedMaterials = new ArrayList<String>();
        this.blockedMaterials.add("FURNACE");
        this.blockedMaterials.add("CHEST");
        this.blockedMaterials.add("TRAPPED_CHEST");
        this.blockedMaterials.add("BEACON");
        this.blockedMaterials.add("DISPENSER");
        this.blockedMaterials.add("DROPPER");
        this.blockedMaterials.add("HOPPER");
        this.blockedMaterials.add("WORKBENCH");
        this.blockedMaterials.add("ENCHANTMENT_TABLE");
        this.blockedMaterials.add("ENDER_CHEST");
        this.blockedMaterials.add("ANVIL");
        this.blockedMaterials.add("BED_BLOCK");
        this.blockedMaterials.add("FENCE_GATE");
        this.blockedMaterials.add("SPRUCE_FENCE_GATE");
        this.blockedMaterials.add("BIRCH_FENCE_GATE");
        this.blockedMaterials.add("ACACIA_FENCE_GATE");
        this.blockedMaterials.add("JUNGLE_FENCE_GATE");
        this.blockedMaterials.add("DARK_OAK_FENCE_GATE");
        this.blockedMaterials.add("IRON_DOOR_BLOCK");
        this.blockedMaterials.add("WOODEN_DOOR");
        this.blockedMaterials.add("SPRUCE_DOOR");
        this.blockedMaterials.add("BIRCH_DOOR");
        this.blockedMaterials.add("JUNGLE_DOOR");
        this.blockedMaterials.add("ACACIA_DOOR");
        this.blockedMaterials.add("DARK_OAK_DOOR");
        this.blockedMaterials.add("WOOD_BUTTON");
        this.blockedMaterials.add("STONE_BUTTON");
        this.blockedMaterials.add("TRAP_DOOR");
        this.blockedMaterials.add("IRON_TRAPDOOR");
        this.blockedMaterials.add("DIODE_BLOCK_OFF");
        this.blockedMaterials.add("DIODE_BLOCK_ON");
        this.blockedMaterials.add("REDSTONE_COMPARATOR_OFF");
        this.blockedMaterials.add("REDSTONE_COMPARATOR_ON");
        this.blockedMaterials.add("FENCE");
        this.blockedMaterials.add("SPRUCE_FENCE");
        this.blockedMaterials.add("BIRCH_FENCE");
        this.blockedMaterials.add("JUNGLE_FENCE");
        this.blockedMaterials.add("DARK_OAK_FENCE");
        this.blockedMaterials.add("ACACIA_FENCE");
        this.blockedMaterials.add("NETHER_FENCE");
        this.blockedMaterials.add("BREWING_STAND");
        this.blockedMaterials.add("CAULDRON");
        this.blockedMaterials.add("SIGN_POST");
        this.blockedMaterials.add("WALL_SIGN");
        this.blockedMaterials.add("SIGN");
        this.blockedMaterials.add("LEVER");
        this.blockedMaterials.add("BLACK_SHULKER_BOX");
        this.blockedMaterials.add("BLUE_SHULKER_BOX");
        this.blockedMaterials.add("BROWN_SHULKER_BOX");
        this.blockedMaterials.add("CYAN_SHULKER_BOX");
        this.blockedMaterials.add("GRAY_SHULKER_BOX");
        this.blockedMaterials.add("GREEN_SHULKER_BOX");
        this.blockedMaterials.add("LIGHT_BLUE_SHULKER_BOX");
        this.blockedMaterials.add("LIME_SHULKER_BOX");
        this.blockedMaterials.add("MAGENTA_SHULKER_BOX");
        this.blockedMaterials.add("ORANGE_SHULKER_BOX");
        this.blockedMaterials.add("PINK_SHULKER_BOX");
        this.blockedMaterials.add("PURPLE_SHULKER_BOX");
        this.blockedMaterials.add("RED_SHULKER_BOX");
        this.blockedMaterials.add("SILVER_SHULKER_BOX");
        this.blockedMaterials.add("WHITE_SHULKER_BOX");
        this.blockedMaterials.add("YELLOW_SHULKER_BOX");
        this.blockedMaterials.add("DAYLIGHT_DETECTOR_INVERTED");
        this.blockedMaterials.add("DAYLIGHT_DETECTOR");
    }
    public void registerEvents(){
        PluginManager pm = TitanBox.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanBox.instants);
    }
    @EventHandler
    public final void onInventoryClick(final InventoryClickEvent e){
        boolean shift = false, numberkey = false;
        if(e.isCancelled()) return;
        if(e.getAction() == InventoryAction.NOTHING) return;// Why does this get called if nothing happens??
        if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)){
            shift = true;
        }
        if(e.getClick().equals(ClickType.NUMBER_KEY)){
            numberkey = true;
        }
        if(e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR && e.getSlotType() != SlotType.CONTAINER) return;
        if(e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        ArmorTypeEnum newArmorTypeEnum = ArmorTypeEnum.matchType(shift ? e.getCurrentItem() : e.getCursor());
        if(!shift && newArmorTypeEnum != null && e.getRawSlot() != newArmorTypeEnum.getSlot()){
            // Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
            return;
        }
        if(shift){
            newArmorTypeEnum = ArmorTypeEnum.matchType(e.getCurrentItem());
            if(newArmorTypeEnum != null){
                boolean equipping = true;
                if(e.getRawSlot() == newArmorTypeEnum.getSlot()){
                    equipping = false;
                }
                if(newArmorTypeEnum.equals(ArmorTypeEnum.HELMET) && (equipping ? isAirOrNull(e.getWhoClicked().getInventory().getHelmet()) : !isAirOrNull(e.getWhoClicked().getInventory().getHelmet())) || newArmorTypeEnum.equals(ArmorTypeEnum.CHESTPLATE) && (equipping ? isAirOrNull(e.getWhoClicked().getInventory().getChestplate()) : !isAirOrNull(e.getWhoClicked().getInventory().getChestplate())) || newArmorTypeEnum.equals(ArmorTypeEnum.LEGGINGS) && (equipping ? isAirOrNull(e.getWhoClicked().getInventory().getLeggings()) : !isAirOrNull(e.getWhoClicked().getInventory().getLeggings())) || newArmorTypeEnum.equals(ArmorTypeEnum.BOOTS) && (equipping ? isAirOrNull(e.getWhoClicked().getInventory().getBoots()) : !isAirOrNull(e.getWhoClicked().getInventory().getBoots()))){
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorTypeEnum, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    if(armorEquipEvent.isCancelled()){
                        e.setCancelled(true);
                    }
                }
            }
        }else{
            ItemStack newArmorPiece = e.getCursor();
            ItemStack oldArmorPiece = e.getCurrentItem();
            if(numberkey){
                if(e.getClickedInventory().getType().equals(InventoryType.PLAYER)){// Prevents stuff in the 2by2 crafting
                    // e.getClickedInventory() == The players inventory
                    // e.getHotBarButton() == key people are pressing to equip or unequip the item to or from.
                    // e.getRawSlot() == The slot the item is going to.
                    // e.getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
                    ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                    if(!isAirOrNull(hotbarItem)){// Equipping
                        newArmorTypeEnum = ArmorTypeEnum.matchType(hotbarItem);
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                    }else{// Unequipping
                        newArmorTypeEnum = ArmorTypeEnum.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
                    }
                }
            }else{
                if(isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem())){// unequip with no new item going into the slot.
                    newArmorTypeEnum = ArmorTypeEnum.matchType(e.getCurrentItem());
                }
                // e.getCurrentItem() == Unequip
                // e.getCursor() == Equip
                // newArmorTypeEnum = ArmorTypeEnum.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
            }
            if(newArmorTypeEnum != null && e.getRawSlot() == newArmorTypeEnum.getSlot()){
                ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
                if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), method, newArmorTypeEnum, oldArmorPiece, newArmorPiece);
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if(armorEquipEvent.isCancelled()){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.getAction() == Action.PHYSICAL) return;
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            final Player player = e.getPlayer();
            if(e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK){// Having both of these checks is useless, might as well do it though.
                // Some blocks have actions when you right click them which stops the client from equipping the armor in hand.
                Material mat = e.getClickedBlock().getType();
                for(String s : blockedMaterials){
                    if(mat.name().equalsIgnoreCase(s)) return;
                }
            }
            ArmorTypeEnum newArmorTypeEnum = ArmorTypeEnum.matchType(e.getItem());
            if(newArmorTypeEnum != null){
                if(newArmorTypeEnum.equals(ArmorTypeEnum.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet()) || newArmorTypeEnum.equals(ArmorTypeEnum.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate()) || newArmorTypeEnum.equals(ArmorTypeEnum.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings()) || newArmorTypeEnum.equals(ArmorTypeEnum.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots())){
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorTypeEnum.matchType(e.getItem()), null, e.getItem());
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    if(armorEquipEvent.isCancelled()){
                        e.setCancelled(true);
                        player.updateInventory();
                    }
                }
            }
        }
    }
    @EventHandler
    public void playerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        new BukkitRunnable() {
            @Override
            public void run() {
                sendLoginArmor(event, inventory.getHelmet());
                sendLoginArmor(event, inventory.getChestplate());
                sendLoginArmor(event, inventory.getLeggings());
                sendLoginArmor(event, inventory.getBoots());
            }
        }.runTaskLater(TitanBox.instants, 20);

    }

    private void sendLoginArmor(PlayerLoginEvent event, ItemStack armorStuff) {
        ArmorTypeEnum type = ArmorTypeEnum.matchType(armorStuff);
        ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getPlayer(), ArmorEquipEvent.EquipMethod.LOGIN, type, null, armorStuff);
        Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
    }

    @EventHandler
    public void inventoryDrag(InventoryDragEvent event){
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorTypeEnum slot
        // Can't replace armor using this method making getCursor() useless.
        ArmorTypeEnum type = ArmorTypeEnum.matchType(event.getOldCursor());
        if(event.getRawSlots().isEmpty()) return;// Idk if this will ever happen
        if(type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)){
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                event.setResult(Result.DENY);
                event.setCancelled(true);
            }
        }
        // Debug stuff
		/*System.out.println("Slots: " + event.getInventorySlots().toString());
		System.out.println("Raw Slots: " + event.getRawSlots().toString());
		if(event.getCursor() != null){
			System.out.println("Cursor: " + event.getCursor().getType().name());
		}
		if(event.getOldCursor() != null){
			System.out.println("OldCursor: " + event.getOldCursor().getType().name());
		}
		System.out.println("Type: " + event.getType().name());*/
    }

    @EventHandler
    public void itemBreakEvent(PlayerItemBreakEvent e){
        ArmorTypeEnum type = ArmorTypeEnum.matchType(e.getBrokenItem());
        if(type != null){
            Player p = e.getPlayer();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                ItemStack i = e.getBrokenItem().clone();
                i.setAmount(1);
                i.setDurability((short) (i.getDurability() - 1));
                if(type.equals(ArmorTypeEnum.HELMET)){
                    p.getInventory().setHelmet(i);
                }else if(type.equals(ArmorTypeEnum.CHESTPLATE)){
                    p.getInventory().setChestplate(i);
                }else if(type.equals(ArmorTypeEnum.LEGGINGS)){
                    p.getInventory().setLeggings(i);
                }else if(type.equals(ArmorTypeEnum.BOOTS)){
                    p.getInventory().setBoots(i);
                }
            }
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e){
        Player p = e.getEntity();
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(!isAirOrNull(i)){
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorTypeEnum.matchType(i), i, null));
                // No way to cancel a death event.
            }
        }
    }

    private Location shift(Location start, BlockFace direction, int multiplier){
        if(multiplier == 0) return start;
        return new Location(start.getWorld(), start.getX() + direction.getModX() * multiplier, start.getY() + direction.getModY() * multiplier, start.getZ() + direction.getModZ() * multiplier);
    }

    /**
     * A utility method to support versions that use null or air ItemStacks.
     */
    private boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().equals(Material.AIR);
    }
}

package com.firesoftitan.play.titanbox;

import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.holders.ItemHolder;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import com.firesoftitan.play.titanbox.listeners.ListenerMain;
import com.firesoftitan.play.titanbox.machines.*;
import com.firesoftitan.play.titanbox.modules.InventoryModule;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.MainRunnable;
import com.firesoftitan.play.titanbox.runnables.RouterRunable;
import com.firesoftitan.play.titanbox.runnables.SaverRunable;
import com.firesoftitan.play.titanbox.shops.MainShops;
import com.firesoftitan.play.titanbox.shops.PlayerShopHolder;
import com.firesoftitan.play.titanbox.shops.ShopHolder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.NumberFormat;
import java.util.*;

public class TitanBox extends JavaPlugin
{
    public static SlimefunSetup setup;
    public static TitanBox instants;
    public static ListenerMain listen;
    public static Config config = new Config("plugins" + File.separator + "TitanBox" + File.separator  + "config.yml");
    public static Config barcodes = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "barcodes.yml");
    public Economy economy;
    public static long cost = 10000;
    public HashMap<String, EntityPlayer> npcs;
    private static final NavigableMap<Long, String> suffixes = new TreeMap<> ();
    private static HashMap<EntityType, String> bToMConversion;
    private RouterRunable RouterTimer = new RouterRunable();
    private SaverRunable SaverTimer = new SaverRunable();
    public MainShops mainShops;
    public static long isRunning = 0;
        static {
        bToMConversion = new HashMap<EntityType, String> ();
        bToMConversion.put(EntityType.MUSHROOM_COW, "mooshroom");
        bToMConversion.put(EntityType.PIG_ZOMBIE, "zombie_pigman");
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
    public static String formatCommas(Long value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(value);
        return numberAsString;
    }
    public static String formatCommas(double value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(value);
        return numberAsString;
    }
    public static String convertToTimePasted(long lastping)
    {
        long last = System.currentTimeMillis() - lastping;
        String time = " Seconds";
        last = last / 1000;
        if (last > 60)
        {
            time = " Minutes";
            last = last / 60;
            if (last > 60)
            {
                time = " Hours";
                last = last / 60;
            }
        }
        return last + time;
    }
    public void onEnable()
    {
        instants = this;

        listen = new ListenerMain();
        listen.registerEvents();
        loadConfig();

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        npcs = new HashMap<String, EntityPlayer>();
        for (World world: Bukkit.getWorlds())
        {
            WorldServer nmsWorld = ((CraftWorld)world).getHandle();
            EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.randomUUID(), "TB_NPC_" + world.getName()), new PlayerInteractManager(nmsWorld));

            npc.setLocation(world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ(), 0, 0);
            CraftPlayer opCr = npc.getBukkitEntity();
            opCr.setGameMode(GameMode.SURVIVAL);
            opCr.getPlayer().setGameMode(GameMode.SURVIVAL);
            opCr.getHandle().playerInteractManager.setGameMode(EnumGamemode.SURVIVAL);

            PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(opCr.getUniqueId());
            playerData.ignoreClaims = true;

            npcs.put(world.getName(), npc);
        };

        setup = new SlimefunSetup();

        mainShops  = new MainShops();

        InventoryModule.loadConfig();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MainRunnable(),25, 25);


    }
    public static ItemStack getHead(String Texture)
    {
        try
        {
            return CustomSkull.getItem(Texture);
        }catch (Exception e)
        {
            return null;
        }
    }

    public boolean checkforTitanStone(ItemStack itemA)
    {
        if (itemA != null) {
            if (itemA.getItemMeta() != null) {
                if (itemA.getItemMeta().hasDisplayName() && itemA.getItemMeta().hasLore()) {
                    if (itemA.getItemMeta().getDisplayName().equals(SlimefunItemsHolder.TitanStone.getItemMeta().getDisplayName())) {
                        if (TitanBox.equalsLore(itemA.getItemMeta().getLore(), SlimefunItemsHolder.TitanStone.getItemMeta().getLore())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static void placeSkull(Block block, String skulltexture) {
        // maybe one day Bukkit will have a block set method which takes a MaterialData
        try {
            if (block.getLocation().equals(block.getLocation())) {
                if (CustomSkull.getItem(skulltexture) != null) {
                    try {
                        if (block.getType() != Material.PLAYER_HEAD) {
                            block.setType(Material.PLAYER_HEAD);
                        }
                        /*if (block.getData() != (byte) 0x1) {
                            block.setData((byte) 0x1); // Floor
                        }*/
                        Skull skullE = ((Skull) block.getState());
                        skullE.getData().setData((byte) 0x1);
                        /*
                        if (skullE.getRotation() != BlockFace.NORTH) {
                            skullE.setRotation(BlockFace.NORTH);
                            skullE.update();
                        }
*/
                        CustomSkull.setSkull(block, skulltexture);


                    } catch (Exception e) {
  //                      MaterialData md = new MaterialData(Material.BOOK);

//                        block.setTypeIdAndData(getMaterialData().getItemTypeId(), getMaterialData().getData(), true);
                    }

                } else {
                    //block.setTypeIdAndData(getMaterialData().getItemTypeId(), getMaterialData().getData(), true);
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    public static GameProfile getNonPlayerProfile(String skinURL) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinURL).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));


        //GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        //newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + "http://textures.minecraft.net/texture/" + skinURL + "\"}}}")));
        //newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:[{Value:\"" + skinURL + "\"}]}")));
        //return newSkinProfile;
        return profile;
    }
    private void loadConfig()
    {

        EnergyNet.registerComponent("FOTStorageUnti", EnergyNet.NetworkComponent.CONSUMER);

        StorageUnit.loadStorage();

//        MainModule.loadAllModules();

        //RouterHolder.loadAllRouters();

        Elevator.loadAllElevators();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, SaverTimer, 12000, 12000);

        //Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, RouterTimer, 200, RouterHolder.speed);

    }
    public List<Entity>  getNearbyEntities(Location loc)
    {
        return getNearbyEntities(loc, 5);
    }
    public List<Entity>  getNearbyEntities(Location loc, int rad)
    {
        EntityPlayer npc = TitanBox.instants.npcs.get(loc.getWorld().getName());
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        List<Entity> nearEntity = opCr.getNearbyEntities(rad,rad,rad);
        return nearEntity;
    }
    public static ItemStack addItemsToPlayer(Player player, ItemStack placing)
    {
        placing = placing.clone();
        List<Integer> emptySlos = new ArrayList<Integer>();
        Inventory playersInv = player.getInventory();
        for (int i = 0; i < 36; i++) {
            ItemStack checkItem = playersInv.getItem(i);
            if (TitanBox.isEmpty(checkItem))
            {
                emptySlos.add(i); //this slot is empty lets keep that in mind for later
            }
            else
            {
                checkItem = checkItem.clone();
                if (TitanBox.isItemEqual(checkItem, placing))// is this the same thing
                {
                    if (checkItem.getAmount() < checkItem.getMaxStackSize()) //is there room in this stack for more
                    {
                        int placeAmount = Math.min(checkItem.getMaxStackSize() - checkItem.getAmount(), placing.getAmount()); //how much more can it hold
                        checkItem.setAmount(checkItem.getAmount() + placeAmount); //uppdate the item
                        playersInv.setItem(i, checkItem.clone());//place the item back
                        if (placing.getAmount() - placeAmount <= 0) //is there any left over we need to place
                        {
                            return null; //we are done
                        }
                        placing.setAmount(placing.getAmount() - placeAmount); //some was left, lets keep looking
                    }
                }
            }

        }
        //we finished looking for more room now lets fill in the empty slots
        for (Integer slot: emptySlos)
        {
            int howMuch = Math.min(placing.getMaxStackSize(), placing.getAmount());
            ItemStack itemStack = placing.clone();
            itemStack.setAmount(howMuch);
            playersInv.setItem(slot, itemStack.clone());
            if (placing.getAmount() - howMuch <= 0) //is there any left over we need to place
            {
                return null; //we are done
            }
            placing.setAmount(placing.getAmount() - howMuch); //some was left, lets keep looking
        }

        return placing.clone(); // didn't have enough room, sorry

    }
    public void checkRegisterdPower()
    {
        if (EnergyNet.getComponent("FOTStorageUnti") == null)
        {
            EnergyNet.registerComponent("FOTStorageUnti", EnergyNet.NetworkComponent.CONSUMER);
        }
    }

    private void setDefault(String name) {


    }
    public static ItemStack getItemStackFromBlock(Location block)
    {
        return getItemStackFromBlock(block.getBlock());
    }
    public static ItemStack getItemStackFromBlock(Block block)
    {
        ItemStack test = block.getState().getData().toItemStack(1);
        return test.clone();
    }
    public void saveEveryThing()
    {
        StorageUnit.saveStorage();
        Pumps.savePumps();
        Elevator.saveElevators();
        BackpackRecover.saveRecovers();
        barcodes.save();
        config.save();
        ElectricMiner.miners.save();
    }

    public void onDisable()
    {
        saveEveryThing();
    }

    public static boolean checkStorageForItem(UUID owner,ItemStack toInsert)
    {
        toInsert = toInsert.clone();
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for (int i =0; i < stH.getSize(); i++) {
                    ItemStack stack = stH.viewSlot(i);
                    if (TitanBox.isItemEqual(toInsert, stack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ItemStack addItemToStorage(UUID owner, Material toInsert, int giveamount)
    {
        return addItemToStorage(owner,toInsert, giveamount, (short) 0);
    }
    public static ItemStack addItemToStorage(UUID owner, Material toInsert, int giveamount, short meta)
    {
        return addItemToStorage(owner, new ItemStack(toInsert, giveamount, (short)meta));
    }
    public static ItemStack addItemToStorage(UUID owner, ItemStack toInsert)
    {
        toInsert = toInsert.clone();
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                ItemStack out = stH.insertItem(toInsert);
                if (TitanBox.isEmpty(out))
                {
                    return null;
                }
                if (out.getAmount() < toInsert.getAmount())
                {
                    return out.clone();
                }
            }
        }
        return toInsert.clone();
    }
    public static void pickupItem(UUID owner, Location checkSapling, Material mat, int size) {
        Entity pickupBat = null;
        try {
            pickupBat = checkSapling.getWorld().spawnEntity(checkSapling.add(0 , -2, 0), EntityType.BAT);
            List<Entity> listnear = pickupBat.getNearbyEntities(size, size, size);
            for (Entity e : listnear) {
                if (!e.isDead()) {
                    if (e.getType() == EntityType.DROPPED_ITEM) {
                        ItemStack dropped = ((Item) e).getItemStack().clone();
                        if (mat == null)
                        {
                            ItemStack pickup = TitanBox.addItemToStorage(owner, dropped);
                            if (pickup == null)
                            {
                                e.remove();
                            }
                            else {
                                ((Item) e).setItemStack(pickup);
                            }
                        }
                        else {
                            if (dropped.getType() == mat) {
                                TitanBox.addItemToStorage(owner, dropped);
                                e.remove();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
        finally {
            if (pickupBat != null) {
                pickupBat.remove();
            }
        }
    }
    public static ItemStack getItem(UUID owner, Material typeBucket, short data) {
        return getItem(owner, new ItemStack(typeBucket, 1, data));
    }
    public static ItemStack getItem(UUID owner, Material typeBucket) {
        return getItem(owner, typeBucket, (short) 0);
    }
    public static ItemStack getItem(UUID owner, ItemStack typeBucket) {
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!TitanBox.isEmpty(view))
                    {
                        if (TitanBox.isItemEqual(view, typeBucket))
                        {
                            ItemStack getIt = stH.getItem(i, view.getMaxStackSize());
                            if (!TitanBox.isEmpty(getIt))
                            {
                                return  getIt;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    public static long getItemCount(UUID owner, ItemStack typeBucket) {
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!TitanBox.isEmpty(view))
                    {
                        if (TitanBox.isItemEqual(view, typeBucket))
                        {
                            return stH.getStorageCount(i);
                        }
                    }
                }
            }
        }
        return -1;
    }
    public static ItemStack getItemByPassPower(UUID owner, ItemStack typeBucket, int amount) {
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!TitanBox.isEmpty(view))
                    {
                        if (TitanBox.isItemEqual(view, typeBucket))
                        {
                            ItemStack getIt = stH.getItem(i,amount, true);
                            if (!TitanBox.isEmpty(getIt))
                            {
                                return  getIt;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    public static boolean hasItem(UUID owner, Material typeBucket, short data) {
        return hasItem(owner, new ItemStack(typeBucket, 1, data));
    }
    public static boolean hasItem(UUID owner, Material typeBucket) {
        return hasItem(owner, typeBucket, (short) 0);
    }
    public static boolean hasItem(UUID owner, ItemStack typeBucket) {
        return hasItem(owner, typeBucket, true);
    }
    public static boolean hasItem(UUID owner, ItemStack typeBucket, boolean useItem) {
        boolean foundbucket = false;
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!TitanBox.isEmpty(view))
                    {
                        if (TitanBox.isItemEqual(view, typeBucket))
                        {
                            if (useItem) {
                                ItemStack getIt = stH.getItem(i, 1);
                                if (!TitanBox.isEmpty(getIt)) {
                                    foundbucket = true;
                                    break;
                                }
                            }
                            else
                            {
                                foundbucket = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return foundbucket;
    }

    /**
     * Check if the given block contains a vanilla inventory,
     *
     * @param target the block to check
     * @return true if this block holds a vanilla inventory; false otherwise
     */
    public static boolean isVanillaInventory(Block target) {
        switch (target.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
            case DISPENSER:
            case HOPPER:
            case DROPPER:
            case FURNACE:
            case BREWING_STAND:
            //case BURNING_FURNACE:
                return true;
            default:
                return false;
        }
    }
    public static boolean isInventory(Block clicked)
    {
        BlockStorage storage =  BlockStorage.getStorage(clicked.getLocation().getWorld());

        if (storage.hasUniversalInventory(clicked))
        {
            return true;

        } else if (storage.hasInventory(clicked.getLocation()))
        {
            return true;

        } else if (TitanBox.isVanillaInventory(clicked))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * Get the vanilla inventory for the given block.
     *
     * @param target the block containing the target inventory
     * @return the block's inventory, or null if the block does not have one
     */
    public static Inventory getVanillaInventoryFor(Block target) {
        Chest c;
        switch (target.getType()) {
            case TRAPPED_CHEST:
            case CHEST:
                c = (Chest) target.getState();
                if (c.getInventory().getHolder() instanceof DoubleChest) {
                    DoubleChest dc = (DoubleChest) c.getInventory().getHolder();
                    return dc.getInventory();
                } else {
                    return c.getBlockInventory();
                }
            case DISPENSER:
            case HOPPER:
            case DROPPER:
            case FURNACE:
            case BREWING_STAND:
            //case BURNING_FURNACE:
                return ((InventoryHolder) target.getState()).getInventory();
            // any other vanilla inventory types ?
            default:
                return null;
        }
    }

    public static List<ItemStack> loadListItemStack(Config config, String key)
    {
        List<ItemStack> goingOut = new ArrayList<ItemStack>();
        try {
            List<?> items = config.getConfiguration().getList(key);
            for (Object obj: items)
            {
                if (obj instanceof  ItemStack)
                {
                    goingOut.add((ItemStack)obj);
                }
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }
        return  goingOut;
    }
    public static ItemStack changeName(ItemStack toAdd, String Name) {
        ItemMeta IM = toAdd.getItemMeta();
        IM.setDisplayName(Name);
        toAdd.setItemMeta(IM);
        return toAdd.clone();
    }

    public static int getLoreSize(ItemStack toAdd)
    {
        ItemMeta ITM = null;
        if (toAdd.hasItemMeta()) {
            ITM = toAdd.getItemMeta();
            if (ITM.hasLore()) {
                return ITM.getLore().size();
            }
        }
        return -1;
    }
    public static boolean isEmpty(ItemStack toCheck)
    {
        if (toCheck == null)
        {
            return  true;
        }
        if (toCheck.getType().equals(Material.AIR))
        {
            return  true;
        }
        if (toCheck.getAmount() < 1)
        {
            return  true;
        }
        return false;
    }
    public static ItemStack removeLore(ItemStack toAdd, int line)
    {
        ItemMeta ITM = null;
        if (toAdd.hasItemMeta()) {
            ITM = toAdd.getItemMeta();
            if (ITM.hasLore()) {
                List<String> lore = ITM.getLore();
                lore.remove(line);
                ITM.setLore(lore);
                toAdd.setItemMeta(ITM.clone());
                return toAdd;
            }
        }
        return toAdd.clone();
    }
    public static ItemStack addLore(boolean clear, ItemStack toAdd, List<String> lore)
    {

        ItemMeta ITM = null;
        if (toAdd.hasItemMeta()) {
            ITM = toAdd.getItemMeta();
            if (!clear)
            {
                if (ITM.hasLore())
                {
                    List<String> lore2 = new ArrayList<String>();
                    lore2.addAll(ITM.getLore());
                    lore2.addAll(lore);
                    lore.clear();
                    lore = lore2;
                }
            }
            ITM.setLore(lore);
            toAdd.setItemMeta(ITM.clone());
            return toAdd;
        }
        return toAdd.clone();
    }
    public static ItemStack addLore(ItemStack toAdd, List<String> lore)
    {
        return addLore(false, toAdd, lore);
    }
    public static ItemStack addLore(boolean clear, ItemStack toAdd, String... lores)
    {
        List<String> lore = new ArrayList<String>();
        for (String l : lores) {
            lore.add(l);
        }
        toAdd = addLore(clear, toAdd, lore);
        return toAdd.clone();
    }
    public static ItemStack addLore(ItemStack toAdd, String... lores)
    {
        return addLore(false, toAdd, lores);
    }
    public static ItemStack clearLore(ItemStack toAdd)
    {
        List<String> lore = new ArrayList<String>();
        ItemMeta ITM = null;
        if (toAdd.hasItemMeta()) {
            ITM = toAdd.getItemMeta();
            ITM.setLore(lore);
            toAdd.setItemMeta(ITM.clone());
            return toAdd;
        }
        return toAdd.clone();
    }

    public static ItemStack clearEnchanents(ItemStack toAdd)
    {
        Set<Enchantment> all = toAdd.getEnchantments().keySet();
        for(Enchantment enc: all)
        {
            toAdd.removeEnchantment(enc);
        }
        return toAdd;
    }
    public static boolean equalsLore(List<String> lore, List<String> lore2) {
        String string1 = "";
        String string2 = "";
        Iterator var4 = lore.iterator();

        String string;
        while(var4.hasNext()) {
            string = (String)var4.next();
            string1 = string1 + "-NEW LINE-" + string;
        }

        var4 = lore2.iterator();

        while(var4.hasNext()) {
            string = (String)var4.next();
            string2 = string2 + "-NEW LINE-" + string;
        }

        return string1.equals(string2);
    }
    public static boolean equalsEnchants(Map<Enchantment, Integer> item1, Map<Enchantment, Integer> item2)
    {
        if (item1 == null && item2 == null) return true;
        if (item1 != null && item2 == null) return false;
        if (item2 != null && item1 == null) return false;
        if (item1.size() != item2.size()) return false;
        for(Enchantment e: item1.keySet())
        {
            if (!item2.containsKey(e)) return false;
            if (item1.get(e) != item2.get(e)) return false;
        }
        return true;
    }
    public static boolean isItemEqual(ItemStack item, ItemStack SFitem) {
        if (item == null) return SFitem == null;
        if (SFitem == null) return false;
        if (item.getType() == SFitem.getType()) {//&& item.getAmount() >= SFitem.getAmount()
            if (isWeapon(item) || isArmor(item)) {
                if (item.getData().getData() != SFitem.getData().getData()) {
                    if (!(SFitem.getDurability() == item.getData().getData() && SFitem.getData().getData() == item.getDurability()))
                        return false;
                }
            }
            if (!equalsEnchants(item.getEnchantments(), SFitem.getEnchantments())) return false;
            if (item.hasItemMeta() && SFitem.hasItemMeta()) {
                ItemMeta a = item.getItemMeta();
                ItemMeta b = SFitem.getItemMeta();
                if (a instanceof BannerMeta && b instanceof BannerMeta)
                {
                    if(!((BannerMeta)a).getPatterns().equals(((BannerMeta)b).getPatterns())) return false;

                    if (((BannerMeta)a).getBaseColor() == null && ((BannerMeta)b).getBaseColor() != null) return false;
                    if (((BannerMeta)a).getBaseColor() != null && ((BannerMeta)b).getBaseColor() == null) return false;
                    if (((BannerMeta)a).getBaseColor() != null && ((BannerMeta)b).getBaseColor() != null) {
                        if (!((BannerMeta) a).getBaseColor().equals(((BannerMeta) b).getBaseColor())) return false;
                    }
                }
                if (a instanceof EnchantmentStorageMeta && b instanceof EnchantmentStorageMeta)
                {
                    if (((EnchantmentStorageMeta)a).getEnchants() !=null && ((EnchantmentStorageMeta)b).getEnchants() ==null) return false;
                    if (((EnchantmentStorageMeta)a).getEnchants() ==null && ((EnchantmentStorageMeta)b).getEnchants() !=null) return false;
                    if (((EnchantmentStorageMeta)a).getEnchants() !=null && ((EnchantmentStorageMeta)b).getEnchants() !=null) {
                        if (!((EnchantmentStorageMeta) a).getEnchants().equals(((EnchantmentStorageMeta) b).getEnchants()))
                            return false;
                    }
                }
                if (a instanceof SpawnEggMeta && b instanceof SpawnEggMeta)
                {
                    if (((SpawnEggMeta)a).getSpawnedType() !=null && ((SpawnEggMeta)b).getSpawnedType() ==null) return false;
                    if (((SpawnEggMeta)a).getSpawnedType() ==null && ((SpawnEggMeta)b).getSpawnedType() !=null) return false;
                    if (((SpawnEggMeta)a).getSpawnedType() !=null && ((SpawnEggMeta)b).getSpawnedType() !=null) {
                        if (!((SpawnEggMeta) a).getSpawnedType().equals(((SpawnEggMeta) b).getSpawnedType()))
                            return false;
                    }
                }
                if (a instanceof SkullMeta && b instanceof SkullMeta)
                {
                    /*
                    if (((SkullMeta)a).getOwningPlayer() != null && ((SkullMeta)b).getOwningPlayer() ==null) return false;
                    if (((SkullMeta)a).getOwningPlayer() == null && ((SkullMeta)b).getOwningPlayer() !=null) return false;
                    if (((SkullMeta)a).getOwningPlayer() != null && ((SkullMeta)b).getOwningPlayer() !=null) {
                        if (!((SkullMeta) a).getOwningPlayer().equals(((SkullMeta) b).getOwningPlayer())) return false;
                    }*/
                }
                if (a instanceof PotionMeta && b instanceof PotionMeta)
                {
                    if(!((PotionMeta)a).getCustomEffects().equals(((PotionMeta)b).getCustomEffects())) return false;
                    if(!((PotionMeta)a).getBasePotionData().equals(((PotionMeta)b).getBasePotionData())) return false;
                    if (((PotionMeta)a).getColor() !=null && ((PotionMeta)b).getColor() ==null) return false;
                    if (((PotionMeta)a).getColor() ==null && ((PotionMeta)b).getColor() !=null) return false;
                    if (((PotionMeta)a).getColor() !=null && ((PotionMeta)b).getColor() !=null) {
                        if (!((PotionMeta) a).getColor().equals(((PotionMeta) b).getColor())) return false;
                    }

                }
                if (a instanceof BookMeta && b instanceof BookMeta)
                {
                    if(!((BookMeta)a).getAuthor().equals(((BookMeta)b).getAuthor())) return false;
                    if(!((BookMeta)a).getGeneration().equals(((BookMeta)b).getGeneration())) return false;
                    if(!((BookMeta)a).getPages().equals(((BookMeta)b).getPages())) return false;
                    if(!((BookMeta)a).getTitle().equals(((BookMeta)b).getTitle())) return false;
                }
                if (a instanceof LeatherArmorMeta && b instanceof LeatherArmorMeta)
                {
                    if(!((LeatherArmorMeta)a).getColor().equals(((LeatherArmorMeta)b).getColor())) return false;
                }
                if (a instanceof KnowledgeBookMeta && b instanceof KnowledgeBookMeta)
                {
                    if(!((FireworkMeta)a).getEffects().equals(((FireworkMeta)b).getEffects())) return false;
                    if(((FireworkMeta)a).getPower() != ((FireworkMeta)b).getPower()) return false;
                }
                if (item.getItemMeta().hasDisplayName() && SFitem.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().equals(SFitem.getItemMeta().getDisplayName())) {
                        if (item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore()) {
                            return false;
                        }
                        if (item.getItemMeta().hasLore() && SFitem.getItemMeta().hasLore()) {
                            return equalsLore(item.getItemMeta().getLore(), SFitem.getItemMeta().getLore());
                        }
                        else return !item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore();
                    }
                    else return false;
                }
                else if (!item.getItemMeta().hasDisplayName() && !SFitem.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore()) {
                        return false;
                    }
                    if (item.getItemMeta().hasLore() && SFitem.getItemMeta().hasLore()) {
                        return equalsLore(item.getItemMeta().getLore(), SFitem.getItemMeta().getLore());
                    }
                    else return !item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore();

                }
                else return false;
            }
            else return !item.hasItemMeta() && !SFitem.hasItemMeta();
        }
        else return false;
    }
    public static boolean isArmor(ItemStack mat)
    {
        return isArmor(mat.getType());
    }
    public static boolean isArmor(Material mat)
    {
        switch (mat)
        {
            case DIAMOND_CHESTPLATE:LATE:
                return true;
            case CHAINMAIL_CHESTPLATE:
                return true;
            case GOLDEN_CHESTPLATE:
                return true;
            case IRON_CHESTPLATE:
                return true;
            case LEATHER_CHESTPLATE:
                return true;
            case DIAMOND_HELMET:
                return true;
            case LEATHER_HELMET:
                return true;
            case IRON_HELMET:
                return true;
            case CHAINMAIL_HELMET:
                return true;
            case GOLDEN_HELMET:
                return true;
            case DIAMOND_LEGGINGS:
                return true;
            case CHAINMAIL_LEGGINGS:
                return true;
            case GOLDEN_LEGGINGS:
                return true;
            case IRON_LEGGINGS:
                return true;
            case LEATHER_LEGGINGS:
                return true;
            case DIAMOND_BOOTS:
                return true;
            case CHAINMAIL_BOOTS:
                return true;
            case GOLDEN_BOOTS:
                return true;
            case IRON_BOOTS:
                return true;
            case LEATHER_BOOTS:
                return true;
            case ELYTRA:
                return true;
        }
        return false;
    }
    public static boolean isWeapon(ItemStack mat)
    {
        return isWeapon(mat.getType());
    }
    public static boolean hasCustomName(ItemStack mat)
    {
        if (mat.hasItemMeta())
        {
            if (mat.getItemMeta().hasDisplayName())
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isExpensive(ItemStack mat)
    {
        return isExpensive(mat.getType());
    }
    public static boolean isExpensive(Material mat)
    {
        switch (mat)
        {
            case BEDROCK:
                return true;
            case BEACON:
                return true;
            case SKELETON_SKULL:L:
                return true;
            case SKELETON_WALL_SKULL:
                return true;
            case BOOKSHELF:
                return true;
            case BOOK:
                return true;
            case WRITABLE_BOOK:
                return true;
            case ENCHANTED_BOOK:
                return true;
            case KNOWLEDGE_BOOK:
                return true;
            case WRITTEN_BOOK:
                return true;
            case ANVIL:
                return true;
            case POTION:
                return true;
            case SPAWNER:
                return true;
            case CHEST:
                return true;
            case DRAGON_EGG:
                return true;
            case DRAGON_BREATH:
                return true;
            case ENDER_CHEST:
                return true;
            case NETHER_STAR:
                return true;
            case FURNACE:
                return true;
            case CRAFTING_TABLE:
                return true;
            case DISPENSER:
                return true;
            case DROPPER:
                return true;
            case BUCKET:
                return true;
            case WATER_BUCKET:
                return true;
            case LAVA_BUCKET:
                return true;
            case BREWING_STAND:
                return true;
            case HOPPER:
                return true;
            case ENCHANTING_TABLE:
                return true;
            case EXPERIENCE_BOTTLE:
                return true;
            case TRIPWIRE_HOOK:
                return true;
        }
        if (mat.toString().contains("SKULL"))
        {
            return true;
        }
        if (mat.toString().contains("BOOK"))
        {
            return true;
        }
        if (mat.toString().contains("BED"))
        {
            return true;
        }
        if (mat.toString().contains("DOOR"))
        {
            return true;
        }
        if (mat.toString().contains("BLOCK"))
        {
            return true;
        }
        if (mat.toString().contains("ORE"))
        {
            return true;
        }
        if (mat.toString().contains("SHULKER"))
        {
            return true;
        }
        return false;
    }

    public static boolean isWeapon(Material mat)
    {
        switch (mat)
        {
            case DIAMOND_SWORD:
                return true;
            case GOLDEN_SWORD:
                return true;
            case IRON_SWORD:
                return true;
            case STONE_SWORD:
                return true;
            case WOODEN_SWORD:
                return true;
            case DIAMOND_AXE:
                return true;
            case GOLDEN_AXE:
                return true;
            case IRON_AXE:
                return true;
            case STONE_AXE:
                return true;
            case WOODEN_AXE:
                return true;
            case SHIELD:
                return true;
        }
        return false;
    }
    public static boolean isTool(ItemStack mat)
    {
        return isTool(mat.getType());
    }
    public static boolean isTool(Material mat)
    {
        switch (mat)
        {
            case DIAMOND_PICKAXE:
            return true;
            case GOLDEN_PICKAXE:
                return true;
            case IRON_PICKAXE:
                return true;
            case STONE_PICKAXE:
                return true;
            case WOODEN_PICKAXE:
                return true;
            case DIAMOND_AXE:
                return true;
            case GOLDEN_AXE:
                return true;
            case IRON_AXE:
                return true;
            case STONE_AXE:
                return true;
            case WOODEN_AXE:
                return true;
            case DIAMOND_SHOVEL:
                return true;
            case STONE_SHOVEL:
                return true;
            case GOLDEN_SHOVEL:
                return true;
            case IRON_SHOVEL:
                return true;
            case WOODEN_SHOVEL:
                return true;
            case DIAMOND_HOE:
                return true;
            case GOLDEN_HOE:
                return true;
            case IRON_HOE:
                return true;
            case STONE_HOE:
                return true;
            case WOODEN_HOE:
                return true;
            case SHEARS:
                return true;
            case FLINT_AND_STEEL:
                return true;
            case FISHING_ROD:
                return true;
        }
        return false;
    }
    public static ItemStack getSkull(String texture)
    {
        try {
            ItemStack placeMe = CustomSkull.getItem(texture).clone();
            return placeMe;
        }
        catch (Exception e)
        {

        }
        return  null;
    }
    private String[] addRightEmptyLore(int size)
    {
        List<String> lore = new ArrayList<String>();
        String tmp = "";
        ChatColor chatColor = ChatColor.GRAY;
        lore.add(ChatColor.YELLOW + " " + ChatColor.STRIKETHROUGH + "---------------------------");
        for(int i = 0; i < size; i++)
        {
            tmp = tmp + chatColor+ "empty ";
            if (tmp.length() > 60)
            {
                lore.add(tmp);
                tmp = "";
            }
            if (chatColor.equals(ChatColor.GRAY))
            {
                chatColor = ChatColor.DARK_GRAY;
            }else {
                chatColor = chatColor.GRAY;
            }
        }
        if (!tmp.equals("")) {
            lore.add(tmp);
        }
        lore.add(ChatColor.BLUE + " " + ChatColor.STRIKETHROUGH + "---------------------------");
        lore.add(ChatColor.BLUE + "" + size + " J/s");
        String[] out = new String[lore.size()];
        out = lore.toArray(out);
        return out;
    }
    public static boolean isSuperItemHoler(ItemStack  toCheck)
    {
        if (!TitanBox.isEmpty(toCheck)) {
            if (toCheck.hasItemMeta()) {
                ItemMeta itemMeta = toCheck.getItemMeta();
                if (itemMeta.hasDisplayName()) {
                    if (itemMeta.getDisplayName().equals(ChatColor.YELLOW + "Super Item Mover"))
                    {
                        return true;
                    }
                }
                if (itemMeta.hasLore())
                {
                    List<String> lore = itemMeta.getLore();
                    for (String s : lore) {
                        if (s.startsWith(ChatColor.YELLOW + "   ") || ChatColor.stripColor(s).startsWith("   ")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public static Object[] returnSuperItemHolder(ItemStack SuperItemHolder)
    {
        if (!TitanBox.isEmpty(SuperItemHolder)) {
            if (TitanBox.isSuperItemHoler(SuperItemHolder)) {
                SuperItemHolder = SuperItemHolder.clone();
                if (SuperItemHolder.hasItemMeta()) {
                    ItemMeta itemMeta = SuperItemHolder.getItemMeta();
                    if (itemMeta.hasLore()) {
                        String name = "error";
                        Long amount = Long.valueOf(0);
                        List<String> lore = itemMeta.getLore();
                        List<String> Reaklore = new ArrayList<String>();
                        for (String s : lore) {
                            String noColoerS = ChatColor.stripColor(s);
                            if (noColoerS.startsWith("   Name: ")) {
                                String value = s.replace(ChatColor.YELLOW + "   Name: " + ChatColor.WHITE, "");
                                name = value;
                            }
                            if (noColoerS.startsWith("   Barcode: ")) {
                                String value = noColoerS.replace("   Barcode: ", "");
                                amount = Long.valueOf(value);
                            }
                            if (!s.startsWith(ChatColor.YELLOW + "   ") && !noColoerS.startsWith("   ")) {
                                Reaklore.add(s);
                            }
                        }
                        ItemStack itemStackOut = new ItemStack(SuperItemHolder.getType(), 1, SuperItemHolder.getDurability());
                        if (!name.equalsIgnoreCase("normal")) {
                            itemStackOut = SuperItemHolder.clone();
                            itemStackOut.setAmount(1);
                            itemStackOut = TitanBox.changeName(itemStackOut, name);
                        }
                        itemStackOut = TitanBox.clearLore(itemStackOut);
                        if (Reaklore.size() > 0) {
                            itemStackOut = TitanBox.addLore(itemStackOut, Reaklore);
                        }
                        Object[] tmpout = {itemStackOut.clone(), amount};
                        return tmpout;

                    }
                }
            }
        }
        return  null;
    }
    public static ItemStack getSuperItemHolder(ItemStack mat, Long amount)
    {
        String name = "Normal";
        if (!TitanBox.isEmpty(mat))
        {
            if (!TitanBox.isSuperItemHoler(mat)) {
                mat = mat.clone();
                if (mat.hasItemMeta()) {
                    ItemMeta itemMeta = mat.getItemMeta();
                    if (itemMeta.hasDisplayName()) {
                        name = itemMeta.getDisplayName();
                    }
                }
                mat.setAmount(1);
                mat = TitanBox.changeName(mat, ChatColor.YELLOW + "Super Item Mover");
                mat = TitanBox.addLore(mat, ChatColor.YELLOW + "   Name: " + ChatColor.WHITE + name, ChatColor.YELLOW + "   Amount: " + ChatColor.WHITE + TitanBox.format(amount), ChatColor.YELLOW + "   Barcode: " + ChatColor.MAGIC + amount);
                return mat.clone();
            }
        }
        return  null;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        try {
            if (sender instanceof Player) {
                if (label.equalsIgnoreCase("shop") || label.equalsIgnoreCase("shops")) {
                    if (args.length > 0) {
                        if (args[0].equalsIgnoreCase("admin")) {
                            if (sender.hasPermission("titanbox.admin"))
                            {
                                if (args.length > 1) {
                                    String shopname = "";
                                    for (int i = 1; i < args.length; i++) {
                                        shopname = shopname + args[i] + " ";
                                    }
                                    shopname = shopname.trim();
                                    MainShops.instance.makeAdminShop((Player) sender, shopname);
                                    return true;
                                }
                            }
                        }
                        if (args[0].equalsIgnoreCase("find") || args[0].equalsIgnoreCase("search")) {
                            if (args.length > 1) {
                                String shopname = "";
                                for (int i = 1; i < args.length; i++) {
                                    shopname = shopname + args[i] + " ";
                                }
                                shopname = shopname.trim();
                                HashMap<ShopHolder,List<ItemStack>> shops = new HashMap<ShopHolder,List<ItemStack>>();
                                for(ShopHolder shopHolder: mainShops.allShopsByIDS.values())
                                {
                                    List<ItemStack> itemStack = shopHolder.searchItem(shopname);
                                    if (itemStack != null && itemStack.size() > 0)
                                    {
                                        shops.put(shopHolder, itemStack);
                                    }
                                }
                                for(ShopHolder shopHolder: shops.keySet())
                                {
                                    List<ItemStack> itemStack = shops.get(shopHolder);
                                    String name = Bukkit.getOfflinePlayer(shopHolder.getOwner()).getName();
                                    for (ItemStack itemStack1: itemStack) {
                                        if (!shopHolder.isAdmin()) {
                                            sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.WHITE + TitanBox.getName(itemStack1) + ChatColor.LIGHT_PURPLE + " Owner:"  + ChatColor.WHITE + name + ChatColor.LIGHT_PURPLE + " Shop: " + ChatColor.WHITE + shopHolder.getName());
                                        } else {
                                            sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.WHITE + TitanBox.getName(itemStack1) + ChatColor.RED + " Admin Shop" + ChatColor.LIGHT_PURPLE + " Shop: " + ChatColor.WHITE + shopHolder.getName());
                                        }
                                    }
                                }

                                return true;
                            }
                        }
                        if (args[0].equalsIgnoreCase("remove")) {
                            if (args.length > 1) {
                                String shopname = "";
                                for (int i = 1; i < args.length; i++) {
                                    shopname = shopname + args[i] + " ";
                                }
                                shopname = shopname.trim();
                                mainShops.deleteShop((Player) sender, shopname);
                                return true;
                            }
                        }
                        if (args[0].equalsIgnoreCase("new")) {
                            if (args.length > 1) {
                                String shopname = "";
                                for (int i = 1; i < args.length; i++) {
                                    shopname = shopname + args[i] + " ";
                                }
                                shopname = shopname.trim();
                                mainShops.createNewShop((Player) sender, shopname);
                                mainShops.openShop((Player) sender, shopname);
                                return true;
                            }
                        }
                        if (args[0].equalsIgnoreCase("open")) {
                            if (args.length > 1) {
                                String shopname = "";
                                for (int i = 1; i < args.length; i++) {
                                    shopname = shopname + args[i] + " ";
                                }
                                shopname = shopname.trim();
                                mainShops.openShop((Player) sender, shopname);
                                return true;
                            }
                        }
                        if (args[0].equalsIgnoreCase("view")) {
                            String openPlayer = sender.getName();
                            if (args.length > 1) {
                                openPlayer = args[1];
                            }
                            UUID uuid = Bukkit.getOfflinePlayer(openPlayer).getUniqueId();
                            PlayerShopHolder psh = mainShops.allShopsByOwners.get(uuid);
                            if (psh != null) {
                                HashMap<String, ShopHolder> playerShops = psh.getIdList();
                                Inventory tmp = Bukkit.createInventory(null, 54, "Shops: " + openPlayer);
                                for (String name : playerShops.keySet()) {
                                    ItemStack shopITS = new ItemStack(Material.BROWN_SHULKER_BOX);
                                    shopITS = TitanBox.changeName(shopITS, name);
                                    shopITS = TitanBox.addLore(shopITS, "Click To View This Shop");
                                    tmp.addItem(shopITS.clone());
                                }
                                ((Player) sender).openInventory(tmp);
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Player has no shops.");
                                return true;
                            }

                        }
                        if (args[0].equalsIgnoreCase("list")) {
                            if (args.length > 1) {
                                String name = Bukkit.getOfflinePlayer(args[1]).getName();
                                UUID owner = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                                PlayerShopHolder psh = mainShops.allShopsByOwners.get(owner);
                                int size = psh.getSize();
                                int max = psh.getMax();
                                HashMap<String, ShopHolder> playerShops = psh.getIdList();
                                for (String namepsh : playerShops.keySet()) {
                                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.YELLOW + namepsh);
                                }
                                sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.BLUE + name + ChatColor.WHITE + ": " + size + "/" + max);
                            } else {
                                for (UUID owner : mainShops.allShopsByOwners.keySet()) {
                                    String name = Bukkit.getOfflinePlayer(owner).getName();
                                    PlayerShopHolder psh = mainShops.allShopsByOwners.get(owner);
                                    int size = psh.getSize();
                                    int max = psh.getMax();
                                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.BLUE + name + ChatColor.WHITE + ": " + size + "/" + max);
                                }
                            }
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "Command, " + ChatColor.BLUE + "Option,  " + ChatColor.WHITE + "Required, "+ ChatColor.GRAY + "Optional");
                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "/shop " + ChatColor.BLUE + "new " + ChatColor.WHITE + "shop name");
                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "/shop " + ChatColor.BLUE + "open " + ChatColor.WHITE + "shop name");
                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "/shop " + ChatColor.BLUE + "remove " + ChatColor.WHITE + "shop name");
                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "/shop " + ChatColor.BLUE + "find " + ChatColor.WHITE + "item");
                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "/shop " + ChatColor.BLUE + "view " + ChatColor.GRAY + "owner");
                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.RED + "/shop " + ChatColor.BLUE + "list " + ChatColor.GRAY + "owner");

                    return true;
                }
                if (label.equalsIgnoreCase("titanbox") || label.equalsIgnoreCase("tb")) {

                    if (args[0].equalsIgnoreCase("save")) {
                        if (sender.hasPermission("titanbox.admin")) {
                            saveEveryThing();
                        }
                    }
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.hasPermission("titanbox.admin")) {
                            config.reload();
                            RouterHolder.loadConfig();
                            InventoryModule.loadConfig();
                            sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Config reloaded, Server must be restart to update already loaded Modules!");
                        }
                    }
                    if (args[0].equalsIgnoreCase("restart"))
                    {
                        if (sender.hasPermission("titanbox.admin")) {
                            if (System.currentTimeMillis() - TitanBox.isRunning >  1000) {
                                Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, RouterTimer, 200, RouterHolder.speed);
                                sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Restarted!");
                            }
                            sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.GREEN + TitanBox.convertToTimePasted(TitanBox.isRunning));
                        }
                    }
                    if (args[0].equalsIgnoreCase("SIM")) {
                        if (sender.hasPermission("titanbox.admin")) {
                            Long amount = Long.valueOf(args[1]);
                            ItemStack toCheat = ((Player) sender).getInventory().getItemInMainHand();
                            if (!TitanBox.isEmpty(toCheat))
                            {
                                ItemStack gotit = TitanBox.getSuperItemHolder(toCheat, amount);
                                ((Player) sender).getInventory().addItem(gotit.clone());
                                sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.GREEN + "Done sir.");
                            }

                        }
                    }
                    if (args[0].equalsIgnoreCase("give")) {
                        if (sender.hasPermission("titanbox.admin")) {
                            if (args.length > 2)
                            {
                                giveItems((Player) sender, args[1], args[2]);
                            }
                            else {
                                giveItems((Player) sender, args[1]);
                            }
                        }
                    }
                }

            }
            else
            {
                try {
                    if (label.equalsIgnoreCase("shop") || label.equalsIgnoreCase("shops")) {
                        if (args.length > 0) {
                            if (args[0].equalsIgnoreCase("addmax")) {
                                Player player = Bukkit.getPlayer(args[1]);
                                mainShops.increaseMax(player, 1);
                            }
                        }
                    }
                    if (label.equalsIgnoreCase("titanbox") || label.equalsIgnoreCase("tb")) {
                        if (args[0].equalsIgnoreCase("give")) {
                            Player player = Bukkit.getPlayer(args[args.length - 1]);
                             if (args.length > 2)
                            {
                                giveItems(player, args[1], args[2]);
                            }
                            else {
                                giveItems(player, args[1]);
                            }

                            System.out.println("[TitanBox]: " +"Item Sent!");
                        }
                        if (args[0].equalsIgnoreCase("restart"))
                        {
                            if (System.currentTimeMillis() - TitanBox.isRunning > 1000) {
                                Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, RouterTimer, 200, RouterHolder.speed);
                                sender.sendMessage( "[TitanBox]: " + "Restarted!");
                            }
                            sender.sendMessage("[TitanBox]: " + TitanBox.convertToTimePasted(TitanBox.isRunning));
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("[TitanBox]: " + "Error try /tb give item player");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public ItemStack getItem(String whatItem)
    {
        return getItem(whatItem, null);
    }
    public ItemStack getItem(String whatItem, String sub)
    {
            return getItem(whatItem, sub, null);
    }
    public ItemStack getItem(String whatItem, String sub, Player giveFor)
    {
        if (whatItem.equalsIgnoreCase("a") || whatItem.equalsIgnoreCase("b") ||whatItem.equalsIgnoreCase("c") || whatItem.equalsIgnoreCase("d") || whatItem.equalsIgnoreCase("e"))
        {
            try {
                ItemHolder me = ItemHolder.valueOf("UNIT_" + whatItem.toUpperCase());
                if (me != null) {
                    ItemStack placeMe = me.getItem();
                    placeMe = TitanBox.changeName(placeMe, ChatColor.YELLOW + "New Storage Unit, Size: " + me.getSize());
                    placeMe = TitanBox.addLore(placeMe, addRightEmptyLore(me.getSize()));
                    return  placeMe.clone();
                }
            } catch (Exception e) {

            }
        }
        if (whatItem.equalsIgnoreCase("network"))
        {
            ItemStack placeMe = NetworkMonitor.getMeAsDrop();
            return  placeMe.clone();

        }
        if (whatItem.equalsIgnoreCase("module"))
        {
            ModuleTypeEnum type = ModuleTypeEnum.Inventory;
            if (sub !=null)
            {
                type = ModuleTypeEnum.valueOf(sub);
            }
            ItemStack placeMe = MainModule.getNewModule(type);
            return  placeMe.clone();

        }
        if (whatItem.equalsIgnoreCase("router"))
        {
            ItemHolder me = ItemHolder.ROUTER;
            if (me != null) {
                ItemStack placeMe = me.getItem();
                placeMe = TitanBox.changeName(placeMe, ChatColor.YELLOW + "Item Routing Router");
                placeMe = TitanBox.addLore(placeMe,  "Links: " + ChatColor.WHITE + "Slimefun, Chest, and Storage units", ChatColor.WHITE + "45 J/s");

                return  placeMe.clone();
            }
        }
        if (whatItem.equalsIgnoreCase("upgrade"))
        {
            ItemHolder me = ItemHolder.UPGRADE;
            if (me != null && giveFor != null) {
                ItemStack placeMe = me.getItem();
                placeMe = TitanBox.changeName(placeMe, ChatColor.YELLOW + "Upgrade Device");
                placeMe = TitanBox.addLore(placeMe,  "Used On: " + ChatColor.WHITE + "Storage Unit, and Routers", ChatColor.WHITE + "Hold in main hand and click block thats placed!");
                placeMe = TitanBox.getNewBarcode(placeMe);
                placeMe = TitanBox.addLore(placeMe, ChatColor.YELLOW  + "User: " + ChatColor.WHITE + giveFor.getName(), ChatColor.GRAY + "If this item is dupped the above user will be perm banned!");
                return  placeMe.clone();
            }
        }
        if (whatItem.equalsIgnoreCase("killer") || whatItem.equalsIgnoreCase("water") || whatItem.equalsIgnoreCase("lava") || whatItem.equalsIgnoreCase("ice") || whatItem.equalsIgnoreCase("item"))
        {
            ItemStack pump = Pumps.getMeAsDrop(whatItem);
            return  pump.clone();
        }
        if (whatItem.equalsIgnoreCase("elevator"))
        {
            ItemStack elevator = Elevator.getMeAsDrop();
            return  elevator.clone();

        }
        if (whatItem.equalsIgnoreCase("backpack"))
        {
            ItemStack elevator = BackpackRecover.getMeAsDrop();
            return  elevator.clone();

        }
        if (whatItem.equalsIgnoreCase("storage"))
        {
            ItemStack elevator = StorageRecover.getMeAsDrop();
            return  elevator.clone();

        }
        return null;
    }
    public static void duppedAlert(Player player, ItemStack itemStack)
    {
        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "This is an invalid device! It has been duped and you have been reported.");
        String command = "mail send freethemice ";
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command + "-----StartReport-----");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command + player.getName() + " has used a dupped Upgrade Device!");
        if (itemStack.hasItemMeta())
        {
            if (itemStack.getItemMeta().hasDisplayName())
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command + "Item: " + itemStack.getItemMeta().getDisplayName());
            }
            if (itemStack.getItemMeta().hasLore())
            {
                int i = 0;
                for (String lore: itemStack.getItemMeta().getLore())
                {
                    i++;
                    String list = "Line " + i + ": " + ChatColor.stripColor(lore);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command + list);
                }
            }
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command + "-----EndReport-----");

    }
    public void giveItems(Player sender, String whatItem) {
        giveItems(sender, whatItem, null);
    }
    public void giveItems(Player sender, String whatItem, String sub) {
        ItemStack item = getItem(whatItem,sub, sender);
        if (!isEmpty(item)) {
            sender.getInventory().addItem(item);
        }
    }
    public static String getName(ItemStack toName)
    {
        String name = toName.getType().name();
        if (toName.hasItemMeta())
        {
            if (toName.getItemMeta().hasDisplayName())
            {
                String test = ChatColor.stripColor(toName.getItemMeta().getDisplayName());
                if (test.length() > 0)
                {
                    return test;
                }

            }
        }
        return name;
    }
    public static boolean hasBarcodeGood(ItemStack toBarcode)
    {
        if (!TitanBox.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = getName(toBarcode);
                    List<String> check = toBarcode.getItemMeta().getLore();
                    for(String s: check)
                    {
                        if (s.startsWith(ChatColor.MAGIC + "barcode:"))
                        {
                            String saltStr = s.replace(ChatColor.MAGIC + "barcode:", "");
                            if (barcodes.contains(name + "." + saltStr)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public static void setBarcodeTrue(ItemStack toBarcode, Player player)
    {
        if (!TitanBox.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = getName(toBarcode);
                    List<String> check = toBarcode.getItemMeta().getLore();
                    int line = 0;
                    String saltStr  = "";
                    for(String s: check)
                    {
                        if (s.startsWith(ChatColor.MAGIC + "barcode:"))
                        {
                            saltStr = s.replace(ChatColor.MAGIC + "barcode:", "");
                            if (barcodes.contains(name + "." + saltStr)) {
                                barcodes.setValue(name + "." + saltStr, true);
                            }
                        }
                    }
                    barcodes.setValue(name + ".info." + saltStr + ".time" , System.currentTimeMillis());
                    barcodes.setValue(name + ".info." + saltStr + ".user" , player.getName());
                    barcodes.setValue(name + ".info." + saltStr + ".item" , toBarcode.getItemMeta().getDisplayName());
                    for(String s: check) {
                        barcodes.setValue(name + ".info." + saltStr + ".line" + line, s);
                        line++;
                    }
                }
            }
        }
    }
    public static String readBarcode(ItemStack toBarcode)
    {
        if (!TitanBox.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = getName(toBarcode);
                    List<String> check = toBarcode.getItemMeta().getLore();
                    for(String s: check)
                    {
                        if (s.startsWith(ChatColor.MAGIC + "barcode:"))
                        {
                            String saltStr = s.replace(ChatColor.MAGIC + "barcode:", "");
                            if (barcodes.contains(name + "." + saltStr)) {
                                return barcodes.getBoolean(name + "." + saltStr) + "";
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    public static ItemStack getNewBarcode(ItemStack toBarcode)
    {
        if (!TitanBox.isEmpty(toBarcode)) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random(System.currentTimeMillis());
            while (salt.length() < 36) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();

            String name = getName(toBarcode);
            if (barcodes.contains(name + "." + saltStr)) {
                return getNewBarcode(toBarcode);
            }
            barcodes.setValue(name + "." + saltStr, false);
            toBarcode = TitanBox.addLore(false, toBarcode.clone(), ChatColor.MAGIC + "barcode:" + saltStr);
        }
        return toBarcode;
    }

}

/* Location:           D:\Daniel\Downloads\WildTp-1.2.jar
 * Qualified Name:     MainClass
 * JD-Core Version:    0.6.2
 */
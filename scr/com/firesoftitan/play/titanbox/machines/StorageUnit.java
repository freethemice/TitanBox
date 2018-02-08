package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.buttonEnum;
import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import com.firesoftitan.play.titanbox.guis.mainGUI;
import com.firesoftitan.play.titanbox.holders.ItemHolder;
import com.firesoftitan.play.titanbox.interfaces.InventoryHolder;
import com.firesoftitan.play.titanbox.runnables.StorageGuiRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class StorageUnit implements InventoryHolder {
    private Long lastUpdated = Long.valueOf(0);
    private int size = 2;
    private ItemStack[] storageItems;
    private Long[] storageCount;
    private Location location;
    //private ItemStack meAsDrop;
    private int battery = 0;
    private ItemHolder me;
    private String MyId;
    private Long lastAccessed;
    public static String name = ChatColor.YELLOW + "Storage Unit";
    private UUID owner;
    //private Inventory myGui;
    private mainGUI gui;
    public static String guiname = "Storage Unit";
    private boolean remember = true;
    private boolean locked = false;
    private boolean moving = false;
    private int from = -1;
    public static HashMap<String, StorageUnit> StorageById = new HashMap<String, StorageUnit>();
    public static HashMap<String, StorageUnit>  StorageByLocation = new HashMap<String, StorageUnit>();
    public static HashMap<String, List<StorageUnit>>  StorageByOwner = new HashMap<String, List<StorageUnit>>();
    public static Config storage = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "storage.yml");
    public StorageUnit(String MynewId)
    {
        MyId = MynewId;
        loadMe();
    }
    public ItemStack viewSlot(int slot)
    {
        if (storageItems[slot] == null) return  null;
        return storageItems[slot].clone();
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setCharge(int amount)
    {
        BlockStorage.addBlockInfo(this.getLocation(), "energy-charge", amount + "");
    }
    public void setCapacity(int amount)
    {
        BlockStorage.addBlockInfo(this.getLocation(), "energy-capacity", amount + "");
    }
    public int getCharge()
    {
        int c = 0;
        if (this.getLocation() == null)
        {
            return  c;
        }
        try {
            c = Integer.parseInt(BlockStorage.getBlockInfo(this.getLocation()).getString("energy-charge"));
        }
        catch (Exception e)
        {

        }
        return c;

    }
    public int getCapacity()
    {
        int c = 0;
        if (this.getLocation() == null)
        {
            return  c;
        }
        try {
            c = Integer.parseInt(BlockStorage.getBlockInfo(this.getLocation()).getString("energy-capacity"));
        }
        catch (Exception e)
        {

        }
        return c;

    }
    public void moveSlot(int from, int to)
    {
        ItemStack tmpFrom = null;
        Long countFrom = Long.valueOf(0);
        ItemStack tmpTo = null;
        Long countTo = Long.valueOf(0);

        if (storageItems[from] != null) {
            tmpFrom = storageItems[from].clone();
            countFrom = storageCount[from];
        }
        if (storageItems[to] != null) {
            tmpTo = storageItems[to].clone();
            countTo = storageCount[to];
        }
        storageItems[from] = tmpTo;
        storageCount[from] = countTo;
        storageItems[to] = tmpFrom;
        storageCount[to] = countFrom;
    }
    public void reBuildSlots()
    {
        if (gui == null)
        {
            return;
        }
        //All Blank ----------------------------------------------------------------------------------
        for (int i = 0; i < size; i++)
        {
            gui.getButton(i).setToggle(buttonEnum.EMPTY);
        }
        //Storage ----------------------------------------------------------------------------------
        for (int i = 0; i < size; i++)
        {
            if (storageItems[i] != null)
            {
                storageItems[i].setAmount(1);
            }
            if (!TitanBox.isEmpty(storageItems[i]))
            {
                ItemStack interfaceItem = storageItems[i].clone();
                interfaceItem.setAmount(1);
                String type = "";
                if (!interfaceItem.hasItemMeta())
                {
                    type = ChatColor.WHITE + interfaceItem.getType().name();
                }
                else
                {
                    type = ChatColor.WHITE + interfaceItem.getItemMeta().getDisplayName();
                }
                Double per = Double.valueOf(storageCount[i]) / Double.valueOf(Long.MAX_VALUE);
                per = per *100;
                int perc = (int) Math.round(per);
                String storpec =  "" + ChatColor.WHITE + perc + "%";
                if (per < 1)
                {
                    storpec =  "" + ChatColor.WHITE + "<1%";
                }
                if (per > 99 && !this.isFull(i))
                {
                    storpec =  "" + ChatColor.WHITE + "<100%";
                }
                if (storageCount[i].equals(Long.valueOf(0)))
                {
                    storpec =  "" + ChatColor.WHITE + "empty";
                }
                buttonGUIs tmp = gui.getButton(i);
                tmp.setMaterialFalse(interfaceItem);
                tmp.setNameFalse("Stored Item");
                tmp.addLore(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "---------------------------", ChatColor.YELLOW + "Name: " + type,  ChatColor.YELLOW + "Stock: " + ChatColor.WHITE + TitanBox.formatCommas(storageCount[i]), ChatColor.YELLOW + "Capacity: " +storpec, ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "---------------------------",ChatColor.AQUA + "Left Click: " + ChatColor.WHITE + "64", ChatColor.AQUA + "Right Click: " + ChatColor.WHITE + "1", ChatColor.AQUA + "Shift Left Click: " + ChatColor.WHITE + "4x64 (256)", ChatColor.AQUA + "Shift Right Click: " + ChatColor.WHITE + "ALL (SIM)", ChatColor.AQUA + "Right Click Empty: " + ChatColor.WHITE + "Remove" );
                tmp.setMaterialFalse(interfaceItem);
                tmp.setToggle(buttonEnum.FALSE);
            }
            else
            {
                if (isLocked())
                {
                    ItemStack blocked = new ItemStack(Material.BARRIER);
                    buttonGUIs tmp = gui.getButton(i);
                    tmp.setMaterialFalse(blocked);
                    tmp.setNameFalse("Lock is on");
                    tmp.setLore(new ArrayList<String>());
                    tmp.setToggle(buttonEnum.FALSE);
                }

            }
        }
        if (from > -1)
        {
            ItemStack blocked = new ItemStack(Material.BARRIER);
            buttonGUIs tmp  = gui.getButton(from);
            tmp.setNameFalse("Click new slot to switch!");
            tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZhNjA1ZTI1ZjRmYzJjZWE1YTc2NmQ3OWE4YmZhMjkwMzEzZTQ1ZDhmNWU5NTdkOTU4YTBmMzNmY2IxNiJ9fX0=");
            tmp.setLore(new ArrayList<String>());
            tmp.setToggle(buttonEnum.FALSE);
        }
    }
    private void wipeGUI()
    {
        gui = null;
    }
    private void buildGUI()
    {
        gui = new mainGUI(54, guiname + " " + getMyId(), this);
        for (int i = 0; i < 54; i++)
        {
            buttonGUIs tmp = new buttonGUIs(gui, i);
            gui.addButton(tmp, tmp.getSlot());
        }


        //All Blank ----------------------------------------------------------------------------------
        for (int i = size; i < 54; i++)
        {
            gui.getButton(i).setToggle(buttonEnum.BLANK);
        }

        reBuildMenu();
        reBuildSlots();
    }
    public void reBuildMenu()
    {
        if (gui == null)
        {
            return;
        }
        //Slot45 ----------------------------------------------------------------------------------
        buttonGUIs tmp = gui.getButton(45);
        tmp.setNameFalse("Sort All");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFlMjk0MjJkYjQwNDdlZmRiOWJhYzJjZGFlNWEwNzE5ZWI3NzJmY2NjODhhNjZkOTEyMzIwYjM0M2MzNDEifX19");
        tmp.setToggle(buttonEnum.FALSE);
        //Slot46 ----------------------------------------------------------------------------------
        tmp = gui.getButton(46);
        tmp.setNameFalse("Remember Empty Slots: " + ChatColor.GRAY + "off");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUzZGQ0NTc5ZWRjMmE2ZjIwMzJmOTViMWMxODk4MTI5MWI2YzdjMTFlYjM0YjZhOGVkMzZhZmJmYmNlZmZmYiJ9fX0=");
        tmp.setNameTrue("Remember Empty Slots: " + ChatColor.BLUE + "on");
        tmp.setTextureTrue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4ZGVmNjdhMTI2MjJlYWQxZGVjZDNkODkzNjQyNTdiNTMxODk2ZDg3ZTQ2OTgxMzEzMWNhMjM1YjVjNyJ9fX0=");
        tmp.setToggle(buttonEnum.getType(isRemember()));
        //Slot47 ----------------------------------------------------------------------------------
        tmp  = gui.getButton(47);
        tmp.setNameFalse("Lock Empty Slots: " + ChatColor.GRAY + "off");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJjODFiNDM1ZGMyMmQyOWQ0Nzc4ZmZkMjJmZWI4NDZhNjhiNjQ4ZGQxYWY1ZGU4MThiNTE3ZjA1NzRkIn19fQ==");
        tmp.setNameTrue("Lock Empty Slots: " + ChatColor.BLUE + "on");
        tmp.setTextureTrue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2NjMjE3YTliOWUzY2UzY2QwNDg0YzdlOGNlNDlkMWNmNzQxMjgxYmRkYTVhNGQ2Y2I4MjFmMzc4NzUyNzE4In19fQ==");
        tmp.setToggle(buttonEnum.getType(isLocked()));
        //Slot48 ----------------------------------------------------------------------------------
        tmp  = gui.getButton(48);
        tmp.setNameFalse("Charge Unit");
        tmp.setMaterialFalse(SlimefunItems.BATTERY.clone());
        tmp.addLore("If unit isn't powered, you can charge it manually ","click here and it will remove 1 battery from your invitory", "and will charge the unit " + ChatColor.WHITE + "40j");
        tmp.setToggle(buttonEnum.FALSE);
        //Slot53 ----------------------------------------------------------------------------------
        tmp  = gui.getButton(53);
        tmp.setNameFalse("Re-organize: " + ChatColor.GRAY + "off");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxZDZlZGE4M2VkMmMyNGRjZGNjYjFlMzNkZjM2OTRlZWUzOTdhNTcwMTIyNTViZmM1NmEzYzI0NGJjYzQ3NCJ9fX0=");
        tmp.setNameTrue("Re-organize: " + ChatColor.BLUE + "on");
        tmp.setTextureTrue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M2OGRlYjhiYzU3NmI0ZDYzMWVhZGRlODlkYzljZDk0NmEzYmI3ZTM0N2I4YzRkYTVkODJjODg4ZWQxNWMzYyJ9fX0=");
        tmp.setToggle(buttonEnum.getType(isMoving()));
        //Slot49 ----------------------------------------------------------------------------------
        tmp  = gui.getButton(49);
        String bTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQyYzE5YjQ0MjU0MTM1MWE2YjgxZWViNmNiZWY0MTk2NmZmYjdkYmU0YzEzNmI4N2Y1YmFmOWQxNGEifX19";
        double percent = (double)this.getCharge() / (double)this.getCapacity();
        percent = percent *100;
        int iPercent = (int)percent;
        String bName = ChatColor.YELLOW + "Charge@ " + ChatColor.WHITE + (int)iPercent + "%";
        if (iPercent > 0)
        {
            bTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM4YTU4M2JiMWNkZDlkMWE1MTQ0YjI0YWQ1NTBkYTlhMmY2NGRhZTIxZjIwNGU3MWJjYzhkZTVhNTM5ZDgifX19";
        }
        if (iPercent > 33)
        {
            bTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ3OGE5NWIxYmU1ODU5M2EwM2NmMTQ3MjYzYmRhM2I4YjM0Njg5OWJlMTEwZDMxNDY1ZWQyZmViYzBiZDEwIn19fQ==";
        }
        if (iPercent > 66)
        {
            bTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE2YjY4Mzc2YzE4MWM5YTczNDNmZWUzNjk3ZmFhY2VjMzUxMjlmYjY0ZGU1OTE0YmRiZjg2OWM2NTJjIn19fQ==";
        }
        tmp.setNameFalse(bName);
        tmp.setTextureFalse(bTexture);
        tmp.setToggle(buttonEnum.FALSE);

        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(owner);
        tmp =gui.getButton(52);
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM4OGQ2MTYzZmFiZTdjNWU2MjQ1MGViMzdhMDc0ZTJlMmM4ODYxMWM5OTg1MzZkYmQ4NDI5ZmFhMDgxOTQ1MyJ9fX0=");
        tmp.setNameFalse("Owner: " + oPlayer.getName());
        tmp.setToggle(buttonEnum.FALSE);

        //Storage ----------------------------------------------------------------------------------
    }
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public UUID getOwner() {
        return owner;
    }
    public StorageUnit(String MynewId, int mySize, ItemHolder me, Player owner)
    {
        size = mySize;
        storageItems = new ItemStack[size];
        storageCount = new Long[size];
        location = null;
        this.me = me;
        MyId = MynewId;
        lastAccessed =System.currentTimeMillis();
        this.owner = owner.getUniqueId();
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
        if (!this.remember)
        {
            for(int i = 0; i < storageCount.length;i++)
            {
                if (storageCount[i] != null) {
                    if (storageCount[i].equals(Long.valueOf(0))) {
                        storageItems[i] = null;
                    }
                }
            }
        }
    }

    public void saveMe()
    {

        StorageUnit.storage.setValue(getMyId() + ".id", getMyId());
        StorageUnit.storage.setValue(getMyId() + ".owner", getOwner());
        StorageUnit.storage.setValue(getMyId() + ".size", getSize());
        StorageUnit.storage.setValue(getMyId() + ".accessed", getLastAccessed());
        StorageUnit.storage.setValue(getMyId() + ".me",  me.toString());
        StorageUnit.storage.setValue(getMyId() + ".location",  getLocation());
        StorageUnit.storage.setValue(getMyId() + ".remember",  isRemember());
        StorageUnit.storage.setValue(getMyId() + ".locked",  isLocked());
        StorageUnit.storage.setValue(getMyId() + ".battery",  battery);

        List<ItemStack> itemList = new ArrayList<ItemStack>();
        List<String> countList = new ArrayList<String>();
        for(int i = 0; i < storageItems.length; i++)
        {
            if (storageItems[i] !=null) {
                if (!storageItems[i].getType().equals(Material.AIR)) {
                    itemList.add(storageItems[i].clone());
                    countList.add(storageCount[i] + "");
                }
            }
        }
        StorageUnit.storage.setValue(getMyId() + ".items",  itemList);
        StorageUnit.storage.setValue(getMyId() + ".counts",  countList);
    }
    public void loadMe()
    {
        battery = 0;
        if (StorageUnit.storage.contains(getMyId() + ".battery")) {
            battery = StorageUnit.storage.getInt(getMyId() + ".battery");
        }
       size = StorageUnit.storage.getInt(getMyId() + ".size");
       owner = StorageUnit.storage.getUUID(getMyId() + ".owner");
       storageItems = new ItemStack[size];
       storageCount = new Long[size];
       List<ItemStack> itemList = new ArrayList<ItemStack>();
       List<String> itemLongs = new ArrayList<String>();
       location = null;
       locked = StorageUnit.storage.getBoolean(getMyId() + ".locked");
       remember = StorageUnit.storage.getBoolean(getMyId() + ".remember");
       lastAccessed = StorageUnit.storage.getLong(getMyId() + ".accessed");
       if (StorageUnit.storage.contains(getMyId() + ".items")) {
           itemList = TitanBox.loadListItemStack(StorageUnit.storage, getMyId() + ".items");
       }
       if (StorageUnit.storage.contains(getMyId() + ".counts")) {
           try {
               itemLongs = StorageUnit.storage.getStringList(getMyId() + ".counts");
           }
           catch (Exception e)
           {
               e.printStackTrace();
           }
       }
       for(int i = 0; i < storageItems.length; i++)
       {
           if (itemList.size() > i) {
               storageItems[i] = itemList.get(i).clone();
               storageItems[i].setAmount(1);
               storageCount[i] = Long.valueOf(itemLongs.get(i));
           }
       }
       me = ItemHolder.valueOf(StorageUnit.storage.getString(getMyId() + ".me"));
       if (StorageUnit.storage.contains(getMyId() + ".location")) {
           location = StorageUnit.storage.getLocation(getMyId() + ".location");
       }

    }
    public String getMyId() {
        return MyId;
    }

    public mainGUI getGui() {
        return gui;
    }

    public void OpenGui(Player player)
    {
        if (lastUpdated + 750 < System.currentTimeMillis()) {
            lastUpdated = System.currentTimeMillis();
            if (location != null) {
                if ((GriefPrevention.instance.allowBuild(player, location) == null) || player.hasPermission("titanbox.admin")) {
                    if (gui == null) {
                        lastAccessed = System.currentTimeMillis();

                        buildGUI();

                        gui.showGUI(player);

                        StorageGuiRunnable SGR = new StorageGuiRunnable();
                        SGR.myStorageUnit = this;
                        SGR.myID = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, SGR, 1, 1);

                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Someone is viewing this storage unit.");
                    }
                }
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public ItemHolder getMe() {
        return me;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public ItemStack getMeAsDrop()
    {
        lastAccessed = System.currentTimeMillis();
        ItemStack dropasClone = me.getItem();
        dropasClone = TitanBox.changeName(dropasClone, name);
        dropasClone.setAmount(1);
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + getMyId());
        ChatColor colorRotate = ChatColor.WHITE;
        ChatColor chatColor = ChatColor.GRAY;
        String tmp = "";
        lore.add(ChatColor.GRAY + " " + ChatColor.STRIKETHROUGH + "---------------------------");
        for(ItemStack it: storageItems)
        {
            if (it != null)
            {
                if (it.hasItemMeta())
                {
                    if (it.getItemMeta().hasDisplayName()) {
                        tmp = tmp + colorRotate + it.getItemMeta().getDisplayName() + " ";
                    }
                    else
                    {
                        tmp = tmp + colorRotate  +  it.getType().name() + " ";
                    }
                }
                else
                {
                    tmp = tmp + colorRotate  +  it.getType().name() + " ";
                }
            }
            else
            {
                tmp = tmp + chatColor +  "empty ";
            }
            if (colorRotate.equals(ChatColor.WHITE))
            {
                colorRotate = ChatColor.YELLOW;
                chatColor = ChatColor.DARK_GRAY;
            }
            else
            {
                colorRotate = ChatColor.WHITE;
                chatColor = chatColor.GRAY;
            }
            if (tmp.length() > 60)
            {
                lore.add(tmp);
                tmp = "";
            }
        }
        if (!tmp.equals("")) {
            lore.add(tmp);
        }
        OfflinePlayer oPlayer =Bukkit.getOfflinePlayer(owner);
        lore.add(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + oPlayer.getName());
        lore.add(ChatColor.BLUE + " " + ChatColor.STRIKETHROUGH + "---------------------------");
        lore.add(ChatColor.BLUE + "" + size + " J/s");

        dropasClone = TitanBox.addLore(dropasClone, lore);
        return dropasClone;

    }
    public void clearStorageCount(int Slot)
    {
        storageCount[Slot] = Long.valueOf(0);
        if (!isRemember()) {
            storageItems[Slot] = null;
        }
        if (storageItems[Slot] != null) {
            if (storageItems[Slot].getType().equals(Material.AIR)) {
                storageItems[Slot] = null;
            }
        }
    }
    public Long getStorageCount(int Slot)
    {
        Long count = Long.valueOf(0);
        if (storageItems[Slot] != null) {
            count = storageCount[Slot];
        }
        return count;

    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getBattery() {
        return battery;
    }

    public ItemStack getItem(int Slot, int count)
    {
        try {
            if (count > 0) {
                if (getCharge() < 1) {
                    return null;
                }
                checkPower(this);
                if (storageItems[Slot] != null) {
                    int charge = this.getCharge();
                    charge--;
                    this.setCharge(charge);
                    if (count <= storageCount[Slot]) {
                        lastAccessed = System.currentTimeMillis();
                        ItemStack gettting = storageItems[Slot].clone();
                        gettting.setAmount(count);
                        storageCount[Slot] = storageCount[Slot] - count;
                        if (storageCount[Slot].equals(Long.valueOf(0))) {
                            if (!isRemember()) {
                                storageItems[Slot] = null;
                            }
                            if (storageItems[Slot] != null) {
                                if (storageItems[Slot].getType().equals(Material.AIR)) {
                                    storageItems[Slot] = null;
                                }
                            }
                        }
                        saveMe();
                        return gettting.clone();
                    } else {
                        lastAccessed = System.currentTimeMillis();
                        count = Math.toIntExact(storageCount[Slot]);
                        ItemStack gettting = storageItems[Slot].clone();
                        gettting.setAmount(count);
                        storageCount[Slot] = Long.valueOf(0);
                        if (!isRemember()) {
                            storageItems[Slot] = null;
                        }
                        if (storageItems[Slot] != null) {
                            if (storageItems[Slot].getType().equals(Material.AIR)) {
                                storageItems[Slot] = null;
                            }
                        }
                        saveMe();
                        return gettting.clone();
                    }
                }
            }
            saveMe();
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public boolean isFull(int slot)
    {
        if (storageCount[slot] ==  Long.MAX_VALUE)
        {
            return true;
        }
        return false;
    }
    public ItemStack insertItem(ItemStack placing)
    {
        ItemStack placingClone = placing.clone();
        for (int i =0; i < storageItems.length; i++)
        {
           ItemStack stack = storageItems[i];
           if (TitanBox.isItemEqual(placingClone, stack))
           {
                Long canPlace = Long.MAX_VALUE - storageCount[i];
                if (canPlace >= placing.getAmount())
                {
                    storageCount[i] = storageCount[i] + Long.valueOf(placingClone.getAmount());
                    saveMe();
                    return null;
                }
                else {
                    storageCount[i] =  Long.MAX_VALUE;
                    Long OutValue = placingClone.getAmount() - canPlace;
                    placingClone.setAmount(Math.toIntExact(OutValue));
                    lastAccessed =System.currentTimeMillis();
                    saveMe();
                    return placingClone;
                }

           }
        }
        return placingClone;
    }
    public boolean insertItem(ItemStack placing, Long Amount)
    {
        ItemStack placingClone = placing.clone();
        for (int i =0; i < storageItems.length; i++)
        {
            ItemStack stack = storageItems[i];
            if (TitanBox.isItemEqual(placingClone, stack))
            {
                Long canPlace = Long.MAX_VALUE - storageCount[i];
                if (canPlace >= placing.getAmount())
                {
                    storageCount[i] = storageCount[i] + Amount;
                    saveMe();
                    return true;
                }
                else {
                    storageCount[i] =  Long.MAX_VALUE;
                    Long OutValue = Amount - canPlace;
                    placingClone.setAmount(Math.toIntExact(OutValue));
                    lastAccessed =System.currentTimeMillis();
                    saveMe();
                    return true;
                }

            }
        }
        return false;
    }
    private boolean addToEmpty(ItemStack placingClone, Long Amount) {
        if (!isLocked()) {
            for (int i = 0; i < storageItems.length; i++) {
                if (storageItems[i] == null) {
                    storageCount[i] = Amount;
                    placingClone.setAmount(1);
                    storageItems[i] = placingClone;
                    lastAccessed = System.currentTimeMillis();
                    saveMe();
                    return true;
                }
            }
        }
        return false;
    }
    private boolean addToEmpty(ItemStack placingClone) {
        if (!isLocked()) {
            for (int i = 0; i < storageItems.length; i++) {
                if (storageItems[i] == null) {
                    storageCount[i] = Long.valueOf(placingClone.getAmount());
                    placingClone.setAmount(1);
                    storageItems[i] = placingClone;
                    lastAccessed = System.currentTimeMillis();
                    saveMe();
                    return true;
                }
            }
        }
        return false;
    }

    public Long getLastAccessed() {
        return lastAccessed;
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size)
    {
        this.size = size;
        this.saveMe();
        this.loadMe();
    }
    public static void checkPower(StorageUnit newHold)
    {

        String ID = BlockStorage.getBlockInfo(newHold.getLocation(), "id");
        String energy =  BlockStorage.getBlockInfo(newHold.getLocation(), "energy-capacity");
        if (ID == null || energy == null)
        {
            addEnergy(newHold);
        }
    }
    public static void addEnergy(StorageUnit newHold) {
        BlockStorage.addBlockInfo(newHold.getLocation(), "id", "FOTStorageUnti");
        BlockStorage.addBlockInfo(newHold.getLocation(), "energy-capacity", newHold.getSize() + "");
    }
    public static void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        ItemStack item = event.getItemInHand();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "New Storage Unit, Size: ")) {
                        if ((GriefPrevention.instance.allowBuild(event.getPlayer(), event.getBlockPlaced().getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                            try {
                                event.setCancelled(true);
                                int size = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace("New Storage Unit, Size: ", ""));
                                StorageUnit newHold = new StorageUnit(StorageUnit.getNewIDString(), size, ItemHolder.getBySize(size), event.getPlayer());
                                newHold.setLocation(event.getBlock().getLocation());
                                StorageUnit.StorageById.put(newHold.getMyId(), newHold);
                                StorageUnit.StorageByLocation.put(newHold.getLocation().toString(), newHold);
                                List<StorageUnit> tmp = StorageUnit.StorageByOwner.get(newHold.getOwner().toString());
                                tmp.add(newHold);
                                StorageUnit.StorageByOwner.put(newHold.getOwner().toString(), tmp);

                                Bukkit.getScheduler().scheduleSyncDelayedTask(TitanBox.instants, new Runnable() {
                                    @Override
                                    public void run() {
                                        Block block = newHold.getLocation().getBlock();
                                        TitanBox.placeSkull(block, newHold.getMe().getTexute());
                                        addEnergy(newHold);
                                    }
                                }, 1);

                                if (item.getAmount() < 2) {
                                    event.getPlayer().getInventory().setItemInMainHand(null);
                                } else {
                                    item.setAmount(item.getAmount() - 1);
                                    event.getPlayer().getInventory().setItemInMainHand(item);
                                }


                            } catch (Exception e) {

                            }
                        }
                    }
                    if (item.getItemMeta().getDisplayName().equalsIgnoreCase(StorageUnit.name)) {
                        if ((GriefPrevention.instance.allowBuild(event.getPlayer(), event.getBlockPlaced().getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
                            if (item.getItemMeta().hasLore()) {
                                String id = ChatColor.stripColor(item.getItemMeta().getLore().get(0));
                                if (StorageUnit.StorageById.containsKey(id)) {
                                    StorageUnit tmp = StorageUnit.StorageById.get(id);
                                    if (tmp.getLocation() != null) {
                                        BlockStorage._integrated_removeBlockInfo(tmp.getLocation(), true);
                                        tmp.getLocation().getBlock().setType(Material.AIR);
                                        StorageUnit.StorageByLocation.remove(tmp.getLocation().toString());
                                    }
                                    tmp.setLocation(event.getBlock().getLocation());
                                    StorageUnit.StorageByLocation.put(tmp.getLocation().toString(), tmp);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TitanBox.instants, new Runnable() {
                                        @Override
                                        public void run() {
                                            addEnergy(tmp);
                                            tmp.setCharge(tmp.getCharge() + tmp.getBattery());
                                            tmp.setBattery(0);
                                        }
                                    }, 1);

                                }
                            }
                        }
                    }
                }
            }
        }

    }
    public static void reDrawStorage(Player player)
    {
        try {
            for (StorageUnit tmp : StorageUnit.StorageById.values()) {
                if (tmp.getOwner().toString().equals(player.getUniqueId().toString())) {
                    if (tmp.getLocation() != null) {
                        TitanBox.placeSkull(tmp.getLocation().getBlock(), tmp.getMe().getTexute());
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
    }
    public static boolean onBlockRemoveEvent(Block block)
    {
        String location = block.getLocation().toString();
        if (StorageUnit.StorageByLocation.containsKey(location)) {
            return true;
        }
        return false;
    }
    public static void onBlockBreakEvent(BlockBreakEvent event)
    {
        if ((GriefPrevention.instance.allowBreak(event.getPlayer(), event.getBlock(), event.getBlock().getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")) {
            String location = event.getBlock().getLocation().toString();
            if (StorageUnit.StorageByLocation.containsKey(location)) {
                StorageUnit tmp = StorageUnit.StorageByLocation.get(location);
                ItemStack drop = tmp.getMeAsDrop();
                tmp.setBattery(tmp.getCharge());
                BlockStorage._integrated_removeBlockInfo(tmp.getLocation(), true);
                tmp.setLocation(null);

                StorageUnit.StorageByLocation.remove(location);
                event.setDropItems(false);
                event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), drop);
                event.getPlayer().closeInventory();
            }
        }
    }
    public static void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory().getName().startsWith(StorageUnit.guiname)) {
            event.setCancelled(true);
            if (event.getWhoClicked() == null) {
                return;
            }
            String id = event.getInventory().getName().replace(StorageUnit.guiname + " ", "");
            StorageUnit tmp = StorageUnit.StorageById.get(id);
            if (tmp == null) {
                return;
            }
            tmp.gui.onInventoryClickEvent(event);
        }
    }
    public static void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null)
        {
            if (StorageUnit.StorageByLocation.containsKey(event.getClickedBlock().getLocation().toString()))
            {
                StorageUnit tmp = StorageUnit.StorageByLocation.get(event.getClickedBlock().getLocation().toString());
                checkPower(tmp);
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
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
                                boolean barcodeTrue = Boolean.valueOf(barcode);
                                if (barcodeTrue)
                                {
                                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "This is an invalid device! It has been duped and you have been reported.");
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mail send freethemice " + event.getPlayer().getName() + " has used a dupped Upgrade Device!");
                                    return;
                                }
                                TitanBox.setBarcodeTrue(item);

                                Random rnd = new Random(System.currentTimeMillis());
                                int addint = rnd.nextInt(7) + 1;
                                int cap = Math.min(tmp.getSize(), addint);
                                if (tmp.getSize() >= 45)
                                {
                                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Upgrade maxed out at " + ChatColor.WHITE + 45);
                                    return;
                                }
                                tmp.setSize(tmp.getSize()+ cap);
                                if (item.getAmount() < 2)
                                {
                                    event.getPlayer().getInventory().setItemInMainHand(null);
                                }
                                else
                                {
                                    item.setAmount(item.getAmount() - 1);
                                    event.getPlayer().getInventory().setItemInMainHand(item);
                                }
                                event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Upgrade completed! " + ChatColor.WHITE  + tmp.getSize() + ChatColor.GREEN + "/" + ChatColor.WHITE + 45);

                                return;
                            }
                        }
                    }
                }
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                {
                    tmp.OpenGui(event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }
    public static void onInventoryCloseEvent(InventoryCloseEvent event)
    {

        if (event.getInventory().getTitle().startsWith(StorageUnit.guiname)) {
            String id = event.getInventory().getTitle().replace(StorageUnit.guiname + " ", "");
            StorageUnit tmp = StorageUnit.StorageById.get(id);
            tmp.wipeGUI();
            tmp.saveMe();
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
        if (StorageUnit.StorageById.containsKey(saltStr))
        {
            return getNewIDString();
        }
        return saltStr;

    }
    public static void saveStorage() {
        for(StorageUnit key: StorageUnit.StorageById.values())
        {
            key.saveMe();
        }
        StorageUnit.storage.save();
    }
    public static void loadStorage()
    {
        int count = 0;
        int removedcount = 0;
        Set<String> keys = StorageUnit.storage.getKeys();
        for(String key: keys)
        {
            String id = StorageUnit.storage.getString(key + ".id");
            StorageUnit newHolder = new StorageUnit(id);

            Long Month = Long.valueOf("2592000000");
            Month = System.currentTimeMillis() - Month;
            if (newHolder.getLastAccessed() > Month) {
                StorageUnit.StorageById.put(id, newHolder);
                if (newHolder.getLocation() != null) {
                    StorageUnit.StorageByLocation.put(newHolder.getLocation().toString(), newHolder);
                }
                count++;
            }
            else
            {
                removedcount++;
            }
        }

        System.out.println("[TitanBox]: loaded: " + count + ", removed out of date: " + removedcount);



    }
    public static List<StorageUnit> getStorageFromOwner(UUID uuid)
    {
        List<StorageUnit> tmp = StorageUnit.StorageByOwner.get(uuid.toString());
        if (tmp ==null)
        {
            tmp = new ArrayList<StorageUnit>();
        }
        return tmp;
    }

    @Override
    public void onInventoryClickEvent(InventoryClickEvent event, buttonGUIs button) {
        int slot = event.getRawSlot();
        if (slot > -1 && slot < 54) {
            if (slot == 45)
            {
                for (int i = 0; i < 36; i++) {
                    ItemStack item =event.getWhoClicked().getInventory().getItem(i);
                    if (!TitanBox.isEmpty(item)) {

                        ItemStack left = this.insertItem(item);
                        if (!TitanBox.isEmpty(left)) {
                            if (left.getAmount() == item.getAmount()) {
                                if (!TitanBox.checkStorageForItem(event.getWhoClicked().getUniqueId(), left)) {
                                    if (addToEmpty(left)) left = null;
                                }
                            }
                        }

                        event.getWhoClicked().getInventory().setItem(i, left);
                    }
                }
                saveMe();
                return;
            }
            if (slot == 46)
            {
                this.setRemember(!this.isRemember());
                button.setToggle(buttonEnum.getType(this.isRemember()));
                saveMe();
                return;
            }
            if (slot == 47)
            {
                this.setLocked(!this.isLocked());
                button.setToggle(buttonEnum.getType(this.isLocked()));
                saveMe();
                return;
            }
            if (slot == 48)
            {
                boolean havebattery = false;
                for (int i = 0; i < 36; i++) {
                    ItemStack checkItem = event.getWhoClicked().getInventory().getItem(i);
                    if (!TitanBox.isEmpty(checkItem)) {
                        if (TitanBox.isItemEqual(SlimefunItems.BATTERY, checkItem))
                        {
                            if (checkItem.getAmount() < 2)
                            {
                                event.getWhoClicked().getInventory().setItem(i, null);
                            }else {
                                checkItem.setAmount(checkItem.getAmount() - 1);
                                event.getWhoClicked().getInventory().setItem(i, checkItem);
                            }
                            havebattery = true;
                            break;
                        }
                    }
                }
                if (havebattery) {
                    String power = BlockStorage.getBlockInfo(this.getLocation(), "energy-charge");
                    int powerA = 0;
                    try {
                        powerA = Integer.parseInt(power);
                    }
                    catch (Exception e)
                    {

                    }
                    powerA = powerA + 40;
                    BlockStorage.addBlockInfo(this.getLocation(), "energy-charge", powerA + "");
                }
                saveMe();
                reBuildMenu();
                return;
            }
            if (slot == 53)
            {
                this.setMoving(!this.isMoving());
                button.setToggle(buttonEnum.getType(this.isMoving()));
                saveMe();
                reBuildMenu();
                return;
            }
            if (slot > this.getSize())
            {
                return;
            }
            ItemStack item = this.viewSlot(slot);
            boolean superStorage = false;
            int Amount = 64;
            if (event.isShiftClick())
            {
                Amount = 256;
            }
            if (event.isRightClick())
            {
                if (Amount == 256)
                {
                    superStorage = true;
                }
                Amount = 1;
                if (storageCount[slot] == 0)
                {
                    storageItems[slot] = null;
                    saveMe();
                }
            }
            if (BlockStorage.getBlockInfo(this.getLocation()) != null) {
                if (!this.isMoving()) {
                    if (!superStorage) {
                        ItemStack IS = this.getItem(slot, Amount);
                        if (IS != null) {
                            event.getWhoClicked().getInventory().addItem(IS);
                            for (int i = 0; i < 36; i++) {
                                ItemStack checkItem = event.getWhoClicked().getInventory().getItem(i);
                                if (!TitanBox.isEmpty(checkItem)) {
                                    if (checkItem.getAmount() > checkItem.getMaxStackSize()) {
                                        int dif = checkItem.getAmount() - checkItem.getMaxStackSize();
                                        checkItem.setAmount(checkItem.getMaxStackSize());
                                        //make sure we don't go over the bigMax stack size, like arrmor, things over 64 doesn't matter...
                                        event.getWhoClicked().getInventory().setItem(i, checkItem.clone());
                                        while (dif > 0) {
                                            dif = dif - checkItem.getMaxStackSize();
                                            event.getWhoClicked().getInventory().addItem(checkItem.clone());
                                        }
                                    }
                                }
                            }

                            saveMe();
                        }
                    }
                    else
                    {
                        Long allCoung = this.getStorageCount(slot);
                        ItemStack IS = this.viewSlot(slot);
                        this.clearStorageCount(slot);
                        if (IS != null) {
                            if (!allCoung.equals(Long.valueOf(0)))
                            {
                                ItemStack itemStackOut = TitanBox.getSuperItemHolder(IS, allCoung);
                                if (!TitanBox.isEmpty(itemStackOut)) {
                                    event.getWhoClicked().getInventory().addItem(itemStackOut);
                                }
                            }
                        }
                        saveMe();
                    }
                }
                else
                {
                    if (this.from < 0)
                    {
                        this.from = event.getRawSlot();

                    }
                    else
                    {
                        this.moveSlot(this.from, event.getRawSlot());
                        this.from = -1;
                    }
                    saveMe();
                }
            }

        }
        else
        {
            if (slot > 53 && slot < 90) {
                ItemStack item = event.getClickedInventory().getItem(event.getSlot());
                ItemStack left = null;
                if (!TitanBox.isEmpty(item)) {
                    if (TitanBox.isSuperItemHoler(item))
                    {
                        Object[] results = TitanBox.returnSuperItemHolder(item);
                        if (results != null)
                        {
                            if (results[0] instanceof ItemStack && results[1] instanceof  Long)
                            {
                                ItemStack toInsert = (ItemStack)results[0];
                                Long toAmount = (Long)results[1];
                                boolean found = this.insertItem(toInsert, toAmount);
                                if (!found)
                                {
                                    this.addToEmpty(toInsert, toAmount);
                                }
                            }
                        }
                    }
                    else {
                        left = this.insertItem(item);
                    }
                    if (!TitanBox.isEmpty(left)) {
                        if (left.getAmount() == item.getAmount()) {
                            if (!TitanBox.checkStorageForItem(event.getWhoClicked().getUniqueId(), left)) {
                                if (addToEmpty(left)) left = null;
                            }
                            else
                            {
                                event.getWhoClicked().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "That item is in another storage unit!");
                            }
                        }
                    }

                    event.getWhoClicked().getInventory().setItem(event.getSlot(), left);

                    saveMe();
                }
            }
        }

    }
}

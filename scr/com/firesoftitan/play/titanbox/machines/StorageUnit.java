package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.buttonEnum;
import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import com.firesoftitan.play.titanbox.guis.mainGUI;
import com.firesoftitan.play.titanbox.holders.ItemHolder;
import com.firesoftitan.play.titanbox.interfaces.InventoryHolder;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.StorageGuiRunnable;
import com.firesoftitan.play.titansql.CallbackResults;
import com.firesoftitan.play.titansql.DataTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.Table;
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
    private boolean needsaving = false;
    private boolean remember = true;
    private boolean locked = false;
    private boolean moving = false;
    private int from = -1;
    public static HashMap<String, StorageUnit> StorageById = new HashMap<String, StorageUnit>();
    public static HashMap<String, StorageUnit>  StorageByLocation = new HashMap<String, StorageUnit>();
    public static HashMap<UUID, List<StorageUnit>>  StorageByOwner = new HashMap<UUID, List<StorageUnit>>();
    //public static Config storage = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "storage.yml");
    public static Table storageSQL = new Table("tb_storage");

    public StorageUnit(String MynewId)
    {
        MyId = MynewId;
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

    public void saveMeNext()
    {
        needsaving = true;
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
                type = ChatColor.WHITE + TitanBox.getName(interfaceItem);

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
                String[] lore = {ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "---------------------------", ChatColor.YELLOW + "Name: " + type, ChatColor.YELLOW + "Stock: " + ChatColor.WHITE + TitanBox.formatCommas(storageCount[i]), ChatColor.YELLOW + "Capacity: " +storpec, ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "---------------------------",ChatColor.AQUA + "Left Click: " + ChatColor.WHITE + "64", ChatColor.AQUA + "Right Click: " + ChatColor.WHITE + "1", ChatColor.AQUA + "Shift Left Click: " + ChatColor.WHITE + "4x64 (256)", ChatColor.AQUA + "Shift Right Click: " + ChatColor.WHITE + "ALL (SIM)", ChatColor.AQUA + "Right Click Empty: " + ChatColor.WHITE + "Remove"};

                if (interfaceItem.hasItemMeta())
                {
                    if (interfaceItem.getItemMeta().hasLore())
                    {
                        List<String> loreList = interfaceItem.getItemMeta().getLore();
                        String[] insertlore = new String[loreList.size()];
                        for(int ii = 0; ii < loreList.size(); ii++)
                        {
                            insertlore[ii] =  ChatColor.DARK_PURPLE + loreList.get(ii);
                        }
                        List<String> rework = new ArrayList<String>();
                        rework.add(lore[0]);
                        rework.add(lore[1]);
                        for(String e: insertlore)
                        {
                            if (!ChatColor.stripColor(e.replace(" ", "")).equals("")) {
                                rework.add("          " + e);
                            }
                        }
                        for (int o = 2; o < lore.length; o++)
                        {
                            rework.add(lore[o]);
                        }
                        lore = new String[rework.size()];
                        lore = rework.toArray(lore);

                    }
                }
                buttonGUIs tmp = gui.getButton(i);
                tmp.setMaterialFalse(interfaceItem);
                tmp.setNameFalse("Stored Item");
                tmp.addLore(lore);
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
        this.saveMeNext();
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
    public static void setupTable()
    {

        storageSQL.addDataType("id", DataTypeEnum.CHARARRAY, true, false, true);
        storageSQL.addDataType("owner", DataTypeEnum.UUID, false, false, false);
        storageSQL.addDataType("size", DataTypeEnum.INTEGER, false, false, false);
        storageSQL.addDataType("accessed", DataTypeEnum.LONG, false, false, false);
        storageSQL.addDataType("me", DataTypeEnum.STRING, false, false, false);
        storageSQL.addDataType("location", DataTypeEnum.LOCATION, false, false, false);
        storageSQL.addDataType("remember", DataTypeEnum.BOOLEAN, false, false, false);
        storageSQL.addDataType("locked", DataTypeEnum.BOOLEAN, false, false, false);
        storageSQL.addDataType("battery", DataTypeEnum.INTEGER, false, false, false);
        storageSQL.addDataType("items", DataTypeEnum.ITEMLIST, false, false, false);
        storageSQL.addDataType("counts", DataTypeEnum.STRINGLIST, false, false, false);
        storageSQL.createTable();
    }
    public void saveMe()
    {
        if (needsaving) {
            storageSQL.setDataField("id", getMyId());
            storageSQL.setDataField("owner", getOwner());
            storageSQL.setDataField("size", getSize());
            storageSQL.setDataField("accessed", getLastAccessed());
            storageSQL.setDataField("me", me.toString());
            storageSQL.setDataField("location", getLocation());
            storageSQL.setDataField("remember", isRemember());
            storageSQL.setDataField("locked", isLocked());
            storageSQL.setDataField("battery", battery);

            List<ItemStack> itemList = new ArrayList<ItemStack>();
            List<String> countList = new ArrayList<String>();
            for (int i = 0; i < storageItems.length; i++) {
                if (storageItems[i] != null) {
                    if (!storageItems[i].getType().equals(Material.AIR)) {
                        itemList.add(storageItems[i].clone());
                        countList.add(storageCount[i] + "");
                    }
                }
            }


            storageSQL.setDataField("items", itemList);
            storageSQL.setDataField("counts", countList);
            storageSQL.insertData();
            needsaving = false;
        }

        /*
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
        StorageUnit.storage.setValue(getMyId() + ".counts",  countList);*/
    }
    public void loadMe(HashMap<String, ResultData> result)
    {
        battery = result.get("battery").getInteger();
        size = result.get("size").getInteger();
        owner = result.get("owner").getUUID();

        storageItems = new ItemStack[size];
        storageCount = new Long[size];
        List<ItemStack> itemList = result.get("items").getItemList();
        List<String> itemLongs = result.get("counts").getStringList();
        location = null;

        locked =  result.get("locked").getBoolean();
        remember =  result.get("remember").getBoolean();
        lastAccessed =  result.get("accessed").getLong();

        for (int i = 0; i < storageItems.length; i++) {
            if (itemList != null) {
                if (itemList.size() > i) {
                    storageItems[i] = itemList.get(i).clone();
                    storageItems[i].setAmount(1);
                    storageCount[i] = Long.valueOf(itemLongs.get(i));
                }
            }
        }

        me = ItemHolder.valueOf(result.get("me").getString());
        location = result.get("location").getLocation();
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
        this.saveMeNext();
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
        return  getItem(Slot, count, false);
    }
    public ItemStack getItem(int Slot, int count, boolean byPassPower)
    {
        try {
            if (count > 0) {
                if (!byPassPower) {
                    if (getCharge() < 1) {
                        return null;
                    }
                    checkPower(this);
                }
                if (storageItems[Slot] != null) {
                    if (!byPassPower) {
                        int charge = this.getCharge();
                        charge--;
                        this.setCharge(charge);
                    }
                    if (count <= storageCount[Slot]) {
                        lastAccessed = System.currentTimeMillis();
                        ItemStack gettting = storageItems[Slot].clone();
                        if (!TitanBox.isArmor(gettting) && !TitanBox.isWeapon(gettting))
                        {
                            gettting.setDurability((short) 0);
                        }
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
                        saveMeNext();
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
                        saveMeNext();
                        return gettting.clone();
                    }
                }
            }
            saveMeNext();
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
                    saveMeNext();
                    return null;
                }
                else {
                    storageCount[i] =  Long.MAX_VALUE;
                    Long OutValue = placingClone.getAmount() - canPlace;
                    placingClone.setAmount(Math.toIntExact(OutValue));
                    lastAccessed =System.currentTimeMillis();
                    saveMeNext();
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
                    saveMeNext();
                    return true;
                }
                else {
                    storageCount[i] =  Long.MAX_VALUE;
                    Long OutValue = Amount - canPlace;
                    placingClone.setAmount(Math.toIntExact(OutValue));
                    lastAccessed =System.currentTimeMillis();
                    saveMeNext();
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
                    saveMeNext();
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
                    saveMeNext();
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
        ItemStack[] temp = storageItems.clone();
        Long[] tempL = storageCount.clone();
        storageItems = new ItemStack[size];
        storageCount = new Long[size];
        for (int i = 0; i <temp.length; i++)
        {
            if (temp[i] != null) storageItems[i] = temp[i].clone();
            storageCount[i] = tempL[i];

        }
        this.saveMeNext();
    }
    public static void checkPower(StorageUnit newHold)
    {
        try {
            if (newHold != null) {
                if (newHold.getLocation() != null) {
                    String ID = BlockStorage.getBlockInfo(newHold.getLocation(), "id");
                    String energy = BlockStorage.getBlockInfo(newHold.getLocation(), "energy-capacity");
                    if (ID == null || energy == null) {
                        addEnergy(newHold);
                    }
                }
            }
        }catch (Exception e)
        {
            System.out.println("----------Power Check Fail Report: Start-------");
            e.printStackTrace();
            System.out.println(newHold.getMyId());
            System.out.println(newHold.getOwner());
            System.out.println(newHold.getLocation());
            System.out.println("----------Power Check Fail Report: End-------");
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
                                List<StorageUnit> tmp = StorageUnit.StorageByOwner.get(newHold.getOwner());
                                tmp.add(newHold);
                                StorageUnit.StorageByOwner.put(newHold.getOwner(), tmp);

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
                                    if (tmp.getOwner().toString().equals(event.getPlayer().getUniqueId().toString()) ||  event.getPlayer().hasPermission("titanbox.admin"))
                                    {
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
                                    else
                                    {
                                        event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "You are not the owner of this unit, so you can't place it. The Owner is " + ChatColor.WHITE + Bukkit.getOfflinePlayer(tmp.getOwner()).getName());
                                    }
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
                                Random rnd = new Random(System.currentTimeMillis());
                                int addint = rnd.nextInt(7) + 1;
                                int cap = Math.min(tmp.getSize(), addint);
                                if (tmp.getSize() >= 45)
                                {
                                    event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Upgrade maxed out at " + ChatColor.WHITE + 45);
                                    return;
                                }

                                boolean barcodeTrue = Boolean.valueOf(barcode);
                                if (barcodeTrue)
                                {
                                    TitanBox.duppedAlert(event.getPlayer(), item);
                                    return;
                                }
                                TitanBox.setBarcodeTrue(item, event.getPlayer());


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
            tmp.saveMeNext();
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

        for(StorageUnit SU: StorageUnit.StorageById.values())
        {
            SU.saveMe();
        }
       /* for(StorageUnit key: StorageUnit.StorageById.values())
        {
            key.saveMe();
        }
        StorageUnit.storage.save();*/
    }

    public static void loadStorage() {
        setupTable();
        loadStorageB();
    }
    public static void loadStorageB()
    {

        int count = 0;
        int removedcount = 0;
        Utilities.reTryLoad(storageSQL, StorageUnit.class,"loadStorageB", "Storage");
        storageSQL.search(new CallbackResults() {
            @Override
            public void onResult(List<HashMap<String, ResultData>> results) {
                int count = 0;
                int removedcount = 0;
                if (results != null && results.size() > 0) {
                    for (HashMap<String, ResultData> result : results) {
                        String id = result.get("id").getString();
                        StorageUnit newHolder = new StorageUnit(id);
                        newHolder.loadMe(result);

                        Long Month = Long.valueOf("2592000000") * 4;
                        Month = System.currentTimeMillis() - Month;
                        if (newHolder.getLastAccessed() > Month) {
                            StorageUnit.StorageById.put(id, newHolder);
                            if (newHolder.getLocation() != null) {
                                StorageUnit.StorageByLocation.put(newHolder.getLocation().toString(), newHolder);
                            }
                            List<StorageUnit> tmp = StorageUnit.StorageByOwner.get(newHolder.getOwner());
                            if (tmp == null)
                            {
                                tmp = new ArrayList<StorageUnit>();
                            }
                            tmp.add(newHolder);
                            StorageUnit.StorageByOwner.put(newHolder.getOwner(), tmp);
                            count++;
                        } else {
                            removedcount++;
                        }

                    }
                    System.out.println("[TitanBox]: Storage loaded: " + count + ", removed out of date: " + removedcount + ", owners online: " + StorageUnit.StorageByOwner.size());
                }
                Utilities.doneTryLoading(storageSQL);
                MainModule.loadAllModules();
            }
        });



    }
    public static List<StorageUnit> getStorageFromOwner(UUID uuid)
    {
        List<StorageUnit> tmp = StorageUnit.StorageByOwner.get(uuid);
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
                saveMeNext();
                return;
            }
            if (slot == 46)
            {
                this.setRemember(!this.isRemember());
                button.setToggle(buttonEnum.getType(this.isRemember()));
                saveMeNext();
                return;
            }
            if (slot == 47)
            {
                this.setLocked(!this.isLocked());
                button.setToggle(buttonEnum.getType(this.isLocked()));
                saveMeNext();
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
                saveMeNext();
                reBuildMenu();
                return;
            }
            if (slot == 53)
            {
                this.setMoving(!this.isMoving());
                button.setToggle(buttonEnum.getType(this.isMoving()));
                saveMeNext();
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
                    saveMeNext();
                }
            }
            if (BlockStorage.getBlockInfo(this.getLocation()) != null) {
                if (!this.isMoving()) {
                    if (!superStorage) {
                        ItemStack IS = this.getItem(slot, Amount);
                        if (!TitanBox.isEmpty(IS))
                        {
                            ItemStack leftOver = TitanBox.addItemsToPlayer((Player) event.getWhoClicked(), IS);
                            if (!TitanBox.isEmpty(leftOver))
                            {
                                event.getWhoClicked().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Your Inventory is full placing back in storage!");
                                TitanBox.addItemToStorage(event.getWhoClicked().getUniqueId(), leftOver);
                            }
                        }
                        saveMeNext();

                      /*  if (IS != null) {
                            event.getWhoClicked().getInventory().addItem(IS);
                            for (int i = 0; i < 36; i++) {
                                ItemStack checkItem = event.getWhoClicked().getInventory().getItem(i);
                                if (!TitanBox.isEmpty(checkItem)) {
                                    if (checkItem.getAmount() > checkItem.getMaxStackSize()) {
                                        int dif = checkItem.getAmount() - checkItem.getMaxStackSize();
                                        checkItem.setAmount(checkItem.getMaxStackSize());
                                        //make sure we don't go over the bigMax stack size, like arrmor, things over 64 doesn't matter...
                                        event.getWhoClicked().getInventory().setItem(i, checkItem.clone());
                                        Long killtime = System.currentTimeMillis();
                                        while (dif > 0) {
                                            dif = dif - checkItem.getMaxStackSize();
                                            event.getWhoClicked().getInventory().addItem(checkItem.clone());
                                            if (System.currentTimeMillis() - killtime > 1000)
                                            {
                                                System.out.println("[Titan Bot]: StorageUnit.java 1177: Took to long");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            saveMeNext();
                        }*/
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
                        saveMeNext();
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
                    saveMeNext();
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

                    saveMeNext();
                }
            }
        }

    }
}

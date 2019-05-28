package com.firesoftitan.play.titanbox.machines;


import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ButtonEnum;
import com.firesoftitan.play.titanbox.guis.ButtonGUIs;
import com.firesoftitan.play.titanbox.guis.MainGUI;
import com.firesoftitan.play.titanbox.managers.ConfigManager;
import com.firesoftitan.play.titanbox.managers.RouterManager;
import com.firesoftitan.play.titanbox.interfaces.InventoryInterface;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.GUIRouterRunnable;
import com.firesoftitan.play.titanbox.runnables.IRRUserRunnable;
import com.firesoftitan.play.titansql.ResultData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemRoutingRouter implements InventoryInterface {
    private int max = 3;
    private int startIndex = 0;
    private List<MainModule> modules;
    private UUID owner;
    private String ID = "";
    private MainGUI gui;
    private GUIRouterRunnable guiRouterRunnable;
    private Long lastTick = Long.valueOf(0);
    private boolean needsaving =true;

    private List<MainModule> buffer = new ArrayList<MainModule>();


    public ItemRoutingRouter(String ID)
    {
        this.ID = ID;
        modules = new ArrayList<MainModule>();
    }

    public int getLastTick() {
        Double time = Double.valueOf(System.currentTimeMillis() - lastTick);
        int i = (int)(time / 1000f);
        return i;
    }
    private void resetBuffer()
    {
        buffer.clear();
        buffer.addAll(modules);
    }
    public MainModule getNextBuffered()
    {
        if (!hasModules())
        {
            return null;
        }
        if (buffer.size() == 0)
        {
            resetBuffer();
        }

        MainModule next = buffer.get(0);
        buffer.remove(0);
        return next;
    }
    public void setLastTick() {
        this.lastTick = System.currentTimeMillis();
    }

    public void createNewGUI()
    {
        gui = new MainGUI(54, RouterManager.name + ":" + this.ID, this);
        for (int i = 0; i < 54; i++)
        {
            ButtonGUIs newButton = new ButtonGUIs(gui, i);
            gui.addButton(newButton, i);
        }
        buildGUISlots();
        buildGUIMenu();

        guiRouterRunnable = new GUIRouterRunnable();
        guiRouterRunnable.setItemRoutingRouter(this);
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, guiRouterRunnable, 1, 1);
        guiRouterRunnable.setTimerID(id);

    }
    public void showGUI(Player player)
    {
        createNewGUI();
        gui.showGUI(player);
        //player.openInventory(gui.getMyGui());
    }
    public void buildGUIMenu()
    {
        int Paget = (startIndex + 9) / 9;
        //45-53
        ButtonGUIs tmp =gui.getButton(45);
        tmp.setTextureTrue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19");
        tmp.setNameTrue("Last");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmFiYWQ5MjRiMjIzNzJiYzk2NmE2ZDUxN2QyZjFiOGI1N2ZkZDI2MmI0ZTA0ZjQ4MzUyZTY4M2ZmZjkyIn19fQ==");
        tmp.setNameFalse("x");
        if (startIndex == 0)
        {
            tmp.setToggle(ButtonEnum.FALSE);
        }else
        {
            tmp.setToggle(ButtonEnum.TRUE);
        }
        updateStatusButton();


        ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        blank = Utilities.changeName(blank, "-");
        for (int i = 46; i < 52; i++)
        {
            tmp = new ButtonGUIs(gui, i);
            tmp.setBlank(blank.clone());
            tmp.setToggle(ButtonEnum.BLANK);
            gui.addButton(tmp, tmp.getSlot());
        }

        tmp =gui.getButton(53);
        tmp.setTextureTrue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY3NDE2Y2U5ZTgyNmU0ODk5YjI4NGJiMGFiOTQ4NDNhOGY3NTg2ZTUyYjcxZmMzMTI1ZTAyODZmOTI2YSJ9fX0=");
        tmp.setNameTrue("Next");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0=");
        tmp.setNameFalse("x");

        if (startIndex + 45 >= ConfigManager.getRouter_BigMax())
        {
            tmp.setToggle(ButtonEnum.FALSE);
        }else
        {
            tmp.setToggle(ButtonEnum.TRUE);
        }



    }

    public void updateStatusButton() {
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(owner);
        ButtonGUIs tmp;
        tmp =gui.getButton(52);
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM4OGQ2MTYzZmFiZTdjNWU2MjQ1MGViMzdhMDc0ZTJlMmM4ODYxMWM5OTg1MzZkYmQ4NDI5ZmFhMDgxOTQ1MyJ9fX0=");
        tmp.setNameFalse(ChatColor.GREEN + "Owner: " + ChatColor.WHITE + oPlayer.getName());
        int ticks = getLastTick();
        String Status = ChatColor.RED + "Offline!";
        OfflinePlayer ownerPalyer = Bukkit.getOfflinePlayer(this.getOwner());
        //int charge = RouterManager.getCharge(this.getLocation());
        List<String> allLore = new ArrayList<String>();
        boolean running = true;
        if (ticks > 60)
        {
            IRRUserRunnable runnerTimer = RouterManager.bufferListT.get(this.getOwner());
            if (runnerTimer == null)
            {
                allLore.add(ChatColor.RED + "Warning: " + ChatColor.YELLOW + "Log Out and Back In!");
            } else if (runnerTimer.getCountDown() > -1) {
                int left = 50 - runnerTimer.getCountDown();
                if (left < 0)
                {
                    allLore.add(ChatColor.RED + "Warning: " + ChatColor.YELLOW + "Starting in: " + ChatColor.WHITE + "soon" + ChatColor.YELLOW + " sec!");
                }
                else {
                    allLore.add(ChatColor.RED + "Warning: " + ChatColor.YELLOW + "Starting in: " + ChatColor.WHITE + left + ChatColor.YELLOW + " sec!");
                }
            } else {
                allLore.add(ChatColor.RED + "Warning: " + ChatColor.YELLOW + "Server Error!");
            }
            running = false;
        }
        /*
        if (charge == 0)
        {
            allLore.add(ChatColor.RED + "Warning: " + ChatColor.YELLOW + "No Power!");
            running = false;
        }*/
        if (ownerPalyer == null || !ownerPalyer.isOnline())
        {
            allLore.add(ChatColor.RED + "Warning: " + ChatColor.YELLOW + "Owner Offline!");
            running = false;
        }
        if (!this.hasModules())
        {
            allLore.add(ChatColor.RED + "Warning: " + ChatColor.YELLOW + "No Modules!");
            running = false;
        }

        if (running)
        {
            Status = ChatColor.GREEN +  "Running!";
        }
        allLore.add(0, ChatColor.WHITE + "Status: " + Status);
        tmp.setLore(allLore);

        tmp.setToggle(ButtonEnum.FALSE);
    }

    public void buildGUISlots()
    {

        int slot = 0;
        List<MainModule> modules = this.getModules();
        int max = Math.min(startIndex + 45, modules.size());
        for(int i = startIndex; i<max;i++)
        {
            try {
                ButtonGUIs tmp = gui.getButton(slot);
                ItemStack copyMe = new ItemStack(Material.BARRIER, 1);//MainModule.getItemfromModule(modules.get(i));
                try {
                    copyMe = MainModule.getItemfromModule(modules.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tmp.setMaterialTrue(copyMe);
                tmp.setNameTrue(copyMe.getItemMeta().getDisplayName());
                tmp.setLore(copyMe.getItemMeta().getLore());
                tmp.setToggle(ButtonEnum.TRUE);
                slot++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ItemStack blank = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        blank = Utilities.changeName(blank, "Click Module To Add it Here!");
        max = Math.min(startIndex + 45, this.getMax());
        for (int i = startIndex + slot; i<max;i++)
        {
            ButtonGUIs tmp = gui.getButton(slot);
            tmp.setMaterialFalse(blank.clone());
            tmp.setToggle(ButtonEnum.FALSE);
            slot++;
        }
        blank = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        blank = Utilities.changeName(blank, "You can add more slots here!");
        max = Math.min(startIndex + 45, ConfigManager.getRouter_BigMax());
        for (int i = startIndex + slot; i<max;i++)
        {

            ButtonGUIs tmp = gui.getButton(slot);
            tmp.setMaterialFalse(blank.clone());
            tmp.setToggle(ButtonEnum.FALSE);
            slot++;

        }
        blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        blank = Utilities.changeName(blank, "-");
        for (int i = slot; i < 45; i++)
        {
            ButtonGUIs tmp = new ButtonGUIs(gui, i);
            tmp.setBlank(blank.clone());
            tmp.setToggle(ButtonEnum.BLANK);
            gui.addButton(tmp, tmp.getSlot());
        }
    }
    public String getID() {
        return ID;
    }

    public void  clearGui()
    {
        gui = null;
    }
    public MainGUI getGui() {
        return gui;
    }


    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    public boolean hasModules()
    {
        if (modules == null)
        {
            return false;
        }
        if (modules.size() > 0)
        {
            return true;
        }
        return false;
    }
    public List<MainModule> getModules() {
        return modules;
    }

    public void setModules(List<MainModule> modules) {
        this.modules = modules;
    }
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public static void saveAll()
    {
        try {
            for(ItemRoutingRouter iRR: RouterManager.routersByID.values())
            {
                iRR.saveMe();
            }
        } catch (Exception e) {
        }
    }
    public static void loadRRH(HashMap<String, ResultData> result)
    {
        String key = result.get("id").getString();
        List<String> tmp = result.get("modules").getStringList();
        UUID owner = result.get("owner").getUUID();
        int size = result.get("size").getInteger();
        //Location location = result.get("location").getLocation();
        ItemRoutingRouter tmpRouter = new ItemRoutingRouter(key);
        tmpRouter.setOwner(owner);
        tmpRouter.setMax(size);
        List<MainModule> tmpMH = new ArrayList<MainModule>();
        if (tmp != null) {
            for (String tmpKey : tmp) {
                if (MainModule.getModulefromID(tmpKey) != null) {
                    tmpMH.add(MainModule.getModulefromID(tmpKey));
                }
            }
        }
        tmpRouter.setModules(tmpMH);
        RouterManager.routersByID.put(key, tmpRouter);
        RouterManager.routersByOwner.put(tmpRouter.getOwner(), tmpRouter);
        if (result.containsKey("location") && result.get("location") != null && result.get("location").getLocation() != null)
        {
            RouterManager.addRouterLocation(result.get("location").getLocation());
        }

    }
    public void needSaving()
    {
        needsaving = true;
    }
    public void saveMe()
    {
        if (needsaving) {
            needsaving = false;
            List<String> keys = new ArrayList<String>();
            for (MainModule mm : this.getModules()) {
                keys.add(mm.getModuleid());
            }
            RouterManager.routingSQL.setDataField("id", this.getID());
            RouterManager.routingSQL.setDataField("modules", keys);
            RouterManager.routingSQL.setDataField("owner", this.getOwner());
            RouterManager.routingSQL.setDataField("size", this.getMax());
            RouterManager.routingSQL.setDataField("location", new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
            RouterManager.routingSQL.insertData();
        }
    }
    @Override
    public void onInventoryClickEvent(InventoryClickEvent event, ButtonGUIs button) {

        if (button != null)
        {
            if (button.getSlot() < 45) {
                if (button.getSlot() + startIndex< modules.size()) {
                    try {
                        MainModule geting = modules.get(button.getSlot() + startIndex);
                        ItemStack hereItem = MainModule.getItemfromModule(geting);
                        modules.remove(button.getSlot() + startIndex);
                        this.needSaving();
                        event.getWhoClicked().getInventory().addItem(hereItem);
                        buildGUISlots();
                    } catch (Exception e) {
                        modules.remove(button.getSlot() + startIndex);
                        this.needSaving();
                        buildGUISlots();
                    }
                }
            }
            if (button.getSlot() == 45)
            {
                startIndex = startIndex - 9;
                if (startIndex < 0)
                {
                    startIndex = 0;
                }
                buildGUISlots();
                buildGUIMenu();
            }
            if (button.getSlot() == 53)
            {
                startIndex = startIndex + 9;
                if (startIndex + 45 >= ConfigManager.getRouter_BigMax()) {
                    if (startIndex + 45 -9 >= ConfigManager.getRouter_BigMax())
                    {
                        startIndex = startIndex - 9;
                    }
                }
                buildGUISlots();
                buildGUIMenu();
            }
        }
        else
        {
            ItemStack hereItem = event.getWhoClicked().getInventory().getItem(event.getSlot());
            MainModule moduleHolder = MainModule.getModulefromItem(hereItem);
            if (moduleHolder != null)
            {
                if (moduleHolder.getModuleid() != null)
                {

                    int max = Math.min(ConfigManager.getRouter_BigMax(), this.getMax());

                    for (int i = 0; i < modules.size(); i++) {
                        MainModule tmpMH = modules.get(i);
                        if (tmpMH.getModuleid().equals(moduleHolder.getModuleid())) {
                            modules.remove(i);
                            event.getWhoClicked().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module removed from router." + ChatColor.WHITE + "(" + modules.size() + "/" + ConfigManager.getRouter_BigMax() + ")");
                            this.needSaving();
                            return;
                        }
                    }
                    if (modules.size() >= max) {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.RED + "Only " + ChatColor.WHITE + max + ChatColor.RED + " is allowed in this unit.");
                        return;
                    }


                    modules.add(moduleHolder);
                    this.needSaving();
                    event.getWhoClicked().getInventory().setItem(event.getSlot(), null);
                    buildGUISlots();
                }
            }
        }

    }
}

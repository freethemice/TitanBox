package com.firesoftitan.play.titanbox.machines;


import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.buttonEnum;
import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import com.firesoftitan.play.titanbox.guis.mainGUI;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import com.firesoftitan.play.titanbox.interfaces.InventoryHolder;
import com.firesoftitan.play.titanbox.modules.MainModule;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemRoutingRouter implements InventoryHolder {
    private int max = 3;
    private int startIndex = 0;
    private List<MainModule> modules;
    private UUID owner;
    private Location location;
    private String ID = "";
    private mainGUI gui;
    private Long lastTick = Long.valueOf(0);
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
        gui = new mainGUI(54, RouterHolder.name + ":" + this.ID, this);
        for (int i = 0; i < 54; i++)
        {
            buttonGUIs newButton = new buttonGUIs(gui, i);
            gui.addButton(newButton, i);
        }
        buildGUISlots();
        buildGUIMenu();
    }
    public void showGUI(Player player)
    {
        createNewGUI();
        player.openInventory(gui.getMyGui());
    }
    public void buildGUIMenu()
    {
        int Paget = (startIndex + 9) / 9;
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(owner);
        //45-53
        buttonGUIs tmp =gui.getButton(45);
        tmp.setTextureTrue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19");
        tmp.setNameTrue("Last");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmFiYWQ5MjRiMjIzNzJiYzk2NmE2ZDUxN2QyZjFiOGI1N2ZkZDI2MmI0ZTA0ZjQ4MzUyZTY4M2ZmZjkyIn19fQ==");
        tmp.setNameFalse("x");
        if (startIndex == 0)
        {
            tmp.setToggle(buttonEnum.FALSE);
        }else
        {
            tmp.setToggle(buttonEnum.TRUE);
        }
        tmp =gui.getButton(52);
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM4OGQ2MTYzZmFiZTdjNWU2MjQ1MGViMzdhMDc0ZTJlMmM4ODYxMWM5OTg1MzZkYmQ4NDI5ZmFhMDgxOTQ1MyJ9fX0=");
        tmp.setNameFalse("Owner: " + oPlayer.getName());
        tmp.addLore(ChatColor.WHITE + "Ticked: " + getLastTick() + " seconds ago");
        tmp.setToggle(buttonEnum.FALSE);


        ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        blank = TitanBox.changeName(blank, "-");
        for (int i = 46; i < 52; i++)
        {
            tmp = new buttonGUIs(gui, i);
            tmp.setBlank(blank.clone());
            tmp.setToggle(buttonEnum.BLANK);
            gui.addButton(tmp, tmp.getSlot());
        }

        tmp =gui.getButton(53);
        tmp.setTextureTrue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY3NDE2Y2U5ZTgyNmU0ODk5YjI4NGJiMGFiOTQ4NDNhOGY3NTg2ZTUyYjcxZmMzMTI1ZTAyODZmOTI2YSJ9fX0=");
        tmp.setNameTrue("Next");
        tmp.setTextureFalse("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0=");
        tmp.setNameFalse("x");

        if (startIndex + 45 >= RouterHolder.bigMax)
        {
            tmp.setToggle(buttonEnum.FALSE);
        }else
        {
            tmp.setToggle(buttonEnum.TRUE);
        }

        tmp =gui.getButton(49);
        String bTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQyYzE5YjQ0MjU0MTM1MWE2YjgxZWViNmNiZWY0MTk2NmZmYjdkYmU0YzEzNmI4N2Y1YmFmOWQxNGEifX19";
        double percent = (double)RouterHolder.getCharge(this.getLocation()) / (double)RouterHolder.getCapacity(this.getLocation());
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

    }
    public void buildGUISlots()
    {

        int slot = 0;
        List<MainModule> modules = this.getModules();
        int max = Math.min(startIndex + 45, modules.size());
        for(int i = startIndex; i<max;i++)
        {
            buttonGUIs tmp = gui.getButton(slot);
            ItemStack copyMe = MainModule.getItemfromModule(modules.get(i));//MainModule.getItemfromModule(modules.get(i));
            tmp.setMaterialTrue(copyMe);
            tmp.setNameTrue(copyMe.getItemMeta().getDisplayName());
            tmp.setLore(copyMe.getItemMeta().getLore());
            tmp.setToggle(buttonEnum.TRUE);
            slot++;
        }

        ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        blank = TitanBox.changeName(blank, "Click Module To Add it Here!");
        max = Math.min(startIndex + 45, this.getMax());
        for (int i = startIndex + slot; i<max;i++)
        {
            buttonGUIs tmp = gui.getButton(slot);
            tmp.setMaterialFalse(blank.clone());
            tmp.setToggle(buttonEnum.FALSE);
            slot++;
        }
        blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        blank = TitanBox.changeName(blank, "You can add more slots here!");
        max = Math.min(startIndex + 45, RouterHolder.bigMax);
        for (int i = startIndex + slot; i<max;i++)
        {

            buttonGUIs tmp = gui.getButton(slot);
            tmp.setMaterialFalse(blank.clone());
            tmp.setToggle(buttonEnum.FALSE);
            slot++;

        }
        blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        blank = TitanBox.changeName(blank, "-");
        for (int i = slot; i < 45; i++)
        {
            buttonGUIs tmp = new buttonGUIs(gui, i);
            tmp.setBlank(blank.clone());
            tmp.setToggle(buttonEnum.BLANK);
            gui.addButton(tmp, tmp.getSlot());
        }
    }
    public String getID() {
        return ID;
    }

    public mainGUI getGui() {
        return gui;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public static void loadRRH(String key)
    {

        if (RouterHolder.routing.contains("router." + key + ".owner")) {
            List<String> tmp = RouterHolder.routing.getStringList("router." + key + ".modules");
            UUID owner = RouterHolder.routing.getUUID("router." + key + ".owner");
            int size = RouterHolder.routing.getInt("router." + key + ".size");
            Location location = RouterHolder.routing.getLocation("router." + key + ".location");
            ItemRoutingRouter tmpRouter = new ItemRoutingRouter(key);
            tmpRouter.setOwner(owner);
            tmpRouter.setMax(size);
            tmpRouter.setLocation(location);
            List<MainModule> tmpMH = tmpRouter.getModules();
            for (String tmpKey : tmp) {
                if (MainModule.getModulefromID(tmpKey) != null) {
                    tmpMH.add(MainModule.getModulefromID(tmpKey));
                }
            }
            RouterHolder.routersByID.put(key, tmpRouter);
            RouterHolder.routersByLocation.put(tmpRouter.getLocation().toString(), tmpRouter);
            RouterHolder.routersByOwner.put(tmpRouter.getOwner().toString(), tmpRouter);
        }
        else
        {
            RouterHolder.routing.setValue("router." + key, null);
            RouterHolder.routing.save();
        }
    }
    public void SaveMe()
    {
        List<String> keys = new ArrayList<String>();
        for (MainModule mm: this.getModules())
        {
            keys.add(mm.getModuleid());
        }
        RouterHolder.routing.setValue("router." + this.getID() + ".modules", keys);
        RouterHolder.routing.setValue("router." + this.getID() + ".owner", this.getOwner());
        RouterHolder.routing.setValue("router." + this.getID() + ".size", this.getMax());
        RouterHolder.routing.setValue("router." + this.getID() + ".location", this.getLocation());
    }
    @Override
    public void onInventoryClickEvent(InventoryClickEvent event, buttonGUIs button) {

        if (button != null)
        {
            if (button.getSlot() < 45) {
                if (button.getSlot() + startIndex< modules.size()) {
                    try {
                        MainModule geting = modules.get(button.getSlot() + startIndex);
                        ItemStack hereItem = MainModule.getItemfromModule(geting);
                        modules.remove(button.getSlot() + startIndex);
                        this.SaveMe();
                        event.getWhoClicked().getInventory().addItem(hereItem);
                        buildGUISlots();
                    } catch (Exception e) {
                        modules.remove(button.getSlot() + startIndex);
                        this.SaveMe();
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
                if (startIndex + 45 >= RouterHolder.bigMax) {
                    if (startIndex + 45 -9 >= RouterHolder.bigMax)
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

                    int max = Math.min(RouterHolder.bigMax, this.getMax());

                    for (int i = 0; i < modules.size(); i++) {
                        MainModule tmpMH = modules.get(i);
                        if (tmpMH.getModuleid().equals(moduleHolder.getModuleid())) {
                            modules.remove(i);
                            event.getWhoClicked().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module removed from router." + ChatColor.WHITE + "(" + modules.size() + "/" + RouterHolder.bigMax + ")");
                            this.SaveMe();
                            return;
                        }
                    }
                    if (modules.size() >= max) {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.RED + "Only " + ChatColor.WHITE + max + ChatColor.RED + " is allowed in this unit.");
                        return;
                    }


                    modules.add(moduleHolder);
                    this.SaveMe();
                    event.getWhoClicked().getInventory().setItem(event.getSlot(), null);
                    buildGUISlots();
                }
            }
        }

    }
}

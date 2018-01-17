package com.firesoftitan.play.titanbox.guis;

import com.firesoftitan.play.titanbox.interfaces.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class mainGUI {

    private Inventory myGui;
    private int size;
    private String name;
    private HashMap<Integer, buttonGUIs> holdings = new HashMap<Integer, buttonGUIs>();
    private InventoryHolder parent;
    private Player viewer;
    public mainGUI(int size, String name, InventoryHolder parent)
    {
        myGui = Bukkit.createInventory(null, size, name);
        this.name = name;
        this.size = size;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Inventory getMyGui() {
        return myGui;
    }
    public Player getViewer()
    {
        if (viewer != null) {
            if (viewer.getOpenInventory().getTopInventory().getName().equals(name)) {
                return viewer;
            }
        }
        return null;
    }

    public void setParent(InventoryHolder parent) {
        this.parent = parent;
    }

    public void addButton(buttonGUIs button, int slot)
    {
        holdings.put(slot, button);
        Inventory inv = getMyGui();
        if (button == null) {
            inv.setItem(slot, null);
        }
        else
        {
            inv.setItem(slot, button.getButton());
        }
        showGUI();
    }
    public buttonGUIs getButton(int slot)
    {
        return holdings.get(slot);
    }
    public void showGUI()
    {
        if (getViewer() != null) {
            showGUI(getViewer());
        }
    }
    public void showGUI(Player player)
    {
        viewer = player;
        player.openInventory(myGui);
    }

    public InventoryHolder getParent() {
        return parent;
    }
    public void onInventoryClickEvent(InventoryClickEvent event)
    {
        int slot = event.getRawSlot();
        buttonGUIs button = null;
        if (slot > -1 && slot < 54) {
            button = holdings.get(slot);
        }
        parent.onInventoryClickEvent(event, button);
    }
}

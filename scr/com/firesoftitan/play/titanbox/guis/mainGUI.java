package com.firesoftitan.play.titanbox.guis;

import com.firesoftitan.play.titanbox.interfaces.InventoryInterface;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class MainGUI {

    private Inventory myGui;
    private int size;
    private String name;
    private HashMap<Integer, ButtonGUIs> holdings = new HashMap<Integer, ButtonGUIs>();
    private InventoryInterface parent;
    private Player viewer;
    public MainGUI(int size, String name, InventoryInterface parent)
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
            if (viewer.getOpenInventory().getTitle().equals(name)) {
                return viewer;
            }
        }
        return null;
    }

    public void setParent(InventoryInterface parent) {
        this.parent = parent;
    }

    public void addButton(ButtonGUIs button, int slot)
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
    public ButtonGUIs getButton(int slot)
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

    public InventoryInterface getParent() {
        return parent;
    }
    public void onInventoryClickEvent(InventoryClickEvent event)
    {
        int slot = event.getRawSlot();
        ButtonGUIs button = null;
        if (slot > -1 && slot < 54) {
            button = holdings.get(slot);
        }
        parent.onInventoryClickEvent(event, button);
    }
}

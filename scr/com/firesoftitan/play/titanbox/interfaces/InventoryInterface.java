package com.firesoftitan.play.titanbox.interfaces;

import com.firesoftitan.play.titanbox.guis.ButtonGUIs;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryInterface {
    public abstract void  onInventoryClickEvent(InventoryClickEvent event, ButtonGUIs button);

}

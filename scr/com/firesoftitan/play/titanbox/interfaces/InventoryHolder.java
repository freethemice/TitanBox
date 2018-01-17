package com.firesoftitan.play.titanbox.interfaces;

import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryHolder {
    public abstract void  onInventoryClickEvent(InventoryClickEvent event, buttonGUIs button);

}

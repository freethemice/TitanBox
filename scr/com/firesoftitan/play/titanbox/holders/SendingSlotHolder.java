package com.firesoftitan.play.titanbox.holders;

import org.bukkit.inventory.ItemStack;

public class SendingSlotHolder {
    private int slot = -1;
    private ItemStack item = null;
    private long lastchecked = 0;
    public SendingSlotHolder(int slot, ItemStack itemStack)
    {
        this.slot = slot;
        this.item = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public long getLastChecked() {
        return lastchecked;
    }

    public void check()
    {
        lastchecked = System.currentTimeMillis();
    }
}

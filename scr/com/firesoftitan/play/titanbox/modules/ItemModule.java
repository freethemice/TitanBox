package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.machines.Pumps;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemModule extends MainModule {


    private Location itemPump = null;
    private Long lastran = Long.valueOf(0);
    public ItemModule()
    {
        type = ModuleTypeEnum.Item;
    }
    @Override
    public String getLinkLore()
    {
        if (getModuleid() == null)
        {
            return ChatColor.WHITE  + "not set.";
        }
        else
        {
            Location from = itemPump;
            return ChatColor.WHITE + from.getWorld().getName() + ": " + from.getBlockX() + ": " + from.getBlockY() + ": " + from.getBlockZ();
        }
    }
    @Override
    public void OpenGui(Player player)
    {

    }
    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.HOPPER, 1);
    }
    @Override
    public boolean setLink(Location link, Player player) {
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Item"))
            {
                this.itemPump = link.clone();
                saveInfo();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Item Sucker linked!");
                }
                return true;
            }
        }
        saveInfo();
        return false;
    }
    @Override
    public void loadInfo() {
        super.loadInfo();
        itemPump = null;
        if (modules.contains("modules." + moduleid + ".slots.itempump")) {
            itemPump = modules.getLocation("modules." + moduleid + ".slots.itempump");
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        modules.setValue("modules." + moduleid + ".slots.itempump", itemPump);
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }

    @Override
    public boolean isLoaded()
    {
        if (itemPump != null) {
            return itemPump.getChunk().isLoaded();
        }
        return false;
    }

    @Override
    public void runMe(UUID owner)
    {
        if (lastran + 1000  < System.currentTimeMillis()) {
            lastran = System.currentTimeMillis();
            if (itemPump != null) {
                if (Pumps.getLiquid(itemPump, "Item")) {
                    TitanBox.pickupItem(owner, itemPump.clone(), null, 10);
                }
            }
        }
    }
}
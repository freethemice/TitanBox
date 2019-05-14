package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.machines.Pumps;
import com.firesoftitan.play.titansql.ResultData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
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
        if (getModuleid() == null || itemPump == null)
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
        if (isLoaded()) {
            return new ItemStack(Material.HOPPER, 1);
        }
        return new ItemStack(Material.PAPER, 1);
    }
    @Override
    public void unLinkAll()
    {
        this.link = null;
        this.itemPump = null;
        needSaving();
    }
    @Override
    public boolean setLink(Location link, Player player) {
        super.setLink(link, player);
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Item"))
            {
                this.itemPump = link.clone();
                needSaving();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Item Sucker linked!");
                }
                return true;
            }
        }
        needSaving();
        return false;
    }
    @Override
    public void loadInfo(HashMap<String, ResultData> result)
    {
        super.loadInfo(result);
        itemPump = null;
        if (result.get("pump_a") != null) {
            if (result.get("pump_a").getLocation() != null) {
                itemPump = result.get("pump_a").getLocation().clone();
            }
        }
    }


    @Override
    public void saveInfo() {
        super.saveInfo();
        modulesSQL.setDataField("pump_a", itemPump);
        this.sendDate();
        //modules.setValue("modules." + moduleid + ".slots.itempump", itemPump);
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
            if (itemPump.getChunk() != null) {
                return Utilities.isLoaded(itemPump);
            }
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
                    Utilities.pickupItemToStorage(owner, itemPump.clone(), null, 10);
                }
            }
        }
    }
}

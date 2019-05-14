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

public class IceModule extends MainModule {


    private Location icePump = null;
    private Long lastran = Long.valueOf(0);
    public IceModule()
    {
        type = ModuleTypeEnum.Ice;
    }
    @Override
    public String getLinkLore()
    {
        if (getModuleid() == null || icePump == null)
        {
            return ChatColor.WHITE  + "not set.";
        }
        else
        {
            Location from = icePump;
            return ChatColor.WHITE + from.getWorld().getName() + ": " + from.getBlockX() + ": " + from.getBlockY() + ": " + from.getBlockZ();
        }
    }
    @Override
    public void OpenGui(Player player)
    {

    }
    @Override
    public void unLinkAll() {
        this.link = null;
        this.icePump = null;
        needSaving();
    }
    @Override
    public boolean setLink(Location link, Player player) {
        super.setLink(link, player);
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Ice"))
            {
                this.icePump = link.clone();
                needSaving();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Ice Extractor linked!");
                }
                return true;
            }
        }
        needSaving();
        return false;
    }
    @Override
    public ItemStack getMeAsIcon()
    {
        if (isLoaded()) {
            if (Pumps.getLiquid(icePump, "Ice"))
            {
                return new ItemStack(Material.ICE, 1);
            }
        }
        return new ItemStack(Material.PAPER, 1);

    }
    @Override
    public boolean isLoaded()
    {
        if (icePump != null) {
            return Utilities.isLoaded(icePump);
        }
        return false;
    }
    @Override
    public void loadInfo(HashMap<String, ResultData> result)
    {
        super.loadInfo(result);
        icePump = null;
        if (result.get("pump_a") != null) {
            if (result.get("pump_a").getLocation() != null) {
                icePump = result.get("pump_a").getLocation().clone();
            }
        }
    }


    @Override
    public void saveInfo() {
        super.saveInfo();
        modulesSQL.setDataField("pump_a", icePump);
        this.sendDate();
        //modules.setValue("modules." + moduleid + ".slots.icepump", icePump);
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }

    @Override
    public void runMe(UUID owner)
    {
        if (lastran + 1000  < System.currentTimeMillis()) {
            lastran = System.currentTimeMillis();
            if (icePump != null) {

                if (Pumps.getLiquid(icePump, "Ice")) {
                    Utilities.addItemToStorage(owner, Material.ICE, 3);
                }
            }
        }
    }
}

package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.machines.Pumps;
import com.firesoftitan.play.titansql.ResultData;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class FisherModule extends MainModule {


    private Location waterPump = null;
    private Long lastran = Long.valueOf(0);
    private int controle = 1;
    private Random rnd = new Random(System.currentTimeMillis());
    public FisherModule()
    {
        type = ModuleTypeEnum.Fishing;
    }
    @Override
    public String getLinkLore()
    {
        if (getModuleid() == null || waterPump == null)
        {
            return ChatColor.WHITE  + "not set.";
        }
        else
        {
            Location from = waterPump;
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
        this.waterPump = null;
        saveInfo();
    }
    @Override
    public boolean setLink(Location link, Player player) {
        super.setLink(link, player);
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Water"))
            {
                this.waterPump = link.clone();
                saveInfo();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Water pummp linked!");
                }
                return true;
            }
        }
        saveInfo();
        return false;
    }
    @Override
    public void loadInfo(HashMap<String, ResultData> result)
    {
        super.loadInfo(result);
        waterPump = null;
        if (result.get("pump_a") != null) {
            if (result.get("pump_a").getLocation() != null) {
                waterPump = result.get("pump_a").getLocation().clone();
            }
        }
    }


    @Override
    public void saveInfo() {
        super.saveInfo();
        modulesSQL.setDataField("pump_a", waterPump);
        this.sendDate();

        //modules.setValue("modules." + moduleid + ".slots.waterpump", waterPump);
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }

    @Override
    public ItemStack getMeAsIcon()
    {

        if (isLoaded()) {
            if (Pumps.getLiquid(link, "Water"))
            {
                return new ItemStack(Material.FISHING_ROD, 1);
            }
        }
        return new ItemStack(Material.BARRIER, 1);
    }
    @Override
    public boolean isLoaded()
    {
        if (waterPump != null) {
            return Utilities.isLoaded(waterPump);
        }
        return false;
    }
    @Override
    public void runMe(UUID owner)
    {
        if (lastran + 1000 * controle < System.currentTimeMillis()) {
            controle  =  rnd.nextInt(10) + 1;
            lastran = System.currentTimeMillis();
            if (waterPump != null) {
                if (Pumps.getLiquid(waterPump, "Water")) {
                    if (SlimefunStartup.chance(100, 1)) {
                        TitanBox.addItemToStorage(owner, Material.PUFFERFISH, 1);
                    } else {
                        if (SlimefunStartup.chance(100, 5)) {
                            TitanBox.addItemToStorage(owner, Material.TROPICAL_FISH, 1);
                        } else {
                            if (SlimefunStartup.chance(100, 15)) {
                                TitanBox.addItemToStorage(owner, Material.SALMON, 1, (short) 0);
                            } else {
                                TitanBox.addItemToStorage(owner, Material.COD, 1, (short) 0);
                            }
                        }
                    }
                }
            } else {

            }
        }
    }
}

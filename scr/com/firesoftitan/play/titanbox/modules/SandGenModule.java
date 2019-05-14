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

public class SandGenModule extends MainModule {


    private Location waterPump = null;
    private Location lavaPump = null;
    public SandGenModule()
    {
        type = ModuleTypeEnum.Sand;
    }
    @Override
    public String getLinkLore()
    {
        if (getModuleid() == null)
        {
            return ChatColor.WHITE  + "not Initialized.";
        }
        else
        {
            if (waterPump == null || lavaPump == null) {
                return ChatColor.WHITE + "Bucket Mode";
            }
            else
            {
                return ChatColor.WHITE + "Pump Mode";
            }
        }
    }
    @Override
    public void OpenGui(Player player)
    {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!Utilities.isEmpty(mainHand)) {
            MainModule mh = MainModule.getModulefromItem(mainHand);
            if (mh != null) {
                    mh.clearInfo();
                    mh.preLoadSlots();
                    if (mh.getModuleid() == null)
                    {
                        mh.setModuleid(MainModule.getNewIDString());
                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module Initialized!");
                    }
                    updateGUIClicked(player, mh, false);

            }
        }

    }
    @Override
    public void unLinkAll()
    {
        this.link = null;
        this.waterPump = null;
        this.lavaPump = null;
        needSaving();
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
                needSaving();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Water pummp linked!");
                }
                return true;
            }
            if(pump.equals("Lava"))
            {
                this.lavaPump = link.clone();
                needSaving();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Lava pummp linked!");
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
        waterPump = null;
        lavaPump = null;
        if (result.get("pump_a") != null) {
            if (result.get("pump_a").getLocation() != null) {
                waterPump = result.get("pump_a").getLocation().clone();
            }
        }
        if (result.get("pump_b") != null) {
            if (result.get("pump_b").getLocation() != null) {
                lavaPump = result.get("pump_b").getLocation().clone();
            }
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        modulesSQL.setDataField("pump_a", waterPump);
        modulesSQL.setDataField("pump_b", lavaPump);
        this.sendDate();
        //modules.setValue("modules." + moduleid + ".slots.waterpump", waterPump);
        //modules.setValue("modules." + moduleid + ".slots.lavapump", lavaPump);
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
            return new ItemStack(Material.SAND, 1);
        }
        return new ItemStack(Material.PAPER, 1);
    }
    @Override
    public boolean isLoaded()
    {
        if (waterPump != null && lavaPump !=null)
        {
            if (waterPump.getChunk() != null) {
                if (Utilities.isLoaded(waterPump) && Utilities.isLoaded(lavaPump)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void runMe(UUID owner)
    {
        if (waterPump != null && lavaPump !=null)
        {
            if (Pumps.getLiquid(waterPump, "Water") && Pumps.getLiquid(lavaPump, "Lava"))
            {
                Utilities.addItemToStorage(owner, Material.SAND, 64);
                Utilities.addItemToStorage(owner, Material.SAND, 64);
            }
        }
        else
        {
            if (Utilities.hasItemInStorage(owner, Material.WATER_BUCKET))
            {
                if (Utilities.hasItemInStorage(owner, Material.LAVA_BUCKET)) {
                    Utilities.addItemToStorage(owner, Material.SAND, 64);
                    Utilities.addItemToStorage(owner, Material.BUCKET, 1);
                }
                Utilities.addItemToStorage(owner, Material.BUCKET, 1);
            }
        }
    }


}

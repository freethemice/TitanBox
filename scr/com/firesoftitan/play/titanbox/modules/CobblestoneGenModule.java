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

public class CobblestoneGenModule extends MainModule {


    private Location waterPump = null;
    private Location lavaPump = null;
    public CobblestoneGenModule()
    {
        type = ModuleTypeEnum.Cobblestone;
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
        if (!TitanBox.isEmpty(mainHand)) {
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
    public boolean setLink(Location link, Player player) {
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Water"))
            {
                this.waterPump = link.clone();
                saveInfo();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Water pummp  linked!");
                }
                return true;
            }
            if(pump.equals("Lava"))
            {
                this.lavaPump = link.clone();
                saveInfo();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Lava pummp  linked!");
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
        waterPump = null;
        lavaPump = null;
        if (modules.contains("modules." + moduleid + ".slots.waterpump")) {
            waterPump = modules.getLocation("modules." + moduleid + ".slots.waterpump");
        }
        if (modules.contains("modules." + moduleid + ".slots.lavapump")) {
            lavaPump = modules.getLocation("modules." + moduleid + ".slots.lavapump");
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        modules.setValue("modules." + moduleid + ".slots.waterpump", waterPump);
        modules.setValue("modules." + moduleid + ".slots.lavapump", lavaPump);
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }

    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.COBBLESTONE, 1);
    }
    @Override
    public boolean isLoaded()
    {
        if (waterPump != null && lavaPump !=null)
        {
            if (waterPump.getChunk().isLoaded() && lavaPump.getChunk().isLoaded())
            {
                return  true;
            }
            else
            {
                return false;
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
                TitanBox.addItemToStorage(owner, Material.COBBLESTONE, 7);
            }
        }
        else
        {
            if (TitanBox.hasItem(owner, Material.WATER_BUCKET))
            {
                if (TitanBox.hasItem(owner, Material.LAVA_BUCKET)) {
                    TitanBox.addItemToStorage(owner, Material.COBBLESTONE, 64);
                    TitanBox.addItemToStorage(owner, Material.COBBLESTONE, 64);
                    TitanBox.addItemToStorage(owner, Material.BUCKET, 1);
                }
                TitanBox.addItemToStorage(owner, Material.BUCKET, 1);
            }
        }
    }


}

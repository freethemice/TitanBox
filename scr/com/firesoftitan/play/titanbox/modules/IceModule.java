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
        if (getModuleid() == null)
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
    public boolean setLink(Location link, Player player) {
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Ice"))
            {
                this.icePump = link.clone();
                saveInfo();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Ice Extractor linked!");
                }
                return true;
            }
        }
        saveInfo();
        return false;
    }
    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.ICE, 1);
    }
    @Override
    public boolean isLoaded()
    {
        if (icePump != null) {
            return icePump.getChunk().isLoaded();
        }
        return false;
    }
    @Override
    public void loadInfo() {
        super.loadInfo();
        icePump = null;
        if (modules.contains("modules." + moduleid + ".slots.icepump")) {
            icePump = modules.getLocation("modules." + moduleid + ".slots.icepump");
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        modules.setValue("modules." + moduleid + ".slots.icepump", icePump);
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
                    TitanBox.addItemToStorage(owner, Material.ICE, 3, (short) 0);
                }
            }
        }
    }
}

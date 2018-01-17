package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.machines.Pumps;
import com.firesoftitan.play.titanbox.machines.StorageUnit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BucketsModule extends MainModule {


    public BucketsModule()
    {
        type = ModuleTypeEnum.Bucket;
    }

    @Override
    public boolean setLink(Location link, Player player) {
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Water"))
            {
                this.link = link.clone();
                saveInfo();
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Water pummp  linked!");
                }
                return true;
            }
            if(pump.equals("Lava"))
            {
                this.link = link.clone();
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
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.BUCKET, 1);
    }
    @Override
    public void loadInfo() {
        super.loadInfo();
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }

    @Override
    public void runMe(UUID owner)
    {
        if (link != null)
        {
            if (Pumps.getLiquid(link, "Water") || Pumps.getLiquid(link, "Lava"))
            {
                String Type = Pumps.getPumpType(link);
                Material typeBucket = Material.WATER_BUCKET;
                if (Type.equals("Lava"))
                {
                    typeBucket = Material.LAVA_BUCKET;
                }
                boolean foundbucket = false;
                for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
                    if (stH.getOwner().toString().equals(owner.toString())) {
                        for(int i =0;i <stH.getSize(); i++)
                        {
                            ItemStack view = stH.viewSlot(i);
                            if (!TitanBox.isEmpty(view))
                            {
                                if (view.getType() == Material.BUCKET)
                                {
                                    ItemStack getIt = stH.getItem(i, 1);
                                    if (!TitanBox.isEmpty(getIt))
                                    {
                                        foundbucket = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (foundbucket) {
                    for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
                        if (stH.getOwner().toString().equals(owner.toString())) {
                            int giveamount = 1;
                            ItemStack out = stH.insertItem(new ItemStack(typeBucket, giveamount));
                            if (TitanBox.isEmpty(out)) {
                                return;
                            }
                            if (out.getAmount() < giveamount) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}

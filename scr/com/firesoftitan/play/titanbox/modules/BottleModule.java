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
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.UUID;

public class BottleModule extends MainModule {


    public BottleModule()
    {
        type = ModuleTypeEnum.Bottle;
    }

    @Override
    public Location getLink() {
        return link;
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
                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Water pummp linked!");
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
            if (Pumps.getLiquid(link, "Water"))
            {
                boolean foundbucket = false;
                for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
                    if (stH.getOwner().toString().equals(owner.toString())) {
                        for(int i =0;i <stH.getSize(); i++)
                        {
                            ItemStack view = stH.viewSlot(i);
                            if (!TitanBox.isEmpty(view))
                            {
                                if (view.getType() == Material.GLASS_BOTTLE)
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
                            Potion tmp = new Potion(PotionType.WATER);
                            ItemStack out = stH.insertItem(tmp.toItemStack(1));
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

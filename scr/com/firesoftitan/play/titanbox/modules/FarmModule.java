package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class FarmModule extends MainModule {


    private Location[] crops = new Location[20];
    private Long lastran = Long.valueOf(0);
    private int treeIndex = -1;
    private Long ereasme = Long.valueOf(-1);
    public FarmModule()
    {
        type = ModuleTypeEnum.Farm;
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
            return ChatColor.WHITE + "Crops: " + getCrops();
        }
    }
    private int getCrops()
    {
        int count = 0;
        for(Location tre: crops)
        {
            if (tre != null)
            {
                count++;
            }
        }
        return count;
    }
    @Override
    public void OpenGui(Player player)
    {
        if (ereasme + 1000 > System.currentTimeMillis())
        {
            ereasme = Long.valueOf(-1);
            for(int i = 0; i < crops.length; i++)
            {
                crops[i] = null;
            }
            saveInfo();
            updateGUIClicked(player, this, false);
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "All crops removed!");
            return;
        }
        if (ereasme  > 0)
        {
            ereasme = Long.valueOf(-1);
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Removal Canceled!");
            return;
        }
        if (ereasme  < 0)
        {
            ereasme = System.currentTimeMillis();
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Right click again, with in 1 second, to remove all crops!");
            return;
        }
    }
    @Override
    public boolean setLink(Location link, Player player) {
        this.link = null;
        ereasme = Long.valueOf(-1);
        if (link.getBlock().getType() == Material.POTATO || link.getBlock().getType() == Material.CARROT || link.getBlock().getType() == Material.CROPS )
        {
            for(int i = 0; i < crops.length; i++)
            {
                if (crops[i] != null) {
                    if (crops[i].toString().equalsIgnoreCase(link.toString())) {
                        crops[i] = null;
                        saveInfo();
                        if (player != null) {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Crop removed!");
                        }
                        return true;
                    }
                }
            }

            for(int i = 0; i < crops.length; i++)
            {
                if (crops[i] == null)
                {
                    crops[i] = link.clone();

                    saveInfo();
                    if (player != null) {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Crop added!");
                    }
                    return true;
                }
            }

    }
    saveInfo();
        if (player != null) {
        if (getCrops() >= crops.length)
        {
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module Full!");
        }
        else
        {
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Not a crop!");
        }
    }
        return false;
    }
    @Override
    public void loadInfo() {
        super.loadInfo();
        if (modules.contains("modules." + moduleid + ".slots.crops")) {
            for(int i = 0; i < crops.length; i++) {
                if (modules.contains("modules." + moduleid + ".slots.crops." + i)) {
                    crops[i] = modules.getLocation("modules." + moduleid + ".slots.crops." + i);
                }
                else
                {
                    crops[i] = null;
                }
            }
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        for(int i = 0; i < crops.length; i++) {
            modules.setValue("modules." + moduleid + ".slots.crops." + i, crops[i]);
        }
    }

    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.WHEAT, 1);
    }
    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }
    private void getNextWart()
    {
        getNextWart(0);
    }
    private void getNextWart(int times)
    {
        if (times > 10)
        {
            treeIndex = 0;
            return;
        }
        treeIndex++;
        if (treeIndex >= crops.length)
        {
            treeIndex = 0;
        }
        if (crops[treeIndex]==null)
        {
            times++;
            getNextWart(times);
        }

    }
    @Override
    public boolean isLoaded()
    {
        return true;
    }

    @Override
    public void runMe(UUID owner)
    {

        if (lastran + 1000  < System.currentTimeMillis()) {
            lastran = System.currentTimeMillis();
            getNextWart();
            if (crops[treeIndex] != null) {
                if (crops[treeIndex].getChunk().isLoaded()) {
                    crops[treeIndex].getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crops[treeIndex].clone().add(0.5f, 1, 0.5f), 3);
                    if (crops[treeIndex].getBlock().getType() == Material.POTATO || crops[treeIndex].getBlock().getType() == Material.CARROT || crops[treeIndex].getBlock().getType() == Material.CROPS) {
                        if (crops[treeIndex].getBlock().getData() >= 7) {
                            crops[treeIndex].getBlock().setData((byte) 0);
                            crops[treeIndex].getWorld().playEffect(crops[treeIndex], Effect.STEP_SOUND, crops[treeIndex].getBlock().getType());
                            if (crops[treeIndex].getBlock().getType() == Material.CROPS) {
                                TitanBox.addItemToStorage(owner, Material.WHEAT, 1);
                                TitanBox.addItemToStorage(owner, Material.SEEDS, 1);
                            }
                            if (crops[treeIndex].getBlock().getType() == Material.POTATO) {
                                TitanBox.addItemToStorage(owner, Material.POTATO_ITEM, 1);
                            }
                            if (crops[treeIndex].getBlock().getType() == Material.CARROT) {
                                TitanBox.addItemToStorage(owner, Material.CARROT_ITEM, 1);
                            }

                        } else {
                            if (TitanBox.hasItem(owner, Material.INK_SACK, (short) 15)) {
                                crops[treeIndex].getBlock().setData((byte) 7);
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.TitanSQL;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public void unLinkAll() {
        this.link = null;
        for(int i = 0; i < crops.length; i++)
        {
            crops[i] = null;
        }
        saveInfo();
    }

    @Override
    public boolean setLink(Location link, Player player) {
        super.setLink(link, player);
        this.link = null;
        ereasme = Long.valueOf(-1);
        if (link.getBlock().getType() == Material.POTATO || link.getBlock().getType() == Material.CARROT || link.getBlock().getType() == Material.WHEAT )
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
    public void loadInfo(HashMap<String, ResultData> result)
    {
        super.loadInfo(result);
        if (result.get("locations") != null) {
            List<String> tmpWarts = result.get("locations").getStringList();
            if (tmpWarts != null) {
                for (int i = 0; i < crops.length; i++) {
                    if (tmpWarts.size() > i) {
                        String loc = tmpWarts.get(i);
                        Location place = TitanSQL.decodeLocation(loc);
                        crops[i] = place.clone();
                    }
                }
            }
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        List<String> tmpWarts = new ArrayList<String>();
        for(int i = 0; i < crops.length;i++) {
            if (crops[i] != null)
            {
                String saver = TitanSQL.encode(crops[i]);
                tmpWarts.add(saver);
            }
        }
        modulesSQL.setDataField("locations", tmpWarts);
        this.sendDate();
        /*
        for(int i = 0; i < crops.length; i++) {
            modules.setValue("modules." + moduleid + ".slots.crops." + i, crops[i]);
        }*/
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
                if (crops[treeIndex].getChunk() != null) {
                    if (Utilities.isLoaded(crops[treeIndex])) {
                        Block block = crops[treeIndex].getBlock();
                        crops[treeIndex].getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crops[treeIndex].clone().add(0.5f, 1, 0.5f), 3);
                        if (block.getType() == Material.POTATO || block.getType() == Material.CARROT || block.getType() == Material.WHEAT) {
                            org.bukkit.material.Crops cropsState = (Crops) block.getState().getData();

                            Ageable ageable = (Ageable)block.getBlockData();
                            if (ageable.getAge() == ageable.getMaximumAge()) {
                                ageable.setAge(0);
                                block.setBlockData(ageable);
                                crops[treeIndex].getWorld().playEffect(crops[treeIndex], Effect.STEP_SOUND, crops[treeIndex].getBlock().getType());
                                TitanBox.addItemToStorage(owner, Material.NETHER_WART, 2);
                                if (block.getType() == Material.WHEAT_SEEDS) {
                                    TitanBox.addItemToStorage(owner, Material.WHEAT, 1);
                                    TitanBox.addItemToStorage(owner, Material.WHEAT_SEEDS, 1);
                                }
                                if (block.getType() == Material.POTATO) {
                                    TitanBox.addItemToStorage(owner, Material.POTATOES, 1);
                                }
                                if (block.getType() == Material.CARROT) {
                                    TitanBox.addItemToStorage(owner, Material.CARROTS, 1);
                                }

                            } else {
                                if (TitanBox.hasItem(owner, Material.BONE_MEAL)) {
                                    ageable.setAge(ageable.getMaximumAge());
                                    block.setBlockData(ageable);

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

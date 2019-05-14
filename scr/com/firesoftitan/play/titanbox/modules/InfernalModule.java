package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.TitanSQL;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InfernalModule extends MainModule {


    private Location[] warts = new Location[20];
    private Long lastran = Long.valueOf(0);
    private int treeIndex = -1;
    private Long ereasme = Long.valueOf(-1);
    public InfernalModule()
    {
        type = ModuleTypeEnum.Infernal;
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
            return ChatColor.WHITE + "Warts: " + getWarts();
        }
    }
    private int getWarts()
    {
        int count = 0;
        for(Location tre: warts)
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
            for(int i = 0; i < warts.length; i++)
            {
                warts[i] = null;
            }
            needSaving();
            updateGUIClicked(player, this, false);
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "All warts removed!");
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
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Right click again, with in 1 second, to remove all warts!");
            return;
        }
    }
    @Override
    public void unLinkAll()
    {
        this.link = null;
        for(int i = 0; i < warts.length;i++)
        {
            warts[i] = null;
        }
        needSaving();
    }
    @Override
    public boolean setLink(Location link, Player player) {
        super.setLink(link, player);
        this.link = null;
        ereasme = Long.valueOf(-1);
        if (link.getBlock().getType() == Material.NETHER_WART)
        {
            for(int i = 0; i < warts.length;i++)
            {
                if (warts[i] != null) {
                    if (warts[i].toString().equalsIgnoreCase(link.toString())) {
                        warts[i] = null;
                        needSaving();
                        if (player != null) {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Wart removed!");
                        }
                        return true;
                    }
                }
            }

            for(int i = 0; i < warts.length;i++)
            {
                if (warts[i] == null)
                {
                    warts[i] = link.clone();

                    needSaving();
                    if (player != null) {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Wart added!");
                    }
                    return true;
                }
            }

    }
    needSaving();
        if (player != null) {
        if (getWarts() >= warts.length)
        {
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module Full!");
        }
        else
        {
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Not a wart!");
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
                for (int i = 0; i < warts.length; i++) {
                    if (tmpWarts.size() > i) {
                        String loc = tmpWarts.get(i);
                        Location place = TitanSQL.decodeLocation(loc);
                        warts[i] = place.clone();
                    }
                }
            }
        }
    }


    @Override
    public void saveInfo() {
        super.saveInfo();

        List<String> tmpWarts = new ArrayList<String>();
        for(int i = 0; i < warts.length;i++) {
            if (warts[i] != null)
            {
                String saver = TitanSQL.encode(warts[i]);
                tmpWarts.add(saver);
            }
        }
        modulesSQL.setDataField("locations", tmpWarts);
        this.sendDate();

        /*for(int i = 0; i < warts.length;i++) {
            modules.setValue("modules." + moduleid + ".slots.warts." + i, warts[i]);
        }
        */
    }

    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.NETHER_WART, 1);
    }
    @Override
    public boolean isLoaded()
    {
        return true;
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
        if (treeIndex >= warts.length)
        {
            treeIndex = 0;
        }
        if (warts[treeIndex]==null)
        {
            times++;
            getNextWart(times);
        }

    }
    @Override
    public void runMe(UUID owner)
    {

        if (lastran + 1000  < System.currentTimeMillis()) {
            lastran = System.currentTimeMillis();
            getNextWart();
            if (warts[treeIndex] != null) {
                if (Utilities.isLoaded(warts[treeIndex])) {
                    warts[treeIndex].getWorld().spawnParticle(Particle.VILLAGER_HAPPY, warts[treeIndex].clone().add(0.5f, 1, 0.5f), 3);
                    if (warts[treeIndex].getBlock().getType() == Material.NETHER_WART) {
                        Block block = warts[treeIndex].getBlock();
                        if (block != null && block.getType().equals(Material.NETHER_WART)) {
                            Ageable ageable = (Ageable)block.getBlockData();
                            if (ageable.getAge() == ageable.getMaximumAge()) {
                                ageable.setAge(0);
                                block.setBlockData(ageable);
                                warts[treeIndex].getWorld().playEffect(warts[treeIndex], Effect.STEP_SOUND, warts[treeIndex].getBlock().getType());
                                Utilities.addItemToStorage(owner, Material.NETHER_WART, 2);
                            }
                        }
                    }
                }
            }
        }
    }
}

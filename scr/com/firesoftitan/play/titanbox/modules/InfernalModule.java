package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            saveInfo();
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
    public boolean setLink(Location link, Player player) {
        this.link = null;
        ereasme = Long.valueOf(-1);
        if (link.getBlock().getType() == Material.NETHER_WARTS)
        {
            for(int i = 0; i < warts.length;i++)
            {
                if (warts[i] != null) {
                    if (warts[i].toString().equalsIgnoreCase(link.toString())) {
                        warts[i] = null;
                        saveInfo();
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

                    saveInfo();
                    if (player != null) {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Wart added!");
                    }
                    return true;
                }
            }

    }
    saveInfo();
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
    public void loadInfo() {
        super.loadInfo();
        if (modules.contains("modules." + moduleid + ".slots.warts")) {
            for(int i = 0; i < warts.length;i++) {
                if (modules.contains("modules." + moduleid + ".slots.warts." + i)) {
                    warts[i] = modules.getLocation("modules." + moduleid + ".slots.warts." + i);
                }
                else
                {
                    warts[i] = null;
                }
            }
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        for(int i = 0; i < warts.length;i++) {
            modules.setValue("modules." + moduleid + ".slots.warts." + i, warts[i]);
        }
    }

    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.NETHER_STALK, 1);
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
                if (warts[treeIndex].getChunk().isLoaded()) {
                    warts[treeIndex].getWorld().spawnParticle(Particle.VILLAGER_HAPPY, warts[treeIndex].clone().add(0.5f, 1, 0.5f), 3);
                    if (warts[treeIndex].getBlock().getType() == Material.NETHER_WARTS) {
                        if (warts[treeIndex].getBlock().getData() >= 3) {
                            warts[treeIndex].getBlock().setData((byte) 0);
                            warts[treeIndex].getWorld().playEffect(warts[treeIndex], Effect.STEP_SOUND, warts[treeIndex].getBlock().getType());
                            TitanBox.addItemToStorage(owner, Material.NETHER_STALK, 2);
                        }
                    }
                }
            }
        }
    }
}

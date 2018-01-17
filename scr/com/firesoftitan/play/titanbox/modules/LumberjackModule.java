package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.enums.TreeTypeEnum;
import com.firesoftitan.play.titanbox.holders.TreeHolder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LumberjackModule extends MainModule {


    private TreeHolder[] trees = new TreeHolder[10];
    private Long lastran = Long.valueOf(0);
    private int treeIndex = -1;
    private Long ereasme = Long.valueOf(-1);

    public LumberjackModule()
    {
        type = ModuleTypeEnum.Lumberjack;
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
            return ChatColor.WHITE +  "" +getTrees() + " Tree(s) Linked";
        }
    }
    @Override
    public boolean isLoaded()
    {
        return true;
    }
    private int getTrees()
    {
        int count = 0;
        for(TreeHolder tre: trees)
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
            for(int i = 0; i < trees.length;i++)
            {
                trees[i] = null;
            }
            saveInfo();
            updateGUIClicked(player, this, false);
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "All trees removed!");
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
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Right click again, with in 1 second, to remove all trees!");
            return;
        }
    }
    @Override
    public boolean setLink(Location link, Player player) {
        this.link = null;
        ereasme = Long.valueOf(-1);
        if (TreeHolder.isTree(link))
        {
            for(int i = 0; i < trees.length;i++)
            {
                if (trees[i] != null) {
                    if (trees[i].isThisTree(link)) {
                        trees[i] = null;
                        saveInfo();
                        if (player != null) {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Tree removed!");
                        }
                        return true;
                    }
                }
            }

            for(int i = 0; i < trees.length;i++)
            {
                if (trees[i] == null)
                {
                    trees[i] = new TreeHolder(link.clone());

                    saveInfo();
                    if (player != null) {
                        if (trees[i].isBig())
                        {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Large tree added!");
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Tree added!");
                        }
                        //player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GRAY + "Remember for 2x2 trees, like jungle, you will have to add 4 trees, one on each corner.");
                    }
                    return true;
                }
            }

        }
        saveInfo();
        if (player != null) {
            if (getTrees() >= trees.length)
            {
                player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Module Full!");
            }
            else
            {
                player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Not a Tree!");
            }
        }
        return false;
    }


    @Override
    public void loadInfo() {
        super.loadInfo();

        for(int i = 0; i < trees.length;i++) {
            if (modules.contains("modules." + moduleid + ".slots.tree." + i) && modules.contains("modules." + moduleid + ".slots.treetype." + i)) {
                TreeHolder tmpTH = new TreeHolder();
                for (int j= 0; j < 4; j++) {
                    if (modules.contains("modules." + moduleid + ".slots.tree." + i + "." + j)) {
                        tmpTH.setLoc(j, modules.getLocation("modules." + moduleid + ".slots.tree." + i + "." + j));
                    }
                }
                ItemStack tmp = modules.getItem("modules." + moduleid + ".slots.treetype." + i);
                tmpTH.setType(TreeTypeEnum.getFromMaterial(tmp.getType(), tmp.getDurability()));
                trees[i] = tmpTH;

            }
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        for(int i = 0; i < trees.length;i++) {
            int size = 1;
            if (trees[i] != null) {
                if (trees[i].getType() != null) {
                    if (trees[i].isBig()) {
                        size = 4;
                    }
                    for (int j = 0; j < size; j++) {
                        modules.setValue("modules." + moduleid + ".slots.tree." + i + "." + j, trees[i].getLoc(j));
                    }
                    modules.setValue("modules." + moduleid + ".slots.treetype." + i, trees[i].getType().getItemStack());
                }
                else
                {
                    modules.setValue("modules." + moduleid + ".slots.tree." + i, null);
                    modules.setValue("modules." + moduleid + ".slots.treetype." + i, null);
                }
            }
            else
            {
                modules.setValue("modules." + moduleid + ".slots.tree." + i, null);
                modules.setValue("modules." + moduleid + ".slots.treetype." + i, null);
            }
        }
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }
    private void getNextTree()
    {
        getNextTree(0);
    }
    private void getNextTree(int times)
    {
        if (times > 10)
        {
            treeIndex = 0;
            return;
        }
        treeIndex++;
        if (treeIndex >= trees.length)
        {
            treeIndex = 0;
        }
        if (trees[treeIndex]==null)
        {
            times++;
            getNextTree(times);
        }

    }
    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.SAPLING, 1);
    }
    @Override
    public void runMe(UUID owner)
    {
        if (lastran + 1000  < System.currentTimeMillis()) {
            lastran = System.currentTimeMillis();

            getNextTree();

            if (trees[treeIndex] != null) {
                if (trees[treeIndex].getLocMain().getChunk().isLoaded()) {
                    if (trees[treeIndex].growMe(owner)) return;
                    if (trees[treeIndex].plantSapling(owner)) return;
                    trees[treeIndex].chopDownMe(owner);
                }
            }

        }
    }




}

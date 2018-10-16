package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.enums.TreeTypeEnum;
import com.firesoftitan.play.titanbox.holders.TreeHolder;
import com.firesoftitan.play.titansql.ResultData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public void unLinkAll()
    {
        this.link = null;
        for(int i = 0; i < trees.length;i++)
        {
            trees[i] = null;
        }
        saveInfo();
    }
    @Override
    public boolean setLink(Location link, Player player) {
        super.setLink(link, player);
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
    public void loadInfo(HashMap<String, ResultData> result)
    {
        super.loadInfo(result);
        if (result.get("locations") != null && result.get("items") != null)
        {
            List<ItemStack> tmpTrees = result.get("items").getItemList();
            List<String>  tmpLocation = result.get("locations").getStringList();
            if (tmpTrees != null) {
                for (int i = 0; i < tmpTrees.size(); i++) {
                    TreeHolder tmpTH = new TreeHolder();
                    String[] tmpLoc = tmpLocation.get(i).split(",");
                    for (int j = 0; j < tmpLoc.length; j++) {
                        if (tmpLoc[j].length() > 3) {
                            String tmpL = tmpLoc[j];
                            String[] info = tmpL.split("~");
                            if (info.length > 3) {
                                Location tree = new Location(Bukkit.getWorld(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]));
                                tmpTH.setLoc(j, tree.clone());
                            }
                        }

                    }

                    ItemStack tmp = tmpTrees.get(i);
                    tmpTH.setType(TreeTypeEnum.getFromMaterial(tmp.getType(), tmp.getDurability()));
                    trees[i] = tmpTH;
                }
            }
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();

        List<String> saveLocations = new ArrayList<String>();
        List<ItemStack> saveTress = new ArrayList<ItemStack>();

        for(int i = 0; i < trees.length;i++) {
            int size = 1;
            if (trees[i] != null) {
                if (trees[i].getType() != null) {
                    if (trees[i].isBig()) {
                        size = 4;
                    }
                    String adding = "";
                    for (int j = 0; j < size; j++) {
                        Location tree = trees[i].getLoc(j).clone();
                        if (tree != null && tree.getWorld() != null) {
                            adding = adding + tree.getWorld().getName() + "~" + tree.getBlockX() + "~" + tree.getBlockY() + "~" + tree.getBlockZ() + ",";
                        }
                    }
                    saveLocations.add(adding);
                    saveTress.add(trees[i].getType().getItemStack());
                }
            }
        }
        modulesSQL.setDataField("locations", saveLocations);
        modulesSQL.setDataField("items", saveTress);
        this.sendDate();
/*
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
        }*/
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
        return new ItemStack(Material.OAK_SAPLING, 1);
    }
    @Override
    public void runMe(UUID owner)
    {
        if (lastran + 1000  < System.currentTimeMillis()) {
            lastran = System.currentTimeMillis();

            getNextTree();

            if (trees[treeIndex] != null) {
                if (trees[treeIndex].getLocMain() != null) {
                    if (trees[treeIndex].getLocMain().getChunk() != null) {
                        if (Utilities.isLoaded(trees[treeIndex].getLocMain())) {
                            if (trees[treeIndex].growMe(owner)) return;
                            if (trees[treeIndex].plantSapling(owner)) return;
                            trees[treeIndex].chopDownMe(owner);
                        }
                    }
                }
            }

        }
    }




}

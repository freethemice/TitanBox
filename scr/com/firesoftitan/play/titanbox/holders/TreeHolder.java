package com.firesoftitan.play.titanbox.holders;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.TreeTypeEnum;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TreeHolder {
    private TreeTypeEnum type;
    private Location[] loc = new Location[4];
    private boolean isbig= false;
    public TreeHolder()
    {

    }
    public boolean isThisTree(Location locA)
    {
        Location link = TreeHolder.getDirtBlock(locA.clone());
        for (int i = 0; i < loc.length; i++) {
            if (loc[i] != null) {
                if (loc[i].getWorld().getName().equals(link.getWorld().getName()) && loc[i].getBlockX() == link.getBlockX() && loc[i].getBlockY() == link.getBlockY() && loc[i].getBlockZ() == link.getBlockZ()) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean growMe(UUID owner)
    {

        Location checkSides = getLocMain().clone();
        if (checkSides != null) {
            checkSides = checkSides.add(0 ,1 ,0);
            TitanBox.pickupItem(owner, checkSides.clone(), Material.SAPLING, 10);

            if (checkSides.getBlock().getType() == Material.SAPLING) {
                if (TitanBox.hasItem(owner, Material.INK_SACK, (short) 15)) {
                    int size = 1;
                    if (isbig)
                    {
                        size = loc.length;
                    }
                    for (int i = 0; i < size; i++) {
                        getLoc(i).clone().add(0,1,0).getBlock().setType(Material.AIR);
                    }

                    boolean didIgrow = checkSides.getBlock().getLocation().getWorld().generateTree(checkSides.getBlock().getLocation(), getTreeType());
                    if (!didIgrow) {
                        for (int i = 0; i < size; i++) {
                            Block blockd = getLoc(i).clone().add(0,1,0).getBlock();
                            blockd.setType(Material.SAPLING);
                            blockd.setData((byte) type.getSpalingdata());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean plantSapling(UUID owner)
    {
        boolean planted = false;
        int size = 1;
        if (isbig)
        {
            size = loc.length;
        }
        for (int i = 0; i < size; i++) {
            Location checkSides = loc[i].clone();
            if (checkSides != null) {
                checkSides = checkSides.add(0 ,1 ,0);
                if (checkSides.getBlock().getType() == Material.AIR) {
                    if (TitanBox.hasItem(owner, Material.SAPLING, type.getSpalingdata())) {
                        checkSides.getWorld().playSound(checkSides, Sound.BLOCK_GRASS_PLACE, 1, 0.5f);
                        Block blockd = checkSides.getBlock();
                        blockd.setType(Material.SAPLING);
                        blockd.setData((byte) type.getSpalingdata());
                        planted = true;
                    }
                }
            }
        }
        return planted;
    }
    public void chopDownMe(UUID owner)
    {
        int size = 1;
        if (isbig)
        {
            size = loc.length;
        }
        if (type == null)
        {
            type = TreeTypeEnum.OAK;
        }
        for (int i = 0; i < size; i++) {
            Location checkSides = loc[i].clone();
            if (checkSides != null) {
                checkSides = checkSides.add(0 ,1 ,0);
                checkSides.getWorld().playSound(checkSides, Sound.BLOCK_WOOD_BREAK, 1, 0.5f);
                ItemStack sap = new ItemStack(Material.SAPLING, 3, type.getSpalingdata());
                if (sap != null) {
                    TitanBox.addItemToStorage(owner, sap.clone());
                }
                for (int l = 0; l < 50; l++) {
                    Location checkUp = checkSides.clone().add(0, l, 0);
                    if (checkUp.getBlock().getType() == Material.LOG_2 || checkUp.getBlock().getType() == Material.LOG) {
                        ItemStack drop = type.getItemStack().clone();
                        drop.setAmount(1);
                        TitanBox.addItemToStorage(owner, drop.clone());
                        checkUp.getBlock().setType(Material.AIR);
                    } else {
                        break;
                    }
                }
            }
        }

    }
    public TreeHolder(Location locA)
    {
        Location loc1Base = getDirtBlock(locA).clone();
        loc[0] = loc1Base.clone();
        int i = 1;
        for (int x = -1; x< 2; x++) {
            for (int z = -1; z < 2; z++) {
                Location baseTmp = loc1Base.clone().add(x, 0, z);
                if (isTree(baseTmp.clone().add(0, 1, 0)))
                {
                    if (i < loc.length)
                    {
                        if (x != 0 || z != 0)
                        {
                            loc[i] = baseTmp.clone();
                            i++;
                        }
                    }
                }
            }
        }
        if (i > 3)
        {
            isbig =true;
            reorganizeLocations();
        }
        type = TreeTypeEnum.getFromBlock(locA.getBlock());

    }

    public void setType(TreeTypeEnum type) {
        this.type = type;
    }

    public boolean isBig() {
        return isbig;
    }
    public void setLoc(int index, Location location)
    {
        loc[index] = location.clone();
        isbig =false;
        for(Location l: loc)
        {
            if (l == null)
            {
                return;
            }
        }
        isbig = true;
        //reorganizeLocations();
    }
    // lower x higher z
    private void reorganizeLocations()
    {
        int x = loc[0].getBlockX();
        int z = loc[0].getBlockZ();
        for(int i =0; i < 4; i ++)
        {
            if (loc[i].getBlockX() < x)
            {
                x = loc[i].getBlockX();
            }
            if (loc[i].getBlockZ() < z)
            {
                z = loc[i].getBlockZ();
            }
        }
        for(int i =1; i < 4; i ++)
        {
            if (loc[i].getBlockZ() == z && loc[i].getBlockX() == x)
            {
                Location tmp = loc[0].clone();
                loc[0] = loc[i].clone();
                loc[i] = tmp.clone();
                return;
            }
        }
    }
    public Location getLocMain() {
        return loc[0].clone();
    }
    public Location getLoc(int index) {
        return loc[index].clone();
    }
    public TreeType getTreeType()
    {
        if (isbig)
        {
            if (type.getBigtype() != null)
            {
                return type.getBigtype();
            }
            if (type.getTalltype() != null)
            {
                return type.getTalltype();
            }
        }
        if (type.getType() == null)
        {
            return TreeTypeEnum.OAK.getType();
        }
        return type.getType();
    }
    public TreeTypeEnum getType() {
        return type;
    }
    public static Location getDirtBlock(Location loc)
    {
        for (int i = 0;i < 50; i++)
        {
            Location check = loc.clone().add(0, -1 * i, 0);
            if (check.getBlock().getType() != Material.LOG && check.getBlock().getType() != Material.LOG_2)
            {
                if (check.getBlock().getType() == Material.DIRT || check.getBlock().getType() == Material.GRASS)
                {
                    return  loc.clone().add(0, -1 * i, 0).clone();
                }
            }
        }
        return loc.clone();
    }
    public static Boolean isTree(Location loc)
    {
        if (loc.getBlock().getType() != Material.LOG && loc.getBlock().getType() != Material.LOG_2)
        {
            if (loc.getBlock().getType() == Material.SAPLING)
            {
                return true;
            }
            return false;
        }
        Boolean dirt = false;
        for (int i = 0;i < 50; i++)
        {
            Location check = loc.clone().add(0, -1 * i, 0);
            if (check.getBlock().getType() != Material.LOG && check.getBlock().getType() != Material.LOG_2)
            {
                if (check.getBlock().getType() == Material.DIRT || check.getBlock().getType() == Material.GRASS)
                {
                    dirt = true;
                    break;
                }
                else
                {
                    dirt = false;
                }
            }
        }
        if (!dirt)
        {
            return false;
        }
        for (int i = 0;i < 50; i++)
        {
            Location check = loc.clone().add(0,  i, 0);
            if (check.getBlock().getType() != Material.LOG && check.getBlock().getType() != Material.LOG_2)
            {
                if (check.getBlock().getType() == Material.LEAVES || check.getBlock().getType() == Material.LEAVES_2 || check.getBlock().getType() == Material.AIR)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }
}

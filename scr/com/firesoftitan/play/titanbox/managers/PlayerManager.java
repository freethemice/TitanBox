package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ArmorTypeEnum;
import com.firesoftitan.play.titanbox.enums.MonsterRanksEnum;
import net.minecraft.server.v1_14_R1.Chunk;
import net.minecraft.server.v1_14_R1.PacketPlayOutMapChunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private static HashMap<UUID, PlayerManager> playerDate = new HashMap<UUID, PlayerManager>();
    public HashMap<ArmorTypeEnum, MonsterRanksEnum> armorData;
    private Location IBR = null;
    private Location IDR = null;
    private XRayManager xRayManager;
    private Player player;
    public PlayerManager(Player player)
    {
        this.player = player;
        playerDate.put(player.getUniqueId(), this);
        armorData = new HashMap<ArmorTypeEnum, MonsterRanksEnum>();
        xRayManager = null;
    }
    public void addArmor(ItemStack itemStack)
    {
        if (itemStack != null) {
            if (Utilities.isArmor(itemStack)) {
                ArmorTypeEnum armorTypeEnum = ArmorTypeEnum.matchType(itemStack);
                MonsterRanksEnum monsterRanksEnum = MonsterRanksEnum.getGearLevel(itemStack);
                armorData.put(armorTypeEnum, monsterRanksEnum);
            }
        }
    }
    public void stopXRay()
    {

        if (player.isOnline()) {
            for (Chunk chunk : xRayManager.getChunks()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, 65535));
            }
        }
        xRayManager.unloadme();
        xRayManager = null;
    }
    public void startXRay(boolean xray, boolean caves)
    {
        if (xRayManager != null)
        {
            if (xRayManager.isCaves() != caves || xRayManager.isXray() != xray)
            {
                stopXRay();
            }
        }
        if (xRayManager == null) {
            xRayManager = new XRayManager(player.getLocation());
            xRayManager.setLastupDated();
            xRayManager.setCaves(caves);
            xRayManager.setXray(xray);
        }
    }
    public void processXRay()
    {
        if (xRayManager == null) {
            return;
        }
        if (xRayManager.getLastupDated() + XRayManager.updateTime * 1000 > System.currentTimeMillis())
        {
            if (xRayManager.getLoc().distance(player.getLocation()) < XRayManager.updatedistance) {
                return;
            }
        }
        boolean xray = xRayManager.isXray();
        boolean caves = xRayManager.isCaves();
        xRayManager.setLastupDated();

        World world = player.getWorld();
        org.bukkit.Chunk current = player.getLocation().getChunk();
        Location spawn = new Location(player.getWorld(), 0, player.getLocation().getBlockY(), 0);
        if (XRayManager.spawnProtection) {
            if (player.getLocation().distance(spawn) < XRayManager.spawnProtectedArea) {
                return;
            }
        }
        for (int x = -XRayManager.chunksToLoad; x < XRayManager.chunksToLoad; x++)
        {
            for (int z = -XRayManager.chunksToLoad; z < XRayManager.chunksToLoad; z++)
            {

                org.bukkit.Chunk toRender = world.getChunkAt(current.getX() + x, current.getZ() + z);
                if (!xRayManager.hasChunk(toRender)) {
                    net.minecraft.server.v1_14_R1.Chunk cool = XRayManager.xrayChunk(toRender, player, xray, caves);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(cool, 65535));
                    xRayManager.addChunk(toRender);
                }
            }
        }
    }
    public void setTitanDeathRecovery(Location TitanItemRecovery)
    {
        if (IDR == null) {
            IDR = TitanItemRecovery.clone();
        }
    }
    public boolean hasTitanDeathRecovery()
    {
        return IDR != null;
    }

    public Location getTitanDeathRecovery() {
        return IDR.clone();
    }
    public void removeTitanDeathRecovery()
    {
        IDR = null;
    }

    public void setTitanItemRecovery(Location TitanItemRecovery)
    {
        if (IBR == null) {
            IBR = TitanItemRecovery.clone();
        }
    }
    public boolean hasTitanItemRecovery()
    {
        return IBR != null;
    }

    public Location getTitanItemRecovery() {
        return IBR.clone();
    }
    public void removeTitanItemRecovery()
    {
        IBR = null;
    }

    public void removeArmor(ArmorTypeEnum armorTypeEnum)
    {
        armorData.remove(armorTypeEnum);
    }
    public MonsterRanksEnum getHighestType()
    {
        MonsterRanksEnum ranksEnum = MonsterRanksEnum.NORMAL;
        for(MonsterRanksEnum monsterRanksEnum: armorData.values())
        {
            if (monsterRanksEnum.getIndex() > ranksEnum.getIndex())
            {
                ranksEnum = monsterRanksEnum;
            }
        }
        return ranksEnum;
    }
    public static PlayerManager getPlayer(Player player)
    {
        return playerDate.get(player.getUniqueId());
    }

}

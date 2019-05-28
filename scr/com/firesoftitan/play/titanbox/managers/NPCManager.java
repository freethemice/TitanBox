package com.firesoftitan.play.titanbox.managers;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NPCManager {
    private static HashMap<String, EntityPlayer> npcs  = new HashMap<String, EntityPlayer>();
    public static void load()
    {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        for (World world: Bukkit.getWorlds())
        {
            WorldServer nmsWorld = ((CraftWorld)world).getHandle();
            EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.randomUUID(), "TB_NPC_" + world.getName()), new PlayerInteractManager(nmsWorld));
            npc.setLocation(world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ(), 0, 0);
            CraftPlayer opCr = npc.getBukkitEntity();
            opCr.setGameMode(GameMode.SURVIVAL);
            opCr.getPlayer().setGameMode(GameMode.SURVIVAL);
            opCr.getHandle().playerInteractManager.setGameMode(EnumGamemode.SURVIVAL);
            opCr.setOp(true);
            npcs.put(world.getName(), npc);
        }
    }
    public static List<org.bukkit.entity.Entity> getNearbyEntities(Location loc)
    {
        return getNearbyEntities(loc, 5);
    }
    public static List<org.bukkit.entity.Entity>  getNearbyEntities(Location loc, int rad)
    {
        EntityPlayer npc = npcs.get(loc.getWorld().getName());
        if (npc == null) return null;
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        List<Entity> nearEntity = opCr.getNearbyEntities(rad,rad,rad);
        return nearEntity;
    }
    public static EntityPlayer getNMSNPC(Location loc)
    {
        EntityPlayer npc = npcs.get(loc.getWorld().getName());
        if (npc == null) return null;
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        return npc;
    }
    public static CraftPlayer getCraftNPC(Location loc)
    {
        EntityPlayer npc = npcs.get(loc.getWorld().getName());
        if (npc == null) return null;
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        return opCr;
    }
    public static boolean isNPC(Player player)
    {
        return isNPC(player.getUniqueId());
    }
    public static boolean isNPC(UUID uuid)
    {
        for(EntityPlayer entityPlayer: npcs.values())
        {
            if (uuid.equals(entityPlayer.getUniqueID()))
            {
                return true;
            }
        }
        return false;
    }
    public static void sendCommand(Location loc, String command)
    {
        EntityPlayer npc = npcs.get(loc.getWorld().getName());
        if (npc == null) return;
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        CraftPlayer opCr = npc.getBukkitEntity();
        Bukkit.dispatchCommand(opCr, command);
    }
}

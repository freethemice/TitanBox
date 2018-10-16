package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.machines.Pumps;
import com.firesoftitan.play.titansql.ResultData;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KillerModule extends MainModule {


    private Location killerPump = null;
    private Long lastran = Long.valueOf(0);
    public KillerModule()
    {
        type = ModuleTypeEnum.Killer;
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
            Location from = killerPump;
            if (killerPump == null || from == null || from.getWorld() == null)
            {
                return ChatColor.WHITE  + "not set.";
            }
            return ChatColor.WHITE + from.getWorld().getName() + ": " + from.getBlockX() + ": " + from.getBlockY() + ": " + from.getBlockZ();
        }
    }
    @Override
    public void OpenGui(Player player)
    {

    }
    @Override
    public void unLinkAll()
    {
        this.link = null;
        this.killerPump = null;
        saveInfo();
    }
    @Override
    public boolean setLink(Location link, Player player) {
        super.setLink(link, player);
        String pump = Pumps.getPumpType(link);
        this.link = null;
        if (pump != null)
        {
            if (pump.equals("Killer"))
            {
                this.killerPump = link.clone();
                saveInfo();
                if (player != null) {
                player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Killer Block linked!");
            }
                return true;
            }
        }
        saveInfo();
        return false;
    }
    @Override
    public void loadInfo(HashMap<String, ResultData> result)
    {
        super.loadInfo(result);
        killerPump = null;
        if (result.get("pump_a") != null) {
            if (result.get("pump_a").getLocation() != null) {
                killerPump = result.get("pump_a").getLocation().clone();
            }
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        modulesSQL.setDataField("pump_a", killerPump);
        this.sendDate();

        //modules.setValue("modules." + moduleid + ".slots.killerpump", killerPump);
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }

    @Override
    public ItemStack getMeAsIcon()
    {
        if (isLoaded()) {
            if (Pumps.getLiquid(killerPump, "Killer")) {
                return new ItemStack(Material.DIAMOND_SWORD, 1);
            }
        }
        return new ItemStack(Material.BARRIER, 1);

    }
    @Override
    public boolean isLoaded()
    {
        if (killerPump != null) {
            if (killerPump.getChunk() != null) {
                return Utilities.isLoaded(killerPump);
            }
        }
        return false;
    }

    @Override
    public void runMe(UUID owner)
    {
        if (lastran + 100  < System.currentTimeMillis()) {
            lastran = System.currentTimeMillis();
            if (killerPump != null) {
                if (Pumps.getLiquid(killerPump, "Killer")) {
                    try {
                        int Damage = 10;

                        EntityPlayer npc = TitanBox.instants.npcs.get(killerPump.getWorld().getName());
                        npc.setLocation(killerPump.getX(), killerPump.getY(), killerPump.getZ(), 0, 0);
                        CraftPlayer opCr = npc.getBukkitEntity();
                        List<Entity> nearEntity = npc.getBukkitEntity().getNearbyEntities(5,5,5);
                        for (int i = 0; i < nearEntity.size(); i++) {
                            if (nearEntity.get(i).getType() != EntityType.PLAYER) {
                                if (nearEntity.get(i) instanceof LivingEntity) {
                                    if (!nearEntity.get(i).isDead()) {
                                        if (nearEntity.get(i).getLocation().distance(killerPump) < 5) {
                                            if (nearEntity.get(i).getTicksLived() > 10) {
                                                try {
                                                    ((LivingEntity) nearEntity.get(i)).damage(4 * Damage, opCr);
                                                } catch (Exception e) {
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {

                    }

                }
            }
        }
    }
}

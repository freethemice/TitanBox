package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.machines.Pumps;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            return ChatColor.WHITE + from.getWorld().getName() + ": " + from.getBlockX() + ": " + from.getBlockY() + ": " + from.getBlockZ();
        }
    }
    @Override
    public void OpenGui(Player player)
    {

    }
    @Override
    public boolean setLink(Location link, Player player) {
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
    public void loadInfo() {
        super.loadInfo();
        killerPump = null;
        if (modules.contains("modules." + moduleid + ".slots.killerpump")) {
            killerPump = modules.getLocation("modules." + moduleid + ".slots.killerpump");
        }
    }

    @Override
    public void saveInfo() {
        super.saveInfo();
        modules.setValue("modules." + moduleid + ".slots.killerpump", killerPump);
    }

    @Override
    public void clearInfo()
    {
        super.clearInfo();
    }

    @Override
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.DIAMOND_SWORD, 1);
    }
    @Override
    public boolean isLoaded()
    {
        if (killerPump != null) {
            return killerPump.getChunk().isLoaded();
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

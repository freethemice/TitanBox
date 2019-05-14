package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.TurretManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ForceFieldRunnable implements Runnable{

    private HashMap<UUID, ForceFieldManager> inForceField = new HashMap<UUID, ForceFieldManager>();
    private HashMap<UUID, ForceFieldManager> inAreaForceField = new HashMap<UUID, ForceFieldManager>();

    @Override
    public void run() {
        for(WorldManager worldManager : WorldManager.getAll()) {
            for(ForceFieldManager forceFieldManager : worldManager.getAllFields()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!forceFieldManager.isInRang(player.getLocation(), 500) && inAreaForceField.get(player.getUniqueId()) == forceFieldManager)
                    {
                        inAreaForceField.remove(player.getUniqueId());
                        if (!forceFieldManager.isAdmin()) {
                            if (!forceFieldManager.hasRights(player.getUniqueId())) {
                                player.sendMessage(ChatColor.RED + "You left a Force Field's turret area!");
                            }
                        }
                    }
                    if (forceFieldManager.getLocation().getWorld().getName().equals(player.getWorld().getName())) {
                        if (!inAreaForceField.containsKey(player.getUniqueId())) {
                            if (forceFieldManager.isInRang(player.getLocation(), 500)) {
                                if (!forceFieldManager.isAdmin()) {
                                    if (!forceFieldManager.hasRights(player.getUniqueId())) {
                                        inAreaForceField.put(player.getUniqueId(), forceFieldManager);
                                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(forceFieldManager.getOwner());
                                        String name = ChatColor.WHITE + offlinePlayer.getName();
                                        player.sendMessage(ChatColor.RED + "You are approaching a Force Field, and have entered into its turret area: " + name);
                                        player.sendMessage(ChatColor.RED + "You can find " + ChatColor.WHITE + forceFieldManager.getTurretCount() + ChatColor.RED + " turrets in this area.");
                                    }
                                }
                            }
                        }
                    }
                    if (inForceField.get(player.getUniqueId()) == forceFieldManager && !forceFieldManager.isInRang(player.getLocation()))
                    {
                        inForceField.remove(player.getUniqueId());
                        player.sendMessage(ChatColor.YELLOW + "You have left a force field!");
                    }
                    if (forceFieldManager.getLocation().getWorld().getName().equals(player.getWorld().getName())) {
                        if (!inForceField.containsKey(player.getUniqueId())) {
                            if (forceFieldManager.isInRang(player.getLocation())) {
                                inForceField.put(player.getUniqueId(), forceFieldManager);
                                String name = ChatColor.GREEN + "Admin";
                                if (!forceFieldManager.isAdmin()) {
                                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(forceFieldManager.getOwner());
                                    name = ChatColor.WHITE + offlinePlayer.getName();
                                }
                                player.sendMessage(ChatColor.YELLOW + "You have entered in to a force field: " + name);
                            }
                        }
                    }

                    if (TitanBox.settings.getBoolean("settings." + player.getUniqueId().toString() + ".particle")) {
                        if (player.getLocation().getWorld().getName().equals(forceFieldManager.getLocation().getWorld().getName())) {
                            if ((player.getLocation().distance(forceFieldManager.getLocation()) < forceFieldManager.getSize() + 15) && (player.getLocation().distance(forceFieldManager.getLocation()) > forceFieldManager.getSize() - 15)) {
                                forceFieldManager.drawMe(player);
                            }
                        }
                    }


                    if (forceFieldManager.isInRang(player.getLocation(), 500)) {
                        List<TurretManager> turretManagers = forceFieldManager.turretInRange(player.getLocation());
                        for (TurretManager turretManager : turretManagers) {
                            if (turretManager != null) {
                                if (!turretManager.getLocation().clone().add(0, -1, 0).getBlock().getType().toString().endsWith("_FENCE")) {
                                    forceFieldManager.removeTurret(turretManager.getLocation());
                                    turretManager.getLocation().getBlock().setType(Material.AIR);
                                    break;
                                }
                                rotateTurret(player, turretManager);
                                if (!forceFieldManager.hasRights(player.getUniqueId())) {
                                    if (player.getGameMode() == GameMode.SURVIVAL) {
                                        if (!player.isDead()) {
                                            double healthHit = (double) (player.getHealth() - 0.2f);
                                            if (healthHit < 0) healthHit = 0;
                                            player.setHealth(healthHit);
                                            player.playSound(turretManager.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }



                }
            }
        }
    }

    private void rotateTurret(Player player, TurretManager turretManager) {
        if (turretManager.getLocation().getBlock().getType() == Material.PLAYER_HEAD || turretManager.getLocation().getBlock().getType() == Material.PLAYER_WALL_HEAD)
        {
            Block block = turretManager.getLocation().getBlock();
            Location location = turretManager.getLocation();
            BlockFace[] radial = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};
            BlockFace facing = radial[Math.round(lookAt(player.getLocation(), location) / 45f) & 0x7].getOppositeFace();
            BlockFace targetFace = ((Rotatable) block.getState().getBlockData()).getRotation();
            if (targetFace != facing) {
                try {
                    BlockState blockState = block.getState();
                    Rotatable rotatable = (Rotatable) blockState.getBlockData();
                    rotatable.setRotation(facing.getOppositeFace());
                    block.setBlockData(rotatable);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public float lookAt(Location point, Location npcLoc) {

        double xDiff = point.getX() - npcLoc.getX();
        double zDiff = point.getZ() - npcLoc.getZ();

        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;

        if (zDiff < 0.0)
            newYaw = newYaw + Math.abs(180 - newYaw) * 2;
        newYaw = (newYaw - 90);

        return (float) newYaw;

    }
}

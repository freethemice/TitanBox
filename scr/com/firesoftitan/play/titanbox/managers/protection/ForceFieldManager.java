package com.firesoftitan.play.titanbox.managers.protection;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.PrivacyEnum;
import com.firesoftitan.play.titanbox.managers.NPCManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class ForceFieldManager {
    private double size;
    private double max;
    private double min;
    private double efficacy;
    private Location location;
    private UUID owner = null;
    private boolean admin = false;
    private List<UUID> friendList = null;
    private double theta = 0; // max 3.14
    private double gama = 0;
    private boolean needsaving = false;
    private String id;
    private String name;
    private PrivacyEnum privacy;
    private List<TurretManager> turretManagers;
    public ForceFieldManager(UUID owner, Location location)
    {
        this.turretManagers = new ArrayList<TurretManager>();
        friendList = new ArrayList<UUID>();
        this.location = location;
        this.size = 250;
        this.max = 1000;
        this.owner = owner;
        this.privacy = PrivacyEnum.FRIENDS;
        needsaving = true;
        name = "Force Field";
    }
    public void addTurret(Location location)
    {
        TurretManager tmp = new TurretManager(location.clone());
        turretManagers.add(tmp);
        needsaving = true;
    }
    public void removeTurret(Location location)
    {
        for(int i = 0; i < turretManagers.size(); i++)
        {
            if (Utilities.areEqual(location, turretManagers.get(i).getLocation()))
            {
                turretManagers.remove(i);
                break;
            }
        }
        needsaving = true;
    }
    public List<TurretManager> turretInRange(Location location)
    {
        List<TurretManager> tmpe = new ArrayList<TurretManager>();
        for(int i = 0; i < turretManagers.size(); i++)
        {
            if (location.distance(turretManagers.get(i).getLocation()) < 11)
            {
                tmpe.add(turretManagers.get(i));
            }
        }
        return tmpe;
    }
    public PrivacyEnum getPrivacy() {
        return privacy;
    }

    public ItemStack getPricacyItem()
    {
        if (this.isAdmin())
        {
            return Utilities.clearLore(SlimefunItemsManager.FORCE_FIELD_YELLOW.clone());
        }
        if (this.privacy == PrivacyEnum.FRIENDS) {
            return Utilities.clearLore(SlimefunItemsManager.FORCE_FIELD_BLUE.clone());
        }
        if (this.privacy == PrivacyEnum.PUBLIC) {
            return Utilities.clearLore(SlimefunItemsManager.FORCE_FIELD_YELLOW.clone());
        }
        if (this.privacy == PrivacyEnum.PRIVATE) {
            return Utilities.clearLore(SlimefunItemsManager.FORCE_FIELD_GREEN.clone());
        }
        return Utilities.clearLore(SlimefunItemsManager.FORCE_FIELD_YELLOW.clone());
    }
    public String getPrivacyTexture()
    {
        if (this.isAdmin())
        {
            return SlimefunItemsManager.FORCE_FIELD_YELLOW_S;
        }
        if (this.privacy == PrivacyEnum.FRIENDS) {
            return SlimefunItemsManager.FORCE_FIELD_BLUE_S;
        }
        if (this.privacy == PrivacyEnum.PUBLIC) {
            return SlimefunItemsManager.FORCE_FIELD_YELLOW_S;
        }
        if (this.privacy == PrivacyEnum.PRIVATE) {
            return SlimefunItemsManager.FORCE_FIELD_GREEN_S;
        }
        return SlimefunItemsManager.FORCE_FIELD_YELLOW_S;
    }
    public void setPrivacy(PrivacyEnum privacy) {
        try {
            this.privacy = privacy;
            needsaving = true;
            Block me = location.getBlock();
            if (this.privacy == PrivacyEnum.FRIENDS) {
                CustomSkull.setSkull(me, SlimefunItemsManager.FORCE_FIELD_BLUE_S);
            }
            if (this.privacy == PrivacyEnum.PUBLIC) {
                CustomSkull.setSkull(me, SlimefunItemsManager.FORCE_FIELD_YELLOW_S);
            }
            if (this.privacy == PrivacyEnum.PRIVATE) {
                CustomSkull.setSkull(me, SlimefunItemsManager.FORCE_FIELD_GREEN_S);
            }
        } catch (Exception e) {

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        needsaving = true;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void saved() {
        this.needsaving = false;
    }

    public boolean needSaving() {
        return needsaving;
    }

    public double getEfficiency() {
        return efficacy;
    }

    public void setEfficiency(double efficacy) {
        needsaving = true;
        this.efficacy = efficacy;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        needsaving = true;
        this.min = min;
    }

    public void setMax(double max) {
        needsaving = true;
        this.max = max;
    }

    public double getMax() {
        needsaving = true;
        return max;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        needsaving = true;
        this.size = size;
    }

    public void  addFriend(UUID friend)
    {
        needsaving = true;
        friendList.add(friend);
    }
    public void removeFriend(UUID friend)
    {
        needsaving = true;
        friendList.remove(friend);
    }
    public boolean isFriend(UUID player)
    {
        return friendList.contains(player);
    }
    public boolean hasRights(UUID player)
    {
        return  hasRights(player, true);
    }
    public boolean hasRights(UUID player, boolean message)
    {
        try {
            if (NPCManager.isNPC(player)) return true;
            Player p = Bukkit.getPlayer(player);
            if (this.isAdmin()) {
                if (p.hasPermission("titanbox.admin")) {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            if (this.owner.equals(player) || this.isFriend(player)) return true;
            if (p.hasPermission("titanbox.admin") && TitanBox.bypassProtection.contains(p.getUniqueId())) return true;
            if (p.hasPermission("titanbox.admin") && message) p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/override" + ChatColor.RED + " to over ride protection.");
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public List<UUID> getFriends() {
        return friendList;
    }
    public List<String> getFriendsForSave() {
        List<String> tmp = new ArrayList<String>();
        for(UUID toAdd: friendList)
        {
            tmp.add(toAdd.toString());
        }
        return tmp;
    }
    public int getTurretCount()
    {
        return turretManagers.size();
    }
    public List<String> getTurretsForSave() {
        List<String> tmp = new ArrayList<String>();
        for(TurretManager toAdd: turretManagers)
        {
            tmp.add(Utilities.serializeLocation(toAdd.getLocation()));
        }
        return tmp;
    }
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {

        return owner;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        needsaving = true;
        this.admin = admin;
    }

    public void drawMe(Player player)
    {
        Location point = player.getLocation().clone();
        double angleX = point.getBlockX() - location.getBlockX();
        double angleZ = point.getBlockZ() - location.getBlockZ();
        double theta = 0; // max 3.14
        double gama = Math.atan2(angleX, angleZ); // max 2*3.14
        for (theta = 0; theta < 2*Math.PI; theta = theta + (2*Math.PI)/(size*4)) {
            for (gama = 0; gama < (2*Math.PI); gama = gama + (2*Math.PI/(size*3))) {
                double x = location.getBlockX() + size * Math.sin(theta) * Math.cos(gama);
                double z = location.getBlockZ() + size * Math.sin(theta) * Math.sin(gama);
                double y = location.getBlockY() + size * Math.cos(theta);
                Location newPoint = new Location(point.getWorld(), x, y, z);
                if (player.getLocation().distance(newPoint) < 15) {
                    player.spawnParticle(Particle.VILLAGER_HAPPY, newPoint, 10);
                }
            }
        }
    }

    public Location getLocation() {
        return location.clone();
    }
    public boolean isInRang(Location location, double size)
    {

        if (location.getWorld().getName().equals(this.getLocation().getWorld().getName())) {
            double dis = getDistance(location.clone());
            if (dis <= size) {
                return true;
            }
        }

        return false;
    }
    public boolean isInRang(Location location)
    {
        return isInRang(location.clone(), this.size);
    }
    public double getDistance(Location location)
    {
        return this.location.distance(location);
    }


}

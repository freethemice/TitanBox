package com.firesoftitan.play.titanbox.managers.protection;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.PrivacyEnum;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerProtectionManager {
    private UUID player;
    private HashMap<String, ForceFieldManager> forceFieldHolder;
    private static HashMap<UUID, PlayerProtectionManager> protectionCount = new HashMap<UUID, PlayerProtectionManager>();
    public PlayerProtectionManager(UUID player)
    {
        this.player = player;
        this.forceFieldHolder = new HashMap<String, ForceFieldManager>();
    }
    public static PlayerProtectionManager getPlayer(Player player)
    {
        return getPlayer(player.getUniqueId());
    }
    public static PlayerProtectionManager getPlayer(UUID uuid)
    {
        PlayerProtectionManager ppH = protectionCount.get(uuid);
        if (ppH == null)
        {
            ppH = new PlayerProtectionManager(uuid);
            protectionCount.put(uuid, ppH);
        }
        return ppH;
    }
    public void add(ForceFieldManager ffH)
    {
        forceFieldHolder.put(Utilities.serializeLocation(ffH.getLocation()), ffH);
    }
    public void remove(Location location)
    {
        String key = Utilities.serializeLocation(location.clone());
        forceFieldHolder.remove(key);
    }
    public ForceFieldManager getForceField(Location location)
    {
        return forceFieldHolder.get(Utilities.serializeLocation(location));
    }
    public int count()
    {
        return forceFieldHolder.size();
    }
    public Collection<ForceFieldManager> getForceFields()
    {
        return this.forceFieldHolder.values();
    }
    public Collection<ForceFieldManager> getForceFieldsNotPrivate()
    {
        HashMap<String, ForceFieldManager> BRforceFieldHolder = new HashMap<String, ForceFieldManager>();

        for(WorldManager WH: WorldManager.getAll())
        {
            for(ForceFieldManager ffH: WH.getAllFields())
            {
                if (!ffH.isAdmin() && !ffH.getOwner().equals(player)) {
                    if (ffH.getPrivacy() == PrivacyEnum.FRIENDS) {
                        if (ffH.hasRights(player, false)) {
                            String key = Utilities.serializeLocation(ffH.getLocation().clone());
                            BRforceFieldHolder.put(key, ffH);
                        }
                    }
                    if (ffH.getPrivacy() == PrivacyEnum.PUBLIC) {
                        String key = Utilities.serializeLocation(ffH.getLocation().clone());
                        BRforceFieldHolder.put(key, ffH);
                    }
                }

            }
        }
        return BRforceFieldHolder.values();
    }
}

package com.firesoftitan.play.titanbox.managers.protection;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.PrivacyEnum;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titansql.CallbackResults;
import com.firesoftitan.play.titansql.DataTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.Table;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class WorldManager {

    public static Table protectionSQL = new Table("tb_protection");
    private static HashMap<String, WorldManager> protection = new HashMap<String, WorldManager>();
    private HashMap<String, ForceFieldManager> regionList = null;
    private World world = null;
    public static WorldManager getWorldHolder(World world)
    {
        return getWorldHolder(world.getName());
    }
    private static WorldManager getWorldHolder(String world)
    {
        return protection.get(world);
    }
    public static Collection<WorldManager> getAll()
    {
        return protection.values();
    }
    public WorldManager(World world)
    {
        regionList = new HashMap<String, ForceFieldManager>();
        this.world = world;
    }
    public int Size()
    {
        return regionList.size();
    }
    public ForceFieldManager getFieldAt(Location location)
    {
        return regionList.get(Utilities.serializeLocation(location));
    }
    public ForceFieldManager getFieldIn(Location location)
    {
        Collection<ForceFieldManager> tmp = regionList.values();
        for(ForceFieldManager forceFieldManager : tmp)
        {
            if (forceFieldManager.isInRang(location)) return forceFieldManager;
        }
        return null;
    }
    public ForceFieldManager getFieldIn(Location location, int range)
    {
        Collection<ForceFieldManager> tmp = regionList.values();
        for(ForceFieldManager forceFieldManager : tmp)
        {
            if (forceFieldManager.isInRang(location, range)) return forceFieldManager;
        }
        return null;
    }
    public ForceFieldManager canPlaceNewForceField(Location location, ForceFieldManager newField)
    {
        return canPlaceNewForceField(location, newField, 500);
    }
    public ForceFieldManager canPlaceNewForceField(Location location, ForceFieldManager newField, int buffer)
    {
        Collection<ForceFieldManager> tmp = regionList.values();
        for(ForceFieldManager forceFieldManager : tmp)
        {
            if (!Utilities.areEqual(location, forceFieldManager.getLocation())) {
                if (forceFieldManager.isInRang(location, forceFieldManager.getMax() + newField.getMax() + buffer)) {
                    return forceFieldManager;
                }
            }
        }
        return null;
    }
    public boolean addNewForceField(ForceFieldManager newField)
    {
        if (canPlaceNewForceField(newField.getLocation(), newField) == null)
        {
            regionList.put(Utilities.serializeLocation(newField.getLocation()), newField);
            return  true;
        }
        return false;
    }
    public void removeForceField(Location location)
    {
        String ID = regionList.get(Utilities.serializeLocation(location)).getId();
        protectionSQL.delete("id", ID);
        regionList.remove(Utilities.serializeLocation(location));
    }
    public World getWorld() {
        return world;
    }
    public String getName()
    {
        return world.getName();
    }
    public static void setupTable() {
        protectionSQL.addDataType("id", DataTypeEnum.CHARARRAY, true, false, true);
        protectionSQL.addDataType("owner", DataTypeEnum.UUID, false, false, false);
        protectionSQL.addDataType("friends", DataTypeEnum.STRINGLIST, false, false, false);
        protectionSQL.addDataType("size", DataTypeEnum.INTEGER, false, false, false);
        protectionSQL.addDataType("max", DataTypeEnum.INTEGER, false, false, false);
        protectionSQL.addDataType("min", DataTypeEnum.INTEGER, false, false, false);
        protectionSQL.addDataType("efficiency", DataTypeEnum.INTEGER, false, false, false);
        protectionSQL.addDataType("location", DataTypeEnum.LOCATION, false, false, false);
        protectionSQL.addDataType("world", DataTypeEnum.STRING, false, false, false);
        protectionSQL.addDataType("admin", DataTypeEnum.BOOLEAN, false, false, false);
        protectionSQL.addDataType("name", DataTypeEnum.CHARARRAY, false, false, false);
        protectionSQL.addDataType("network", DataTypeEnum.INTEGER, false, false, false);
        protectionSQL.addDataType("turrets", DataTypeEnum.STRINGLIST, false, false, false);
        protectionSQL.createTable();
    }
    public Collection<ForceFieldManager> getAllFields()
    {
        return regionList.values();
    }
    public String getNewIDString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random(System.currentTimeMillis());
        while (salt.length() < 36) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        for(ForceFieldManager ffH: regionList.values())
        {
            if (ffH.getId().equals(saltStr))
            {
                return getNewIDString();
            }

        }
        return saltStr;

    }
    public static void loadProtection()
    {
        for (World world: Bukkit.getWorlds())
        {
            protection.put(world.getName(), new WorldManager(world));
        }

        Utilities.reTryLoad(protectionSQL, MainModule.class, "loadProtection", "protection");
        protectionSQL.search(new CallbackResults() {
            @Override
            public void onResult(List<HashMap<String, ResultData>> results) {
                if (results != null && results.size() >0) {
                    for (HashMap<String, ResultData> result : results) {
                        String key = result.get("id").getString();
                        UUID owner = result.get("owner").getUUID();
                        List<String> friends = result.get("friends").getStringList();
                        int size = result.get("size").getInteger();
                        int max = result.get("max").getInteger();
                        int steps = result.get("min").getInteger();
                        int efficiency = result.get("efficiency").getInteger();
                        Location location = result.get("location").getLocation();
                        String world = result.get("world").getString();
                        Boolean admin = result.get("admin").getBoolean();
                        String name = result.get("name").getString();
                        int network = result.get("network").getInteger();
                        List<String> turrets = result.get("turrets").getStringList();

                        WorldManager WH = WorldManager.protection.get(world);
                        ForceFieldManager ffH = new ForceFieldManager(owner, location);
                        ffH.setId(key);
                        ffH.setMin(steps);
                        ffH.setMax(max);
                        ffH.setSize(size);
                        ffH.setAdmin(admin);
                        ffH.setEfficiency(efficiency);
                        ffH.setName(name);
                        ffH.setPrivacy(PrivacyEnum.valueOf(network));
                        if (friends != null) {
                            for (String friend : friends) {
                                UUID toadd = UUID.fromString(friend);
                                ffH.addFriend(toadd);
                            }
                        }
                        if (turrets != null)
                        {
                            for (String turret: turrets)
                            {
                                Location location1 = Utilities.deserializeLocation(turret);
                                ffH.addTurret(location1);
                                Utilities.placeSkull(location1.getBlock(), SlimefunItemsManager.TURRET);
                            }
                        }
                        PlayerProtectionManager ppH = PlayerProtectionManager.getPlayer(ffH.getOwner());
                        if (!ffH.isAdmin()) {
                            ppH.add(ffH);
                        }
                        else
                        {
                            TitanBox.instants.adminList.add(ffH);
                        }
                        WH.addNewForceField(ffH);
                        ffH.saved();
                        Utilities.placeSkull(ffH.getLocation().getBlock(), ffH.getPrivacyTexture());
                    }
                }

                Utilities.doneTryLoading(protectionSQL);
            }
        });

    }
    public void Save()
    {
     for(ForceFieldManager ffH: regionList.values())
     {
         if (ffH.needSaving())
         {
             saveMe(ffH);
             ffH.saved();
         }
     }
    }
    private void saveMe(ForceFieldManager forceFieldManager)
    {
        List<String> friends = forceFieldManager.getFriendsForSave();
        List<String> turrets = forceFieldManager.getTurretsForSave();
        protectionSQL.setDataField("id", forceFieldManager.getId());
        protectionSQL.setDataField("owner", forceFieldManager.getOwner());
        protectionSQL.setDataField("friends", friends);
        protectionSQL.setDataField("size", (int) forceFieldManager.getMax()); // this is for safty//
        protectionSQL.setDataField("max", (int) forceFieldManager.getMax());
        protectionSQL.setDataField("min", (int) forceFieldManager.getMin());
        protectionSQL.setDataField("efficiency", (int) forceFieldManager.getEfficiency());
        protectionSQL.setDataField("location", forceFieldManager.getLocation());
        protectionSQL.setDataField("admin", forceFieldManager.isAdmin());
        protectionSQL.setDataField("world", forceFieldManager.getLocation().getWorld().getName());
        protectionSQL.setDataField("name", forceFieldManager.getName());
        protectionSQL.setDataField("network", forceFieldManager.getPrivacy().getValue());
        protectionSQL.setDataField("turrets", turrets);
        protectionSQL.insertData();
    }
}

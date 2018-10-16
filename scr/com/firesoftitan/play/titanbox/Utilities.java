package com.firesoftitan.play.titanbox;

import com.firesoftitan.play.titansql.Table;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Utilities {
    private static HashMap<String, Boolean> loadedSQLs = new HashMap<String, Boolean>();

    private Utilities()
    {

    }
    public static boolean isLoaded(Location loc)
    {
        if (loc == null) return false;
        World world = loc.getWorld();
        if (world.isChunkLoaded((int) loc.getBlockX() >> 4, (int) loc.getBlockZ() >> 4)) {
            return true;
        }
        return false;
    }
    public static boolean isDoneLoading(Table sqlTable)
    {
        if (!loadedSQLs.containsKey(sqlTable.getName()))  return false;
        return loadedSQLs.get(sqlTable.getName());
    }
    public static void doneTryLoading(Table sqlTable)
    {
        loadedSQLs.put(sqlTable.getName(), true);
    }
    public static String encode(Location location) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i.x", location.getX());
        config.set("i.y", location.getY());
        config.set("i.z", location.getZ());
        config.set("i.pitch", location.getPitch() + "");
        config.set("i.yaw", location.getYaw() + "");
        if (location.getWorld() == null)
        {
            config.set("i.world", "worldmain");
        }
        else {
            config.set("i.world", location.getWorld().getName());
        }
        return DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }
    /**
     * Decodes an {@link Location} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link Location}
     */
    public static Location decodeLocation(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        double x = config.getDouble("i.x");
        double y = config.getDouble("i.y");
        double z = config.getDouble("i.z");
        float pitch =  Float.valueOf(config.getString("i.pitch"));
        float yaw = Float.valueOf(config.getString("i.yaw"));
        String worldname = config.getString("i.world");
        World world = Bukkit.getWorld(worldname);
        Location location = new Location(world, x, y, z, yaw, pitch);
        return  location.clone();
    }
    public static void reTryLoad(Table sqlTable, Class cls, String MethdoName, String Name)
    {
        try {
            Method getLabel = cls.getMethod(MethdoName, null);


            if (!loadedSQLs.containsKey(sqlTable.getName())) {
                loadedSQLs.put(sqlTable.getName(), false);
            }
            if (!loadedSQLs.get(sqlTable.getName())) {
                final long resetTimer = System.currentTimeMillis() + 4 * 60 * 1000;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        long left = resetTimer - System.currentTimeMillis();
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(left);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(left);

                        if (!loadedSQLs.get(sqlTable.getName()))
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "broadcast " + Name + " database is loading. Will retry in: " + ChatColor.WHITE + minutes + "mins " + seconds + "seconds");
                            if (System.currentTimeMillis() >= resetTimer) {
                                try {
                                    String label = (String)getLabel.invoke(null);
                                    this.cancel();
                                } catch (Exception e) {

                                }
                            }
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "broadcast " + Name + " database loaded!");
                            this.cancel();
                        }

                        }
                    }.runTaskTimer(TitanBox.instants, 20,20);



                }
            }
            catch (Exception e)
            {

            }
    }
}

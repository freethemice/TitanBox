package com.firesoftitan.play.titanbox;

import com.firesoftitan.play.titanbox.machines.ItemRecovery;
import com.firesoftitan.play.titanbox.machines.StorageUnit;
import com.firesoftitan.play.titanbox.managers.ConfigManager;
import com.firesoftitan.play.titanbox.managers.NPCManager;
import com.firesoftitan.play.titanbox.managers.PlayerManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import com.firesoftitan.play.titansql.Table;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Reflection.CraftObject;
import me.mrCookieSlime.CSCoreLibPlugin.general.Reflection.PackageName;
import me.mrCookieSlime.CSCoreLibPlugin.general.Reflection.ReflectionUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.TileEntityTypes;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utilities {
    private static HashMap<String, Boolean> loadedSQLs = new HashMap<String, Boolean>();
    private static final NavigableMap<Long, String> suffixes = new TreeMap<> ();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }
    private Utilities()
    {

    }
    public static boolean hasBuildRights(Player player, Location location)
    {
        return hasBuildRights(player, location, false);
    }
    public static boolean hasBuildRights(Player player, Location location, boolean useNAdminFF)
    {
        try {
            WorldManager worldManager = WorldManager.getWorldHolder(location.getWorld());
            ForceFieldManager forceFieldManager = worldManager.getFieldIn(location);
            if (forceFieldManager == null) return true;
            if (useNAdminFF && forceFieldManager.isAdmin()) return true;
            return forceFieldManager.hasRights(player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean areEqual(Location location1, Location location2)
    {
        if (location1.getWorld().getName().equals(location2.getWorld().getName()))
        {
            if (location1.getBlockX() == location2.getBlockX())
            {
                if (location1.getBlockY() == location2.getBlockY())
                {
                    if (location1.getBlockZ() == location2.getBlockZ())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void startTeleport(Player player, ForceFieldManager ffH)
    {
        startTeleport(player, ffH.getLocation());
    }
    public static void startTeleport(Player player, Location location)
    {
        int time = 9;
        if (WorldManager.getWorldHolder(player.getLocation().getWorld()).getFieldIn(player.getLocation()) !=null)
        {
            time = 3;
        }
        startTeleport(player, location, time);
    }
    public static void startTeleport(Player player, Location location, int time)
    {
        final int x =player.getLocation().getBlockX();
        final int y =player.getLocation().getBlockY();
        final int z =player.getLocation().getBlockZ();

        player.sendMessage(ChatColor.RED + "You Will Be Teleported in " + (time + 1) +" Seconds");
        int finalTime = time;
        int myId = Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, new Runnable() {
            private int passed = 0;
            private boolean ran = false;
            @Override
            public void run() {
                if (!ran) {
                    passed++;
                    int countT =  (finalTime - passed) + 1;
                    if (passed > finalTime) {
                        if ((player.getLocation().getBlockX() != x) || (player.getLocation().getBlockY() != y) || (player.getLocation().getBlockZ() != z)) {
                            player.sendMessage(ChatColor.RED + "Teleport Canceled!");
                        } else {
                            player.teleport(location.clone().add(0, 2, 0));
                            player.sendMessage(ChatColor.RED + "Your new location is: " + location.getBlock().getX() + ", " + location.getBlock().getY() + ", " + location.getBlock().getZ() );
                        }
                        ran = true;
                    } else {
                        if ((player.getLocation().getBlockX() != x) || (player.getLocation().getBlockY() != y) || (player.getLocation().getBlockZ() != z)) {
                            player.sendMessage(ChatColor.RED + "Teleport Canceled!");
                            ran = true;
                        } else {
                            player.sendMessage(ChatColor.RED + "Teleporting in:  " + countT + " seconds.");
                        }
                    }
                }
            }
        }, 20, 20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TitanBox.instants, new Runnable() {

            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(myId);
            }
        }, (time + 1)* 20);
    }
    public static String serializeLocation(Location l) {
        return l.getWorld().getName() + ";" + l.getBlockX() + ";" + l.getBlockY() + ";" + l.getBlockZ();
    }
    public static Location deserializeLocation(String l) {
        try {
            World w = Bukkit.getWorld(l.split(";")[0]);
            if (w != null) return new Location(w, Integer.parseInt(l.split(";")[1]), Integer.parseInt(l.split(";")[2]), Integer.parseInt(l.split(";")[3]));
        } catch (NumberFormatException x) {
        }
        return null;
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

    public static Location getRandomLocation(World world, int size)
    {
        Random letsGo = new Random(System.currentTimeMillis());
        double x = letsGo.nextInt(size * 2);
        double y = 300;
        double z = letsGo.nextInt(size * 2);
        x = x - size;
        z = z - size;
        Location location = new Location(world, x, y, z);
        y = world.getHighestBlockYAt(location);
        Location location1 = new Location(world, x, y, z);
        if (location1.clone().add(0, -1, 0).getBlock().getType() == Material.WATER || location1.clone().add(0, -1, 0).getBlock().getType() == Material.LAVA)
        {
            return getRandomLocation(world, size);
        }
        return location1.clone();
    }
    public static List<ItemStack> getByTexture(String Texture) {
        try {
            List<SlimefunItem> items = SlimefunItem.items;
            List<ItemStack> tmp = new ArrayList<ItemStack>();
            if (Texture == null) {
                return null;
            } else if (Texture.equals("")) {
                return null;
            } else {
                Iterator var2 = items.iterator();

                while(var2.hasNext()) {
                    SlimefunItem sfi = (SlimefunItem)var2.next();
                    if (sfi != null && sfi.getItem() != null && sfi.getItem().getType() != null && sfi.getItem().getType() == Material.PLAYER_HEAD) {
                        String SFS = CustomSkull.getTexture(sfi.getItem());
                        if (SFS != null && SFS.equals(Texture)) {
                            tmp.add(sfi.getItem());
                        }
                    }
                }
                if (Texture.equals(SlimefunItemsManager.FORCE_FIELD_YELLOW_S) ||
                        Texture.equals(SlimefunItemsManager.FORCE_FIELD_BLUE_S) ||
                        Texture.equals(SlimefunItemsManager.FORCE_FIELD_GREEN_S))
                {
                    tmp.add(SlimefunItemsManager.FORCE_FIELD_BLUE);
                }
                return tmp;
            }
        } catch (Exception var5) {
            System.out.println(var5.toString());
            return new ArrayList<ItemStack>();
        }
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
    public static boolean isAir(Material material)
    {
        if (material == Material.AIR || material == Material.CAVE_AIR)
        {
            return true;
        }
        return false;
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
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say " + Name + " database is loading. Will retry in: " + ChatColor.WHITE + minutes + "mins " + seconds + "seconds");
                            if (System.currentTimeMillis() >= resetTimer) {
                                try {
                                    String label = (String)getLabel.invoke(null);
                                    this.cancel();
                                } catch (Exception e) {

                                }
                            }
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say " + Name + " database loaded!");
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

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String formatCommas(Long value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(value);
        return numberAsString;
    }

    public static String formatCommas(double value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(value);
        return numberAsString;
    }

    public static String formatTime(long lastping)
    {
        String time = " Seconds";
        lastping = lastping / 1000;
        if (lastping > 60)
        {
            time = " Minutes";
            lastping = lastping / 60;
            if (lastping > 60)
            {
                time = " Hours";
                lastping = lastping / 60;
                if (lastping > 24)
                {
                    time = " Days";
                    lastping = lastping / 24;
                    if (lastping > 30)
                    {
                        time = " Months";
                        lastping = lastping / 24;
                    }
                }
            }
        }
        return lastping + time;
    }

    public static String convertToTimePasted(long lastping)
    {
        long last = System.currentTimeMillis() - lastping;
        String time = " Seconds";
        last = last / 1000;
        if (last > 60)
        {
            time = " Minutes";
            last = last / 60;
            if (last > 60)
            {
                time = " Hours";
                last = last / 60;
            }
        }
        return last + time;
    }

    public static ItemStack getHead(String Texture)
    {
        try
        {
            return CustomSkull.getItem(Texture);
        }catch (Exception e)
        {
            return null;
        }
    }

    public static void placeSkull(Block block, String skulltexture) {
        // maybe one day Bukkit will have a block set method which takes a MaterialData
        try {
            if (block.getLocation().equals(block.getLocation())) {
                if (CustomSkull.getItem(skulltexture) != null) {
                    try {
                        if (block.getType() != Material.PLAYER_HEAD) {
                            block.setType(Material.PLAYER_HEAD);
                        }
                        //Skull skullE = ((Skull) block.getState());
                        //skullE.getData().setData((byte) 0x1);
                        CustomSkull.setSkull(block, skulltexture);


                    } catch (Exception e) {
  //                      MaterialData md = new MaterialData(Material.BOOK);

//                        block.setTypeIdAndData(getMaterialData().getItemTypeId(), getMaterialData().getData(), true);
                    }

                } else {
                    //block.setTypeIdAndData(getMaterialData().getItemTypeId(), getMaterialData().getData(), true);
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    public static String fixBlockTexture(final Location l) {
        try {
            if (BlockStorage.hasBlockInfo(l.getBlock())) {
                String id = BlockStorage.getLocationInfo(l.getBlock().getLocation(), "id");
                if (id != null) {
                    SlimefunItem item = SlimefunItem.getByID(id);
                    Block b = l.getBlock();
                    if (item != null) {
                        if (item.getItem().getType() == Material.PLAYER_HEAD) {
                            b.setType(item.getItem().getType());
                            String texture = CustomSkull.getTexture(item.getItem());
                            if (ChargableBlock.capacitors.contains(id)) {
                                int charge = ChargableBlock.getCharge(b), capacity = ChargableBlock.getMaxCharge(b);
                                if (b.getState() instanceof Skull) {
                                    if (charge < (int) (capacity * 0.25D))
                                        CustomSkullFix_setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzNjFlNTc2YjQ5M2NiZmRmYWUzMjg2NjFjZWRkMWFkZDU1ZmFiNGU1ZWI0MThiOTJjZWJmNjI3NWY4YmI0In19fQ==");
                                    else if (charge < (int) (capacity * 0.5D))
                                        CustomSkullFix_setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA1MzIzMzk0YTdkOTFiZmIzM2RmMDZkOTJiNjNjYjQxNGVmODBmMDU0ZDA0NzM0ZWEwMTVhMjNjNTM5In19fQ==");
                                    else if (charge < (int) (capacity * 0.75D))
                                        CustomSkullFix_setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU4NDQzMmFmNmYzODIxNjcxMjAyNThkMWVlZThjODdjNmU3NWQ5ZTQ3OWU3YjBkNGM3YjZhZDQ4Y2ZlZWYifX19");
                                    else
                                        CustomSkullFix_setSkull(b, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EyNTY5NDE1YzE0ZTMxYzk4ZWM5OTNhMmY5OWU2ZDY0ODQ2ZGIzNjdhMTNiMTk5OTY1YWQ5OWM0MzhjODZjIn19fQ==");
                                }
                            }
                            else
                            {
                                CustomSkullFix_setSkull(b, texture);
                            }
                        } else {
                            b.setType(item.getItem().getType());
                        }
                        return id;
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private static Object CustomSkullFix_createProfile(String texture) throws Exception {
        Class<?> map_class = Class.forName("com.mojang.authlib.properties.PropertyMap");
        Class<?> profile_class = Class.forName("com.mojang.authlib.GameProfile");
        Class<?> property_class = Class.forName("com.mojang.authlib.properties.Property");
        Method property = ReflectionUtils.getMethod(profile_class, "getProperties");
        Constructor<?> profile_constructor = ReflectionUtils.getConstructor(profile_class, new Class[]{UUID.class, String.class});
        Constructor<?>  property_constructor = ReflectionUtils.getConstructor(property_class, new Class[]{String.class, String.class});
        Method insert_property = ReflectionUtils.getMethod(map_class, "put", new Class[]{String.class, property_class});


        if (!CSCoreLib.getLib().getCfg().contains("skulls.uuids." + texture)) {
            CSCoreLib.getLib().getCfg().setValue("skulls.uuids." + texture, UUID.randomUUID().toString());
            CSCoreLib.getLib().getCfg().save();
        }

        Object profile = profile_constructor.newInstance(UUID.fromString(CSCoreLib.getLib().getCfg().getString("skulls.uuids." + texture)), "CSCoreLib");
        Object properties = property.invoke(profile);
        insert_property.invoke(properties, "textures", property_constructor.newInstance("textures", texture));
        return profile;
    }
    public static void CustomSkullFix_setSkull(Block block, String texture) throws Exception {
        Constructor<?> position = ReflectionUtils.getConstructor(ReflectionUtils.getClass(PackageName.NMS, "BlockPosition"), new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        Method tileentity = ReflectionUtils.getClass(PackageName.NMS, "WorldServer").getMethod("getTileEntity", ReflectionUtils.getClass(PackageName.NMS, "BlockPosition"));
        Class<?> profile_class = Class.forName("com.mojang.authlib.GameProfile");
        Method gameprofile = ReflectionUtils.getClass(PackageName.NMS, "TileEntitySkull").getMethod("setGameProfile", profile_class);

        if (!CustomSkullFix_getTexture(block).equals(texture)) {
            if (ReflectionUtils.isVersion(new String[]{"v1_12_", "v1_11_", "v1_10_", "v1_9_"})) {
                if (!block.getType().equals(Material.valueOf("SKULL"))) {
                    throw new IllegalArgumentException("Block is not a Skull");
                }
            } else if (!block.getType().equals(Material.PLAYER_HEAD) && !block.getType().equals(Material.PLAYER_WALL_HEAD)) {
                throw new IllegalArgumentException("Block is not a Skull");
            }

            Object profile = CustomSkullFix_createProfile(texture);
            Object world = ReflectionUtils.getHandle(CraftObject.WORLD, block.getWorld());
            Object tile = null;
            if (ReflectionUtils.getVersion().startsWith("v1_7_")) {
                tile = tileentity.invoke(world, block.getX(), block.getY(), block.getZ());
            } else {
                tile = tileentity.invoke(world, position.newInstance(block.getX(), block.getY(), block.getZ()));
            }

            try {
                if (tile != null) {
                    gameprofile.invoke(tile, profile);
                    block.getState().update(true);
                }
            } catch (NullPointerException var6) {
                System.err.println("Method: " + gameprofile);
                System.err.println("World: " + world);
                System.err.println("Tile Retriever: " + tileentity);
                System.err.println("Tile: " + tile);
                System.err.println("Profile Retriever: " + gameprofile);
                System.err.println("Profile: " + profile);
                var6.printStackTrace();
            }

        }
    }
    public static String CustomSkullFix_getTexture(Block block) throws Exception {
        Constructor<?> position = ReflectionUtils.getConstructor(ReflectionUtils.getClass(PackageName.NMS, "BlockPosition"), new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        Method tileentity = ReflectionUtils.getClass(PackageName.NMS, "WorldServer").getMethod("getTileEntity", ReflectionUtils.getClass(PackageName.NMS, "BlockPosition"));
        Class<?> map_class = Class.forName("com.mojang.authlib.properties.PropertyMap");
        Method map_list = ReflectionUtils.getMethod(map_class, "get", new Class[]{String.class});
        Class<?> profile_class = Class.forName("com.mojang.authlib.GameProfile");
        Class<?> property_class = Class.forName("com.mojang.authlib.properties.Property");
        Method property = ReflectionUtils.getMethod(profile_class, "getProperties");
        Method get_name = ReflectionUtils.getMethod(property_class, "getName");
        Method get_value = ReflectionUtils.getMethod(property_class, "getValue");
        Object world = ReflectionUtils.getHandle(CraftObject.WORLD, block.getWorld());
        Object tile = null;
        if (ReflectionUtils.getVersion().startsWith("v1_7_")) {
            //tile = tileentity.invoke(world, block.getX(), block.getY(), block.getZ());
        } else {
            tile = tileentity.invoke(world, position.newInstance(block.getX(), block.getY(), block.getZ()));
        }

        if (tile == null) {
            return "";
        } else {
            Object profile = null;
            if (ReflectionUtils.isVersion(new String[]{"v1_14_"})) {
                profile = TileEntityTypes.SKULL.a().gameProfile;
                //profile = ReflectionUtils.getFieldValue(ReflectionUtils.getClass(PackageName.NMS, "TileEntitySkull"), "gameProfile");
            } else {
//                profile = getgameprofile.invoke(tile);
            }

            if (profile != null) {
                Collection<?> collection = (Collection)map_list.invoke(property.invoke(profile), "textures");
                Iterator var5 = collection.iterator();

                while(var5.hasNext()) {
                    Object p = var5.next();
                    if (get_name.invoke(p).equals("textures")) {
                        return (String)get_value.invoke(p);
                    }
                }
            }

            return "";
        }
    }
    public static GameProfile getNonPlayerProfile(String skinURL) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinURL).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));


        //GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        //newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + "http://textures.minecraft.net/texture/" + skinURL + "\"}}}")));
        //newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:[{Value:\"" + skinURL + "\"}]}")));
        //return newSkinProfile;
        return profile;
    }

    public static ItemStack addItemsToPlayer(Player player, ItemStack placing)
    {
        placing = placing.clone();
        List<Integer> emptySlos = new ArrayList<Integer>();
        Inventory playersInv = player.getInventory();
        for (int i = 0; i < 36; i++) {
            ItemStack checkItem = playersInv.getItem(i);
            if (isEmpty(checkItem))
            {
                emptySlos.add(i); //this slot is empty lets keep that in mind for later
            }
            else
            {
                checkItem = checkItem.clone();
                if (isItemEqual(checkItem, placing))// is this the same thing
                {
                    if (checkItem.getAmount() < checkItem.getMaxStackSize()) //is there room in this stack for more
                    {
                        int placeAmount = Math.min(checkItem.getMaxStackSize() - checkItem.getAmount(), placing.getAmount()); //how much more can it hold
                        checkItem.setAmount(checkItem.getAmount() + placeAmount); //uppdate the item
                        playersInv.setItem(i, checkItem.clone());//place the item back
                        if (placing.getAmount() - placeAmount <= 0) //is there any left over we need to place
                        {
                            return null; //we are done
                        }
                        placing.setAmount(placing.getAmount() - placeAmount); //some was left, lets keep looking
                    }
                }
            }

        }
        //we finished looking for more room now lets fill in the empty slots
        for (Integer slot: emptySlos)
        {
            int howMuch = Math.min(placing.getMaxStackSize(), placing.getAmount());
            ItemStack itemStack = placing.clone();
            itemStack.setAmount(howMuch);
            playersInv.setItem(slot, itemStack.clone());
            if (placing.getAmount() - howMuch <= 0) //is there any left over we need to place
            {
                return null; //we are done
            }
            placing.setAmount(placing.getAmount() - howMuch); //some was left, lets keep looking
        }

        return placing.clone(); // didn't have enough room, sorry

    }

    public static ItemStack getItemStackFromBlock(Location block)
    {
        return getItemStackFromBlock(block.getBlock());
    }

    public static ItemStack getItemStackFromBlock(Block block)
    {
        ItemStack test = block.getState().getData().toItemStack(1);
        return test.clone();
    }

    public static ItemStack addItemToStorage(UUID owner, Material toInsert, int giveamount)
    {
        return addItemToStorage(owner, new ItemStack(toInsert, giveamount));
    }

    public static ItemStack addItemToStorage(UUID owner, ItemStack toInsert)
    {
        toInsert = toInsert.clone();
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                ItemStack out = stH.insertItem(toInsert);
                if (isEmpty(out))
                {
                    return null;
                }
                if (out.getAmount() < toInsert.getAmount())
                {
                    return out.clone();
                }
            }
        }
        return toInsert.clone();
    }
    public static void checkDeathItem(Player player, PlayerInventory inventory, int slot)
    {
        if (hasDeathItem(player, inventory, slot))
        {
            player.setHealth(15);
            player.setFoodLevel(10);
        }
    }
    public static boolean isDeathItem(ItemStack itemStack)
    {
        if (!Utilities.isEmpty(itemStack))
        {
            if (itemStack.getType() == Material.FEATHER) {
                String name = Utilities.getName(itemStack, false);
                if (name.equals( ChatColor.translateAlternateColorCodes('&', "&6Death Feather"))) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean hasDeathItem(Player player, PlayerInventory inventory, int slot)
    {
        if (isDeathItem(inventory.getItem(slot)))
        {
            return true;
        }
        return false;
    }
    public static void pickupItemToStorage(UUID owner, Location checkSapling, Material mat, int size) {
        try {
            List<Entity> listnear = NPCManager.getNearbyEntities(checkSapling, size);
            for (Entity e : listnear) {
                if (!e.isDead()) {
                    if (e.getType() == EntityType.DROPPED_ITEM) {
                        ItemStack dropped = ((Item) e).getItemStack().clone();
                        if (mat == null)
                        {
                            ItemStack pickup = addItemToStorage(owner, dropped);
                            if (pickup == null)
                            {
                                e.remove();
                            }
                            else {
                                ((Item) e).setItemStack(pickup);
                            }
                        }
                        else {
                            if (dropped.getType() == mat) {
                                addItemToStorage(owner, dropped);
                                e.remove();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
        finally {

        }
    }
    public static ItemStack getItemFromStorage(UUID owner, Material typeBucket) {
        return getItemFromStorage(owner, new ItemStack(typeBucket, 1));
    }

    public static ItemStack getItemFromStorage(UUID owner, ItemStack typeBucket) {
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!isEmpty(view))
                    {
                        if (isItemEqual(view, typeBucket))
                        {
                            ItemStack getIt = stH.getItem(i, typeBucket.getAmount());
                            if (!isEmpty(getIt))
                            {
                                return  getIt;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    public static ForceFieldManager getForceField(Location ofBlock) {
        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
        if (ffH != null)
        {
            return ffH;
        }
        return null;
    }
    public static boolean isProtected(Cancellable event, Location ofBlock, Player player) {
        if (player == null) return false;
        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
        if (ffH != null)
        {
            if (!ffH.hasRights(player.getUniqueId()))
            {
                return true;
            }
        }
        return false;
    }
    public static long getItemCountInStorage(UUID owner, ItemStack typeBucket) {
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!isEmpty(view))
                    {
                        if (isItemEqual(view, typeBucket))
                        {
                            return stH.getStorageCount(i);
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static ItemStack getItemByPassPower(UUID owner, ItemStack typeBucket, int amount) {
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!isEmpty(view))
                    {
                        if (isItemEqual(view, typeBucket))
                        {
                            ItemStack getIt = stH.getItem(i,amount, true);
                            if (!isEmpty(getIt))
                            {
                                return  getIt;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean hasItemInStorage(UUID owner, Material typeBucket) {
        return hasItemInStorage(owner, new ItemStack(typeBucket, 1));
    }

    public static boolean hasItemInStorage(UUID owner, ItemStack typeBucket) {
        return hasItemInStorage(owner, typeBucket, true);
    }

    public static boolean hasItemInStorage(UUID owner, ItemStack typeBucket, boolean useItem) {
        boolean foundbucket = false;
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for(int i =0;i <stH.getSize(); i++)
                {
                    ItemStack view = stH.viewSlot(i);
                    if (!isEmpty(view))
                    {
                        if (isItemEqual(view, typeBucket))
                        {
                            if (useItem) {
                                ItemStack getIt = stH.getItem(i, 1);
                                if (!isEmpty(getIt)) {
                                    foundbucket = true;
                                    break;
                                }
                            }
                            else
                            {
                                foundbucket = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return foundbucket;
    }

    /**
     * Check if the given block contains a vanilla inventory,
     *
     * @param target the block to check
     * @return true if this block holds a vanilla inventory; false otherwise
     */
    public static boolean isVanillaInventory(Block target) {
        switch (target.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
            case DISPENSER:
            case HOPPER:
            case DROPPER:
            case FURNACE:
            case BREWING_STAND:
            //case BURNING_FURNACE:
                BlockStorage storage = BlockStorage.getStorage(target.getWorld());
                if (storage != null) {
                    if (storage.hasInventory(target.getLocation())) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }
    public static HashMap<Integer, Integer> getGUISlotFromRealSlot(Block target) {
        HashMap<Integer, Integer> trans = new HashMap<Integer, Integer>();
        switch (target.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
                Inventory inventory = Utilities.getVanillaInventoryFor(target);
                if (inventory.getHolder() instanceof DoubleChest)
                {
                    for (int i = 0; i < 54; i++) {
                        trans.put(i, i);
                    }
                }
                else {
                    for (int i = 0; i < 27; i++) {
                        trans.put(i, i);
                    }
                }
                return trans;
            case DISPENSER:
                // 12 13 14
                // 21 22 23
                // 30 31 32
                trans.put(0, 12);
                trans.put(1, 13);
                trans.put(2, 14);
                trans.put(3, 21);
                trans.put(4, 22);
                trans.put(5, 23);
                trans.put(6, 30);
                trans.put(7, 31);
                trans.put(8, 32);
                return trans;
            case HOPPER:
            case DROPPER:
                trans.put(0, 20);
                trans.put(1, 21);
                trans.put(2, 22);
                trans.put(3, 23);
                trans.put(4, 24);
                return trans;
            case FURNACE:
                trans.put(0, 12);
                trans.put(1, 30);
                trans.put(2, 23);
                return trans;
            case BREWING_STAND:
                trans.put(0, 30);//10 13 30 31 32
                trans.put(1, 31);
                trans.put(2, 32);
                trans.put(3, 10);
                trans.put(4, 13);
                return trans;
            default:
                return null;
        }
    }
    public static HashMap<Integer, Integer> getRealSlotFromGUISlot(Block target) {
        HashMap<Integer, Integer> trans = new HashMap<Integer, Integer>();
        switch (target.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
                Inventory inventory = Utilities.getVanillaInventoryFor(target);
                if (inventory.getHolder() instanceof DoubleChest)
                {
                    for (int i = 0; i < 54; i++) {
                        trans.put(i, i);
                    }
                }
                else {
                    for (int i = 0; i < 27; i++) {
                        trans.put(i, i);
                    }
                }
                return trans;
            case DISPENSER:
                // 12 13 14
                // 21 22 23
                // 30 31 32
                trans.put(12, 0);
                trans.put(13, 1);
                trans.put(14, 2);
                trans.put(21, 3);
                trans.put(22, 4);
                trans.put(23, 5);
                trans.put(30, 6);
                trans.put(31, 7);
                trans.put(32, 8);
                return trans;
            case HOPPER:
            case DROPPER:
                trans.put(20, 0);
                trans.put(21, 1);
                trans.put(22, 2);
                trans.put(23, 3);
                trans.put(24, 4);
                return trans;
            case FURNACE:
                trans.put(12, 0);
                trans.put(30, 1);
                trans.put(23, 2);
                return trans;
            case BREWING_STAND:
                trans.put(30, 0); //10 13 30 31 32
                trans.put(31, 1);
                trans.put(32, 2);
                trans.put(13, 3);
                trans.put(10, 4);
                return trans;
            default:
                return null;
        }
    }
    public static boolean isInventory(Block clicked)
    {
        BlockStorage storage =  BlockStorage.getStorage(clicked.getLocation().getWorld());

        if (storage.hasUniversalInventory(clicked))
        {
            return true;

        } else if (storage.hasInventory(clicked.getLocation()))
        {
            return true;

        } else if (isVanillaInventory(clicked))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get the vanilla inventory for the given block.
     *
     * @param target the block containing the target inventory
     * @return the block's inventory, or null if the block does not have one
     */
    public static Inventory getVanillaInventoryFor(Block target) {
        Chest c;
        switch (target.getType()) {
            case TRAPPED_CHEST:
            case CHEST:
                c = (Chest) target.getState();
                if (c.getInventory().getHolder() instanceof DoubleChest) {
                    DoubleChest dc = (DoubleChest) c.getInventory().getHolder();
                    return dc.getInventory();
                } else {
                    return c.getBlockInventory();
                }
            case DISPENSER:
            case HOPPER:
            case DROPPER:
            case FURNACE:
            case BREWING_STAND:
            //case BURNING_FURNACE:
                return ((InventoryHolder) target.getState()).getInventory();
            // any other vanilla inventory types ?
            default:
                return null;
        }
    }

    public static ItemStack changeName(ItemStack toAdd, String Name) {
        ItemMeta IM = toAdd.getItemMeta();
        IM.setDisplayName(Name);
        toAdd.setItemMeta(IM);
        return toAdd.clone();
    }
    public static boolean hasLore(ItemStack toAdd)
    {
        ItemMeta ITM = toAdd.getItemMeta();
        if (ITM != null) {
            if (ITM.hasLore()) {
                return true;
            }
        }

        return false;
    }
    public static List<String> getLore(ItemStack toAdd)
    {
        ItemMeta ITM = getItemMeta(toAdd);

        if (ITM.hasLore()) {
            return ITM.getLore();
        }

        return null;
    }
    public static int getLoreSize(ItemStack toAdd)
    {
        ItemMeta ITM = getItemMeta(toAdd);

        if (ITM.hasLore()) {
            return ITM.getLore().size();
        }

        return -1;
    }

    public static boolean isEmpty(ItemStack toCheck)
    {
        if (toCheck == null)
        {
            return  true;
        }
        if (toCheck.getType().equals(Material.AIR))
        {
            return  true;
        }
        if (toCheck.getAmount() < 1)
        {
            return  true;
        }
        return false;
    }

    public static ItemStack removeLore(ItemStack toAdd, int line)
    {
        ItemMeta ITM = getItemMeta(toAdd);
        if (ITM.hasLore()) {
            List<String> lore = ITM.getLore();
            lore.remove(line);
            ITM.setLore(lore);
            toAdd.setItemMeta(ITM.clone());
            return toAdd;
        }

        return toAdd.clone();
    }

    public static ItemStack addLore(boolean clear, ItemStack toAdd, List<String> lore)
    {

        ItemMeta ITM = getItemMeta(toAdd);

            if (!clear)
            {
                if (ITM.hasLore())
                {
                    List<String> lore2 = new ArrayList<String>();
                    lore2.addAll(ITM.getLore());
                    lore2.addAll(lore);
                    lore.clear();
                    lore = lore2;
                }
            }
            ITM.setLore(lore);
            toAdd.setItemMeta(ITM.clone());
            return toAdd;
    }
    public static String fixCapitalization(String Namespace)
    {
        if (Namespace.length() > 0) {
            String fixing = Namespace.replace("_", " ").toLowerCase();
            return WordUtils.capitalize(fixing);
        }
        return "";
    }
    public  static void checkSFHelmet(Player player, ItemStack newHelment)
    {

        try {
            if (player == null)
            {
                return;
            }
            if (!player.isOnline())
            {
                return;
            }
            PlayerManager playerManager = PlayerManager.getPlayer(player);
            if (newHelment != null) {
                if (newHelment.getItemMeta() != null) {
                    if (newHelment.getType() == Material.LEATHER_HELMET && newHelment.getItemMeta().getDisplayName().equals(SlimefunItemsManager.X_RAY_HELMET.getItemMeta().getDisplayName())) {
                        playerManager.startXRay(true, false);
                        return;
                    }
                    if (newHelment.getType() == Material.LEATHER_HELMET && newHelment.getItemMeta().getDisplayName().equals(SlimefunItemsManager.CAVE_VIEW_HELMET.getItemMeta().getDisplayName())) {
                        playerManager.startXRay(false, true);
                        return;
                    }
                }
            }
            playerManager.stopXRay();
        } catch (Exception e) {

        }
    }
    public static void scanInventory(Player p) {
        Inventory inventory = p.getInventory();
        scanInventory(inventory);
    }
    public static void pickAHand(Player player, BukkitRunnable bukkitRunnable)
    {
        player.closeInventory();
        BukkitTask bukkitTask = new BukkitRunnable() {
            private int time = 5;

            @Override
            public void run() {
                if (time > 0)
                    player.sendMessage(ChatColor.RED + "Hold Item In Main Hand in... " + time + " Seconds");
                time--;
                if (time == 0) {
                    if (!Utilities.isEmpty(player.getInventory().getItemInMainHand())) {
                        bukkitRunnable.runTask(TitanBox.instants);
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "Canceled.");
                    }
                }
            }
        }.runTaskTimer(TitanBox.instants, 20, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                bukkitTask.cancel();
            }
        }.runTaskLater(TitanBox.instants, 11 * 20);
    }
    public static void scanInventory(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (ConfigManager.isRemoveUnnecessaryDamageTags()) {
                if (Utilities.hasNBTTag(inventory.getItem(i), "Damage")) {
                    if (!Utilities.isWeapon(inventory.getItem(i)) && !Utilities.isTool(inventory.getItem(i)) && !Utilities.isArmor(inventory.getItem(i))) {
                        ItemStack fixed = Utilities.removeNBTTag(inventory.getItem(i), "Damage");
                        inventory.setItem(i, fixed);
                    }
                }
            }

            if (checkAlterProbe(inventory.getItem(i)))
            {
                inventory.setItem(i, null);
            }
        }
    }
    public static boolean checkAlterProbe(ItemStack itemStack )
    {
        try {
            String name = Utilities.getName(itemStack, false);
            if (name.contains(ChatColor.DARK_PURPLE + "" + ChatColor.LIGHT_PURPLE + "ALTAR " + ChatColor.DARK_AQUA + "Probe - ")) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }
    public static boolean hasNBTTag(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = getNBTTag(itemStack);
        if (nbtTagCompound != null) {
            if (nbtTagCompound.hasKey(key)) {
                return true;
            }
        }
        return false;
    }
    public static ItemStack removeNBTTag(ItemStack itemStack, String key)
    {
        NBTTagCompound nbtTagCompound = getNBTTag(itemStack);
        if (nbtTagCompound != null) {
            if (nbtTagCompound.hasKey(key)) {
                if (nbtTagCompound.getKeys().size() == 1) {
                    return clearNBTTag(itemStack);
                } else {
                    nbtTagCompound.set(key, null);
                    return setNBTTag(itemStack, nbtTagCompound);
                }
            }
        }
        return itemStack;
    }
    public static ItemStack clearNBTTag(ItemStack itemStack)
    {
        try {
            net.minecraft.server.v1_14_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
            itemStack1.setTag(null);
            return CraftItemStack.asBukkitCopy(itemStack1);
        }
        catch (Exception E)
        {
            E.printStackTrace();
            return null;
        }
    }
    public static ItemStack setNBTTag(ItemStack itemStack, NBTTagCompound nbtTagCompound)
    {
        try {
                net.minecraft.server.v1_14_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
                itemStack1.setTag(nbtTagCompound);
                return CraftItemStack.asBukkitCopy(itemStack1);
        }
        catch (Exception E)
        {
            E.printStackTrace();
            return null;
        }
    }
    public static NBTTagCompound getNBTTag(ItemStack itemStack)
    {
        try {
            net.minecraft.server.v1_14_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
            if (itemStack1.getTag() == null)
            {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                return nbtTagCompound;
            }
            return itemStack1.getTag();
        }
        catch (Exception E)
        {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            return nbtTagCompound;
        }
    }
    public static ItemStack addLore(ItemStack toAdd, List<String> lore)
    {
        return addLore(false, toAdd, lore);
    }

    public static ItemStack addLore(boolean clear, ItemStack toAdd, String... lores)
    {
        List<String> lore = new ArrayList<String>();
        for (String l : lores) {
            lore.add(l);
        }
        toAdd = addLore(clear, toAdd, lore);
        return toAdd.clone();
    }

    public static ItemStack addLore(ItemStack toAdd, String... lores)
    {
        return addLore(false, toAdd, lores);
    }

    public static ItemStack clearLore(ItemStack toAdd)
    {
        List<String> lore = new ArrayList<String>();
        ItemMeta ITM = getItemMeta(toAdd);

        ITM.setLore(lore);
        toAdd.setItemMeta(ITM.clone());
        return toAdd;
    }

    public static ItemMeta getItemMeta(ItemStack toAdd) {
        if (toAdd.hasItemMeta())
        {
            return toAdd.getItemMeta();
        }
        return Bukkit.getItemFactory().getItemMeta(toAdd.getType());

    }

    public static ItemStack clearEnchanents(ItemStack toAdd)
    {
        Set<Enchantment> all = toAdd.getEnchantments().keySet();
        for(Enchantment enc: all)
        {
            toAdd.removeEnchantment(enc);
        }
        return toAdd;
    }

    public static boolean equalsLore(List<String> lore, List<String> lore2) {
        String string1 = "";
        String string2 = "";
        Iterator var4 = lore.iterator();

        String string;
        while(var4.hasNext()) {
            string = (String)var4.next();
            string1 = string1 + "-NEW LINE-" + string;
        }

        var4 = lore2.iterator();

        while(var4.hasNext()) {
            string = (String)var4.next();
            string2 = string2 + "-NEW LINE-" + string;
        }

        return string1.equals(string2);
    }

    public static boolean equalsEnchants(Map<Enchantment, Integer> item1, Map<Enchantment, Integer> item2)
    {
        if (item1 == null && item2 == null) return true;
        if (item1 != null && item2 == null) return false;
        if (item2 != null && item1 == null) return false;
        if (item1.size() != item2.size()) return false;
        for(Enchantment e: item1.keySet())
        {
            if (!item2.containsKey(e)) return false;
            if (item1.get(e) != item2.get(e)) return false;
        }
        return true;
    }
    public static boolean isItemEqual(ItemStack item, ItemStack SFitem)
    {
        return isItemEqual(item, SFitem, true);
    }
    public static boolean isItemEqual(ItemStack item, ItemStack SFitem, boolean checkEnchants) {
        if (item == null) return SFitem == null;
        if (SFitem == null) return false;
        if (item.getType() == SFitem.getType()) {//&& item.getAmount() >= SFitem.getAmount()
            if (isWeapon(item) || isArmor(item)) {
                if (item.getData().getData() != SFitem.getData().getData()) {
                    if (!(SFitem.getDurability() == item.getData().getData() && SFitem.getData().getData() == item.getDurability()))
                        return false;
                }
            }
            if (checkEnchants) {
                if (!equalsEnchants(item.getEnchantments(), SFitem.getEnchantments())) return false;
            }
            if (item.hasItemMeta() && SFitem.hasItemMeta()) {
                ItemMeta a = item.getItemMeta();
                ItemMeta b = SFitem.getItemMeta();
                if (a instanceof BannerMeta && b instanceof BannerMeta)
                {
                    if(!((BannerMeta)a).getPatterns().equals(((BannerMeta)b).getPatterns())) return false;

                    if (((BannerMeta)a).getBaseColor() == null && ((BannerMeta)b).getBaseColor() != null) return false;
                    if (((BannerMeta)a).getBaseColor() != null && ((BannerMeta)b).getBaseColor() == null) return false;
                    if (((BannerMeta)a).getBaseColor() != null && ((BannerMeta)b).getBaseColor() != null) {
                        if (!((BannerMeta) a).getBaseColor().equals(((BannerMeta) b).getBaseColor())) return false;
                    }
                }
                if (a instanceof EnchantmentStorageMeta && b instanceof EnchantmentStorageMeta)
                {
                    if (((EnchantmentStorageMeta)a).getEnchants() !=null && ((EnchantmentStorageMeta)b).getEnchants() ==null) return false;
                    if (((EnchantmentStorageMeta)a).getEnchants() ==null && ((EnchantmentStorageMeta)b).getEnchants() !=null) return false;
                    if (((EnchantmentStorageMeta)a).getEnchants() !=null && ((EnchantmentStorageMeta)b).getEnchants() !=null) {
                        if (!((EnchantmentStorageMeta) a).getEnchants().equals(((EnchantmentStorageMeta) b).getEnchants()))
                            return false;
                    }
                }
                if (a instanceof SpawnEggMeta && b instanceof SpawnEggMeta)
                {
                    if (((SpawnEggMeta)a).getSpawnedType() !=null && ((SpawnEggMeta)b).getSpawnedType() ==null) return false;
                    if (((SpawnEggMeta)a).getSpawnedType() ==null && ((SpawnEggMeta)b).getSpawnedType() !=null) return false;
                    if (((SpawnEggMeta)a).getSpawnedType() !=null && ((SpawnEggMeta)b).getSpawnedType() !=null) {
                        if (!((SpawnEggMeta) a).getSpawnedType().equals(((SpawnEggMeta) b).getSpawnedType()))
                            return false;
                    }
                }
                if (a instanceof SkullMeta && b instanceof SkullMeta)
                {
                    if (CustomSkull.getTexture(item) != null && CustomSkull.getTexture(SFitem) != null) {
                        if (!CustomSkull.getTexture(item).equals(CustomSkull.getTexture(SFitem))) return false;
                    }
                }
                if (a instanceof PotionMeta && b instanceof PotionMeta)
                {
                    if(!((PotionMeta)a).getCustomEffects().equals(((PotionMeta)b).getCustomEffects())) return false;
                    if(!((PotionMeta)a).getBasePotionData().equals(((PotionMeta)b).getBasePotionData())) return false;
                    if(!((PotionMeta)a).getBasePotionData().getType().equals(((PotionMeta)b).getBasePotionData().getType())) return false;
                    if (((PotionMeta)a).getColor() !=null && ((PotionMeta)b).getColor() ==null) return false;
                    if (((PotionMeta)a).getColor() ==null && ((PotionMeta)b).getColor() !=null) return false;
                    if (((PotionMeta)a).getColor() !=null && ((PotionMeta)b).getColor() !=null) {
                        if (!((PotionMeta) a).getColor().equals(((PotionMeta) b).getColor())) return false;
                    }
                }
                if (a instanceof BookMeta && b instanceof BookMeta)
                {
                    if(!((BookMeta)a).getAuthor().equals(((BookMeta)b).getAuthor())) return false;
                    if(!((BookMeta)a).getGeneration().equals(((BookMeta)b).getGeneration())) return false;
                    if(!((BookMeta)a).getPages().equals(((BookMeta)b).getPages())) return false;
                    if(!((BookMeta)a).getTitle().equals(((BookMeta)b).getTitle())) return false;
                }
                if (a instanceof LeatherArmorMeta && b instanceof LeatherArmorMeta)
                {
                    if(!((LeatherArmorMeta)a).getColor().equals(((LeatherArmorMeta)b).getColor())) return false;
                }
                if (a instanceof FireworkMeta && b instanceof FireworkMeta)
                {
                    if(!((FireworkMeta)a).getEffects().equals(((FireworkMeta)b).getEffects())) return false;
                    if(((FireworkMeta)a).getPower() != ((FireworkMeta)b).getPower()) return false;
                }
                if (a instanceof KnowledgeBookMeta && b instanceof KnowledgeBookMeta)
                {
                    //return false;
                }
                if (a instanceof org.bukkit.inventory.meta.EnchantmentStorageMeta && b instanceof EnchantmentStorageMeta)
                {
                    if (((EnchantmentStorageMeta)a).getStoredEnchants().size() != ((EnchantmentStorageMeta)b).getStoredEnchants().size()) return false;
                    Map<Enchantment, Integer> aMap = ((EnchantmentStorageMeta)a).getStoredEnchants();
                    Map<Enchantment, Integer> bMap = ((EnchantmentStorageMeta)b).getStoredEnchants();
                    for(Enchantment enchantment: aMap.keySet())
                    {
                        if (!bMap.containsKey(enchantment)) return false;
                        if ((bMap.get(enchantment) != aMap.get(enchantment))) return false;
                    }
                }
                if (item.getItemMeta().hasDisplayName() && SFitem.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().equals(SFitem.getItemMeta().getDisplayName())) {
                        if (item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore()) {
                            return false;
                        }
                        if (item.getItemMeta().hasLore() && SFitem.getItemMeta().hasLore()) {
                            return equalsLore(item.getItemMeta().getLore(), SFitem.getItemMeta().getLore());
                        }
                        else return !item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore();
                    }
                    else return false;
                }
                else if (!item.getItemMeta().hasDisplayName() && !SFitem.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore()) {
                        return false;
                    }
                    if (item.getItemMeta().hasLore() && SFitem.getItemMeta().hasLore()) {
                        return equalsLore(item.getItemMeta().getLore(), SFitem.getItemMeta().getLore());
                    }
                    else return !item.getItemMeta().hasLore() && !SFitem.getItemMeta().hasLore();

                }
                else return false;
            }
            else return !item.hasItemMeta() && !SFitem.hasItemMeta();
        }
        else return false;
    }

    public static boolean isArmor(ItemStack mat)
    {
        return isArmor(mat.getType());
    }

    public static boolean isArmor(Material mat)
    {
        switch (mat)
        {
            case DIAMOND_CHESTPLATE:LATE:
                return true;
            case CHAINMAIL_CHESTPLATE:
                return true;
            case GOLDEN_CHESTPLATE:
                return true;
            case IRON_CHESTPLATE:
                return true;
            case LEATHER_CHESTPLATE:
                return true;
            case DIAMOND_HELMET:
                return true;
            case LEATHER_HELMET:
                return true;
            case IRON_HELMET:
                return true;
            case CHAINMAIL_HELMET:
                return true;
            case GOLDEN_HELMET:
                return true;
            case DIAMOND_LEGGINGS:
                return true;
            case CHAINMAIL_LEGGINGS:
                return true;
            case GOLDEN_LEGGINGS:
                return true;
            case IRON_LEGGINGS:
                return true;
            case LEATHER_LEGGINGS:
                return true;
            case DIAMOND_BOOTS:
                return true;
            case CHAINMAIL_BOOTS:
                return true;
            case GOLDEN_BOOTS:
                return true;
            case IRON_BOOTS:
                return true;
            case LEATHER_BOOTS:
                return true;
            case ELYTRA:
                return true;
        }
        return false;
    }

    public static boolean isWeapon(ItemStack mat)
    {
        return isWeapon(mat.getType());
    }

    public static boolean hasCustomName(ItemStack mat)
    {
        if (mat.hasItemMeta())
        {
            if (mat.getItemMeta().hasDisplayName())
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeapon(Material mat)
    {
        switch (mat)
        {
            case DIAMOND_SWORD:
                return true;
            case GOLDEN_SWORD:
                return true;
            case IRON_SWORD:
                return true;
            case STONE_SWORD:
                return true;
            case WOODEN_SWORD:
                return true;
            case DIAMOND_AXE:
                return true;
            case GOLDEN_AXE:
                return true;
            case IRON_AXE:
                return true;
            case STONE_AXE:
                return true;
            case WOODEN_AXE:
                return true;
            case SHIELD:
                return true;
        }
        return false;
    }
    public static String getSpawnerType(ItemStack itemStack)
    {
        if (!Utilities.isEmpty(itemStack)) {
            if (Utilities.getName(itemStack, false).equals(ChatColor.translateAlternateColorCodes('&', "&bReinforced Spawner"))) {
                if (!Utilities.isItemEqual(itemStack, SlimefunItems.REPAIRED_SPAWNER)) {
                    List<String> Lore = Utilities.getLore(itemStack);
                    for (int i = 0; i < Lore.size(); i++) {
                        if (ChatColor.stripColor(Lore.get(i)).startsWith("Type: ")) {
                            String type = ChatColor.stripColor(Lore.get(i)).replace("Type: ", "");
                            type = type.replace(" ", "_");
                            return type.toLowerCase();
                        }
                    }
                }
            }
        }
        return  null;
    }
    public static boolean isSpawnEgg(ItemStack itemStack)
    {
        if (!Utilities.isEmpty(itemStack)) {
            if (itemStack.getType().toString().toUpperCase().contains("_SPAWN_EGG")) {
                return true;
            }
        }
        return false;
    }
    public static ItemStack getSpawner(String entityType)
    {
        String FriendlyType = WordUtils.capitalize(entityType.replace("_", " ").toLowerCase());
        ItemStack spawner = SlimefunItemsManager.REPAIRED_SPAWNER_BLANK.clone();
        ItemMeta spawnerItemMeta = spawner.getItemMeta();
        List<String> Lore = spawnerItemMeta.getLore();
        for (int i = 0; i < Lore.size(); i++) {
            if (ChatColor.stripColor(Lore.get(i)).startsWith("Type: ")) {
                Lore.set(i, ChatColor.GRAY + "Type: " + ChatColor.AQUA + FriendlyType);
            }
        }
        spawnerItemMeta.setLore(Lore);
        spawner.setItemMeta(spawnerItemMeta);
        return spawner.clone();
    }
    public static ItemStack getSpawner(ItemStack itemStackEgg)
    {
        if (!Utilities.isEmpty(itemStackEgg)) {
            if (Utilities.isSpawnEgg(itemStackEgg))
            {
                String RawType = itemStackEgg.getType().name().replace("_SPAWN_EGG", "");
                EntityType entityType = EntityType.fromName(RawType);
                String FriendlyType = WordUtils.capitalize(entityType.name().replace("_", " ").toLowerCase());
                ItemStack spawner = SlimefunItemsManager.REPAIRED_SPAWNER_BLANK.clone();
                ItemMeta spawnerItemMeta = spawner.getItemMeta();
                List<String> Lore = spawnerItemMeta.getLore();
                for (int i = 0; i < Lore.size(); i++) {
                    if (ChatColor.stripColor(Lore.get(i)).startsWith("Type: ")) {
                        Lore.set(i, ChatColor.GRAY + "Type: " + ChatColor.AQUA + FriendlyType);
                    }
                }
                spawnerItemMeta.setLore(Lore);
                spawner.setItemMeta(spawnerItemMeta);
                return spawner.clone();
            }
        }
        return null;
    }
    public static void fixSpawnerPlace(Block block, EntityType type)
    {
        CreatureSpawner cs = ((CreatureSpawner) block.getState());
        cs.setSpawnedType(type);
        cs.update(true, false);
    }
    public static boolean saveDeathItem(Player player, ItemStack BrokenItem)
    {
        if (player == null) return false;
        PlayerManager playerManager = PlayerManager.getPlayer(player);
        if (playerManager == null) return false;
        if (!playerManager.hasTitanDeathRecovery()) return false;
        Location b = playerManager.getTitanDeathRecovery();
        int freeslots = 0;
        boolean placed = false;
        for (int slot: ItemRecovery.instance.getInputSlots()) {
            ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);
            if (Utilities.isEmpty(item))
            {
                if (!isItemEqual(SlimefunItems.SOULBOUND_AXE, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_BOOTS, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_BOW, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_CHESTPLATE, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_ELYTRA, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_HELMET, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_HOE, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_LEGGINGS, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_PICKAXE, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_SHOVEL, BrokenItem, false) &&
                        !isItemEqual(SlimefunItems.SOULBOUND_SWORD, BrokenItem, false)) {
                    if (!placed) {
                        BlockStorage.getInventory(b).replaceExistingItem(slot, BrokenItem);
                        placed = true;
                    } else {
                        freeslots++;
                    }
                }
            }
        }
        if (placed)
        {
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean hasBrokenRecovery(Player player)
    {
        if (player == null) return false;
        PlayerManager playerManager = PlayerManager.getPlayer(player);
        if (playerManager == null) return false;
        return playerManager.hasTitanDeathRecovery();
    }
    public static void saveBrokenItem(Player player, ItemStack BrokenItem) {
        if (player == null) return;
        PlayerManager playerManager = PlayerManager.getPlayer(player);
        if (playerManager == null) return;
        if (!playerManager.hasTitanItemRecovery()) return;
        Location b = playerManager.getTitanItemRecovery();
        int freeslots = 0;
        boolean placed = false;
        for (int slot: ItemRecovery.instance.getInputSlots()) {
            ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);
            if (Utilities.isEmpty(item))
            {
                if (!placed) {
                    BlockStorage.getInventory(b).replaceExistingItem(slot, BrokenItem);
                    placed = true;
                }
                else
                {
                    freeslots++;
                }
            }
        }
        if (placed)
        {
            player.sendMessage(ChatColor.WHITE + "" + freeslots + "" + ChatColor.GREEN + " Free Spaces Left, Your Broken Item Was Saved!");
        }
        else {
            player.sendMessage(ChatColor.RED + "No Free Space, Your Broken Item Was NOT Saved!");
        }
    }
    public static boolean isTool(ItemStack mat)
    {
        return isTool(mat.getType());
    }

    public static boolean isTool(Material mat)
    {
        switch (mat)
        {
            case DIAMOND_PICKAXE:
            return true;
            case GOLDEN_PICKAXE:
                return true;
            case IRON_PICKAXE:
                return true;
            case STONE_PICKAXE:
                return true;
            case WOODEN_PICKAXE:
                return true;
            case DIAMOND_AXE:
                return true;
            case GOLDEN_AXE:
                return true;
            case IRON_AXE:
                return true;
            case STONE_AXE:
                return true;
            case WOODEN_AXE:
                return true;
            case DIAMOND_SHOVEL:
                return true;
            case STONE_SHOVEL:
                return true;
            case GOLDEN_SHOVEL:
                return true;
            case IRON_SHOVEL:
                return true;
            case WOODEN_SHOVEL:
                return true;
            case DIAMOND_HOE:
                return true;
            case GOLDEN_HOE:
                return true;
            case IRON_HOE:
                return true;
            case STONE_HOE:
                return true;
            case WOODEN_HOE:
                return true;
            case SHEARS:
                return true;
            case FLINT_AND_STEEL:
                return true;
            case FISHING_ROD:
                return true;
        }
        return false;
    }

    public static ItemStack getSkull(String texture)
    {
        try {
            ItemStack placeMe = CustomSkull.getItem(texture).clone();
            return placeMe;
        }
        catch (Exception e)
        {

        }
        return  null;
    }

    public static String getName(ItemStack toName)
    {
        return getName(toName, true);
    }

    public static String getName(ItemStack toName, boolean stripcolor)
    {
        String name = toName.getType().name();
        if (toName.hasItemMeta())
        {
            if (toName.getItemMeta().hasDisplayName())
            {
                String test = toName.getItemMeta().getDisplayName();
                if (stripcolor) test = ChatColor.stripColor(test);
                if (test.length() > 0)
                {
                    return test;
                }

            }
        }
        return name;
    }

    public static void fenceMachinMaker(Block block, Location location, BlockFace targetFace, String skullTexture) {
        if (targetFace == BlockFace.UP || targetFace == BlockFace.DOWN)
        {
            targetFace = BlockFace.NORTH;
        }
        BlockFace finalTargetFace = targetFace;
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    block.setType(Material.PLAYER_HEAD);
                    CustomSkull.setSkull(block, skullTexture);
                    Block blockat = block.getWorld().getBlockAt(location);
                    //((Rotatable)block.getState().getBlockData()).setRotation(targetFace);
                    BlockState blockState = blockat.getState();
                    Rotatable rotatable = (Rotatable) blockState.getBlockData();
                    rotatable.setRotation(finalTargetFace.getOppositeFace());
                    block.setBlockData(rotatable);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(TitanBox.instants, 20);
    }
}

package com.firesoftitan.play.titanbox.managers;


import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XRayManager {
    private Location loc;
    private boolean xray = false;
    private boolean caves = false;
    private long lastupDated;
    private List<Chunk> toRefresh = new ArrayList<Chunk>();
    private boolean commandUsed = true;
    public static boolean spawnProtection = true;
    public static int updateTime = 1;
    public static int spawnProtectedArea = 500;
    public static int chunksToLoad = 5;
    public static int updatedistance = 10;
    public static List<org.bukkit.Material> keepList = new ArrayList<org.bukkit.Material>();
    public static boolean hideTransparent = false;
    private static Config config = new Config("plugins" + File.separator + "TitanBox" + File.separator  + "xray_config.yml");

    public static void loadConfig()
    {
        XRayManager.config.reload();

        keepList.add(org.bukkit.Material.COAL_ORE);
        keepList.add(org.bukkit.Material.DIAMOND_ORE);
        keepList.add(org.bukkit.Material.EMERALD_ORE);
        keepList.add(org.bukkit.Material.GOLD_ORE);
        keepList.add(org.bukkit.Material.IRON_ORE);
        keepList.add(org.bukkit.Material.LAPIS_ORE);
        keepList.add(org.bukkit.Material.REDSTONE_ORE);
        //keepList.add(Material.LAVA);
        //keepList.add(Material.LAV);
        //keepList.add(Material.WATER);
        keepList.add(org.bukkit.Material.SPAWNER);
        //keepList.add(Material.STATIONARY_WATER);
        keepList.add(org.bukkit.Material.BEDROCK);
        /*keepList.add(Material.CHEST);
        keepList.add(Material.SILVER_SHULKER_BOX);
        keepList.add(Material.BLACK_SHULKER_BOX);
        keepList.add(Material.BLUE_SHULKER_BOX);
        keepList.add(Material.BROWN_SHULKER_BOX);
        keepList.add(Material.CYAN_SHULKER_BOX);
        keepList.add(Material.GRAY_SHULKER_BOX);
        keepList.add(Material.GREEN_SHULKER_BOX);
        keepList.add(Material.LIGHT_BLUE_SHULKER_BOX);
        keepList.add(Material.LIME_SHULKER_BOX);
        keepList.add(Material.MAGENTA_SHULKER_BOX);
        keepList.add(Material.ORANGE_SHULKER_BOX);
        keepList.add(Material.PINK_SHULKER_BOX);
        keepList.add(Material.PURPLE_SHULKER_BOX);
        keepList.add(Material.RED_SHULKER_BOX);
        keepList.add(Material.WHITE_SHULKER_BOX);
        keepList.add(Material.YELLOW_SHULKER_BOX);
        keepList.add(Material.ENDER_CHEST);*/
        config.reload();
        if (!XRayManager.config.contains("xray.chunksToLoad"))
        {
            XRayManager.config.setValue("xray.chunksToLoad", chunksToLoad);
        }
        else
        {
            chunksToLoad = XRayManager.config.getInt("xray.chunksToLoad");
        }
        if (!XRayManager.config.contains("xray.spawnProtectedArea.size"))
        {
            XRayManager.config.setValue("xray.spawnProtectedArea.size", spawnProtectedArea);
        }
        else
        {
            spawnProtectedArea = XRayManager.config.getInt("xray.spawnProtectedArea.size");
        }
        if (!XRayManager.config.contains("xray.spawnProtection.enabled"))
        {
            XRayManager.config.setValue("xray.spawnProtection.enabled", spawnProtection);
        }
        else
        {
            spawnProtection = XRayManager.config.getBoolean("xray.spawnProtection.enabled");
        }
        if (!XRayManager.config.contains("xray.update.distance"))
        {
            XRayManager.config.setValue("xray.update.distance", updatedistance);
        }
        else
        {
            updatedistance = XRayManager.config.getInt("xray.update.distance");
        }
        if (!XRayManager.config.contains("xray.update.timeseconds"))
        {
            XRayManager.config.setValue("xray.update.timeseconds", updateTime);
        }
        else
        {
            updateTime = XRayManager.config.getInt("xray.update.timeseconds");
        }
        if (!XRayManager.config.contains("xray.hideTransparent"))
        {
            XRayManager.config.setValue("xray.hideTransparent", hideTransparent);
        }
        else
        {
            hideTransparent = XRayManager.config.getBoolean("xray.hideTransparent");
        }
        if (!XRayManager.config.contains("xray.keepblocks"))
        {
            List<String> names = new ArrayList<String>();
            for (org.bukkit.Material mat: keepList)
            {
                names.add(mat.name());
            }
            XRayManager.config.setValue("xray.keepblocks", names);
        }
        else
        {
            keepList.clear();
            List<String> names =XRayManager.config.getStringList("xray.keepblocks");
            for(String name:names)
            {
                keepList.add(org.bukkit.Material.getMaterial(name));
            }
        }

        XRayManager.config.save();
    }

    public boolean isCaves() {
        return caves;
    }

    public boolean isXray() {
        return xray;
    }

    public void setCaves(boolean caves) {
        this.caves = caves;
    }

    public void setXray(boolean xray) {
        this.xray = xray;
    }

    public XRayManager(Location start)
    {
        loc = start.clone();
    }

    public boolean isCommandUsed() {
        return commandUsed;
    }

    public void setCommandUsed(boolean commandUsed) {
        this.commandUsed = commandUsed;
    }

    public void resetChunks()
    {
        toRefresh.clear();
    }
    public void unloadme()
    {
        toRefresh.clear();
        toRefresh = null;
        loc = null;
        lastupDated = 0;
    }
    public Location getLoc() {
        return loc;
    }

    public List<Chunk> getChunks() {
        return toRefresh;
    }
    public boolean hasChunk(org.bukkit.Chunk toRefresh) {
        return hasChunk(((CraftChunk)toRefresh).getHandle());
    }
    public boolean hasChunk(Chunk toRefresh) {
        return this.toRefresh.contains(toRefresh);
    }
    public void addChunk(org.bukkit.Chunk toRefresh) {
        addChunk(((CraftChunk)toRefresh).getHandle());
    }
    public void addChunk(Chunk toRefresh) {
        this.toRefresh.add(toRefresh);
    }
    public void setLoc(Location loc) {
        this.loc = loc.clone();
    }

    public void setLastupDated() {
        this.lastupDated = System.currentTimeMillis();
    }

    public long getLastupDated() {
        return lastupDated;
    }

    public static net.minecraft.server.v1_14_R1.Chunk  xrayChunk(org.bukkit.Chunk real, Player player, boolean xray, boolean caves)
    {
        BiomeBase[] BB = (((CraftChunk)real).getHandle()).getBiomeIndex();
        ChunkConverter F = (((CraftChunk)real).getHandle()).p();
        long m = (((CraftChunk)real).getHandle()).q();
        ChunkSection[] achunksection = (((CraftChunk)real).getHandle()).getSections();
        //Consumer<Chunk> consumer = (((CraftChunk)real).getHandle()).
        //World world, ChunkCoordIntPair chunkcoordintpair, BiomeBase[] abiomebase, ChunkConverter chunkconverter, TickList<Block> ticklist, TickList<FluidType> ticklist1, long i, @Nullable ChunkSection[] achunksection, @Nullable Consumer<Chunk> consumer
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(real.getX(), real.getZ());
        net.minecraft.server.v1_14_R1.Chunk cool = new net.minecraft.server.v1_14_R1.Chunk(((CraftWorld)real.getWorld()).getHandle(), chunkCoordIntPair, BB, F, null, null, m, achunksection, null);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {

                    BlockPosition fakeBlock = new BlockPosition(x, y, z);
                    org.bukkit.block.Block realBlock = real.getBlock(x, y, z);

                    //if (cool.c(fakeBlock))
                    {
                        //IBlockData tmps = cool.getBlockData(fakeBlock);
                        if ((!realBlock.getType().equals(org.bukkit.Material.CAVE_AIR)) && (!realBlock.getType().equals(org.bukkit.Material.AIR)) && (!realBlock.getType().equals(org.bukkit.Material.BARRIER))) {
                            if (realBlock.getType().equals(org.bukkit.Material.LAVA) || realBlock.getType().equals(org.bukkit.Material.WATER)|| realBlock.getType().equals(org.bukkit.Material.SPAWNER)) {
                                if (realBlock.getLocation().distance(player.getLocation()) > 15) {
                                    if (realBlock.getType().equals(org.bukkit.Material.LAVA)) {
                                            //cool.setType(fakeBlock, Blocks.RED_STAINED_GLASS.getBlockData(), false);
                                    }
                                    else
                                    {
                                        //cool.setType(fakeBlock, Blocks.BLUE_STAINED_GLASS.getBlockData(), false);
                                    }
                                }
                            }
                            else {
                                if (keepList.contains(realBlock.getType()) && xray) {
                                    net.minecraft.server.v1_14_R1.Block var1 = ((CraftChunk) real).getHandle().getType(fakeBlock).getBlock();
                                    cool.setType(fakeBlock, var1.getBlockData(), false);

                                } else {

                                    if (realBlock.getType().isTransparent()) {
                                        if (hideTransparent) {
                                            cool.setType(fakeBlock, Blocks.AIR.getBlockData(), false);
                                        }
                                    } else {
                                        cool.setType(fakeBlock, Blocks.BARRIER.getBlockData(), false);
                                    }
                                }
                            }
                        }
                        else {
                            if (realBlock.getType().equals(org.bukkit.Material.AIR) || realBlock.getType().equals(org.bukkit.Material.CAVE_AIR)) {
                                if (realBlock.getLocation().distance(player.getLocation()) > 15) {
                                    if (y < realBlock.getWorld().getHighestBlockAt(realBlock.getLocation()).getY() - 15) {
                                        if (caves) {
                                            cool.setType(fakeBlock, Blocks.YELLOW_STAINED_GLASS.getBlockData(), false);
                                        }
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }

        return cool;
    }
}

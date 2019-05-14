package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.managers.RouterManager;
import com.firesoftitan.play.titansql.CallbackResults;
import com.firesoftitan.play.titansql.DataTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.Table;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MainModule {
    protected ModuleTypeEnum type = null;
    protected Location link = null;
    protected String moduleid = null;
    protected boolean needsaving = true;
    public static Table modulesSQL = new Table("tb_modules");
    public static HashMap<String, MainModule> moduleByID = new HashMap<String, MainModule>();

    public MainModule()
    {

    }

    public ModuleTypeEnum getType() {
        return type;
    }

    public static void setupTable()
    {
        modulesSQL.addDataType("id", DataTypeEnum.CHARARRAY, true, false, true);
        modulesSQL.addDataType("type", DataTypeEnum.STRING, true, false, false);
        modulesSQL.addDataType("link", DataTypeEnum.LOCATION, false, false, false);
        modulesSQL.addDataType("pump_a", DataTypeEnum.LOCATION, false, false, false);
        modulesSQL.addDataType("pump_b", DataTypeEnum.LOCATION, false, false, false);
        modulesSQL.addDataType("locations", DataTypeEnum.STRINGLIST, false, false, false);
        modulesSQL.addDataType("items", DataTypeEnum.ITEMLIST, false, false, false);
        modulesSQL.addDataType("items_slots", DataTypeEnum.INTLIST, false, false, false);
        modulesSQL.addDataType("hide", DataTypeEnum.BOOLEAN, false, false, false);
        modulesSQL.addDataType("slots", DataTypeEnum.INTLIST, false, false, false);
        modulesSQL.addDataType("mode", DataTypeEnum.STRING, false, false, false);
        modulesSQL.addDataType("selection", DataTypeEnum.INTEGER, false, false, false);
        modulesSQL.addDataType("keep", DataTypeEnum.INTLIST, false, false, false);
        modulesSQL.addDataType("keep_slots", DataTypeEnum.INTLIST, false, false, false);
        modulesSQL.addDataType("max", DataTypeEnum.INTLIST, false, false, false);
        modulesSQL.addDataType("max_slots", DataTypeEnum.INTLIST, false, false, false);
        modulesSQL.createTable();
    }
    public static void loadAllModules() {
        setupTable();
        loadModules();
    }
    public static void loadModules()
    {
        Utilities.reTryLoad(modulesSQL, MainModule.class, "loadProtection", "Modules");
        modulesSQL.search(new CallbackResults() {
            @Override
            public void onResult(List<HashMap<String, ResultData>> results) {
                if (results != null && results.size() >0) {
                    for (HashMap<String, ResultData> result : results) {
                        ModuleTypeEnum type = null;
                        String tmpType = result.get("type").getString();
                        String key = result.get("id").getString();
                        type = getModuleTypeSafe(tmpType);
                        if (type != null) {
                            MainModule mh = type.getNew();
                            mh.setModuleid(key);
                            mh.loadInfo(result);
                            moduleByID.put(key, mh);
                        }
                    }
                }
                Utilities.doneTryLoading(modulesSQL);
                RouterManager.loadAllRouters();
            }
        });

    }
    public static void saveALL() {
        for (MainModule mainModule : moduleByID.values()) {
            if (mainModule.needsaving) {
                if (mainModule instanceof BottleModule) {
                    ((BottleModule) mainModule).saveInfo();
                } else if (mainModule instanceof BucketsModule) {
                    ((BucketsModule) mainModule).saveInfo();
                } else if (mainModule instanceof CobblestoneGenModule) {
                    ((CobblestoneGenModule) mainModule).saveInfo();
                } else if (mainModule instanceof FarmModule) {
                    ((FarmModule) mainModule).saveInfo();
                } else if (mainModule instanceof IceModule) {
                    ((IceModule) mainModule).saveInfo();
                } else if (mainModule instanceof InfernalModule) {
                    ((InfernalModule) mainModule).saveInfo();
                } else if (mainModule instanceof InventoryModule) {
                    ((InventoryModule) mainModule).saveInfo();
                } else if (mainModule instanceof ItemModule) {
                    ((ItemModule) mainModule).saveInfo();
                } else if (mainModule instanceof KillerModule) {
                    ((KillerModule) mainModule).saveInfo();
                } else if (mainModule instanceof PlacerModule) {
                    ((PlacerModule) mainModule).saveInfo();
                } else if (mainModule instanceof SandGenModule) {
                    ((SandGenModule) mainModule).saveInfo();
                } else {
                    System.out.println("[TitanBox]: Unsupported Module Type.");
                    mainModule.saveInfo();
                }
            }
        }
    }
    private static ModuleTypeEnum getModuleTypeSafe(String tmpType) {
        try {
            ModuleTypeEnum type;
            type = ModuleTypeEnum.valueOf(tmpType);
            return type;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    public boolean isLoaded()
    {
        if (link != null)
        {
            if (link.getChunk() != null) {
                return Utilities.isLoaded(link);
            }
        }
        return false;
    }
    public void loadInfo(HashMap<String, ResultData> result)
    {
        if (result.get("link") != null) {
            if (result.get("link").getLocation() != null) {
                link = result.get("link").getLocation().clone();
            }
        }
    }
    public void needSaving()
    {
        needsaving = true;
    }
    public void saveInfo()
    {
        if (type == null)
        {
            type = ModuleTypeEnum.Inventory;
        }

        modulesSQL.setDataField("id", moduleid);
        modulesSQL.setDataField("type", type.getType());
        modulesSQL.setDataField("link", link);


        //modules.setValue("modules." + moduleid + ".slots.link", link);
        //modules.setValue("modules." + moduleid + ".slots.type", type.getType());
    }
    protected void sendDate()
    {
        if (moduleid != null) {
            modulesSQL.insertData();
        }
    }
    public void clearInfo()
    {

    }
    public static ItemStack getNewModule(ModuleTypeEnum type)
    {

        ItemStack itStack = new ItemStack(Material.PAPER, 1);
        itStack = Utilities.changeName(itStack, type.getTitle());
        List<String> info = new ArrayList<String>();

        info.add(ChatColor.YELLOW + "link: " + ChatColor.WHITE + "not set");
        info.add(ChatColor.YELLOW + "Type: " + ChatColor.WHITE + type.getType());

        itStack = Utilities.addLore(itStack, info);
        return itStack;
    }
    public ItemStack getMeAsIcon()
    {
        return new ItemStack(Material.PAPER, 1);
    }
    public boolean ready()
    {
        return true;
    }
    public static ItemStack getItemfromModule(MainModule from)
    {
        ItemStack block = from.getMeAsIcon();
        String SLIMEFUNname = "";
        if (from.getLink() != null) {
            if (from.getLink().getBlock().getType() != Material.AIR) {
                short blockDate = from.getLink().getBlock().getData();
                Material type = from.getLink().getBlock().getType();
                block = new ItemStack(type, 1, blockDate);
                if (block.getType() == Material.PLAYER_HEAD) {
                    String texture = "";
                    try {
                        texture = CustomSkull.getTexture(from.getLink().getBlock());
                        block = CustomSkull.getItem(texture);
                    } catch (Exception e) {

                    }
                }
                if (from.getLink().getBlock().getType() == Material.BREWING_STAND) {
                    block = new ItemStack(Material.BREWING_STAND);
                }
                if (BlockStorage.getBlockInfo(from.getLink().getBlock()).contains("id")) {
                    SLIMEFUNname = BlockStorage.getBlockInfo(from.getLink().getBlock()).getString("id");
                    if (SLIMEFUNname != null) {
                        SLIMEFUNname = Utilities.fixCapitalization(SLIMEFUNname);
                    }
                    else
                    {
                        SLIMEFUNname = "";
                    }
                }
            }
        }
        NBTTagCompound data = new NBTTagCompound();
        List<String> info = new ArrayList<String>();
        info.add(ChatColor.YELLOW + "link: " + from.getLinkLore());
        if (from.getLink() != null)
        {
            data.setString("link", Utilities.serializeLocation(from.getLink()));
        }
        if (from.getModuleid() != null) {
            data.setString("id", from.getModuleid());
        }
        if (from.getType() != null) {
            info.add(ChatColor.YELLOW + "Type: " + ChatColor.WHITE + from.getType().getType());
            data.setString("type", from.getType().getType());
        }
        if (!SLIMEFUNname.equals(""))
        {
            info.add(ChatColor.YELLOW + "Name: " + ChatColor.WHITE + SLIMEFUNname);
            data.setString("name", SLIMEFUNname);
        }


        block = Utilities.changeName(block, from.getType().getTitle());
        block = Utilities.addLore(block, info);
        NBTTagCompound main = Utilities.getNBTTag(block);
        main.set("module", data);
        block = Utilities.setNBTTag(block, main);

        if (block == null)
        {
            return Utilities.addLore(new ItemStack(Material.PAPER), info);
        }
        return block.clone();
    }
    public String getLinkLore()
    {
        if (getLink() == null)
        {
            return ChatColor.WHITE  + "not set";
        }
        else
        {
            Location from = getLink();
            return ChatColor.WHITE + from.getWorld().getName() + ": " + from.getBlockX() + ": " + from.getBlockY() + ": " + from.getBlockZ();
        }
    }
    public void OpenGui(Player player)
    {

    }

    public static MainModule getModulefromID(String id)
    {
        if (moduleByID.containsKey(id))
        {
            return moduleByID.get(id);
        }
       return null;
    }
    public static MainModule getModulefromItem(ItemStack from)
    {
        if (from == null)
        {
            return null;
        }
        MainModule printing = null;
        ItemMeta IMeta = from.getItemMeta();
        ModuleTypeEnum type = null;
        String moduleID = null;
        Location location = null;
        NBTTagCompound nbtTagCompound = Utilities.getNBTTag(from);

        if (nbtTagCompound != null && nbtTagCompound.hasKey("module"))
        {
            NBTTagCompound subnbtTagCompound = nbtTagCompound.getCompound("module");
            moduleID = subnbtTagCompound.getString("id");
            if (moduleByID.containsKey(moduleID)) {
                return moduleByID.get(moduleID);
            }
            if (subnbtTagCompound.hasKey("type")) {
                type = getModuleTypeSafe(subnbtTagCompound.getString("type"));
            }
            if (subnbtTagCompound.hasKey("link")) {
                location = Utilities.deserializeLocation(subnbtTagCompound.getString("link"));
            }
            if (type == null)
            {
                type = ModuleTypeEnum.Inventory;
            }
            printing = type.getNew();
            if (moduleID != null) {
                printing.setModuleid(moduleID);
            }
            if (location != null)
            {
                printing.setLink(location, null);
            }
        }
        else if (IMeta != null)
        {
            if (IMeta.hasDisplayName()) {
                if (IMeta.getDisplayName().startsWith(ChatColor.DARK_BLUE + "")) {
                    List<String> lore = IMeta.getLore();
                    if (lore != null) {

                        for (String line : lore) {
                            String[] info = ChatColor.stripColor(line).split(": ");

                            if (info[0].equalsIgnoreCase("Module ID")) {
                                if (moduleByID.containsKey(info[1])) {
                                    return moduleByID.get(info[1]);
                                }
                                moduleID = info[1];
                            }
                            if (info[0].equalsIgnoreCase("Type")) {
                                type = getModuleTypeSafe(info[1]);
                            }
                            if (info[0].equalsIgnoreCase("Link")) {
                                if (info.length > 2) {
                                    if (info[4].contains("Secondary"))
                                    {
                                        info[4] = info[4].split(" ")[0];
                                    }
                                    World world = Bukkit.getWorld(info[1]);
                                    int x = Integer.parseInt(info[2]);
                                    int y = Integer.parseInt(info[3]);
                                    int z = Integer.parseInt(info[4]);
                                    location = new Location(world, x, y, z);
                                }
                            }

                        }
                        if (type == null)
                        {
                            type = ModuleTypeEnum.Inventory;
                        }
                        printing = type.getNew();
                        if (moduleID != null) {
                            printing.setModuleid(moduleID);
                        }
                        if (location != null)
                        {
                            printing.setLink(location, null);
                        }


                    }
                }
                if (type == null)
                {
                    return null;
                }
                if (!IMeta.getDisplayName().startsWith(type.getTitle())) {
                    return null;
                }
            }
        }
        if (printing !=null && printing.getModuleid() != null) {
            moduleByID.put(printing.getModuleid(), printing);
        }
        return printing;
    }

    public Location getLink() {
        return link;
    }

    public String getModuleid() {
        return moduleid;
    }

    public boolean setLink(Location link, Player player) {
        if (link !=null)
        {
            if (link.getBlock().getType() == Material.PLAYER_WALL_HEAD)
            {
                Block block = link.getBlock();
                block.setType(Material.PLAYER_HEAD);
                Utilities.fixBlockTexture(link);
            }
        }
        return false;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }

    public static String getNewIDString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random(System.currentTimeMillis());
        while (salt.length() < 36) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();

        if (moduleByID.containsKey(SALTCHARS))
        {
            return getNewIDString();
        }
        return saltStr;

    }
    public static void onInventoryClickEvent(InventoryClickEvent event) {
        for (ModuleTypeEnum mte: ModuleTypeEnum.values()) {
            if (event.getView().getTitle().startsWith(mte.getTitle())) {
                event.setCancelled(true);
                ItemStack mainHand = event.getWhoClicked().getInventory().getItemInMainHand();
                if (Utilities.isEmpty(mainHand)) {
                    event.getWhoClicked().closeInventory();
                    return;
                }
                MainModule mh = MainModule.getModulefromItem(mainHand);
                if (mh == null) {
                    event.getWhoClicked().closeInventory();
                    return;
                }
                if (mh instanceof InventoryModule) {
                    InventoryModule.onInventoryClickEvent(event, (InventoryModule) mh);
                }

            }
        }
    }
    public void unLinkAll()
    {

    }
    public static void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
        if (!Utilities.isEmpty(mainHand)) {
            MainModule mh = MainModule.getModulefromItem(mainHand);
            if (mh != null) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (event.getPlayer().isSneaking())
                    {
                        mh.unLinkAll();
                        mh.clearInfo();
                        mh.preLoadSlots();
                        updateGUIClicked(event.getPlayer(), mh, false);
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: Device(s) unlinked!");
                    }
                    else {
                        mh.OpenGui(event.getPlayer());
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: you can clear all link devices by Shift-Clicking air!");
                    }
                }

                if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    String location = event.getClickedBlock().getLocation().toString();
                    if (RouterManager.routersByLocation.containsKey(location)) {
                        return;
                    }
                    event.setCancelled(true);
                    Block clicked = event.getClickedBlock();
                    if ((Utilities.hasBuildRights(event.getPlayer(), clicked.getLocation())))
                    {
                            if (mh.getModuleid() == null) {
                                mh.setModuleid(MainModule.getNewIDString());
                            }
                            boolean didset = mh.setLink(clicked.getLocation(), event.getPlayer());
                            if (!didset)
                            {
                                mh.OpenGui(event.getPlayer());
                                event.setCancelled(true);
                                return;
                            }
                            mh.clearInfo();
                            mh.preLoadSlots();
                            updateGUIClicked(event.getPlayer(), mh, false);
                    }
                    else
                    {
                        event.getPlayer().sendMessage(ChatColor.RED + "[TitanBox]: you can't build here!");
                    }

                }
            }
        }

    }
    public void preLoadSlots()
    {

    }
    public void runMe(UUID owner)
    {
        System.out.println("Wrong one!");
    }

    public static void updateGUIClicked(Player who, MainModule mh, boolean showgui) {
        mh.needSaving();
        ItemStack update = MainModule.getItemfromModule(mh);
        ItemStack mainHand = who.getInventory().getItemInMainHand();
        if (mainHand != null)
        {
            if (mainHand.getAmount() > 1)
            {
                mainHand.setAmount(mainHand.getAmount() - 1);
                who.getInventory().setItemInMainHand(update);
                who.getInventory().addItem(mainHand);
            }
            else
            {
                who.getInventory().setItemInMainHand(update);
            }
        }
        else {
            who.getInventory().setItemInMainHand(update);
        }
        if (showgui) {
            mh.OpenGui(who);
        }
    }
}

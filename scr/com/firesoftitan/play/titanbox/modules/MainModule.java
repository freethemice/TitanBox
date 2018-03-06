package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class MainModule {
    protected ModuleTypeEnum type = null;
    protected Location link = null;
    protected String moduleid = null;
    public static Config modules = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "modules.yml");
    public static HashMap<String, MainModule> moduleByID = new HashMap<String, MainModule>();

    public MainModule()
    {

    }

    public ModuleTypeEnum getType() {
        return type;
    }

    public static void loadAllModules()
    {
        if (modules.contains("modules")) {
            Set<String> keys = modules.getKeys("modules");
            for (String key : keys) {
                ModuleTypeEnum type = null;
                if (modules.contains("modules." + key + ".slots.type")) {
                    String tmpType = modules.getString("modules." + key + ".slots.type");
                    type = ModuleTypeEnum.valueOf(tmpType);
                }
                if (type == null)
                {
                    type = ModuleTypeEnum.Inventory;
                }
                MainModule mh = type.getNew();
                mh.setModuleid(key);
                mh.loadInfo();
                moduleByID.put(key, mh);
            }
        }
    }
    public boolean isLoaded()
    {
        if (link != null)
        {
            return link.getChunk().isLoaded();
        }
        return false;
    }
    public boolean isIdSaved()
    {
        return  modules.contains("modules." + moduleid);
    }
    public static void saveModules()
    {
        modules.save();
    }
    public void loadInfo()
    {
        if (modules.contains("modules." + moduleid + ".slots.link")) {
            link = modules.getLocation("modules." + moduleid + ".slots.link");
        }

    }

    public void saveInfo()
    {
        if (type == null)
        {
            type = ModuleTypeEnum.Inventory;
        }
        modules.setValue("modules." + moduleid + ".slots.link", link);
        modules.setValue("modules." + moduleid + ".slots.type", type.getType());
    }
    public void clearInfo()
    {

    }
    public static ItemStack getNewModule(ModuleTypeEnum type)
    {

        ItemStack itStack = new ItemStack(Material.PAPER, 1);
        itStack = TitanBox.changeName(itStack, type.getTitle());
        List<String> info = new ArrayList<String>();

        info.add(ChatColor.YELLOW + "link: " + ChatColor.WHITE + "not set");
        info.add(ChatColor.YELLOW + "Type: " + ChatColor.WHITE + type.getType());

        itStack = TitanBox.addLore(itStack, info);
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
                if (from.getLink().getBlock().getType() == Material.BURNING_FURNACE) {
                    type = Material.FURNACE;
                }
                block = new ItemStack(type, 1, blockDate);
                if (block.getType() == Material.SKULL) {
                    String texture = "";
                    try {
                        texture = CustomSkull.getTexture(from.getLink().getBlock());
                        block = CustomSkull.getItem(texture);
                    } catch (Exception e) {

                    }
                }
                if (from.getLink().getBlock().getType() == Material.BREWING_STAND) {
                    block = new ItemStack(Material.BREWING_STAND_ITEM);
                }
                if (BlockStorage.getBlockInfo(from.getLink().getBlock()).contains("id")) {
                    SLIMEFUNname = BlockStorage.getBlockInfo(from.getLink().getBlock()).getString("id");
                    if (SLIMEFUNname != null) {
                        SLIMEFUNname = SLIMEFUNname.replace("_", " ").toLowerCase();
                        SLIMEFUNname = WordUtils.capitalize(SLIMEFUNname);
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }
        block = TitanBox.changeName(block, from.getType().getTitle());
        List<String> info = new ArrayList<String>();
        info.add(ChatColor.YELLOW + "link: " + from.getLinkLore());
        if (from.getModuleid() != null) {
            info.add(ChatColor.YELLOW + "Module ID: " + ChatColor.WHITE + from.getModuleid());
        }
        if (from.getType() != null) {
            info.add(ChatColor.YELLOW + "Type: " + ChatColor.WHITE + from.getType().getType());
        }
        if (!SLIMEFUNname.equals(""))
        {
            info.add(ChatColor.YELLOW + "Name: " + ChatColor.WHITE + SLIMEFUNname);
        }

        block = TitanBox.addLore(block, info);
        if (block == null) return null;
        return block.clone();
    }
 /*   public static ItemStack getItemfromModule(MainModule from)
    {
        ItemStack itStack = new ItemStack(Material.PAPER, 1);
        itStack = TitanBox.changeName(itStack, from.getType().getTitle());
        List<String> info = new ArrayList<String>();
        info.add(ChatColor.YELLOW + "link: " + from.getLinkLore());
        if (from.getModuleid() != null) {
            info.add(ChatColor.YELLOW + "Module ID: " + ChatColor.WHITE + from.getModuleid());
        }
        if (from.getType() != null) {
            info.add(ChatColor.YELLOW + "Type: " + ChatColor.WHITE + from.getType().getType());
        }
        itStack = TitanBox.addLore(itStack, info);
        return itStack;
    }*/
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
        if (IMeta != null)
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
                                type = ModuleTypeEnum.valueOf(info[1]);
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
                        if (printing.isIdSaved()) {
                            printing.loadInfo();
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
        if (player != null) {
            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "Default Mode Error!");
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

        if (modules.contains("items." + saltStr))
        {
            return getNewIDString();
        }
        return saltStr;

    }
    public static void onInventoryClickEvent(InventoryClickEvent event) {
        for (ModuleTypeEnum mte: ModuleTypeEnum.values()) {
            if (event.getInventory().getName().startsWith(mte.getTitle())) {
                event.setCancelled(true);
                ItemStack mainHand = event.getWhoClicked().getInventory().getItemInMainHand();
                if (TitanBox.isEmpty(mainHand)) {
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
    public static void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
        if (!TitanBox.isEmpty(mainHand)) {
            MainModule mh = MainModule.getModulefromItem(mainHand);
            if (mh != null) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    mh.OpenGui(event.getPlayer());
                    event.setCancelled(true);
                }

                if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    String location = event.getClickedBlock().getLocation().toString();
                    if (RouterHolder.routersByLocation.containsKey(location)) {
                        return;
                    }
                    event.setCancelled(true);
                    Block clicked = event.getClickedBlock();
                    if ((GriefPrevention.instance.allowBuild(event.getPlayer(), clicked.getLocation()) == null) || event.getPlayer().hasPermission("titanbox.admin")){
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

    }

    public static void updateGUIClicked(Player who, MainModule mh, boolean showgui) {
        mh.saveInfo();
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

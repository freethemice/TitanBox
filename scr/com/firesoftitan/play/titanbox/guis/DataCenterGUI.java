package com.firesoftitan.play.titanbox.guis;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.PrivacyEnum;
import com.firesoftitan.play.titanbox.managers.DCCommandManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.PlayerProtectionManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import com.firesoftitan.play.titanbox.runnables.ChatGetterRunnable;
import com.firesoftitan.play.titanbox.runnables.PickAPlayerRunnable;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public class DataCenterGUI {
    private Inventory inventory;
    private Player player;
    private int startindex = 0;
    private int currentSelector = 0;
    private static int max = 0;
    private HashMap<Integer, Location> locationList = new HashMap<Integer, Location>();
    private static HashMap<Integer, DCCommandManager> commandHolders = new HashMap<Integer, DCCommandManager>();
    private static HashMap<UUID, DataCenterGUI> datacenters = new HashMap<UUID, DataCenterGUI>();
    private static Config config = new Config("plugins" + File.separator + "TitanBox" + File.separator  + "datacenter_config.yml");
    private static void saveDataCenter(HashMap<Integer, DCCommandManager> commandHolders)
    {
        for(Integer j: commandHolders.keySet())
        {
            DCCommandManager commandHolder = commandHolders.get(j);
            String key = commandHolder.getSlot() +"";
            DataCenterGUI.config.setValue("players_command_list." + key + ".material", commandHolder.getMaterial().getKey().getKey());
            DataCenterGUI.config.setValue("players_command_list." + key + ".texture", commandHolder.getTexture());
            DataCenterGUI.config.setValue("players_command_list." + key + ".commands", commandHolder.getCommands());
            DataCenterGUI.config.setValue("players_command_list." + key + ".lore", commandHolder.getLore());
            DataCenterGUI.config.setValue("players_command_list." + key + ".text", commandHolder.getText());
            DataCenterGUI.config.setValue("players_command_list." + key + ".slot", commandHolder.getSlot());
        }
        DataCenterGUI.config.save();
    }
    private static HashMap<Integer, DCCommandManager> loadDataCenter()
    {
        HashMap<Integer, DCCommandManager> commandHolders = new HashMap<Integer, DCCommandManager>();
        if (DataCenterGUI.config.contains("players_command_list")) {
            Set<String> keys = DataCenterGUI.config.getKeys("players_command_list");
            for (String key : keys) {
                Material material = Material.matchMaterial(DataCenterGUI.config.getString("players_command_list." + key + ".material"));
                String texture = DataCenterGUI.config.getString("players_command_list." + key + ".texture");
                List<String> commands = DataCenterGUI.config.getStringList("players_command_list." + key + ".commands");
                List<String> lore = DataCenterGUI.config.getStringList("players_command_list." + key + ".lore");
                String text = DataCenterGUI.config.getString("players_command_list." + key + ".text");
                int slot = DataCenterGUI.config.getInt("players_command_list." + key + ".slot");
                DCCommandManager temp = new DCCommandManager();
                temp.setMaterial(material);
                temp.setCommands(commands);
                temp.setTexture(texture);
                temp.setText(text);
                temp.setSlot(slot);
                temp.setLore(lore);
                commandHolders.put(temp.getSlot(), temp);
            }
        }
        return null;

    }
    public static DataCenterGUI getDataCenter(Player player)
    {
        return getDataCenter(player.getUniqueId());
    }
    public static DataCenterGUI getDataCenter(UUID uuid)
    {
        if (!datacenters.containsKey(uuid))
        {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                DataCenterGUI dataCenterGUI = new DataCenterGUI(player);
                datacenters.put(uuid, dataCenterGUI);
            }
        }
        return datacenters.get(uuid);
    }
    public static void removeDataCenter(Player player)
    {
        removeDataCenter(player.getUniqueId());
    }
    public static void removeDataCenter(UUID uuid)
    {
        datacenters.remove(uuid);
    }
    public DataCenterGUI(Player player)
    {
        this.player = player;
    }
    public static void load_config()
    {
        commandHolders = DataCenterGUI.loadDataCenter();
        if (commandHolders == null) {
            commandHolders = new HashMap<Integer, DCCommandManager>();
            int i = 0;
            DCCommandManager temp = new DCCommandManager();
            temp.setMaterial(Material.BOOK);
            temp.setCommands("report bug [chatinput:Enter bug in chat:chatinput]" );
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Report A Bug");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("report player [pickplayer] [chatinput:Enter reason for reporting player in chat:chatinput]");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Report A Player");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.ENCHANTED_BOOK);
            temp.setCommands("report suggestion [chatinput:Enter Suggestion in chat:chatinput]");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Make A Suggestion");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.OAK_SIGN);
            temp.setCommands("help");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Rank Info");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.END_CRYSTAL);
            temp.setCommands("rules");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Rules");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("discord link");
            temp.setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJlZmE4M2M5OTgyMzNlOWRlYWY3OTc1YWNlNGNkMTZiNjM2MmE4NTlkNTY4MmMzNjMxNGQxZTYwYWYifX19");
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Link Discord");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("{pickplayer}");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Players");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.CONDUIT);
            temp.setCommands("{particles}");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Toggle Particle");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.ANVIL);
            temp.setCommands("tvr");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Vote Links");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.ARMOR_STAND);
            temp.setCommands("{arrmorstands}");
            temp.setTexture(null);
            temp.setLore(ChatColor.WHITE + "10x10x10 Area centered around you.", ChatColor.WHITE + "Must be wild, or have build rights");
            temp.setText(ChatColor.GOLD + "Remove Broken Arrmor Stand!");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(SlimefunItemsManager.TITAN_MOTOR.getType());
            temp.setCommands("{slimefunblocks}");
            temp.setTexture(CustomSkull.getTexture(SlimefunItemsManager.TITAN_MOTOR));
            temp.setLore(ChatColor.WHITE + "10x10x10 Area centered around you.", ChatColor.WHITE + "Must be wild, or have build rights");
            temp.setText(ChatColor.GOLD + "Fix Invisable Slimefun Block");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.ENCHANTED_BOOK);
            temp.setCommands("sf guide");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Guide");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.SUNFLOWER);
            temp.setCommands("balance");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Balance");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.SUNFLOWER);
            temp.setCommands("withdraw [anvilinput:How Much??:anvilinput]");
            temp.setTexture(null);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Withdraw");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("ah sell [anvilinput:How Much??:anvilinput] [hand]");
            temp.setTexture(SlimefunItemsManager.AUCTIONHOUSE);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Place an item for sell");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("ah bid [anvilinput:How Much??:anvilinput] [hand]");
            temp.setTexture(SlimefunItemsManager.AUCTIONHOUSE);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Place an item for auction");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("rewards");
            temp.setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0=");
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Rewards");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("challenges");
            temp.setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0=");
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Challenges");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            i++;

            temp = new DCCommandManager();
            temp.setMaterial(Material.PLAYER_HEAD);
            temp.setCommands("{datacenter}");
            temp.setTexture(SlimefunItemsManager.DATACENTER);
            temp.setLore((List)null);
            temp.setText(ChatColor.GOLD + "Data Center");
            temp.setSlot(i);
            commandHolders.put(temp.getSlot(), temp);
            DataCenterGUI.saveDataCenter(commandHolders);
        }
        for (DCCommandManager dcCommandManager: commandHolders.values())
        {
            max = Math.max(dcCommandManager.getSlot(), max);
        }
    }
    public void buildCommands()
    {
        //9, 18, 27, 36, 45, 54
        int size =54;
        if (max < 45) size = 45;
        if (max < 36) size = 36;
        if (max < 27) size = 27;
        if (max < 18) size = 18;
        if (max < 9) size = 9;
        inventory = Bukkit.createInventory(null, size, "My Command List");
        for(int i = 0; i <size;i++)
        {
            ItemStack button;
            button = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            button = Utilities.clearLore(button);
            button = Utilities.changeName(button, "");
            inventory.setItem(i, button.clone());
        }
        for(Integer j: commandHolders.keySet()) {
            DCCommandManager commandHolder = commandHolders.get(j);
            inventory.setItem(commandHolder.getSlot(), commandHolder.getButton().clone());
        }
    }
    public void buildGUI()
    {
        inventory = Bukkit.createInventory(null, 54, "My Force Field List");
        buildGUI(3);
    }
    public void buildGUI(int selection)
    {

        PlayerProtectionManager ppH = PlayerProtectionManager.getPlayer(player.getUniqueId());
        ItemStack tmpButton = SlimefunItemsManager.FORCE_FIELD_YELLOW.clone();
        tmpButton = Utilities.clearLore(tmpButton);
        if (ppH != null) {
            locationList.clear();
            for(int i = 0; i <27;i++)
            {
                ItemStack button;
                button = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "");
                inventory.setItem(i, button.clone());
            }
            for(int i = 27; i <36;i++)
            {
                ItemStack button;
                button = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "");
                inventory.setItem(i, button.clone());
            }
            for(int i = 36; i <45;i++)
            {
                ItemStack button;
                button = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "");
                inventory.setItem(i, button.clone());
            }
            for(int i = 45; i <54;i++)
            {
                ItemStack button;
                button = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "");
                inventory.setItem(i, button.clone());
            }
            int j = 36;
            for (ForceFieldManager forceFieldManager : TitanBox.instants.adminList) {
                    ItemStack button = forceFieldManager.getPricacyItem();
                    button = Utilities.changeName(button, "Admin Warp: " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', forceFieldManager.getName()));
                    button = Utilities.addLore(button, "Click to TP", ChatColor.BLUE + "World: " + ChatColor.WHITE + forceFieldManager.getLocation().getWorld().getName(), ChatColor.BLUE + "Location: " + ChatColor.WHITE + forceFieldManager.getLocation().getBlockX() + "," + forceFieldManager.getLocation().getBlockY() + "," + forceFieldManager.getLocation().getBlockZ());
                    inventory.setItem(j, button.clone());
                    locationList.put(j, forceFieldManager.getLocation().clone());
                    j++;
                    if (j > 53) break;
            }



            List<ForceFieldManager> theList = new ArrayList(ppH.getForceFields());
            int index = 0;
            int i = 0;
            currentSelector = selection;
            if (selection == 0 || selection == 3) {
                for (ForceFieldManager forceFieldManager : theList) {
                    if (i > 26) break;
                    if (index >= startindex) {
                        ItemStack button = forceFieldManager.getPricacyItem();
                        button = Utilities.changeName(button, "Yours: " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', forceFieldManager.getName()));
                        button = Utilities.addLore(button, "Click to TP", ChatColor.BLUE + "World: " + ChatColor.WHITE + forceFieldManager.getLocation().getWorld().getName(), ChatColor.BLUE + "Location: " + ChatColor.WHITE + forceFieldManager.getLocation().getBlockX() + "," + forceFieldManager.getLocation().getBlockY() + "," + forceFieldManager.getLocation().getBlockZ());
                        inventory.setItem(i, button.clone());
                        locationList.put(i, forceFieldManager.getLocation().clone());
                        i++;
                    }
                    index++;
                }
            }
            if (selection == 1 || selection == 3) {
                for (ForceFieldManager forceFieldManager : ppH.getForceFieldsNotPrivate()) {
                    if (i > 26) break;
                    if (index >= startindex) {
                        if (forceFieldManager.getPrivacy() == PrivacyEnum.PUBLIC) {
                            ItemStack button = forceFieldManager.getPricacyItem();
                            String name = "???";
                            if (forceFieldManager.getOwner() != null) {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(forceFieldManager.getOwner());
                                if (offlinePlayer != null) {
                                    name = offlinePlayer.getName();
                                }
                            }
                            button = Utilities.changeName(button, "Public: " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', forceFieldManager.getName()));
                            button = Utilities.addLore(button, "Click to TP", ChatColor.BLUE + "Owner: " + ChatColor.WHITE + name, ChatColor.BLUE + "World: " + ChatColor.WHITE + forceFieldManager.getLocation().getWorld().getName(), ChatColor.BLUE + "Location: " + ChatColor.WHITE + forceFieldManager.getLocation().getBlockX() + "," + forceFieldManager.getLocation().getBlockY() + "," + forceFieldManager.getLocation().getBlockZ());
                            inventory.setItem(i, button.clone());
                            locationList.put(i, forceFieldManager.getLocation().clone());
                            i++;
                        }
                    }
                    index++;
                }
            }
            if (selection == 2 || selection == 3)
            {
                for (ForceFieldManager forceFieldManager : ppH.getForceFieldsNotPrivate()) {
                    if (i > 26) break;
                    if (index >= startindex) {
                        if (forceFieldManager.getPrivacy() == PrivacyEnum.FRIENDS) {
                            ItemStack button = forceFieldManager.getPricacyItem();
                            String name = "???";
                            if (forceFieldManager.getOwner() != null) {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(forceFieldManager.getOwner());
                                if (offlinePlayer != null) {
                                    name = offlinePlayer.getName();
                                }
                            }
                            button = Utilities.changeName(button, "Friend: " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', forceFieldManager.getName()));
                            button = Utilities.addLore(button, "Click to TP", ChatColor.BLUE + "Owner: " + ChatColor.WHITE + name, ChatColor.BLUE + "World: " + ChatColor.WHITE + forceFieldManager.getLocation().getWorld().getName(), ChatColor.BLUE + "Location: " + ChatColor.WHITE + forceFieldManager.getLocation().getBlockX() + "," + forceFieldManager.getLocation().getBlockY() + "," + forceFieldManager.getLocation().getBlockZ());
                            inventory.setItem(i, button.clone());
                            locationList.put(i, forceFieldManager.getLocation().clone());
                            i++;
                        }
                    }
                    index++;
                }
            }

            ItemStack button;
            try {
                ItemStack selector = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJjMjI2MGI3MDI3YzM1NDg2NWU4M2MxMjlmMmQzMzJmZmViZGJiODVmNjY0NjliYmY5ZmQyMGYzYzNjNjA3NyJ9fX0=");

                button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjOTZhNWMzZDEzYzMxOTkxODNlMWJjN2YwODZmNTRjYTJhNjUyNzEyNjMwM2FjOGUyNWQ2M2UxNmI2NGNjZiJ9fX0=");
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "Back");
                inventory.setItem(27, button.clone());


                button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzYWU4ZGU3ZWQwNzllMzhkMmM4MmRkNDJiNzRjZmNiZDk0YjM0ODAzNDhkYmI1ZWNkOTNkYThiODEwMTVlMyJ9fX0=");
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "Next");
                inventory.setItem(28, button.clone());

                button = selector.clone();
                if (selection != 0) button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFkMzBlOWUyNTcwNWM1MWI4NDZlNzRlNzc3OTYyM2I2OWMwNzQ0NjQ1ZGEwMDA0ZDRkYjBmZTQ2MzM2ZmY4ZSJ9fX0=");
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "Yours");
                inventory.setItem(29, button.clone());

                button = selector.clone();
                if (selection != 1) button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU1MzE0MWFhYmU4OWE4YTU4MDRhMTcyMTMzYjQzZDVkMGVlMDU0OWNjMTlkYjAzODU2ODQwNDNjZmE5NDZhNSJ9fX0=");
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "Public");
                inventory.setItem(30, button.clone());

                button = selector.clone();
                if (selection != 2) button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0MmIwNjZlMGU1ZTA5YTZlNmJiOTk4OWNjMjc0NTFmMmJkNzhmYjBkYzcyMTA4YWE5NDBmYzlkYjFjMjRlMSJ9fX0=");
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "Friends");
                inventory.setItem(31, button.clone());

                button = selector.clone();
                if (selection != 3) button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTUxN2I0ODI5YjgzMTkyYmQ3MjcxMTI3N2E4ZWZjNDE5NjcxMWU0MTgwYzIyYjNlMmI4MTY2YmVhMWE5ZGUxOSJ9fX0=");
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "All");
                inventory.setItem(32, button.clone());


                button = CustomSkull.getItem(SlimefunItemsManager.COMMANDCENTER);
                button = Utilities.clearLore(button);
                button = Utilities.changeName(button, "Command Center");
                inventory.setItem(35, button.clone());

                if (locationList.size() == 0)
                {
                    if (startindex > 0)
                    {
                        startindex--;
                        buildGUI(currentSelector);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



        }

    }
    public void open()
    {
        player.openInventory(inventory);
    }
    protected static boolean openAvilGUI(Player player, String text)
    {
        if (text.contains("[anvilinput:")) {
            int start = text.indexOf("[anvilinput:") + "[anvilinput:".length();
            int end = text.indexOf(":anvilinput]");
            String caption = text.substring(start, end);
            player.closeInventory();
            TitanBox.instants.getNextMessage(player, new ChatGetterRunnable() {
                @Override
                public void run() {
                    String out = text.replace("[anvilinput:" + caption + ":anvilinput]", this.getChat());
                    runCommand(player, out);
                }
            }, caption);
            return true;
        }
        return false;
    }
    protected static boolean openChatInput(Player player, String text)
    {
        if (text.contains("[chatinput:")) {
            int start = text.indexOf("[chatinput:") + "[chatinput:".length();
            int end = text.indexOf(":chatinput]");
            String caption = text.substring(start, end);
            player.closeInventory();
            TitanBox.instants.getNextMessage(player, new ChatGetterRunnable() {
                @Override
                public void run() {
                    String out = text.replace("[chatinput:" + caption + ":chatinput]", this.getChat());
                    runCommand(player, out);
                }
            }, caption);
            return true;
        }
        return false;
    }
    protected static boolean openPickPlayer(Player player, String text)
    {
        if (text.contains("[pickplayer]")) {
            player.closeInventory();
            PickAPlayerGUI pickAPlayer = new PickAPlayerGUI(player, new PickAPlayerRunnable() {
                @Override
                public void run() {
                    player.closeInventory();
                    OfflinePlayer picked = Bukkit.getOfflinePlayer(this.getPlayerPicked());
                    String out = text.replace("[pickplayer]", picked.getName());
                    runCommand(player, out);
                }
            });
            TitanBox.instants.pickPlayer.put(player.getUniqueId(), pickAPlayer);
            pickAPlayer.buildGUI();
            pickAPlayer.open();
            return true;
        }
        return false;
    }

    protected static boolean openHandInput(Player player, String text)
    {
        if (text.contains("[hand]")) {
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
                            String out = text.replace("[hand]", "");
                            runCommand(player, out);
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
            return true;
        }
        return false;
    }
    protected static void runCommand(Player player, String command)
    {
        if (openPickPlayer(player, command)) return;
        if (openAvilGUI(player, command)) return;
        if (openChatInput(player, command)) return;
        if (openHandInput(player, command)) return;

        TitanBox.bypassCommands.add(player.getUniqueId());
        Bukkit.dispatchCommand(player, command);
    }
    public void onInventoryClickEvent(int slot, InventoryClickEvent event)
    {
        if (event.getView().getTitle().equals("My Command List")) {
            Player player = (Player) event.getWhoClicked();
            WorldManager WH = WorldManager.getWorldHolder(player.getWorld());
            ForceFieldManager FFG = WH.getFieldIn(player.getLocation());
            if (slot > -1 && slot < inventory.getSize()) {
                event.setCancelled(true);

                try {
                    DCCommandManager commandHolder = commandHolders.get((Integer) slot);
                    if (commandHolder != null) {
                        player.closeInventory();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                TitanBox.bypassCommands.add(player.getUniqueId());
                                for (String command : commandHolder.getCommands()) {
                                    if (command.toLowerCase().contains("{datacenter}")) {
                                        buildGUI();
                                        open();
                                    }
                                    if (command.toLowerCase().contains("{arrmorstands}")) {
                                        if (FFG == null || FFG.hasRights(player.getUniqueId())) {
                                            List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                                            int found = 0;
                                            for (Entity entity : entities) {
                                                if (entity.getType() == EntityType.ARMOR_STAND) {
                                                    entity.remove();
                                                    found++;
                                                }
                                            }
                                            player.sendMessage(ChatColor.RED + "Armor Stand(s) Removed: " + ChatColor.WHITE + found);
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You don't have right to Force Field.");
                                        }
                                        command = command.replace("{arrmorstands}", "");
                                    }
                                    if (command.toLowerCase().contains("{slimefunblocks}")) {
                                        if (FFG == null || FFG.hasRights(player.getUniqueId())) {
                                            Location center = player.getLocation().clone();
                                            int found = 0;
                                            for (int x = -5; x < 6; x++) {
                                                for (int y = -5; y < 6; y++) {
                                                    for (int z = -5; z < 6; z++) {
                                                        Location checkPoint = center.clone().add(x, y, z);
                                                        String id = Utilities.fixBlockTexture(checkPoint);
                                                        if (id != null) {
                                                            found++;
                                                            player.sendMessage(ChatColor.YELLOW + "Found: " + ChatColor.WHITE + id);
                                                        }
                                                    }
                                                }
                                            }
                                            player.sendMessage(ChatColor.RED + "Total Found: " + ChatColor.WHITE + found);
                                        }
                                        command = command.replace("{slimefunblocks}", "");
                                    }
                                    if (command.toLowerCase().contains("{pickplayer}")) {
                                        player.closeInventory();
                                        PickAPlayerGUI pickAPlayer = new PickAPlayerGUI(player, new PickAPlayerRunnable() {
                                            @Override
                                            public void run() {
                                                player.closeInventory();
                                            }
                                        });
                                        TitanBox.instants.pickPlayer.put(player.getUniqueId(), pickAPlayer);
                                        pickAPlayer.buildGUI();
                                        pickAPlayer.open();

                                        command = command.replace("{pickplayer}", "");

                                    }

                                    if (command.toLowerCase().contains("{particles}")) {
                                        UUID myUUDI = player.getUniqueId();
                                        boolean current = TitanBox.settings.getBoolean("settings." + myUUDI.toString() + ".particle");
                                        TitanBox.settings.setValue("settings." + myUUDI.toString() + ".particle", Boolean.valueOf(!current));
                                        player.sendMessage(ChatColor.RED + "Your particle effects have been set to: " + (!current));
                                        player.closeInventory();
                                        command = command.replace("{particles}", "");
                                    }
                                    if (command.replace(" ", "").length() > 0) {
                                        runCommand(player, command);
                                    }
                                }
                            }
                        }.runTaskLater(TitanBox.instants, 5);
                    }
                } catch (Exception e) {

                }
            }
        }
        if (event.getView().getTitle().equals("My Force Field List")) {
            try {
                PlayerProtectionManager ppH = PlayerProtectionManager.getPlayer(event.getWhoClicked().getUniqueId());
                List<ForceFieldManager> theList = new ArrayList(ppH.getForceFields());
                if (slot > -1 && slot < inventory.getSize()) {
                    event.setCancelled(true);
                    if (slot < 27 || slot > 35)
                    {
                        if (locationList.containsKey((Integer)slot)) {
                            Location location = locationList.get((Integer)slot).clone();
                            Utilities.startTeleport((Player) event.getWhoClicked(), location);
                            event.getWhoClicked().closeInventory();
                        }
                        return;
                    }

                    Player player = (Player) event.getWhoClicked();
                    WorldManager WH = WorldManager.getWorldHolder(player.getWorld());
                    ForceFieldManager FFG = WH.getFieldIn(player.getLocation());

                    if (slot == 27) {
                        startindex--;
                        if (startindex < 1) startindex =0;
                        this.buildGUI(currentSelector);
                        //this.open();
                        return;
                    }
                    if (slot == 28) {
                        startindex++;
                        this.buildGUI(currentSelector);
                        //this.open();
                        return;
                    }
                    if (slot == 29) {
                        this.buildGUI(0);
                        //this.open();
                        return;
                    }
                    if (slot == 30) {
                        this.buildGUI(1);
                        //this.open();
                        return;
                    }
                    if (slot == 31) {
                        this.buildGUI(2);
                        //this.open();
                        return;
                    }
                    if (slot == 32) {
                        this.buildGUI(3);
                        //this.open();
                        return;
                    }
                    if (slot == 35) {
                        this.buildCommands();
                        this.open();
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                event.getWhoClicked().closeInventory();
                event.setCancelled(true);
            }
        }
    }
}

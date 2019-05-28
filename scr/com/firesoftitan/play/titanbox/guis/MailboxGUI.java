package com.firesoftitan.play.titanbox.guis;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.managers.BulkPackageManager;
import com.firesoftitan.play.titanbox.managers.PackageManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.runnables.ChatGetterRunnable;
import com.firesoftitan.play.titanbox.runnables.PickAPlayerRunnable;
import com.firesoftitan.play.titansql.ResultData;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MailboxGUI {
    private Inventory inventory;
    private UUID player;
    private List<PackageManager> mailbox;
    private long lastmessage;
    private boolean needsaving = false;
    private static HashMap<UUID, MailboxGUI> mailboxes = new HashMap<UUID, MailboxGUI>();
    private static HashMap<UUID, UUID> viewing = new HashMap<UUID, UUID>();

    public static void checkAllMail() {
        try {
            for(MailboxGUI mailboxGUI: mailboxes.values())
            {
                mailboxGUI.checkMail();
            }
        } catch (Exception e) {

        }
    }
    public static MailboxGUI setupNewBox(Player player)
    {
        return setupNewBox(player.getUniqueId());
    }
    public static MailboxGUI setupNewBox(UUID uuid)
    {
        MailboxGUI mailboxGUI = mailboxes.get(uuid);
        if (mailboxGUI == null)
        {
            mailboxGUI = new MailboxGUI(uuid);
            mailboxes.put(uuid, mailboxGUI);
        }
        return mailboxGUI;
    }
    public static MailboxGUI getMailBox(Player player)
    {
        return getMailBox(player.getUniqueId());
    }
    public static MailboxGUI getMailBox(UUID uuid)
    {
        MailboxGUI mailboxGUI = mailboxes.get(uuid);
        if (mailboxGUI == null)
        {
            mailboxGUI = new MailboxGUI(uuid);
            mailboxes.put(uuid, mailboxGUI);
        }
        return mailboxGUI;
    }
    public static void saveALL()
    {
        for (MailboxGUI mailbox: mailboxes.values())
        {
            mailbox.saveAll();
        }
    }
    public MailboxGUI(UUID player)
    {
        this.player = player;
        mailbox = new ArrayList<PackageManager>();
        inventory = Bukkit.createInventory(null, 54, "Mail Box");
        lastmessage = 0;
    }
    public UUID getOwner()
    {
        return player;
    }
    public void checkMail()
    {
        if (mailbox.size() > 0) {
            long time = System.currentTimeMillis() - lastmessage;
            if (time > 5 * 60 * 1000) {
                if (isPlayerOnline()) {
                    int count = 0;
                    int dmg = 0;
                    for(int i =0; i < mailbox.size(); i++)
                    {
                        PackageManager packageManager = mailbox.get(i);
                        if (!packageManager.isInRoute()) {
                            count++;
                        }
                        else
                        {
                            if (!packageManager.givenDeliveryMessage())
                            {
                                dmg++;
                                packageManager.deliveryMessageGiven();
                            }
                        }

                    }
                    if (dmg > 0) {
                        lastmessage = System.currentTimeMillis();
                        getPlayer().sendMessage(ChatColor.GRAY + "You have a package on its way, ETA: 6 minutes.");
                    }
                    if (count > 0) {
                        lastmessage = System.currentTimeMillis();
                        getPlayer().sendMessage(ChatColor.RED + "You have mail! Go to a mailbox and check!");
                        getPlayer().sendMessage(ChatColor.GRAY + "To make a mail box, place a chest on a fence post.");
                    }
                }
            }
        }
    }
    public void addPackage(UUID from, ItemStack mailItem)
    {
        addPackage(from, mailItem, System.currentTimeMillis());
    }
    public void addPackage(Player from, ItemStack mailItem)
    {
        addPackage(from.getUniqueId(), mailItem, System.currentTimeMillis());
    }
    public void addPackage(UUID from, ItemStack mailItem, long time)
    {
        for(PackageManager ph: mailbox)
        {
            while (ph.getSent() == time)
            {
                time = time - 1;
            }
        }
        String id = PackageManager.getNewIDString();
        TitanBox.packagesIDS.add(id);
        PackageManager packageManager = new PackageManager(from, player, mailItem.clone(), time, id);
        packageManager.setNeedSaving();
        mailbox.add(packageManager);
        needsaving = true;
    }
    public void loadData(HashMap<String, ResultData> vaules)
    {
        String id = vaules.get("id").getString();
        UUID to = vaules.get("topost").getUUID();
        UUID from = vaules.get("frompost").getUUID();
        long sent = vaules.get("sentpost").getLong();
        ItemStack item = vaules.get("item").getItemStack();
        PackageManager packageManager = new PackageManager(from, to, item.clone(), sent, id);
        mailbox.add(packageManager);
    }
    public void saveAll()
    {
        if (needsaving) {
            for (PackageManager packageManager : mailbox) {
                packageManager.save();
            }
        }
        needsaving = false;
    }
    public boolean isPlayerOnline()
    {
        return Bukkit.getOfflinePlayer(player).isOnline();
    }

    public Player getPlayer() {
        if (isPlayerOnline())
        {
            Player player = Bukkit.getPlayer(this.player);
            return player;
        }
        return null;
    }
    public String getPlayerName() {
        if (isPlayerOnline())
        {
            Player player = Bukkit.getPlayer(this.player);
            if (player != null) {
                return player.getName();
            }
        }
        else
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.player);
            if (offlinePlayer != null) {
                return offlinePlayer.getName();
            }
        }
        return "????";
    }
    public void buildGui(Player player)
    {
        viewing.put(player.getUniqueId(), this.player);
        inventory = Bukkit.createInventory(null, 54, "Mail Box");
        int max = Math.min(45, mailbox.size());
        for(int i = 0; i < 45; i++)
        {
            inventory.setItem(i, Utilities.changeName(new ItemStack(Material.BARRIER),ChatColor.WHITE + "No Mail"));
        }
        for(int i = 0; i < max; i++)
        {
            PackageManager packageManager = mailbox.get(i);
            ItemStack item = packageManager.getItemStack();
            OfflinePlayer from = Bukkit.getOfflinePlayer(packageManager.getFrom());
            String date = Utilities.formatTime(System.currentTimeMillis() - packageManager.getSent());
            if (packageManager.isInRoute())
            {
                String lefy = packageManager.getTimeLeft();
                item = Utilities.addLore(item, ChatColor.RED + "------------", "In Route", "Time To Delivery: " + lefy, ChatColor.RED + "------------");

            }
            item = Utilities.addLore(item, ChatColor.GRAY + "------------", ChatColor.AQUA +"from: " + ChatColor.WHITE +from.getName(), ChatColor.AQUA +"when: " + ChatColor.WHITE +date + " ago.");
            inventory.setItem(i, item.clone());
        }
        for(int i = 45; i < 54; i++)
        {
            inventory.setItem(i, Utilities.changeName(new ItemStack(Material.LIME_STAINED_GLASS_PANE),""));
        }
        try {
            String name = this.getPlayerName();
            ItemStack button = CustomSkull.getItem(SlimefunItemsManager.INFO);
            button = Utilities.changeName(button, ChatColor.WHITE + "Click On Any Item Bellow");
            button = Utilities.addLore(button,  ChatColor.AQUA + "Owner: " + ChatColor.WHITE + name, ChatColor.WHITE + "To Mail It.", "", ChatColor.WHITE + "If Your Mail Box Is Full", ChatColor.WHITE + "You May Have More Mail", ChatColor.WHITE +"Click Above To Remove Mail");
            inventory.setItem(45, button.clone());

            button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhjYjk4OWVmMjljNzM0ZmUwNWE5NWJmYTE1YWYwOGFiYmI2NGExNzNlYTMyZjBmNmYwNmQ2ZTM0ZjliZDM4In19fQ==");
            button = Utilities.changeName(button, ChatColor.WHITE + "Send Message");
            button = Utilities.addLore(button,  ChatColor.WHITE + "Send a message to a player offline.", ChatColor.GREEN + "<Click Here>");
            inventory.setItem(46, button.clone());

            if (player.hasPermission("titanbox.admin"))
            {
                button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIwMzRmYzc5ODVhMTEwZmU3YWNjODczNTY5MjYzZmI0YjZiMzgyNjI5OGRkOGJlM2RmYmRiZWVkZGFkIn19fQ==");
                button = Utilities.changeName(button, ChatColor.WHITE + "View Packages");
                button = Utilities.addLore(button,  ChatColor.RED + "Admin Only", ChatColor.WHITE + "View Starting Packages", ChatColor.GREEN + "<Click Here>");
                inventory.setItem(47, button.clone());

                button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiNmEzNmEwYmZiOTM2N2RmZTk3NjJlMjc1YzNhZTY4YmFkMWY5ZDg4NjE2N2YyNDk0NzZiMTFjN2E4MzM4In19fQ==");
                button = Utilities.changeName(button, ChatColor.WHITE + "View Another Players Mail Box");
                button = Utilities.addLore(button,  ChatColor.RED + "Admin Only", ChatColor.WHITE + "View Another Players Mail Box", ChatColor.GREEN + "<Click Here>");
                inventory.setItem(48, button.clone());

                button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM3MmFmZjhmNmMxNDRkNzk5ZjJkMWExYTBjMWZlZmM0ZjA0N2YyYzQ2YWJkMjkyNjhjODBlY2JiN2IyNzU2In19fQ==");
                button = Utilities.changeName(button, ChatColor.WHITE + "Send Mail To All Online");
                button = Utilities.addLore(button,  ChatColor.RED + "Admin Only", ChatColor.WHITE + "This will send the item in your", ChatColor.WHITE + "hand to all online players", ChatColor.GREEN + "<Click Here>");
                inventory.setItem(49, button.clone());

                button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRhYjk1NTRkMGMyNTI4NTE1YTc5MjNjOTVlNjg1Y2VhNDRlOTY2NjRkMDYzOGQ3ZTE0ZGZhNWNjZDc3NmJhNSJ9fX0=");
                button = Utilities.changeName(button, ChatColor.WHITE + "Send Mail To All Players");
                button = Utilities.addLore(button,  ChatColor.RED + "Admin Only", ChatColor.WHITE + "This will send the item in your", ChatColor.WHITE + "hand to all online and offline players", ChatColor.GREEN + "<Click Here>");

                inventory.setItem(50, button.clone());

                button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjViZTQ5YmJkZDFkYjM1ZGVmMDRhZDExZjA2ZGVhYWY0NWM5NjY2YzA1YmMwMmJjOGJmMTQ0NGU5OWM3ZSJ9fX0=");
                button = Utilities.changeName(button, ChatColor.WHITE + "Send Mail To All and New Player");
                button = Utilities.addLore(button,  ChatColor.RED + "Admin Only", ChatColor.WHITE + "This will send the item in your", ChatColor.WHITE + "hand to all online, offline and future players", ChatColor.GREEN + "<Click Here>");
                inventory.setItem(51, button.clone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        player.openInventory(inventory);
    }
    public static void clickEvent(int slot, Player player, InventoryClickEvent event)
    {
        UUID ownerOfMailBox = viewing.get(player.getUniqueId());
        if (ownerOfMailBox == null)
        {
            ownerOfMailBox = player.getUniqueId();
        }
        MailboxGUI mailboxGUI = MailboxGUI.getMailBox(ownerOfMailBox);
        mailboxGUI.subClickEvent(slot, player, event);

    }
    public void subClickEvent(int slot, Player player, InventoryClickEvent event)
    {
        if (slot > -1 && slot < mailbox.size())
        {
            PackageManager packageManager = mailbox.get(slot);
            ItemStack gettting = packageManager.getItemStack().clone();
            if (player.getInventory().firstEmpty() > -1) {
                if (!packageManager.isInRoute()) {
                    if (player.getUniqueId().equals(this.player)) {
                        TitanBox.mailbox_SQL.delete("id", mailbox.get(slot).getID());
                        mailbox.remove(slot);
                        needsaving = true;
                    }
                    if (packageManager.isLetter())
                    {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(packageManager.getFrom());
                        String name = "???";
                        if (offlinePlayer != null)
                        {
                            name = offlinePlayer.getName();
                        }
                        NBTTagCompound nbtTagCompound = Utilities.getNBTTag(gettting);
                        String message = nbtTagCompound.getString("mailLetter");
                        if (message.contains("<br>"))
                        {
                            String[] multiLines = message.split("<br>");
                            for(String line: multiLines)
                            {
                                player.sendMessage(ChatColor.GREEN + "Message [" + ChatColor.WHITE + name + ChatColor.GREEN + "]: " + ChatColor.AQUA + ChatColor.translateAlternateColorCodes('&', line));
                            }

                        }
                        else {
                            player.sendMessage(ChatColor.GREEN + "Message [" + ChatColor.WHITE + name + ChatColor.GREEN + "]: " + ChatColor.AQUA + ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }
                    else {
                        player.getInventory().addItem(gettting);
                    }
                    buildGui(player);
                }
                else {
                    player.sendMessage(ChatColor.RED + "This package is in route, should be less than 6 mins");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "No room in inventory!");
            }

        }
        if (slot == 46)
        {
            new BukkitRunnable() {
                @Override
                public void run() {
                    PickAPlayerGUI pickAPlayerGUI = new PickAPlayerGUI(player, new PickAPlayerRunnable() {
                        @Override
                        public void run() {
                            UUID uuid = this.getPlayerPicked();
                            if (uuid == null || Bukkit.getOfflinePlayer(uuid) == null) {
                                player.sendMessage(ChatColor.RED + "Player not found.");
                                return;
                            }
                            TitanBox.instants.getNextMessage(player, new ChatGetterRunnable() {
                                @Override
                                public void run() {
                                    MailboxGUI.mailLetter(player, uuid, this.getChat());
                                    event.getClickedInventory().setItem(event.getSlot(), null);
                                    player.sendMessage(ChatColor.RED + "Message Sent!");
                                    return;
                                }
                            }, "Enter message in chat.");


                        }
                    });
                    TitanBox.instants.pickPlayer.put(player.getUniqueId(), pickAPlayerGUI);
                    pickAPlayerGUI.buildGUI();
                    pickAPlayerGUI.open();
                }
            }.runTaskLater(TitanBox.instants, 5);
        }
        if (player.hasPermission("titanbox.admin")) {
            if (slot == 47) {

            }
            if (slot == 48) {
                player.closeInventory();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PickAPlayerGUI pickAPlayerGUI = new PickAPlayerGUI(player, new PickAPlayerRunnable() {
                            @Override
                            public void run() {
                                UUID uuid = this.getPlayerPicked();
                                if (uuid == null || Bukkit.getOfflinePlayer(uuid) == null) {
                                    player.sendMessage(ChatColor.RED + "Player not found.");
                                    return;
                                }
                                getMailBox(uuid).buildGui(player);
                                return;
                            }
                        });
                        TitanBox.instants.pickPlayer.put(player.getUniqueId(), pickAPlayerGUI);
                        pickAPlayerGUI.buildGUI();
                        pickAPlayerGUI.open();
                    }
                }.runTaskLater(TitanBox.instants, 5);
            }
            if (slot == 49) {
                Utilities.pickAHand(player, new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Player player1: Bukkit.getOnlinePlayers())
                        {
                            MailboxGUI gui = MailboxGUI.getMailBox(player1);
                            gui.addPackage(player, player.getInventory().getItemInMainHand().clone());
                            player.sendMessage(ChatColor.RED + "Packages Sent to: " +ChatColor.WHITE + player1.getName());
                        }

                    }
                });
            }
            if (slot == 50) {
                Utilities.pickAHand(player, new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (MailboxGUI gui : mailboxes.values()) {
                            gui.addPackage(player, player.getInventory().getItemInMainHand().clone());
                        }
                        player.sendMessage(ChatColor.RED + "Packages Sent!");
                    }
                });
            }
            if (slot == 51) {
                Utilities.pickAHand(player, new BukkitRunnable() {
                            @Override
                            public void run() {
                                String myID = PackageManager.getNewIDString();
                                TitanBox.packagesIDS.add(myID);
                                BulkPackageManager packageHolder = new BulkPackageManager(player.getUniqueId(), player.getUniqueId(), player.getInventory().getItemInMainHand().clone(), System.currentTimeMillis(), myID);
                                packageHolder.setNeedSaving();
                                BulkPackageManager.addNewPackage(packageHolder);
                                player.sendMessage(ChatColor.RED + "New Start Packages Sent!");
                            }
                        });
            }
        }

        if (slot > 53 && slot < 90)
        {
            if (Utilities.isEmpty(event.getClickedInventory().getItem(event.getSlot()))) return;
            player.closeInventory();


            new BukkitRunnable() {
                @Override
                public void run() {
                    PickAPlayerGUI pickAPlayerGUI = new PickAPlayerGUI(player, new PickAPlayerRunnable() {
                        @Override
                        public void run() {
                            UUID uuid = this.getPlayerPicked();
                            if (uuid == null || Bukkit.getOfflinePlayer(uuid) == null) {
                                player.sendMessage(ChatColor.RED + "Player not found.");
                                return;
                            }
                            getMailBox(uuid).addPackage(player, event.getClickedInventory().getItem(event.getSlot()).clone());
                            event.getClickedInventory().setItem(event.getSlot(), null);
                            player.sendMessage(ChatColor.RED + "Package Sent!");
                            return;
                        }
                    });
                    TitanBox.instants.pickPlayer.put(player.getUniqueId(), pickAPlayerGUI);
                    pickAPlayerGUI.buildGUI();
                    pickAPlayerGUI.open();
                    }
                }.runTaskLater(TitanBox.instants, 5);

        }
    }
    public static void mailLetter(Player fromPlayer, Player toPlayer, String message)
    {
        mailLetter(fromPlayer, toPlayer.getUniqueId(), message);
    }
    public static void mailLetter(Player fromPlayer, UUID toPlayer, String message)
    {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("mailLetter", message);
        itemStack = Utilities.setNBTTag(itemStack, nbtTagCompound);
        if (fromPlayer.hasPermission("titanbox.admin"))
        {
            itemStack = Utilities.changeName(itemStack, ChatColor.GOLD + "Message from a Admin");
        }
        else {
            itemStack = Utilities.changeName(itemStack, "Message from a Player");
        }
        getMailBox(toPlayer).addPackage(fromPlayer, itemStack.clone());
        return;
    }
    public void load() {

    }
}

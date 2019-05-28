package com.firesoftitan.play.titanbox.guis;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.runnables.PickAPlayerRunnable;
import com.mojang.authlib.GameProfile;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import net.minecraft.server.v1_14_R1.TileEntitySkull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PickAPlayerGUI {

    private Inventory inventory;
    private Player player;
    private List<UUID> onlinePlayers = new ArrayList<UUID>();
    private List<UUID> allPlayers = new ArrayList<UUID>();
    private PickAPlayerRunnable runner;
    private int startindex = 0;
    private int currentSelector = 0;

    public PickAPlayerGUI(Player player, PickAPlayerRunnable runnable)
    {
        this.player = player;
        this.runner = runnable;
        inventory = Bukkit.createInventory(null, 54, "Pick A Player");
    }
    public void buildGUI() {
        buildGUI(1);
    }
    public void buildGUI(int Selector) {

        onlinePlayers.clear();
        allPlayers.clear();
        for(OfflinePlayer player1: Bukkit.getOfflinePlayers())
        {
            allPlayers.add(player1.getUniqueId());
            if (player1.isOnline())
            {
                onlinePlayers.add(player1.getUniqueId());
            }
        }
        for(int i = 0; i <45;i++)
        {
            ItemStack button;
            button = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
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

        this.currentSelector = Selector;
        if (currentSelector == 0)
        {
            int i = 0;
            int index = 0;
            if (startindex >= allPlayers.size()) startindex = allPlayers.size() - 1;
            for (UUID uuid: allPlayers)
            {
                if (i >= startindex) {
                    int finalIndex = index;
                    TileEntitySkull.b(new GameProfile(uuid, null), gameProfile -> {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta meta = (SkullMeta) skull.getItemMeta();
                        meta.setOwningPlayer(offlinePlayer);
                        skull.setItemMeta(meta);
                        if (!offlinePlayer.isOnline()) {
                            skull = Utilities.changeName(skull, ChatColor.RED + offlinePlayer.getName());
                            skull = Utilities.addLore(skull, ChatColor.RED + "Offline", ChatColor.RED + "Last Played: " + Utilities.formatTime(System.currentTimeMillis() - offlinePlayer.getLastPlayed()));
                        } else {
                            skull = Utilities.changeName(skull, ChatColor.GREEN + offlinePlayer.getName());
                            skull = Utilities.addLore(skull, ChatColor.GREEN + "Online");
                        }
                        inventory.setItem(finalIndex, skull);
                        return false;
                    }, true);
                    index++;
                }
                i++;

                if (i > 45) break;
            }
        }
        if (currentSelector == 1)
        {
            int i = 0;
            int index = startindex;
            if (startindex >= onlinePlayers.size()) startindex = onlinePlayers.size() - 1;
            for (UUID uuid: onlinePlayers)
            {
                int finalI = i;
                if (i >= index) {
                    TileEntitySkull.b(new GameProfile(uuid, null), gameProfile -> {
                        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta meta = (SkullMeta) skull.getItemMeta();
                        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
                        skull.setItemMeta(meta);
                        if (!Bukkit.getOfflinePlayer(uuid).isOnline()) {
                            skull = Utilities.addLore(skull, ChatColor.RED + "Offline");
                        } else {
                            skull = Utilities.addLore(skull, ChatColor.GREEN + "Online");
                        }
                        inventory.setItem(finalI, skull);
                        return false;
                    }, true);
                    index++;
                }
                i++;

                if (i > 45) break;
            }
        }
        ItemStack button;

        try {
            ItemStack selector = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJjMjI2MGI3MDI3YzM1NDg2NWU4M2MxMjlmMmQzMzJmZmViZGJiODVmNjY0NjliYmY5ZmQyMGYzYzNjNjA3NyJ9fX0=");

            button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjOTZhNWMzZDEzYzMxOTkxODNlMWJjN2YwODZmNTRjYTJhNjUyNzEyNjMwM2FjOGUyNWQ2M2UxNmI2NGNjZiJ9fX0=");
            button = Utilities.clearLore(button);
            button = Utilities.changeName(button, "Back");
            inventory.setItem(45, button.clone());


            button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzYWU4ZGU3ZWQwNzllMzhkMmM4MmRkNDJiNzRjZmNiZDk0YjM0ODAzNDhkYmI1ZWNkOTNkYThiODEwMTVlMyJ9fX0=");
            button = Utilities.clearLore(button);
            button = Utilities.changeName(button, "Next");
            inventory.setItem(46, button.clone());

            button = selector.clone();
            if (currentSelector != 0) button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTUxN2I0ODI5YjgzMTkyYmQ3MjcxMTI3N2E4ZWZjNDE5NjcxMWU0MTgwYzIyYjNlMmI4MTY2YmVhMWE5ZGUxOSJ9fX0=");
            button = Utilities.clearLore(button);
            button = Utilities.changeName(button, "ALL");
            inventory.setItem(47, button.clone());

            button = selector.clone();
            if (currentSelector != 1) button = CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUzODViMDVlN2FmNTQ2MzViMTBmMDJjZGIwMDQ1NjcyYzkxYzcyNGNmMTY0ZTUxOTNhNGY3YmU3MjkyZmYzMCJ9fX0=");
            button = Utilities.clearLore(button);
            button = Utilities.changeName(button, "Online");
            inventory.setItem(48, button.clone());
        } catch (Exception e) {

        }


    }
    public void open()
    {
        player.openInventory(inventory);
    }
    public void onInventoryClickEvent(int slot, InventoryClickEvent event)
    {
        UUID uuid = null;
        if (slot > -1 && slot < 45) {
            int index = startindex + slot;
            if (currentSelector == 0) {
                if (index >= allPlayers.size()) return;
                uuid = allPlayers.get(index);
            }
            if (currentSelector == 1) {
                if (index >= onlinePlayers.size()) return;
                uuid = onlinePlayers.get(index);
            }

            runner.setPlayerPicked(uuid);
            runner.runTask(TitanBox.instants);
            player.closeInventory();
            TitanBox.instants.pickPlayer.remove(player.getUniqueId());
        }
        if (slot == 45) {
            startindex--;
            if (startindex < 1) startindex =0;
            this.buildGUI(currentSelector);
            //this.open();
            return;
        }
        if (slot == 46) {
            startindex++;
            this.buildGUI(currentSelector);
            //this.open();
            return;
        }
        if (slot == 47) {
            this.buildGUI(0);
            //this.open();
            return;
        }
        if (slot == 48) {
            this.buildGUI(1);
            //this.open();
            return;
        }

    }
}

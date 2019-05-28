package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.guis.MailboxGUI;
import com.firesoftitan.play.titanbox.managers.ConfigManager;
import com.firesoftitan.play.titanbox.managers.SlimefunItemsManager;
import com.firesoftitan.play.titanbox.managers.protection.PlayerProtectionManager;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainRunnable implements Runnable {
    BukkitRunnable clearItems = null;
    @Override
    public void run() {


        checkItems();


        for(Player p: Bukkit.getOnlinePlayers()) {
            boolean flying = false;
            PlayerInventory inventory = p.getInventory();
            for(int i=0; i < inventory.getSize(); i++)
            {
                if (i > -1 && i < 9)
                {
                    //hotbar
                    if (!flying) flying = checkFlyOrb(p, inventory.getItem(i));
                    Utilities.checkDeathItem(p, inventory, i);
                }

            }

            isNewPlayerReady(p);

            checkDurability(p);

            if (!flying) flying = checkFlyObjects(p, inventory.getChestplate());
            if (!flying) flying = checkFlyObjects(p, inventory.getBoots());
            if (p.getGameMode() != GameMode.SURVIVAL) flying = true;
            if (!flying)
            {
                p.setFlying(false);
                p.setAllowFlight(false);
            }
            else
            {
                p.setAllowFlight(true);
            }

        }

        ReBirth();
        MailboxGUI.checkAllMail();
    }
    private void checkItems() {
        List<Item> itmes = new ArrayList<Item>();
        List<Monster> monsters = new ArrayList<Monster>();
        for(World world: Bukkit.getWorlds())
        {
            itmes.clear();
            for (Entity entity: world.getEntities())
            {
                if (entity instanceof Item) {
                    itmes.add((Item) entity);
                }
                if (entity instanceof Monster)
                {
                    monsters.add((Monster) entity);
                }
            }
            if (ConfigManager.isLag_monsters_enabled()) {
                if (monsters.size() > ConfigManager.getLag_monsters_max() && clearItems == null) {

                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[" + ChatColor.WHITE + world.getName() + ": Monsters=" + monsters.size() + "" + ChatColor.RED + "" + ChatColor.BOLD + "] Clearing Monsters in " + ChatColor.WHITE + "30 " + ChatColor.RED + "" + ChatColor.BOLD + "Seconds");
                    clearItems = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (clearItems != null && clearItems == this) {
                                for (Entity entity : world.getEntities()) {
                                    if (entity instanceof Monster) {
                                        Monster item = (Monster) entity;
                                        if (item != null) {
                                            if (!item.isDead()) {
                                                item.remove();
                                            }
                                        }
                                    }
                                }
                                clearItems = null;
                                Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[" + ChatColor.WHITE + world.getName() + ChatColor.GREEN + "" + ChatColor.BOLD + "] All Monsters. Cleared!");
                            }
                        }
                    };
                    clearItems.runTaskLater(TitanBox.instants, 30 * 20);
                    break;
                }
            }
            if (ConfigManager.isLag_items_enabled()) {
                if (itmes.size() > ConfigManager.getLag_items_max() && clearItems == null) {

                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Clearing Items in " + ChatColor.WHITE + "30 " + ChatColor.RED + "" + ChatColor.BOLD + "Seconds");
                    clearItems = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (clearItems != null && clearItems == this) {
                                for (Entity entity : world.getEntities()) {
                                    if (entity instanceof Item) {
                                        Item item = (Item) entity;
                                        if (item != null) {
                                            if (!item.isDead()) {
                                                item.remove();
                                            }
                                        }
                                    }
                                }
                                clearItems = null;
                                Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "All Items Cleared!");
                            }
                        }
                    };
                    clearItems.runTaskLater(TitanBox.instants, 30 * 20);
                    break;
                }
            }

        }
    }

    private void checkDurability(Player player)
    {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (Utilities.isWeapon(itemStack) || Utilities.isTool(itemStack) || Utilities.isArmor(itemStack)) {
            if (!Utilities.isEmpty(itemStack)) {
                if (itemStack.getDurability() > itemStack.getType().getMaxDurability()) {
                    Utilities.saveBrokenItem(player, itemStack);
                    player.getInventory().setItemInMainHand(null);
                }
            }
        }
    }
    private boolean isNewPlayerReady(Player player) {
        PlayerProtectionManager playerProtectionManager = PlayerProtectionManager.getPlayer(player);
        if (playerProtectionManager.count() < 1) {
            player.setHealth(20);
            player.setFoodLevel(15);
            return false;
        }
        return true;
    }
    private boolean checkFlyObjects(Player p, ItemStack itemStack)
    {
        UUID uuid = p.getUniqueId();
        if (Bukkit.getPlayer(uuid) == null) return false;
        else if (Bukkit.getPlayer(uuid).isDead()) return false;
        else if (!Bukkit.getPlayer(uuid).isSneaking()) return false;


        float cost = 0.075F;
        float charge = ItemEnergy.getStoredEnergy(itemStack);
        if (charge >= cost) {
            return true;
        }
        else return false;


    }
    private boolean checkFlyOrb(Player p, ItemStack itemStack) {
        if (Utilities.isItemEqual(itemStack, SlimefunItemsManager.FLY_ORB))
        {
            return true;
        }
        return false;
    }




    private void ReBirth()
    {
        try {
            UUID toRemove = null;
            for (UUID player: TitanBox.listen.playerListSaves.keySet()) {
                Player Dieing=  Bukkit.getPlayer(player);
                if (Dieing != null) {
                    if (Dieing.isDead() == false) {
                        PlayerInventory CheckInv = Dieing.getInventory();
                        ItemStack[] tmpSave = TitanBox.listen.playerListSaves.get(Dieing.getUniqueId());
                        boolean firstStone = false;
                        for (int i = 0; i < CheckInv.getSize(); i++) {
                            if (TitanBox.instants.checkforTitanStone(tmpSave[i]) && firstStone == false)
                            {
                                firstStone = true;
                                if (tmpSave[i].getAmount() > 1)
                                {
                                    tmpSave[i].setAmount(tmpSave[i].getAmount() - 1);
                                    CheckInv.setItem(i, tmpSave[i]);
                                }
                            }
                            else {
                                CheckInv.setItem(i, tmpSave[i]);
                            }
                        }
                        toRemove = Dieing.getUniqueId();
                        break;
                    }
                }
            }
            if (toRemove != null)
            {
                TitanBox.listen.playerListSaves.remove(toRemove);
            }
        } catch (Exception e) {

        }


    }
}

package com.firesoftitan.play.titanbox.listeners;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import com.firesoftitan.play.titanbox.runnables.ChatGetterRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class ShopListener implements Listener {

    public ShopListener()
    {

    }
    public void registerEvents(){
        PluginManager pm = TitanBox.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanBox.instants);
    }
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        Action action = event.getAction();
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        Location ofBlock = null;
        if (clickedBlock != null) ofBlock = clickedBlock.getLocation();
        Material clickedBlockType = null;
        if (clickedBlock != null) {
            clickedBlockType = clickedBlock.getType();
        } else {
            clickedBlockType = Material.AIR;
        }
        ItemStack mainhand = null;
        mainhand = player.getInventory().getItemInMainHand();
        if (clickedBlockType.name().endsWith("_SIGN"))
        {
            Sign sign = (Sign) clickedBlock.getState();
            String[] lines = sign.getLines();
            if ( ChatColor.stripColor(lines[0]).contains("[XP Shop]"))
            {
                ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
                if (ffH != null)
                {
                    if (ffH.isAdmin())
                    {
                        if (lines[1].length() < 1) {
                            if (ffH.hasRights(player.getUniqueId())) {
                                if (!Utilities.isEmpty(mainhand)) {
                                    player.closeInventory();
                                    ItemStack finalMainhand = mainhand;
                                    TitanBox.instants.getNextMessage(player, new ChatGetterRunnable() {
                                        @Override
                                        public void run() {
                                            String price = this.getChat();
                                            TitanBox.instants.getNextMessage(player, new ChatGetterRunnable() {
                                                @Override
                                                public void run() {

                                                    sign.setLine(1, ChatColor.RED + "Cost: " + ChatColor.BLACK + price + " " + ChatColor.BLUE + "Lvls");
                                                    sign.setLine(2, ChatColor.MAGIC + this.getChat());
                                                    sign.setLine(3, Utilities.getName(finalMainhand, false));
                                                    sign.update();
                                                }
                                            }, "Enter command to use to give the item in your hand (no slash), example: sf give <player> something");
                                        }
                                    }, "Enter how many levels it cost");

                                }
                                else
                                {
                                    player.sendMessage(ChatColor.YELLOW +"Hold the item in your hand that you want to sell.");
                                }
                            }
                            else
                            {
                                player.sendMessage(ChatColor.YELLOW +"Shop coming soon.");
                            }
                        }
                        else
                        {
                            String cost = ChatColor.stripColor(lines[1]).replace("Cost: ", "").replace(" Lvls", "");
                            int levels = Integer.parseInt(cost);
                            int thereLevels = player.getLevel();
                            if (thereLevels >= levels)
                            {
                                player.setLevel(thereLevels - levels);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.stripColor(lines[2].replace("<player>", player.getName())));
                                player.sendMessage(ChatColor.GREEN + "Thank you for your purchase!");
                            }
                            else
                            {
                                player.sendMessage(ChatColor.RED + "Not enough XP");
                            }

                        }

                    }
                }
            }
        }
    }
}

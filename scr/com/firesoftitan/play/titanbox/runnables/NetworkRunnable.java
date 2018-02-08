package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.machines.NetworkMonitor;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NetworkRunnable implements Runnable  {
    public int myId = -1;
    public Long startTime = Long.valueOf(0);
    public Location loc;
    public Player player;
    public int gameTick =0;
    public double perSecond = -1;
    public double perTick = -1;
    public double finalperSecond = -1;
    public double finalperTick = -1;
    public double[] currentData;
    public Inventory myInv;
    @Override
    public void run() {
        gameTick++;
        double[] check = NetworkMonitor.getEnergyInfo(loc);
        if (System.currentTimeMillis() - startTime >= 1000 && perSecond == -1) {
            double here = currentData[2] + currentData[4];
            double here2 = check[2] + check[4];
            perSecond = here2 - here;
        }
        if (gameTick >= 20 && perTick == -1) {
            double here = currentData[2] + currentData[4];
            double here2 = check[2] + check[4];
            perTick = here2 - here;
        }
        updateGUI(check);
        if (perSecond != -1 && perTick != -1)
        {
            currentData = check;
            finalperSecond = perSecond;
            finalperTick = perTick;
            perSecond = -1;
            perTick = -1;
            gameTick = 0;
            startTime = System.currentTimeMillis();
        }
        if (myInv.getViewers().size() < 1)
        {
            Bukkit.getScheduler().cancelTask(myId);
        }
    }
    private void updateGUI(double[] check)
    {
        ItemStack powerGen = SlimefunItems.COAL_GENERATOR.clone();
        powerGen = TitanBox.clearLore(powerGen);
        powerGen = TitanBox.changeName(powerGen, "Power Generation");
        List<String> lore = new ArrayList<String>();

        ChatColor plusminus = ChatColor.GREEN;
        if (finalperSecond < 0)
        {
            plusminus = ChatColor.RED;
        }
        lore.add(ChatColor.DARK_GRAY + "\u21E8 Real Time: "+ ChatColor.YELLOW +"\u26A1 " + plusminus + TitanBox.formatCommas(finalperSecond) + " J/s");
        lore.add(ChatColor.DARK_GRAY + "\u21E8 Game Time: "+ ChatColor.YELLOW +"\u26A1 " + plusminus +  TitanBox.formatCommas(finalperTick) + " J/t");


        powerGen = TitanBox.addLore(powerGen, lore);
        myInv.setItem(0 , powerGen);

        ItemStack stats = SlimefunItems.COAL_GENERATOR.clone();
        stats = TitanBox.clearLore(stats);
        stats = TitanBox.changeName(stats, "Statistics");
        lore = new ArrayList<String>();

        lore.add(ChatColor.DARK_GRAY + "\u21E8 Generation: "+ ChatColor.YELLOW +"\u26A1 " + ChatColor.GREEN + TitanBox.formatCommas(check[7]) + " J/s");
        lore.add(ChatColor.DARK_GRAY + "\u21E8 Cosumption: "+ ChatColor.YELLOW +"\u26A1 " + ChatColor.GREEN + TitanBox.formatCommas(check[6]) + " J/s");

        stats = TitanBox.addLore(stats, lore);
        myInv.setItem(1 , stats);



        ItemStack battery = SlimefunItems.SMALL_CAPACITOR.clone();
        battery = TitanBox.clearLore(battery);
        battery = TitanBox.changeName(battery, "Energy Storage");
        lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_GRAY + "\u21E8 Capacitors: "+ ChatColor.YELLOW +"\u26A1 " + ChatColor.GREEN + TitanBox.formatCommas(check[2]) + "/" + TitanBox.formatCommas(check[3])  +" J");
        lore.add(ChatColor.DARK_GRAY + "\u21E8 Machines: "+ ChatColor.YELLOW +"\u26A1 " + ChatColor.GREEN + TitanBox.formatCommas(check[4]) + "/" + TitanBox.formatCommas(check[5])  +" J");
        lore.add(ChatColor.DARK_GRAY + "\u21E8 Buffer: "+ ChatColor.YELLOW +"\u26A1 " + ChatColor.GREEN + TitanBox.formatCommas(check[0]) + "/" + TitanBox.formatCommas(check[1])  +" J");
        battery = TitanBox.addLore(battery, lore);

        myInv.setItem(2 , battery);
        try {
            player.updateInventory();
        }
        catch (Exception e)
        {

        }
    }
}

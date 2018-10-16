package com.firesoftitan.play.titanbox.shops;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.buttonEnum;
import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import com.firesoftitan.play.titanbox.guis.mainGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerShopHolder {

    private int max = 3;
    private UUID owner;
    private String ownername;
    private HashMap<String, ShopHolder> idList = new HashMap<String, ShopHolder>();
    private mainGUI gui;
    public PlayerShopHolder(UUID owner, String ownername)
    {
        this.owner = owner;
        this.ownername = ownername;
    }

    public mainGUI getGui() {
        return gui;
    }
    public void save()
    {
        MainShops.shopsSettingsSQL.setDataField("id", owner.toString());
        MainShops.shopsSettingsSQL.setDataField("playername", ownername);
        MainShops.shopsSettingsSQL.setDataField("max", max);
        MainShops.shopsSettingsSQL.insertData();
    }
    public void openShop(ShopHolder shopToView)
    {
        Player player = Bukkit.getPlayer(owner);
        gui = new mainGUI(54, "Titan Shop: " + shopToView.getName(), MainShops.instance);
        for (int i = 0; i < 54; i++)
        {
            buttonGUIs newButton = new buttonGUIs(gui, i);
            gui.addButton(newButton, i);
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        for(int i = 0; i<shopToView.getSize();i++)
        {
            ItemStack item = shopToView.getItem(i);
            List<String> lore = item.getItemMeta().getLore();
            if (lore == null)
            {
                lore = new ArrayList<String>();
            }
            long invCount = TitanBox.getItemCount(shopToView.getOwner(), item);
            String textCount = invCount + "";

            if (invCount < 0)
            {
                textCount = "Infinite";
            }
            else
            {
                textCount = TitanBox.formatCommas(invCount);
            }
            lore.add(ChatColor.GRAY + "---Shop Info---");
            if (shopToView.getPricesSelling(i) > 0) lore.add(ChatColor.GRAY + "(Left Click)*1 "+ ChatColor.GREEN + "Buy: " + formatter.format(shopToView.getPricesSelling(i)));
            if (shopToView.getPricesSelling(i) > 0) lore.add(ChatColor.GRAY + "(Left Click + Shift)*64 "+ ChatColor.GREEN + "Buy: " + formatter.format(shopToView.getPricesSelling(i) *64));
            if (shopToView.getPricesBuying(i) > 0) lore.add(ChatColor.GRAY + "(Right Click)*1 "+ ChatColor.RED + "Sell: " + formatter.format(shopToView.getPricesBuying(i)));
            if (shopToView.getPricesBuying(i) > 0) lore.add(ChatColor.GRAY + "(Right Click + Shift)*64 "+ ChatColor.RED + "Sell: " +  formatter.format(shopToView.getPricesBuying(i)*64));
            if (shopToView.getInfo(i).length() > 0) lore.add(ChatColor.translateAlternateColorCodes('&', shopToView.getInfo(i)));
            lore.add(ChatColor.WHITE + "Inventory: " + ChatColor.GREEN + textCount);
            buttonGUIs tmpButton = gui.getButton(i);
            tmpButton.setMaterialTrue(item.clone());
            if (item.getItemMeta().hasDisplayName()) tmpButton.setNameTrue(item.getItemMeta().getDisplayName());
            if (!item.getItemMeta().hasDisplayName()) tmpButton.setNameTrue(ChatColor.YELLOW + "Item: " + ChatColor.WHITE + item.getType().name());
            tmpButton.setLore(lore);
            tmpButton.setToggle(buttonEnum.TRUE);
        }
        gui.showGUI(player);
    }
    public UUID getOwner() {
        return owner;
    }

    public String getOwnername() {
        return ownername;
    }
    public int getSize()
    {
        return idList.size();
    }
    public PlayerShopHolder(int max)
    {
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public HashMap<String, ShopHolder> getIdList() {
        return idList;
    }
    public void addShop(ShopHolder shopHolder)
    {
        idList.put(shopHolder.getName(), shopHolder);
    }
    public void removeShop(ShopHolder shopHolder)
    {
        idList.remove(shopHolder.getName());
    }
}

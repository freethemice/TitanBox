package com.firesoftitan.play.titanbox.shops;

import com.firesoftitan.play.titanbox.TitanBox;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShopHolder {

    private UUID owner;
    private String ownername;
    private String name;
    private List<ItemStack> itmes;
    private List<Double> pricesselling;
    private List<Double> pricesbuying;
    private List<String> info;

    public ShopHolder(UUID owner, String name, String ownername)
    {
        this.ownername = ownername;
        this.owner = owner;
        this.name = name;
        this.itmes = new ArrayList<ItemStack>();
        this.pricesselling = new ArrayList<Double>();
        this.pricesbuying = new ArrayList<Double>();
        this.info = new ArrayList<String>();
    }
    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isAdmin()
    {
        if (this.ownername.equals("Admin Shop"))
        {
            return true;
        }
        return false;
    }
    public void setAdmin()
    {
        this.ownername = "Admin Shop";
        save();
    }
    public void removeAdmin()
    {
        try {
            this.ownername = Bukkit.getOfflinePlayer(this.owner).getName();
        } catch (Exception e) {
            this.ownername = "NA";
        }
    }
    public List<ItemStack> searchItem(String search)
    {
        List<ItemStack> itemStacks = new ArrayList<ItemStack>();
        for(int i = 0; i < itmes.size(); i++)
        {
            ItemStack cloned = itmes.get(i).clone();
            if (TitanBox.getName(cloned).toLowerCase().contains(search.toLowerCase()))
            {
                itemStacks.add(cloned);
            }
        }
        return itemStacks;
    }
    public ItemStack getItem(int slot)
    {
        ItemStack cloned = itmes.get(slot).clone();
        cloned.setAmount(1);
        return cloned;
    }
    public void removeItem(int slot)
    {
        this.itmes.remove(slot);
        this.pricesbuying.remove(slot);
        this.pricesselling.remove(slot);
        this.info.remove(slot);
        save();
    }
    public void  addItem(ItemStack itemStack, double priceselling, double pricebuyin, String info)
    {
        itmes.add(itemStack);
        this.pricesselling.add(priceselling);
        this.pricesbuying.add(pricebuyin);
        this.info.add(info);
        save();
    }
    public void delete()
    {
        MainShops.shopsSQL.delete("id", name);
    }
    public void save()
    {
        List<String> saveSell = new ArrayList<String>();
        List<String> saveBuy = new ArrayList<String>();
        for (int i = 0; i < pricesselling.size(); i++)
        {
            saveSell.add(pricesselling.get(i)  + "");
            saveBuy.add(pricesbuying.get(i)  + "");
        }
        MainShops.shopsSQL.setDataField("id", name);
        MainShops.shopsSQL.setDataField("owner", owner);
        MainShops.shopsSQL.setDataField("playername", ownername);
        MainShops.shopsSQL.setDataField("items", itmes);
        MainShops.shopsSQL.setDataField("priceselling", saveSell);
        MainShops.shopsSQL.setDataField("pricesbuying", saveBuy);
        MainShops.shopsSQL.setDataField("info", info);
        MainShops.shopsSQL.insertData();
    }
    public double getPricesSelling(int slot)
    {
        return pricesselling.get(slot);
    }
    public double getPricesBuying(int slot)
    {
        return pricesbuying.get(slot);
    }
    public String getInfo(int slot)
    {
        return info.get(slot);
    }
    public int getSize()
    {
        return itmes.size();
    }
    public void setInfo(List<String> info) {
        this.info = info;
    }

    public void setItmes(List<ItemStack> itmes) {
        this.itmes = itmes;
    }

    public void setPricesSelling(List<Double> prices) {
        this.pricesselling = prices;
    }
    public void setPricesBuying(List<Double> prices) {
        this.pricesbuying = prices;
    }
}

package com.firesoftitan.play.titanbox.shops;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.guis.buttonGUIs;
import com.firesoftitan.play.titanbox.interfaces.InventoryHolder;
import com.firesoftitan.play.titansql.CallbackResults;
import com.firesoftitan.play.titansql.DataTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.Table;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainShops implements InventoryHolder {

    public HashMap<String, ShopHolder> allShopsByIDS = new HashMap<String, ShopHolder>();
    public HashMap<UUID, PlayerShopHolder> allShopsByOwners = new HashMap<UUID, PlayerShopHolder>();
    public static HashMap<UUID, Long> clicks = new HashMap<UUID, Long>();
    public static Table shopsSQL = new Table("tb_shops");
    public static Table shopsSettingsSQL = new Table("tb_shops_settings");
    public static MainShops instance;
    public MainShops()
    {
        instance = this;
        shopsSettingsSQL.addDataType("id", DataTypeEnum.CHARARRAY, true, false, true);
        shopsSettingsSQL.addDataType("playername", DataTypeEnum.CHARARRAY, false, false, false);
        shopsSettingsSQL.addDataType("max", DataTypeEnum.INTEGER, false, false, false);
        shopsSettingsSQL.createTable();

        shopsSQL.addDataType("id", DataTypeEnum.CHARARRAY, true, false, true);
        shopsSQL.addDataType("playername", DataTypeEnum.STRING, false, false, false);
        shopsSQL.addDataType("owner", DataTypeEnum.UUID, false, false, false);
        shopsSQL.addDataType("items", DataTypeEnum.ITEMLIST, false, false, false);
        shopsSQL.addDataType("priceselling", DataTypeEnum.STRINGLIST, false, false, false);
        shopsSQL.addDataType("pricesbuying", DataTypeEnum.STRINGLIST, false, false, false);
        shopsSQL.addDataType("info", DataTypeEnum.STRINGLIST, false, false, false);
        shopsSQL.createTable();

        shopsSettingsSQL.search(new CallbackResults() {
            @Override
            public void onResult(List<HashMap<String, ResultData>> results) {
                for(HashMap<String, ResultData> result: results)
                {
                    UUID uuid = UUID.fromString(result.get("id").getString());
                    String playername = result.get("playername").getString();
                    int max = result.get("max").getInteger();
                    PlayerShopHolder tmpPH = allShopsByOwners.get(uuid);
                    if (tmpPH == null)
                    {
                        tmpPH = new PlayerShopHolder(uuid, playername);
                        tmpPH.setMax(max);
                        tmpPH.save();
                    }
                    allShopsByOwners.put(uuid, tmpPH);
                }

                shopsSQL.search(new CallbackResults() {
                    @Override
                    public void onResult(List<HashMap<String, ResultData>> results) {
                        for(HashMap<String, ResultData> result: results)
                        {
                            String name = result.get("id").getString();
                            String playername = result.get("playername").getString();
                            UUID uuid = result.get("owner").getUUID();
                            List<ItemStack> itemStacks = result.get("items").getItemList();
                            List<String>  price = result.get("priceselling").getStringList();
                            List<String>  priceb = result.get("pricesbuying").getStringList();

                            List<String> info = result.get("info").getStringList();
                            ShopHolder tmp = new ShopHolder(uuid, name, playername);
                            if (info != null) {
                                tmp.setInfo(info);
                            }
                            if (price != null) {
                                List<Double> price2 = new ArrayList<Double>();
                                for (String prices : price) {
                                    price2.add(Double.valueOf(prices));
                                }
                                tmp.setPricesSelling(price2);
                            }
                            if (priceb != null) {
                                List<Double> priceb2 = new ArrayList<Double>();
                                for (String prices : priceb) {
                                    priceb2.add(Double.valueOf(prices));
                                }
                                tmp.setPricesBuying(priceb2);
                            }
                            if (itemStacks  != null) {
                                tmp.setItmes(itemStacks);
                            }
                            allShopsByIDS.put(name, tmp);
                            PlayerShopHolder tmpPH = allShopsByOwners.get(uuid);
                            if (tmpPH == null)
                            {
                                tmpPH = new PlayerShopHolder(uuid, playername);
                                tmpPH.save();
                            }
                            tmpPH.addShop(tmp);
                            allShopsByOwners.put(uuid, tmpPH);
                        }
                    }
                });
            }
        });


    }
    public void openShop(Player player, String shopname)
    {
        PlayerShopHolder tmpPH = allShopsByOwners.get(player.getUniqueId());
        if (tmpPH == null)
        {
            tmpPH = new PlayerShopHolder(player.getUniqueId(), player.getName());
            tmpPH.save();
        }
        ShopHolder shopHolder = allShopsByIDS.get(shopname);
        if (shopHolder == null)
        {
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "There is no shop with that name");
            return;
        }
        tmpPH.openShop(shopHolder);
    }
    public void makeAdminShop(Player player, String name)
    {
        if (player.hasPermission("titanbox.admin") || player.isOp()) {
            ShopHolder admining = allShopsByIDS.get(name);
            if (admining != null) {
                if (!admining.isAdmin()) {
                    admining.setAdmin();
                    player.sendMessage(ChatColor.GRAY + "[TitanBox]: " + "Shop is now admin shop!");
                } else {
                    admining.removeAdmin();
                    player.sendMessage(ChatColor.GRAY + "[TitanBox]: " + "Shop is no longer a admin shop!");
                }
                admining.save();
            }
        }
    }
    public void deleteShop(Player player, String name)
    {
        ShopHolder tmp = this.allShopsByIDS.get(name);
        if (tmp == null)
        {
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "No shop buy that name.");
            return;
        }
        if (!player.hasPermission("titanbox.admin") && !player.isOp()) {
            if (!tmp.getOwner().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "You are not the owner of the shop.");
                return;
            }
        }
        PlayerShopHolder tmpPH = allShopsByOwners.get(player.getUniqueId());
        if (tmpPH == null)
        {
            tmpPH = new PlayerShopHolder(player.getUniqueId(), player.getName());
            tmpPH.save();
        }
        tmp.delete();
        tmpPH.removeShop(tmp);
        allShopsByIDS.remove(name);


    }
    public void createNewShop(Player player, String name)
    {
        if (allShopsByIDS.containsKey(name))
        {
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "There is already a shop with that name");
            return;
        }
        PlayerShopHolder tmpPH = allShopsByOwners.get(player.getUniqueId());
        if (tmpPH == null)
        {
            tmpPH = new PlayerShopHolder(player.getUniqueId(), player.getName());
            tmpPH.save();
        }
        if (tmpPH.getSize() < tmpPH.getMax()) {
            ShopHolder tmp = new ShopHolder(player.getUniqueId(), name, player.getName());
            tmpPH.addShop(tmp);
            allShopsByIDS.put(name, tmp);
            allShopsByOwners.put(player.getUniqueId(), tmpPH);
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Shop added " + tmpPH.getSize() + "/" + tmpPH.getMax());
            player.sendMessage(ChatColor.GRAY + "[TitanBox]: " + "Shop won't save until you add your first item.");
        }
        else
        {
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "You can't add any more shops your att the max: " + tmpPH.getSize() + "/" + tmpPH.getMax());
        }

    }
    public void increaseMax(Player player, int amount)
    {
        PlayerShopHolder tmpPH = allShopsByOwners.get(player.getUniqueId());
        if (tmpPH == null)
        {
            tmpPH = new PlayerShopHolder(player.getUniqueId(), player.getName());
        }
        tmpPH.setMax(tmpPH.getMax() + amount);
        tmpPH.save();
    }

    public static void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        String shopName = "";
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (inventory.getTitle().startsWith("Titan Shop: "))
        {
            if (clicks.containsKey(player.getUniqueId()))
            {
                Long lastTime = clicks.get(player.getUniqueId());
                if (System.currentTimeMillis() - lastTime < 250)
                {
                    event.setCancelled(true);
                    return;
                }
            }

            clicks.put(player.getUniqueId(), System.currentTimeMillis());
            shopName = inventory.getTitle().replaceFirst("Titan Shop: ", "");
            ShopHolder shop = MainShops.instance.allShopsByIDS.get(shopName);
            if (shop != null)
            {
                try {

                    if (slot > -1 && slot < 54) {
                        if (shop.getSize() > slot) {
                            if (!player.getUniqueId().equals(shop.getOwner())) {
                                switch (event.getClick()) {
                                    case LEFT:
                                        selltoPlayer(player, slot, shop, 1);
                                        break;
                                    case RIGHT:
                                        buyfromPlayer(player, slot, shop, 1);
                                        break;
                                    case SHIFT_LEFT:
                                        selltoPlayer(player, slot, shop, 64);
                                        break;
                                    case SHIFT_RIGHT:
                                        buyfromPlayer(player, slot, shop, 64);
                                        break;
                                }
                            } else {
                                //they can edit
                                shop.removeItem(slot);
                                MainShops.instance.openShop((Player) player,shop.getName());
                            }
                        }
                    }
                    else
                    {
                         if (player.getUniqueId().equals(shop.getOwner()))
                         {
                             //they can edit
                             ItemStack find = event.getClickedInventory().getItem(event.getSlot());
                             boolean admineMode = shop.isAdmin();
                             if (!TitanBox.isEmpty(find))
                             {
                                 find = find.clone();
                                 find.setAmount(1);
                                 if (admineMode || TitanBox.hasItem(player.getUniqueId(), find, false))
                                 {
                                     ItemStack finalFind = find.clone();
                                     player.closeInventory();
                                     boolean finalAdmineMode = admineMode;
                                     new AnvilGUI(TitanBox.instants, player, "Selling price for 1?", (playera, reply) -> {
                                         try {
                                             double sellingp = Double.valueOf(reply);
                                             new AnvilGUI(TitanBox.instants, player, "Buying price for 1?", (playerb, replyb) -> {
                                                 try {
                                                     double buyingp = Double.valueOf(replyb);

                                                     new AnvilGUI(TitanBox.instants, player, "Any Message?", (playerc, replyc) -> {
                                                         try {
                                                             replyc = replyc.replace("Admin", "");
                                                             replyc = replyc.replace("admin", "");
                                                             replyc = replyc.replace("Shop", "");
                                                             replyc = replyc.replace("shop", "");
                                                             if (finalAdmineMode)
                                                             {
                                                                replyc = "Admin Shop";
                                                             }
                                                             shop.addItem(finalFind.clone(), sellingp, buyingp, replyc);
                                                             MainShops.instance.openShop((Player) player,shop.getName());
                                                         } catch (NumberFormatException e) {
                                                             MainShops.instance.openShop((Player) player,shop.getName());
                                                         } finally {
                                                             return "got it";

                                                         }});
                                                 } catch (NumberFormatException e) {
                                                     MainShops.instance.openShop((Player) player,shop.getName());
                                                 } finally {
                                                     return "got it";

                                                 }});
                                         } catch (NumberFormatException e) {
                                             MainShops.instance.openShop((Player) player,shop.getName());
                                         } finally {
                                             return "got it";

                                         }});
                                 }
                                 else {
                                     player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Only Items In Your Storage Units can be Sold");
                                 }

                             }


                         }
                    }
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                    event.setCancelled(true);
                }

            }
        }
    }
    private static void buyfromPlayer(Player player, int slot, ShopHolder shop, int amount) {
        double price = shop.getPricesBuying(slot) * amount;
        boolean adminshop = shop.isAdmin();;
        OfflinePlayer ownerOFP  = Bukkit.getOfflinePlayer(shop.getOwner());
        if (TitanBox.instants.economy.getBalance(ownerOFP) >= price || adminshop)
        {
            ItemStack check = shop.getItem(slot);
            PlayerInventory playerInventory = player.getInventory();
            for(int i = 0; i < 36; i++)
            {
                if (!TitanBox.isEmpty(playerInventory.getItem(i))) {
                    ItemStack pcheck = playerInventory.getItem(i).clone();
                    if (TitanBox.isItemEqual(pcheck, check)) {
                        if (pcheck.getAmount() > amount) {
                            pcheck.setAmount(pcheck.getAmount() - amount);
                            playerInventory.setItem(i, pcheck.clone());
                            player.updateInventory();
                            pcheck.setAmount(amount);
                            if (!adminshop) {
                                TitanBox.addItemToStorage(shop.getOwner(), pcheck.clone());
                                TitanBox.instants.economy.withdrawPlayer(ownerOFP, price);
                            }
                            TitanBox.instants.economy.depositPlayer(player, price);
                            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Thank you for your business.");
                            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.WHITE + ownerOFP.getName() + ChatColor.GREEN + " deposit " + ChatColor.RED + price);
                            if (!adminshop && Bukkit.getPlayer(shop.getOwner()) != null) {
                                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                Player ownerOL = Bukkit.getPlayer(shop.getOwner());
                                ownerOL.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "You just bought: " + ChatColor.BLUE + amount + "x" + TitanBox.getName(shop.getItem(slot)) + ChatColor.GREEN + " for " + ChatColor.WHITE + formatter.format(price));
                            }
                            return;
                        } else if (pcheck.getAmount() == amount) {
                            playerInventory.setItem(i, null);
                            player.updateInventory();
                            pcheck.setAmount(amount);
                            if (!adminshop) {
                                TitanBox.addItemToStorage(shop.getOwner(), pcheck.clone());
                                TitanBox.instants.economy.withdrawPlayer(ownerOFP, price);
                            }
                            TitanBox.instants.economy.depositPlayer(player, price);
                            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Thank you for your business.");
                            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.WHITE + ownerOFP.getName() + ChatColor.GREEN + " deposit " + ChatColor.RED + price);
                            if (!adminshop && Bukkit.getPlayer(shop.getOwner()) != null) {
                                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                                Player ownerOL = Bukkit.getPlayer(shop.getOwner());
                                ownerOL.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "You just bought: " + ChatColor.BLUE + amount + "x" + TitanBox.getName(shop.getItem(slot)) + ChatColor.GREEN + " for " + ChatColor.WHITE + formatter.format(price));
                            }
                            return;
                        }
                    }
                }
            }
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "You don't have that much of that item!");
        }
        else
        {
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Shop owner doesn't have enough money!");
        }
    }
    private static void selltoPlayer(Player player, int slot, ShopHolder shop, int amount) {
        double price = shop.getPricesSelling(slot) * amount;
        boolean adminshop = shop.isAdmin();
        OfflinePlayer ownerOFP  = Bukkit.getOfflinePlayer(shop.getOwner());
        if (TitanBox.instants.economy.getBalance(player) >= price)
        {
            ItemStack toGive =  null;
            if (!adminshop) {
                toGive = TitanBox.getItemByPassPower(shop.getOwner(), shop.getItem(slot), amount);

            }
            else
            {
                toGive = shop.getItem(slot).clone();
                toGive.setAmount(amount);
            }
            if (!TitanBox.isEmpty(toGive))
            {
                if (toGive.getAmount() != amount)
                {
                    amount = toGive.getAmount();
                    price = shop.getPricesSelling(slot) * amount;
                }
                TitanBox.instants.economy.withdrawPlayer(player, price);
                if (!adminshop) TitanBox.instants.economy.depositPlayer(ownerOFP, price);

                toGive = TitanBox.addItemsToPlayer(player, toGive.clone());
                if (!TitanBox.isEmpty(toGive)) {
                    player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Your stuff dropped on the ground.");
                    player.getWorld().dropItem(player.getLocation(), toGive.clone());
                }
                player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Thank you for your business.");
                player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.WHITE + ownerOFP.getName() + ChatColor.GREEN + " withdraw " + ChatColor.RED + price);
                if (!adminshop && Bukkit.getPlayer(shop.getOwner()) != null)
                {
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    Player ownerOL = Bukkit.getPlayer(shop.getOwner());
                    ownerOL.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "You just sold: " + ChatColor.BLUE + amount + "x" + TitanBox.getName(shop.getItem(slot)) + ChatColor.GREEN + " for " + ChatColor.WHITE + formatter.format(price));
                }
            }
            else
            {
                player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Owner Doesn't have enough of that item!");
            }
        }
        else
        {
            player.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "You don't have enough money!");
        }
    }

    @Override
    public void onInventoryClickEvent(InventoryClickEvent event, buttonGUIs button) {
        //not used
    }
}

package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.fotxray.FoTXRay;
import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.holders.SlimefunItemsHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class MainRunnable implements Runnable {
    @Override
    public void run() {
        for(Player p: Bukkit.getOnlinePlayers()) {
            checkSFHelment(p);
        }

        ReBirth();

    }
    public  static void  checkSFHelment(Player player)
    {
        ItemStack helment = player.getInventory().getHelmet();
        if (helment != null) {

            if (helment.getItemMeta() != null) {
                if (helment.getType() == Material.LEATHER_HELMET && helment.getItemMeta().getDisplayName().equals(SlimefunItemsHolder.X_RAY_HELMEY.getItemMeta().getDisplayName())) {
                    if (!FoTXRay.xraying.containsKey(player.getUniqueId())) {
                        try {
                            FoTXRay.sendXray(player);
                            com.firesoftitan.play.fotxray.PlayerData info = FoTXRay.xraying.get(player.getUniqueId());
                            info.setCommandUsed(false);
                            FoTXRay.xraying.put(player.getUniqueId(), info);
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
                else
                {
                    if (FoTXRay.xraying.containsKey(player.getUniqueId())) {
                        com.firesoftitan.play.fotxray.PlayerData info = FoTXRay.xraying.get(player.getUniqueId());
                        if (!info.isCommandUsed())
                        {
                            FoTXRay.stopXraying(player);
                        }
                    }
                }
            }
            else
            {
                if (FoTXRay.xraying.containsKey(player.getUniqueId())) {
                    com.firesoftitan.play.fotxray.PlayerData info = FoTXRay.xraying.get(player.getUniqueId());
                    if (!info.isCommandUsed())
                    {
                        FoTXRay.stopXraying(player);
                    }
                }
            }
        }
        else
        {
            if (FoTXRay.xraying.containsKey(player.getUniqueId())) {
                com.firesoftitan.play.fotxray.PlayerData info = FoTXRay.xraying.get(player.getUniqueId());
                if (!info.isCommandUsed())
                {
                    FoTXRay.stopXraying(player);
                }
            }
        }
    }
    private void ReBirth()
    {
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


    }
}

package com.firesoftitan.play.titanbox.items;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.custom.CustomCategories;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class TitanTalisman extends SlimefunItem {

    boolean consumed;
    boolean cancel;
    PotionEffect[] effects;
    String suffix;
    int chance;
    private static BukkitRunnable checkTimer = null;

    public TitanTalisman(ItemStack item, String name, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, PotionEffect... effects) {
        super(CustomCategories.SLIMEFUN_PARTS, item, name, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItem(item, consumable ? 4: 1));
        this.consumed = consumable;
        this.cancel = cancelEvent;
        this.suffix = messageSuffix;
        this.effects = effects;
        this.chance = 100;
        greatCheckTimer();
    }

    public TitanTalisman(ItemStack item, String name, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, int chance, PotionEffect... effects) {
        super(CustomCategories.SLIMEFUN_PARTS, item, name, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItem(item, consumable ? 4: 1));
        this.consumed = consumable;
        this.cancel = cancelEvent;
        this.suffix = messageSuffix;
        this.effects = effects;
        this.chance = chance;
        greatCheckTimer();
    }

    public TitanTalisman(ItemStack item, String name, ItemStack[] recipe, String messageSuffix, int chance, PotionEffect... effects) {
        super(CustomCategories.SLIMEFUN_PARTS, item, name, RecipeType.MAGIC_WORKBENCH, recipe, item);
        this.consumed = true;
        this.cancel = true;
        this.suffix = messageSuffix;
        this.effects = effects;
        this.chance = chance;
        greatCheckTimer();
    }
    private void greatCheckTimer()
    {
        if (checkTimer != null) return;
        checkTimer = new BukkitRunnable()
        {

            @Override
            public void run() {
                try {
                    for(Player player: Bukkit.getOnlinePlayers())
                    {
                        HashMap<Integer,ItemStack> findTally = TitanTalisman.checkFor(player, SlimefunItem.getByID("TALISMAN_VOID"));
                        if (findTally != null)
                        {
                            if (findTally.size() > 0)
                            {
                                Inventory playerinv =  player.getInventory();
                                for(int i = 0; i < 36; i++)
                                {
                                    ItemStack item = playerinv.getItem(i);
                                    if (!Utilities.isEmpty(item)) {
                                        ItemStack leftOver = Utilities.addItemToStorage(player.getUniqueId(), item);
                                        if (leftOver != null) {
                                            if (leftOver.getAmount() != playerinv.getItem(i).getAmount()) {
                                                playerinv.setItem(i, leftOver.clone());
                                            }
                                        } else {
                                            playerinv.setItem(i, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        };
        checkTimer.runTaskTimer(TitanBox.instants, 5*20, 5*20);
    }

    public PotionEffect[] getEffects()	{		return this.effects;	}
    public boolean isConsumable()	 	{		return this.consumed;	}
    public boolean isEventCancelled() 	{		return this.cancel;		}
    public String getSuffix() 			{		return this.suffix;		}
    public int getChance()				{		return this.chance;		}

    public static HashMap<Integer,ItemStack>  checkFor(Player p, SlimefunItem talisman) {
        if (talisman != null) {
            if (talisman instanceof TitanTalisman) {
                boolean message = ((TitanTalisman) talisman).getSuffix().equalsIgnoreCase("") ? false: true;
                if (SlimefunStartup.chance(100, ((TitanTalisman) talisman).getChance())) {

                    boolean pass = true;

                    for (PotionEffect effect: ((TitanTalisman) talisman).getEffects()) {
                        if (effect != null) if (p.hasPotionEffect(effect.getType())) pass = false;
                    }

                    if (pass) {
                        HashMap<Integer,ItemStack> tmpY = new HashMap<Integer, ItemStack>();

                        for (int i = 0; i < p.getInventory().getSize(); i++)
                        {
                            if (Utilities.isItemEqual(p.getInventory().getItem(i), talisman.getItem()))
                            {
                                if (Slimefun.hasUnlocked(p, talisman.getItem(), true)) {
                                    tmpY.put(i, p.getInventory().getItem(i));
                                }
                            }
                        }
                        if (tmpY.size() > 0) {
                            return (HashMap<Integer,ItemStack>)tmpY.clone();
                        }
                        else return null;
                    }
                    else return null;
                }
                else return null;
            }
            else return null;
        }
        else return null;
    }
}

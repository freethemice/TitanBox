package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.Utilities;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Random;

public class BarcodeManager
{
    private static Config barcodes = new Config("data-storage" + File.separator + "TitanBox" + File.separator  + "barcodes.yml");
    public static void save()
    {
        barcodes.save();
    }
    public static boolean hasBarcode(ItemStack toBarcode)
    {
        if (!Utilities.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = Utilities.getName(toBarcode);
                    String barcode = BarcodeManager.getBarcode(toBarcode);
                    if (barcodes.contains(name + "." + barcode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void setBarcodeTrue(ItemStack toBarcode, Player player)
    {

        if (!Utilities.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = Utilities.getName(toBarcode);
                    String barcode = BarcodeManager.getBarcode(toBarcode);
                    List<String> check = toBarcode.getItemMeta().getLore();
                    int line = 0;
                    if (barcodes.contains(name + "." + barcode)) {
                        barcodes.setValue(name + "." + barcode, true);
                        barcodes.setValue(name + ".info." + barcode + ".time" , System.currentTimeMillis());
                        barcodes.setValue(name + ".info." + barcode + ".user" , player.getName());
                        barcodes.setValue(name + ".info." + barcode + ".item" , toBarcode.getItemMeta().getDisplayName());
                        for(String s: check) {
                            barcodes.setValue(name + ".info." + barcode + ".line" + line, s);
                            line++;
                        }
                    }
                }
            }
        }
    }
    public static String getBarcode(ItemStack toBarcode)
    {
        if (!Utilities.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = Utilities.getName(toBarcode);
                    if(Utilities.hasNBTTag(toBarcode, "barcode"))
                    {
                        NBTTagCompound nbtTagCompound =  Utilities.getNBTTag(toBarcode);
                        String barcode = nbtTagCompound.getString("barcode");
                        return barcode;
                    }

                    List<String> check = toBarcode.getItemMeta().getLore();
                    for(String s: check)
                    {
                        if (s.startsWith(ChatColor.MAGIC + "barcode:"))
                        {
                            String saltStr = s.replace(ChatColor.MAGIC + "barcode:", "");
                            return saltStr;
                        }
                    }
                }
            }
        }
        return null;
    }
    public static String scanBarcode(ItemStack toBarcode)
    {
        if (!Utilities.isEmpty(toBarcode)) {
            if (toBarcode.hasItemMeta()) {
                if (toBarcode.getItemMeta().hasLore()) {
                    String name = Utilities.getName(toBarcode);
                    String barcode = getBarcode(toBarcode);
                    if (barcodes.contains(name + "." + barcode)) {
                        return barcodes.getBoolean(name + "." + barcode) + "";
                    }
                }
            }
        }
        return null;
    }
    public static ItemStack getNewBarcode(ItemStack toBarcode)
    {
        if (!Utilities.isEmpty(toBarcode)) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random(System.currentTimeMillis());
            while (salt.length() < 36) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();

            String name = Utilities.getName(toBarcode);
            if (barcodes.contains(name + "." + saltStr)) {
                return getNewBarcode(toBarcode);
            }
            barcodes.setValue(name + "." + saltStr, false);
            NBTTagCompound nbtTagCompound = Utilities.getNBTTag(toBarcode);
            nbtTagCompound.setString("barcode", saltStr);
            toBarcode = Utilities.setNBTTag(toBarcode, nbtTagCompound);
        }
        return toBarcode;
    }
}

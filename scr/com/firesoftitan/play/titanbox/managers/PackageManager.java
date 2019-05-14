package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public class PackageManager {
    protected String myId = "";
    protected UUID from;
    protected ItemStack itemStack;
    protected long sent;
    protected UUID to;
    protected boolean deliveryMessage;
    protected boolean needSaving = false;
    public static String getNewIDString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random(System.currentTimeMillis());
        while (salt.length() < 36) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        if (TitanBox.packagesIDS.contains(saltStr))
        {
            return getNewIDString();
        }
        return saltStr;

    }

    public String getID() {
        return myId;
    }

    public PackageManager(UUID from, UUID to, ItemStack item, long sent, String myID)
    {
        this.from = from;
        this.to = to;
        this.itemStack = item.clone();
        this.sent = sent;
        this.deliveryMessage = false;
        this.myId = myID;
    }
    public void setNeedSaving()
    {
        needSaving = true;
    }
    public void save()
    {
        if (needSaving) {
            TitanBox.mailbox_SQL.setDataField("id", myId);
            TitanBox.mailbox_SQL.setDataField("topost", to);
            TitanBox.mailbox_SQL.setDataField("frompost", from);
            TitanBox.mailbox_SQL.setDataField("sentpost", sent);
            TitanBox.mailbox_SQL.setDataField("item", itemStack.clone());
            TitanBox.mailbox_SQL.setDataField("bulk", false);
            TitanBox.mailbox_SQL.insertData();
        }
        needSaving = false;
    }
    public boolean givenDeliveryMessage() {
        return deliveryMessage;
    }
    public void deliveryMessageGiven()
    {
        deliveryMessage = true;
    }
    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public long getSent() {
        return sent;
    }

    public UUID getFrom() {
        return from;
    }

    public UUID getTo() {
        return to;
    }
    public boolean isLetter()
    {
        if (Utilities.hasNBTTag(itemStack, "mailLetter"))
        {
            return true;
        }
        return false;
    }
    public String getTimeLeft()
    {
        if (isInRoute()) {
            long time = 6 * 60 * 1000 - (System.currentTimeMillis() - this.getSent());
            String lefy = Utilities.formatTime(time);
            return lefy;
        }
        return "0 Seconds";
    }
    public boolean isInRoute()
    {
        if (isLetter()) return false;
        if (System.currentTimeMillis() - this.getSent() > 6*60*1000)  return false;
        return true;
    }
}

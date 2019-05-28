package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.guis.MailboxGUI;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BulkPackageManager extends PackageManager {
    private List<UUID> received;
    private static List<BulkPackageManager> packages = new ArrayList<BulkPackageManager>();

    public static void checkUndeliveredPackages(Player player)
    {
        for(BulkPackageManager bulkPackageManager : packages)
        {
            if (!bulkPackageManager.hasReceived(player))
            {
                MailboxGUI.getMailBox(player).addPackage(bulkPackageManager.getFrom(), bulkPackageManager.getItemStack().clone());
                bulkPackageManager.playerReceived(player);
            }
        }
    }
    public static void addNewPackage(BulkPackageManager packageHolder)
    {
        packages.add(packageHolder);
    }
    public static void saveAll()
    {
        for(BulkPackageManager bulkPackageManager : packages)
        {
            bulkPackageManager.save();
        }
    }
    public BulkPackageManager(UUID from, UUID to, ItemStack item, long sent, String myId) {
        super(from, to, item, sent, myId);
        received = new ArrayList<UUID>();
    }
    public boolean hasReceived(Player player)
    {
        return hasReceived(player.getUniqueId());
    }


    public boolean hasReceived(UUID uuid)
    {
        return received.contains(uuid);
    }
    public void playerReceived(UUID uuid)
    {
        received.add(uuid);
        needSaving=true;
    }
    public void playerReceived(Player player)
    {
        playerReceived(player.getUniqueId());
    }
    @Override
    public void save()
    {
        if (needSaving) {
            TitanBox.mailbox_SQL.setDataField("id", this.myId);
            TitanBox.mailbox_SQL.setDataField("topost", this.to);
            TitanBox.mailbox_SQL.setDataField("frompost", this.from);
            TitanBox.mailbox_SQL.setDataField("sentpost", this.sent);
            TitanBox.mailbox_SQL.setDataField("item", this.itemStack.clone());
            TitanBox.mailbox_SQL.setDataField("bulk", true);
            List<String> list = new ArrayList<String>();
            for (UUID uuid : received) {
                list.add(uuid.toString());
            }
            TitanBox.mailbox_SQL.setDataField("received", list);
            TitanBox.mailbox_SQL.insertData();
        }
        needSaving = false;
    }

}

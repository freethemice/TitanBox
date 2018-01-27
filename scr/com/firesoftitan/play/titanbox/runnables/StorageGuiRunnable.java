package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.machines.StorageUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

import java.util.List;

public class StorageGuiRunnable implements Runnable {
    public int myID = -1;
    public StorageUnit myStorageUnit = null;
    @Override
    public void run() {
        if (myStorageUnit.getGui() != null) {
            List<HumanEntity> lookers = myStorageUnit.getGui().getMyGui().getViewers();
            boolean exit = true;
            for (HumanEntity he : lookers) {
                if (he.getUniqueId().toString().equals(myStorageUnit.getGui().getViewer().getUniqueId().toString())) {
                    exit = false;
                }
            }
            if (!exit) {
                myStorageUnit.reBuildMenu();
                myStorageUnit.reBuildSlots();
                myStorageUnit.getGui().getViewer().updateInventory();
            } else {
                Bukkit.getScheduler().cancelTask(myID);
            }
        }
        else {
            Bukkit.getScheduler().cancelTask(myID);
        }
    }
}

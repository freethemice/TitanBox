package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.machines.ItemRoutingRouter;
import org.bukkit.Bukkit;

public class GUIRouterRunnable implements Runnable {
    private ItemRoutingRouter itemRoutingRouter = null;
    private int timerID = - 1;
    private long closeTime = -1;

    @Override
    public void run() {
        if (itemRoutingRouter !=null) {
            if (itemRoutingRouter.getGui() != null) {
                if (itemRoutingRouter.getGui().getViewer() != null) {
                    closeTime = -1;
                    itemRoutingRouter.updateStatusButton();
                }
            }
            if (closeTime > 0) {
                if (System.currentTimeMillis() - closeTime > 30000) {
                    itemRoutingRouter.clearGui();
                    Bukkit.getScheduler().cancelTask(timerID);
                }
            }
            else
            {
                closeTime = System.currentTimeMillis();
            }
        }
    }

    public ItemRoutingRouter getItemRoutingRouter() {
        return itemRoutingRouter;
    }

    public void setItemRoutingRouter(ItemRoutingRouter myItemRoutingRouter) {
        this.itemRoutingRouter = myItemRoutingRouter;
    }

    public int getTimerID() {
        return timerID;
    }

    public void setTimerID(int timerID) {
        this.timerID = timerID;
    }
}

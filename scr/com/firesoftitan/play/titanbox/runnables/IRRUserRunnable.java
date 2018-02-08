package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import com.firesoftitan.play.titanbox.machines.ItemRoutingRouter;
import com.firesoftitan.play.titanbox.modules.MainModule;

import java.util.UUID;

public class IRRUserRunnable implements Runnable  {
    private ItemRoutingRouter itemRoutingRouter = null;
    private int timerID = - 1;

    @Override
    public void run() {
        TitanBox.instants.checkRegisterdPower();
        if (itemRoutingRouter !=null) {
            if (itemRoutingRouter.hasModules()) {
                itemRoutingRouter.setLastTick();
                UUID owner = itemRoutingRouter.getOwner();
                int size = Math.min(itemRoutingRouter.getModules().size(), RouterHolder.bufferSize);
                for (int i = 0; i < size; i++) {
                    MainModule mh = itemRoutingRouter.getNextBuffered();
                    if (mh != null) {
                        if (mh.isLoaded()) {
                            mh.runMe(owner);
                        }
                    }
                }
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

package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import com.firesoftitan.play.titanbox.machines.ItemRoutingRouter;
import com.firesoftitan.play.titanbox.modules.MainModule;

import java.util.UUID;

public class IRRUserRunnable implements Runnable  {
    private ItemRoutingRouter itemRoutingRouter = null;
    private int timerID = - 1;
    private long countdown = -1;

    @Override
    public void run() {
        TitanBox.instants.checkRegisterdPower();
        if (itemRoutingRouter !=null) {
            itemRoutingRouter.setLastTick();
            if (itemRoutingRouter.hasModules()) {
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
    public void startCountDown()
    {
        countdown = System.currentTimeMillis();
    }
    public int getCountDown()
    {
        if (countdown < 0)
        {
            return  -1;
        }
        long timeSecond = System.currentTimeMillis() - countdown;
        timeSecond = timeSecond / 1000;
        return (int) timeSecond;
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

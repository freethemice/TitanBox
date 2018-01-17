package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.holders.RouterHolder;

public class RouterRunable implements Runnable  {
    @Override
    public void run() {
        try {
            TitanBox.isRunning = System.currentTimeMillis();
            TitanBox.instants.checkRegisterdPower();
            RouterHolder.tick();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

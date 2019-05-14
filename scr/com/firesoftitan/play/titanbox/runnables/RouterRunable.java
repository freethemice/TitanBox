package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.managers.RouterManager;

public class RouterRunable implements Runnable  {
    @Override
    public void run() {
        try {
            TitanBox.isRunning = System.currentTimeMillis();
            TitanBox.instants.checkRegisterdPower();
            RouterManager.tick();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

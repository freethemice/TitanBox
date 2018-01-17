package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;

public class SaverRunable implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("[TitanBox]: Saving all, running: " + TitanBox.convertToTimePasted(TitanBox.isRunning));
            TitanBox.instants.saveEveryThing();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

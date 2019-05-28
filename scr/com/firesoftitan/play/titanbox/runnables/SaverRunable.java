package com.firesoftitan.play.titanbox.runnables;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.Utilities;

public class SaverRunable implements Runnable {
    @Override
    public void run() {

        try {
            System.out.println("[TitanBox]: Saving all, running: " + Utilities.convertToTimePasted(TitanBox.isRunning));
            TitanBox.instants.saveEveryThing();
            System.out.println("[TitanBox]: Saving all, Done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

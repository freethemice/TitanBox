package com.firesoftitan.play.titanbox.runnables;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public abstract class PickAPlayerRunnable extends BukkitRunnable {
    private UUID playerpicked;

    public UUID getPlayerPicked() {
        return playerpicked;
    }

    public void setPlayerPicked(UUID playerpicked) {
        this.playerpicked = playerpicked;
    }
}

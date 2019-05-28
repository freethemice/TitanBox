package com.firesoftitan.play.titanbox.runnables;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class ChatGetterRunnable extends BukkitRunnable {
    private String chat;

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}

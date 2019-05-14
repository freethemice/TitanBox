package com.firesoftitan.play.titanbox.listeners;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;


/**
 * Created by Daniel on 9/12/2017.
 */
public class XRayListener implements Listener {

    public XRayListener()
    {

    }
    public void registerEvents(){
        PluginManager pm = TitanBox.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanBox.instants);
    }
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        try {
            PlayerManager playerManager = PlayerManager.getPlayer(event.getPlayer());
            if (playerManager != null) {
                playerManager.stopXRay();
            }
        } catch (Exception e) {

        }

    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        PlayerManager playerManager = PlayerManager.getPlayer(event.getPlayer());
        if (playerManager != null) {
            playerManager.processXRay();
        }
    }
}

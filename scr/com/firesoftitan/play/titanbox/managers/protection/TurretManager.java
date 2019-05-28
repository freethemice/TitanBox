package com.firesoftitan.play.titanbox.managers.protection;

import org.bukkit.Location;

public class TurretManager {
    private Location location;
    public TurretManager(Location location)
    {
        this.location = location;
    }

    public Location getLocation() {
        return location.clone();
    }
}

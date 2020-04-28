package com.ghifari160.bedwarsplugin.world;

import org.bukkit.World;

public class BWWorld
{
    final World world;
    boolean persistent;

    public BWWorld(World world, boolean persistent)
    {
        this.world = world;
        this.persistent = persistent;
    }

    public World getWorld()
    {
        return world;
    }

    public void isPersistent(boolean persistence)
    {
        persistent = persistence;
    }

    public boolean isPersistent()
    {
        return persistent;
    }
}

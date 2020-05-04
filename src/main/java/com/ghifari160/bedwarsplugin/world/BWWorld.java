package com.ghifari160.bedwarsplugin.world;

import org.bukkit.World;

public class BWWorld
{
    private final World world;
    private boolean persistent;
    private boolean arena;

    public BWWorld(World world)
    {
        this(world, false);
    }

    public BWWorld(World world, boolean persistent)
    {
        this(world, persistent, false);
    }

    public BWWorld(World world, boolean persistent, boolean arena)
    {
        this.world = world;
        this.persistent = persistent;
        this.arena = arena;
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

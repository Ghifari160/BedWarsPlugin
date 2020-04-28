package com.ghifari160.bedwarsplugin.world;

import org.bukkit.World;

import java.util.Map;

public interface IWorldManager
{
    boolean unloadWorld(String name);

    boolean loadWorld(String name);
    boolean loadWorld(String name, boolean persistent);

    void setDefaultWorld(String name);
    World getDefaultWorld();

    Map<String, BWWorld> getLoadedWorlds();

    void start();
    void shutdown();
}

package com.ghifari160.bedwarsplugin.world;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WorldManager implements IWorldManager
{
    private final BedWarsPlugin plugin;
    private final Map<String, BWWorld> worlds;

    private String defaultWorld;

    public WorldManager(BedWarsPlugin core)
    {
        plugin = core;
        worlds = new HashMap<String, BWWorld>();
    }

    public boolean loadWorld(String name)
    {
        return loadWorld(name, false);
    }

    public boolean loadWorld(String name, boolean persistent)
    {
        WorldCreator creator = new WorldCreator(name);
        BWWorld world = new BWWorld(plugin.getServer().createWorld(creator), persistent);

        worlds.put(name, world);

        return true;
    }

    public boolean unloadWorld(String name)
    {
        if(worlds.containsKey(name))
        {
            removePlayersFromWorld(name);

            plugin.getServer().unloadWorld(worlds.get(name).getWorld(), worlds.get(name).isPersistent());
            worlds.remove(name);
        }

        return true;
    }

    void removePlayersFromWorld(String name)
    {
        if(worlds.containsKey(name))
        {
            Player[] players = new Player[worlds.get(name).getWorld().getPlayers().size()];
            worlds.get(name).getWorld().getPlayers().toArray(players);

            Location loc = getDefaultWorld().getSpawnLocation();

            for(Player player : players)
                player.teleport(loc);
        }
    }

    public void setDefaultWorld(String name)
    {
        if(worlds.containsKey(name))
        {
            defaultWorld = name;

            plugin.getLogger().info("Default world is set to \"" + name + "\"");
        }
    }

    public World getDefaultWorld()
    {
        if(worlds.containsKey(defaultWorld))
            return worlds.get(defaultWorld).getWorld();

        return null;
    }

    public Map<String, BWWorld> getLoadedWorlds()
    {
        return worlds;
    }

    public void start()
    {
        World[] serverWorlds = new World[plugin.getServer().getWorlds().size()];
        plugin.getServer().getWorlds().toArray(serverWorlds);

        for(World world : serverWorlds)
            loadWorld(world.getName());

        setDefaultWorld(serverWorlds[0].getName());
    }

    public void shutdown()
    {
        String[] worldsLoaded = new String[worlds.size()];
        worlds.keySet().toArray(worldsLoaded);

        for(String world : worldsLoaded)
        {
            if(!world.equals(defaultWorld))
                unloadWorld(world);
        }

        worlds.clear();
    }
}

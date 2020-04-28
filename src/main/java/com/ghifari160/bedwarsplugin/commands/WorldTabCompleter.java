package com.ghifari160.bedwarsplugin.commands;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import com.ghifari160.bedwarsplugin.world.IWorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class WorldTabCompleter implements TabCompleter
{
    private static final String[] PARAMS = { "load", "teleport", "tp", "unload" };

    private final BedWarsPlugin plugin;
    private final IWorldManager worldManager;

    public WorldTabCompleter(BedWarsPlugin core, IWorldManager manager)
    {
        plugin = core;
        worldManager = manager;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        final List<String> options = new ArrayList<String>();

        if(args.length < 2)
        {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(PARAMS), options);
        }
        else if(args[0].equalsIgnoreCase("load"))
        {
            options.add("");
        }
        else if(args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp"))
        {
            String[] loadedWorlds = new String[worldManager.getLoadedWorlds().keySet().size()];
            worldManager.getLoadedWorlds().keySet().toArray(loadedWorlds);

            if(args.length < 3)
                StringUtil.copyPartialMatches(args[1], Arrays.asList(loadedWorlds), options);
            else if(args.length < 4)
            {
                Player[] players = new Player[plugin.getServer().getOnlinePlayers().size()];
                plugin.getServer().getOnlinePlayers().toArray(players);

                List<String> playerNames = new ArrayList<String>();

                for(Player player : players)
                    playerNames.add(player.getName());

                StringUtil.copyPartialMatches(args[2], playerNames, options);
            }
        }
        else
        {
            String[] loadedWorlds = new String[worldManager.getLoadedWorlds().keySet().size()];
            worldManager.getLoadedWorlds().keySet().toArray(loadedWorlds);

            StringUtil.copyPartialMatches(args[1], Arrays.asList(loadedWorlds), options);
        }

        Collections.sort(options);

        return options;
    }
}

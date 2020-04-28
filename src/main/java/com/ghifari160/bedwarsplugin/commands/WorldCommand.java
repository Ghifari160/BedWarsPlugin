package com.ghifari160.bedwarsplugin.commands;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import com.ghifari160.bedwarsplugin.world.BWWorld;
import com.ghifari160.bedwarsplugin.world.IWorldManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Map;

public class WorldCommand implements CommandExecutor
{
    private final BedWarsPlugin plugin;
    private final IWorldManager manager;

    public WorldCommand(BedWarsPlugin core, IWorldManager worldManager)
    {
        plugin = core;
        manager = worldManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length < 1)
            return false;

        if(args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("unload")
            || args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp"))
        {
            if(args.length < 2)
                return false;
        }

        if(args[0].equalsIgnoreCase("load"))
        {
            Permission loadPerms = new Permission("bedwarsplugin.world.load",
                    "BedWars Plugin world manager loading permission", PermissionDefault.OP);

            if(!sender.hasPermission(loadPerms))
            {
                sender.sendMessage(plugin.getServer().getPermissionMessage());

                return false;
            }

            manager.loadWorld(args[1]);

            sender.sendMessage("Loaded world \"" + args[1] + "\"");
        }
        else if(args[0].equalsIgnoreCase("unload"))
        {
            Permission unloadPerms = new Permission("bedwarsplugin.world.unload",
                    "BedWars Plugin world manager unloading permission", PermissionDefault.OP);

            if(!sender.hasPermission(unloadPerms))
            {
                sender.sendMessage(plugin.getServer().getPermissionMessage());

                return false;
            }

            manager.unloadWorld(args[1]);

            sender.sendMessage("Unloaded world \"" + args[1] + "\"");
        }
        else if(args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp"))
        {
            Permission tpFullPerms = new Permission("bedwarsplugin.world.teleport.*",
                    "BedWars Plugin world manager teleportation permission", PermissionDefault.OP);
            Permission tpSelfPerms = new Permission("bedwarsplugin.world.teleport.self",
                    "BedWars Plugin world manager teleportation permission", PermissionDefault.OP);
            Permission tpOthersPerms = new Permission("bedwarsplugin.world.teleport.others",
                    "BedWars Plugin world manager teleportation permission", PermissionDefault.OP);

            if(manager.getLoadedWorlds().containsKey(args[1]))
            {
                Player player;
                Location loc = manager.getLoadedWorlds().get(args[1]).getWorld().getSpawnLocation();

                if(args.length > 2)
                {
                    if(!sender.hasPermission(tpOthersPerms))
                    {
                        sender.sendMessage(plugin.getServer().getPermissionMessage());

                        return false;
                    }

                    player = plugin.getServer().getPlayer(args[2]);

                    if(player != null)
                    {
                        player.teleport(loc);

                        sender.sendMessage("Teleported player \"" + args[2] + "\" to world \"" + args[1] + "\"");
                    }
                    else
                    {
                        sender.sendMessage("Player \"" + args[2] + "\" is not a valid player.");

                        return false;
                    }
                }
                else if(sender instanceof Player)
                {
                    if(!sender.hasPermission(tpSelfPerms))
                    {
                        sender.sendMessage(plugin.getServer().getPermissionMessage());

                        return false;
                    }

                    player = (Player) sender;

                    player.teleport(loc);

                    sender.sendMessage("Teleported player \"" + player.getName() + "\" to world \"" + args[1] + "\"");
                }
            }
            else
            {
                sender.sendMessage("World \"" + args[1] + "\" is not loaded.");

                return false;
            }
        }
        else if(args[0].equalsIgnoreCase("list"))
        {
            Map<String, BWWorld> worldMap = manager.getLoadedWorlds();

            String[] names = new String[worldMap.keySet().size()];
            worldMap.keySet().toArray(names);

            StringBuilder sb = new StringBuilder();

            for(String name : names)
                sb.append(name + ", ");

            sender.sendMessage("Loaded worlds: " + sb.toString());
        }

        return true;
    }
}

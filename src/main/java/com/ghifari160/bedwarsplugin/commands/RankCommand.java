package com.ghifari160.bedwarsplugin.commands;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import com.ghifari160.bedwarsplugin.ranks.IRankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor
{
    BedWarsPlugin plugin;
    IRankManager manager;

    public RankCommand(BedWarsPlugin core, IRankManager rankManager)
    {
        plugin = core;
        manager = rankManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length < 1)
            return false;
        else if(args[0].equalsIgnoreCase("list"))
        {
            StringBuilder ranks = new StringBuilder();

            for(String rank : manager.getRegisteredRanks())
                ranks.append(rank + ", ");

            sender.sendMessage("Registered ranks: " + ranks);
        }
        else if(args[0].equalsIgnoreCase("assign"))
        {
            Player player = null;

            if(manager.getRegisteredRanks().contains(args[1]))
            {
                if(args.length > 2)
                    player = plugin.getServer().getPlayer(args[2]);
                else if(sender instanceof Player)
                    player = (Player) sender;

                if(player != null)
                {
                    manager.addRank(player, args[1]);

                    sender.sendMessage("Assigned rank " + args[1] + " to " + player.getName());
                }
                else
                    sender.sendMessage("Player " + args[2] + " does not exists.");
            }
            else
            {
                sender.sendMessage("Rank " + args[1] + " does not exists.");
            }
        }

        return true;
    }
}

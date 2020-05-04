package com.ghifari160.bedwarsplugin.commands;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import com.ghifari160.bedwarsplugin.ranks.IRankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class RankCommand implements CommandExecutor
{
    BedWarsPlugin plugin;
    IRankManager rankManager;

    public RankCommand(BedWarsPlugin core, IRankManager rankManager)
    {
        plugin = core;
        this.rankManager = rankManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        String[] opts = { "list" };

        if(args.length < 1)
            sender.sendMessage("Invalid usage." + Arrays.toString(opts));
        else if(args[0].equalsIgnoreCase("list"))
        {
            String[] registeredRankNames = new String[rankManager.getRegisteredRankNames().size()];
            rankManager.getRegisteredRankNames().toArray(registeredRankNames);

            sender.sendMessage("Registered ranks: " + Arrays.toString(registeredRankNames));
        }

        return true;
    }
}

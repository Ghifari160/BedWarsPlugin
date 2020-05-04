package com.ghifari160.bedwarsplugin.ranks;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class RankTeamRunnable implements Runnable
{
    private final BedWarsPlugin plugin;
    private final IRankManager rankManager;

    public RankTeamRunnable(BedWarsPlugin core, IRankManager rManager)
    {
        plugin = core;
        rankManager = rManager;
    }

    public void run()
    {
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        UUID uuid;
        Team team;

        for(Player player : plugin.getServer().getOnlinePlayers())
        {
//            uuid = UUID.fromString(rankManager.getChatPrefix(player));
            uuid = UUID.nameUUIDFromBytes(rankManager.getChatPrefix(player).getBytes());

            team = scoreboard.getTeam("bwr_" + uuid.toString().split("-")[4]);

            if(team == null)
                team = scoreboard.registerNewTeam("bwr_" + uuid.toString().split("-")[4]);

            team.setPrefix(rankManager.getChatPrefix(player));

            team.addEntry(player.getName());
        }
    }
}

package com.ghifari160.bedwarsplugin.ranks;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import com.ghifari160.bedwarsplugin.permissions.IPermissionManager;
import org.bukkit.entity.Player;

import java.util.*;

public class RankManager implements IRankManager
{
    final BedWarsPlugin plugin;
    final IPermissionManager permissionManager;
    final HashMap<UUID, RankAttachment> playerRanks;
    final HashMap<String, Rank> registeredRanks;

    String defaultRank;

    public RankManager(BedWarsPlugin core, IPermissionManager manager)
    {
        plugin = core;
        permissionManager = manager;

        playerRanks = new HashMap<UUID, RankAttachment>();
        registeredRanks = new HashMap<String, Rank>();
    }

    public void addPlayer(Player player)
    {
        RankAttachment attachment = new RankAttachment();
        playerRanks.put(player.getUniqueId(), attachment);

        assignRanks(player.getUniqueId());
        assignPerms(player);
    }

    public void removePlayer(Player player)
    {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid)
    {
        playerRanks.remove(uuid);
        permissionManager.removePlayer(uuid);
    }

    public void addRank(Player player, String rank)
    {
        if(playerRanks.containsKey(player.getUniqueId()) && registeredRanks.containsKey(rank))
        {
            playerRanks.get(player.getUniqueId()).addRank(registeredRanks.get(rank));

            assignPerms(player);
        }
    }

    public void removeRank(Player player, String rank)
    {
        if(playerRanks.containsKey(player.getUniqueId()) && registeredRanks.containsKey(rank))
        {
            playerRanks.get(player.getUniqueId()).removeRank(registeredRanks.get(rank));

            assignPerms(player);
        }
    }

    public List<String> getRegisteredRanks()
    {
        List<String> ranks = new ArrayList<String>();

        for(String rank : registeredRanks.keySet())
            ranks.add(rank);

        return ranks;
    }

    public String getFullPrefix(Player player)
    {
        return getFullPrefix(player.getUniqueId());
    }

    public String getFullPrefix(UUID uuid)
    {
        Rank[] ranks = new Rank[playerRanks.get(uuid).getRanks().size()];
        StringBuilder sb = new StringBuilder();

        playerRanks.get(uuid).getRanks().toArray(ranks);

        for(Rank rank : ranks)
        {
            if(rank.getPrefix() != null)
                sb.append("[" + rank.getPrefix() + "]");
        }

        return (sb.length() < 1) ? "" : sb + " ";
    }

    void assignRanks(UUID uuid)
    {
        RankAttachment attachment = playerRanks.get(uuid);

        attachment.addRank(registeredRanks.get(defaultRank));
    }

    void assignPerms(Player player)
    {
        permissionManager.removePlayer(player.getUniqueId());
        permissionManager.addPlayer(player);

        for(Rank rank : playerRanks.get(player.getUniqueId()).getRanks())
        {
            for(String perm : rank.getPermissions())
                permissionManager.addPermission(player.getUniqueId(), perm);
        }
    }

    public void start()
    {
        for(String rankName : plugin.getConfig().getConfigurationSection("ranks").getKeys(false))
        {
            Rank rank = new Rank(rankName, plugin.getConfig().getString("ranks." + rankName + ".prefix"));

            for(String perm : plugin.getConfig().getStringList("ranks." + rankName + ".permissions"))
                rank.addPermission(perm);

            registeredRanks.put(rankName, rank);

            if(plugin.getConfig().getBoolean("ranks." + rankName + ".default"))
                defaultRank = rankName;
        }
    }

    public void shutdown()
    {
        playerRanks.clear();
        registeredRanks.clear();
    }
}

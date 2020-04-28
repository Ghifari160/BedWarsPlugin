package com.ghifari160.bedwarsplugin.ranks;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IRankManager
{
    void addPlayer(Player player);

    void removePlayer(Player player);
    void removePlayer(UUID uuid);

    void addRank(Player player, String rank);
    void removeRank(Player player, String rank);

    List<String> getRegisteredRanks();

    String getFullPrefix(Player player);
    String getFullPrefix(UUID uuid);

    void start();
    void shutdown();
}

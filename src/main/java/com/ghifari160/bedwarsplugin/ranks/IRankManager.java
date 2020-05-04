package com.ghifari160.bedwarsplugin.ranks;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IRankManager
{
    void registerRank(Rank rank);

    void deregisterRank(Rank rank);
    void deregisterRank(UUID uuid);

    void registerPlayer(Player player);

    void deregisterPlayer(Player player);
    void deregisterPlayer(UUID uuid);

    void addPlayer(Player player);

    void removePlayer(Player player);

    void addRank(Player player, Rank rank);
    void addRank(Player player, UUID rankUUID);

    void removeRank(Player player, Rank rank);
    void removeRank(Player player, UUID rankUUID);

    List<Rank> getRegisteredRanks();
    List<String> getRegisteredRankNames();

    String getChatPrefix(Player player);
    String getChatPrefix(UUID rankUUID);

    void start();
    void shutdown();
}
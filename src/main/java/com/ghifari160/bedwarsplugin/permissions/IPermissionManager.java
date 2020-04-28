package com.ghifari160.bedwarsplugin.permissions;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IPermissionManager
{
    void addPlayer(Player player);

    void removePlayer(Player player);
    void removePlayer(UUID uuid);

    void addPermission(Player player, String permission);
    void addPermission(UUID uuid, String permission);

    void removePermission(Player player, String permission);
    void removePermission(UUID uuid, String permission);

    void shutdown();
}

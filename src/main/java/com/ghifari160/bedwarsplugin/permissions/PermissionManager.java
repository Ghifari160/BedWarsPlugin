package com.ghifari160.bedwarsplugin.permissions;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermissionManager implements IPermissionManager
{
    final BedWarsPlugin plugin;
    final HashMap<UUID, PermissionAttachment> playerPermissions;

    public PermissionManager(BedWarsPlugin core)
    {
        plugin = core;

        playerPermissions = new HashMap<UUID, PermissionAttachment>();
    }

    public void addPlayer(Player player)
    {
        PermissionAttachment attachment = player.addAttachment(plugin);
        playerPermissions.put(player.getUniqueId(), attachment);
    }

    public void removePlayer(Player player)
    {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid)
    {
        playerPermissions.remove(uuid);
    }

    public void shutdown()
    {
        playerPermissions.clear();
    }

    public void addPermission(Player player, String permission)
    {
        addPermission(player.getUniqueId(), permission);
    }

    public void addPermission(UUID uuid, String permission)
    {
        playerPermissions.get(uuid).setPermission(permission, true);
    }

    public void removePermission(Player player, String permission)
    {
        removePermission(player.getUniqueId(), permission);
    }

    public void removePermission(UUID uuid, String permission)
    {
        playerPermissions.get(uuid).unsetPermission(permission);
    }

    public List<String> getPermissions(UUID uuid)
    {
        List<String> perms = new ArrayList<String>();

        for(String perm : playerPermissions.get(uuid).getPermissions().keySet())
        {
            if(!perms.contains(perm))
                perms.add(perm);
        }

        return perms;
    }

    public void clearPermissions(UUID uuid)
    {
        for(String perm : getPermissions(uuid))
            playerPermissions.get(uuid).unsetPermission(perm);
    }
}

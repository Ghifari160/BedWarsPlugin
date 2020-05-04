package com.ghifari160.bedwarsplugin.ranks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rank
{
    private UUID uuid;
    private String name;
    private String prefix;
    private String color;
    private List<String> permissions;
    private boolean isDefault;

    public Rank(String rankName)
    {
        this(rankName, null);
    }

    public Rank(UUID rankUUID, String rankName)
    {
        this(rankUUID, rankName, null);
    }

    public Rank(String rankName, String rankPrefix)
    {
        this(UUID.randomUUID(), rankName, rankPrefix);
    }

    public Rank(UUID rankUUID, String rankName, String rankPrefix)
    {
        uuid = rankUUID;
        name = rankName;
        prefix = rankPrefix;
        color = "WHITE";
        permissions = new ArrayList<String>();
        isDefault = false;
    }

    public void setName(String rankName)
    {
        name = rankName;
    }

    public UUID getUniqueID()
    {
        return uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setPrefix(String rankPrefix)
    {
        prefix = rankPrefix;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setColor(String color)
    {
        if(color != null && color.length() > 0)
            this.color = color.toUpperCase();
    }

    public String getColor()
    {
        return color;
    }

    public void isDefault(boolean isDef)
    {
        isDefault = isDef;
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public void addPermission(String permission)
    {
        permissions.add(permission);
    }

    public void removePermission(String permission)
    {
        permissions.remove(permission);
    }

    public void setPermissions(List<String> rankPermissions)
    {
        permissions = rankPermissions;
    }

    public List<String> getPermissions()
    {
        return permissions;
    }
}

package com.ghifari160.bedwarsplugin.ranks;

import java.util.ArrayList;
import java.util.List;

public class Rank
{
    String name;
    String prefix;
    List<String> permissions;

    public Rank()
    {
        this("default", "");
    }

    public Rank(String rankName, String rankPrefix)
    {
        name = rankName;
        prefix = rankPrefix;
        permissions = new ArrayList<String>();
    }

    public void setName(String rankName)
    {
        name = rankName;
    }

    public String getName()
    {
        return name;
    }

    public  void setPrefix(String rankPrefix)
    {
        prefix = rankPrefix;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void addPermission(String permission)
    {
        permissions.add(permission);
    }

    public void removePermission(String permission)
    {
        permissions.remove(permission);
    }

    public List<String> getPermissions()
    {
        return permissions;
    }


}

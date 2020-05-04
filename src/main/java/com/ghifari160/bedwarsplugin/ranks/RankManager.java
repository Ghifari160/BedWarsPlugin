package com.ghifari160.bedwarsplugin.ranks;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import com.ghifari160.bedwarsplugin.database.IDatabaseManager;
import com.ghifari160.bedwarsplugin.permissions.IPermissionManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RankManager implements IRankManager
{
    final BedWarsPlugin plugin;
    final IPermissionManager permissionManager;
    final IDatabaseManager databaseManager;

    public RankManager(BedWarsPlugin plugin, IPermissionManager permissionManager, IDatabaseManager databaseManager)
    {
        this.plugin = plugin;
        this.permissionManager = permissionManager;
        this.databaseManager = databaseManager;
    }

    String listToInlineCSV(List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        String inlineCSV;

        for(String item : list)
            sb.append(item).append(",");

        inlineCSV = (sb.length() > 1) ? sb.substring(0, sb.length() - 1) : sb.toString();

        return inlineCSV;
    }

    List<String> inlineCSVToList(String inlineCSV)
    {
        List<String> list;

        if(inlineCSV != null && inlineCSV.length() > 0 && inlineCSV.contains(","))
            list = new ArrayList<String>(Arrays.asList(inlineCSV.split(",")));
        else if(inlineCSV != null && inlineCSV.length() > 0 && !inlineCSV.contains(","))
            list = new ArrayList<String>(Collections.singletonList(inlineCSV));
        else
            list = new ArrayList<String>();

        return list;
    }

    List<String> rankListToUUIDStringList(List<Rank> rankList)
    {
        List<String> list = new ArrayList<String>();

        for(Rank rank : rankList)
            list.add(rank.getUniqueID().toString());

        return list;
    }

    List<Rank> uuidStringListToRankList(List<String> uuidStringList)
    {
        List<Rank> list = new ArrayList<Rank>();
        Rank rank;

        for(String uuidString : uuidStringList)
        {
            rank = lookupRank(UUID.fromString(uuidString));

            if(rank != null)
                list.add(rank);
        }

        return list;
    }

    boolean isRankStored(Rank rank)
    {
        boolean stored = false;

        try
        {
            String sql = "SELECT uuid FROM ranks WHERE name = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
            ResultSet results;

            stmt.setString(1, rank.getName());

            results = stmt.executeQuery();

            while(results.next())
                stored = true;
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to lookup rank");

            e.printStackTrace();
        }

        return stored;
    }

    boolean isRankStored(UUID uuid)
    {
        boolean stored = false;

        try
        {
            String sql = "SELECT uuid FROM ranks WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
            ResultSet results;

            stmt.setString(1, uuid.toString());

            results = stmt.executeQuery();

            while(results.next())
                stored = true;
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to lookup rank");

            e.printStackTrace();
        }

        return stored;
    }

    void storeRank(Rank rank)
    {
        try
        {
            String sql = "INSERT INTO ranks (uuid, name, prefix, color, opt_default, permissions)" +
                    " VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.setString(1, rank.getUniqueID().toString());
            stmt.setString(2, rank.getName());
            stmt.setString(3, rank.getPrefix());
            stmt.setString(4, rank.getColor());
            stmt.setBoolean(5, rank.isDefault());
            stmt.setString(6, listToInlineCSV(rank.getPermissions()));

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to store rank");

            e.printStackTrace();
        }
    }

    Rank createRankFromResults(ResultSet results) throws SQLException
    {
        Rank rank = new Rank(UUID.fromString(results.getString("uuid")),
                results.getString("name"), results.getString("prefix"));

        rank.setColor(results.getString("color"));
        rank.isDefault(results.getBoolean("opt_default"));
        rank.setPermissions(inlineCSVToList(results.getString("permissions")));

        return rank;
    }

    @Nullable
    Rank lookupRank(UUID uuid)
    {
        Rank rank = null;

        try
        {
            String sql = "SELECT * FROM ranks WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
            ResultSet results;

            stmt.setString(1, uuid.toString());

            results = stmt.executeQuery();

            while(results.next())
                rank = createRankFromResults(results);
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to lookup rank");

            e.printStackTrace();
        }

        return rank;
    }

    @Nullable
    Rank lookupDefaultRank()
    {
        Rank rank = null;

        try
        {
            String sql = "SELECT * FROM ranks WHERE opt_default = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
            ResultSet results;

            stmt.setBoolean(1, true);

            results = stmt.executeQuery();

            while(results.next())
                rank = createRankFromResults(results);
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to lookup default rank");

            e.printStackTrace();
        }

        return rank;
    }

    void updateRank(Rank rank)
    {
        try
        {
            String sql = "UPDATE ranks SET name = ?, prefix = ?, color = ?, opt_default = ?, permissions = ? " +
                    "WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.setString(1, rank.getName());
            stmt.setString(2, rank.getPrefix());
            stmt.setString(3, rank.getColor());
            stmt.setBoolean(4, rank.isDefault());
            stmt.setString(5, listToInlineCSV(rank.getPermissions()));
            stmt.setString(6, rank.getUniqueID().toString());

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to update rank");

            e.printStackTrace();
        }
    }

    void deleteRank(Rank rank)
    {
        deleteRank(rank.getUniqueID());
    }

    void deleteRank(UUID uuid)
    {
        try
        {
            String sql = "DELETE FROM ranks WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.setString(1, uuid.toString());

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to delete rank");

            e.printStackTrace();
        }
    }

    public void registerRank(Rank rank)
    {
        if(!isRankStored(rank))
            storeRank(rank);
        else
            updateRank(rank);
    }

    public void deregisterRank(Rank rank)
    {
        deregisterRank(rank.getUniqueID());
    }

    public void deregisterRank(UUID uuid)
    {
        if(isRankStored(uuid))
            deleteRank(uuid);
    }

    boolean isPlayerStored(Player player)
    {
        return isPlayerStored(player.getUniqueId());
    }

    boolean isPlayerStored(UUID uuid)
    {
        boolean stored = false;

        try
        {
            String sql = "SELECT uuid FROM players WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
            ResultSet results;

            stmt.setString(1, uuid.toString());

            results = stmt.executeQuery();

            while(results.next())
                stored = true;
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to lookup player");

            e.printStackTrace();
        }

        return stored;
    }

    @Nullable
    RankAttachment lookupPlayerRankAttachment(UUID uuid)
    {
        RankAttachment player = null;

        try
        {
            String sql = "SELECT * FROM players WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
            ResultSet results;

            stmt.setString(1, uuid.toString());

            results = stmt.executeQuery();

            while(results.next())
            {
                player = new RankAttachment();
                player.setRanks(uuidStringListToRankList(inlineCSVToList(results.getString("ranks"))));
            }
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to lookup player");

            e.printStackTrace();
        }

        return player;
    }

    void updatePlayer(Player player, RankAttachment attachment)
    {
        try
        {
            String sql = "UPDATE players SET name = ?, ranks = ? WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.setString(1, player.getName());
            stmt.setString(2, listToInlineCSV(rankListToUUIDStringList(attachment.getRanks())));
            stmt.setString(3, player.getUniqueId().toString());

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to update player");

            e.printStackTrace();
        }
    }

    void deletePlayer(Player player)
    {
        deletePlayer(player.getUniqueId());
    }

    void deletePlayer(UUID uuid)
    {
        try
        {
            String sql = "DELETE FROM players WHERE uuid = ?;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.setString(1, uuid.toString());

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to delete player");

            e.printStackTrace();
        }
    }

    void storePlayer(Player player, RankAttachment attachment)
    {
        try
        {
            String sql = "INSERT INTO players (uuid, name, ranks) VALUES (?, ?, ?);";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());
            stmt.setString(3, listToInlineCSV(rankListToUUIDStringList(attachment.getRanks())));

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to store player");

            e.printStackTrace();
        }
    }

    void assignPerms(UUID uuid)
    {
        List<String> perms;
        RankAttachment attachment;

        if(isPlayerStored(uuid))
        {
            perms = new ArrayList<String>();
            attachment = lookupPlayerRankAttachment(uuid);

            assert attachment != null;
            for(Rank rank : attachment.getRanks())
            {
                for(String perm : rank.getPermissions())
                {
                    if(!perms.contains(perm))
                        perms.add(perm);
                }
            }

            permissionManager.clearPermissions(uuid);

            for(String perm : perms)
                permissionManager.addPermission(uuid, perm);
        }
    }

    public void registerPlayer(Player player)
    {
        RankAttachment attachment;

        if(!isPlayerStored(player))
            attachment = new RankAttachment();
        else
            attachment = lookupPlayerRankAttachment(player.getUniqueId());

        assert attachment != null;
        if(attachment.getRanks().size() < 1)
        {
            Rank defaultRank = lookupDefaultRank();

            if(defaultRank != null)
                attachment.addRank(defaultRank);
        }

        if(!isPlayerStored(player.getUniqueId()))
            storePlayer(player, attachment);
        else
            updatePlayer(player, attachment);

        permissionManager.addPlayer(player);

        assignPerms(player.getUniqueId());
    }

    public void deregisterPlayer(Player player)
    {
        deregisterPlayer(player.getUniqueId());
    }

    public void deregisterPlayer(UUID uuid)
    {
        if(isPlayerStored(uuid))
            deletePlayer(uuid);

        permissionManager.removePlayer(uuid);
    }

    public void addPlayer(Player player)
    {
        RankAttachment attachment;
        Rank defaultRank;

        if(isPlayerStored(player))
        {
            attachment = lookupPlayerRankAttachment(player.getUniqueId());

            assert attachment != null;
            if(attachment.getRanks().size() < 1)
            {
                defaultRank = lookupDefaultRank();

                if(defaultRank != null)
                    attachment.addRank(defaultRank);
            }

            updatePlayer(player, attachment);

            permissionManager.addPlayer(player);

            assignPerms(player.getUniqueId());
        }
        else
            registerPlayer(player);
    }

    public void removePlayer(Player player)
    {
        permissionManager.removePlayer(player);
    }

    public void addRank(Player player, Rank rank)
    {
        addRank(player, rank.getUniqueID());
    }

    public void addRank(Player player, UUID rankUUID)
    {
        RankAttachment attachment = lookupPlayerRankAttachment(player.getUniqueId());
        Rank rank = lookupRank(rankUUID);

        if(isPlayerStored(player) && attachment != null && rank != null)
        {
            attachment.addRank(rank);

            updatePlayer(player, attachment);

            assignPerms(player.getUniqueId());
        }
    }

    public void removeRank(Player player, Rank rank)
    {
        removeRank(player, rank.getUniqueID());
    }

    public void removeRank(Player player, UUID rankUUID)
    {
        RankAttachment attachment = lookupPlayerRankAttachment(player.getUniqueId());
        Rank rank = lookupRank(rankUUID);

        if(isPlayerStored(player) && attachment != null && rank != null)
        {
            attachment.removeRank(rank);

            updatePlayer(player, attachment);

            assignPerms(player.getUniqueId());
        }
    }

    void setupRankTable()
    {
        try
        {
            String sql = "CREATE TABLE IF NOT EXISTS ranks (" +
                    "uuid varchar(36) NOT NULL," +
                    "name text NOT NULL," +
                    "prefix text," +
                    "color text NOT NULL," +
                    "opt_default boolean NOT NULL," +
                    "permissions text," +
                    "PRIMARY KEY (uuid));";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to create rank table");

            e.printStackTrace();
        }
    }

    void setupPlayerTable()
    {
        try
        {
            String sql = "CREATE TABLE IF NOT EXISTS players (" +
                    "uuid varchar(36) NOT NULL," +
                    "name text NOT NULL," +
                    "ranks text," +
                    "PRIMARY KEY (uuid));";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to create player table");

            e.printStackTrace();
        }
    }

    void registerRanksFromConfig()
    {
        List<Rank> ranks = new ArrayList<Rank>();

        Rank rank;
        List<String> perms;

        for(String rankName : plugin.getConfig().getConfigurationSection("ranks").getKeys(false))
        {
            rank = new Rank(rankName);

            perms = new ArrayList<String>(plugin.getConfig().getStringList("ranks." + rankName + ".permissions"));

            rank.setPrefix(plugin.getConfig().getString("ranks." + rankName + ".prefix"));
            rank.setColor(plugin.getConfig().getString("ranks." + rankName + ".color"));
            rank.isDefault(plugin.getConfig().getBoolean("ranks." + rankName + ".default"));
            rank.setPermissions(perms);

            if(!isRankStored(rank))
                storeRank(rank);
            else
                updateRank(rank);
        }
    }

    public List<Rank> getRegisteredRanks()
    {
        List<Rank> ranks = new ArrayList<Rank>();

        try
        {
            String sql = "SELECT * FROM ranks;";
            PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
            ResultSet results;

            results = stmt.executeQuery();

            while(results.next())
            {
                ranks.add(createRankFromResults(results));
            }
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("Unable to lookup ranks");

            e.printStackTrace();
        }

        return ranks;
    }

    public List<String> getRegisteredRankNames()
    {
        List<String> rankNames = new ArrayList<String>();
        List<Rank> ranks = getRegisteredRanks();

        for(Rank rank : ranks)
            rankNames.add(rank.getName());

        return rankNames;
    }

    public String getChatPrefix(Player player)
    {
        return getChatPrefix(player.getUniqueId());
    }

    public String getChatPrefix(UUID uuid)
    {
        String chatPrefix;

        RankAttachment attachment;
        StringBuilder sb;

        if(isPlayerStored(uuid))
        {
            attachment = lookupPlayerRankAttachment(uuid);
            sb = new StringBuilder();

            assert attachment != null;
            for(Rank rank : attachment.getRanks())
            {

                if(rank.getPrefix() != null && rank.getPrefix().length() > 0)
                    sb.append(ChatColor.valueOf(rank.getColor())).append("[").append(rank.getPrefix()).append("]");
            }

            chatPrefix = (sb.length() > 0) ? sb.append(" ").toString() : "";
        }
        else
            chatPrefix = "";

        return chatPrefix;
    }

    public void start()
    {
        setupRankTable();
        setupPlayerTable();

        registerRanksFromConfig();
    }

    public void shutdown()
    {
        try
        {
            for (Team team : plugin.getServer().getScoreboardManager().getMainScoreboard().getTeams())
            {
                if (team.getName().contains("bwr_"))
                    team.unregister();
            }
        }
        catch (IllegalStateException e)
        {
            plugin.getLogger().severe("Unabled to unregister team");

            e.printStackTrace();
        }
    }
}
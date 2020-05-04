package com.ghifari160.bedwarsplugin.database;

import com.ghifari160.bedwarsplugin.BedWarsPlugin;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager implements IDatabaseManager
{
    final BedWarsPlugin plugin;
    Connection connection;

    public DatabaseManager(BedWarsPlugin core)
    {
        plugin = core;
    }

    public boolean isConnected()
    {
        try
        {
            if(connection != null && !connection.isClosed())
                return true;
        }
        catch (SQLException e){}

        return false;
    }

    public Connection getConnection()
    {
        return connection;
    }

    void check_for_driver()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            plugin.getLogger().info(ChatColor.RED + "MySQL API not found!");
            e.printStackTrace();
        }
    }

    void establish_connection()
    {
        try
        {
            String db_host = plugin.getConfig().getString("database.host"),
                    db_port = plugin.getConfig().getString("database.port"),
                    db_username = plugin.getConfig().getString("database.username"),
                    db_password = plugin.getConfig().getString("database.password"),
                    db_name = plugin.getConfig().getString("database.db-name");

            boolean useSSL = plugin.getConfig().getBoolean("database.use-ssl");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + db_host + ":" + db_port + "/" + db_name + "?useSSL=" + useSSL,
                    db_username, db_password);

            plugin.getLogger().info("Connected to MySQL database at " + db_host + ":" + db_port + "!");
        }
        catch(SQLException e)
        {
            plugin.getLogger().info(ChatColor.RED + "SQL exception!");
            e.printStackTrace();
        }
    }

    void close_connection()
    {
        try
        {
            if(connection != null && !connection.isClosed())
                connection.close();
        }
        catch (Exception e)
        {
            plugin.getLogger().info(ChatColor.RED + "Unable to close SQL connection!");
            e.printStackTrace();
        }
    }

    public void start()
    {
        check_for_driver();

        establish_connection();
    }

    public void shutdown()
    {
        close_connection();
    }
}

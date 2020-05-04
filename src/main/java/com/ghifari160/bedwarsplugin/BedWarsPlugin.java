package com.ghifari160.bedwarsplugin;

import com.ghifari160.bedwarsplugin.commands.RankCommand;
import com.ghifari160.bedwarsplugin.commands.WorldCommand;
import com.ghifari160.bedwarsplugin.commands.WorldTabCompleter;
import com.ghifari160.bedwarsplugin.database.DatabaseManager;
import com.ghifari160.bedwarsplugin.database.IDatabaseManager;
import com.ghifari160.bedwarsplugin.permissions.IPermissionManager;
import com.ghifari160.bedwarsplugin.permissions.PermissionManager;
import com.ghifari160.bedwarsplugin.ranks.IRankManager;
import com.ghifari160.bedwarsplugin.ranks.RankManager;
import com.ghifari160.bedwarsplugin.ranks.RankTeamRunnable;
import com.ghifari160.bedwarsplugin.world.IWorldManager;
import com.ghifari160.bedwarsplugin.world.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWarsPlugin extends JavaPlugin implements Listener {
    final static String VERSION = "0.1.0";

    final IDatabaseManager databaseManager = new DatabaseManager(this);
    final IWorldManager worldManager = new WorldManager(this);
    final IPermissionManager permissionManager = new PermissionManager(this);
    final IRankManager rankManager = new RankManager(this, permissionManager, databaseManager);

    void registerCommands()
    {
        getCommand("bwworld").setExecutor(new WorldCommand(this, worldManager));
        getCommand("bwworld").setTabCompleter(new WorldTabCompleter(this, worldManager));

        getCommand("bwrank").setExecutor(new RankCommand(this, rankManager));
    }

    void loadConfig()
    {
        getConfig().options().copyDefaults();
        saveConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onEnable()
    {
        getLogger().info("Starting database manager...");
        databaseManager.start(); // TODO: crash the server if unable to connect

        getLogger().info("Registering commands...");
        registerCommands();

        getLogger().info("Loading config...");
        loadConfig();

        getLogger().info("Starting rank manager...");
        rankManager.start();

        getLogger().info("Starting world manager...");
        worldManager.start();

        getServer().getScheduler().runTaskTimer(this, new RankTeamRunnable(this, rankManager),
                0, 40);

        getLogger().info("BedWarsPlugin v" + VERSION + " is enabled.");
    }

    @Override
    public void onDisable()
    {
        getServer().getScheduler().cancelTasks(this);

        getLogger().info("Shutting down rank manager...");
        rankManager.shutdown();

        getLogger().info("Shutting down permission manager...");
        permissionManager.shutdown();

        getLogger().info("Shutting down world manager...");
        worldManager.shutdown();

        getLogger().info("Shutting down database manager...");
        databaseManager.shutdown();

        getLogger().info("BedWarsPlugin v" + VERSION + " is disabled.");
    }

    @EventHandler
    public void join(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        rankManager.addPlayer(player);
    }

    @EventHandler
    public void leave(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        rankManager.removePlayer(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        event.setFormat(rankManager.getChatPrefix(event.getPlayer()) + ChatColor.WHITE + "%s: %s");
    }
}

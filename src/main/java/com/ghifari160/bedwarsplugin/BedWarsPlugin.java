package com.ghifari160.bedwarsplugin;

import com.ghifari160.bedwarsplugin.commands.RankCommand;
import com.ghifari160.bedwarsplugin.commands.WorldCommand;
import com.ghifari160.bedwarsplugin.commands.WorldTabCompleter;
import com.ghifari160.bedwarsplugin.permissions.IPermissionManager;
import com.ghifari160.bedwarsplugin.permissions.PermissionManager;
import com.ghifari160.bedwarsplugin.ranks.IRankManager;
import com.ghifari160.bedwarsplugin.ranks.RankManager;
import com.ghifari160.bedwarsplugin.world.IWorldManager;
import com.ghifari160.bedwarsplugin.world.WorldManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWarsPlugin extends JavaPlugin implements Listener {
    final static String VERSION = "0.1.0";
    final IWorldManager worldManager = new WorldManager(this);
    final IPermissionManager permissionManager = new PermissionManager(this);
    final IRankManager rankManager = new RankManager(this, permissionManager);

    @Override
    public void onEnable()
    {
        getLogger().info("Registering commands...");
        getCommand("bwworld").setExecutor(new WorldCommand(this, worldManager));
        getCommand("bwworld").setTabCompleter(new WorldTabCompleter(this, worldManager));

        getCommand("bwrank").setExecutor(new RankCommand(this, rankManager));

        getLogger().info("Loading config...");
        getConfig().options().copyDefaults();
        saveConfig();
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Starting rank manager...");
        rankManager.start();

        getLogger().info("Starting world manager...");
        worldManager.start();

        getLogger().info("BedWarsPlugin v" + VERSION + " is enabled.");
    }

    @Override
    public void onDisable()
    {
        getLogger().info("Shutting down rank manager...");
        rankManager.shutdown();

        getLogger().info("Shutting down permission manager...");
        permissionManager.shutdown();

        getLogger().info("Shutting down world manager...");
        worldManager.shutdown();

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
        String prefix = rankManager.getFullPrefix(event.getPlayer());
        event.setFormat(prefix + "%s: %s");
    }
}

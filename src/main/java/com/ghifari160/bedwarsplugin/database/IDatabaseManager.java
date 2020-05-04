package com.ghifari160.bedwarsplugin.database;

import java.sql.Connection;

public interface IDatabaseManager
{
    boolean isConnected();

    Connection getConnection();

    void start();
    void shutdown();
}

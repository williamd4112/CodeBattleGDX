package com.codebattle.network.server;

public interface ConnectionListener {
    public void onReceiveMessage(Connection connection, String msg);
    public void onDisconnect(Connection connection);
}

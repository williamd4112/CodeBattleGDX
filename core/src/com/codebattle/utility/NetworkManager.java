package com.codebattle.utility;

import com.codebattle.network.client.Client;

public class NetworkManager {
    private static NetworkManager instance;

    private static String default_ip = "127.0.0.1";
    private static String default_port = "8000";

    private final Client client;

    private NetworkManager(final String addr, final String port) {
        this.client = new Client();
        this.client.connectToServer(addr, port);
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager(default_ip, default_port);
        }
        return instance;
    }

    public Client getClient() {
        return this.client;
    }
}

package com.codebattle.utility;

import com.codebattle.network.client.Client;

public class NetworkManager {
    private static NetworkManager instance;

    private static String default_ip = "140.114.253.141";
    private static String default_port = "8000";

    private Client client;

    private NetworkManager(String addr, String port) {
        client = new Client();
        client.connectToServer(addr, port);
    }

    public static NetworkManager getInstance() {
        if (instance == null)
            instance = new NetworkManager(default_ip, default_port);
        return instance;
    }

    public Client getClient() {
        return this.client;
    }
}

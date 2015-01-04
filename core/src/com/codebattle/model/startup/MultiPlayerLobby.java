package com.codebattle.model.startup;

import java.net.Socket;

import com.codebattle.network.PeerListener;
import com.codebattle.network.client.Client;
import com.codebattle.scene.StartupScene;

public class MultiPlayerLobby extends SelectionStage implements PeerListener {

    private Client client;

    private String[] rooms = { "room01", "room02", "room03", "room04", "room05", "room06" };

    public MultiPlayerLobby(StartupScene parent) {
        super(parent);
        this.list.setItems(rooms);

    }

    @Override
    public void onReceivedMessage(String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Socket socket) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnected(Socket socket) {
        // TODO Auto-generated method stub

    }
}

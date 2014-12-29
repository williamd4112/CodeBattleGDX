package com.codebattle.network;

import java.net.Socket;

public interface PeerListener {
    public void onReceivedMessage(String msg);

    public void onConnected(Socket socket);
}

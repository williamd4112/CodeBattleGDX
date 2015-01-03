package com.codebattle.network;

import java.net.Socket;

public interface PeerListener {
    public void onReceivedMessage(String msg);

    public void onConnected(Socket socket);

    /**
     * @author 1/3
     * @param socket
     */
    public void onDisconnected(Socket socket);
}

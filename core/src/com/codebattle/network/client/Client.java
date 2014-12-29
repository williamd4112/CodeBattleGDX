package com.codebattle.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import com.codebattle.network.PeerListener;

public class Client {
    // private Game game;
    // private GameAgent listener = null;

    // Network IO
    private Socket socket = null;
    private String serverIP;
    private int serverPort;
    private BufferedReader reader;
    private PrintWriter writer;
    private ListenThread listenThread;

    // Event listeners
    private List<PeerListener> listeners;

    public Client() {
        this.listeners = new LinkedList<PeerListener>();
        this.listenThread = new ListenThread();
    }

    public void connectToServer(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);

        try {
            if (socket == null) {
                this.socket = new Socket(this.serverIP, this.serverPort);
                this.reader = new BufferedReader(new InputStreamReader(
                        this.socket.getInputStream()));
                this.writer = new PrintWriter(new OutputStreamWriter(
                        this.socket.getOutputStream()));
                start();
                this.emitConnectEvent(socket);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        this.listenThread.start();
    }

    /**
     * Event handling
     */
    private void emitMessageReceiveEvent(String msg) {
        // this.game.onReceiveMessage(msg);
        for (PeerListener p : this.listeners)
            p.onReceivedMessage(msg);
    }

    private void emitConnectEvent(Socket socket) {
        for (PeerListener p : this.listeners)
            p.onConnected(socket);
    }

    public void addPeerListener(PeerListener listener) {
        this.listeners.add(listener);
    }

    public void sendMessage(String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }

    private class ListenThread extends Thread {
        @Override
        public void run() {
            try {
                String rawMessage = null;
                while (!socket.isClosed()) {
                    rawMessage = Client.this.reader.readLine();
                    emitMessageReceiveEvent(rawMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

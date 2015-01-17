package com.codebattle.network.client;

import com.codebattle.network.PeerListener;
import com.codebattle.network.dataHandle.DataHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Client {

    // Network IO
    private Socket socket = null;
    private String serverIP;
    private int serverPort;
    private BufferedReader reader;
    private PrintWriter writer;
    private final ListenThread listenThread;
    private final TimerThread timerThread;

    // Event listeners
    private final Stack<PeerListener> listeners;

    public Client() {
        this.listeners = new Stack<PeerListener>();
        this.listenThread = new ListenThread();
        this.timerThread = new TimerThread();
    }

    public void connectToServer(final String serverIP, final String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);

        try {
            if (this.socket == null) {
                this.socket = new Socket(this.serverIP, this.serverPort);
                this.reader = new BufferedReader(new InputStreamReader(
                        this.socket.getInputStream()));
                this.writer = new PrintWriter(new OutputStreamWriter(
                        this.socket.getOutputStream()));
                this.start();
                this.emitConnectEvent(this.socket);
            }
        } catch (final UnknownHostException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        this.listenThread.start();
        this.timerThread.start();
    }

    /**
     * Event handling
     * emitReceiveMessage only for top listener
     */
    private void emitReceiveMessage(final String msg) {
        if (!this.listeners.isEmpty()) {
            final PeerListener p = this.listeners.peek();
            System.out.println("emitReceiveMessage@" + p.getClass() + ": " + msg);
            p.onReceivedMessage(msg);
        }
    }

    private void emitConnectEvent(final Socket socket) {
        for (final PeerListener p : this.listeners) {
            p.onConnected(socket);
        }
    }

    public void addPeerListener(final PeerListener listener) {
        this.listeners.add(listener);
    }

    public void popPeerListener() {
        this.listeners.pop();
    }

    public void send(final String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }

    private class ListenThread extends Thread {
        @Override
        public void run() {
            try {
                String rawMessage = null;
                while (!Client.this.socket.isClosed()) {
                    if ((rawMessage = Client.this.reader.readLine()) != null) {
                        Client.this.emitReceiveMessage(rawMessage);
                        // System.out.println("Raw: " + rawMessage);
                    }
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class TimerThread extends Thread {
        long interval = 30; // unit: second
        Timer timer = new Timer();

        @Override
        public void run() {
            this.timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Client.this.send(DataHandler.report().toString());
                }

            }, TimeUnit.SECONDS.toMillis(this.interval),
                    TimeUnit.SECONDS.toMillis(this.interval));
        }
    }
}

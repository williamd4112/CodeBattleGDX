package com.codebattle.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Room: connection listener
 * Socket: client's socket
 *
 * Connection is headless , it only receive message and send send message but never process message
 * @author williamd
 *
 */
public class Connection extends Thread {

    // Network
    private final int ID;
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    // EventListener
    private ConnectionListener server = null;
    private ConnectionListener room = null;
    private ConnectionTimer timer = null;

    // Client Player Info
    private Player player;

    public Connection(final int ID, final Socket clientSocket) throws IOException {
        this.ID = ID;
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(
                this.clientSocket.getInputStream()));
        this.writer = new PrintWriter(this.clientSocket.getOutputStream());
        this.timer = new ConnectionTimer();
    }

    public void bind(final ConnectionListener listener) {
        if (listener.getClass()
                .equals(Server.class)) {
            if (this.server == null) {
                this.server = listener;
            }
        }
        else if (listener.getClass()
                .equals(Room.class)) {
            if (this.room == null) {
                this.room = listener;
            }
        }
        else if (listener.getClass()
                .equals(ConnectionTimer.class)) {
            if (this.timer == null) {
                this.timer = (ConnectionTimer) listener;
            }
        }
    }

    public void unbindRoom() {
        this.room = null;
    }

    @Override
    public void run() {
        this.listen();
        this.timer.start();
    }

    /**
     * Listen on the message from client
     */
    public void listen() {
        try {
            String msg;
            while (!this.clientSocket.isClosed()) {
                if ((msg = this.reader.readLine()) != null) {
                    this.emitReceiveMessage(this, msg);
                } else {
                    break;
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            this.emitDisconnect(this);
        }
    }

    /**
     * Send the raw message to the client
     * @param msg
     */
    public void send(final String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }

    /**
     * Event handling
     */
    public void emitReceiveMessage(final Connection connection, final String msg) {
        if (this.room != null) {
            this.room.onReceiveMessage(connection, msg);
        }
        else {
            System.out.println("Transfer to server");
            this.server.onReceiveMessage(connection, msg);
        }
    }

    public void emitDisconnect(final Connection connection) {
        if (this.room != null) {
            this.room.onDisconnect(connection);
        }
        this.server.onDisconnect(connection);
    }

    /**
     * Getters and setters
     */
    public Player getPlayer() {
        return this.player;
    }

    public Socket getSocket() {
        return this.clientSocket;
    }

    public int getID() {
        return this.ID;
    }

    public void setPlayer(final String name) {
        this.player = new Player(name);
    }

    private class ConnectionTimer extends Thread implements ConnectionListener {
        long interval = 45; // unit: second
        Timer timer = new Timer();

        public ConnectionTimer() {
            super();
            Connection.this.bind(this);
        }

        @Override
        public void run() {
            this.timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Connection.this.emitDisconnect(Connection.this);
                }

            }, TimeUnit.SECONDS.toMillis(this.interval),
                    TimeUnit.SECONDS.toMillis(this.interval));
        }

        @Override
        public void onReceiveMessage(final Connection connection, final String msg) {
            this.start();
        }

        @Override
        public void onDisconnect(final Connection connection) {

        }

    }
}

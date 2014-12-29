package com.codebattle.network.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    // EventListener
    private ConnectionListener server = null;
    private ConnectionListener room = null;

    // Client Player Info
    private Player player;

    public Connection(final Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(
                this.clientSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(
                this.clientSocket.getOutputStream()));
    }

    public void connect(ConnectionListener listener, String type) {
        if (type.equals("Server")) {
            if (this.server == null) {
                this.server = listener;
            }
        } else if (type.equals("Room")) {
            this.room = listener;
            if (this.room == null) {
                this.room = listener;
            }
        }
    }

    @Override
    public void run() {
        this.listen();
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
                }
            }
            this.emitDisconnect(this);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the raw message to the client
     * @param msg
     */
    public void send(final String msg) {
        try {
            this.writer.write(msg);
            this.writer.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event handling
     */
    public void emitReceiveMessage(final Connection connection, final String msg) {
        if (this.room != null) {
            this.room.onReceiveMessage(connection, msg);
        } else {
            this.server.onReceiveMessage(connection, msg);
        }
    }

    public void emitDisconnect(final Connection connection) {

    }

    /**
     * Getters and setters
     */
    public Player getPlayer() {
        return this.player;
    }

    /*
     * public void setRoom(Room room) {
     * this.room = room;
     * }
     */

}

package com.codebattle.network.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Rooms: server has a lot of rooms , and rooms bind two or one connection
 * IdleConnections: connections without room (when it add to any room , it will be removed from this list
 *
 * Event handling: (Server will handle the following type of message
 *
 * Type: Server
 * Opt: Login
 * Data: <Username>
 *
 * Type: Room
 * Opt: Create
 * Data: <Room Name>
 *
 * @author williamd
 *
 */

public class Server implements ConnectionListener {

    // Network IO
    private final ServerSocket serverSocket;

    // Connection management
    // Rooms will store connections (if connection enter the room or create a room
    // Idle connection (no room connections
    private final List<Room> rooms;
    private final List<Connection> idleConnections;

    // Threading
    private final ListenThread listenThread;

    // Message Receiving routing
    private Map<String, Method> routingTable;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.rooms = new ArrayList<Room>();
        this.idleConnections = new ArrayList<Connection>();
        this.listenThread = new ListenThread();
    }

    public void start() {
        this.listenThread.start();
    }

    public void initRoutingTable() {

    }

    public void addRoom(String name) {
        this.rooms.add(new Room(name));
    }

    /**
     * Event handling
     */
    @Override
    public void onReceiveMessage(Connection connection, String rawMessage) {
        // TODO : handling message (Advise to enclose this block with try/catch

        // Extract data
        Message msg = new Message(rawMessage);
        // Routing (will be replaced with routing table if there is enough time
        if (msg.type.equals("Server")) {
            // TODO: Login the server (Although connection has been created, but its player data
            // hasn't been set
            if (msg.opt.equals("Login")) {
                connection.getPlayer()
                        .setName(msg.data);
            }
        } else if (msg.type.equals("Room")) {
            if (msg.opt.equals("Create")) {
                // TODO: Create Room
                this.addRoom(msg.data);
            }
        }
    }

    @Override
    public void onDisconnect(Connection connection) {
        // TODO : remove this connection from idle connection
        this.idleConnections.remove(connection);
    }

    /**
     * Continuously listen new client connection reqeust
     * @author williamd
     *
     */
    private class ListenThread extends Thread {
        @Override
        public void run() {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();

                    // Create connection
                    Connection connection = new Connection(clientSocket);
                    idleConnections.add(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTimeout(Connection connection) {
        // TODO Auto-generated method stub

    }
}

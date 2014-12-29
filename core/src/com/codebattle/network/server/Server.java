package com.codebattle.network.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.codebattle.network.PeerListener;

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
 * Type: Server
 * Opt: RoomList
 * Data: <Room Array>
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

    // Interface with Agent
    private List<PeerListener> peerListeners;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.rooms = new ArrayList<Room>();
        this.idleConnections = new ArrayList<Connection>();
        this.listenThread = new ListenThread();
        this.peerListeners = new LinkedList<PeerListener>();
    }

    public void start() {
        this.listenThread.start();
    }

    public void initRoutingTable() {

    }

    public Room addRoom(String name) {
        Room room = new Room(name);

        this.rooms.add(room);
        return room;
    }

    public void addPeerListener(PeerListener listener) {
        this.peerListeners.add(listener);
    }

    /**
     * Event handling
     */
    @Override
    public void onReceiveMessage(Connection connection, String rawMessage) {
        // TODO : handling message (Advise to enclose this block with try/catch

        // Emit rawMessage to peer listener
        this.emitReceiveMessage(rawMessage);

        try {
            // Extract data
            Message msg = new Message(rawMessage);
            // Routing (will be replaced with routing table if there is enough time
            if (msg.type.equals("Server")) {
                // TODO: Login the server (Although connection has been created, but its player
                // data
                // hasn't been set
                if (msg.opt.equals("Login")) {
                    connection.getPlayer()
                            .setName(msg.data);
                } else if (msg.opt.equals("RoomList")) {
                    // TODOL Send RoomList
                }
            } else if (msg.type.equals("Room")) {
                if (msg.opt.equals("Create")) {
                    // TODO: Create Room
                    Room room = this.addRoom(msg.data);
                    connection.connect(room, "Room");
                    room.addConnection(connection);
                    this.idleConnections.remove(connection);
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect(Connection connection) {
        // TODO : remove this connection from idle connection
        this.idleConnections.remove(connection);
    }

    public void emitReceiveMessage(String msg) {
        for (PeerListener p : this.peerListeners)
            p.onReceivedMessage(msg);
    }

    public void emitConnectEvent(Socket socket) {
        for (PeerListener p : this.peerListeners)
            p.onConnected(socket);
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
                    connection.connect(Server.this, "Server");
                    connection.start();

                    emitConnectEvent(clientSocket);
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

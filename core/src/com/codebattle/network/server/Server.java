package com.codebattle.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.codebattle.network.PeerListener;
import com.codebattle.network.dataHandle.DataHandler;
import com.codebattle.network.dataHandle.Message;

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

public class Server implements RoomListener, ConnectionListener {

    // Network IO
    private final ServerSocket serverSocket;

    // Connection management
    // Rooms will store connections (if connection enter the room or create a room
    // Idle connection (no room connections
    private final Map<String, Room> rooms;
    private final Map<String, String> roomsInfo;
    private final List<Connection> idleConnections;

    // Threading
    private final ListenThread listenThread;

    // Message Receiving routing
    // private Map<String, Method> routingTable;

    // Interface with Agent
    private List<PeerListener> peerListeners;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.rooms = new HashMap<String, Room>();
        this.roomsInfo = new HashMap<String, String>();
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
        Room room = new Room(name, this);

        this.rooms.put(name, room);
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
        // handling message (Advise to enclose this block with try/catch
        // Emit rawMessage to peer listener
        this.emitReceiveMessage(rawMessage); // For Testing

        try {
            // Extract data
            Message msg = new Message(rawMessage);
            System.out.println("Extract:");
            System.out.println("Type: " + msg.type);
            System.out.println("Opt: " + msg.opt);
            System.out.println("Data: " + msg.data);

            // Routing (will be replaced with routing table if there is enough time
            if (msg.type.equals("Server")) {
                // Login the server (Although connection has been created, but its player
                // data
                // hasn't been set
                if (msg.opt.equals("Login")) {
                    System.out.println("onReceiveMessage@Server: Player login");
                    connection.setPlayer(msg.data.toString());
                    connection.send(DataHandler.accept("Login Succeed")
                            .toString());
                }
                else if (msg.opt.equals("RoomList")) {
                    System.out.println("onReceiveMessage@Server: Room list request");
                    for (String key : this.roomsInfo.keySet())
                        System.out.println(this.roomsInfo.get(key));
                    connection.send(DataHandler.RoomList(roomsInfo)
                            .toString());
                }
            }
            else if (msg.type.equals("Room")) {
                if (msg.opt.equals("Create")) {
                    Room room = this.addRoom(msg.data.toString()); // Data: Room name
                    rooms.put(room.getName(), room);
                    roomsInfo.put(room.getName(), room.getScene());
                    room.addConnection(connection);
                    connection.send(DataHandler.accept("")
                            .toString());
                    this.idleConnections.remove(connection);
                    System.out.println("onReceiveMessage@Server: " + room.getName()
                            + " created.");
                } else if (msg.opt.equals("Join")) {
                    Room room = this.rooms.get(msg.data.toString());
                    if (room == null) {
                        connection.send(DataHandler.deny("Fail")
                                .toString());
                    }
                    else if (room.isFull()) { // Room is full
                        connection.send(DataHandler.deny("Full")
                                .toString());
                    }
                    else { // Is able to join
                        room.addConnection(connection);
                        connection.send(DataHandler.accept("")
                                .toString());
                        this.idleConnections.remove(connection);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect(Connection connection) {
        this.idleConnections.remove(connection);
        this.emitDisconnectEvent(connection.getSocket());
    }

    // Trigger by Room
    @Override
    public void ChangeScene(String roomName, String sceneName) {
        this.roomsInfo.put(roomName, sceneName);
    }

    @Override
    public void ChangeName(String newRoomName, String oldRoomName) {
        Room room = rooms.get(oldRoomName);
        rooms.put(newRoomName, room);
    }
    
    @Override
	public void addIdleConnection(Connection connection) {
		this.idleConnections.add(connection);
	}

    @Override
    public void destroyRoom(String roomName) {
        this.rooms.remove(roomName);
        this.roomsInfo.remove(roomName);
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
     * @author 1/3
     * @param socket
     */
    public void emitDisconnectEvent(Socket socket) {
        for (PeerListener p : this.peerListeners)
            p.onDisconnected(socket);
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
                    connection.bind(Server.this);
                    connection.start();

                    emitConnectEvent(clientSocket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

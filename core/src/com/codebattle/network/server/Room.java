package com.codebattle.network.server;

/**
 * name: room name(this name will be stored at server's hashmap
 * connection_red: red player
 * connection_blue: blue player
 *
 * Event handling: (Room will handle the following type of message
 *
 * Assign scene
 * Type: Room
 * Opt: Assign
 * Data: <Scene Name>
 *
 * Close Message will make two player exit game but remain in the room
 * Type: Room
 * Opt: Close
 * Data: <Timeout/Disconnect/Exit>
 *
 * @author williamd
 *
 */
public class Room implements ConnectionListener {

    // Connection Management info
    private String name;
    private Connection connection_red;
    private Connection connection_blue;

    // Game Variable
    private String sceneName = "";

    public Room(String name) {
        this.name = name;
    }

    public boolean addConnection(Connection connection) {
        // Add to red
        if (this.connection_red == null) {
            this.connection_red = connection;
            // Add to blue
        } else if (this.connection_blue == null) {
            this.connection_blue = connection;
            // Full
        } else {
            return false;
        }

        return true;
    }

    /**
     * Event Handling
     */
    @Override
    public void onReceiveMessage(Connection connection, String rawMessage) {
        // TODO: routing the message to the connection(e.g. if message is a game script, then the
        // message will be routed to the blue connection

        // Extract data
        Message msg = new Message(rawMessage);
        if (msg.type.equals("Room")) {
            if (msg.opt.equals("Assign")) {
                this.sceneName = msg.data;
            } else if (msg.opt.equals("Close")) {
                // TODO: send close message to client through connection (if data field is "Exit")
                // TODO: remove connection if data is timeout or disconnect
            }
        }
    }

    @Override
    public void onDisconnect(Connection connection) {
        // TODO: this event will tell room which connection is disconnected, and send a close
        // message to another connection
    }

    @Override
    public void onTimeout(Connection connection) {
        // TODO Auto-generated method stub

    }
}

package com.codebattle.network.server;

import com.codebattle.gui.server.models.ClientItem;
import com.codebattle.network.dataHandle.DataHandler;
import com.codebattle.network.dataHandle.Message;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
 * Type: Room
 * Opt: Join
 * Data: <Room Name>
 *
 * @author williamd
 *
 */
public class Room implements ConnectionListener {

    // Connection Management info
    private final String name;
    private final RoomListener server;
    private Connection connection_red;
    private Connection connection_blue;
    private int connectionNum = 0;
    private final int ID;

    // Game Variable
    private final String default_scene = "scene_demo";
    private String scene_name = this.default_scene;
    private boolean playing = false;
    private boolean redSelected = false;
    private boolean blueSelected = false;
    private int selectedPlayer = 0;

    // Counter for ready players
    private final Map<String, Boolean> playersReadyStates;

    public Room(final int ID, final String name, final RoomListener server) {
        this.ID = ID;
        this.name = name;
        this.server = server;
        this.playersReadyStates = new HashMap<String, Boolean>();
        this.playersReadyStates.put("Red", false);
        this.playersReadyStates.put("Blue", false);
    }

    public boolean addConnection(final Connection connection) {
        // Add to red
        if (this.connection_red == null) {
            this.connection_red = connection;
        }
        // Add to blue
        else if (this.connection_blue == null) {
            this.connection_blue = connection;
            // Full
        } else {
            return false;
        }
        connection.bind(this);
        this.connectionNum++;
        this.boardcast(DataHandler.playerstState(this.getPlayerState()));
        return true;
    }

    /**
     * Event Handling
     */
    @Override
    public void onReceiveMessage(final Connection connection, final String rawMessage) {
        // routing the message to the connection(e.g. if message is a game script, then the
        // message will be routed to the blue connection
        final Connection toDestination = this.getDestination(connection);
        // Extract data
        final Message msg = new Message(rawMessage);
        if (msg.type.equals("Room") && this.playing == false) {
            if (msg.opt.equals("Assign")) {
                this.scene_name = msg.data.toString(); // Data: Scene name
                this.emitChangeScene();
            } else if (msg.opt.equals("Leave")) {
                this.removeConnection(connection);
                this.connectionNum--;
                this.updateRoomStatus("Waiting");
                connection.unbindRoom();
                this.server.addIdleConnection(connection);
                if (this.connectionNum == 0) {
                    this.emitDestroyRoom(this.name);
                }

            } else if (msg.opt.equals("RequestPlayerState")) {
                this.broadcastPlayerState();
            } else if (msg.opt.equals("Ready")) {
                this.setReady((String) msg.data);
                this.checkReady();
            }
        } else if (msg.type.equals("Game")) {
            if (msg.opt.equals("Select") && this.playing == false) {
                if (this.getSource(connection) == "Red" && this.redSelected == false) {
                    this.redSelected = true;
                    this.selectedPlayer++;
                } else if (this.getSource(connection) == "Blue" && this.blueSelected == false) {
                    this.blueSelected = true;
                    this.selectedPlayer++;
                } else {
                    connection.send(DataHandler.deny("DuplicateSelect").toString());
                }

                if (this.selectedPlayer == 2) {
                    this.startGame();
                }
            } else if (msg.opt.equals("Team") && this.playing == false) {
                connection.send(DataHandler.getTeam(this.getSource(connection)).toString());
            } else if (this.playing == true) {
                if (msg.opt.equals("Script")) {
                    toDestination.send(rawMessage);
                } else if (msg.opt.equals("Close")) {
                    toDestination.send(rawMessage);
                    this.resetGameVariable();
                    this.updatePlayerStatus(this.connection_red, "Connected");
                    this.updatePlayerStatus(this.connection_blue, "Connected");
                }
            }
        }
    }

    private Connection getDestination(final Connection connection) {
        return connection == this.connection_red ? this.connection_blue
                : this.connection_red;
    }

    private String getSource(final Connection connection) {
        if (connection == this.connection_red) {
            return "Red";
        } else {
            return "Blue";
        }
    }

    private void startGame() {
        this.boardcast(DataHandler.startGame(this.scene_name));
        this.playing = true;
    }

    private void closeGame(final Connection toDestination) { // Only sent when disconnect occurs.
        if (toDestination != null) {
            toDestination.send(DataHandler.closeGame("").toString());
        }
    }

    private void resetGameVariable() {
        this.scene_name = this.default_scene;
        for (final String key : this.playersReadyStates.keySet()) {
            this.playersReadyStates.put(key, false);
        }
        this.redSelected = false;
        this.blueSelected = false;
        this.selectedPlayer = 0;
        this.playing = false;

    }

    private void boardcast(final JSONObject messsage) {
        if (this.connection_red != null) {
            this.connection_red.send(messsage.toString());
        }
        if (this.connection_blue != null) {
            this.connection_blue.send(messsage.toString());
        }
    }

    private void broadcastPlayerState() {
        this.boardcast(DataHandler.playerstState(this.getPlayerState()));
    }

    private void broadcastStartGame() {
        this.boardcast(DataHandler.startGame(this.scene_name));
        this.playing = true;
    }

    private void updateRoomStatus(final String status) {
        final ClientItem clientItem = ((Server) this.server).roomItems.get(this);
        clientItem.setStatus("Player Number: " + this.connectionNum + "  Status: " + status);
        ((Server) this.server).clientListPresenter.updateItem("Rooms", clientItem);
    }

    private void updatePlayerStatus(final Connection connection, final String status) {
        final ClientItem clientItem = ((Server) this.server).connectionItems.get(connection);
        clientItem.setStatus(status);
        ((Server) this.server).clientListPresenter.updateItem("Players", clientItem);
    }

    private void setReady(final String team) {
        if (this.playersReadyStates.containsKey(team)) {
            this.playersReadyStates.put(team, true);
            this.showReadyState();
        }
    }

    private void checkReady() {
        if (this.playersReadyStates.get("Red") && this.playersReadyStates.get("Blue")) {
            this.broadcastStartGame();
            this.updateRoomStatus("Playing");
            this.updatePlayerStatus(this.connection_red, "Playing");
            this.updatePlayerStatus(this.connection_blue, "Playing");
        }
    }

    private void showReadyState() {
        for (final String key : this.playersReadyStates.keySet()) {
            System.out.println(key + " : " + this.playersReadyStates.get(key));
        }
    }

    @Override
    public void onDisconnect(final Connection connection) {
        // this event will tell room which connection is disconnected, and send a close
        // message to another connection
        connection.unbindRoom();
        this.closeGame(this.getDestination(connection));
        this.connectionNum--;
        if (this.connectionNum == 0) {
            this.emitDestroyRoom(this.name);
            ((Server) this.server).clientListPresenter.removeItem("Rooms",
                    ((Server) this.server).roomItems.get(this));
        } else {
            this.resetGameVariable();
            this.updateRoomStatus("Waiting");
            this.updatePlayerStatus(this.getDestination(connection), "Connected");
            ((Server) this.server).clientListPresenter.removeItem("Players",
                    ((Server) this.server).connectionItems.get(connection));
            this.removeConnection(connection);
        }
    }

    private void removeConnection(final Connection connection) {
        if (connection == this.connection_red) {
            this.connection_red = null;
        } else {
            this.connection_blue = null;
        }
        this.broadcastPlayerState();
    }

    private Map<String, String> getPlayerState() {
        final Map<String, String> map = new HashMap<String, String>();
        if (this.connection_red != null) {
            map.put("Red", "Online");
        } else {
            map.put("Red", "Offline");
        }

        if (this.connection_blue != null) {
            map.put("Blue", "Online");
        } else {
            map.put("Blue", "Offline");
        }

        return map;
    }

    public void emitDestroyRoom(final String roomName) {
        this.server.destroyRoom(roomName);
    }

    public void emitChangeScene() {
        this.server.ChangeScene(this.name, this.scene_name);
    }

    public void emitChangeName(final String oldRoomName) {
        this.server.ChangeName(this.name, oldRoomName);
    }

    public String getScene() {
        return this.scene_name;
    }

    public String getName() {
        return this.name;
    }

    public int getPlayerNum() {
        return this.connectionNum;
    }

    public int getID() {
        return this.ID;
    }

    public boolean isFull() {
        return this.connectionNum == 2 ? true : false;
    }
}

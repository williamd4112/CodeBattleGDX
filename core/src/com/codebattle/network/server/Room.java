package com.codebattle.network.server;

import org.json.JSONObject;

import com.codebattle.network.dataHandle.DataHandler;
import com.codebattle.network.dataHandle.Message;

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
    private String name;
    private RoomListener server;
    private Connection connection_red;
    private Connection connection_blue;
    private int connectionNum = 0;

    // Game Variable
    private String sceneName = "Scene01";
    private boolean playing = false;
    private boolean redSelected = false;
    private boolean blueSelected = false;
    private int selectedPlayer = 0;

    public Room(String name, RoomListener server) {
        this.name = name;
        this.server = server;
    }

    public boolean addConnection(Connection connection) {
        // Add to red
        if (this.connection_red == null) {
            this.connection_red = connection;
        }
        // Add to blue
        else if (this.connection_blue == null) {
            this.connection_blue = connection;
            // Full
        }
        else {
            return false;
        }
        connection.bind(this);
        this.connectionNum++;
        return true;
    }

	/**
     * Event Handling
     */
    @Override
    public void onReceiveMessage(Connection connection, String rawMessage) {
        // routing the message to the connection(e.g. if message is a game script, then the
        // message will be routed to the blue connection
    	Connection toDestination = this.getDestination(connection);
        // Extract data
        Message msg = new Message(rawMessage);
        if (msg.type.equals("Room") && this.playing == false) {
        	if (msg.opt.equals("Assign")) {
                this.sceneName = msg.data.toString(); //Data: Scene name
                this.emitChangeScene();
            }
        }
        else if (msg.type.equals("Game")) {
        	if(msg.opt.equals("Select") && this.playing == false){
        		if (this.getSource(connection) == "Red" && this.redSelected == false){
        			this.redSelected = true;
        			this.selectedPlayer++;
        		}
        		else if(this.getSource(connection) == "Blue" && this.blueSelected == false){
        			this.blueSelected = true;
        			this.selectedPlayer++;
        		}
        		else {
        			connection.send(DataHandler.deny("DuplicateSelect").toString());
        		}
        		
        		if(this.selectedPlayer == 2){
        			this.startGame();
        		}
        	}
			if (this.playing == true) {
				if (msg.opt.equals("Script")) {
					toDestination.send(rawMessage);
				} else if (msg.opt.equals("Close")) {
					toDestination.send(rawMessage);
					this.resetGameVariable();
				}
			}
        }
    }
    
    private Connection getDestination(Connection connection){
    	return connection.equals(this.connection_red)? this.connection_blue : this.connection_red;
    }
    
    private String getSource(Connection connection){
    	if (connection == this.connection_red) {
    		return "Red";
    	}
    	else {
    		return "Blue";
    	}
    }
    
    private void startGame() {
		boardcast(DataHandler.startGame(""));
		this.playing = true;
	}
    
    private void closeGame(Connection toDestination) { // Only sent when disconnect occurs.
    	toDestination.send(DataHandler.closeGame("").toString());
    }
    
    private void resetGameVariable(){
    	this.sceneName = "Scene01";
    	this.redSelected = false;
        this.blueSelected = false;
        this.selectedPlayer = 0;
        this.playing = false;
    }
    
    private void boardcast(JSONObject messsage) {
    	connection_red.send(messsage.toString());
    	connection_blue.send(messsage.toString());
    }

    @Override
    public void onDisconnect(Connection connection) {
        // this event will tell room which connection is disconnected, and send a close
        // message to another connection
		this.closeGame(this.getDestination(connection));
		this.removeConnection(connection);
		this.connectionNum--;
		if (this.connectionNum == 0) {
			emitDestroyRoom(this.name);
		}
		else {
			resetGameVariable();
		}
    }
    
    private void removeConnection(Connection connection){
    	if (connection == this.connection_red) {
    		this.connection_red = null;
    	}
    	else {
    		this.connection_blue = null;
    	}
    }
    
    public void emitDestroyRoom(String roomName){
    	this.server.destroyRoom(roomName);
    }
    
    public void emitChangeScene(){
    	this.server.ChangeScene(this.name, this.sceneName);
    }
    
    public void emitChangeName(String oldRoomName){
    	this.server.ChangeName(this.name, oldRoomName);
    }
    
    public String getScene(){
    	return this.sceneName;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public boolean isFull() {
    	return connectionNum == 2? true : false;
    }
}

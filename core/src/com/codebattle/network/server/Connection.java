package com.codebattle.network.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    // EventListener
    private ConnectionListener server = null;
    private ConnectionListener room = null;
    private ConnectionTimer timer = null;

    // Client Player Info
    private Player player;

    public Connection(final Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(
                this.clientSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(
                this.clientSocket.getOutputStream()));
        this.timer = new ConnectionTimer();
    }

    public void bind(ConnectionListener listener) {
        if (listener.getClass().equals(Server.class)) {
            if (this.server == null) {
                this.server = listener;
            }
        }
        else if (listener.getClass().equals(Room.class)) {
            if (this.room == null) {
                this.room = listener;
            }
        }
        else if (listener.getClass().equals(ConnectionTimer.class)){
        	if (this.timer == null) {
                this.timer = (ConnectionTimer)listener;
            }
        }
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
                	//System.out.println(msg);
                    this.emitReceiveMessage(this, msg);
                }
            }
        } catch (final Exception e) {
            //e.printStackTrace();
        } finally{
        	this.emitDisconnect(this);
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
        }
        else {
            this.server.onReceiveMessage(connection, msg);
        }
    }

    public void emitDisconnect(final Connection connection) {
    	System.out.println("Disconnect");
    	if (this.room != null) {
            this.room.onDisconnect(connection);
        }
        else {
            this.server.onDisconnect(connection);
        }
    }

    /**
     * Getters and setters
     */
    public Player getPlayer() {
        return this.player;
    }

    private class ConnectionTimer extends Thread implements ConnectionListener{
    	long interval = 45; // unit: second
		Timer timer = new Timer();
    	
    	public ConnectionTimer() {
    		super();
    		Connection.this.bind(this);
    	}
    	
    	@Override
    	public void run(){
    		timer.schedule(new TimerTask() {

				@Override
				public void run() {
					emitDisconnect(Connection.this);
				}

			}, TimeUnit.SECONDS.toMillis(interval),
					TimeUnit.SECONDS.toMillis(interval));
    	}
    	
		@Override
		public void onReceiveMessage(Connection connection, String msg) {
			this.start();
		}

		@Override
		public void onDisconnect(Connection connection) {}
    	
    }
}

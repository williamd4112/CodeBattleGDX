package com.codebattle.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.codebattle.network.PeerListener;
import com.codebattle.network.dataHandle.DataHandler;

public class Client {
    // private Game game;
    // private GameAgent listener = null;

    // Network IO
    private Socket socket = null;
    private String serverIP;
    private int serverPort;
    private BufferedReader reader;
    private PrintWriter writer;
    private ListenThread listenThread;
    private TimerThread timerThread;

    // Event listeners
    private List<PeerListener> listeners;

    public Client() {
        this.listeners = new LinkedList<PeerListener>();
        this.listenThread = new ListenThread();
        this.timerThread = new TimerThread();
    }

    public void connectToServer(String serverIP, String serverPort) {
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);

        try {
            if (socket == null) {
                this.socket = new Socket(this.serverIP, this.serverPort);
                this.reader = new BufferedReader(new InputStreamReader(
                        this.socket.getInputStream()));
                this.writer = new PrintWriter(new OutputStreamWriter(
                        this.socket.getOutputStream()));
                start();
                this.emitConnectEvent(socket);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        this.listenThread.start();
        this.timerThread.start();
    }

    /**
     * Event handling
     */
    private void emitReceiveMessage(String msg) {
        // this.game.onReceiveMessage(msg);
        for (PeerListener p : this.listeners)
            p.onReceivedMessage(msg);
    }

    private void emitConnectEvent(Socket socket) {
        for (PeerListener p : this.listeners)
            p.onConnected(socket);
    }

    public void addPeerListener(PeerListener listener) {
        this.listeners.add(listener);
    }

    public void send(String msg) {
    	this.writer.println(msg);
		this.writer.flush();
    }

    private class ListenThread extends Thread {
        @Override
        public void run() {
            try {
                String rawMessage = null;
                while (!socket.isClosed()) {
                    rawMessage = Client.this.reader.readLine();
                    emitReceiveMessage(rawMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
	private class TimerThread extends Thread {
		long interval = 30; // unit: second
		Timer timer = new Timer();

		@Override
		public void run() {
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					Client.this.send(DataHandler.report().toString());
				}

			}, TimeUnit.SECONDS.toMillis(interval),
					TimeUnit.SECONDS.toMillis(interval));
		}
	}
}

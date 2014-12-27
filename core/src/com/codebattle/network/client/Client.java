package com.codebattle.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Game game;
    private GameAgent listener = null;
    private Socket socket = null;
    private String serverIP;
    private int serverPort;
    private BufferedReader reader;
    private PrintWriter writer;

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
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException {
        while (true) {
            emitMessageReeceiveEvent(this.reader.readLine());
        }

    }

    private void emitMessageReeceiveEvent(String msg) {
        this.game.MessageReceived(msg);
    }

    public void addGameAgent(GameAgent listener) {
        if (this.listener == null) {
            this.listener = listener;
        }
    }

    public void sendMessage(String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }
}

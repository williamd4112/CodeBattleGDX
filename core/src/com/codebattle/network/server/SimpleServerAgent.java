package com.codebattle.network.server;

import com.codebattle.network.Monitor;
import com.codebattle.network.PeerListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;

public class SimpleServerAgent extends JFrame implements PeerListener {

    final public Server server;
    private final Monitor monitor;
    private final JButton btn_start;

    public SimpleServerAgent() throws IOException {
        super();
        this.server = new Server(8000);
        this.server.addPeerListener(this);

        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.monitor = new Monitor();
        this.monitor.setPreferredSize(new Dimension(600, 500));
        this.btn_start = new JButton("Start");
        this.btn_start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                SimpleServerAgent.this.start();
            }

        });

        this.add(this.monitor);
        this.add(this.btn_start);
        this.setVisible(true);
    }

    @Override
    public void onReceivedMessage(final String msg) {
        this.monitor.printMessage(msg);

    }

    @Override
    public void onConnected(final Socket socket) {
        this.monitor.printMessage("Client from " + socket.getRemoteSocketAddress()
                + " is online");

    }

    public void start() {
        this.server.start();
        this.monitor.printMessage("***Server start listen on port 8000***");
    }

    public static void main(final String[] args) {
        try {
            new SimpleServerAgent();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnected(final Socket socket) {
        this.monitor.printMessage("Client from " + socket.getRemoteSocketAddress()
                + " disconnect.");
    }
}

package com.codebattle.network.client;

import com.codebattle.network.Monitor;
import com.codebattle.network.PeerListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class SimpleGameAgent extends JFrame implements PeerListener {

    final public Client client;
    private final Monitor monitor;
    private final JTextField editText_IP, editText_username;
    private final JButton btn_connect, btn_submit;

    public SimpleGameAgent() {
        super("SimpleGameAgent");
        this.client = new Client();
        this.client.addPeerListener(this);

        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);

        this.monitor = new Monitor();
        this.monitor.setPreferredSize(new Dimension(600, 500));
        this.editText_IP = new JTextField("127.0.0.1");
        this.editText_username = new JTextField("anonymous");

        this.btn_connect = new JButton("Connect");
        this.btn_connect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final String address = SimpleGameAgent.this.editText_IP.getText();
                SimpleGameAgent.this.connect(address);
                SimpleGameAgent.this.monitor.printMessage("***Connected to " + address
                        + "***");
                SimpleGameAgent.this.btn_connect.setEnabled(false);
                SimpleGameAgent.this.btn_submit.setEnabled(true);
            }

        });

        this.btn_submit = new JButton("Submit");
        this.btn_submit.setEnabled(false);
        this.btn_submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                SimpleGameAgent.this.sendMessage(SimpleGameAgent.this.monitor.getWriterMessage());
                System.out.println("Client sending");
            }

        });

        this.add(this.monitor);
        this.add(this.editText_IP);
        this.add(this.editText_username);
        this.add(this.btn_connect);
        this.add(this.btn_submit);

        this.setVisible(true);
    }

    @Override
    public void onReceivedMessage(final String rawMessage) {
        try {
            System.out.println("onReceiveMessage@Client: " + rawMessage);
            // Message msg = new Message(rawMessage);
            this.monitor.printMessage(rawMessage);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(final Socket socket) {
        this.monitor.printMessage("***Server at " + socket.getRemoteSocketAddress()
                + " connected***");
    }

    @Override
    public void onDisconnected(final Socket socket) {
        // TODO Auto-generated method stub

    }

    public void connect(final String address) {
        this.client.connectToServer(address, "8000");
    }

    public void sendMessage(final String msg) {
        this.client.send(msg);
    }

    public static void main(final String[] args) {
        new SimpleGameAgent();
    }

}

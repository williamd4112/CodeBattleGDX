package com.codebattle.network;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Monitor extends JPanel {
    private JTextArea readerBoard, writerBoard;

    public Monitor() {
        super();
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.readerBoard = new JTextArea();
        this.readerBoard.setPreferredSize(new Dimension(600, 200));
        this.readerBoard.setEditable(false);

        this.writerBoard = new JTextArea();
        this.writerBoard.setPreferredSize(new Dimension(600, 200));

        JScrollPane pane = new JScrollPane(this.readerBoard);
        pane.setPreferredSize(new Dimension(600, 200));

        this.add(new JLabel("Received"));
        this.add(pane);
        this.add(new JLabel("Send"));
        this.add(this.writerBoard);
    }

    public void printMessage(String msg) {
        this.readerBoard.append(msg + "\n");
    }

    public String getWriterMessage() {
        return this.writerBoard.getText();
    }
}

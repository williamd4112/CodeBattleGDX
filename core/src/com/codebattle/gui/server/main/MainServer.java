package com.codebattle.gui.server.main;

import com.codebattle.gui.server.views.ServerView;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class MainServer {
    /**
     * Program start entry.
     *
     * @param args  Arguments
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");

                    new ServerView();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

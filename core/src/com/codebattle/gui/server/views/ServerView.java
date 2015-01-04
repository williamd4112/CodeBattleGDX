package com.codebattle.gui.server.views;

import com.codebattle.gui.server.presenters.ServerPresenter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class ServerView {
    private JFrame frmCodeBattleServer;

    private ServerPresenter presenter = new ServerPresenter(this);

    private JTabbedPane mainTabbedPane;
    private JPanel playersPanel;
    private JPanel roomsPanel;
    private JMenuItem mntmStartServer;
    private JMenuItem mntmCloseServer;

    /**
     * Create the application.
     */
    public ServerView() {
        this.initialize();
        this.initializeCustomView();

        this.frmCodeBattleServer.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.frmCodeBattleServer = new JFrame();
        this.frmCodeBattleServer.setTitle("Code Battle Server");
        this.frmCodeBattleServer.setBounds(100, 100, 500, 380);
        this.frmCodeBattleServer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.mainTabbedPane = new JTabbedPane(SwingConstants.TOP);
        this.frmCodeBattleServer.getContentPane().add(this.mainTabbedPane,
                BorderLayout.CENTER);

        final JPanel summaryTab = new JPanel();
        this.mainTabbedPane.addTab("Summary", null, summaryTab, null);
        final GridBagLayout gbl_summaryTab = new GridBagLayout();
        gbl_summaryTab.columnWidths = new int[] { 0 };
        gbl_summaryTab.rowHeights = new int[] { 0 };
        gbl_summaryTab.columnWeights = new double[] { Double.MIN_VALUE };
        gbl_summaryTab.rowWeights = new double[] { Double.MIN_VALUE };
        summaryTab.setLayout(gbl_summaryTab);

        final JPanel clientsTab = new JPanel();
        this.mainTabbedPane.addTab("Clients", null, clientsTab, null);
        clientsTab.setLayout(new BorderLayout(0, 0));

        final JTabbedPane clientsTabbedPane = new JTabbedPane(SwingConstants.TOP);
        clientsTab.add(clientsTabbedPane);

        final JPanel statsTab = new JPanel();
        this.mainTabbedPane.addTab("Statistics", null, statsTab, null);

        final JPanel consoleTab = new JPanel();
        this.mainTabbedPane.addTab("Console", null, consoleTab, null);

        final JPanel settingsTab = new JPanel();
        this.mainTabbedPane.addTab("Settings", null, settingsTab, null);

        this.playersPanel = new JPanel();
        this.playersPanel.setLayout(new BorderLayout(0, 0));

        final JScrollPane playersScrollPane = new JScrollPane(this.playersPanel);
        clientsTabbedPane.addTab("Players", null, playersScrollPane, null);

        roomsPanel = new JPanel();
        roomsPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane roomsScrollPane = new JScrollPane(roomsPanel);
        clientsTabbedPane.addTab("Rooms", null, roomsScrollPane, null);

        JMenuBar menuBar = new JMenuBar();
        frmCodeBattleServer.setJMenuBar(menuBar);

        JMenu mnServer = new JMenu("Server");
        mnServer.setMnemonic('S');
        menuBar.add(mnServer);

        mntmStartServer = new JMenuItem("Start Server");
        mntmStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (presenter.isServerReady()) {
                    presenter.startServer();

                    getMntmStartServer().setEnabled(false);
                    getMntmCloseServer().setEnabled(true);
                }
            }
        });
        mnServer.add(mntmStartServer);

        mntmCloseServer = new JMenuItem("Close Server");
        mntmCloseServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (presenter.isServerReady()) {
                    presenter.closeServer();

                    getMntmStartServer().setEnabled(true);
                    getMntmCloseServer().setEnabled(false);
                }
            }
        });
        mnServer.add(mntmCloseServer);

        JSeparator separator = new JSeparator();
        mnServer.add(separator);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                showConfirmExit();
            }
        });
        mnServer.add(mntmExit);
    }

    private void initializeCustomView() {
        this.frmCodeBattleServer.setLocationRelativeTo(null);
        this.frmCodeBattleServer.setMinimumSize(new Dimension(400, 300));
        frmCodeBattleServer.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                showConfirmExit();
            }
        });

        this.getMntmStartServer().setEnabled(true);
        this.getMntmCloseServer().setEnabled(false);

        this.getMainTabbedPane().setSelectedIndex(1);

        this.getPlayersPanel().add(new ClientListView());
        this.getRoomsPanel().add(new ClientListView());
    }

    private void showConfirmExit() {
        if (this.presenter.isServerRunning()) {
            if (JOptionPane.showConfirmDialog(null, "Exit Code Battle server?",
                    "Confirm exit", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                presenter.dispose();
                System.exit(0);
            }
        } else {
            presenter.dispose();
            System.exit(0);
        }
    }

    protected JTabbedPane getMainTabbedPane() {
        return this.mainTabbedPane;
    }

    protected JPanel getPlayersPanel() {
        return this.playersPanel;
    }

    protected JPanel getRoomsPanel() {
        return roomsPanel;
    }

    protected JMenuItem getMntmStartServer() {
        return mntmStartServer;
    }

    protected JMenuItem getMntmCloseServer() {
        return mntmCloseServer;
    }
}

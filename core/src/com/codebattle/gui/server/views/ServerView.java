package com.codebattle.gui.server.views;

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
import javax.swing.WindowConstants;

import com.codebattle.gui.server.presenters.PresenterFactory;
import com.codebattle.gui.server.presenters.ServerPresenter;

public class ServerView implements View {
    private JFrame frmCodeBattleServer;

    private final PresenterFactory presenterFactory;
    private ServerPresenter presenter;

    private JTabbedPane mainTabbedPane;
    private JPanel playersPanel;
    private JPanel roomsPanel;
    private JMenuItem mntmStartServer;
    private JMenuItem mntmCloseServer;

    /**
     * Create the application.
     */
    public ServerView(final PresenterFactory presenterFactory) {
        this.presenterFactory = presenterFactory;
        this.initializePresenter();

        this.initialize();
        this.initializeCustomView();

        this.frmCodeBattleServer.setVisible(true);
    }

    private void initializePresenter() {
        this.presenter = (ServerPresenter) this.presenterFactory.getPresenter(
                ServerPresenter.class, this);
        this.presenter.setPresenterFactory(this.presenterFactory);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.frmCodeBattleServer = new JFrame();
        this.frmCodeBattleServer.setTitle("Code Battle Server");
        this.frmCodeBattleServer.setBounds(100, 100, 500, 380);
        this.frmCodeBattleServer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

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

        this.roomsPanel = new JPanel();
        this.roomsPanel.setLayout(new BorderLayout(0, 0));

        final JScrollPane roomsScrollPane = new JScrollPane(this.roomsPanel);
        clientsTabbedPane.addTab("Rooms", null, roomsScrollPane, null);

        final JMenuBar menuBar = new JMenuBar();
        this.frmCodeBattleServer.setJMenuBar(menuBar);

        final JMenu mnServer = new JMenu("Server");
        mnServer.setMnemonic('S');
        menuBar.add(mnServer);

        this.mntmStartServer = new JMenuItem("Start Server");
        this.mntmStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (ServerView.this.getPresenter().isServerReady()) {
                    ServerView.this.getPresenter().startServer();

                    ServerView.this.getMntmStartServer().setEnabled(false);
                    ServerView.this.getMntmCloseServer().setEnabled(true);
                }
            }
        });
        mnServer.add(this.mntmStartServer);

        this.mntmCloseServer = new JMenuItem("Close Server");
        this.mntmCloseServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (ServerView.this.getPresenter().isServerReady()) {
                    ServerView.this.getPresenter().closeServer();

                    ServerView.this.getMntmStartServer().setEnabled(true);
                    ServerView.this.getMntmCloseServer().setEnabled(false);
                }
            }
        });
        mnServer.add(this.mntmCloseServer);

        final JSeparator separator = new JSeparator();
        mnServer.add(separator);

        final JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                ServerView.this.showConfirmExit();
            }
        });
        mnServer.add(mntmExit);
    }

    private void initializeCustomView() {
        this.frmCodeBattleServer.setLocationRelativeTo(null);
        this.frmCodeBattleServer.setMinimumSize(new Dimension(400, 300));
        this.frmCodeBattleServer.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(final java.awt.event.WindowEvent windowEvent) {
                ServerView.this.showConfirmExit();
            }
        });

        this.getMntmStartServer().setEnabled(true);
        this.getMntmCloseServer().setEnabled(false);

        this.getMainTabbedPane().setSelectedIndex(1);

        // Add client list views
        this.getPlayersPanel().add(new ClientListView("Players", this.presenterFactory));
        this.getRoomsPanel().add(new ClientListView("Rooms", this.presenterFactory));
    }

    private void showConfirmExit() {
        if (this.getPresenter().isServerRunning()) {
            if (JOptionPane.showConfirmDialog(null, "Exit Code Battle server?",
                    "Confirm exit", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                this.getPresenter().dispose();
                System.exit(0);
            }
        } else {
            this.getPresenter().dispose();
            System.exit(0);
        }
    }

    private ServerPresenter getPresenter() {
        return this.presenter;
    }

    protected JTabbedPane getMainTabbedPane() {
        return this.mainTabbedPane;
    }

    protected JPanel getPlayersPanel() {
        return this.playersPanel;
    }

    protected JPanel getRoomsPanel() {
        return this.roomsPanel;
    }

    protected JMenuItem getMntmStartServer() {
        return this.mntmStartServer;
    }

    protected JMenuItem getMntmCloseServer() {
        return this.mntmCloseServer;
    }
}

package com.codebattle.gui.server.views;

import com.codebattle.gui.server.models.ClientItem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

public class ClientItemView extends JPanel {
    private static final long serialVersionUID = 1304788367225793192L;

    private static final String PATH_ICON_ACTIONS =
            "/icons/navigation/ic_menu_white_18dp.png";

    private static final Dimension iconSize = new Dimension(27, 27);

    private JPanel parent;

    private ClientItem clientItem;

    private final JLabel title;
    private final JLabel status;
    private final JButton actions;
    private final JPopupMenu popupMenu;

    /**
     * Create a client item view.
     *
     * @param clientItem    Client item model.
     */
    public ClientItemView(final JPanel parent, final ClientItem clientItem) {
        this();
        this.parent = parent;
        this.clientItem = clientItem;
        this.update();
    }

    /**
     * Create the panel.
     */
    public ClientItemView() {
        final JPopupMenu popupMenu = new JPopupMenu();
        addPopup(this, popupMenu);

        final JMenuItem kickClient = new JMenuItem("Kick");
        popupMenu.add(kickClient);
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 10, 0, 10, 0 };
        gridBagLayout.rowHeights = new int[] { 2, 0, 2, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        this.setLayout(gridBagLayout);

        final JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(192, 192, 192), 2));
        final GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 1;
        this.add(panel, gbc_panel);
        final GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        final JLabel title = new JLabel("{Title}");
        final GridBagConstraints gbc_title = new GridBagConstraints();
        gbc_title.anchor = GridBagConstraints.WEST;
        gbc_title.insets = new Insets(0, 0, 5, 5);
        gbc_title.gridx = 0;
        gbc_title.gridy = 0;
        panel.add(title, gbc_title);
        title.setFont(new Font("Tahoma", Font.PLAIN, 16));

        final JButton actions = new JButton("");
        final GridBagConstraints gbc_actions = new GridBagConstraints();
        gbc_actions.insets = new Insets(0, 0, 5, 0);
        gbc_actions.gridx = 1;
        gbc_actions.gridy = 0;
        panel.add(actions, gbc_actions);

        final JLabel status = new JLabel("{Status}");
        final GridBagConstraints gbc_status = new GridBagConstraints();
        gbc_status.anchor = GridBagConstraints.WEST;
        gbc_status.insets = new Insets(0, 0, 0, 5);
        gbc_status.gridx = 0;
        gbc_status.gridy = 1;
        panel.add(status, gbc_status);

        /*
         * Exports
         */
        this.title = title;
        this.status = status;
        this.actions = actions;
        this.popupMenu = popupMenu;

        /*
         * Custom
         */
        this.initializeCustomView();
        this.initializeCustomActions();
    }

    /**
     * Update view.
     */
    public void update() {
        this.title.setText(this.clientItem.getTitle());
        this.status.setText(this.clientItem.getStatus());

        this.revalidate();
        this.parent.revalidate();
    }

    private void initializeCustomView() {
        this.actions.setPreferredSize(iconSize);
        this.actions.setIcon(new ImageIcon(this.getClass().getResource(PATH_ICON_ACTIONS)));
    }

    private void initializeCustomActions() {
        this.actions.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent event) {
                ClientItemView.this.popupMenu.show(event.getComponent(), event.getX(),
                        event.getY());
            }
        });
    }

    private static void addPopup(final Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    this.showMenu(event);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    this.showMenu(event);
                }
            }

            private void showMenu(final MouseEvent event) {
                popup.show(event.getComponent(), event.getX(), event.getY());
            }
        });
    }
}

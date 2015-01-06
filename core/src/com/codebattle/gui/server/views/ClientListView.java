package com.codebattle.gui.server.views;

import com.codebattle.gui.server.presenters.ClientListPresenter;
import com.codebattle.gui.server.presenters.PresenterFactory;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class ClientListView extends JPanel implements View {
    private static final long serialVersionUID = 475731162060924131L;

    private final String role;
    private final PresenterFactory presenterFactory;
    private ClientListPresenter presenter;

    private final Map<Object, Component> components =
            new HashMap<Object, Component>();

    private final JPanel dummyPanel = new JPanel();

    /**
     * Create the panel.
     */
    public ClientListView(final String role, final PresenterFactory presenterFactory) {
        this.role = role;
        this.presenterFactory = presenterFactory;
        this.initializePresenter();

        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0 };
        gridBagLayout.rowHeights = new int[] { 0 };
        gridBagLayout.columnWeights = new double[] { Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { Double.MIN_VALUE };
        this.setLayout(gridBagLayout);
    }

    /**
     * Add new item.
     *
     * @param item          Item as key
     * @param component     Component to be added
     */
    public void addItem(final Object item, final Component component) {
        this.components.put(item, component);

        final GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = this.components.size() - 1;
        constraints.weighty = 0;

        this.add(component, constraints);

        this.resetDummyPanel();
        this.revalidate();
    }

    public Component getItem(final Object item) {
        return this.components.get(item);
    }

    /**
     * Remove a item.
     *
     * @param item  Item as key
     */
    public void removeItem(final Object item) {
        final Component component = this.components.get(item);

        this.components.remove(component);
        this.remove(component);

        this.updateConstraints();

        this.resetDummyPanel();
        this.revalidate();
    }

    /**
     * Clear all components.
     */
    public void clearAllItems() {
        this.components.clear();
        this.removeAll();

        this.resetDummyPanel();
        this.revalidate();
        this.repaint();
    }

    private void initializePresenter() {
        this.presenter =
                (ClientListPresenter) this.presenterFactory.getPresenter(
                        ClientListPresenter.class, this);
        this.presenter.setView(this.role, this);
    }

    private void updateConstraints() {
        final GridBagLayout gridBagLayout = (GridBagLayout) this.getLayout();
        int gridy = 0;

        for (final Component component : this.components.values()) {
            final GridBagConstraints constraints = gridBagLayout.getConstraints(component);

            constraints.gridy = gridy;

            gridBagLayout.setConstraints(component, constraints);

            gridy += 1;
        }
    }

    private void resetDummyPanel() {
        final GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = this.components.size();
        constraints.weighty = 1;

        this.remove(this.dummyPanel);
        this.add(this.dummyPanel, constraints);
    }
}
